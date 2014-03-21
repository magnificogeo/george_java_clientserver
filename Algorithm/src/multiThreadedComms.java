import java.io.*;
import java.net.*;

/**
 * This class implements Runnable. Runnable objects are code to be executed within a thread in a process.
 * Constructor takes in
 */
public class multiThreadedComms implements Runnable {

    private Socket server;
    private String clientSentence;
    private int nodeNum;

    multiThreadedComms(Socket server, int nodeNum) {
        this.server = server;
        this.nodeNum = nodeNum;
    }

    public void run() {

    	BufferedReader inFromClient;

        try {
        	 inFromClient =
            new BufferedReader(new InputStreamReader(server.getInputStream()));
        	DataOutputStream outToClient = new DataOutputStream(server.getOutputStream());
        	
            clientSentence = inFromClient.readLine();

            System.out.println("Node " + nodeNum +  " server received:" + clientSentence);

            // Upon receiving the start_algorithm command, the thread calls the algorithmStart() method in the respective object
            if ( clientSentence.equals("start_algorithm")) {
                if ( nodeNum == 1 )

                        NodeOne.algorithmStart();

                if ( nodeNum == 2 )

                        NodeTwo.algorithmStart();

                if ( nodeNum == 3 )

                        NodeThree.algorithmStart();

            } else {

                    	if(clientSentence.equals("server_one_power")){
                    		String resultOne = "";
                    		for (int i=0; i<NodeOne.optimizedTotalPAR.length; i++){
                    			String string = String.valueOf(NodeOne.optimizedTotalPAR[i]);
                    			resultOne = resultOne + " " + string;
                    		}
                    		outToClient.writeBytes(resultOne);
                    	}
                    	
                    	if(clientSentence.equals("server_two_power")){
                    		String resultTwo = "";
                    		for (int i=0; i<NodeTwo.optimizedTotalPAR.length; i++){
                    			String string = String.valueOf(NodeTwo.optimizedTotalPAR[i]);
                    			resultTwo = resultTwo + " " + string;
                    		};
                    		outToClient.writeBytes(resultTwo);
                    	}
                    	
                    	if(clientSentence.equals("server_three_power")){
                    		String resultThree = "";
                    		for (int i=0; i<NodeThree.optimizedTotalPAR.length; i++){
                    			String string = String.valueOf(NodeThree.optimizedTotalPAR[i]);
                    			resultThree = resultThree + " " + string;
                    		}
                    		outToClient.writeBytes(resultThree);
                    	}
                    	server.close();

            }

        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }
	
    }
    
}
