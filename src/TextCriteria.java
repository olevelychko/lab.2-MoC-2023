import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class TextCriteria {

    private static boolean isCyrillicOrSpace(char ch) {
        String alp = "абвгдеєжзиіїйклмнопрстуфхцчшщьюя";
        for (int i = 0; i < alp.length(); i++) {
            if (ch == alp.charAt(i)) return true;
        }
        return false;
    }


    public static Integer numOfLetters(StringBuilder text) {
        return text.toString().length();
    }


    public static void main(String[] args) throws Exception {
        ArrayList<Double> plainText = new ArrayList<>();

        File doc = new File("C:\\TextForSecondLab.txt");
        Scanner scanner = new Scanner(doc);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine().toLowerCase().trim();
            if (!word.isEmpty()) {
                for (char ch : word.toCharArray()) {
                    if (isCyrillicOrSpace(ch)) {
                        builder.append(ch);
                    }
                }
            }
        }
        System.out.println(builder);
        int numLet = numOfLetters(builder);
        System.out.println("Tne number of letters is: " + numLet);


    }
}
