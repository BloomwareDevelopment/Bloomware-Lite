package me.offeex.bloomware.api.proxy;

public record Proxy(String ipPort, String username, String password, ProxyType type) {
    public int getPort() {
        return Integer.parseInt(ipPort.split(":")[1]);
    }

    public String getIp() {
        return ipPort.split(":")[0];
    }

    public enum ProxyType {
        SOCKS4,
        SOCKS5
    }
}
