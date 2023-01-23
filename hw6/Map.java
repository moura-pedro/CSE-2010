import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Map {
   
   /* Map Fields */
   private Node root;
   private Node human;
   private List<Node> zombies;
   /* End Map Fields */

   /* Map Constructor */
   Map (Node root) {
      this.root = root;
      zombies = new ArrayList<>();
   }
   /* End Map Constructor */

   /* Access Methods */
   public Node getRoot() { return this.root; }
   public Node getHuman() { return this.human; }
   public List<Node> getZombies() { return this.zombies; }
   /* End Access Methods */

   /* Modify Methods */
   public void setRoot(Node root) { this.root = root; }
   public void setHuman(Node human) { this.human = human; }
   public void addZombie(Node newZombie) { this.zombies.add(newZombie); }
   /* Modify Methods */


   public void printPath(List<Node> list) {
      for (Node node : list) {
         System.out.println(node.getCoordinates());
      }
   }

   /* Print all map's elements */
   public void printMap () {
      Node currLine = this.root;
      Node currNode = currLine;

      // For indentation reasons
      int count = 0;

      // Prints x indexs
      System.out.print(" ");
      while (currNode != null) {
         System.out.print(count);
         count++;
         currNode = currNode.getNext();
      }

      // Reset count
      count = 0;
      System.out.println();

      // Prints each elements from the map
      while (currLine != null) {
         // possition of y index
         System.out.print(count);

         currNode = currLine;
         while (currNode != null) {
            System.out.print(currNode.getData());
            currNode = currNode.getNext();
         }

         System.out.println();
         currLine = currLine.getBelow();
         count++;
      }
   }

   // TODO { Sorry, Josias... I've failed you :( }
   public Stack<Node> getPath(Node start) {
      Queue<Node> nodes = new ArrayDeque<>();
      List<Node> visited = new ArrayList<>();
      List<Node> path = new ArrayList<>();
      Stack<Node> newPath = new Stack<>();

      nodes.offer(start);
      while (!nodes.isEmpty()) {
         Node curr = nodes.poll();
         if (!visited.contains(curr)) {
            if (curr == this.human) {
               return newPath;
            }
            visited.add(curr);
            List<Node> neighbors = curr.getNeighbors();
            for (Node n : neighbors) {
               if (!visited.contains(n) && !n.getData().equals("#")) {
                  newPath.push(curr);
                  nodes.offer(n);
               }
            }
            newPath.pop();
         }
      }
      return newPath;
   }


}
