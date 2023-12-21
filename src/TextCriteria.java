import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import static java.lang.Math.abs;

public class TextCriteria {
    private static final String alp = "абвгдеєжзиіїйклмнопрстуфхцчшщьюя";
    private static final int limLet =100000;
    private static final int limBi = 50;

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
            } else {
                int i = mapOfBi.get(bigram);
                i++;
                mapOfBi.put(bigram, i);
            }
            num_of_bi++;
        }
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
        for (int i = 0; i < alp.length(); i++) {
            char tempLet = alp.charAt(i);
            if (mapOfLet.containsKey(tempLet) && (mapOfLet.get(tempLet) > lim))
                mapOfPopLet.put(tempLet, mapOfLet.get(tempLet));
        }
        return mapOfPopLet;
    }

    public static HashMap<Character, Integer> mapOfnonPopularLet(String text, int lim) {
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
        HashMap<Character, Integer> mapOfnonPopLet = new HashMap<>();
        for (int i = 0; i < alp.length(); i++) {
            char tempLet = alp.charAt(i);
            if (mapOfLet.get(tempLet) < lim) mapOfnonPopLet.put(tempLet, mapOfLet.get(tempLet));
        }
        return mapOfnonPopLet;
    }

    public static HashMap<String, Integer> mapOfPopularBi(String text, int lim) {
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
        ArrayList<String> alpBi = bigramAlph();
        ArrayList<String> bigram = new ArrayList<>();
        for (String s : alpBi) {
            if (mapOfBi.containsKey(s)) {
                bigram.add(s);
                bigram.add(String.valueOf(mapOfBi.get(s)));
            }
        }
        HashMap<String, Integer> popBi = new HashMap<>();
        for (int i = 1; i < bigram.size(); i += 2) {
            int freq = Integer.parseInt(bigram.get(i));
            if (freq > lim) popBi.put(bigram.get(i - 1), freq);
        }
        return popBi;
    }

    public static HashMap<String, Integer> mapOfnonPopularBi(String text, int lim) {
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
        ArrayList<String> alpBi = bigramAlph();
        ArrayList<String> bigram = new ArrayList<>();
        for (String s : alpBi) {
            if (mapOfBi.containsKey(s)) {
                bigram.add(s);
                bigram.add(String.valueOf(mapOfBi.get(s)));
            }
        }
        HashMap<String, Integer> nonpopBi = new HashMap<>();
        for (int i = 1; i < bigram.size(); i += 2) {
            int freq = Integer.parseInt(bigram.get(i));
            if (freq < lim) nonpopBi.put(bigram.get(i - 1), freq);
        }
        return nonpopBi;
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
        return Y;
    }

    public static ArrayList<String> GeneratedSeq(int l, int n, String alp, int deg) {
        ArrayList<String> Y = new ArrayList<>();
        if (deg == 1) {
            Random r = new Random();
            for (int i = 0; i < n; i++) {
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j < l; j++) {
                    int index = r.nextInt(32);
                    temp.append(alp.charAt(index));
                }
                Y.add(temp.toString());
            }
        } else {
            Random r = new Random();
            ArrayList<String> allBi = bigramAlph();
            for (int i = 0; i < n; i++) {
                StringBuilder temp = new StringBuilder();
                for (int j = 0; j + 1 < l; j = j + 2) {
                    int index = r.nextInt(1024);
                    temp.append(allBi.get(index));
                }
                Y.add(temp.toString());
            }
        }
        return Y;
    }

    public static ArrayList<String> CorrelationSeq(int l, int n, String alp, int deg) {
        ArrayList<String> Y = new ArrayList<>();
        if (deg == 1) {
            Random r = new Random();
            for (int i = 0; i < n; i++) {
                StringBuilder temp = new StringBuilder();
                int s0 = r.nextInt(32);
                int s1 = r.nextInt(32);
                int sTemp = (s0 + s1) % 32;
                temp.append(alp.charAt(sTemp));
                for (int j = 1; j < l; j++) {
                    s0 = s1;
                    s1 = sTemp;
                    sTemp = (s0 + s1) % 32;
                    temp.append(alp.charAt(sTemp));
                }
                Y.add(temp.toString());
            }
        } else {
            ArrayList<String> allBi = bigramAlph();
            Random r = new Random();
            for (int i = 0; i < n; i++) {
                StringBuilder temp = new StringBuilder();
                int s0 = r.nextInt(1024);
                int s1 = r.nextInt(1024);
                int sTemp = (s0 + s1) % 1024;
                temp.append(allBi.get(sTemp));
                for (int j = 1; j + 1 < l; j = j + 2) {
                    s0 = s1;
                    s1 = sTemp;
                    sTemp = (s0 + s1) % 1024;
                    temp.append(allBi.get(sTemp));
                }
                Y.add(temp.toString());
            }
        }
        return Y;
    }

    //in criteria false symbolizes H_1, true -- H_0
    public static int CriteriaZero(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            for (String l : N) {
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    char lTemp = l.charAt(j);
                    if (!Afrq.containsKey(lTemp)) {
                        countfalse++;
                        break;
                    }
                    if (j == ((N.get(0)).length() - 1)) {
                        counttrue++;
                        break;
                    }
                }
            }
        } else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            for (String l : N) {
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (!Afrq.containsKey(lTemp)) {
                        countfalse++;
                        break;
                    }
                    if (j == ((N.get(0)).length() - 2)) {
                        counttrue++;
                        break;
                    }
                }
            }
        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static int CriteriaOne(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            for (String l : N) {
                HashMap<Character, Integer> Aaf = new HashMap<>();
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    char lTemp = l.charAt(j);
                    if (Afrq.containsKey(lTemp)) Aaf.put(lTemp, 1);
                }
                if (abs(Afrq.size() - Aaf.size()) <= 3) {
                    countfalse++;
                }
                if (abs(Afrq.size() - Aaf.size()) > 3) {
                    counttrue++;
                }
            }
        } else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            for (String l : N) {
                HashMap<String, Integer> Aaf = new HashMap<>();
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp)) Aaf.put(lTemp, 1);
                }
                if (abs(Afrq.size() - Aaf.size()) <= 200) {
                    countfalse++;
                }
                if (abs(Afrq.size() - Aaf.size()) > 200) {
                    counttrue++;
                }
            }
        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static int CriteriaTwo(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            for (String l : N) {
                HashMap<Character, Integer> Aaf = mapOfPopularLet(l, 0);
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    if (Afrq.containsKey(l.charAt(j))) {
                        if (Aaf.get(l.charAt(j)) < 450) {
                            countfalse++;
                            break;
                        }
                        if (Aaf.get(l.charAt(j)) >= 450) {
                            counttrue++;
                            break;
                        }
                    }
                }
            }
        } else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);

            for (String l : N) {
                HashMap<String, Integer> Aaf = mapOfPopularBi(l, 0);
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp)) {
                        if (Aaf.get(lTemp) < 15) {
                            countfalse++;
                            break;
                        }
                        if (Aaf.get(lTemp) >= 15) {
                            counttrue++;
                            break;
                        }
                    }

                }
            }
        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static int CriteriaThree(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> Afrq = mapOfPopularLet(text, limLet);
            System.out.println(Afrq);
            int sum = 0;
            for (String l : N) {
                HashMap<Character, Integer> Aaf = mapOfPopularLet(l, 0);
                //System.out.println(l);
                //System.out.println(Aaf);
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    if (Afrq.containsKey(l.charAt(j))) {
                        sum += Aaf.get(l.charAt(j));
                    }

                }
                if (sum < l.length()*500.25) {
                    countfalse++;
                }
                if (sum >= l.length()*500.25) {
                    counttrue++;
                }
                sum=0;
            }
        } else {
            HashMap<String, Integer> Afrq = mapOfPopularBi(text, limBi);
            int sum = 0;
            for (String l : N) {
                HashMap<String, Integer> Aaf = mapOfPopularBi(l, 0);
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (Afrq.containsKey(lTemp)) {
                        sum += Aaf.get(lTemp);
                    }
                }
                if (sum < 118000) {
                    countfalse++;
                }
                if (sum >= 118000) {
                    counttrue++;
                }
                sum=0;
            }
        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static double BiSubIndex(String L, HashMap<String, Integer> freq) {
        ArrayList<String> alp = bigramAlph();
        double a = 0;
        for (String s : alp) {
            if (freq.containsKey(s)) a = a + freq.get(s) * (freq.get(s) - 1);
        }
        a = a / (L.length() * (L.length() - 1));
        return a;
    }

    public static double LetSubIndex(String L, HashMap<Character, Integer> freq) {
        double a = 0;
        for (int i = 0; i < alp.length(); i++) {
            if (freq.containsKey(alp.charAt(i))) a = a + freq.get(alp.charAt(i)) * (freq.get(alp.charAt(i)) - 1);
        }
        a = a / (L.length() * (L.length() - 1));
        return a;
    }

    public static int CriteriaFour(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> frq = mapOfPopularLet(text, 0);
            double SubInd = LetSubIndex(text, frq);
            for (String l : N) {
                HashMap<Character, Integer> Aaf = mapOfPopularLet(l, 0);
                double SubLInd = LetSubIndex(l, Aaf);
                if (abs(SubInd - SubLInd) > 1.6268) countfalse++;
                else counttrue++;
            }
        } else {
            HashMap<String, Integer> frq = mapOfPopularBi(text, 0);
            double SubInd = BiSubIndex(text, frq);
            for (String l : N) {
                HashMap<String, Integer> Aaf = mapOfPopularBi(l, 0);
                double SubLInd = BiSubIndex(l, Aaf);
                if (abs(SubInd + SubLInd) > 50.9529) countfalse++;
                else counttrue++;
            }

        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static int CriteriaFive(ArrayList<String> N, String text, int mod, int FFPP) {
        int counttrue = 0, countfalse = 0;
        if (mod == 1) {
            HashMap<Character, Integer> frq = mapOfnonPopularLet(text, 80000);
            int sum;
            for (String l : N) {
                HashMap<Character, Integer> Brf = new HashMap<>();
                for (int j = 0; j < (N.get(0)).length(); j++) {
                    if (frq.containsKey(l.charAt(j))) {
                        Brf.put(l.charAt(j), 1);
                    }
                }
                sum = frq.size() - Brf.size();
                if (sum < ((int) Math.ceil(frq.size() * 0.1))) countfalse++;
                else counttrue++;
            }
        } else {
            HashMap<String, Integer> frq = mapOfnonPopularBi(text, 10);
            int sum;
            for (String l : N) {
                HashMap<String, Integer> Brf = new HashMap<>();
                for (int j = 0; j + 1 < (N.get(0)).length(); j = j + 2) {
                    String lTemp = l.substring(j, j + 2);
                    if (frq.containsKey(lTemp)) {
                        Brf.put(lTemp, 1);
                    }

                }
                sum = frq.size() - Brf.size();
                if (sum < ((int) Math.ceil(frq.size() * 0.80))) countfalse++;
                else counttrue++;
            }

        }
        if (FFPP == 1) return counttrue;
        else return countfalse;
    }

    public static boolean StructureCriteria(ArrayList<String> N) throws UnsupportedEncodingException, DataFormatException {
        int sum = 0;
        int hz = 0, ho = 0;
        for (String ignored : N) {
            byte[] input = ignored.getBytes(StandardCharsets.UTF_8);
            // Compress the bytes
            byte[] output = new byte[100];
            Deflater compressor = new Deflater();
            compressor.setInput(input);
            compressor.finish();
            int compressedDataLength = compressor.deflate(output);
            compressor.end();
            for(int i = 0; i < output.length; i++)
            {
                sum = sum + output[i];
            }
            if(abs(sum) > 400) hz++;
            else ho++;
            sum = 0;
            String putString = new String(output, 0, compressedDataLength, StandardCharsets.UTF_8);
            //System.out.println(putString);
            // Decompress the bytes
            Inflater decompressor = new Inflater();
            decompressor.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[100];
            int resultLength = decompressor.inflate(result);
            decompressor.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, StandardCharsets.UTF_8);
            //System.out.println(outputString);
        }
        sum = sum/1000;
        System.out.println("This is a simple text" + hz);
        System.out.println("This is a random text" + ho);
        return true;
    }

    public static Integer StructureUniCriteria(ArrayList<String> N, int FFPP)
    {
        int counttrue =0, countfalse =0;
        for(String n : N)
        {
            HashMap<Character,Integer> let = mapOfPopularLet(n,0);
            List<Integer> Ind = new ArrayList(let.values());
            Collections.sort(Ind);
            HashMap<Character,String> Uni = new HashMap<>();
            String a = "1";
            for(int i = Ind.size()-1; i > -1;i--)
            {
                int key = Ind.get(i);
                for (int j = 0; j < alp.length(); j++)
                {
                    if(let.containsKey(alp.charAt(j)) && let.get(alp.charAt(j)) == key && !Uni.containsKey(alp.charAt(j)))
                    {
                        Uni.put(alp.charAt(j), a);
                        break;
                    }
                }
                a = "0" + a;
            }
            //System.out.println(Uni);
            a = "";
            for(int k = 0; k < n.length(); k++)
            {
                a = a + Uni.get(n.charAt(k));
            }
            if(a.length() < 97_000) countfalse++;
            else counttrue++;
        }
        if(FFPP == 1) return countfalse;
        else return counttrue;
    }

    public static boolean StructureLZMCriteria (ArrayList<String> N) throws IOException {
        int sum = 0;
        int hz = 0, ho = 0;
        for(String n : N) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(n.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(n.getBytes());
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            for(int i = 0; i < compressed.length; i++)
            {
                sum = sum + compressed[i];
            }
            if(abs(sum) > 3000) hz++;
            else ho++;
            sum = 0;
        }
        sum = sum/1000;
        System.out.println("This is a simple text" + hz);
        System.out.println("This is a random text" + ho);
        return true;
    }

    public static void testfunction(ArrayList<String> delaytext, ArrayList<String> modifytext, String builder, int l, int deg) throws DataFormatException, IOException {
        System.out.println("This is a criteria test seq");
        System.out.println("lenght :" + l + "  l-gram, l = " + deg);

        System.out.println("Criteria Zero -- 2.0");
        System.out.println("For simple text");
        System.out.println(CriteriaZero(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println("vigener 1 mod");
        System.out.println(CriteriaZero(modifytext, builder, deg, 1));

        System.out.println("Criteria One -- 2.1");
        System.out.println("For simple text");
        System.out.println(CriteriaOne(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println(CriteriaOne(modifytext, builder, deg, 1));

        System.out.println("Criteria Two -- 2.2");
        System.out.println("For simple text");
        System.out.println(CriteriaTwo(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println(CriteriaTwo(modifytext, builder, deg, 1));

        System.out.println("Criteria Three -- 2.3");
        System.out.println("For simple text");
        System.out.println(CriteriaThree(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println(CriteriaThree(modifytext, builder, deg, 1));

        System.out.println("Criteria Four -- 4.0");
        System.out.println("For simple text");
        System.out.println(CriteriaFour(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println(CriteriaFour(modifytext, builder, deg, 1));

        System.out.println("Criteria Five -- 5.0");
        System.out.println("For simple text");
        System.out.println(CriteriaFive(delaytext, builder, deg, 2));
        System.out.println("For text after manipulation");
        System.out.println(CriteriaFive(modifytext, builder, deg, 1));

        System.out.println("Structure Criteria -- 6.0");
        System.out.println("For simple text");
        System.out.println(StructureCriteria(delaytext));
        System.out.println("For text after manipulation");
        System.out.println(StructureCriteria(modifytext));

        System.out.println("Structure UniCriteria -- 6.1");
        System.out.println("For simple text");
        System.out.println(StructureUniCriteria(delaytext,2));
        System.out.println("For text after manipulation");
        System.out.println(StructureUniCriteria(modifytext,1));

        System.out.println("Structure LZMCriteria -- 6.2");
        System.out.println("For simple text");
        System.out.println(StructureLZMCriteria(delaytext));
        System.out.println("For text after manipulation");
        System.out.println(StructureLZMCriteria(modifytext));
    }

    public static void Oneletterseq()
    {
        String keyword = "метропоїзд";
        String a = "a";
        String c = a;
        for (int i = 0; i < 10_000; i++)
        {
            c = c +a;
        }
        ArrayList<String> oursq = new ArrayList<>();
        oursq.add(c);
        oursq = Vigenere(oursq, keyword, 10, alp);
        System.out.println(StructureUniCriteria(oursq,1));
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
        System.out.println("LENGTH OF TEXT= " + builder.length());
        int numLet = numOfLetters(builder);
        //System.out.println("The number of letters is: " + numLet);
        int numBi = numOfBigrams(builder);
        //System.out.println("The number of bigrams is: " + numBi);
        int l = 10000;
        int n = 1000;
        ArrayList<String> tensym = delaytext(builder, l, n);
       // Vigenere(tensym, keyword, 2, alp);
        int deg = 2;
        //Affine(tensym, keyword.substring(0, deg), keyword.substring(deg, deg + deg), alp, deg);
        //ArrayList<String> N1 = GeneratedSeq(l, n, alp, deg);
        //CorrelationSeq(l, n, alp, deg);
        int ind = CriteriaFive(Vigenere(tensym, keyword, 10, alp), builder.toString(), deg, 1);

        System.out.println("This is a criteria test seq");
        System.out.println("lenght :" + l + "  l-gram, l = " + deg);

        //testfunction(tensym, CorrelationSeq(l, n, alp, deg), builder.toString(), l, deg);
        Oneletterseq();
    }
}
