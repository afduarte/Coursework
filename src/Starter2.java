/**
 * Created by antero on 09/11/15.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Starter2 {
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

        TreeMap<String,Country> world = new TreeMap<>();
        /*TreeMap<String,String> countries = new TreeMap<>();
        for (int i=2004;i<2016;i++) {
            BufferedReader getCountries =
                    new BufferedReader(new FileReader("data/" + i + ".txt"));
            String s1 = getCountries.readLine(); //Throw away the first line
            while ((s1 = getCountries.readLine()) != null) {
                String[] wrds = s1.split("\t");
                if(!countries.containsKey(wrds[0]))
                    countries.put(wrds[0],wrds[0]);
            }
            getCountries.close();
        }*/

        for (int i=2004;i<2016;i++){
            TreeMap<String,int[]> interestMap = new TreeMap<>();
            BufferedReader getInterest =
                    new BufferedReader(new FileReader("data/"+i+".txt"));
            String s2 = getInterest.readLine(); //Throw away the first line


            while ((s2=getInterest.readLine())!=null)
            {
                String[] wrds = s2.split("\t");
                int[] interest = new int[] {
                        Integer.parseInt(wrds[1]),
                        Integer.parseInt(wrds[2]),
                        Integer.parseInt(wrds[3]),
                        Integer.parseInt(wrds[4]),
                        Integer.parseInt(wrds[5])
                };
                interestMap.put(wrds[0],interest);

            }

            for(String country:interestMap.keySet()) {
                Country c = world.get(country);
                if(c==null) {
                    c = new Country(country);
                    world.put(country,c);
                }
                c.setYear(i,interestMap.get(country));
                }
            getInterest.close();
            }


        // Data structures available are: langs, iot, regionsByYear
        // langs is a list of languages
        // iot is interest over time - a map from week to a map from languages
        // world maps every country in the world.
        // an object of type country contains data for every country in the world over the years

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
        System.out.println("Q4 There was more interest in C++ over C# in "+q4acc+" weeks");

        //Question 5: How many regions have never demonstrated any interest in Python?
        int q5acc=0;
        for(Country c:world.values()) {
            if (c.getInterestByYear("python").isEmpty())
                q5acc++;
        }
        System.out.printf("Q5 %d regions have never demonstrated any interest in python\n ",q5acc);

        //Medium ones:
        //Question 6: Which regions have demonstrated interests in exactly two programming language in 2010? (Give the region and the two languages.)

        System.out.println("Q6 The following countries have demonstrated interest in exactly 2 languages in 2010: ");
        for(Country c:world.values()) {
            if (c.hasYear(2010) && c.getInterestByLang(2010).size() == 2)
                System.out.println("\t"+c.getName()+" has shown interest in: "
                        +c.getInterestByLang(2010).keySet().toArray()[0]+" and "
                        +c.getInterestByLang(2010).keySet().toArray()[1]);
        }

        //Question 7: What are the most and least popular programming languages all over the world in 2014 ()?

        //Question 8: Which are the least popular programming languages in the United Kingdom for each of the years 2009 to 2014?
        //Question 9: In which year did JavaScript have the greatest minimum interest? Consider the year that week starts in.
        //Question 10: Since 2010, which regions have demonstrated the most interests in Python, and which programming languages were those regions least interested in?





        //Question 11: Which are the top 5 regions that demonstrated significant growth of interests in programming languages in general?
        //Significant growth will be 10% more than the average interest
        /*for(HashMap yr:regionsByYear.values()){
            for(Object country :yr.values()) {
                String print = String.format("%s ----",country);
                System.out.println(print);
            }

        }*/
    }
}
