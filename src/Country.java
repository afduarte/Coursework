import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Antero on 12/11/15.
 * Class country stores all the data for a country in a TreeMap of Integer(year) to
 * a TreeMap of String(language) to Integer(interest in language)
 *
 */
public class Country {
    //Fields
    private String country; // The name of the country
    private TreeMap<Integer,TreeMap<String,Integer>> langInterestByYear; // A TreeMap that holds the TreeMap of language to interest against an Integer key (the year)

    private String[] langs = new String[]{"java","c++","c#","python","JavaScript"}; // The list of languages
    // Methods
    public void setYear(int year,int[] interestInLang){
        //Takes a year and an array of Integer(interest in each language) and puts them into a TreeMap
        //That TreeMap is then put into the TreeMap langInterestByYear of this country
        TreeMap<String,Integer> interestByLang = new TreeMap<>();
        for(int i=0;i<langs.length;i++)
            interestByLang.put(langs[i],interestInLang[i]);
        this.langInterestByYear.put(year,interestByLang);
    }
    // Method to get either the most or the least popular language in a specified year.
    public String popInYear(int year,String maxOrmin){
        int valOfPop;
        switch (maxOrmin.toLowerCase()){
            case "max":
                valOfPop=(Collections.max(this.getInterestByLang(year).values()));
                String mostPop="";
                for (Map.Entry<String, Integer> entry : this.getInterestByLang(year).entrySet()) {
                    if (entry.getValue()==valOfPop) {
                        mostPop = entry.getKey();
                    }
                }
                return mostPop;
            case "min":
                valOfPop=(Collections.min(this.getInterestByLang(year).values()));
                String leastPop="";
                for (Map.Entry<String, Integer> entry : this.getInterestByLang(year).entrySet()) {
                    if (entry.getValue()==valOfPop) {
                        leastPop = entry.getKey();
                    }
                }
                return leastPop;
            default:
                return "You must pick 'max' or 'min'";
        }
    }

    // Method to return an Integer that represents the interest in a given language for a given year
    public int getInterest(int year,String lang) {
        return this.langInterestByYear.get(year).get(lang);
    }

    // Method to return a sum of all the interest values in all the languages for a given year.
    public int getCombinedInterest(int year){
        // Checks if there is data for this country, if there is no data, returns 0.
        if(this.hasYear(year)) {
            // If there is data, accumulates the interest for every language
            int combined = 0;
            for (String lang : langs)
                combined += this.getInterest(year, lang);
            return combined;
        } else return 0;
    }

    // Checks if the country has the given year by checking against the keys in the TreeMap.
    // Returns a boolean value
    public boolean hasYear(int year){
        return this.langInterestByYear.containsKey(year);
    }

    // Returns a TreeMap of the interest in the different languages if the interest value is higher than 0
    // Organized by year
    public TreeMap<Integer,Integer> getInterestByYear(String lang) {
        TreeMap<Integer,Integer> interestInLangByYear = new TreeMap<>();
        for(int year:this.getYears()) {
            int interestInLang = this.langInterestByYear.get(year).get(lang);
            if(interestInLang > 0)
                interestInLangByYear.put(year,interestInLang);
        }
        return interestInLangByYear;
    }

    // Returns a TreeMap of the interest in the different languages if the interest value is higher than 0
    // Organized by language
    public TreeMap<String,Integer> getInterestByLang(int year) {
        TreeMap<String,Integer> interestInLangByYear = new TreeMap<>();
        for (String lang : langs) {
            int interestInLang = langInterestByYear.get(year).get(lang);
            if (interestInLang > 0)
                interestInLangByYear.put(lang, interestInLang);
        }
        return interestInLangByYear;
    }

    // Returns the name of the country
    public String getName() {
        return this.country;
    }

    // Returns an ArrayList of every year for which this country has data for
    public ArrayList<Integer> getYears(){
        return new ArrayList<>(this.langInterestByYear.keySet());
    }

    @Override
    // Simply provides a toString method.
    public String toString(){
        String years = this.langInterestByYear.keySet().toString();
        return String.format("This country is %s, I have data from the following years: %s",this.country,years);
    }

    //Constructors
    //Takes in a String(country name) and initializes a new empty TreeMap to hold the data for the country
    Country(String name){
        this.country = name;
        langInterestByYear = new TreeMap<>();

    }
}
