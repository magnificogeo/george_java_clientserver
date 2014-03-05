import java.util.Arrays;

/*
public class Algo {

	static int[] inflexible;
	static int[] flexibleOne;
	static int[] flexibleTwo;
	static int[] serverone;
	static int[] servertwo;
	static int[] total;
	static int[] temp;
	static int[] result;
	static int[] zero;
	static int[] temp2;

    public static void main(String[] args){

    inflexible = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};
    flexibleOne = new int[] {1,1,1};
    flexibleTwo = new int[] {2,2,2};
    
    serverone = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};
    servertwo = new int[] {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0};
    
    total = new int[] {3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,0,0,0};
    
    temp = new int[24];
    
    zero = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    
    result = new int[24];
    
    temp2 = new int[24];
    
    result = zero;
    
    int StartTime=0;
    int EndTime=0;
    
    int maxPeak = 100;
    
    int check;
    
    int check2;
    
    
    // Start and end of 
    for (int i=1; i<24; i++){
    	for (int counter=0; counter<flexibleTwo.length; counter++){ 
    		temp = Arrays.copyOf(total, 24);
    		check = i + counter;
    		if (check<24){
			int value= total[i+counter] + flexibleTwo[counter];
			//System.out.println(total[i+counter]);
			temp[i + counter] = value;
    		}
    		else {
    			check = check-24;
    			int value= total[check] + flexibleTwo[counter];
    			//System.out.println("Total: " + total[check]);
    			temp[check] = value;
    		}
    	}
		
    	for (int j=20; j<24; j++){
    		for (int counter2=0; counter2<flexibleOne.length; counter2++){
    			temp2=Arrays.copyOf(temp, 24);
    			check2 = j+counter2;
    			if (check2<24){
    			int value= temp[j+counter2] + flexibleOne[counter2];
    			temp2[j + counter2] = value;
    			}
    			else {
    				check2 = check2-24;
    				int value= temp[check2] + flexibleOne[counter2];
    				//System.out.println("Temp: " + temp[check2]);
    				//System.out.println("Flexible: " + flexibleOne[counter2]);
    				//System.out.println("Value: " + value);
        			temp2[check2] = value;
    			}
    		}


    		if (FindMax(temp2) < maxPeak){
    			maxPeak = FindMax(temp2);
    			System.out.println("Peak: " + maxPeak);
    			result = Arrays.copyOf(temp2, 24);
    			StartTime = j;
    			System.out.println("Start: " + StartTime);
    			EndTime = j + flexibleOne.length;
    			System.out.println("End: " + EndTime);
    		}

    	}
    	
    }
    
    
	}
	
	private static int FindMax(int[] array){
		
		int max = array[0];

		for ( int i = 1; i < array.length; i++) {
		    if ( array[i] > max) {
		      max = array[i];
		}
		}
		
		
		return max;
	}
}*/
	

