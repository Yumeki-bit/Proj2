import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * The Class Project2.
 * This Class file prompts the user to enter a .csv file, reads the data in, reformats the dates, calculates the difference between sensors,
 * and calculates the average of each row of sensors
 * 
 * @author Merrick Bronik
 */
public class Project2 {

    /** The dates. */
    public static ArrayList<String> dates = new ArrayList<String>();
    
    /** The times. */
    public static ArrayList<String> times = new ArrayList<String>();
    
    /** The sensor 2278. */
    public static ArrayList<Double> sensor2278 = new ArrayList<Double>();
    
    /** The sensor 3276. */
    public static ArrayList<Double> sensor3276 = new ArrayList<Double>();
    
    /** The sensor 4689. */
    public static ArrayList<Double> sensor4689 = new ArrayList<Double>();
    
    /** The sensor 5032. */
    public static ArrayList<Double> sensor5032 = new ArrayList<Double>();
    
    /** The section 1 difference. */
    public static ArrayList<Double> section1Diff = new ArrayList<Double>();
    
    /** The section 2 difference. */
    public static ArrayList<Double> section2Diff = new ArrayList<Double>();
    
    /** The total average. */
    public static ArrayList<Double> totalAvg = new ArrayList<Double>();

    /**
     * The main method.
     * The main method has while loops to keep the program running after a file the user provided either isn't available,
     * or contains bad data
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Project 2 Data Preprocessing\n");

        //booleans to keep looping the code if it contains bad data or file does not exist
        boolean fileNotFound = true;
        boolean badData = false;
        String fileName = null;
       
        while (fileNotFound || badData)
        {
        	badData = false;
            System.out.println("Enter file name & location.");
            fileName = in.nextLine();
            System.out.println("Reading in Data from the file " + fileName);
            File file = new File(fileName);

            try {
                FileReader fileReader = new FileReader(file);
                Scanner fileScanner = new Scanner(fileReader);
                fileScanner.nextLine();
                while (fileScanner.hasNextLine()) 
                {
                    String line = fileScanner.nextLine();
                    String[] data = line.split(",");
                    if (data.length == 6) 
                    {
                        try
                        {
                            dates.add(formatDate(data[0]));
                        }
                        catch(ParseException e)
                        {
                        	System.out.println("*Bad Date Data in CSV File.*");
                        	System.out.println("Check CSV file data and try again.");
                        	for(int i = 0; i < 6; i++)
                        	{
                        		data[i] = "";
                        	}
                        	dates.clear();
                        	times.clear();
                        	sensor2278.clear();
                        	sensor3276.clear();
                        	sensor4689.clear();
                        	sensor5032.clear();
                        	badData = true;
                            break;
                        }
                        try
                        {
                            times.add(data[1]);
                            sensor2278.add(Double.parseDouble(data[2]));
                            sensor3276.add(Double.parseDouble(data[3]));
                            sensor4689.add(Double.parseDouble(data[4]));
                            sensor5032.add(Double.parseDouble(data[5]));
                        }
                        catch (NumberFormatException e) 
                        {
                            System.out.println("*Bad Number Data in CSV File.*");
                            System.out.println("Check CSV file data and try again.");
                            for(int i = 0; i < 6; i++)
                        	{
                        		data[i] = "";
                        	}
                            dates.clear();
                        	times.clear();
                        	sensor2278.clear();
                        	sensor3276.clear();
                        	sensor4689.clear();
                        	sensor5032.clear();
                            badData = true;
                            break;
                        }
                    }
                    
                   
                }//end while

                fileScanner.close();
                fileNotFound = false;
                
                
               
            }
            catch (FileNotFoundException e)
            {
                System.out.println("*File does not exist or path was entered incorrectly.*");
                System.out.println("Please try again.");
            }
        }
        System.out.println("Converting Dates from MM/DD/YYYY to YYYY/MM/DD");
        
    	System.out.println("Calculating Speed Difference");
    	section1Diff = sensorCalc1(sensor3276, sensor2278);
    	section2Diff = sensorCalc2(sensor5032, sensor4689);
    
    	System.out.println("Calculating Speed Average");
    	totalAvg = (sensorAvg(sensor2278, sensor3276, sensor4689, sensor5032));
    
    	System.out.println("Writing data to file Speed_Data_Difference.csv");
		writeNewFile(fileName);
    	System.out.println("Done! Exiting Program");
        in.close();
    }

    /**
     * Write new file.
     * Writes a new output file that displays the sensor data along with the differences and averages
     * @param fileName the file name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void writeNewFile(String fileName) throws IOException
    {
    	//parses the name to get rid of .csv at the end 
    	String[] fileNameParsed = fileName.split("\\.");
    	PrintWriter reportWriter = new PrintWriter(fileNameParsed[0] + "_Difference.csv");
    	
    	reportWriter.println("Date,Time,Sensor_2278,Sensor_3276,Sensor_4689,Sensor_5032,Section1_Diff,Section2_Diff,Total_Avg");
    	for(int i = 0; i < dates.size(); i++)
    	{
    		reportWriter.println(dates.get(i) + "," + times.get(i) + "," + sensor2278.get(i) + "," + sensor3276.get(i) +
    				"," + sensor4689.get(i) + "," + sensor5032.get(i) + "," + section1Diff.get(i) + "," + 
    				section2Diff.get(i) + "," + totalAvg.get(i));
    	}	
    	reportWriter.close();
	}

	/**
	 * Sensor average.
	 * takes in each sensor ArrayList, and calculates the average of each row through a for loop
	 * @param sensor2278 the sensor 2278
	 * @param sensor3276 the sensor 3276
	 * @param sensor4689 the sensor 4689
	 * @param sensor5032 the sensor 5032
	 * @return the array average list
	 */
	private static ArrayList<Double> sensorAvg(ArrayList<Double> sensor2278, ArrayList<Double> sensor3276, ArrayList<Double> sensor4689, ArrayList<Double> sensor5032)
    {
		ArrayList<Double> sensorAverage = new ArrayList<Double>();
    	double total = 0;
    	double average = 0;
    	for(int i = 0; i < dates.size(); i++)
    	{
    		total += sensor2278.get(i) + sensor3276.get(i) + sensor4689.get(i) + sensor5032.get(i);
    		average = total / 4;
    		sensorAverage.add(average);
    	}
		return sensorAverage;
	}

	/**
	 * Sensor calc 2.
	 * The second difference calculation that calculates the difference for each row of sensor5032 and sensor 4689
	 * @param sensor5032 the sensor 5032
	 * @param sensor4689 the sensor 4689
	 * @return the array list
	 */
	private static ArrayList<Double> sensorCalc2(ArrayList<Double> sensor5032, ArrayList<Double> sensor4689)
    {
    	ArrayList<Double> sensorDifference = new ArrayList<Double>();
		for(int i = 0; i < sensor5032.size(); i++)
		{
			double result;
			result = sensor5032.get(i) - sensor4689.get(i);
			sensorDifference.add(result);
		}
		
		return sensorDifference;
	}

	/**
	 * Sensor calc 1.
	 * Calculates the difference for each row of sensor3276 and sensor2278
	 * @param sensor3276 the sensor 3276
	 * @param sensor2278 the sensor 2278
	 * @return the array list
	 */
	private static ArrayList<Double> sensorCalc1(ArrayList<Double> sensor3276, ArrayList<Double> sensor2278) 
    {
    	ArrayList<Double> sensorDifference = new ArrayList<Double>();
		for(int i = 0; i < sensor3276.size(); i++)
		{
			double result;
			result = sensor3276.get(i) - sensor2278.get(i);
			sensorDifference.add(result);
		}
		
		return sensorDifference;
	}

	/**
	 * Format date.
	 * Reformats the date from MM/dd/yyyy to yyyy/MM/dd
	 * @param dateString the date string
	 * @return the string
	 * @throws ParseException the parse exception
	 */
	public static String formatDate(String dateString) throws ParseException {
        SimpleDateFormat currentFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date dateObject = currentFormat.parse(dateString);
        return newFormat.format(dateObject);
    }
    
}