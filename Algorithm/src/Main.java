import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;
/**
 * This class contains the code for one of the server. All nodes start off as a server, listening for command, before executing :)
 */
public class Main {

    static int nodeOnePort = 11000;
    static int nodeTwoPort = 12000;
    static int nodeThreePort = 13000;

    static int cycles = 0;
    /*static int final_power_profile;
    static int final_system_PAR;
    static int final_system_VAR;

    static int nodeOnePAR;
    static int nodeTwoPAR;
    static int nodeThreePAR;*/



    public static void main(String[] args) {

        // Read in commandline arguments
        for (String s: args) {
           if (s.equals("start"))
               System.out.println("Kick off!");

                try {
                    sendData(nodeOnePort,"start_algorithm");
                } catch ( Exception e ) {
                    System.out.println("Send data failed!");
                }

                try {
                    startServer();
                    System.out.println("Server started. Listening on port 10000");
                } catch ( Exception e ) {
                    System.out.println("Server cannot be started. Another process may be using the port!");
                }

        }
    }

    /**
     * This function starts the server running.
     * @throws Exception
     */
    private static void startServer() throws Exception {
        String clientSentence;
        //String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(10000);
        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(
                            connectionSocket.getInputStream()));
            DataOutputStream outToClient =
                    new DataOutputStream(
                            connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("FROM CLIENT: " + clientSentence);

            cycles++;

            System.out.println("Cycle: " + cycles);
            /*capitalizedSentence =
                    clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);*/
        }
    }

    private static void sendData(int portNum, String data) throws Exception {
        Socket clientSocket = new Socket("127.0.0.1", portNum);
        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(data + '\n');
        /*modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);*/
        clientSocket.close();
    }



}



