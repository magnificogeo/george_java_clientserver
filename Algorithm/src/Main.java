import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;


public class Main {

    static double[] inflexible;
    static double[] flexibleOne;
    static double[] flexibleTwo;
    static double[] flexibleThree;
    static double[] tempArray;
    static double[] tempFlexTwo;
    static double[] tempFlexOne;
    //static double[] serverOne;
    //static double[] serverTwo;
    static double calculatedPAR = 0.0;
    static double lowestPAR = 0.0;

    static double calculatedVAR = 0.0;
    static double lowestVAR = 0.0;

    static double[] optimizedFlexTwo;
    static double[] optimizedFlexOne;

    static int flexOneShift = 8; // flexibleOne can be rightShifted 8 times
    static int flexTwoShift = 7; // flexibleTwo can be rightShifted 7 times
    static int flexThreeShift = 7; // flexibleThree can be rightShifted 7 times


    public static void main(String[] args) {

    /**
    * This section defines the power profiles of flexible and inflexible loads for computation.
    * They are stored in 24 element-wide arrays with each array element as the power load for that hour.
    * These configurations are used as reference and should not be changed!
    */
    inflexible = new double[] {2.01,1.76,1.76,1.76,1.76,1.76,3.26,4.81,0.56,0.26,0.16,0.16,0.16,0.16,0.16,0.16,0.16,1.76,2.06,0.56,0.71,0.71,2.31,4.81}; // power profile for inflexible loads
    flexibleOne = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0,0};
    flexibleTwo = new double[] {0,0,0,0,0,0,0,0,1.8,1.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    flexibleThree = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2};


    tempArray = new double[24];
    tempFlexTwo = new double[24];
    tempFlexOne = new double[24];

    tempFlexTwo = Arrays.copyOf(flexibleTwo,24);
    tempFlexOne = Arrays.copyOf(flexibleOne,24);
    //serverOne = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};
    //serverTwo = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};

        /*try {
            startServer();
        } catch ( Exception e ) {
            System.out.println("Server cannot be started. You might have another process running.");
        }
        try {
            startClient();
        } catch ( Exception e ) {
            System.out.println("Client cannot be started. You might hve another process running.");
        }*/


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
            for(int j = 0; j < flexTwoShift; j++) {
                if ( j == 0 ) {

                    tempArray = addThreeArray(inflexible,tempFlexOne,tempFlexTwo);

                    calculatedPAR = calculatePAR(tempArray);
                    if ( lowestPAR == 0.0 )
                        lowestPAR = calculatedPAR;
                    else if ( calculatedPAR < lowestPAR ) {
                        lowestPAR = calculatedPAR;
                    }

                    calculatedVAR = calculateVAR(tempArray);
                    if ( lowestVAR == 0.0 )
                        lowestVAR = calculatedVAR;
                    else if ( calculatedVAR < lowestVAR ) {
                        lowestVAR = calculatedVAR;
                    }

                    System.out.println("PAR :" + calculatedPAR);
                    System.out.println("VAR :" + calculatedVAR);

                } else {
                    tempFlexTwo = rightShiftArray(tempFlexTwo);
                    tempArray = addThreeArray(inflexible, tempFlexOne, tempFlexTwo);

                    calculatedPAR = calculatePAR(tempArray);
                    if ( lowestPAR == 0.0 ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                    } else if ( calculatedPAR < lowestPAR ) {
                        lowestPAR = calculatedPAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                    }

                    calculatedVAR = calculateVAR(tempArray);
                    if ( lowestVAR == 0.0 ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                    } else if ( calculatedVAR < lowestVAR ) {
                        lowestVAR = calculatedVAR;
                        optimizedFlexOne = Arrays.copyOf(tempFlexOne,24);
                        optimizedFlexTwo = Arrays.copyOf(tempFlexTwo,24);
                    }

                    System.out.println("PAR :" + calculatedPAR);
                    System.out.println("VAR :" + calculatedVAR);

                }
            }
            tempFlexTwo = Arrays.copyOf(flexibleTwo,24); // reset copy of flexible two to default for comparison again :)
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Lowest PAR " + lowestPAR);
        System.out.println("Lowest VAR " + lowestVAR);
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
        //System.out.println("Verify PAR: " + calculatePAR(addThreeArray(inflexible,optimizedFlexOne,optimizedFlexTwo)));
        //System.out.println("Verify VAR: " + calculateVAR(addThreeArray(inflexible,optimizedFlexOne,optimizedFlexTwo)));
        System.out.println("Power profile 1: " + printArray(optimizedFlexOne));
        System.out.println("Power profile 2: " + printArray(optimizedFlexTwo));


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
     * TODO : To implement client server here!
     */

    /**
     * This function starts the server running.
     * @throws Exception
     */
     private static void startServer() throws Exception {
         String clientSentence;
         String capitalizedSentence;
         ServerSocket welcomeSocket = new ServerSocket(6789);
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

    /**
     * This function starts the client running.
     * @throws Exception
     */
     private static void startClient() throws Exception {
         String sentence;
         String modifiedSentence;
         BufferedReader inFromUser = new BufferedReader(
                 new InputStreamReader(System.in));
         //Socket clientSocket = new Socket("change to server's IP address", 6789);
         Socket clientSocket = new Socket("127.0.0.1", 6789);
         DataOutputStream outToServer = new DataOutputStream(
                 clientSocket.getOutputStream());
         BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         sentence = inFromUser.readLine();
         outToServer.writeBytes(sentence + '\n');
         modifiedSentence = inFromServer.readLine();
         System.out.println("FROM SERVER: " + modifiedSentence);
         clientSocket.close();
     }

     private static void requestData() {

     }

    private static void sendData() {

    }


}
	

