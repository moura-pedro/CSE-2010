/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 12
   Description of this file: Recommend music that the customers might like in order to
   improve the experience of customers and potentially revenue, 

*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HW4 {
   public static void main(String[] args) throws FileNotFoundException {

      // List of all customers and their songs ratings
      final List<Customer> customerList = getCustomerRatings(args[0]);

      // Heap PQueue to store the distance between target and other customers
      final HeapPriorityQueue<Double, Customer> distanceHeap = new HeapPriorityQueue<>();

      // Construct scanner to read the customer actions
      final Scanner customerActions = new Scanner(new FileInputStream(args[1]));
      
      // Read the customer actions (input file)
      while (customerActions.hasNext()) {
         final String action = customerActions.next();

         switch (action) {
            case "AddCustomer":
               final String name = customerActions.next();
               final String ratings = customerActions.nextLine();
               addCustomer(customerList, name, ratings);
               break;
      
            case "RecommendSongs":
               recommendSongs(distanceHeap, customerList);
               break;

            case "PrintCustomerDistanceRatings":
               calculateDistance(customerList);
               printDistanceRatings(customerList);
               break;
         
            default:
               break;
         }
      }

   }

// ----------------------------- Methods ----------------------------- //
   /**
    * Read the input for the customers and ratings and return a list containing this data
    * @param filename file which cotains customers name and songs ratings
    * @return List of all customers
    * @throws FileNotFoundException
    */
   public static List<Customer> getCustomerRatings (String filename) throws FileNotFoundException {
      Scanner ratingsInput = new Scanner (new FileInputStream(filename));
      List<Customer> tempList = new ArrayList<>();

      while (ratingsInput.hasNext()) {
         final String name = ratingsInput.next();
         final Customer customer = new Customer(name);
         for (int i = 0; i < 10; i++) {
            customer.addRating(ratingsInput.nextInt());
         }
         tempList.add(customer);
      }
      ratingsInput.close();
      return tempList;
   }

   /**
    * Add a given customer into the customer list
    * @param list customer list
    * @param name customer's name
    * @param rate Ratings of songs rated by the customer 
    */
   public static void addCustomer ( List<Customer> list, String name, String rate) {
      System.out.printf("AddCustomer %s%s\n", name, rate);
      Customer tempCustomer = new Customer(name);

      for (int i = 0; i < rate.length(); i++) {
         if (Character.isDigit(rate.charAt(i))) {
            tempCustomer.addRating(Character.getNumericValue(rate.charAt(i)));
         }
      }
      list.add(tempCustomer);
   }

   /**
    * Prints recommends songs based on target and customer
    * @param heap PQ of distance between target and customer
    * @param list list of customers
    * @credits to Jean-Pierre for providind test cases
    */
   public static void recommendSongs (HeapPriorityQueue<Double, Customer> heap, List<Customer> list) {
      for (int i = 1; i < list.size(); i ++) {
         if (list.get(i).getDistance() != 0) {
            heap.insert(list.get(i).getDistance(), list.get(i));
         }
      }
      // Get the closest
      while (heap.size() > 0) {
         Customer closestCustomer = heap.min().getValue();
         String songs = songsRecommended(list.get(0), closestCustomer);

         if (songs.equals("none")) {
            heap.removeMin();
         } else {
            System.out.printf("RecommendSongs %s%s\n", closestCustomer.getCustomer(), songs);
            return;
         }
      }
      System.out.printf("RecommendSongs %s\n", "none");

   }

   /**
    * Recommend songs based on target and customer songs ratings
    * @param target
    * @param closest
    * @return songs recommended
    */
   public static String songsRecommended(Customer target, Customer closest) {
      String recommendation = "";
      for (int i = 0; i < 10; i++) {
         if (target.getRatingList().get(i) == 0 && closest.getRatingList().get(i) >= 4) {
            recommendation += " song" + i + " " + closest.getRatingList().get(i);
         }
      }

      // return none if there is no songs to recommend
      if (recommendation.equals("")) {
         return "none";
      }

      return recommendation;
   }

   /**
    * Prints the distance between a target and a customer
    * @param list Customer's list
    */
   public static void printDistanceRatings (List<Customer> list) {
      Customer target = list.get(0);

      // create a new list with a copy of the customers list
      List<Customer> cpList = new ArrayList<>();
      cpList.addAll(list);

      // remove target from the list
      cpList.remove(0);
      Collections.sort(cpList);
      
      System.out.println("PrintCustomerDistanceRatings");
      System.out.printf("%5s %-10s %s\n", " ", target.getCustomer(), target.getRating());

      final String format = "%.3f %-10s %s\n";
      for (Customer customer : cpList) {
         if (customer.getDistance() == 0) {
            System.out.printf("%s %-10s %s\n", "-----", customer.getCustomer(), customer.getRating());
         } else {
            System.out.printf(format, customer.getDistance(), customer.getCustomer(), customer.getRating());
         }
      }      
   }

   /**
    * Calculates distance between a target and a customer
    * @param list customer's list
    */
   public static void calculateDistance (List<Customer> list) {
      final List<Integer> targetRating = list.get(0).getRatingList();

      // Calculate distance for each customer
      for (int i = 1; i < list.size(); i++) {
         final Customer currentCustomer = list.get(i);
         final List<Integer> currRating = currentCustomer.getRatingList();

         int differenceRate = 0;
         double numOfSongsRated = 0;

         // Check the songs both target and customer rated
         for (int j = 0; j < 10; j++) {
            if (targetRating.get(j) > 0 && currRating.get(j) > 0) {
               differenceRate += Math.abs( targetRating.get(j) - currRating.get(j) );
               numOfSongsRated++;
            }
         }

         // Set distance equal zero if customer did not rate any song
         if (numOfSongsRated == 0) {
            currentCustomer.setDistance(0);
         } else {
            double distance = (1 / Math.abs(numOfSongsRated)) + (differenceRate / Math.abs(numOfSongsRated));
            currentCustomer.setDistance(distance);
         }
      }
   }
}
