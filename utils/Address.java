package utils;

import java.net.InetAddress;

public class Address {
  public InetAddress address;
  public int port;

  public Address(InetAddress address, int port) {
    this.address = address;
    this.port = port;
  }
}
