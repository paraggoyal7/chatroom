import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket();
    InetAddress serverAddress = InetAddress.getByName("192.168.1.4");

    String message = "Hello UDP Server!";
    byte[] sendData = message.getBytes();

    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9999);
    socket.send(sendPacket);

    // Receive response
    byte[] buffer = new byte[1024];
    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
    socket.receive(receivePacket);

    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
    System.out.println("Response from server: " + response);

    socket.close();
  }
}
