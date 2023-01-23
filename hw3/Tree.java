import java.util.ArrayList;
import java.util.List;

public class Tree<E> {
   // Root node of the tree
   private Node<E> root;

   // Construct the tree
   public Tree(Node<E> root) { this.root = root; }

   public Node<E> getRoot() { return this.root; }

   public void setRoot(Node<E> node) {
      this.root = node;
   }

   // Search for a key node and return it
   public Node<E> findNode(Node<E> node, E keyNode) {
      if (node == null) { return null; }

      if (node.getData().equals(keyNode)) {
         return node;
      } else {
         Node<E> temp = null;
         for (Node<E> child : node.getChildren()) {
            if ((temp = findNode(child, keyNode)) != null) {
               return temp;
            }
         }
      }
      return null;
   }

   // Add a child node in a specific node
   public void addChildAt(Node<E> root, E parent, Node<E> child) {
      Node<E> tempParent = findNode(root, parent);
      tempParent.addChild(child);
   }

   // From a node, traverse the tree by the node and return a list of the nodes visites in Pre Order
   public List<Node<E>> preOrderTraverse(Node<E> node) {
      List<Node<E>> preOrder = new ArrayList<Node<E>>();
      preOrder(node, preOrder);
      return preOrder;
   }

   // Traverse a a tree in Pre Order
   private void preOrder(Node<E> node, List<Node<E>> preOrder) {
      preOrder.add(node);
      for (Node<E> child : node.getChildren()) {
         preOrder(child, preOrder);
      }
   }

}
