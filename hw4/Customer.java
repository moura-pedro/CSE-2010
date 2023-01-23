/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 12
   Description of this file: Helper Class for HW4

*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Customer implements Comparable<Customer>{
   private String name;
   private String rating;
   private List<Integer> ratingList;
   private double distance;

   // Construct Customer with just the name
   public Customer (String name) {
      this.name = name;
      this.ratingList = new ArrayList<>();
      this.distance = 0.0;
      this.rating = "";
   }

   // Construct Customer with both the name rating
   public Customer (String name, String rate) {
      this.name = name;
      List<String> tempList = new ArrayList<String>(Arrays.asList(rate.split(" ")));
      this.ratingList = convertStringtToInt(tempList);
      this.distance = 0.0;
      this.rating = rate;
   }

   // Set and get customer name
   public void setCustomer (String name) { this.name = name; }
   public String getCustomer () { return this.name; }

   // add and get customer song's ratings
   public void addRating (int rate) { this.ratingList.add(rate); }
   public List<Integer> getRatingList () { return this.ratingList; }
   public String getRating () { 
      String temp = getRatingList().get(0).toString();
      for (int i = 1; i < this.ratingList.size(); i++) {
         temp += " " + this.ratingList.get(i).toString();
      }
      this.rating = temp;
      return rating;
   }

   // set and get distance data
   public void setDistance (double distance) { this.distance = distance; }
   public double getDistance () { return this.distance; }

   // debug func
   public void printDetais () {
      System.out.println("Customer : " + this.name);
      System.out.println("Distance: " + this.distance);
      System.out.println("Rating : " + this.ratingList);
   }

   // convert a list of string into a list of integers
   public List<Integer> convertStringtToInt (List<String> list) {
      List <Integer> tempLIntegers = new ArrayList<Integer>();
      for (String string : list) {
         tempLIntegers.add(Integer.parseInt(string));
      }
      return tempLIntegers;
   }

   @Override
   public int compareTo(Customer u) {
      if (this.getCustomer() == null || u.getCustomer() == null) {
         return 0;
      }
      return this.getCustomer().compareTo(u.getCustomer());
   }
}
