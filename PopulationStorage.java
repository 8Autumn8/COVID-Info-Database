//this takes the text from the population.txt file and puts it into a HashMap. This is  used for the Infectivity calculation

import java.net.*;
import java.util.*;
import java.io.*;

class PopulationStorage {

  HashMap<String, Integer> populationData = new HashMap<String, Integer>();
  public PopulationStorage(){
    try{
      File myObj = new File("population.txt");
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()){
        String data = myReader.nextLine();
        String[] split = data.split(">");
        populationData.put(split[0], Integer.parseInt(split[1]));
      }
    } catch (Exception e){
      System.out.println("error");
    }
  }

  public int getValue(String state){
    return populationData.get(state);
  }

}
