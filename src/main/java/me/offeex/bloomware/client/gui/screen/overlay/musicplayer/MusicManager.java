package me.offeex.bloomware.client.gui.screen.overlay.musicplayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class MusicManager {
    private final ArrayList<File> tracks = new ArrayList<>();
    private final Random random = new Random();
    private File currentTrack;
    private PlaybackMode mode = PlaybackMode.NEXT;
    private SourceDataLine line;

    public void loadFolder(File path) {
        tracks.clear();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) tracks.add(file);
        }
    }

    public void play(String filePath) {
        final File file = new File(filePath);
        try (final AudioInputStream in = getAudioInputStream(file)) {
            if (currentTrack != null) line.close();
            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            if (line != null) {
                line.open(outFormat);
                currentTrack = file;
                line.start();
                stream(getAudioInputStream(outFormat, in), line);
                line.drain();
                line.close();
                new Thread(this::selectAndPlayNext).start();
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line) throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }

    public ArrayList<File> getTracks() {
        return tracks;
    }

    public File getCurrentTrack() {
        return currentTrack;
    }

    public void playNext() {
        if (currentTrack != null) {
            line.stop();
            line.close();
            int index = getIndex(currentTrack.getAbsolutePath());
            play(index == tracks.size() - 1 || index == -1 ? tracks.get(0).getPath() : tracks.get(++index).getPath());
        }
    }

    public void selectAndPlayNext() {
        switch (mode) {
            case NEXT -> playNext();
            case REPEAT -> {
                try {
                    play(currentTrack.getPath());
                } catch (Exception ignored) {}
            }
            case RANDOM -> play(tracks.get(random.nextInt(0, tracks.size() - 1)).getPath());
        }
    }

    public void playPrevious() {
        if (currentTrack != null) {
            line.stop();
            line.close();
            int index = getIndex(currentTrack.getAbsolutePath());
            switch (index) {
                case -1 -> play(tracks.get(0).getPath());
                case 0 -> play(tracks.get(tracks.size() - 1).getPath());
                default -> play(tracks.get(--index).getPath());
            }
        }
    }

    private int getIndex(String path) {
        for (File file : tracks) {
            if (file.getAbsolutePath().equals(path)) return tracks.indexOf(file);
        }
        return -1;
    }

    public void stopPlaying() {
        if (currentTrack != null) {
            line.stop();
            line.close();
        }
        currentTrack = null;
    }

    public long getSecondsElapsed() {
        return currentTrack != null ? line.getMicrosecondPosition() / 1000000 : 0;
    }

    public enum PlaybackMode {
        REPEAT(0), NEXT(1), RANDOM(2);

        private final int id;

        PlaybackMode(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

    public PlaybackMode getMode() {
        return mode;
    }

    public void setMode(int id) {
        mode = PlaybackMode.values()[id];
    }
}
