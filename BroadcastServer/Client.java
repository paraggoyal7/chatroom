package BroadcastServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import utils.UdpUtil;

public class Client {

  static String username;
  static InetAddress serverAddress;
  static int serverPort;

  static volatile boolean isClientActive = false;

  private static final int DISCOVERY_PORT = 8888;

  static AtomicLong lastPingTime = new AtomicLong(0);

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

    isClientActive = true;
    lastPingTime.set(System.currentTimeMillis());

    // Receiver Thread (listens for messages from server)
    Thread receiver = new Thread(() -> {
      try {
        byte[] buffer = new byte[1024];
        while (isClientActive) {
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String message = new String(packet.getData(), 0, packet.getLength());
          UdpUtil.printMessage(message);
        }
      } catch (Exception e) {
        if(isClientActive) e.printStackTrace();
      }
    });

    // Sender Thread (sends messages to server)
    Thread sender = new Thread(() -> {
      try (scanner) {
        while (isClientActive && !Thread.currentThread().isInterrupted()) {
          if (scanner.hasNextLine()) {   // <-- non-blocking check
            String msg = scanner.nextLine();
            System.out.println("You: " + msg);
            UdpUtil.sendMessage(socket, username, serverAddress, serverPort, msg);
          } else {
            Thread.sleep(100); // don’t burn CPU
          }
        }
      } catch (Exception e) {
        if(isClientActive) e.printStackTrace();
      }
    });

    DatagramSocket pingSocket = new DatagramSocket(DISCOVERY_PORT);

    Thread clientPingChecker = new Thread(() -> {
      try {

        pingSocket.setSoTimeout(5000);
        byte[] buffer = new byte[1024];
        while (isClientActive){
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          pingSocket.receive(packet);
          if (isMyClient(packet)) {
            lastPingTime.set(System.currentTimeMillis());
          }
          else if (System.currentTimeMillis() - lastPingTime.get() > 5000) {
            closeClient(socket, pingSocket, sender, scanner);
            break;
          }
          else {
            continue;
          }
        }
      }
      catch (Exception e) {
        closeClient(socket, pingSocket, sender, scanner);
        System.err.println("An error occurred while pinging the server");
      }
    });

    clientPingChecker.start();
    receiver.start();
    sender.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      closeClient(socket, pingSocket, sender, scanner);
    }));
  }

  public static boolean isMyClient(DatagramPacket packet) {
    return packet.getAddress().equals(serverAddress) && packet.getPort() == serverPort;
  }

  public static void closeClient(DatagramSocket socket, DatagramSocket pingSocket, Thread sender, Scanner scanner) {
    isClientActive = false;
    System.out.println("Closing client...");

    if (!pingSocket.isClosed()) pingSocket.close();
    if (!socket.isClosed()) {
      UdpUtil.disconnectFromServer(socket, username, serverAddress, serverPort);
    }

    sender.interrupt();

    //close scanner and stdin
    byte[] inputBytes = "\n".getBytes();
    InputStream inputStream = new ByteArrayInputStream(inputBytes);
    System.setIn(inputStream);
    scanner.close();
    try { System.in.close(); } catch (IOException ignored) {}

    System.out.println("Client closed cleanly.");
  }


  public static void comp(){}
}
