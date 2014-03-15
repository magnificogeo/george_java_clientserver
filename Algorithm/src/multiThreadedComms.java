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

    //multiThreadedComms(ServerSocket master, Socket server, int nodeNum) {
    	multiThreadedComms(Socket server, int nodeNum) {
    	//this.master = master;
        this.server = server;
        this.nodeNum = nodeNum;
    }

    public void run() {
    	
    	PrintWriter put = null;
    	BufferedReader inFromClient = null;
    	
    	
    	
        try {
        	
        	 put = new PrintWriter(server.getOutputStream(), true); // output stream for sending stuff back to client
            
        	 inFromClient =
            new BufferedReader(new InputStreamReader(
                            server.getInputStream()));
            //DataOutputStream outToClient = new DataOutputStream(
                            //server.getOutputStream());
            clientSentence = inFromClient.readLine();

            //BufferedReader fileBufferedReader = new BufferedReader(new FileReader("/Users/JooSiong/Documents/GitHub/george_java_clientserver/Algorithm/" + clientSentence));;
            
            System.out.println("FROM CLIENT: " + clientSentence);

            // Upon receiving the start_algorithm command, the thread calls the algorithmStart() method in the respective object
            if ( clientSentence.equals("start_algorithm")) {
                if ( nodeNum == 1 )
                    NodeOne.algorithmStart();
                if ( nodeNum == 2 )
                    NodeTwo.algorithmStart();
                if ( nodeNum == 3 )
                    NodeThree.algorithmStart();
            }

            //if ( clientSentence.equals("server_one_power.txt")) {
            else{
                    File f1 = new File(clientSentence);
                    if (f1.exists()) {
                        //BufferedReader fileBufferedReader = new BufferedReader(new FileReader("/Users/george/Dropbox/interpc/EE4210 Project Workspace/Algorithm/bin/" + clientSentence));
                    	//BufferedReader fileBufferedReader = new BufferedReader(new FileReader(clientSentence));
                    	BufferedReader fileBufferedReader = new BufferedReader(new FileReader("/Users/JooSiong/Documents/GitHub/george_java_clientserver/Algorithm/" + clientSentence));;
                    	String line;
                        //line = fileBufferedReader.readLine();
                        
                        System.out.println("BEFORE WHILE haha");
                        //System.out.println(fileBufferedReader.readLine());
                        while ( (line = fileBufferedReader.readLine())!=null ) {
                        	System.out.println(line);// debug;
                        	System.out.println("within WHILE haha"); // debug;
                            put.write(line);
                            put.flush();
                        }
                        System.out.println("after WHILE haha"); // debug;
                        fileBufferedReader.close();
                        server.close();
                        //master.close();
                    }
            }

            
            /*
            if ( clientSentence.equals("server_two_power.txt")) {
                System.out.println("ENTER LOOP"); // debug
                File f2 = new File(clientSentence);
                if (f2.exists()) {
                    System.out.println("ENTER EXISTS"); // debug;
                    //BufferedReader fileBufferedReader = new BufferedReader(new FileReader("/Users/george/Dropbox/interpc/EE4210 Project Workspace/Algorithm/bin/" + clientSentence));
                    //BufferedReader fileBufferedReader = new BufferedReader(new FileReader(clientSentence));
                    String line;
                    //line = fileBufferedReader.readLine();


                    System.out.println("BEFORE WHILE haha"); // debug;
                    System.out.println(fileBufferedReader.readLine());
                    while ((line = fileBufferedReader.readLine())!=null) {
                        System.out.println("INSIDE WHILE! haha"); // debug
                        put.write(line);
                        put.flush();
                    }
                    System.out.println("AFTER WHILE"); // debug
                    fileBufferedReader.close();
                }
            }

            if ( clientSentence.equals("server_three_power.txt")) {
                File f3 = new File(clientSentence);
                if (f3.exists()) {
                    //BufferedReader fileBufferedReader = new BufferedReader(new FileReader("/Users/george/Dropbox/interpc/EE4210 Project Workspace/Algorithm/bin/" + clientSentence));
                	//BufferedReader fileBufferedReader = new BufferedReader(new FileReader(clientSentence));
                	String line;
                    line = fileBufferedReader.readLine();

                    System.out.println("BEFORE WHILE"); // debug;
                    while ( line != null ) {
                        System.out.println("INSIDE WHILE!"); // debug
                        put.write(line);
                        put.flush();
                    }
                    System.out.println("AFTER WHILE"); // debug
                    fileBufferedReader.close();
                }
            }*/

        } catch ( IOException ioe ) {
            System.out.println("IOException on socket listen: " + ioe );
            ioe.printStackTrace();
        }
	
    }
    
}
