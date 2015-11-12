/**
 * Created by antero on 09/11/15.
 */
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*public class Starter{
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
        //Hashmap K=Int
            //Hashmap K=String
                //Hashmap K= String V= Int
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


        //Easy ones:
        System.out.println("Easy Ones:");
        //Question 1: How many weeks does the data set cover in total?
        System.out.println("Q1 Number of Weeks: "+iot.size());

        //Question 2: What was the interest in JavaScript in the week "2014-10-12 - 2014-10-18"?
        System.out.println("Q2 Interest in JS in week 2014-10-12 - 2014-10-18: "+iot.get("2014-10-12 - 2014-10-18").get("JavaScript"));
        //Question 3: In which week did Java's level of interest first dropped below 50?
        for(String wk:iot.keySet())
            if(iot.get(wk).get("java")<50) {
                System.out.println("Q3 Java first dropped below 50 in week: " + wk);
                break;
            }
        //Question 4: In how many weeks was there more interests in C++ over C#?
        int q4acc = 0;
        for(String wk:iot.keySet())
            if(iot.get(wk).get("c++")>iot.get(wk).get("c#"))
                q4acc++;
        System.out.printf("Q4 There was more interest in C++ over C# in %d weeks\n ",q4acc);

        //Question 5: How many regions have never demonstrated any interest in Python?
        ArrayList<String> noInterestInPython = new ArrayList<>();
        boolean hasInterestInPython = true;
        *//*for(int yr :regionsByYear.keySet()) {
            for (String country : regionsByYear.get(yr).keySet()) {
                if (regionsByYear.get(yr).get(country).get("python") == 0) {
                    hasInterestInPython = false;
                } else {
                    hasInterestInPython = true;
                }
            }
            if (!noInterestInPython.contains(country))
                noInterestInPython.add(country);
        }*//*
        for(int yr:regionsByYear.keySet())

        System.out.println(noInterestInPython);
        System.out.printf("Q5 %d regions have never demonstrated any interest in python\n ",noInterestInPython.size());

        //Medium ones:
        //Question 6: Which regions have demonstrated interests in exactly two programming language in 2010? (Give the region and the two languages.)
        TreeMap<String,ArrayList<String>> answerq6 = new TreeMap<>();
        for(String country : regionsByYear.get(2010).keySet()) {
            ArrayList<String> interested = new ArrayList<>();
            for(String lang: langs)
                if(regionsByYear.get(2010).get(country).get(lang)!=0)
                    interested.add(lang);
            if(interested.size()==2)
                answerq6.put(country,interested);
        }
        System.out.printf("Q6 There are %d countries that demonstrate interest in 2 languages in 2010: %n",answerq6.size());
        for(String answer: answerq6.keySet())
            System.out.printf("\tQ6: %s %s \n",answer,answerq6.get(answer));

        //Question 7: What are the most and least popular programming languages all over the world in 2014 ()?
        //Question 8: Which are the least popular programming languages in the United Kingdom for each of the years 2009 to 2014?
        //Question 9: In which year did JavaScript have the greatest minimum interest? Consider the year that week starts in.
        //Question 10: Since 2010, which regions have demonstrated the most interests in Python, and which programming languages were those regions least interested in?





        //Question 11: Which are the top 5 regions that demonstrated significant growth of interests in programming languages in general?
        //Significant growth will be 10% more than the average interest
        *//*for(HashMap yr:regionsByYear.values()){
            for(Object country :yr.values()) {
                String print = String.format("%s ----",country);
                System.out.println(print);
            }

        }*//*
    }
}*/
