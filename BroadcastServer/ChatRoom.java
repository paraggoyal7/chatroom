package BroadcastServer;

import java.util.Scanner;

import BroadcastServer.launcher.LauncherFactory;
import utils.Launchers;

public class ChatRoom {
    public static void main(String[] args) {
      System.out.println("Welcome to the chat room!");
      Scanner scanner = new Scanner(System.in);

      while (true) {
        System.out.println("Enter:");
        System.out.println("[1] to start a new chat room");
        System.out.println("[2] to join an existing chat room");
        System.out.println("[3] to exit");

        int choice = scanner.nextInt();

        if (choice == 1) {
          System.out.println("Starting a new chat room...");
          Launchers.launchBroadcast();
        }
        else if (choice == 2) {
          System.out.println("Joining an existing chat room...");
          Launchers.launchListener();
        }
        else if (choice == 3) {
          System.out.println("Exiting...");
          break;
        }
        else {
          System.out.println("Invalid choice. Please try again.");
        }
      }
    }
}
