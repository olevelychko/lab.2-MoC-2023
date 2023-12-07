import java.io.File;
import java.util.*;

import static java.lang.Math.abs;

public class TextCriteria {
    private static final String alp = "абвгдеєжзиіїйклмнопрстуфхцчшщьюя";
    private static final int limLet = 6000;
    private static final int limBi = 3000;

    private static boolean isCyrillicOrSpace(char ch) {
        for (int i = 0; i < alp.length(); i++) {
            if (ch == alp.charAt(i)) return true;
        }
        return false;
    }

    private static ArrayList<String> bigramAlph() {
        ArrayList<String> allBi = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                String bi = String.valueOf(alp.charAt(i)) + alp.charAt(j);
                allBi.add(bi);
            }
        }
        return allBi;
    }

    private static int searchLet(char let) {
        for (int i = 0; i < alp.length(); i++) {
            if (let == alp.charAt(i)) return i;
        }
        System.out.println("Can not find a char in alphabet");
        return -1;
    }

    private static int searchBi(String bi) {
        ArrayList<String> arrayBi = bigramAlph();
        for (int i = 0; i < arrayBi.size(); i++) {
            if (Objects.equals(bi, arrayBi.get(i))) return i;
        }
        System.out.println("Can not find a bigram in alphabet");
        return -1;
    }


    public static Integer numOfLetters(StringBuilder text) {
        return text.toString().length();
    }

    public static Integer numOfBigrams(StringBuilder text) {
        int num_of_bi = 0;
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

    public static HashMap<Character, Integer> mapOfPopularLet(String text, int lim) {
        HashMap<Character, Integer> mapOfLet = new HashMap<>();
        for (int k = 0; k < text.length(); k++) {
            if (!mapOfLet.containsKey(text.charAt(k))) {
                mapOfLet.put(text.charAt(k), 1);
            } else {
                int i = mapOfLet.get(text.charAt(k));
                i++;
                mapOfLet.put(text.charAt(k), i);
            }
        }
        HashMap<Character, Integer> mapOfPopLet = new HashMap<>();
        for(int i=0; i<alp.length(); i++){
            char tempLet = alp.charAt(i);
            if(mapOfLet.get(tempLet)>lim) mapOfPopLet.put(tempLet,mapOfLet.get(tempLet));
        }
        return mapOfPopLet;
    }

    public static  HashMap<String, Integer> mapOfPopularBi(String text, int lim) {
        HashMap<String, Integer> mapOfBi = new HashMap<>();
        for (int k = 0; k + 1 < text.length(); k++) {
            String bigram = text.substring(k, k + 2);
            k++;

            if (!mapOfBi.containsKey(bigram)) {
                mapOfBi.put(bigram, 1);
            } else {
                int i = mapOfBi.get(bigram);
                i++;
                mapOfBi.put(bigram, i);
            }
        }
        //System.out.println(mapOfBi);
        ArrayList<String> alpBi = bigramAlph();
        ArrayList<String> bigram = new ArrayList<>();
        for (String s : alpBi) {
            if (mapOfBi.containsKey(s)) {
                bigram.add(s);
                bigram.add(String.valueOf(mapOfBi.get(s)));
            }
        }
        HashMap<String, Integer> popBi = new HashMap<>();
        for(int i = 1; i < bigram.size(); i+=2)
        {
            int freq = Integer.parseInt(bigram.get(i));
            if(freq > lim) popBi.put(bigram.get(i-1), freq);
        }
        return popBi;
    }

    public static ArrayList<String> delaytext(StringBuilder text, int l, int n) {
        ArrayList<String> delay = new ArrayList<>();
        if (l > 100) {
            StringBuilder text1 = new StringBuilder();
            for (int j = 0; j < 3; j++) {
                text1 = text.append(text);
            }
        }
        for (int i = 0; i < n; i++) {
            delay.add(text.substring(i * l, i * l + l));
        }
        return delay;
    }

    public static ArrayList<String> Vigenere(ArrayList<String> X, String key, int mod, String alp) {
        ArrayList<String> Y = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        int term;
        for (String x : X) {
            for (int j = 0; j < x.length(); j++) {
                term = (searchLet(x.charAt(j)) + searchLet(key.charAt(j % mod))) % 32;
                temp.append(alp.charAt(term));
            }
            Y.add(temp.toString());
            temp = new StringBuilder();
        }
        // System.out.println(Y);
        return Y;
    }

    public static ArrayList<String> Affine(ArrayList<String> X, String a, String b, String alp, int deg) {
        ArrayList<String> Y = new ArrayList<>();
        if (deg == 1) {
            StringBuilder temp = new StringBuilder();
            int term;
            int aNum = searchLet(a.charAt(0));
            int bNum = searchLet(b.charAt(0));
            for (String x : X) {
                for (int j = 0; j < x.length(); j++) {
                    int xNum = searchLet(x.charAt(j));
                    term = (aNum * (xNum) + bNum) % 32;
                    temp.append(alp.charAt(term));
                }
                Y.add(temp.toString());
                temp = new StringBuilder();
            }
        } else {
            StringBuilder temp = new StringBuilder();
            ArrayList<String> allBi = bigramAlph();
            int term;
            int aNum = searchBi(a);
            int bNum = searchBi(b);
            for (String x : X) {
                for (int j = 0; j + 1 < x.length(); j = j + 2) {
                    int xNum = searchBi(x.substring(j, j + 2));
                    term = (aNum * (xNum) + bNum) % (1024);
                    temp.append(allBi.get(term));
                }
                Y.add(temp.toString());
                temp = new StringBuilder();
            }
        }
        // System.out.println(Y);
        return Y;
    }

    public static ArrayList<String> GeneratedSeq(int l, int n, String alp, int deg){
        ArrayList<String> Y = new ArrayList<>();
        if(deg==1){
            Random r = new Random();
            for(int i=0; i<n; i++){
                StringBuilder temp = new StringBuilder();
                for(int j=0; j<l; j++){
                    int index = r.nextInt(32);
                    temp.append(alp.charAt(index));
                }
                Y.add(temp.toString());
            }
        }
        else{
            Random r = new Random();
            ArrayList<String> allBi = bigramAlph();
            for(int i=0; i<n; i++){
                StringBuilder temp = new StringBuilder();
                for(int j=0; j+1<l; j=j+2){
                    int index = r.nextInt(1024);
                    temp.append(allBi.get(index));
                }
                Y.add(temp.toString());
            }
        }
        // System.out.println(Y);
        return Y;
    }

    public static ArrayList<String> CorrelationSeq(int l, int n, String alp, int deg){
        ArrayList<String> Y = new ArrayList<>();
        if(deg==1) {
            Random r = new Random();
            for(int i=0; i<n; i++){
                StringBuilder temp = new StringBuilder();
                int s0 = r.nextInt(32);
                int s1 = r.nextInt(32);
                int sTemp = (s0+s1)%32;
                temp.append(alp.charAt(sTemp));
                for(int j=1; j<l; j++)
                {
                    s0 = s1;
                    s1 = sTemp;
                    sTemp = (s0+s1)%32;
                    temp.append(alp.charAt(sTemp));
                }
                Y.add(temp.toString());
            }
        }
        else {
            ArrayList<String> allBi = bigramAlph();
            Random r = new Random();
            for(int i=0; i<n; i++){
                StringBuilder temp = new StringBuilder();
                int s0 = r.nextInt(1024);
                int s1 = r.nextInt(1024);
                int sTemp = (s0+s1)%1024;
                temp.append(allBi.get(sTemp));
                for(int j=1; j+1<l; j=j+2)
                {
                    s0 = s1;
                    s1 = sTemp;
                    sTemp = (s0+s1)%1024;
                    temp.append(allBi.get(sTemp));
                }
                Y.add(temp.toString());
            }
        }
        //System.out.println(Y);
        return Y;
    }

    //in criteria false symbolizes H_1, true -- H_0
    public static boolean CriteriaZero(ArrayList<String> N, String text, int mod) {
        if (mod == 1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            for (String l : N) {
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    char lTemp = alp.charAt(j);
                    if (!Afrq.containsKey(lTemp)) return false;
                }
            }
        } else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
        for (String l : N) {
            for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                String lTemp = l.substring(j, j + 2);
                if (!Afrq.containsKey(lTemp)) return false;
            }
        }
    }
        //System.out.println(Afrq);
        //System.out.println(N);
        return true;
    }

    public static boolean CriteriaOne(ArrayList<String> N, String text, int mod)
    {
        if(mod==1){
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            HashMap<Character, Integer> Aaf = new HashMap<>();
            for (String l : N) {
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    char lTemp = alp.charAt(j);
                    if (Afrq.containsKey(lTemp)) Aaf.put(lTemp, 1);
                }
            }
            return abs(Afrq.size() - Aaf.size()) >= 2;
        }
        else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            HashMap<String, Integer> Aaf = new HashMap<>();
            for (String l : N) {
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp)) Aaf.put(lTemp, 1);
                }
            }
            return abs(Afrq.size() - Aaf.size()) >= 2;
        }
        //System.out.println(Afrq);
        //System.out.println(N);
    }

    public static boolean CriteriaTwo(ArrayList<String> N, String text, int mod)
    {
        if(mod==1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            HashMap<Character, Integer> Aaf ;
            for (String l : N) {
                Aaf = mapOfPopularLet(l,0);
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    if(Afrq.containsKey(l.charAt(j)))
                    {
                        if(Aaf.get(l.charAt(j)) < 2) return false;
                    }
                }
            }
            return true;
        }
        else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            HashMap<String, Integer> Aaf;
            for (String l : N) {
                Aaf = mapOfPopularBi(l,0);
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp))
                    {
                        if(Aaf.get(lTemp) < 2) return false;
                    }
                }
            }
            return true;
        }
    }

    public static boolean CriteriaThree(ArrayList<String> N, String text, int mod)
    {
        if(mod == 1)
        {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            HashMap<Character, Integer> Aaf;
            int sum = 0;
            for (String l : N) {
                Aaf = mapOfPopularLet(l,0);
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    if(Afrq.containsKey(l.charAt(j)))
                    {
                        sum+= Aaf.get(l.charAt(j));
                    }
                }
                if(sum < 5) return false;
            }
            return true;
        }
        else{
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            HashMap<String, Integer> Aaf;
            int sum = 0;
            for (String l : N) {
                Aaf = mapOfPopularBi(l,0);
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp))
                    {
                        sum+=Aaf.get(lTemp);
                    }
                }
                if(sum < 3) return false;
            }
            return true;
        }

    }


    public static void main(String[] args) throws Exception {
        String keyword = "метропоїзд";
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
        int l = 10;
        int n = 10_000;
        ArrayList<String> tensym = delaytext(builder, l, n);
        Vigenere(tensym, keyword, 1, alp);
        int deg = 2;
        Affine(tensym, keyword.substring(0, deg), keyword.substring(deg, deg + deg), alp, deg);
        ArrayList<String > N1 = GeneratedSeq(l, n, alp, deg);
        CorrelationSeq(l, n, alp, deg);
        boolean ind = CriteriaOne(tensym, builder.toString(), deg);
        System.out.println(ind);
    }
}