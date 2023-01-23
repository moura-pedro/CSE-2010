import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 12
   Description of this file: manage the timeline and allow the user to specify a time range to display/share his/her major events.

*/

public class HW5 {

   public static void main(String[] args) throws FileNotFoundException {
      // Creates list with dates:events
      SkipList<String> list = new SkipList<>();

      // Get user inputs
      Scanner input = new Scanner(new FileInputStream(args[0]));

      // manage the time line of events
      while (input.hasNext()) {
         String action = input.next();
         int date; 
         String event;
         
         switch (action) {
            case "DisplayEvent": // DisplayEvent date event/none
               date = input.nextInt();
               event = list.find(date).getValue();

               if (event != null) {
                  System.out.printf("DisplayEvent %04d %s\n", date, event);
               } else {
                  System.out.printf("DisplayEvent %04d\n", date);
               }
               break;

            case "AddEvent":  // AddEvent date event success
               date = input.nextInt();
               event = input.next();
               list.insert(date, event);
               System.out.printf("AddEvent %04d %s success\n", date, event);
               break;

            case "DeleteEvent":  // DeleteEvent date success/noDateError
               date = input.nextInt();
               if (list.delete(date)) {
                  System.out.printf("DeleteEvent %04d success\n", date);
               } else {
                  System.out.printf("DeleteEvent %04d noDateError\n", date);
               }
               break;

            case "DisplayEventsBetweenDates":   // DisplayEventsBetweenDates startDate endDate date1:event1 ... or none
               int date1 = input.nextInt();
               int date2 = input.nextInt();
               System.out.printf("DisplayEventsBetweenDates %04d %04d", date1, date2);
               list.fromKey1ToKey2(date1, date2);
               break;

            case "DisplayEventsFromStartDate":  // DisplayEventsFromStartDate startDate date1:event1 ... or none
               date = input.nextInt();
               System.out.printf("DisplayEventsFromStartDate %04d", date);
               list.fromKeytoEnd(date);
               break;

            case "DisplayEventsToEndDate":   // DisplayEventsToEndDate endDate date1:event1 ... or none
               date = input.nextInt();
               System.out.printf("DisplayEventsToEndDate %04d", date);
               list.fromStartToKey(date);
               break;

            case "DisplayAllEvents":   // DisplayAllEvents date1:event1 ... or none
               System.out.print("DisplayAllEvents");
               list.printAllEntries();
               break;

            case "PrintSkipList": // PrintSkipList (Sh) entries ...
            
               System.out.println("PrintSkipList");
               list.printSkipList();
               break;
         
            default:
               break;
         }
      }
   }
}
