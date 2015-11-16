import java.time.LocalDate;

/**
 * Created by antero on 14/11/15.
 */
public class Week implements Comparable<Week> {
    //Fields
    private LocalDate[] week = new LocalDate[2];

    //Methods
    public LocalDate start(){
        return this.week[0];
    }

    public LocalDate end(){
        return this.week[1];
    }

    @Override
    public String toString(){
        return String.format("%s - %s",this.start(),this.end());
    }

    // Constructors
    Week(LocalDate start, LocalDate end){
        this.week[0] = start;
        this.week[1] = end;
    }

    Week(String startString, String endString){

        String start[] = startString.split("-");
        String end[] = endString.split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(start[0]),Integer.parseInt(start[1]),Integer.parseInt(start[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(end[0]),Integer.parseInt(end[1]),Integer.parseInt(end[2]));

        this.week[0] = startDate;
        this.week[1] = endDate;
    }

    @Override
    public int compareTo(Week otherWeek) {
        return this.start().compareTo(otherWeek.start());
    }
}
