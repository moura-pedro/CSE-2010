/*

  Author: Tyler Ton
  Email: tton2021@my.fit.edu
  Course: CSE 2010
  Section: 04
  Description of this file:
  Skip list class that has the skiplist 
  class and node class. Also includes
  other methods that implements
  the commands from the main 
  file.

 */

public class Skiplist {
    private Node header;
    private Node trailer;
    private int heightSkip = 0;

    // creating the instance of randHeight
    public FakeRandHeight randHeight = new FakeRandHeight();


    // Constructs a new empty list
    public Skiplist() {
        header = new Node(Integer.MIN_VALUE, null, null, null, null, null);      
        trailer = new Node(Integer.MAX_VALUE, null, null, header, null, null);
        header.setNext(trailer);           
      }
    
    // node class
    public static class Node {
        private int date;
        private String event;

        private Node next;
        private Node previous;
        private Node above;
        private Node below;
        
        public Node(int d, String e, Node n, Node p, Node a, Node b) {
            date = d;
            event = e;
            next = n;
            previous = p;
            above = a;
            below = b;
          }
        // access methods
        public Node getNext() { return next; }
        public Node getPrevious() { return previous; }
        public Node getAbove() { return above; }
        public Node getBelow() { return below; }
        public int getDate(){ return date; }
        public String getEvent() {return event; }
        // setter methods
        public void setNext(Node n) { next = n; }
        public void setPrev(Node p) { previous = p; }
        public void setAbove(Node a) { above = a; }
        public void setBelow(Node b) {below = b;}
        public void setDate(int d) { date = d; }
        public void setEvent(String e) { event = e; }
        
    }
    // search for a key, returning a node
    public Node searchValue(int date) {
        Node n = header;
        // keep going down until we can't
        while (n.getBelow()!= null) {
            n = n.getBelow();
            // if next key is less then we move forward
            while(date >= n.getNext().getDate()) {
                n = n.getNext();
            }
        }
       
        return n;
    }

    public Node SkipInsert(int date, String event) {
        Node pos = searchValue(date);

        Node currNode = null;
        int currHeight = -1;
        
        // best case scenario
        if (pos.getDate() == date) {
            while(pos != null) {
                pos.setEvent(event);
                pos = pos.getAbove();
            }
            return pos;

        }
        
        // grab the height that is calculated randomly
        int height = randHeight.get();

        // iterates again for levels of the height
        for (int x = 0; x <= height; x++) {
            currHeight++;
            if (currHeight >= heightSkip) {
                heightSkip++;
                
                // creating new header and trailer node to be inserted
                Node negInfinity = new Node(Integer.MIN_VALUE, null, null, null, null, null);
                Node posInfinity = new Node(Integer.MAX_VALUE, null, null, null, null, null);

                // inserting new level with neg infinity as header
                insertAfterAbove(null, header, negInfinity);
                header = negInfinity;
                //inserting new level with pos infinity as header
                insertAfterAbove(header, trailer, posInfinity);
                trailer = posInfinity;
            }
            // adding actual new node with values in 
            Node valueNode = new Node(date, event, null, null, null, null);
            insertAfterAbove(pos, currNode, valueNode);
            currNode = valueNode;

            while(pos.getAbove() == null) {
                // scans backwards
                pos = pos.getPrevious();
            }
            // jumps to higher level
            pos = pos.getAbove();
        }

        return currNode;
    }

    public void insertAfterAbove(Node nodeBefore, Node nodeBelow, Node newnode) {
        // if there is a value for node before
        if(nodeBefore != null) {
            newnode.setNext(nodeBefore.getNext());
            if(nodeBefore.getNext() != null) {
            //saying that this is null
            nodeBefore.getNext().setPrev(newnode);
            }

            nodeBefore.setNext(newnode); 
            newnode.setPrev(nodeBefore);
        }
        // if node before equals to null
        if(nodeBelow != null) {
            nodeBelow.setAbove(newnode);
            newnode.setBelow(nodeBelow);
        }
    }

    public Boolean removeNode(int date) {
        Node removeNode = searchValue(date);
        // boolean check if date exist or not
        if(removeNode.getDate() != date) {
            return false;
        }
        while (removeNode != null) {
            resetremoveNreferences(removeNode);
            removeNode = removeNode.getAbove();
        }
        return true;
    }

    // references get changed
    public void resetremoveNreferences(Node removedNode) {
        Node afterRNode = removedNode.getNext();
        Node beforeRNode = removedNode.getPrevious();

        beforeRNode.setNext(afterRNode);
        afterRNode.setPrev(beforeRNode);

    }

    public void printSkipList() {
        StringBuilder sb = new StringBuilder();

        Node currNode = header;
        Node highestLevel = currNode;
        int level = heightSkip;

        while(highestLevel != null) {
            sb.append("(S" + level + ") ");
            
            while(currNode != null) {
                // if current node points to trailer on first level, it is empty
                if(currNode.getNext() == trailer) {
                    sb.append("empty");
                }
                if(currNode.getEvent() != null) {
                    sb.append(String.format("%04d", currNode.getDate()));
                    sb.append(":");
                    sb.append(currNode.getEvent() + " ");
                }
                currNode = currNode.getNext();
            }
                sb.append("\n");
            
            highestLevel = highestLevel.getBelow();
            currNode = highestLevel;
            level--;
        }
        // prints out everything after all the appends
        System.out.print(sb.toString());
    
    }


    public void displayAllEvents() {
        Node currNode = header;
        //goes to bottom most layer
        while(currNode.getBelow() != null) {
            currNode = currNode.getBelow();
        }
        // skips the first negInfinity node
        currNode = currNode.getNext();

        System.out.print("DisplayAllEvents ");

        if(currNode.getEvent() == null) {
            System.out.print(" none");
        }
        // iterates through last row until we reach the end
        while (currNode.getDate() != Integer.MAX_VALUE) {
            System.out.print(String.format("%04d", currNode.getDate()) + ":" + currNode.getEvent() + " ");
            currNode = currNode.getNext();

            }
        
        System.out.println();

    }

    public void displayEventsFromStartDate(int date) {
        // find out where we are starting
        Node value = searchValue(date);

        System.out.print("DisplayEventsFromStartDate " + String.format("%04d",date) + " ");
        
        // checker
        if(value.getNext().getEvent() == null) {
            System.out.print(" none");
        }
        // start iterating and printing all values after 
        while(value.getDate() != Integer.MAX_VALUE) {
    
            System.out.print(String.format("%04d", value.getDate()) + ":" + value.getEvent() + " ");
            value = value.getNext();
        }
        System.out.println();
    }

    public void DisplayEventsBetweenDates(int s, int e) {
        // search for position of begin and end dates
        Node value = searchValue(s).getNext();
        Node end = searchValue(e).getNext();
 
        System.out.print("DisplayEventsBetweenDates " + String.format("%04d", s) 
            + " " + String.format("%04d", e) + " ");

        if(value.getNext().getDate() > end.getDate()) {
            System.out.print(" none");
        }

        // loops through and prints everything in between the interval
        while(value.getDate() != end.getDate()) {
             System.out.print(String.format("%04d", value.getDate()) + ":" + value.getEvent() + " ");
             value = value.getNext();
         }

         //System.out.print(" none");
         System.out.println();
     }

     public void displayEventstoEndDate(int date) {

         System.out.print("DisplayEventsToEndDate " + String.format("%04d", date) + " ");

         Node currNode = header;

         //goes to bottom most layer
         while(currNode.getBelow() != null) {
             currNode = currNode.getBelow();
         }
         // skips the first node
         currNode = currNode.getNext();
         // get position of end date
         Node value = searchValue(date).getNext();
         
         // checker
         if(currNode.getEvent() == null) {
            System.out.print("none");
        }
 
        // iterates until position of end date
        while(currNode.getDate() != value.getDate()) {
             System.out.print(String.format("%04d", currNode.getDate()) + ":" + currNode.getEvent() + " ");
             currNode = currNode.getNext();
         }
         System.out.println();
     }
    
}
