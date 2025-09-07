import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket(9999); // Bind to port 9999
    byte[] buffer = new byte[1024];

    System.out.println("UDP Server is listening on port 9999...");

    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet); // Wait for data

      String message = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Received: " + message + " from " + packet.getAddress() + ":" + packet.getPort());

      // Send response back
      String response = "Hello from UDP Server";
      byte[] responseData = response.getBytes();
      DatagramPacket responsePacket = new DatagramPacket(
          responseData, responseData.length, packet.getAddress(), packet.getPort()
      );
      socket.send(responsePacket);
    }
  }
}
