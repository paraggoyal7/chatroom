package BroadcastServer.clientLauncher;

import java.net.InetAddress;

public interface ClientLauncher {
    public Boolean launchClient(String username, InetAddress serverAddress, int serverPort);
}
