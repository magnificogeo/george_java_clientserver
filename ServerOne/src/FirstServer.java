import java.io.*;
import java.net.*;
import java.util.Arrays;


public class FirstServer {
	
	//TCPClient Client = new TCPClient();
	//TCPClient Server = new TCPClient();
	
	public int[] myPower;
	
	public FirstServer(){
		super();
		myPower = new int[24];
	}
	
	public void ServerOne(int[] PServerTwo, int[] PServerThree, int mode) throws Exception {
    	
    	int[] inflexiblePower;
    	inflexiblePower = new int[24];
    	
    	int[] flexiblePower;
    	inflexiblePower = new int[24];
    	
    	// If server 1 is acting as client
    	if (mode==0){
    		
    		while (true) {
    			AskFromServerTwo();
        		AskFromServerThree();
        		
        		//Do my calculations/algorithm here
    		}

    	}
    	
    	
    	// If server 1 is acting as a server
    	else {
    		
    		while(true){
    		ActAsServer();
    		}

    	}
    	
    	
    	
    }
	
	 private void AskFromServerTwo() throws Exception{
	        String sentence;
	        String modifiedSentence;
	        BufferedReader inFromUser = new BufferedReader(
	                new InputStreamReader(System.in));
	        //Socket clientSocket = new Socket("change to server's IP address", 6789);
	        Socket clientSocket = new Socket("127.0.0.1", 6788);
	        DataOutputStream outToServer = new DataOutputStream(
	                clientSocket.getOutputStream());
	        BufferedReader inFromServer = 
	                new BufferedReader(new InputStreamReader(
	                    clientSocket.getInputStream()));
	        sentence = inFromUser.readLine();
	        outToServer.writeBytes(sentence + '\n');
	        modifiedSentence = inFromServer.readLine();
	        System.out.println("FROM SERVER: " + modifiedSentence);
	        clientSocket.close();        
	    }
	 
	 
	 private void AskFromServerThree() throws Exception{
	        String sentence;
	        String modifiedSentence;
	        BufferedReader inFromUser = new BufferedReader(
	                new InputStreamReader(System.in));
	        //Socket clientSocket = new Socket("change to server's IP address", 6789);
	        Socket clientSocket = new Socket("127.0.0.1", 6789);
	        DataOutputStream outToServer = new DataOutputStream(
	                clientSocket.getOutputStream());
	        BufferedReader inFromServer = 
	                new BufferedReader(new InputStreamReader(
	                    clientSocket.getInputStream()));
	        sentence = inFromUser.readLine();
	        outToServer.writeBytes(sentence + '\n');
	        modifiedSentence = inFromServer.readLine();
	        System.out.println("FROM SERVER: " + modifiedSentence);
	        clientSocket.close();        
	    }
	 
	 public void ActAsServer() throws Exception
	    {
	        String clientSentence;
	        String capitalizedSentence;
	        ServerSocket welcomeSocket = new ServerSocket(6787);
	        while(true) {
	            Socket connectionSocket = welcomeSocket.accept();
	            BufferedReader inFromClient = 
	                    new BufferedReader(new InputStreamReader(
	                        connectionSocket.getInputStream()));
	            DataOutputStream outToClient = 
	                    new DataOutputStream(
	                        connectionSocket.getOutputStream());
	            clientSentence = inFromClient.readLine();
	            capitalizedSentence = 
	                    clientSentence.toUpperCase() + '\n';
	            outToClient.writeBytes(capitalizedSentence);
	        }
	    }
	 
	 
	 public int[] Power(){
		 int[] Powermine;
		 Powermine = Arrays.copyOf(myPower, 24);
		 return Powermine;
	 }
    
}
