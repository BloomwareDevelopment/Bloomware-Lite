package me.offeex.bloomware.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerUtil {
    public static final String UUID_REQUEST_URL = "https://api.mojang.com/user/profiles/%s/names";
    private static final Gson gson = new Gson();

    public static String getNicknameByUUID(UUID uuid) {
        try {
            String data = readURL(new URL(String.format(UUID_REQUEST_URL, uuid.toString())));
            if (data != null) {
                JsonArray array = gson.fromJson(data, JsonArray.class);
                if (array == null) return "unknown";
                JsonObject object = array.get(array.size() - 1).getAsJsonObject();
                return object.get("name").getAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getNicknamesByUUID(UUID uuid) {
        try {
            String data = readURL(new URL(String.format(UUID_REQUEST_URL, uuid.toString())));
            final ArrayList<String> a = new ArrayList<>();
            if (data != null) {
                JsonArray array = gson.fromJson(data, JsonArray.class);
                array.forEach(object -> a.add(object.getAsJsonObject().get("name").getAsString()));
                return a;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10 * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        for (String line; (line = input.readLine()) != null; ) {
            buffer.append(line);
            buffer.append("\n");
        }
        input.close();
        return buffer.toString();
    }
}
