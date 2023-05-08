package test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOSearcher {

    public IOSearcher() {
    }

    public static boolean search(String word, String... fileNames) {
        //Returns true if contains word
        for (String s : fileNames) {
            File file = new File(s);

            try {
                Scanner scanner = new Scanner(file);

                //now read the file line by line...
                int lineNum = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    lineNum++;
                    if (line.contains(word)){
                        return true;
                    }
                }
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        return false;
    }

}
