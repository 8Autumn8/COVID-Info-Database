//provides a class where the data is stored, retrived, and displayed...

import java.util.*;
import java.io.*;
import java.net.*;

class DataStorage{

  //place where the data is stored and retrived from
  private HashMap<String,HashMap<String,String[]>> database;
  private Scanner scanner;


  public DataStorage(){
    database = new HashMap<String,HashMap<String,String[]>>();
    scanner = new Scanner(System.in);
    
  }



  public void createHashMap(){
    // variable names = database<KeyMain,innerMap<minorKey,data>>
    try{
      File myObj = new File("real-data.txt");
      Scanner myReader = new Scanner(myObj);
      HashMap<String,String[]> innerMap = new HashMap<String,String[]>();
      String keyMain = myReader.nextLine();
      while (myReader.hasNextLine()){
      //for (int i = 0; i < 400; i++){
        //System.out.println(keyMain);
        String data = myReader.nextLine();
        if (data.contains(",>")){
          String[] split = data.split(",>");
          String minorKey = split[0];
          split[1] = split[1].substring(split[1].indexOf("[")).replaceAll("[\\[\\](){}]","");
          String[] details;
          //this checks to see if it is looking at the date, because the date has a different formating, and it breaks the code
          if (minorKey.equals("Date:")){
            details = split[1].split("\",\"");
            details[0] = details[0].replaceAll("\"","");
            details[details.length-1].trim();
            
          }else {
            details = split[1].split(",");
          }
          details[details.length-1].trim();
          //System.out.println(minorKey + " " + Arrays.toString(details));
          innerMap.put(minorKey, details);

        } else {
          //new hashMap is created to avoid referencing problems.
          database.put(keyMain, new HashMap<String, String[]>(innerMap));
          keyMain = data;
          innerMap.clear();
        }
      }
    }
		catch (Exception e){
      System.out.println("error");
    }
  }

  public String printState(String state){
    String toReturn = "";
    HashMap<String, String[]> innerMap = database.get(state);
    for (String key: innerMap.keySet()){
      toReturn += "key: " + key +" Values: " + Arrays.toString(innerMap.get(key));
    }

    return toReturn;
  }

  public String hasState(String state){
    if (database.containsKey(state)){
      HashMap<String, String[]> checkMap = getStateMap(state);
      if (checkMap.isEmpty()){
        System.out.println("There is no data for that state");
        return "please try a valid value";
      } else {
        return state;
      }
      

    } else{
      //if the user does not put in the 
      String realKeyValue = "";
      for (String key: database.keySet()){
        if (key.toLowerCase().contains(state.toLowerCase())){
          System.out.println("Did you mean: " + key + " yes or no");
          String ans = scanner.nextLine();
          if (ans.contains("y")){
            return key;
          }     
        }
      }
    }
    System.out.println("That was not a valid state."); 
    return "please try a valid value";
  }

  //gets the innerHashMap
  public HashMap<String, String[]> getStateMap(String state){
    HashMap<String, String[]> innerMap = new HashMap<String, String[]>();
    if (database.containsKey(state)){
      innerMap = database.get(state);

    } else{
      //if the user does not put in the 
      String realKeyValue = "";
      for (String key: database.keySet()){
        if (key.toLowerCase().contains(state.toLowerCase())){
          realKeyValue = key;
        }
      }
      innerMap = database.get(realKeyValue);
    }
    return innerMap;

  }

  public String getData(String state, String date, String dataType){
    
    HashMap<String, String[]> innerMap = getStateMap(state);

    try{
      String[] values = (String[])innerMap.get(dataType);
      String[] allDatesListed = innerMap.get("Date:");
      //gets location based on the date
      int location = indexOfArray(date, allDatesListed);
      //System.out.println(location);
      if (location == -1){
        //System.out.println(Arrays.asList(allDatesListed));
        return "is not a valid date";
      } else {
        //System.out.println(values[location]);
        //System.out.println(Arrays.toString(values));
        return values[location];
      }
    } catch (Exception e){
      return "not a valid state";
    }

  }


  public int indexOfArray(String date, String[] arrayOfDates){

    //if it is a valid date that is in the correct format, it will return the index here
    for (int i = 0; i < arrayOfDates.length; i++){
      if (arrayOfDates[i].equals(date)){
        return i;
      }
    }

    // else it will loop through all the dates to find the correct one
    String[] splitDate = date.split(" ");
    String month = "";
    String day = "";
    String year = "";
    for (int i = 0; i < arrayOfDates.length; i++){
      String[] splitAll =  arrayOfDates[i].split(" ");
      if (splitAll[0].contains(splitDate[0]) || splitDate[0].contains(splitAll[0]) && month.equals("")){
        month = splitAll[0];
      }
      if (splitAll[1].replaceFirst("^0+(?!$)", "").equals(splitDate[1].replaceFirst("^0+(?!$)", "")) && day.equals("")){
        day = splitAll[1];
      }
      if (arrayOfDates[i].contains(splitDate[2]) && year.equals("")){
        year = splitAll[2];
      }
      //if all the values found, just break out of the loop.
      if (!month.equals("") && !day.equals("") && !year.equals("")){
        break;
      }
    }

    String newDay = month + " " + day + " " + year;
    for (int i = 0; i < arrayOfDates.length; i++){
      if (arrayOfDates[i].equals(newDay)){
        return i;
      }
    }



    return -1;
  }

  public String[] getArrayOfDates(String state){
    HashMap<String, String[]> innerMap = getStateMap(state);
    return innerMap.get("Date:");
  }

  public String checkDate(String state, String date){

    String[] arrayOfDates = getArrayOfDates(state);
    //if it is a valid date that is in the correct format, it will return the index here
    for (int i = 0; i < arrayOfDates.length; i++){
      if (arrayOfDates[i].equals(date)){
        return date;
      }
    }

    // else it will loop through all the dates to find the correct one
    String[] splitDate = date.split(" ");
    String month = "";
    String day = "";
    String year = "";
    for (int i = 0; i < arrayOfDates.length; i++){
      String[] splitAll =  arrayOfDates[i].split(" ");
      //month
      if (splitAll[0].toLowerCase().contains(splitDate[0].toLowerCase()) || splitDate[0].toLowerCase().contains(splitAll[0].toLowerCase()) && month.equals("")){
        month = splitAll[0];
      }
      //day
      if (splitAll[1].replaceFirst("^0+(?!$)", "").equals(splitDate[1].replaceFirst("^0+(?!$)", "")) && day.equals("")){
        day = splitAll[1];
      }
      //year
      if (arrayOfDates[i].contains(splitDate[2]) && year.equals("")){
        year = splitAll[2];
      }
      //if all the values found, just break out of the loop.
      if (!month.equals("") && !day.equals("") && !year.equals("")){
        break;
      }
    }

    String newDay = month + " " + day + " " + year;
    System.out.println("Did you mean: " + newDay + " (yes or no)");
    String ans = scanner.nextLine();
    if (ans.contains("y")){
      return newDay;
    }   
    return "Try again";
  }


  //mainly for debugging purposes only, but prints out the entire dataBase.
  public String toString(){
    String full = "";
    for (String keyMain : database.keySet()){
      full += keyMain + "\n";
      HashMap<String, String[]> innerMap = database.get(keyMain);
      for (String minorKey : innerMap.keySet()){
        full += minorKey + " " + Arrays.toString(innerMap.get(minorKey)) + "\n";
      }
    }
    return full;
  }
} 
 
 
 
