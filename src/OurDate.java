import java.time.LocalDateTime;
import java.time.ZoneId;

public class OurDate {
    static LocalDateTime time;
    static boolean firstTime = true;

    public OurDate(){
        if(firstTime){
            time = LocalDateTime.now();
            firstTime = false;
        }
    }

    public void addDay(){
        time = time.plusDays(1);
    }

    public LocalDateTime getDate(){
        return time;
    }

    public long getMilisec(){
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
