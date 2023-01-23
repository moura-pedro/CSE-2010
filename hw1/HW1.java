/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 01
   Description of this file: An online retailer provides a service that allows chatting on their website with a customer service representative.
   When a customer requests a chat and the retailer does not have an available representative, the customer can be put on hold. The customer 
   decides to be put on hold or try again later. A customer can also quit waiting after being put on hold. Customers are served in the order of
   their requests. Similarly, representatives are selected to serve customers based on the order of when they become available.

 */

import java.util.Scanner;
import java.io.FileInputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.text.ParseException;

public class HW1 {

   // Variable to store maximum time waited by a customer
   public static int maxTime = 0;
   
   // Output formats
   private final static String format2 = "%s %s\n";
   private final static String format3 = "%s %s %s\n";
   private final static String format4 = "%s %s %s %s\n";

   private final static SinglyLinkedList<String> available = new SinglyLinkedList<>(); // Store available representatives
   private final static SinglyLinkedList<String> busy = new SinglyLinkedList<>();      // Store busy representatives
   private final static SinglyLinkedList<String> hold = new SinglyLinkedList<>();      // Store customers on hold
   private final static SinglyLinkedList<String> reqTime = new SinglyLinkedList<>();   // Store the time when a service is requested

   // Create a hour format for future manipulations
   private final static SimpleDateFormat format = new SimpleDateFormat("HHmm");

   public static void main(String[] args) throws IOException, ParseException {

      // Check if the program was run with the command line argument
      if (args.length < 1) {
         System.out.println("Error, usage: java Class filename");
         System.exit(1);
      }

      // Representatives Names
      final String[] representatives = {"Alice", "Bob", "Carol", "David", "Emily"}; 

      // Add all available representatives in a list
      for (int i = 0; i < representatives.length; i++) {
         available.addLast(representatives[i]);
      }

      // Create a scanner to read the input
      final Scanner input = new Scanner(new FileInputStream(args[0]));

      // Read the input file and run the main program logic
      while (input.hasNext()) {
         String request = input.next();

         // Check which type of request the customer is doing 
         switch (request) {

            // Requested a customer service with a representative
            case "ChatRequest":
               chatRequest(input);
               break;
            
            // Custumer that was on hold requested to quit
            case "QuitOnHold":
               quitOnHold(input);
               break;

            // Prints the availabe representatives to chat with customers
            case "PrintAvailableRepList":
               printAvailableRepresentatives(input);
               break;

            // Ends a chat between a customer and a representative
            case "ChatEnded":
               endChat(input);
               break;

            // Prints maximum time waited by a customer
            case "PrintMaxWaitTime":
               printMaxWaitTime(input);
               break;
         }
      }
      input.close();
   }

   /* -------------------------------- Methods ------------------------------------ */

   /**
   * Description: Assign a representivive, if any available, to a customer. If no representatives
   *              available, check if customer would like to wait or not. If customer would like
   *              to wait, put customer on hold.
   *
   * @param    input
   * @return   nothing
    **/
   public static void chatRequest(Scanner input) {
      final String currTime = input.next();
      final String customer = input.next();
      final String service = input.next();

      System.out.printf(format4, "ChatRequest", currTime, customer, service);

      if (!available.isEmpty()) {   // Check if there is any available representative
         System.out.printf(format4, "RepAssignment", customer, available.first(), currTime);
         busy.addLast(available.first()); // Assign customer to a representative 
         available.removeFirst();         // Remove representative assigned from available list
      } else if (available.isEmpty() && service.equals("wait")) {   // Check if customer would like to wait for a representative
         System.out.printf(format3, "PutOnHold", customer, currTime);
         hold.addLast(customer);       // Put representative on hold 
         reqTime.addLast(currTime);    // Store time to keep track the wait time until be assigned to a representative
      } else if (available.isEmpty() && service.equals("later")) {
         System.out.printf(format3, "TryLater", customer, currTime);
      }
   }

   /**
   * Description: If customer is on hold and would like to quit the hold line, removes customer
   *              from the on hold line.
   * 
   * @param    input
   * @return   nothing
    **/
   public static void quitOnHold (Scanner input) {
      final String currTime = input.next();
      final String customer = input.next();

      System.out.printf(format3, "QuitOnHold", currTime, customer);

      int idxDel = hold.deleteElem(customer);

      reqTime.deleteElem(reqTime.findElement(idxDel));
   }

   /**
   * Description: Check if there is any representive available. If there is, prints the names
   *              of all representatives availabe. Otherwise, prints nothing.
   *
   * @param    input
   * @return   nothing
    **/
   public static void printAvailableRepresentatives (Scanner input) {
      final String currTime = input.next();

      if (available.allElements() == null) {
         System.out.printf(format2, "AvailableRepList", currTime);
      } else {
         System.out.printf(format3, "AvailableRepList", currTime, available.allElements());
      }
               
   }

   /**
   * Description: Assign customer that is on hold to reprensentative. Also removes both customer from the on hold list
   *              and representative from the available list
   *
   * @param    currTime
   * @return   nothing
   * @throws ParseException
   * @throws NumberFormatException
    **/
   public static void updateOnHold (String currTime) throws NumberFormatException, ParseException {
      System.out.printf(format4, "RepAssignment", hold.first(), available.first(), currTime);

      busy.addLast(available.first()); // Set representative as busy
      available.removeFirst();         // Remove representative as available
      hold.removeFirst();              // Remove the first customer from on hold

      // Update max waited time by a customer
      final int waitedTime = Integer.parseInt(totalWaitTime(currTime, reqTime.first()));
      if (maxTime <= waitedTime) {
         maxTime = waitedTime;
      }
      reqTime.removeFirst(); // remove the time stored when the customer assigned resquested a chat service
   }

   /**
   * Description: Ends the chat service between a customer and a representative. Also checks 
   *              if there is anyone on the hold line. If there is, assign the first person on
   *              hold with the new available representative.
   *
   * @param    input
   * @return   nothing
   * @throws ParseException
   * @throws NumberFormatException
    **/
   public static void endChat (Scanner input) throws NumberFormatException, ParseException {
      final String customer = input.next();
      available.addLast(input.next());
      final String currTime = input.next();
      busy.deleteElem(available.last());

      System.out.printf(format4, "ChatEnded", customer, available.last(), currTime);

      // if a customer still on hold, assign the customer to a representative
      if (hold.size() > 0) {
         updateOnHold(currTime);
      }
   }

   /**
   * Description: Prints the maximum waiting time for a customer to be assigned to a representative.
   *
   * @param    input
   * @return   nothing
   **/
   public static void printMaxWaitTime (Scanner input) {
      final String currTime = input.next();

      if (maxTime == 0) {  // Do not print anything if wait time is 0
         System.out.printf(format2, "MaxWaitTime", currTime);
         return;
      }
      System.out.printf(format3, "MaxWaitTime", currTime, String.valueOf(maxTime));
   }

   /**
   * Description: Calculate the maximum waiting time for a customer to be assigned to a representative.
   *              Return the maximun waiting time as a String.
   *
   * @param    curr,req
   * @return   String
   * @throws ParseException
    **/
   public static String totalWaitTime(String curr, String req) throws ParseException {
      Date stored = format.parse(req);
      Date current = format.parse(curr);

      long diff = current.getTime() - stored.getTime();  // Calculate max waiting time
      long min =  diff / (60 * 1000) % 60;               // Special case: base 60
      long hours = diff / (60 * 60 * 1000) % 24;         // Special case: base 60

      if (hours < 1) {  // Print just the minutes if the max waiting time was less the one hour
         return String.valueOf(min);
      }

      return String.valueOf(hours) + String.valueOf(min);
   }
   /* -------------------------------- End Methods ------------------------------------ */
}
