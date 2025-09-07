package ManualServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import utils.UdpUtil;

public class UDPChatClient_4 {
  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket(8003); // client listens on its own port
    InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
    int serverPort = 9999;
    String username = "Client4";

    UdpUtil.connectToServer(socket, username, serverAddress, serverPort);
    System.out.println("Connected to UDP Chat Server at 127.0.0.1:9999");

    // Receiver Thread (listens for messages from server)
    Thread receiver = new Thread(() -> {
      try {
        byte[] buffer = new byte[1024];
        while (true) {
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String message = new String(packet.getData(), 0, packet.getLength());
          UdpUtil.printMessage(message);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // Sender Thread (sends messages to server)
    Thread sender = new Thread(() -> {
      try (Scanner scanner = new Scanner(System.in)) {
        while (!socket.isClosed()) {
          UdpUtil.sendThread(socket, username, serverAddress, serverPort, scanner);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    receiver.start();
    sender.start();
  }
}
