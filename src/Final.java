import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by antero on 26/11/15.
 */
public class Final {
    public static void main(String[] args) throws Exception {
        BufferedReader fh =
                new BufferedReader(new FileReader("data/iot.txt"));             //Open file iot.txt
        //First line contains the language names
        String s = fh.readLine();
        List<String> langs =
                new ArrayList<>(Arrays.asList(s.split("\t")));
        langs.remove(0);	//Throw away the first word - "week"

        //Create a Treemap of Week(See Week class) to a treemap of String(Language) to Integer(Interest)
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
        //Go through every year file, create a new country for every country there and put it in the TreeMap world
        for (int i=2004;i<=2015;i++){
            //Temporary holder for the country name and interest values
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
            //Checks if the country exists in world.
            //If it doesn't exist, creates it
            for(String country:interestMap.keySet()) {
                Country c = world.get(country);
                if(c==null) {
                    c = new Country(country);
                    world.put(country,c);
                }
                //Sets the year for country c
                c.setYear(i,interestMap.get(country));
            }
            getInterest.close(); //Closes the file
        }


        // Data structures available are: langs, interestByWeek, world
        // langs is a list of languages
        // iot is interest over time - a map from week to a map from languages
        //interestByWeek is a map of Week(Class created to have start and end dates as LocalDate instead of string)
        // to a map of Languages to Interest
        // world maps every country in the world.
        // an object of type country contains data for every country in the world over the years


        //Medium ones:
        System.out.println("Medium Ones:");
        //Question 6: Which regions have demonstrated interests in exactly two programming language in 2010? (Give the region and the two languages.)

        System.out.println("Q6 The following countries have demonstrated interest in exactly 2 languages in 2010: ");
        world.values().stream()
                .filter(c -> c.hasYear(2010) && c.getInterestByLang(2010).size() == 2)
                .forEach(c ->System.out.println("\t"+c.getName()+" has shown interest in: "
                        +c.getInterestByLang(2010).keySet().toArray()[0]+" and "
                        +c.getInterestByLang(2010).keySet().toArray()[1]));

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

        int valOfPop=(Collections.max(popularity.values()));  // This will return max value in the TreeMap
        int valOfNotPop=(Collections.min(popularity.values()));  // This will return max value in the TreeMap
        String mostPop="";
        String leastPop="";
        for (Map.Entry<String, Integer> entry : popularity.entrySet()) {
            if (entry.getValue()==valOfPop) {
                mostPop = entry.getKey();
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
        //System.out.println(mostInPython);

        //Hard ones:
        System.out.println("Hard Ones:");
        //Question 11: Which are the top 5 regions that demonstrated significant growth of interests in programming languages in general?
        //Significant growth will be 20% more than the interest in first year

        TreeMap<String,Integer> grownInterest = new TreeMap<>(); //Holds growth against  country name
        world.values().stream()
                //Filter for countries where growth was more than 20%
                .filter(c->(float)c.getCombinedInterest(c.getYears().get(c.getYears().size()-1))*1.20>c.getCombinedInterest(c.getYears().get(0)))
                //Put stream results in grownInterest map
                .forEach(c-> grownInterest.put(c.getName(),c.getCombinedInterest(c.getYears().get(c.getYears().size()-1))-c.getCombinedInterest(c.getYears().get(0))));

        grownInterest.entrySet().stream()
                //Sort grownInterest according to values in reverse order (get country with biggest growth first)
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue)))
                //Limit to 5 so we get only the 5 biggest growth countries
                .limit(5)
                //Print the limited stream
                .forEach(x -> System.out.println(x.getKey()+" grew "+x.getValue()+" points."));

        //Question 12: Which programming language set the record for losing the most interests over a 12 months period? When did this happen?
        System.out.println("Q12:");
        /*  BLACK BOX METHOD
        *   INPUT -> BOX -> OUTPUT
        *   interestByWeek -> BOX -> language that lost the most interest over a 12 month period and when in happened
        *       iBW -> BOX -> languages that lost interest
        *           langs lost int -> BOX ->
        *   get lost of interest period, put it to map of period to language
        *
        */
        TreeMap<Week,TreeMap<String,Integer>> temp = new TreeMap<>();

        for(int i=0; i<interestByWeek.size()-1;i++) {
            TreeMap<String,Integer> temp2 = new TreeMap<>();
            for (String lang: langs) {
                if (interestByWeek.get(interestByWeek.keySet().toArray()[i]).get(lang)
                        > interestByWeek.get(interestByWeek.keySet().toArray()[i + 1]).get(lang))
                    temp2.put(lang, interestByWeek.get(interestByWeek.keySet().toArray()[i]).get(lang)
                            -interestByWeek.get(interestByWeek.keySet().toArray()[i+1]).get(lang));
            }
            Week wk1 =(Week)interestByWeek.keySet().toArray()[i];
            Week wk2 =(Week)interestByWeek.keySet().toArray()[i+1];
            temp.put(new Week(wk1.start(),wk2.end()),temp2);
        }

        /*for(Week wk:temp.keySet())
            if(temp.get(wk).isEmpty())
                temp.remove(wk);*/

        for(Week wk:temp.keySet()) {
            System.out.println(wk.toString() + " " + temp.get(wk));
        }


        System.out.println("TESTE!!!");
        System.out.println(interestByWeek.get(interestByWeek.keySet().toArray()[0]));
        System.out.println(interestByWeek.get(interestByWeek.keySet().toArray()[1]));


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

        System.out.println("Q13: ");
        System.out.printf("Acad Year\tAvg Yr\tAvg Sep/Oct\n");


        for (int i=2004;i<2015;i++){
            float totalJava =0;
            int totalCount =0;
            float acadJava = 0;
            int acadCount =0;
            for (Week wk:interestByWeek.keySet())
                if(wk.start().isAfter(LocalDate.of(i,Month.JANUARY, 1)) && wk.start().isBefore(LocalDate.of(i+1,Month.JANUARY, 1))) {
                    totalJava += interestByWeek.get(wk).get("java");
                    totalCount++;
                }
            for (Week wk:interestByWeek.keySet())
                if(wk.start().isAfter(LocalDate.of(i,Month.SEPTEMBER, 1)) && wk.start().isBefore(LocalDate.of(i,Month.NOVEMBER, 1))) {
                    acadJava += interestByWeek.get(wk).get("java");
                    acadCount++;
                }
            System.out.printf("%s\t%1.2f\t%1.2f\t%s\n",i+"/"+(i+1),totalJava/totalCount,acadJava/acadCount,"java");
        }



    }
}