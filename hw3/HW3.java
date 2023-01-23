/*

   Author: Pedro Moura
   Email: pmoura2020@my.fit.edu
   Course: CSE 2010
   Section: 2
   Description of this file: Build a tree from organizational data and answer queries on the organizational structure

*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HW3 {

   public static void main(String[] args) throws FileNotFoundException {

      // Get files name from args
      final String orgsDataFile = args[0];
      final String queriesFile = args[1];

      // Output format
      final String format3Out = "%s %s %s\n";
      final String format4Out = "%s %s %s %s\n";

      // Read queries file
      Scanner queries = new Scanner(new FileInputStream(queriesFile));

      // Create and set up the organizational tree
      Tree<String> orgTree = setUpTree(orgsDataFile);

      // Answer queries on the organizational structure
      while (queries.hasNext()) {
         String query = queries.next();      // Get query
         String[] entity = new String[2];    // Store entities

         switch (query) {
            case "DirectSupervisor":
               entity[0] = queries.next();   // Get entity
               System.out.printf(format3Out, query, entity[0], getDirectSupervisor(orgTree, entity[0]));
               break;

            case "DirectSubordinates":
               entity[0] = queries.next();   // Get entity
               String subordinates = convertListToString(getDirectSubordinates(orgTree, entity[0]));
               System.out.printf(format3Out, query, entity[0], subordinates);
               break;

            case "AllSupervisors":
               entity[0] = queries.next();   // Get entity
               String supervisors = convertListToString(getAllSupervisors(orgTree, entity[0]));
               System.out.printf(format3Out, query, entity[0], supervisors);
               break;
   
            case "AllSubordinates":
               entity[0] = queries.next();   // Get entity
               String allSubordinates = convertListToString(getAllSubordinates(orgTree, entity[0]));
               System.out.printf(format3Out, query, entity[0], allSubordinates);
               break;

            case "NumberOfAllSupervisors":
               entity[0] = queries.next();   // Get entity
               int numOfSup = getNumberOfAllSupervisors(orgTree, entity[0]);
               System.out.printf(format3Out, query, entity[0], numOfSup);
               break;

            case "NumberOfAllSubordinates":
               entity[0] = queries.next();   // Get entity
               int numOfSub = getNumberOfAllSubordinates(orgTree, entity[0]);
               System.out.printf(format3Out, query, entity[0], numOfSub);
               break;

            case "IsSupervisor":
               entity[0] = queries.next();   // Get entity1
               entity[1] = queries.next();   // Get entity2
               String isSupervisor = isSupervisor(orgTree, entity[0], entity[1]);
               System.out.printf(format4Out, query, entity[0], entity[1], isSupervisor);
               break;

            case "IsSubordinate":
               entity[0] = queries.next();   // Get entity1
               entity[1] = queries.next();   // Get entity2
               String isSubordinate = isSubordinate(orgTree, entity[0], entity[1]);
               System.out.printf(format4Out, query, entity[0], entity[1], isSubordinate);
               break;
            
            case "CompareRank":
               entity[0] = queries.next();   // Get entity1
               entity[1] = queries.next();   // Get entity2
               String rank = compareRank(orgTree, entity[0], entity[1]);
               System.out.printf(format4Out, query, entity[0], entity[1], rank);
               break;

            case "ClosestCommonSupervisor":
               entity[0] = queries.next();   // Get entity1
               entity[1] = queries.next();   // Get entity2
               String closestSup = closestCommonSupervisor(orgTree, entity[0], entity[1]);
               System.out.printf(format4Out, query, entity[0], entity[1], closestSup);
               break;

            default:
               break;
         }
      }
      
      
      
   }


   // ---------------------------- Methods ---------------------------- //

   /*
      Create and set up the from according from a input list

      @param filename
      @return the setted up tree
   */
   public static Tree<String> setUpTree (String file) throws FileNotFoundException {
      Scanner orgsData = new Scanner(new FileInputStream(file));

      Node<String> root = new Node<>(orgsData.next());
      Tree<String> tree = new Tree<>(root);

      while (orgsData.hasNext()) {
         String parent = orgsData.next();
         String child = orgsData.next();
         tree.addChildAt(root, parent, new Node<>(child));
      }
      orgsData.close();
      return tree;
   }


   /*
      Find entity node in the tree and gets it's supervisor(parent)

      @params tree and entity to find the node on the tree
      @return entitiy's node parent data
   */
   public static String getDirectSupervisor (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      return entityNode.getParent().getData();
   }


   /*
      Find entity node in the tree and gets it's subordinate(children)

      @params tree and entity to find the node on the tree
      @return entitiy's node children data list
   */
   public static List<String> getDirectSubordinates (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      return convertNodeToData(entityNode.getChildren());
   }


   /*
      Find entity node in the tree and gets it's all supervisors

      @params tree and entity to find the node on the tree
      @return entitiy's list of supervisors
   */
   public static List<String> getAllSupervisors (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> supervisors = new ArrayList<Node<String>>(); 

      traverseUpRec(entityNode, supervisors);
      supervisors.remove(0);

      return convertNodeToData(supervisors);
   }


   /*
      Find entity node in the tree and get it's all subordinates

      @params tree and entity to find the node on the tree
      @return entitiy's list of all subordinates
   */
   public static List<String> getAllSubordinates (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> subordinates = new ArrayList<Node<String>>(); 

      subordinates = tree.preOrderTraverse(entityNode);
      subordinates.remove(0);
      return convertNodeToData(subordinates);
      
   }


   /*
      Find entity node in the tree and count it's all supervisors

      @params tree and entity to find the node on the tree
      @return number of supervisors
   */
   public static int getNumberOfAllSupervisors (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> supervisors = new ArrayList<Node<String>>(); 

      traverseUpRec(entityNode, supervisors);
      supervisors.remove(0);
      
      return supervisors.size();
   }


   /*
      Traverse from a node to the root

      @params node to start the seach list to store visited nodes
      @return number of supervisors
   */
   public static void traverseUpRec (Node<String> node, List<Node<String>> supervisors) {
      if (node == null) { return; }
      supervisors.add(node);
      traverseUpRec(node.getParent(), supervisors);
   }


   /*
      Find entity node in the tree and count it's all subordinates

      @params tree and entity to find the node on the tree
      @return number of subordinates
   */
   public static int getNumberOfAllSubordinates (Tree<String> tree, String entity) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> subordinates = new ArrayList<Node<String>>(); 

      subordinates = tree.preOrderTraverse(entityNode);
      subordinates.remove(0);

      return subordinates.size();
   }


   /*
      Check if the a node is supervisor of an entity

      @params tree, entity to find the node on the tree, and supervisor
      @return yes if is supervisor, otherwise no
   */
   public static String isSupervisor (Tree<String> tree, String entity, String supervisor) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> supervisors = new ArrayList<Node<String>>(); 

      traverseUpRec(entityNode, supervisors);
      supervisors.remove(0);
      for (Node<String> node : supervisors) {
         if (node.getData().equals(supervisor)) {
            return "yes";
         }
      }
      return "no";
   }


   /*
      Check if the a node is subordinate of an entity

      @params tree, entity to find the node on the tree, and subordinate
      @return yes if is subordinate, otherwise no
   */
   public static String isSubordinate (Tree<String> tree, String entity, String subordinate) {
      Node<String> entityNode = tree.findNode(tree.getRoot(), entity);
      List<Node<String>> subordinates = entityNode.getChildren();

      for (Node<String> node : subordinates) {
         if (node.getData().equals(subordinate)) {
            return "yes";
         }
      }
      return "no";
   }


   /*
      Compare rank between entity1 and entity2

      @params tree, entity1 to find the node on the tree, and entity2
      @return higher (lower/same) if entity1 has a higher (lower/same) rank than entity2
   */
   public static String compareRank (Tree<String> tree, String entity1, String entity2) {
      int entity1Rank = getNumberOfAllSupervisors(tree, entity1);
      int entity2Rank = getNumberOfAllSupervisors(tree, entity2);

      if (entity1Rank < entity2Rank){
         return "higher";
      } else if (entity1Rank > entity2Rank) {
         return "lower";
      } else {
         return "same";
      }
   }


   /*
      Find the closest common supervisor between entity1 and entity2

      @params tree, entity1 to find the node on the tree, and entity2
      @return closest common supervisor
   */
   public static String closestCommonSupervisor (Tree<String> tree, String entity1, String entity2) {
      List<String> entity1Sup = getAllSupervisors(tree, entity1);
      List<String> entity2Sup = getAllSupervisors(tree, entity2);

      String closestCommonSupervisor = "";
      
      for (int i = 0; i < entity1Sup.size(); i++) {
         for (int j = 0; j < entity2Sup.size(); j++) {
            if (entity1Sup.get(i).equals(entity2Sup.get(j))) {
               return entity1Sup.get(i);
            }
         }
      }
      return closestCommonSupervisor;
   }


   /*
      Convert list of String into a string 

      @params list of String
      @return string with all elements from the list concatonated
   */
   public static String convertListToString (List<String> list) {
      if (list.size() == 0) { return ""; }
      if (list.size() == 1) { return list.get(0); }

      String s = list.get(0);
      list.remove(0);

      for (String elem : list) {
         s += " " + elem;
      }
      return s;
   }


   /*
      Convert list of Node data into a list of string 

      @params list of node
      @return list of string
   */
   public static List<String> convertNodeToData (List<Node<String>> nodeList) {
      List<String> dataList = new ArrayList<String>();

      for (Node<String> node : nodeList) {
         dataList.add(node.getData());
      }
      return dataList;
   }

}
