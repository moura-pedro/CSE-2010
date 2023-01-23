/*

  Author: Pedro Moura
  Email: pmoura2020@my.fit.edu
  Course: CSE 2010
  Section: 01
  Description of this file: A multi-word palindromes finder.

 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;;

public class HW2
{

  public static void main(String[] args) throws FileNotFoundException 
  {
    // Use Scanner to obtain the input from a file
    Scanner sc = new Scanner(new FileInputStream(args[0]));

    // number of words in a palindrome
    int palLength = Integer.parseInt(args[1]);
    
    // List to store both input and output
    List<String> input = new ArrayList<String>();
    List<String> output = new ArrayList<String>();

    // Read the input and store it in a list
    while (sc.hasNext()) {
      input.add(sc.next());
    }

    // Sort the input list
    Collections.sort(input);

    // prints palindromes in alphabetical/lexicographical
    findPalindrome(palLength, input, output);

  }

  /* ------------------------------ Methods ------------------------------ */

  /*
    Permutates through the list generating new sets of possible palindromes
    @params int numOfWords, List<String> inputList, List<String> outputList
    @return nothing.
   */
  public static void findPalindrome (int numOfWords, List<String> inputList, List<String> outputList) {
    // if the number of the words in a sentence is 0, return
    if (numOfWords == 0 ) { return; }

    // Create a new list and copy all elements from @param inputList
    List<String> inputCopy = new ArrayList<String>();
    inputCopy.addAll(inputList);

    // For each word in input copy list
    for (int i = 0; i < inputCopy.size(); i ++) {
      String word = inputCopy.get(i); // Get i'th word
      inputCopy.remove(word);         // Remove i'th word from input copy list
      outputList.add(word);           // Add i'th word in @param outputList

      // Checks number of permutations by using @param numOfWords
      if (numOfWords == 1) {
        palindromeCheck(outputList); // check if @param outputList is a palindrome set
      } else {
        // Recursion
        findPalindrome(numOfWords - 1, inputCopy, outputList);
      }
      outputList.remove(word);  // Remove i'th word from @param outputList
      inputCopy.add(i, word);   // Add i'th word back in input copy list
    }
    return;
  }


  /* 
    Check if a string is a palindrome or not by calling a recursive algorithm, if word length greater than 0.
    @params String word.
    @return boolean.
   */
  public static boolean isPalindrome (String word) {
    int size = word.length();

    if (size == 0) {
      return true;
    }
    return isPalindromeRec(word, 0, size - 1);
  }


  /* 
    Recursive algorithm that checks if a string is a palindrome or not.
    @params String word, int firstIndex, int lastIndex.
    @return boolean.
   */
  public static boolean isPalindromeRec (String word, int firstIdx, int lastIdx) {
    // return true if both first and last have the same index
    if (firstIdx == lastIdx) { return true; }
    // return false if the same index have different values
    if (word.charAt(firstIdx) != word.charAt(lastIdx)) { return false; }

    // Recursion
    if (firstIdx < lastIdx + 1) {
      return isPalindromeRec(word, firstIdx + 1, lastIdx - 1);
    }
    return true;
  }

  
  /*
    Check and prints palindromes in alphabetical/lexicographical
    @params List<String> list
    @return nothing
   */
  public static void palindromeCheck (List<String> list) {
    // Return if @param list is empty
    if (list.isEmpty()) { return; }

    // Concatenate string by adding each element in @param list with a space as delimeter
    String sentence = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      sentence += " " + list.get(i);
    }

    // Remove all blank spaces from sentence and store the new string
    String sentWithoutSpaces = sentence.replaceAll(" ", "");

    // Check if the string without blank space is a palindrome
    if (isPalindrome(sentWithoutSpaces)) {
      // Prints the actual string with space between words
      System.out.println(sentence);
    }
  }
  /* ------------------------------ End Methods ------------------------------ */
}
