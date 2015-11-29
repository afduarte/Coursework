import java.time.LocalDate;

/**
 * Created by Antero on 14/11/15.
 *
 * Class Week turns an input of two strings in the format YYYY-MM-DD into LocalDate Objects
 * Making it possible to use all the methods in the LocalDate class rather than just having a String.
 * e.g.: Week.start().getYear() ---> Returns an integer that represents the year in that week.
 *       Week.start().isBefore(Week anotherWeek) ---> returns true if the first week is chronologically before the second week
 * Week implements Comparable, making it possible to compare two weeks thus, iterating through a structure that uses weeks as key
 */
public class Week implements Comparable<Week> {
    //Week is an array of LocalDate that contains two values, a start date and an end date.
    //Fields
    private LocalDate[] week = new LocalDate[2];

    //Methods
    //start() returns the LocalDate of the start of the Week
    public LocalDate start(){
        return this.week[0];
    }

    //end() returns the LocalDate of the end of the Week
    public LocalDate end(){
        return this.week[1];
    }

    @Override
    //toString() Override method returns both dates in a readable format: YYYY-MM-DD - YYYY-MM-DD
    public String toString(){
        return String.format("%s - %s",this.start(),this.end());
    }

    // Constructors
    Week(LocalDate start, LocalDate end){
        this.week[0] = start;
        this.week[1] = end;
    }

    Week(String startString, String endString){
        //The input strings are split on the "-" character and turned into a LocalDate.of(YYYY,MM,DD)
        String start[] = startString.split("-");
        String end[] = endString.split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(start[0]),Integer.parseInt(start[1]),Integer.parseInt(start[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(end[0]),Integer.parseInt(end[1]),Integer.parseInt(end[2]));

        this.week[0] = startDate;
        this.week[1] = endDate;
    }

    @Override
    // Override method to make Week objects comparable. The use of the .start() method makes the comparison between
    // two LocalDate values, which already has a well defined compareTo() method.
    public int compareTo(Week otherWeek) {
        return this.start().compareTo(otherWeek.start());
    }
}
