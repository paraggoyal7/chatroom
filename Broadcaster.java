import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Broadcaster {

    public static void main(String[] args) throws Exception {
      DatagramSocket socket = new DatagramSocket();
      InetAddress broadcast = InetAddress.getByName("255.255.255.255");
      int discoveryPort = 8888;

      while (true) {
        String announce = "CHATROOM:MyRoom,IP:192.168.1.25,PORT:9999";
        byte[] data = announce.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, discoveryPort);
        socket.send(packet);
        Thread.sleep(2000);
      }
    }

}
