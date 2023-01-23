import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CS2010
   Section: 12
   Description of this file:

*/

public class HW6 {

   public static void main (String[] args) throws FileNotFoundException {
      
      String[][] test = getInput(args[0]);

      // Create and setup the graph
      List<Node> sub = genSubGraph(test);
      Map map = genMap(sub);

      map.printMap();
      

      // Prompt user for move
      Boolean validMove = moveHuman(map);
      while (validMove != true) {
         validMove = moveHuman(map);
      }
      map.printMap();

   }

   /* ------------------------- Methods ------------------------- */
   /**
    * Generate a subgraph, with no pointers on Y axis
    * @param input
    * @return  list of subgraphs
    */
   public static List<Node> genSubGraph (String[][] input) {
      List<Node> subList = new ArrayList<>();

      for (int i = 0; i < input.length; i++) {
         Node currLine = new Node(input[i][0], i, 0);
         Node currNode = currLine;
         for (int j = 0; j < input[0].length - 1; j++) {
            currNode.setNext(new Node(input[i][j+1], i, j+1));
            currNode.getNext().setPrev(currNode);
            currNode = currNode.getNext();
         }
         subList.add(currLine);
      }

      return subList;
   }

   /**
    * setup the map pointers
    * @param list
    * @return map
    */
   public static Map genMap (List<Node> list) {
      Map map = new Map(list.get(0));

      for (int i = 1; i < list.size() - 1; i++) {
         Node above = list.get(i-1);
         Node curr = list.get(i);
         Node below = list.get(i+1);

         while (curr != null) {
            curr.setAbove(above);
            curr.getAbove().setBelow(curr);
            curr.setBelow(below);
            curr.getBelow().setAbove(curr);

            if (curr.getData().equals("0")) {
               map.setHuman(curr);
            } else if (!curr.getData().equals(" ") && !curr.getData().equals("#") &&
                        !curr.getData().equals("*")){
               map.addZombie(curr);
            }
            
            curr = curr.getNext();
            above = above.getNext();
            below = below.getNext();
         }
      }
      
      return map;
   }

   /**
    * Get user input from a file
    * @param filename
    * @return return a matrix with the input data on it
    * @throws FileNotFoundException
    */
   public static String[][] getInput (String filename) throws FileNotFoundException {
      Scanner sc = new Scanner(new FileInputStream(filename));

      int rows = sc.nextInt();
      int columns = sc.nextInt();

      String inputLine = sc.nextLine();

      String[][] input = new String[rows][columns];

      for (int i = 0; i < rows; i++) {
         inputLine = sc.nextLine();
         for (int j = 0; j < columns; j++) {
            input[i][j] = Character.toString(inputLine.charAt(j));
         }
      }
      return input;
   }

   /**
    *  Move and update human in the map
    * @param map
    * @return movement was valid or not
    */
   public static boolean moveHuman(Map map) {
      
      Scanner sc = new Scanner(System.in);
      System.out.print("\nPlayer 0, please enter your move [u(p), d(own), l(elf), or r(ight)]: ");
      String direction = sc.next();

      Node humanNode = map.getHuman();
   
      if (direction.equals("u") && humanNode.getAbove() != null &&
         !humanNode.getAbove().getData().equals("#")) {
         humanNode.setData(" ");
         humanNode.getAbove().setData("0");
         map.setHuman(humanNode.getAbove());
         System.out.println();
         return true;

      } else if (direction.equals("d") && humanNode.getBelow() != null &&
                  !humanNode.getBelow().getData().equals("#")) {
         humanNode.setData(" ");
         humanNode.getBelow().setData("0");
         map.setHuman(humanNode.getBelow());
         System.out.println();
         return true;
         
      } else if (direction.equals("l") && humanNode.getPrev() != null &&
                  !humanNode.getPrev().getData().equals("#")) {
         humanNode.setData(" ");
         humanNode.getPrev().setData("0");
         map.setHuman(humanNode.getPrev());
         System.out.println();
         return true;
         
      } else if (direction.equals("r") && humanNode.getNext() != null &&
                  !humanNode.getNext().getData().equals("#")) {
         humanNode.setData(" ");
         humanNode.getNext().setData("0");
         map.setHuman(humanNode.getNext());
         System.out.println();
         return true;
         
      } else {
         return false;
      }
      
   }
}
