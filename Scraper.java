//This goes to the website: https://www.worldometers.info/coronavirus/ and takes all the data in regarding cases, deaths, etc....

import java.net.*;
import java.util.*;
import java.io.*;

class Scraper{

  public void parseData(){
    try{
      URL url = new URL("https://www.worldometers.info/coronavirus/country/us/");
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");
      httpURLConnection.setReadTimeout(10000 /*milliseconds */);
      httpURLConnection.setConnectTimeout(15000 /*milliseconds */);
      httpURLConnection.connect();      
      //reading file

      InputStream inputStream = httpURLConnection.getInputStream(); 
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


      //scans through all the lines of html on the main page
      String inputLine;
      while ((inputLine = bufferedReader.readLine()) != null) {
        if (inputLine.contains("<a class=\"mt_a\" href=\"/coronavirus/usa/")){
          //pulls the state name
          String state = inputLine.substring(inputLine.indexOf("href")+23);
          state = state.substring(state.indexOf(">")+1,state.indexOf("<"));
          //System.out.println(state);
          checkState(state);
        }
      }
    } catch (Exception e){
      System.out.println("error");
    }    
  }

  //checks to see if it keeps track of counties
  public boolean checkState(String state){
    try {
      URL url = new URL("https://www.worldometers.info/coronavirus/usa/" + state.toLowerCase());

      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");
      httpURLConnection.setReadTimeout(10000 /*milliseconds */);
      httpURLConnection.setConnectTimeout(15000 /*milliseconds */);
      httpURLConnection.connect();

      
      InputStream inputStream = httpURLConnection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
      String inputLine;
      

      //scans through all the lines of html on the main page
      HashMap<String, String> map = new HashMap<String,String>();
      String previous = "";
      boolean repeat = false;
      int count = 0;
      boolean foundDate = false;
      while ((inputLine = bufferedReader.readLine()) != null) {
        
        if (inputLine.contains("name:")){
          String nameLine = inputLine;
          while (!inputLine.contains("data:")){
            inputLine = bufferedReader.readLine();
          }
          //removing ugly things
          nameLine = nameLine.substring(nameLine.indexOf("\'"));

          if (repeat){
            //changing the name so that it includes the 
            nameLine = previous.substring(0,previous.length()-2) + ": " + nameLine.substring(1);
            count++;
            //there are three detailed data, three day average, seven day average, and daily, so i used a counter after it repeated.
            if (count == 3){
              repeat = false;
              previous = "";
            }
          } else {
            if (nameLine.equals(previous)){
              repeat = true;
            }
            previous = nameLine; 
          }         
          //making the data pretty
          String data = inputLine.substring(inputLine.indexOf("data"));
          //System.out.println(nameLine);
          //adding it to map
          map.put(nameLine,data);
          
          //this is to get the dates as a list
        } else if (inputLine.contains("categories:") && foundDate==false){
          String timeLine = inputLine.substring(inputLine.indexOf("["));
          String dates = "Date:,";
          map.put(dates, timeLine);
          foundDate = true;
        }
      }
      //System.out.println(map);
      addToFile(state, map);
    } catch (Exception e){
      System.out.println("error");
    }  
    return false;
  }
  
  public void addToFile(String state, HashMap<String, String> data){
    try{
      FileWriter myWriter = new FileWriter("real-data.txt", true);
      BufferedWriter bw = new BufferedWriter(myWriter);
      bw.write(state);
      //writes all the data to the txt file
      for (String key : data.keySet()){
        bw.newLine();
        bw.write(key + ">" + data.get(key));
      }
      bw.newLine();
      bw.close();
      //System.out.println("Successfully wrote to the file.");
    }
		catch(Exception e){
      System.out.println("error");
    }
  }

}
