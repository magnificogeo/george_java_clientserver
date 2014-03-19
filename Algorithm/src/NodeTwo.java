import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;
/**
 * This class contains the code for one of the server. All nodes start off as a server, listening for command, before executing :)
 */
public class NodeTwo {

    static double[] inflexible;
    static double[] flexibleOne;
    static double[] flexibleTwo;
    static double[] flexibleThree;
    static double[] tempArray;

    static double[] tempFlexOne;
    static double[] tempFlexThree;

    static double[] serverOne;
    static double[] serverThree;

    static double calculatedPAR = 0.0;
    static double lowestPAR = 0.0;

    static double calculatedVAR = 0.0;
    static double lowestVAR = 0.0;

    static double[] optimizedFlexThree; // array to store the optimized power profile for flexible appliance 3 for lowest PAR
    static double[] optimizedFlexOne; // array to store the optimized power profile for flexible appliance 1 for lowest PAR
    static double[] optimizedFlexOneVAR; // array to store the optimized power profile for flexible appliance 1 for lowest VAR
    static double[] optimizedFlexThreeVAR; // array to store the optimized power profile for flexible appliance 3 for lowest VAR
    static double[] optimizedTotalPAR = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // array to store the optimized power profile for lowest PAR
    static double[] optimizedTotalVAR = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // aray to store the optimized power profile for the lowest VAR

    static int nodeOnePort = 11000;
    static int nodeThreePort = 13000;

    static int serverRunning = 0;
    static int maxConnections = 1000;

    public static void main(String[] args) {
        if ( serverRunning == 0 ) {
            try {
                startServer();
                System.out.println("Server started successfully");
                serverRunning = 1;
            } catch ( Exception e ) {
                System.out.println("Unable to start server process. Process may be used!");
            }
        }
    }

    /**
     * This is the main method that does the optimizations for the power profile!
     */
    public static void algorithmStart() {
        /**
         * This section defines the power profiles of flexible and inflexible loads for computation.
         * They are stored in 24 element-wide arrays with each array element as the power load for that hour.
         * These configurations are used as reference and should not be changed!
         */
        inflexible = new double[] {2.01,1.76,1.76,1.76,1.76,1.76,3.26,4.81,0.56,0.26,0.16,0.16,0.16,0.16,0.16,0.16,0.16,1.76,2.06,0.56,0.71,0.71,2.31,4.81}; // power profile for inflexible loads
        flexibleOne = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0,0};
        flexibleTwo = new double[] {0,0,0,0,0,0,0,0,1.8,1.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        flexibleThree = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2};

        int flexOneShift = 8; // flexibleOne can be rightShifted 8 times
        //int flexTwoShift = 7; // flexibleTwo can be rightShifted 7 times
        int flexThreeShift = 7; // flexibleThree can be rightShifted 7 times

        tempArray = new double[24];
        tempFlexThree = new double[24];
        tempFlexOne = new double[24];


        tempFlexThree = Arrays.copyOf(flexibleThree,24);
        tempFlexOne = Arrays.copyOf(flexibleOne,24);

        try {
            sendData(nodeOnePort,"server_one_power.txt");
        } catch ( Exception e ) {
            System.out.println("power profile request from node 1 failed!");
        }

        try {
            sendData(nodeThreePort,"server_three_power.txt");
        } catch ( Exception e ) {
            System.out.println("power profile request from node 3 failed!");
        }

        inflexible = addThreeArray(inflexible,serverOne,serverThree);

        /**
         * This loop finds the lowest PAR between inflexible, flexibleOne and flexibleTwo
         */
        long startTime = System.currentTimeMillis();
        // Right shifting through 8 combinations of flexibleOne
        for(int i = 0; i < flexOneShift; i++) {
            if ( i == 0 ) {
                // do nothing
            } else {
                tempFlexOne = rightShiftArray(tempFlexOne);
            }
            // Right shifting through 7 combinations of flexibleTwo
            for(int j = 0; j < flexThreeShift; j++) {
                if ( j == 0 ) {

                    tempArray = addThreeArray(inflexible,tempFlexOne,tempFlexThree);

                    calculatedPAR = calculatePAR(tempArray);
                    if ( lowestPAR == 0.0 )
                        lowestPAR = calculatedPAR;
                    else if ( calculatedPAR <= lowestPAR ) {
                        lowestPAR = calculatedPAR;
                    }

                    calculatedVAR = calculateVAR(tempArray);
                    if ( lowestVAR == 0.0 )
                        lowestVAR = calculatedVAR;
                    else if ( calculatedVAR <= lowestVAR ) {
                        lowestVAR = calculatedVAR;
                    }

                    System.out.println("PAR :" + calculatedPAR);
                    System.out.println("VAR :" + calculatedVAR);

                } else {
                    tempFlexThree = rightShiftArray(tempFlexThree);
                    tempArray = addThreeArray(inflexible, tempFlexOne, tempFlexThree);

                    calculatedPAR = calculatePAR(tempArray);
                    if ( lowestPAR == 0.0 ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexThree = Arrays.copyOf(tempFlexThree,24);
                        optimizedTotalPAR = addThreeArray(inflexible, optimizedFlexOne, optimizedFlexThree);
                    } else if ( calculatedPAR <= lowestPAR ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexThree = Arrays.copyOf(tempFlexThree,24);
                        optimizedTotalPAR = addThreeArray(inflexible, optimizedFlexOne, optimizedFlexThree);
                    }

                    calculatedVAR = calculateVAR(tempArray);
                    if ( lowestVAR == 0.0 ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOneVAR = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexThreeVAR = Arrays.copyOf(tempFlexThree,24);
                        optimizedTotalVAR = addThreeArray(inflexible, optimizedFlexOneVAR, optimizedFlexThreeVAR);
                    } else if ( calculatedVAR <= lowestVAR ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOneVAR = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexThreeVAR = Arrays.copyOf(tempFlexThree,24);
                        optimizedTotalVAR = addThreeArray(inflexible, optimizedFlexOneVAR, optimizedFlexThreeVAR);
                    }

                    System.out.println("PAR :" + calculatedPAR);
                    System.out.println("VAR :" + calculatedVAR);

                }
            }
            tempFlexThree = Arrays.copyOf(flexibleThree,24); // reset copy of flexible two to default for comparison again :)
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Lowest PAR " + lowestPAR);
        System.out.println("Lowest VAR " + lowestVAR);

        System.out.println("Execution time: " + (endTime - startTime) + "ms");

        // TODO: Write to file
        // TODO: Detect terminating condition

        try {
            sendData(nodeThreePort,"start_algorithm");
        } catch ( Exception e ) {
            System.out.println("Send data failed!");
        }
    }

    /**
     * This method calculates the PAR of the array when given an array.
     * @param loadArray
     * @return calculatedPAR
     */
    private static double calculatePAR(double[] loadArray) {

        double sum = 0.0;
        double average = 0.0;
        double calculatedPAR = 0.0;
        int arrayLength = loadArray.length;

        for(int i = 0;i < arrayLength;i++) {
            sum += loadArray[i];
        }

        average = sum/arrayLength;
        Arrays.sort(loadArray);

        calculatedPAR = loadArray[arrayLength - 1]/average;

        return calculatedPAR;
    }

    /**
     * This method calculates the Variance of the array when given an array.
     * @param loadArray
     * @return calculatedVAR
     */
    private static double calculateVAR(double[] loadArray) {

        double sum = 0.0;
        double mean = 0.0;
        double calculatedVAR = 0.0;
        int arrayLength = loadArray.length;

        for(int i = 0;i < arrayLength;i++) {
            sum += loadArray[i];
        }

        mean = sum/arrayLength;

        for(int i = 0;i< arrayLength;i++) {
            calculatedVAR += Math.pow((loadArray[i] - mean),2);
        }

        return calculatedVAR/24; // formula for variance taken::http://mathworld.wolfram.com/SampleVariance.html
    }

    /**
     * This method prints out the array contents. Used for debugging.
     * @param loadArray
     * @return String
     */
    private static String printArray(double[] loadArray) {
        String array_string = "[ ";
        for(int i = 0;i < 24;i++) {
            array_string += loadArray[i] + " ";
        }
        array_string += " ]";
        return array_string;
    }

    /**
     * This method adds two array together and returns an array that is summed.
     * @param array_1
     * @param array_2
     * @return array_sum
     */
    private static double[] addTwoArray(double[] array_1, double[] array_2) {

        double[] array_sum = new double[24]; // initialising an empty array
        int arrayLength = 24;

        for(int i = 0;i < arrayLength; i++) {
            array_sum[i] = array_1[i] + array_2[i];
        }

        return array_sum;
    }

    /**
     * This method adds three array together and returns an array that is summed.
     * @param array_1
     * @param array_2
     * @param array_3
     * @return array_sum
     */
    private static double[] addThreeArray(double[] array_1, double[] array_2, double[] array_3) {

        double[] array_sum = new double[24]; // initialising an empty array
        int arrayLength = 24;

        for(int i = 0;i < arrayLength; i++) {
            array_sum[i] = array_1[i] + array_2[i] + array_3[i];
        }

        return array_sum;
    }

    /**
     * This method adds four array together and returns an array that is summed.
     * @param array_1
     * @param array_2
     * @param array_3
     * @param array_4
     * @return array_sum
     */
    private static double[] addFourArray(double[] array_1, double[] array_2, double[] array_3, double[] array_4) {

        double[] array_sum = new double[24]; // initialising an empty array
        int arrayLength = 24;

        for(int i = 0;i < arrayLength; i++) {
            array_sum[i] = array_1[i] + array_2[i] + array_3[i] + array_4[i];
        }

        return array_sum;
    }

    /**
     * This method shifts all the items in an array right.
     * @param array
     * @return right_shifted_array
     */
    private static double[] rightShiftArray(double[] array) {

        int arrayLength = array.length;
        double [] temp_array = new double[24];
        double [] right_shifted_array = new double[24];
        temp_array = Arrays.copyOf(array, 24); // makes a copy of the array given

        for(int i = 0;i < arrayLength;i++) {
            if (i == 23) {
                right_shifted_array[0] = temp_array[i];
                return right_shifted_array;
            } else {
                right_shifted_array[i+1] = temp_array[i];
            }
        }

        return right_shifted_array;
    }

    /**
     * This function starts the server running.
     * @throws Exception
     */
    private static void startServer() throws Exception {
        int i = 0;
        try {
            ServerSocket welcomeSocket = new ServerSocket(12000);

            while ((i++ < maxConnections || maxConnections == 0)) {

                System.out.println("I IS :" + i);

                Socket connectionSocket = welcomeSocket.accept();
                multiThreadedComms connection = new multiThreadedComms(connectionSocket,2);
                Thread t = new Thread(connection);
        t.start();
    }

} catch (IOException ioe) {
        System.out.println("IOException on socket listen: " + ioe);
        }
        }

private static void sendData(int portNum, String data) throws Exception {

        Socket clientSocket = null;
        BufferedReader inFromServer = null;
        //PrintWriter put=null;

        try
        {
            clientSocket = new Socket("127.0.0.1", portNum);

            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //put=new PrintWriter(clientSocket.getOutputStream(),true);
        }
        catch(Exception e)
        {
            System.out.println("ERROR");
        }

        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());


        if ( !data.equals("start_algorithm") ) {

            outToServer.writeBytes(data + '\n');
            //put.println(data);

            String inputString;


            //File f2 = new File(data);
            //FileOutputStream f2OutStream = new FileOutputStream(f2);

            serverOne = new double[24];
            serverThree = new double[24];

            while ((inputString = inFromServer.readLine())!=null ) { // read in what is being returned from the server

                String [] temp;

                //byte inputByte[] = inputString.getBytes();
                //f2OutStream.write(inputByte);
                //f2OutStream.flush();
                
                System.out.println("Received by 2 inputstring :" + inputString);

                temp = inputString.split(" ");

                if (data.equals("server_one_power.txt")){
                    for( int j = 1 ; j < 24; j++) {
                        serverOne[j-1] = Double.valueOf(temp[j]);
                    }
                }

                if (data.equals("server_three_power.txt")){
                    for( int j = 1 ; j < 24; j++) {
                        serverThree[j-1] = Double.valueOf(temp[j]);
                    }
                }


            }


            //f2OutStream.close();

            clientSocket.close();


        } else {
        	System.out.println("sent req to node 3 to start");
            outToServer.writeBytes(data + '\n');
            clientSocket.close();
        }
    }


	private static void arrayToFile(double array[]){
		String[] temp = new String[array.length];
	
		for (int i=0; i<array.length; i++){
			temp[i] = String.valueOf(array[i]);
		}
	
		try{  
			FileWriter fr = new FileWriter("server_two_power.txt");  
			BufferedWriter br = new BufferedWriter(fr);  
			PrintWriter out = new PrintWriter(br);  
			for(int i=0; i<temp.length; i++){  
				if(temp[i] != null)  
                     
					out.write(temp[i] + " ");  
                 	//out.write("\n");         
			}  
          	out.close();  
             
             
		}  
         
		catch(IOException e){  
			System.out.println(e);     
		}  
	}




}



