package BroadcastServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import utils.UdpUtil;

public class Client {

  static String username;
  static InetAddress serverAddress;
  static int serverPort;

  //public Client(String username, InetAddress serverAddress, int serverPort) {
  //  this.username = username;
  //  this.serverAddress = serverAddress;
  //  this.serverPort = serverPort;
  //}

  public static void main(String[] args) throws Exception {
    username = args[0];
    serverAddress = InetAddress.getByName(args[1]);
    serverPort = Integer.parseInt(args[2]);
    Client client = new Client();
    client.startClient(new Scanner(System.in));
  }

  public void startClient(Scanner scanner) throws Exception {
    DatagramSocket socket = new DatagramSocket(); // client listens on its own port

    UdpUtil.connectToServer(socket, username, serverAddress, serverPort);
    System.out.println("Connected to UDP Chat Server at "+serverAddress.getHostAddress()+":"+serverPort);

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
      try (scanner) {
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

  public static void comp(){}
}
