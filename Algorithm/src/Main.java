import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;


public class Main {

	static double[] inflexible;
	static double[] flexibleOne;
	static double[] flexibleTwo;
    static double[] flexibleThree;
	//static double[] serverOne;
	//static double[] serverTwo;
    static double calculatedPAR;

    public static void main(String[] args) {

    /**
    * This section defines the power profiles of flexible and inflexible loads for computation.
    * They are stored in 24 element-wide arrays with each array element as the power load for that hour.
    */
    inflexible = new double[] {2.01,1.76,1.76,1.76,1.76,1.76,3.26,4.81,0.56,0.26,0.16,0.16,0.16,0.16,0.16,0.16,0.16,1.76,2.06,0.56,0.71,0.71,2.31,4.81}; // power profile for inflexible loads
    flexibleOne = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0,0};
    flexibleTwo = new double[] {0,0,0,0,0,0,0,0,1.8,1.8,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    flexibleThree = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2};

    
    //serverone = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};
    //servertwo = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};

    calculatePAR(inflexible);
    addArray(flexibleOne,flexibleTwo);
    rightShiftArray(flexibleThree);

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
     * This method adds two array together and returns an array that is summed.
     * @param array_1
     * @param array_2
     * @return array_sum
     */
    private static double[] addArray(double[] array_1, double[] array_2) {

        double[] array_sum = new double[24]; // initialising an empty array
        int arrayLength = 24;

        for(int i = 0;i < arrayLength; i++) {
            array_sum[i] = array_1[i] + array_2[i];
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



}
	

