import java.io.*;
import java.net.*;

/**
 * This class implements Runnable. Runnable objects are code to be executed within a thread in a process.
 * Constructor takes in
 */
public class multiThreadedComms implements Runnable {
	
	//private ServerSocket master;
    private Socket server;
    private String clientSentence;
    private int nodeNum;

    multiThreadedComms(Socket server, int nodeNum) {
        this.server = server;
        this.nodeNum = nodeNum;
    }

    public void run() {
    	
    	PrintWriter put;
    	BufferedReader inFromClient;

        try {
        	
        	 put = new PrintWriter(server.getOutputStream(), true); // output stream for sending stuff back to client
            
        	 inFromClient =
            new BufferedReader(new InputStreamReader(
                            server.getInputStream()));
            clientSentence = inFromClient.readLine();

            System.out.println("FROM CLIENT: " + clientSentence);

            // Upon receiving the start_algorithm command, the thread calls the algorithmStart() method in the respective object
            if ( clientSentence.equals("start_algorithm")) {
                if ( nodeNum == 1 )
                    NodeOne.algorithmStart();
                if ( nodeNum == 2 )
                    NodeTwo.algorithmStart();
                if ( nodeNum == 3 )
                    NodeThree.algorithmStart();
            } else {
                    File f1 = new File(clientSentence);
                    if (f1.exists()) {
                    	BufferedReader fileBufferedReader = new BufferedReader(new FileReader(clientSentence));
                    	String line;

                        while ( (line = fileBufferedReader.readLine())!=null ) {
                            put.write(line);
                            put.flush();
                        }
                        fileBufferedReader.close();
                        server.close();
                    }
            }

        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }
	
    }
    
}
