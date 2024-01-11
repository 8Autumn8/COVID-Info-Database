//This keeps track of all the times the data has been updated.

import java.net.*;
import java.util.*;
import java.io.*;
import java.text.*; 

public class DateStorage{
  File file = new File("lastupdate.txt");

  public String recentDate(){
    try{
      Scanner myReader = new Scanner(file);
      String date = "";
      while (myReader.hasNextLine()){
        date = myReader.nextLine();
      }
      return date;
    } catch (Exception e){
      return "error";
    }
  }

  public void writeDateToFile(){
    try{
      FileWriter myWriter = new FileWriter("lastupdate.txt", true);
      BufferedWriter bw = new BufferedWriter(myWriter);
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
      Date toWrite = new Date();
      bw.write("\n" + dateFormat.format(toWrite));
      bw.close();
    }
    catch(Exception e){
      System.out.println("error");
    }    
  }
}
