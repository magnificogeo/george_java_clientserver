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
        	
        	// put = new PrintWriter(server.getOutputStream(), true); // output stream for sending stuff back to client
            
        	 inFromClient =
            new BufferedReader(new InputStreamReader(server.getInputStream()));
        	DataOutputStream outToClient = new DataOutputStream(server.getOutputStream());
        	
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

                    	if(clientSentence.equals("server_one_power.txt")){
                    		System.out.println("Enter Server1: ");
                    		
                    		String resultOne = "";
                    	
                    		for (int i=0; i<NodeOne.optimizedTotalPAR.length; i++){
                    			String string = String.valueOf(NodeOne.optimizedTotalPAR[i]);
                    			resultOne = resultOne + " " + string;
                    		}
                    		
                    		System.out.println("Write CLIENT by 1:" +resultOne );
                    		
                    		outToClient.writeBytes(resultOne);
                    		//put.write(resultOne);
                            //put.flush();
                    	}
                    	
                    	if(clientSentence.equals("server_two_power.txt")){
                    		
                    		System.out.println("Enter Server2: ");

                    		String resultTwo = "";
                    		for (int i=0; i<NodeTwo.optimizedTotalPAR.length; i++){

                    			String string = String.valueOf(NodeTwo.optimizedTotalPAR[i]);
                    			resultTwo = resultTwo + " " + string;
                    		}
                    		
                    		System.out.println("Write CLIENT by 2:" +resultTwo );
                    		
                    		outToClient.writeBytes(resultTwo);
                    		//put.write(resultTwo);
                            //put.flush();
                    	}
                    	
                    	if(clientSentence.equals("server_three_power.txt")){
                    		
                    		System.out.println("Enter Server3: ");
                    		
                    		String resultThree = "";
                    		for (int i=0; i<NodeThree.optimizedTotalPAR.length; i++){
                    			String string = String.valueOf(NodeThree.optimizedTotalPAR[i]);
                    			resultThree = resultThree + " " + string;
                    		}
                    		
                    		System.out.println("Write CLIENT by 3:" +resultThree );
                    		
                    		outToClient.writeBytes(resultThree);
                    		//put.write(resultThree);
                            //put.flush();
                    	}
                    	server.close();
                    	

            }

        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }
	
    }
    
}
