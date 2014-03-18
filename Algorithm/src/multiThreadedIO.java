/**
 * Created by george on 17/3/14.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class multiThreadedIO implements Runnable {

    double[] mainArray;
    String fileName;

    multiThreadedIO(double inputArray[], String filename) {
        this.mainArray = Arrays.copyOf(inputArray,24);
        this.fileName = filename;
    }

    public void run() {

        String[] temp = new String[mainArray.length];

        for (int i=0; i< mainArray.length; i++){
            temp[i] = String.valueOf(mainArray[i]);
        }

        try{
            FileWriter fr = new FileWriter(fileName);
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
