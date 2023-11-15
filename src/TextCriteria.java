import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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


    public static Integer numOfBigrams(StringBuilder text) {
        int num_of_bi=0;
        HashMap<String, Integer> mapOfBi = new HashMap<>();
        for (int k = 0; k + 1 < text.length(); k++) {
            String bigram = text.substring(k, k + 2);
            k++;

            if (!mapOfBi.containsKey(bigram)) {
                mapOfBi.put(bigram, 1);
                num_of_bi++;
            } else {
                int i = mapOfBi.get(bigram);
                i++;
                mapOfBi.put(bigram, i);
                num_of_bi++;
            }
        }
        System.out.println(mapOfBi);
        return num_of_bi;
    }

    public static ArrayList<String> delaytext(StringBuilder text, int l, int n)
    {
        ArrayList<String> delay = new ArrayList<>();
        if(l > 100)
        {
            StringBuilder text1 = new StringBuilder();
            for(int j = 0; j < 3; j++)
            {
                text1 = text.append(text);
            }
            text = text1;
        }
        for(int i = 0; i < n; i++)
        {
            delay.add(text.substring(i*l,i*l+l));
        }
        return delay;
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
        int numLet = numOfLetters(builder);
        System.out.println("The number of letters is: " + numLet);
        int numBi = numOfBigrams(builder);
        System.out.println("The number of bigrams is: " + numBi);
        ArrayList<String> tensym = delaytext(builder,10, 10000);
        //System.out.println(tensym);
        ArrayList<String> hundredsym = delaytext(builder, 100, 10000);
        //System.out.println(hundredsym);
        ArrayList<String> thousandsym = delaytext(builder, 1000, 10000);
        //System.out.println(thousandsym);
        ArrayList<String> tenthousym = delaytext(builder, 10000, 1000);
        //System.out.println(tenthousym);
    }
}
