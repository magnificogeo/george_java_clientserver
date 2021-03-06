import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;
/**
 * This class contains the code for one of the server. All nodes start off as a server, listening for command, before executing :)
 */
public class NodeThree {

    /**
     *  Arrays used to store power profiles for inflexible and flexible appliances.
     */
    static double[] inflexible;
    static double[] flexibleOne;
    static double[] flexibleTwo;
    static double[] flexibleThree;

    /**
     * Temporary arrays used in optimization operations.
     */
    static double[] tempArray;
    static double[] tempFlexTwo;
    static double[] tempFlexOne;

    /**
     * Array definitions to store the requested power profiles from other two nodes before starting optimization.
     */
    static double[] serverOne = new double[24];
    static double[] serverTwo = new double[24];

    /**
     *  Variables to store calculated Peak-to-Average Ratio.
     */
    static double calculatedPAR = 0.0;
    static double lowestPAR = 0.0;

    /**
     * Variables to store calculated variance.
     */
    static double calculatedVAR = 0.0;
    static double lowestVAR = 0.0;

    /**
     * Definition of port numbers for other two nodes.
     */
    static double[] optimizedFlexTwo; // array to store the optimized power profile for flexible appliance 2 for lowest PAR
    static double[] optimizedFlexOne; // array to store the optimized power profile for flexible appliance 1 for lowest PAR
    static double[] optimizedFlexOneVAR; // array to store the optimized power profile for flexible appliance 1 for lowest VAR
    static double[] optimizedFlexTwoVAR; // array to store the optimized power profile for flexible appliance 2 for lowest VAR
    static double[] optimizedTotalPAR = {2.01,1.76,1.76,1.76,1.76,1.76,3.26,4.81,2.36,2.06,0.16,0.16,0.16,0.16,0.16,0.16,0.16,3.76,4.06,2.56,2.71,2.71,2.31,4.81}; // array to store the optimized power profile for lowest PAR
    static double[] optimizedTotalVAR = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // aray to store the optimized power profile for the lowest VAR

    /**
     * Definition of port numbers for other two nodes.
     */
    static int nodeTwoPort = 12000;
    static int nodeOnePort = 11000;

    /**
     * Flags used to control the flow and start-stop of servers.
     */
    static int serverRunning = 0;
    static int maxConnections = 100;
    static int i = 0;
    static int timesRan = 0;
    static int terminateCounter = 0;

    public static void main(String[] args) {

            try {
                double[] tempArray = fileToArray("serverThree_profile_default.txt");
                arrayToFile(tempArray,"serverThree_profile.txt");
                startServer();
                System.out.println("Server started successfully");
                //serverRunning = 1;
            } catch ( Exception e ) {
                System.out.println("Unable to start server process. Process may be used!");
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
        int flexTwoShift = 6; // flexibleTwo can be rightShifted 6 times
        //int flexThreeShift = 7; // flexibleThree can be rightShifted 7 times

        tempArray = new double[24];
        tempFlexTwo = new double[24];
        tempFlexOne = new double[24];

        tempFlexTwo = Arrays.copyOf(flexibleTwo,24);
        tempFlexOne = Arrays.copyOf(flexibleOne,24);

        try {
            NodeThree.sendData(NodeThree.nodeTwoPort, "server_two_power");

        } catch ( Exception e ) {
            System.out.println("power profile request from node 2 failed! Reverting to back-up power profile for calculations.");
            try {
                serverTwo = fileToArray("serverTwo_profile_default.txt");
            } catch ( Exception f ) {
                System.out.println("There is a problem reading the back up power profiles!");
            }
        }


        try {
            NodeThree.sendData(NodeThree.nodeOnePort, "server_one_power");

        } catch ( Exception e ) {
            System.out.println("power profile request from node 1 failed! Reverting to back-up power profile for calculations.");
            try {
                serverOne = fileToArray("serverOne_profile_default.txt");
            } catch ( Exception f ) {
                System.out.println("There is a problem reading the back up power profiles!");
            }
        }


        inflexible = Arrays.copyOf(addThreeArray(inflexible, serverTwo, serverOne), 24);

        /**
         * This loop finds the lowest PAR between inflexible, flexibleOne and flexibleTwo
         */
        long startTime = System.currentTimeMillis();
        // Right shifting through 8 combinations of flexibleOne
        for(int i = 0; i <= flexOneShift; i++) {
            if ( i == 0 ) {
                // do nothing
            } else {
                tempFlexOne = Arrays.copyOf(rightShiftArray(tempFlexOne), 24);
            }
            // Right shifting through 7 combinations of flexibleTwo
            for(int j = 0; j <= flexTwoShift; j++) {
                if ( j == 0 ) {

                    tempArray = Arrays.copyOf(addThreeArray(inflexible, tempFlexOne, tempFlexTwo), 24);

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
                    tempFlexTwo = Arrays.copyOf(rightShiftArray(tempFlexTwo), 24);
                    tempArray = Arrays.copyOf(addThreeArray(inflexible, tempFlexOne, tempFlexTwo), 24);

                    calculatedPAR = calculatePAR(tempArray);
                    if ( lowestPAR == 0.0 ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                        optimizedTotalPAR = Arrays.copyOf(addThreeArray(inflexible, optimizedFlexOne, optimizedFlexTwo), 24);
                    } else if ( calculatedPAR <= lowestPAR ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                        optimizedTotalPAR = Arrays.copyOf(addThreeArray(inflexible, optimizedFlexOne, optimizedFlexTwo), 24);
                    }

                    calculatedVAR = calculateVAR(tempArray);
                    if ( lowestVAR == 0.0 ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOneVAR = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwoVAR = Arrays.copyOf(tempFlexTwo,24);
                        optimizedTotalVAR = Arrays.copyOf(addThreeArray(inflexible, optimizedFlexOneVAR, optimizedFlexTwoVAR), 24);
                    } else if ( calculatedVAR <= lowestVAR ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOneVAR = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwoVAR = Arrays.copyOf(tempFlexTwo,24);
                        optimizedTotalVAR = Arrays.copyOf(addThreeArray(inflexible, optimizedFlexOneVAR, optimizedFlexTwoVAR), 24);
                    }

                    System.out.println("PAR :" + calculatedPAR);
                    System.out.println("VAR :" + calculatedVAR);

                }
            }
            tempFlexTwo = Arrays.copyOf(flexibleTwo,24); // reset copy of flexible two to default for comparison again :)
        }
        long endTime = System.currentTimeMillis();

        ++timesRan; // increment the timesRan counter after executing a loop

        System.out.println("Lowest PAR " + lowestPAR);
        System.out.println("Lowest VAR " + lowestVAR);

        System.out.println("Execution time: " + (endTime - startTime) + "ms");

        try {
            sendData(nodeOnePort,"start_algorithm");
        } catch ( Exception e ) {
            System.out.println("Send data");
        }

        if ((isTerminate( optimizedTotalPAR, "serverThree_profile.txt" ))) {
            terminateCounter++;
        }
        if ((isTerminate( optimizedTotalPAR, "serverThree_profile.txt" )) && (terminateCounter == 10)) {
            try {
                sendData(nodeOnePort,"stop_algorithm_all");
                sendData(nodeTwoPort,"stop_algorithm_all");
                System.out.println("terminating condition reached");
            } catch ( Exception e ) {
                System.out.println("Server has been terminated.");
            }
        }


        /**
         * This code block writes out the output of the files for the power profiles.
         */
        arrayToFile(optimizedTotalPAR,"serverThree_profile.txt"); // Power profile that has the lowest PAR
        arrayToFile(optimizedTotalVAR,"THREE_optimized_for_VAR_profile.txt"); // Power profile that has the lowest VAR
        arrayToFile(optimizedFlexOne,"THREE_optimized_FlexOne_profile.txt"); // Power profile for flexible app 1 for minimum PAR
        arrayToFile(optimizedFlexTwo,"THREE_optimized_FlexTwo_profile.txt"); // Power profile for flexible app 2 for minimum PAR
        arrayToFile(optimizedFlexOneVAR,"THREE_optimized_FlexOne_profile_for_VAR.txt"); // Power profile for flexible app 1 for minimum VAR
        arrayToFile(optimizedFlexTwoVAR,"THREE_optimized_FlexTwo_profile_For_VAR.txt"); // Power profile for flexible app 2 for minimum VAR
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
     * This function starts the server running and spins up a thread to handle multiple connections.
     * @throws Exception
     */
    private static void startServer() throws Exception {
        try {
            ServerSocket welcomeSocket = new ServerSocket(13000);

            while ((i++ < maxConnections || maxConnections == 0)) {

                Socket connectionSocket = welcomeSocket.accept();
                multiThreadedComms connection = new multiThreadedComms(connectionSocket,3);
                Thread t = new Thread(connection);
                t.start();
            }

        } catch (IOException ioe) {
            System.out.println("IOException on socket listen: " + ioe);
        }
    }

    /**
     * This function initializes the client-side of the server for sending data/commands and request for data from other servers.
     * @param portNum
     * @param data
     * @throws Exception
     */
    public static void sendData(int portNum, String data) throws Exception {

        Socket clientSocket = null;
        BufferedReader inFromServer = null;

        clientSocket = new Socket("127.0.0.1", portNum);
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        DataOutputStream outToServer = new DataOutputStream(
                clientSocket.getOutputStream());


        if ( !data.equals("start_algorithm") ) {
            outToServer.writeBytes(data + '\n');

            String inputString;

            while ((inputString = inFromServer.readLine())!=null ) { // read in what is being returned from the server

                String [] temp;

                temp = inputString.split(" ");

                if (data.equals("server_one_power")){
                    for( int j = 1 ; j < 24; j++) {
                        serverOne[j-1] = Double.valueOf(temp[j]);
                    }
                }

                if (data.equals("server_two_power")){
                    for( int j = 1 ; j < 24; j++) {
                        serverTwo[j-1] = Double.valueOf(temp[j]);
                    }
                }
            }

            clientSocket.close();

        } else {
            outToServer.writeBytes(data + '\n');
            clientSocket.close();
        }
    }

    /**
     * This function takes in an array and write it's content into a text file.
     * @param array
     * @param filename
     */
    private static void arrayToFile(double array[], String filename){
    	String[] temp = new String[array.length];

    	for (int i=0; i<array.length; i++){
    		temp[i] = String.valueOf(array[i]);
    	}

        try {
           FileWriter fr = new FileWriter(filename);
           BufferedWriter br = new BufferedWriter(fr);
           PrintWriter out = new PrintWriter(br);
           for(int i=0; i<temp.length; i++){
               if(temp[i] != null)
                   out.write(temp[i] + " ");
           }
           out.close();
        }

        catch(IOException e){
            System.out.println(e);
        }
    }

    /**
     * This function exits the terminates the execution and exits the program.
     */
    public static void exit() {
        System.exit(0);
    }

    /**
     * This function checks for terminating condition
     * @param inArray
     * @param readFromFile
     */
    public static boolean isTerminate(double[] inArray, String readFromFile) {

        double[] tempArray;
        tempArray = fileToArray(readFromFile);

        for(int i = 0; i < inArray.length; i++) {
            if ( tempArray[i] != inArray[i] ) {
                return false;
            }
        }

        return true;
    }

    /**
     * This function takes in filename of the txt file to be read and returns an array.
     * @param fileToBeRead
     * @return array
     * @throws NumberFormatException
     * @throws IOException
     */
    public static double[] fileToArray(String fileToBeRead){

        double[] arr = new double[24];


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileToBeRead));
        } catch (FileNotFoundException e) {
            System.out.print("There is no such file.");
        }

        String line;
        String [] temp;

        try {
            while ((line = br.readLine())!= null){

                temp = line.split(" "); //split spaces

                for (int j = 0; j<temp.length; j++) {
                    arr[j] = Double.valueOf(temp[j]);
                }

            }
        } catch (NumberFormatException e) {
            System.out.print("The input read is of a wrong type.");
        } catch (IOException e) {
            System.out.print("The file cannot be read.");
        }

        try {
            br.close();
        } catch (IOException e) {
            System.out.print("The file cannot be closed.");
        }

        return arr;

    }
}


