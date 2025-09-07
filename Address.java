import java.net.InetAddress;

class Address {
  InetAddress address;
  int port;

  public Address(InetAddress address, int port) {
    this.address = address;
    this.port = port;
  }
}
