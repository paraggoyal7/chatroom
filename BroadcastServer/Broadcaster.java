package BroadcastServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import utils.Address;

public class Broadcaster {

  static Map<String, Address> clients = new HashMap<>(); // Map to store client addresses and ports>

  public static void main(String[] args) throws Exception {
    DatagramSocket socket = new DatagramSocket();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        socket.close();
        System.out.println("Graceful shutdown.");
      } catch (Exception e) {
        // ignore if socket already closed
      }
    }));

    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter Chat room name: ");
    String chatRoom = scanner.nextLine();

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
      try {
        InetAddress broadcast = InetAddress.getByName("255.255.255.255");
        int discoveryPort = 8888;
        while (true) {
          String announce = "CHATROOM:"+chatRoom;
          byte[] data = announce.getBytes();
          DatagramPacket packet = new DatagramPacket(data, data.length, broadcast, discoveryPort);
          socket.send(packet);
          Thread.sleep(2000);
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
