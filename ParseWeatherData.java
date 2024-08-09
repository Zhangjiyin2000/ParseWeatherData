
/**
 * Write a description of ParseWeatherData here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.io.*;
import edu.duke.*;
import org.apache.commons.csv.*;
public class ParseWeatherData {
    CSVRecord coldestHourInFile(CSVParser parser) {
        CSVRecord coldestSoFar = null;
        for (CSVRecord record : parser) {
            CSVRecord currentRow = record;
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if (coldestSoFar == null) {
                if (currentTemp != -9999) {
                    coldestSoFar = currentRow;
                } 
            } else {
                double coldestTemp = Double.parseDouble(coldestSoFar.get("TemperatureF"));
                if (currentTemp < coldestTemp && currentTemp != -9999) {
                    coldestSoFar = currentRow;
                }
            }
        }
        return coldestSoFar;
    }
    
    void testColdestHourInFile() {
        FileResource fr = new FileResource("nc_weather/2012/weather-2012-01-01.csv");
        CSVRecord coldestRow = coldestHourInFile(fr.getCSVParser());
        double coldestTemp = Double.parseDouble(coldestRow.get("TemperatureF"));
        String coldestTime = coldestRow.get("TimeEST");
        System.out.println("Coldest temperature on weather-2012-01-01.csv was " + coldestTemp + " at " + coldestTime );
    }
    
    String fileWithColdestTemperature() {
        DirectoryResource dr = new DirectoryResource();
        CSVRecord coldestSoFar = null;
        String rowsInColdestDay = "";
        File coldestFile = null;
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
            double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if (coldestSoFar == null) {
                if (currentTemp != -9999) {
                    coldestSoFar = currentRow;
                    coldestFile = f;
                } 
            } else {
                double coldestTemp = Double.parseDouble(coldestSoFar.get("TemperatureF"));
                if (currentTemp < coldestTemp && currentTemp != -9999) {
                    coldestSoFar = currentRow;
                    coldestFile = f;
                }
            }
        }
        FileResource fr = new FileResource(coldestFile);
        for (CSVRecord record : fr.getCSVParser()) {
            String currentTemp = record.get("TemperatureF"); 
            String currentTime = record.get("DateUTC");
            rowsInColdestDay = rowsInColdestDay + currentTime + ": " + currentTemp + "\n";
        }
        
        return "Coldest day was in file " + coldestFile.getName() + "\n" + "Coldest temperature on that day was " + coldestSoFar.get("TemperatureF") + "\n" + "All the Temperatures on the coldest day were:\n" + rowsInColdestDay;
    }
    
    void testFileWithColdestTemperature() {
        System.out.println(fileWithColdestTemperature());
    }
    
    CSVRecord lowestHumidityInFile(CSVParser parser) {
        CSVRecord lowestSoFar = null;
        for (CSVRecord record : parser) {
            CSVRecord currentRow = record;
            String humidityValue = currentRow.get("Humidity");
            if (lowestSoFar == null) {
                if (!humidityValue.equals("N/A")) {
                    lowestSoFar = currentRow;
                } 
            } else {
                double lowestHumid = Double.parseDouble(lowestSoFar.get("Humidity"));
                //System.out.println("lowestHumid: " + lowestHumid);
                if (!humidityValue.equals("N/A")) {
                    double currentHumid = Double.parseDouble(currentRow.get("Humidity"));
                    //System.out.println("currentHumid: " + currentHumid);
                    if (currentHumid < lowestHumid ) {
                        lowestSoFar = currentRow;
                    }
                }
                
            }
        }
        return lowestSoFar;
    }
    
    void testLowestHumidityInFile() {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        System.out.println("=========");
        CSVRecord csv = lowestHumidityInFile(parser);
        
        System.out.println("Lowest Humidity was " + csv.get("Humidity") + " at " + csv.get("DateUTC"));
    }
    
    CSVRecord lowestHumidityInManyFiles() {
        CSVRecord lowest = null;
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
            if(lowest == null) {
                lowest = currentRow;
            }
            else {
                double currentHumid = Double.parseDouble(currentRow.get("Humidity"));
                double lowestHumid = Double.parseDouble(lowest.get("Humidity"));
                if (currentHumid < lowestHumid) {
                    lowest = currentRow;
                }                
            }
            
        }
        return lowest;
    }
    
    void testLowestHumidityInManyFiles() {
        CSVRecord lowest = lowestHumidityInManyFiles();
        double lowestHumid = Double.parseDouble(lowest.get("Humidity"));
        String lowestTime = lowest.get("DateUTC");
        System.out.println("Lowest Humidity was " + lowestHumid + " at " + lowestTime);
    }
    
    double averageTemperatureInFile(CSVParser parser) {
        double sum = 0.0;
        int count = 0;
        for (CSVRecord record : parser) {
            double temp = Double.parseDouble(record.get("TemperatureF"));
            sum += temp;
            count++;
        }
        return sum / count;
    }
    
    void testAverageTemperatureInFile() {
        FileResource fr = new FileResource();
        double ave = averageTemperatureInFile(fr.getCSVParser());
        System.out.println("Average temperature in file is " + ave);
    }
    
    double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value) {
        double sum = 0.0;
        int count = 0;
        for (CSVRecord record : parser) {
            double temp = Double.parseDouble(record.get("TemperatureF"));
            double humid = Double.parseDouble(record.get("Humidity"));
            if (humid >= value) {
                sum += temp;
                count++;
            }
        }
        return sum / count; 
    }
    
    void testAverageTemperatureWithHighHumidityInFile()  {
        FileResource fr = new FileResource();
        double ave = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(), 80);
        System.out.println("Average temp when high humidity in file is " + ave);
    }
}
