/* Congratulations, you found the source code!
 * Want to help us, or just yell at us at how this code hurts your soul? 
 * Contact us here: [insert ways to contact us]
 */

import java.net.*;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

/**
 * Created by: 
 * Belinda Vela 
 * Alan Morelos
 * Leo Sun
 * Benjamin Sun 
 * nick is the best
 */

class Main {
  public static void main(String[] args) {
    System.out.println("Covid Info Database \n\nCreated By: \nBelinda Vela\nAlan Morelos\nLeo Sun\nBenjamin Sun\nUnder Hack Club run by: Nick Zhao\n\nAll information from: https://www.worldometers.info/coronavirus/");
    System.out.println("\nThis is a database with covid data (cases, deaths, etc..) for each day based on each state grabbed from the website. We are not responsible for the accuracy of the data.");
    Scraper scraper = new Scraper(); 
    Scanner scanner = new Scanner(System.in);
    DateStorage dates = new DateStorage();
    
    System.out.println("\nThe data was last updated from the website at " + dates.recentDate());
    System.out.println("If the data is not recent, it may be missing some information or be inaccurate");
    System.out.println("Do you want to reset the data?  (yes or no)");
    String input = scanner.nextLine();
    if (input.contains("y")){
      dates.writeDateToFile();
      clearTheFile();
      scraper.parseData();
      System.out.println("The data has been reset");
    }
    menu();
  }

  public static void clearTheFile() {
    File myObj = new File("real-data.txt"); 
    if (myObj.delete()) { 
      System.out.println("Deleted the file: " + myObj.getName());
      
    } 
    try {
      File newFile = new File("real-data.txt");
      if (newFile.createNewFile()) {
        System.out.println("File created: " + newFile.getName());
        System.out.println("please wait while the data is reset...");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }    

  }

  //prints out the hashMap to TXT file, only for debugging purposes
  public static void debug(DataStorage database){
    try{
      FileWriter myWriter = new FileWriter("debug.txt", true);
      BufferedWriter bw = new BufferedWriter(myWriter);
      bw.write(database.toString());
      bw.close();
      //System.out.println("Successfully wrote to the file.");
    }
		catch(Exception e){
      System.out.println("error");
    }
  }


  public static void menu(){
    //creating the covid data
    DataStorage database = new DataStorage();
    database.createHashMap();
    //debug(database);

    
    String[] possibleData = new String[]{"'Deaths'","'Cases: Daily Cases'","'Deaths: 7-day moving average'","'Deaths: 3-day moving average'","'Cases'","'Currently Infected'","'Cases: 3-day moving average'", "'Deaths: Daily Deaths'", "'Cases: 7-day moving average'"};

    //clarified version because the stings were not easy to understand
    String[] clarifiedData = new String[]{"'Total deaths, counting all deaths previous'","'Cases: Daily Cases'","'Deaths: 7-day moving average'","'Deaths: 3-day moving average'","'Total cases, counting all active, recovered, and dead previous'","'Currently Infected (Active cases)'","'Cases: 3-day moving average'", "'Deaths: Daily Deaths'", "'Cases: 7-day moving average'"};
    Scanner scanner = new Scanner(System.in);

    while(true){

      //keep looping until get a valid state
      boolean confirm = false;
      System.out.println("What state are you looking at? (full name)");
      String state = scanner.nextLine().trim();
      state = database.hasState(state);
      
      while (database.hasState(state).contains("please try a valid")){
        System.out.println("What state are you looking at? (full name)");
        state = scanner.nextLine().trim();
        state = database.hasState(state);
      }



      System.out.println("\nWhat is the date you are looking at? \nValid dates are Mar 12, 2020 to the day before data was updated. \nPlease put in the format MM DD, YYYY. \nExample Input: Apr 01, 2020 \n\n***DISCLAIMER***: If you give data that is invalid, everything will break. \nWe did not implement failsafes against every possible outcome.\n");

      String date = scanner.nextLine();

      while (database.checkDate(state, date).contains("Try again") || date.isEmpty()){
        System.out.println("Please try again and put the date in");
        date = scanner.nextLine();
        date = database.checkDate(state, date);
      }

      for (int i = 0; i < clarifiedData.length; i++){
        System.out.println(i+1 + " - " + clarifiedData[i]);
      }


      //keep looping until get a valid data
      int questionNum = 0;
      while(questionNum > 9 || questionNum < 1){
        System.out.print("\nWhat information are you looking for? (1-9)");
        System.out.println();
        try{
          questionNum  = scanner.nextInt();
        } catch(Exception e){
          System.out.println("Not a number. Please try again.");
          questionNum  = scanner.nextInt();
        }
      }



      //printing out the data 
      String inputForDay = database.getData(state,date,possibleData[questionNum-1]);
      System.out.println(clarifiedData[questionNum-1] + " " + inputForDay);
    

      //get risks for that day
      if (!inputForDay.contains("is not")){
        System.out.println("Would you like to get the risk for that day, a.k.a chance of getting sick? ) (yes or no)");
        String temp = scanner.nextLine();
        String userWantRisk = scanner.nextLine();
        if (userWantRisk.contains("y")){
          double numberOfCases = Double.parseDouble(database.getData(state,date,possibleData[1]));
          System.out.println("\nCases that day: " + numberOfCases);
//        System.out.println("\ngetting risk calculations...");
          infectivityCalculations(state, numberOfCases);
          temp = scanner.nextLine(); 
        }     
      }

		}
  }
  //goes to the infectivity class and pulls out the calculated value
  public static void infectivityCalculations(String state, double numberOfCases) {
    PopulationStorage fullpopulation = new PopulationStorage();
    double statePopulation = (double) fullpopulation.getValue(state);
    System.out.println("State population " + statePopulation);   
		Infectivity infectivity = new Infectivity(); 
		double[] data = infectivity.riskPercentCalculated(numberOfCases, statePopulation);
		double infectionRisk = data[0];
    System.out.println("The risk that a person would be infected on that day is: " + BigDecimal.valueOf(infectionRisk).toPlainString() + "%\n");
		System.out.println("The projected amount of new infections the next day (with the projection of mask efficacy): "+data[1]+".");
		System.out.println("The projected amount of new infections without masks: "+data[2]+".");
  }
}
