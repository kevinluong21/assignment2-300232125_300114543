// **** Class created for E50 KL

import java.io.*;
import common.*;
import ocsf.server.*;

public class ServerConsole implements ChatIF {

  EchoServer server;

  public ServerConsole(int port) {
    server = new EchoServer(port);

    try {
      server.listen(); // Start listening for connections

    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  public void accept() {
    try {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) {
        message = fromConsole.readLine();

        if (message.trim().charAt(0) == '#') { // **** Changed for E50 KL

          String[] splitMessage = message.split(" "); //splits message by space in the cases for specifying a port number

          switch (splitMessage[0].substring(1)) {
            case "quit":
              System.out.println("Terminated server...");
              System.exit(0); //quits program
              break;
            case "stop":
              System.out.println("No longer accepting new connections...");
              server.stopListening();
              break;
            case "close":
              server.close();
              break;
            case "setport":
              if (splitMessage.length == 2) { //if message is length 2, then there is most likely a port number specified
                if (!server.isListening()) { //sets port number only when the server is not listening
                  try {
                    int portFromMessage = Integer.parseInt(splitMessage[1]);
                    System.out.println("Port was set successfully.");
                    server.setPort(portFromMessage);
                  } catch (NumberFormatException e) {
                    System.out.println("ERROR: Port must be an int.");
                  }
                } else {
                  System.out.println("ERROR: Cannot set port while server is listening.");
                }
              } else {
                System.out.println("ERROR: No port specified.");
              }
              break;
            case "start":
              if (!server.isListening()) { //if server is already listening, it does not need to start again
                System.out.println("Server is now connected.");
                server.listen();
              } else {
                System.out.print("ERROR: Server is already listening.");
              }
              break;
            case "getport":
              System.out.println(server.getPort());
              break;
          }
        } else { // **** Changed for E50 KL
          this.display(message);
        }
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  public void display(String message) {
    server.sendToAllClients("SERVER MSG> " + message);
    System.out.println("SERVER MSG> " + message);
  }

  public static void main(String[] args) {
    int port; // The port number

    try {
      port = Integer.parseInt(args[0]); // **** Changed for E50 KL
    } catch (ArrayIndexOutOfBoundsException e) {
      port = 5555; //default port number
    }
    ServerConsole console = new ServerConsole(port);
    console.accept(); // Wait for console data
  }
}