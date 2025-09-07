package BroadcastServer.launcher;

import java.net.InetAddress;

public interface Launcher {
    void buildProcess(String className, String[] args);
}
