import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Listener {
  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket(8888);
    byte[] buffer = new byte[1024];
    System.out.println("Listening for chatrooms...");
    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet);
      String msg = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Found: " + msg + " from " + packet.getAddress() + ":" + packet.getPort());
    }

  }
}
