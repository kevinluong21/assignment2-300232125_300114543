// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import client.ChatClient;
import common.*;
import ocsf.client.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();

        if (message.trim().charAt(0) == '#') { // **** Changed for E50 KL

          String[] splitMessage = message.split(" "); //splits message by the space if a port number or host name is specified as well

          switch(splitMessage[0].substring(1)) {
            case "quit":
              System.out.println("Terminating client...");
              client.quit();
              break;
            case "logoff":
              System.out.println("Logging off of server...");
              client.closeConnection();
              break;
            case "sethost":
              if (splitMessage.length == 2) { //if the message has a length of 2, then it means a host name was specified
                if (!client.isConnected()) { //client cannot be connected while setting host
                  System.out.println("Host was set successfully.");
                  client.setHost(splitMessage[1]);
                }
                else {
                  System.out.println("ERROR: Cannot set host while client is connected.");
                }
              }
              else {
                System.out.println("ERROR: No host specified.");
              }
              break;
            case "setport":
              if (splitMessage.length == 2) { //if the message has a length of 2, then it means a port number was specified
                if (!client.isConnected()) { //client cannot be connected while setting port
                  try {
                    int portFromMessage = Integer.parseInt(splitMessage[1]);
                    System.out.println("Port was set successfully. Remember to login again to use the server.");
                    client.setPort(portFromMessage);
                  }
                  catch (NumberFormatException e) {
                    System.out.println("ERROR: Port must be an int.");
                  }
                }
                else {
                  System.out.println("ERROR: Cannot set port while client is connected.");
                }
              }
              else {
                System.out.println("ERROR: No port specified.");
              }
              break;
            case "login":
              if (!client.isConnected()) { //client does not need to open connection again if it is already connected
                System.out.println("Client is now connected.");
                client.openConnection();
              }
              else {
                System.out.print("ERROR: Client already connected.");
              }
              break;
            case "gethost":
              System.out.println(client.getHost());
              break;
            case "getport":
              System.out.println(client.getPort());
              break;
          }
        }
        else { // **** Changed for E50 KL
          client.handleMessageFromClientUI(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) // **** Changed for E50 KL
  {
    if (message.contains("SERVER MSG> ")) { //if the message contains SERVER MSG>, it came from the server
      System.out.println(message);
    }
    else {
      System.out.println("> " + message); //message is an echo from the server
    }
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port = DEFAULT_PORT;  //The port number

    try
    {
      host = args[0];
      port = Integer.parseInt(args[1]); // **** Changed for E50 KL
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host, port); // **** Changed for E50 KL
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
