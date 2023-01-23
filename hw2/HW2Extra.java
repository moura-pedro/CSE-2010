public class HW2Extra {
   public static void main (String[] args) {
      String a = "jonpa";
      System.out.println(isPalindrome(a));
   }

   public static boolean isPalindrome (String word) {
      int size = word.length();
      if (size <= 1) { return true; } 

      for (int i = 0; i < size / 2; i ++) {
         if (word.charAt(i) != word.charAt(size - i - 1)) {
            return false;
         }
      }
      return true;
   }
}
