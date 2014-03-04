import java.io.*;
import java.net.*;


class Main {
	
	
	static FirstServer first = new FirstServer();
	static SecondServer second = new SecondServer();
	static ThirdServer third = new ThirdServer();
	
	//int[] fixedDemand;
    //int[] flexibleDemand;
    
	static int[] PServerOne; // this will be the power from the respective java classes
    static int[] PServerTwo; // this will be the power from the respective java classes
    static int[] PServerThree; // this will be the power from the respective java classes
   
    
    
    public static void main(String argv[]) throws Exception
    {
    	int flag =1;
    	
    	//state the condition here or the loop will run forever
    	while (true) {
    	
    	// Mode 0 means client and Mode 1 means server
    	if (flag==1){
    		
    		//Server 3 acts as server
    		third.ServerThree(PServerOne, PServerTwo,1);
    		
    		//Server 2 acts as server
    		second.ServerTwo(PServerOne, PServerThree,1);
    		
    		//Server 1 acts as client
    		first.ServerOne(PServerTwo, PServerThree, 0);
    		
    		
    		flag=2;
    		
    	}
    	
    	if (flag==2){
    		
    		//Server 3 acts as server
    		third.ServerThree(PServerOne, PServerTwo,1);
    		
    		//Server 1 acts as server
    		first.ServerOne(PServerTwo, PServerThree, 1);
    		
    		//Server 2 acts as client
    		second.ServerTwo(PServerOne, PServerThree,0);
    		
    		
    		
    		flag=3;
    	}
    	
    	if (flag==3){
    		
    		//Server 2 acts as server
    		second.ServerTwo(PServerOne, PServerThree,1);
    		
    		//Server 1 acts as server
    		first.ServerOne(PServerTwo, PServerThree, 1);
    		
    		//Server 3 acts as client
    		third.ServerThree(PServerOne, PServerTwo,0);
    		
    		
    		
    		flag=1;
    	}
    	
    	}
    	
    	//Take the 3 powers from the 3 servers and this should give you the lowest PAR and variance.
    	
       
    }
    
    

    
    
}
