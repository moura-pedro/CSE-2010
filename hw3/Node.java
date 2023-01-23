import java.util.ArrayList;
import java.util.List;

public class Node<E> {
   private E data;
   private Node<E> parent;
   private List<Node<E>> children;

   // Constructors
   public Node (E data) {
      this.data = data;
      this.children = new ArrayList<>();
   }

   public Node (Node<E> node) {
      this.data = node.getData();
      children = new ArrayList<>();
   }

   // Methods
   public E getData () { return this.data; }
   public void setData(E data) { this.data = data; }
   public Node<E> getParent() { return this.parent; }
   public void setParent(Node<E> parent) { this.parent = parent; }
   public List<Node<E>> getChildren() { return this.children; }
   public void addChild (Node <E> child) {
      child.setParent(this);
      children.add(child);
   }

}
