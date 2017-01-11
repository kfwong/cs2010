import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Scanner;

public class Addition { // as the class name that contains the main method is "Addition", you have to save this file as "Addition.java", and submit "Addition.java" to Codecrunch
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    while(true){

      BigInteger aVal = sc.nextBigInteger();
      BigInteger bVal = sc.nextBigInteger();

      if(aVal.equals(BigInteger.valueOf(-1)) && bVal.equals(BigInteger.valueOf(-1))){
        break;
      }else{
        System.out.println(aVal.add(bVal).toString());
      }
  }
}

}
