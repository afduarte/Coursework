import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

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
        // Stream the values in world
        world.values().stream()
                // filter for the countries that have the year 2010 and have shown interest in 2 languages
                .filter(c -> c.hasYear(2010) && c.getInterestByLang(2010).size() == 2)
                // for each country that is still in the stream, print the name and the languages it has shown interest in
                .forEach(c ->System.out.printf("\t%s has shown interest in: %s and %s\n",c.getName(),c.getInterestByLang(2010).keySet().toArray()[0],c.getInterestByLang(2010).keySet().toArray()[1]));

        //Question 8: Which are the least popular programming languages in the United Kingdom for each of the years 2009 to 2014?
        System.out.println("Q8");
        // Go through the years 2009 to 2014
        for(int i =2009;i<=2014;i++)
            // Check if there is data for the specified country in the specified year
            if(world.get("United Kingdom").hasYear(i))
                // Print the year and get the least popular language by using the method popInYear provided by the country class
                System.out.printf("\tLeast popular for year %d is: %s\n",i,world.get("United Kingdom").popInYear(i,"min"));


        //Hard ones:
        System.out.println("\nHard Ones:");
        //Question 11: Which are the top 5 regions that demonstrated significant growth of interests in programming languages in general?
        //Significant growth will be 20% more than the interest in first year
        System.out.println("\nQ11:");

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



        //Question 12: Which programming language set the record for losing the most interests over a 12 months period?
        // When did this happen?
        System.out.println("\nQ12:");
        //Create a clone of the interestWeek Map that indexes by the starting LocalDate instead of a pair of LocalDates
        TreeMap<LocalDate,TreeMap<String,Integer>> interestByStartDate = new TreeMap<>();
        for(Map.Entry entry:interestByWeek.entrySet()) {
            Week wk = (Week)entry.getKey();
            TreeMap<String,Integer> val = (TreeMap<String,Integer>) entry.getValue();
            interestByStartDate.put(wk.start(),val);
        }


        //Week holds two values of type LocalDate. Even though it wasn't created to work like this,
        // it can hold values with a 12 month difference, being perfect to hold the LocalDates for this case.

        //Initialize values that will be needed to print the answer and keep track of the biggest loss of interest so far
        ArrayList<Week> lossPair = null;
        String lossLang = "";
        int loss = 0;

        //Go through interestByStartDate
        for(LocalDate wk:interestByStartDate.keySet()) {
            // Check every language returned for that week
            for (String lang : langs) {
                // wk1 holds the interest for the language "lang" in the week "wk"
                int wk1 = interestByStartDate.get(wk).get(lang);
                // Get list of weeks that exist in the map and are 12 months after the week "wk" (within 1 week tolerance)
                ArrayList<LocalDate> closestArray = getCloseWeeks(interestByStartDate, wk);
                // If closestArray is not empty and for each Date in said array, get the interest for each language "lang"
                // and act accordingly
                if (!closestArray.isEmpty()) {
                    for(LocalDate closest:closestArray) {
                        int wk2 = interestByStartDate.get(closest).get(lang);
                        /* There are 2 different scenarios:
                         *      1. The language "lang" is the same as the current lossLang (language with highest loss)
                         *         and the loss (difference between wk1 and wk2) is the same value.
                         *      2. The loss is higher than the current highest and the language is either the same or a
                         *         different one (doesn't matter because even if it is the same language but there are
                         *         different loss values, the Array would have to be cleared).
                         */
                        if(lang.equals(lossLang) && wk1-wk2==loss) {
                            // If 1 is the case, lossPair ArrayList is initialized if it is null, and the 12 month period
                            // is added to it as an object of type Week.
                            if (lossPair == null)
                                lossPair = new ArrayList<>();
                            lossPair.add(new Week(wk, closest));
                            // If 2 is the case, the ArrayList is cleared and the 12 month period is added to it as an
                            // object of type Week. The current values are updated: lossLang and loss
                        }   else if(wk1-wk2 > loss){
                            // Even though clearing the existing ArrayList instead of creating a new one would be more
                            // efficient, not having to check if it was initialized before (null check) simplifies the code.
                            lossPair = new ArrayList<>();
                            lossPair.add(new Week(wk, closest));
                            lossLang = lang;
                            loss = wk1-wk2;
                        }
                    }
                }
            }
        }
        //Print the results. Used iterator for loop to easily detect the last time it loops and print a new line character.
        System.out.printf("%s lost the most interest(%d points) in the following 12 month periods: ",lossLang,loss);
        for (Iterator<Week> iterator = lossPair.iterator(); iterator.hasNext(); ) {
            Week wk = iterator.next();
            System.out.printf("|%s| ", wk);
            if(!iterator.hasNext())
                System.out.printf("\n");
        }


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

        System.out.println("\nQ13: ");
        System.out.printf("Acad Year\tAvg Yr\tAvg Sep/Oct\n");

        // Will iterate through the years 2004 to 2014, because 2015 has no data for academic months
        for (int i=2004;i<2015;i++){
            //Initialize variables
            float totalJava =0; // Holds the total interest throughout the year
            int totalCount =0;  // Holds the number of weeks included
            float acadJava = 0; // Holds the total interest for the academic months
            int acadCount =0;   // Holds the number of weeks included
            String lang = "java";// Holds the language to calculate for
            for (Week wk:interestByWeek.keySet()) {
                // Check if the week is inside the current year
                // (week start is after 1st of September of the current year and week end is before 1st of September of the next year)
                if (wk.start().isAfter(LocalDate.of(i, Month.SEPTEMBER, 1)) && wk.start().isBefore(LocalDate.of(i + 1, Month.SEPTEMBER, 1))) {
                    // Accumulates interest for the language and increments the counter
                    totalJava += interestByWeek.get(wk).get(lang);
                    totalCount++;
                }
                // Check if the week is inside the current academic year
                // (week start is after 31st of August of the current year and week end is before 1st of November of the current year)
                if (wk.start().isAfter(LocalDate.of(i, Month.AUGUST, 31)) && wk.start().isBefore(LocalDate.of(i, Month.NOVEMBER, 1))) {
                    // Accumulates interest for the language and increments the counter
                    acadJava += interestByWeek.get(wk).get(lang);
                    acadCount++;
                }
            }
            // Prints "Academic Year", "Language average for whole year", "Language average for academic months", "Language"
            System.out.printf("%s\t%1.2f\t%1.2f\t%s\n",i+"/"+(i+1),totalJava/totalCount,acadJava/acadCount,lang);
        }







    }
    // Method to find a match in the map for the closest week 12 months after the input date
    // Returns an ArrayList of all the matches with a tolerance of one week before the date and one week after the date
    public static ArrayList<LocalDate> getCloseWeeks(TreeMap<LocalDate,?> map, LocalDate originalDate){
        //Takes in originalDate and adds 12 months
        LocalDate twelveAfter = originalDate.plusMonths(12);
        //Create an ArrayList to hold the LocalDate values that match the criteria
        ArrayList<LocalDate> answer = new ArrayList<>();
        // Go through the input map and check if it is within the tolerance timespan
        for(LocalDate wk: map.keySet()){                                                        // Although a stream API
            if(twelveAfter.isAfter(wk.minusWeeks(1)) && twelveAfter.isBefore(wk.plusWeeks(1)))  // call could have been
                answer.add(wk);                                                                 // used, there was a
        }                                                                                       // performance loss in doing so.
        // Returns the ArrayList
        return answer;
    }
}
