package me.offeex.bloomware.api.proxy;

import com.google.gson.*;
import me.offeex.bloomware.Bloomware;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class ProxyManager {
    private final ArrayList<Proxy> proxies = new ArrayList<>();
    private final File directory = Bloomware.fileManager.getProxyDirectory();
    private final JsonArray array = new JsonArray();
    private final Gson gson = new Gson();
    private Proxy currentProxy = null;

    public void addProxy(Proxy proxy) {
        proxies.add(proxy);
    }

    public Proxy getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(Proxy proxy) {
        currentProxy = proxy;
    }

    public void removeProxy(Proxy proxy) {
        proxies.remove(proxy);
    }

    public void saveData() {
        proxies.forEach(proxy -> array.add(convertToJson(proxy)));
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(directory.toPath())))) {
            writer.write(gson.toJson(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            String content = Bloomware.fileManager.loadFileContent(directory.toPath());
            if (!Objects.equals(content, "")) {
                JsonArray jsonArray = JsonParser.parseString(content).getAsJsonArray();
                jsonArray.forEach(jsonElement -> {
                    final String[] data = new String[5];
                    int i = 0;
                    for (Map.Entry entry : ((JsonObject) jsonElement).entrySet()) {
                        data[i] = entry.getValue().toString();
                        i++;
                    }
                    proxies.add(new Proxy(data[0] + ":" + data[1], data[2], data[3], Objects.equals(data[4], "SOCKS4") ? Proxy.ProxyType.SOCKS4 : Proxy.ProxyType.SOCKS5));
                });
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    private JsonObject convertToJson(Proxy proxy) {
        JsonObject object = new JsonObject();
        object.add("ip", new JsonPrimitive(proxy.getIp()));
        object.add("port", new JsonPrimitive(proxy.getPort()));
        object.add("username", new JsonPrimitive(proxy.username()));
        object.add("password", new JsonPrimitive(proxy.type() == Proxy.ProxyType.SOCKS4 ? "" : proxy.password()));
        object.add("type", new JsonPrimitive(proxy.type() == Proxy.ProxyType.SOCKS4 ? "SOCKS4" : "SOCKS5"));
        return object;
    }
}
