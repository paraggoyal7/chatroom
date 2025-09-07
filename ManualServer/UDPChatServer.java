package ManualServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import utils.Address;

public class UDPChatServer {
  static Map<String, Address> clients = new HashMap<>(); // Map to store client addresses and ports>

  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket(9999);
    System.out.println("UDP Chat Server started on port 9999...");


    // Receiver Thread (listens for messages from client)
    Thread receiver = new Thread(() -> {
      try {
        byte[] buffer = new byte[1024];
        while (true) {
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String message = new String(packet.getData(), 0, packet.getLength());
          String[] parts = message.split("###");
          String username = parts[0];
          String messageText = parts[1];
          InetAddress clientAddress = packet.getAddress();
          int clientPort = packet.getPort();
          System.out.println("\n"+ username + ": " + messageText);
          if(checkConnectMsg(messageText)) {
            addClient(username, clientAddress, clientPort);
            continue;
          }
          else if (checkDisconnectMsg(messageText)) {
            clients.remove(username);
            continue;
          }
          for (Map.Entry<String, Address> entry : clients.entrySet()) {
            String clientUsername = entry.getKey();
            if (clientUsername.equals(username)) {
              continue;
            }
            Address cntAddress = entry.getValue();
            byte[] data = message.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(data, data.length, cntAddress.address, cntAddress.port);
            socket.send(responsePacket);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    // Sender Thread (sends messages to client)
    Thread sender = new Thread(() -> {
      try (Scanner scanner = new Scanner(System.in)) {
        InetAddress clientAddress = InetAddress.getByName("127.0.0.1"); // adjust if needed
        int clientPort = 8888; // client’s listening port
        while (true) {
          String msg = scanner.nextLine();
          byte[] data = msg.getBytes();
          DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);
          socket.send(packet);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    receiver.start();
    sender.start();
  }

  public static void addClient(String username, InetAddress address, int port) {
    clients.put(username, new Address(address, port));
  }

  public static boolean checkConnectMsg(String msg) {
    return msg.equals("connect");
  }

  public static boolean checkDisconnectMsg(String msg) {
    return msg.equals("disconnect");
  }
}
