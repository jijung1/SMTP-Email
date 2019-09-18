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
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Enter sender of message: ");
    final String sender = inFromUser.readLine();

    System.out.print("Enter recipient of message: ");
    final String recipient = inFromUser.readLine();

    System.out.print("Enter subject of message: ");
     final String subject = inFromUser.readLine();

    String message = "";
    boolean boole = true;
    while (boole) {
      System.out.println("Enter message: (append last line of your message with two stars **)");
      String temp = "";
      temp = inFromUser.readLine();
      if (temp.charAt(temp.length() - 1) == '*' && temp.charAt(temp.length() - 2) == '*') {
        temp = temp.substring(0,temp.length() - 2);
        boole = false;
      }
      message = message + temp + '\n';
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
    System.out.println("Client: HELO icd.chapman.edu");
    String response = inFromServer.readLine();
    System.out.println("Server: " + response);

    outToServer.println("MAIL FROM: " + sender);
    System.out.println("Client: MAIL FROM: " + sender);

    response = inFromServer.readLine();
    System.out.println("Server: " + response);

    outToServer.println("RCPT TO: " + recipient);
    System.out.println("Client: RCPT TO: " + recipient);

    response = inFromServer.readLine();
    System.out.println("Server: " + response);

    System.out.println("client: DATA");
    outToServer.println("DATA");
    response = inFromServer.readLine();
    System.out.println("Server: " + response);
    message = "To: " + recipient + "\nFrom: " + sender + "\nSubject: " + subject + "\n" + message;
    System.out.println("Client: \n" + message);
    outToServer.print(message);
    response = inFromServer.readLine();
    System.out.println("Server: " + response);

    outToServer.println("QUIT");
    System.out.println("Client: QUIT");

    response = inFromServer.readLine();
    System.out.println("Server: " + response);

    inFromUser.close();
    clientSocket.close();
  }
}
