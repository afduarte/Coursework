import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by antero on 12/11/15.
 */
public class Country {
    //Fields
    private String country;
    private TreeMap<Integer,TreeMap<String,Integer>> langInterestByYear;

    private String[] langs = new String[]{"java","c++","c#","python","JavaScript"};
    // Methods
    public void setYear(int year,int[] interestInLang){
        //TreeMap<Integer,TreeMap<String,Integer>> interestByYear = new TreeMap<>();
        TreeMap<String,Integer> interestByLang = new TreeMap<>();
        interestByLang.put("java", interestInLang[0]);
        interestByLang.put("c++", interestInLang[1]);
        interestByLang.put("c#", interestInLang[2]);
        interestByLang.put("python", interestInLang[3]);
        interestByLang.put("JavaScript", interestInLang[4]);
        this.langInterestByYear.put(year,interestByLang);
    }

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

    public int getInterest(int year,String lang) {
        return this.langInterestByYear.get(year).get(lang);
    }
    public int getCombinedInterest(int year){
        int combined=0;
        for(String lang:langs)
            combined+= this.getInterest(year,lang);
        return combined;

    }

    public boolean hasYear(int year){
        return this.langInterestByYear.containsKey(year);
    }

    public TreeMap<Integer,Integer> getInterestByYear(String lang) {
        TreeMap<Integer,Integer> interestInLangByYear = new TreeMap<>();
        for(int year:this.getYears()) {
            int interestInLang = this.langInterestByYear.get(year).get(lang);
            if(interestInLang!=0)
                interestInLangByYear.put(year,interestInLang);
        }
        return interestInLangByYear;
    }

    public TreeMap<String,Integer> getInterestByLang(int year) {
        TreeMap<String,Integer> interestInLangByYear = new TreeMap<>();
        for (String lang : langs) {
            int interestInLang = langInterestByYear.get(year).get(lang);
            if (interestInLang != 0)
                interestInLangByYear.put(lang, interestInLang);
        }
        return interestInLangByYear;
    }

    public String getName() {
        return this.country;
    }

    public ArrayList<Integer> getYears(){
        return new ArrayList<>(this.langInterestByYear.keySet());
    }

    @Override
            public String toString(){
        String years = this.langInterestByYear.keySet().toString();
        return String.format("This country is %s, I have data from the following years: %s",this.country,years);
    }

    //Constructors

    Country(String name){
        this.country = name;
        langInterestByYear = new TreeMap<>();

    }
}
