public class SinglyLinkedList<E> {

   // Node Class
   public static class Node<E> {
      private E element;      // Reference to the element stored at this node
      private Node<E> next;   // Reference to the subsequent node in the list

      // Construct Node
      public Node(E e, Node<E> n) {
         element = e;
         next = n;
      }

      // Node methods
      public E getElement() { return element; }
      public Node<E> getNext() { return next; }
      public void setNext(Node<E> n) { next = n; }
      // End Node methods

   }
   // End Node Class
   
   // Instance variables of the SinglyLinkedList
   private Node<E> head = null;  // Head node of the list
   private Node<E> tail = null;  // Tail node of the list
   private int size = 0;         // Size of the list
   public SinglyLinkedList() {}  // Constructs an initially empty list

   // Access list methods
   public int size() { return size; }
   public boolean isEmpty() { return size == 0; }

   public E first() {                  // Return the first element
      if (isEmpty()) { return null; }
      return head.getElement();
   }

   public E last() {                   // Return the last element
      if (isEmpty()) { return null; }
      return tail.getElement();
   }

   public E findElement(int i) {       // Return the i'th element
      if (isEmpty() && size <= i) { return null; }
      Node<E> elem = head;

      for (int j = 0; j < i; j++) {    // find i'th element
         elem = elem.getNext();
      }
      return elem.getElement();
   }   

   public String allElements () {      // Return an string with all elements from the list
      if (isEmpty()) { return null; }

      Node<E> temp = head;                            // Create a temporary node to interate through list
      String allElem = temp.getElement().toString();  // Variable to store all list elements
      temp = temp.getNext();                          // Move to the next element

      while (temp != null) {                             // Keep moving to next elem until elem == null
         allElem += " " + temp.getElement().toString();  // 
         temp = temp.getNext();                          // Move to the next element
      }
      return allElem;
   }

   // Update list methods
   public void addFirst(E e) {
      head = new Node<>(e, head);   // Create and link a new node
      if (isEmpty()) {              // Special case: new node becomes tail also
         tail = head;
      }
      size++;
   }

   public void addLast(E e) {
      Node<E> newest = new Node<>(e, null);  // Create a new node
      if (isEmpty()) {                          // Special case: previous empty list
         head = newest;
      } else {
         tail.setNext(newest);                  // New node after existing a tail
      }
      tail = newest;                            // New node becomes tail
      size++;
   }

   public E removeFirst() {
      if (isEmpty()) { return null; }        // Nothing to remove
      E firstElem = head.getElement();
      head = head.getNext();                 // Head will become null if list had only one node
      size--;
      if (size == 0) {
         tail = null;                        // Special case as list now empty
      }
      return firstElem;                      // Return first element
   }

   public int deleteElem(E e) {
      if (isEmpty()) { return -1; }

      Node<E> temp = head;
      Node<E> prev = null;
      int index = 0;

      if (temp != null && temp.getElement().equals(e)) {    // if the element is in the head
         head = temp.getNext();
         size--;
         return 0;
      }

      while (temp != null && !temp.getElement().equals(e)) {     // Search for the element
         prev = temp;
         temp = temp.getNext();
         index++;
      }

      if (temp == null) {     // Hopped the whole list, and did not find element
         return -1;
      }

      // Make point skip the next element so it removes the element in between.
      prev.setNext(temp.next);
      size--;
      return index;
   }
}
