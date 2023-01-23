import java.util.ArrayList;
import java.util.List;

public class Node {

   /* Node Fields */
   private String data;

   private int x, y;
   private String coordinates;

   private Node prev;
   private Node next;
   private Node above;
   private Node below;
   /* End Node Fields */

   /* Node Constructors */
   Node (String data, int y, int x) {
      this.data = data;
      this.y = y;
      this.x = x;
      this.coordinates = "(" + x + "," + y + ")";
      this.prev = null;
      this.next = null;
      this.above = null;
      this.below = null;
   }

   Node (String data, Node prev, int y, int x) {
      this.data = data;
      this.y = y;
      this.x = x;
      this.coordinates = "(" + x + "," + y + ")";
      this.prev = prev;
      this.next = null;
      this.above = null;
      this.below = null;
   } /* End Node Constructors */


   /* Access Methods */
   public String getData() { return this.data; }
   public int getX() { return this.x; }
   public int getY() { return this.y; }
   public String getCoordinates() { return this.coordinates; }
   public Node getPrev() { return this.prev; }
   public Node getNext() { return this.next; }
   public Node getAbove() { return this.above; }
   public Node getBelow() { return this.below; }
   public List<Node> getNeighbors() {
      List<Node> neighbors = new ArrayList<>();
      if (this.above != null) {
         neighbors.add(this.above);
      }
      if (this.below != null) {
         neighbors.add(this.below);
      }
      if (this.prev != null) {
         neighbors.add(this.prev);
      }
      if (this.next != null) {
         neighbors.add(this.next);
      }
      return neighbors;
   }
   /* End Access Methods */

   /* Modify Methods */
   public void setData(String data) { this.data = data; }
   public void setX(int x) { this.x = x; }
   public void setY(int y) { this.y = y; }
   public void setCoordinates(String coord) { this.coordinates = coord; }
   public void setPrev(Node prev) { this.prev = prev; }
   public void setNext(Node next) { this.next = next; }
   public void setAbove(Node above) { this.above = above; }
   public void setBelow(Node below) { this.below = below; }
   /* End Modify Methods */

   
   /**
    * Function for debug purpose
    */
   public void debug() { 
      System.out.println("Data : " + data);
      System.out.println("Coord : " + coordinates);
      System.out.println("Prev : " + prev);
      System.out.println("Next : " + next);
      System.out.println("Above : " + above);
      System.out.println("Below : " + below);
   }

}
