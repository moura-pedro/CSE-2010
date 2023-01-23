/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 12
   Description of this file: Generic SkipList

*/
public class SkipList<E> {
   
   /* SkipNode Class */
   public class SkipNode<E> {
      private int key;
      private E value;

      private SkipNode<E> next;
      private SkipNode<E> prev;
      private SkipNode<E> above;
      private SkipNode<E> below;

      /* SkipNode Constructors */
      public SkipNode (int k, E e) {
         key = k;
         value = e;

         prev = null;
         next = null;
         above = null;
         below = null;
      }

      public SkipNode (int k, E e, SkipNode<E> p, SkipNode<E> n) {
         key = k;
         value = e;

         prev = p;
         next = n;
         above = null;
         below = null;
      }

      public SkipNode(SkipNode<E> node) {
         key = node.getKey();
         value = node.getValue();

         prev = null;
         next = null;
         above = null;
         below = node;
     }


      public SkipNode (int k, E e, SkipNode<E> p, SkipNode<E> n, SkipNode<E> a, SkipNode<E> b) {
         key = k;
         value = e;

         next = n;
         prev = p;
         above = a;
         below = b;
      }
      /* End Constructors */


      /* Access & Modify fields */
      public int getKey() { return key; }
      public E getValue() { return value; }
      public SkipNode<E> getNext() { return next; }
      public SkipNode<E> getPrev() { return prev; }
      public SkipNode<E> getAbove() { return above; }
      public SkipNode<E> getBelow() { return below; }

      public void setKey(int k) { key = k; }
      public void setValue(E e) { value = e; }
      public void setNext(SkipNode<E> n) { next = n; }
      public void setPrev(SkipNode<E> p) { prev = p; }
      public void setAbove(SkipNode<E> a) { above = a; }
      public void setBelow(SkipNode<E> b) { below = b; }
      
   }
   /* End SkipNode */

   /* SkipList fields */
   private SkipNode<E> header;
   private SkipNode<E> trailer;
   private int numOfLevels;
   private FakeRandHeight coinToss = new FakeRandHeight();
   
   /* SkipList Constructors */
   public SkipList() {
      header = new SkipNode(Integer.MIN_VALUE, null);
      trailer = new SkipNode(Integer.MAX_VALUE, header);
      header.setNext(trailer);
      numOfLevels = 0;
   }
   /* End Constructors */


   /* --------------- SkipList Methods --------------- */
   /**
    * insert an "entry" in the skiplist
    * @param key 
    * @param value
    * @return return node inserted
    */
   public SkipNode<E> insert(int key, E value) {
      SkipNode<E> currNode = find(key);
      SkipNode<E> tempNode = null;
      int currHeight = -1;
      
      // check if there is something in that key spot
      if (currNode.getKey() == key) {
         while(currNode != null) {
            currNode.setValue(value);
            currNode = currNode.getAbove();
         }
         return currNode;
      }
      
      // pseudorandom heigth
      int height = coinToss.get();

      // go through levels
      for (int i = 0; i <= height; i++) {
         currHeight++;
         if (currHeight >= numOfLevels) {
            numOfLevels++;
            
            // new list to set above
            SkipNode<E> newHeader = new SkipNode<>(Integer.MIN_VALUE, null);
            SkipNode<E> newTrailer = new SkipNode<>(Integer.MAX_VALUE, null);

            // update list pointers to after adding new list above
            updateAbove(newHeader, null, header);
            header = newHeader;
            updateAbove(newTrailer, header, trailer);
            trailer = newTrailer;
         }
         // actually insert
         SkipNode<E> valueNode = new SkipNode<>(key, value);
         updateAbove(valueNode, currNode, tempNode);
         tempNode = valueNode;

         while(currNode.getAbove() == null) {
            currNode = currNode.getPrev();
         }
         currNode = currNode.getAbove();
      }
      return tempNode;
  }

  /**
   * update the list above when insert new entry in the list
   * @param newNode node inserted
   * @param prevNode node to set pointers to prev node
   * @param nodeBelow node to set pointers to below node
   */
  public void updateAbove(SkipNode<E> newNode, SkipNode<E> prevNode, SkipNode<E> nodeBelow) {
      if(prevNode != null) {
         newNode.setNext(prevNode.getNext());
         if(prevNode.getNext() != null) {
            prevNode.getNext().setPrev(newNode);
         }
         prevNode.setNext(newNode); 
         newNode.setPrev(prevNode);
      }
      
      if(nodeBelow != null) {
         nodeBelow.setAbove(newNode);
         newNode.setBelow(nodeBelow);
      }
   }

   /**
    * prints all entries on the skipList
    */
   public void printAllEntries() {
      SkipNode<E> temp = header;
      
      // move down until nulll
      while(temp.getBelow() != null) {
         temp = temp.getBelow();
      }
     
      // get rid of the -(1/0)
      temp = temp.getNext();

      if(temp.getValue() == null) {
         System.out.print(" none");
      }
      
      // move right until find its value is less then next
      while (temp.getKey() != Integer.MAX_VALUE) {
         System.out.print(String.format(" %04d", temp.getKey()) + ":" + temp.getValue());
            temp = temp.getNext();
         }
   
      System.out.println();

   }

   /**
    *  "get rid" of the empty lists
    * @return number of list with actual content
    */
   public int funkyEmptyListCheck() {
      SkipNode<E> currNode = header;
      SkipNode<E> highestLevel = currNode;
      int count = 0;

      while(highestLevel != null) {
         boolean check = false;
         while(currNode != null) {
            if(currNode.getValue() != null && currNode.getKey() != Integer.MAX_VALUE) {
               check = true;
            }
            currNode = currNode.getNext();
         }      
         if (check) {count++;}
         highestLevel = highestLevel.getBelow();
         currNode = highestLevel;
      }
      return count;
   }
   

   /**
    * Print the whole SkipList
    */
   public void printSkipList() {
      StringBuilder sb = new StringBuilder();

      SkipNode<E> currNode = header;
      SkipNode<E> highestLevel = currNode;
      int level = numOfLevels;

      System.out.println("(S" + funkyEmptyListCheck() + ")" + " empty");

      for (int i = 0; i < numOfLevels - funkyEmptyListCheck() + 1; i++) {
         highestLevel = highestLevel.getBelow();
         currNode = highestLevel;
         level--;
      }

      while(highestLevel != null) {
         //appends the level of the list
         sb.append("(S" + level + ")");
         
         // appends all the entries of that level
         while(currNode != null) {
            if(currNode.getValue() != null && currNode.getKey() != Integer.MAX_VALUE) {
               sb.append(String.format(" %04d", currNode.getKey()) + ":" + currNode.getValue());
            }
            currNode = currNode.getNext();
         }
         sb.append("\n");
         
         // update level and node
         highestLevel = highestLevel.getBelow();
         currNode = highestLevel;
         level--;
      }
      System.out.print(sb.toString());
   }
      

   /**
    * Find node by the key
    * @param key
    * @return node or null
    */
   public SkipNode<E> find(int key) {
      SkipNode<E> curr = header;

      while (curr.getBelow() != null) {
         curr = curr.getBelow();

         while (curr.getNext().getKey() <= key) {
            curr = curr.getNext();
         }
      }
      return curr;
   }

   /**
    * Delete node in the skiplist that contains this key
    * @param key
    * @return true if was deleted; otherwise, false
    */
   public boolean delete(int key) {
      SkipNode<E> curr = find(key);

      if (curr.getKey() != key) {
         return false;
      }

      while (curr != null) {
         SkipNode<E> prev = curr.getPrev();
         SkipNode<E> next = curr.getNext();

         prev.setNext(next);
         next.setPrev(prev);
         curr = curr.getAbove();
      }
      return true;
   }

   /**
    * prints all entries from the start until key
    * @param key
    */
   public void fromStartToKey(int key) {
      SkipNode<E> currNode = header;

      // move all the way to tghe bottom list
      while(currNode.getBelow() != null) {
         currNode = currNode.getBelow();
      }
      currNode = currNode.getNext();

      SkipNode<E> nextNode = find(key).getNext();
      
      // check if there is anything in that list
      if(currNode.getValue() == null) {
         System.out.print("none");
      }

      while(currNode.getKey() != nextNode.getKey()) {
         System.out.printf(" %04d:%s", currNode.getKey(), currNode.getValue());
         currNode = currNode.getNext();
      }
      System.out.println();
   }

   /**
    * prints every entries starting from key to the end
    * @param key
    */
   public void fromKeytoEnd(int key) {
      // find key node to start
      SkipNode<E> currNode = find(key);
   
      if(currNode.getNext().getValue() == null) {
         System.out.print(" none");
      }
      
      // move next until reach the end of the list
      while(currNode.getKey() != Integer.MAX_VALUE) {
         System.out.printf(" %04d:%s", currNode.getKey(), currNode.getValue());
         currNode = currNode.getNext();
      }
      System.out.println();
   }

   /**
    * prints every entries starting from key1 to the key2
    * @param k1
    * @param k2
    */
   public void fromKey1ToKey2(int k1, int k2) {
      // find nodes with k1 and k2 (start and end, respectively)
      SkipNode<E> key1 = find(k1).getNext();
      SkipNode<E> key2 = find(k2).getNext();

      if(key1.getNext().getKey() > key2.getKey()) {
          System.out.print(" none");
      }

      // check if k1 haven still < k2
      while(key1.getKey() != key2.getKey()) {
           System.out.printf(" %04d:%s", key1.getKey(), key1.getValue());
           key1 = key1.getNext();
       }
       System.out.println();
   }
   /* --------------- End SkipList Methods --------------- */

}
