import java.io.*;
import java.net.*;

public class multiThreadedComms implements Runnable {

    private Socket server;
    private String clientSentence;
    private int nodeNum;

    multiThreadedComms(Socket server, int nodeNum) {
        this.server = server;
        this.nodeNum = nodeNum;
    }

    public void run() {

        try {
            BufferedReader inFromClient =
            new BufferedReader(new InputStreamReader(
                            server.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(
                            server.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("FROM CLIENT: " + clientSentence);
            if ( clientSentence.equals("start_algorithm")) {
                if ( nodeNum == 1 )
                    NodeOne.algorithmStart();
                if ( nodeNum == 2 )
                    NodeTwo.algorithmStart();
                if ( nodeNum == 3 )
                    NodeThree.algorithmStart();
            }
            /*capitalizedSentence =
                    clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);*/
        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }

    }
}
