/**
 * Created by antero on 09/11/15.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
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

        TreeMap<Week,TreeMap<String,Integer>> interestByWeek = new TreeMap<>();

        while ((s=fh.readLine())!=null)
        {
            String [] wrds = s.split("\t");

            TreeMap<String,Integer> interest = new TreeMap<>();
            for(int i =0;i<langs.size();i++)
                interest.put(langs.get(i),Integer.parseInt(wrds[i+1]));

            String [] period = wrds[0].split(" - ");
            Week week = new Week(period[0],period[1]);
            interestByWeek.put(week,interest);

        }
        fh.close();

        TreeMap<String,Country> world = new TreeMap<>();

        for (int i=2004;i<2016;i++){
            TreeMap<String,int[]> interestMap = new TreeMap<>(); //Temporarily hold the country name and interest values
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


        // Data structures available are: langs, interestByWeek, world
        // langs is a list of languages
        // iot is interest over time - a map from week to a map from languages
        //interestByWeek is a map of Week(Class created to have start and end dates as LocalDate instead of string)
            // to a map of Languages to Interest
        // world maps every country in the world.
        // an object of type country contains data for every country in the world over the years

        //Easy ones:
        System.out.println("Easy Ones:");
        //Question 1: How many weeks does the data set cover in total?
        System.out.println("Q1 Number of Weeks: "+interestByWeek.size());

        //Question 2: What was the interest in JavaScript in the week "2014-10-12 - 2014-10-18"?
        System.out.println("Q2 Interest in JS in week 2014-10-12 - 2014-10-18: "+interestByWeek.get(new Week("2014-10-12","2014-10-18")).get("JavaScript"));
        //Question 3: In which week did Java's level of interest first dropped below 50?
        for(Week wk:interestByWeek.keySet())
            if(interestByWeek.get(wk).get("java")<50) {
                System.out.println("Q3 Java first dropped below 50 in week: " + wk);
                break;
            }
        //Question 4: In how many weeks was there more interests in C++ over C#?
        int q4acc = 0;
        for(Week wk:interestByWeek.keySet())
            if(interestByWeek.get(wk).get("c++")>interestByWeek.get(wk).get("c#"))
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
        System.out.println("Medium Ones");
        //Question 6: Which regions have demonstrated interests in exactly two programming language in 2010? (Give the region and the two languages.)

        System.out.println("Q6 The following countries have demonstrated interest in exactly 2 languages in 2010: ");
        for(Country c:world.values()) {
            if (c.hasYear(2010) && c.getInterestByLang(2010).size() == 2)
                System.out.println("\t"+c.getName()+" has shown interest in: "
                        +c.getInterestByLang(2010).keySet().toArray()[0]+" and "
                        +c.getInterestByLang(2010).keySet().toArray()[1]);
        }

        //Question 7: What are the most and least popular programming languages all over the world in 2014 ()?
        TreeMap<String,Integer> popularity = new TreeMap<>();
        for(Week wk:interestByWeek.keySet())
            if (wk.start().getYear() == 2014)
                for (String lang : langs)
                    if (popularity.get(lang) == null) {
                        popularity.put(lang, interestByWeek.get(wk).get(lang));
                    } else {
                        popularity.put(lang, popularity.get(lang) + interestByWeek.get(wk).get(lang));
                    }

        System.out.println(popularity);

        int valOfPop=(Collections.max(popularity.values()));  // This will return max value in the Hashmap
        int valOfNotPop=(Collections.min(popularity.values()));  // This will return max value in the Hashmap
        String mostPop="";
        String leastPop="";
        for (Map.Entry<String, Integer> entry : popularity.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==valOfPop) {
                mostPop = entry.getKey();     // Print the key with max value
            } else if(entry.getValue()==valOfNotPop) {
                leastPop = entry.getKey();
            }
        }
        System.out.printf("Q7 For 2014(iot file), the most popular language was %s and the least popular was %s \n",mostPop,leastPop);

        for(Country c:world.values()){
            for(String lang:langs){
                if (popularity.get(lang) == null) {
                    popularity.put(lang, c.getInterest(2014,lang));
                } else {
                    popularity.put(lang, popularity.get(lang) + c.getInterest(2014,lang));
                }
            }
        }
        System.out.println(popularity);
        valOfPop=(Collections.max(popularity.values()));  // This will return max value in the Hashmap
        valOfNotPop=(Collections.min(popularity.values()));  // This will return max value in the Hashmap
        mostPop="";
        leastPop="";
        for (Map.Entry<String, Integer> entry : popularity.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==valOfPop) {
                mostPop = entry.getKey();     // Print the key with max value
            } else if(entry.getValue()==valOfNotPop) {
                leastPop = entry.getKey();
            }
        }
        System.out.printf("Q7 For 2014(2014 file), the most popular language was %s and the least popular was %s \n",mostPop,leastPop);




        //Question 8: Which are the least popular programming languages in the United Kingdom for each of the years 2009 to 2014?
        System.out.println("Q8");
        for(int i =2009;i<=2014;i++)
            System.out.printf("\tLeast popular for year %d is: %s\n",i,world.get("United Kingdom").popInYear(i,"min"));

        //Question 9: In which year did JavaScript have the greatest minimum interest? Consider the year that week starts in.

        TreeMap<Integer,Integer> jsMinIOT = new TreeMap<>();
        int jsMinInterest = 0;
        for(Week wk: interestByWeek.keySet()) {
            if(interestByWeek.get(wk).get("JavaScript")>jsMinInterest)
                if(jsMinIOT.get(wk.start().getYear())==null) {
                    jsMinIOT.put(wk.start().getYear(), interestByWeek.get(wk).get("JavaScript"));
                } else if(jsMinIOT.get(wk.start().getYear())>interestByWeek.get(wk).get("JavaScript"))
                    jsMinIOT.put(wk.start().getYear(), interestByWeek.get(wk).get("JavaScript"));
        }
        Map.Entry<Integer,Integer> maxOfMin = null;
        for(Map.Entry entry:jsMinIOT.entrySet())
            maxOfMin = maxOfMin==null || Integer.parseInt(entry.getValue().toString()) > maxOfMin.getValue()?entry:maxOfMin;
        System.out.println("Q9: JavaScript had the greatest minimum interest in: "+maxOfMin.getKey());

        //Question 10: Since 2010, which regions have demonstrated the most interests in Python, and which programming languages were those regions least interested in?
        //NOT SOLVED YET!!!
        TreeMap<Integer,String> mostInPython = new TreeMap<>();
        for(Country c:world.values())
            for (int i = 2010; i <= 2015; i++)
                if (c.hasYear(i)) {
                    for(Map.Entry entry:mostInPython.entrySet()) {
                        if (entry.getValue().equals(c.getName())) {
                            if (mostInPython.size() < 5) {
                                mostInPython.put(c.getInterest(i, "python"), c.getName());
                            } else {
                                mostInPython.remove(mostInPython.firstKey());
                                mostInPython.put(c.getInterest(i, "python"), c.getName());
                            }
                        }
                    }
                }
        System.out.println(mostInPython);

        //Question 11: Which are the top 5 regions that demonstrated significant growth of interests in programming languages in general?
        //Significant growth will be 20% more than the interest in first year
        TreeMap<Integer,String> grownInterest = new TreeMap<>(); //Holds growth against  country name
        for(Country c:world.values()){
            int combInFirst = c.getCombinedInterest(c.getYears().get(0));
            int combInLast = c.getCombinedInterest(c.getYears().get(c.getYears().size()-1));
            if(combInLast>(float)combInFirst*1.20)
                if(grownInterest.size()<5)
                    grownInterest.put((combInLast-combInFirst),c.getName());
            else{
                    grownInterest.remove(grownInterest.firstKey());
                    grownInterest.put((combInLast-combInFirst),c.getName());
                }
        }

        System.out.println("Q11");
        int counter = 0;
        for (Map.Entry entry : grownInterest.descendingMap().entrySet()) {
            counter++;
            if (counter <= 5) {
                System.out.println("\t"+entry.getValue()+" grew "+entry.getKey()+" points");
            }
        }

        //Question 12: Which programming language set the record for losing the most interests over a 12 months period? When did this happen?


        //Question 13: Languages popular at University may be higher in September and October
        // this means the Sept/Oct average is higher than the whole year average.
        // Show this trend for the language Java. The example shown has figures for c++.
        // You may assume the academic year starts on 1st September every year.
        //Ac Yr   All Yr  Sep/Oct
        //2009/10	8.69	9.75 c++
        //2010/11	7.67	8.56 c++
        //2011/12	7.02	8.00 c++
        //2012/13	6.21	6.89 c++
        //2013/14	5.94	6.67 c++
        //2014/15	5.76	6.50 c++

        //Map to hold the weeks in each academic year
        TreeMap<String,ArrayList<Week>> academicYearMap = new TreeMap<>();

        //Get a list of all the academic years
        ArrayList<String> academicYearList = new ArrayList<>();
        for(Week wk : interestByWeek.keySet()){
            String newAcadYear = String.format("%d/%d", wk.start().getYear(), wk.start().getYear() + 1);
            if(!academicYearList.contains(newAcadYear))
                academicYearList.add(newAcadYear);
        }

        //Put all the weeks of September and October into the academicYearMap
        for(String acadYear:academicYearList) {
            ArrayList<Week> academicYear = new ArrayList<>();
            LocalDate startOfAcadYear = LocalDate.of(Integer.parseInt(acadYear.split("/")[0]), Month.SEPTEMBER, 1);
            LocalDate endOfOctober =  LocalDate.of(Integer.parseInt(acadYear.split("/")[0]), Month.OCTOBER, 31);
            for (Week wk : interestByWeek.keySet()) {
                String wkAcadYear = String.format("%d/%d", wk.start().getYear(), wk.start().getYear() + 1);
                if (acadYear.equals(wkAcadYear) && wk.start().isAfter(startOfAcadYear) && wk.start().isBefore(endOfOctober)) {
                    academicYear.add(wk);
                }
                if(!academicYear.isEmpty())
                    academicYearMap.put(acadYear, academicYear);
            }
        }

        TreeMap<String,Float> avgJava = new TreeMap<>();

        for(int i=2004;i<=2014;i++) {
            float totalJava = 0;
            int count = 0;
            for (Week wk : interestByWeek.keySet())
                if(wk.start().getYear()==i) {
                    totalJava += interestByWeek.get(wk).get("java");
                    count++;
                }
            avgJava.put(""+i,totalJava/count);
        }


        TreeMap<String,Float> avgAcadJava = new TreeMap<>();
        for(String acadYear:academicYearMap.keySet()) {
            ArrayList<Week> weeks = academicYearMap.get(acadYear);
            float totalJava = 0;
            int count = 0;
            for (Week wk : weeks){
                totalJava += interestByWeek.get(wk).get("java");
                count++;
            }
            avgAcadJava.put(acadYear,totalJava/count);
        }
        System.out.println("Q13: ");
        System.out.printf("Acad Year\tAvg Yr\tAvg Sep/Oct\n");
        if(avgAcadJava.size()==avgJava.size())
            for(int i=0;i<avgAcadJava.size();i++){
                System.out.printf("%s\t%1.2f\t%1.2f\t%s\n",academicYearList.get(i),avgAcadJava.get(academicYearList.get(i)),avgJava.get(academicYearList.get(i).split("/")[0]),langs.get(0));
            }




    }
}
