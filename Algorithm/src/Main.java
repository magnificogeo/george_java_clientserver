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
    /*static double final_system_profile;

    static double[] nodeOneProfile;
    static double[] nodeTwoProfile;
    static double[] nodeThreeProfile;

    static double final_system_PAR;
    static double final_system_VAR;

    static double nodeOnePAR;
    static double nodeTwoPAR;
    static double nodeThreePAR;*/

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

            if ( clientSentence.equals("power_profile_one") ) {
                // read and store power profile
            }

            if ( clientSentence.equals("power_profile_two") ) {
                // read and store power profile
            }

            if ( clientSentence.equals("power_profile_three") ) {
                // read and store power profile
            }

            if ( clientSentence.equals("req_power_profile_one") ) {
                // Send power profile back via outToClient

            }

            if ( clientSentence.equals("req_power_profile_two") ) {
                // Send power profile via outToClient
            }

            if ( clientSentence.equals("req_power_profile_three") ) {
                // Send power profile via outToClient
            }

            cycles++;

            System.out.println("Cycle: " + cycles);
        }
    }

    private static void sendData(int portNum, String data) throws Exception {
        Socket clientSocket = new Socket("127.0.0.1", portNum);
        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(data + '\n');
        clientSocket.close();
    }



}



