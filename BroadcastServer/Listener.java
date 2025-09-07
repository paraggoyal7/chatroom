package BroadcastServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import BroadcastServer.launcher.Launcher;
import BroadcastServer.launcher.LauncherFactory;
import utils.Address;
import utils.Launchers;

public class Listener {

  static String username;
  static InetAddress serverAddress;
  static int serverPort;

  static Map<String, Address> rooms = new ConcurrentHashMap<>();
  static List<String> roomNames = new ArrayList<>();

  private static final int DISCOVERY_PORT = 8888;

  private static volatile boolean running = true;


  public static void main(String[] args) throws Exception {
    Client.comp();
    DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT);
    byte[] buffer = new byte[1024];

    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter Username: ");
    username = scanner.nextLine();

    System.out.println("Listening for chat rooms...");

    Thread chatRoomListener = new Thread(() -> {
      while (running) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
          socket.receive(packet);
          String msg = new String(packet.getData(), 0, packet.getLength());
          if(!rooms.containsKey(msg)) {
            Address address = new Address(packet.getAddress(), packet.getPort());
            rooms.put(msg, address);
            roomNames.add(msg);
            printMenu();
          }
        }
        catch (SocketException e) {
          if (running) e.printStackTrace();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    chatRoomListener.start();

    int roomSelected = scanner.nextInt();
    String room = roomNames.get(roomSelected - 1);
    Address address = rooms.get(room);

    scanner.close();
    serverAddress = address.address;
    serverPort = address.port;

    running = false;
    socket.close();
    if(chatRoomListener.isAlive()) {
      chatRoomListener.interrupt();
    }

    Launchers.launchClient(username, serverAddress, serverPort);
  }

  private static void printMenu() {
    // Clear screen
    System.out.print("\033[H\033[2J");
    System.out.flush();

    System.out.println("Discovered Chatrooms:");
    int i = 1;
    for (String room : roomNames) {
      System.out.println("[" + i + "] " + room);
      i++;
    }
    System.out.println("\nEnter a number to join: ");
  }
}
