/**
 * Created by antero on 09/11/15.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Starter{
    public static void main(String[] args) throws Exception {
        BufferedReader fh =
                new BufferedReader(new FileReader("data/iot.txt"));
        //First line contains the language names
        String s = fh.readLine();
        List<String> langs =
                new ArrayList<>(Arrays.asList(s.split("\t")));
        langs.remove(0);	//Throw away the first word - "week"
        Map<String,HashMap<String,Integer>> iot = new TreeMap<>();
        while ((s=fh.readLine())!=null)
        {
            String [] wrds = s.split("\t");
            HashMap<String,Integer> interest = new HashMap<>();
            for(int i=0;i<langs.size();i++)
                interest.put(langs.get(i), Integer.parseInt(wrds[i+1]));
            iot.put(wrds[0], interest);
        }
        fh.close();
        HashMap<Integer,HashMap<String,HashMap<String,Integer>>>
                regionsByYear = new HashMap<>();
        for (int i=2004;i<2016;i++)
        {
            BufferedReader fh1 =
                    new BufferedReader(new FileReader("data/"+i+".txt"));
            String s1 = fh1.readLine(); //Throw away the first line
            HashMap<String,HashMap<String,Integer>> year = new HashMap<>();
            while ((s1=fh1.readLine())!=null)
            {
                String [] wrds = s1.split("\t");
                HashMap<String,Integer>langMap = new HashMap<>();
                for(int j=1;j<wrds.length;j++){
                    langMap.put(langs.get(j-1), Integer.parseInt(wrds[j]));
                }
                year.put(wrds[0],langMap);
            }
            regionsByYear.put(i,year);
            fh1.close();
        }
        // Data structures available are: langs, iot, regionsByYear
        // langs is a list of languages
        // iot is interest over time - a map from week to a map from languages
        // regionsByYear maps years onto a map of regions onto a map of languages

        // Sample question 1: Show the weeks where java interest was exactly 80
        for(String wk:iot.keySet())
        {
            if (iot.get(wk).get("java")==80)
                System.out.printf("Question 1 Interest in Java is 80: %s\n",wk);
        }

        // Sample question 2: show the interest in java in Brazil in 2004
        System.out.printf("Question 2 Interest in java in Brazil in 2004: %d\n",
                regionsByYear.get(2004).get("Brazil").get("java"));
    }
}
