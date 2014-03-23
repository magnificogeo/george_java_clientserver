import java.io.*;
import java.net.*;

/**
 * This class implements Runnable. Runnable objects are code to be executed within a thread in a process.
 * Constructor takes in server object and the nodeNum which instantiates this object.
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

            //System.out.println("Node " + nodeNum +  " server received:" + clientSentence);

            // Upon receiving the start_algorithm command, the thread calls the algorithmStart() method in the respective object
            /**
             * This block starts the algorithm in each of the nodes at the appriopriate time.
             */
            if ( clientSentence.equals("start_algorithm")) {
                if ( nodeNum == 1 )

                        NodeOne.algorithmStart(); // start algorithm in node one

                if ( nodeNum == 2 )

                        NodeTwo.algorithmStart(); // start algorithm in node two

                if ( nodeNum == 3 )

                        NodeThree.algorithmStart(); // start algorithm in node three

            } else {
                        /**
                         * This snippet here sends an array to the client
                         */

                        if(clientSentence.equals("server_one_power")){
                            String resultOne = "";
                            double[] tempArray = NodeOne.fileToArray("serverOne_profile.txt");

                            for (int i=0; i<tempArray.length; i++){
                                String string = String.valueOf(tempArray[i]);
                                resultOne = resultOne + " " + string;
                            }
                            outToClient.writeBytes(resultOne);
                        }

                        if(clientSentence.equals("server_two_power")){
                            String resultTwo = "";
                            double[] tempArray = NodeTwo.fileToArray("serverTwo_profile.txt");

                            for (int i=0; i<tempArray.length; i++){
                                String string = String.valueOf(tempArray[i]);
                                resultTwo = resultTwo + " " + string;
                            };
                            outToClient.writeBytes(resultTwo);
                        }

                        if(clientSentence.equals("server_three_power")){
                            String resultThree = "";
                            double[] tempArray = NodeThree.fileToArray("serverThree_profile.txt");
                            for (int i=0; i<tempArray.length; i++){
                                String string = String.valueOf(tempArray[i]);
                                resultThree = resultThree + " " + string;
                            }
                            outToClient.writeBytes(resultThree);
                        }

                        /**
                         * This block exits the program and stops the execution when received stop_algorithm_all command.
                         */
                        if(clientSentence.equals("stop_algorithm_all")) {
                            NodeOne.exit();
                            NodeTwo.exit();
                            NodeThree.exit();
                        }


                    	server.close();

            }

        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }
	
    }
    
}
