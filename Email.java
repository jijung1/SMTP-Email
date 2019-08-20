/**
*  Email Program
*  Simulates a client session to an SMTP server.
*  Prompts user for email address of the sender
*  Prompts user for recipient of email
*  Prompts user for message body in a loop to read multiple lines.
*  opens socket connection to smtp.chapman.edu via port 25
*  Displays communication between client and server.
*  closes buffered reader stream and socket connection.
*  Closes the socket and exits
*
*  @author: Jin Jung
*  Email: jijung@chapman.edu
*  Date:  2/15/2019
*  @version: 1.0
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

class Email {

  public static void main(String[] argv) throws Exception {

    Socket clientSocket = null;
    String recipient = "";
    String subject = "";
    String message = "";
    String sender = "";

    System.out.print("Enter sender of message: ");
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    sender = inFromUser.readLine();

    System.out.print("Enter recipient of message: ");
    recipient = inFromUser.readLine();

    boolean boole = true;
    while (boole) {
      System.out.println("Enter one line of message: ");
      message += inFromUser.readLine() + '\n';
      System.out.println("Enter another line? (y/n)");
      int n = inFromUser.read();
      if (n != 121 && n != 89) {
        boole = false;
      }
      inFromUser.readLine();
    }
    inFromUser.close();
    message += ".\r\n";
    try {
      clientSocket = new Socket("smtp.chapman.edu", 25);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }
    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

    outToServer.println("HELO icd.chapman.edu");
    System.out.println("Client: HELO llb16.chapman.edu");
    String response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    outToServer.println("MAIL FROM: " + sender);
    System.out.println("Client: sent cmd line: MAIN FROM: " + sender);

    response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    outToServer.println("RCPT TO: " + recipient);
    System.out.println("Client: RCPT TO: " + recipient);

    response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + sender);

    System.out.println("Client: DATA\nsent cmd line " + message);
    outToServer.println("DATA");

    response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    outToServer.print(message);
    response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    outToServer.println("QUIT");
    System.out.println("Client: QUIT");

    response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    inFromUser.close();
    clientSocket.close();
  }
}
