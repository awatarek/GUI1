import java.time.LocalDateTime;
import java.util.ArrayList;

public class ConsumerWarehouse {
    public int space;
    public int servicePartSpace;
    private static int id = 0;
    public int cwid;
    public ArrayList<Item> items = new ArrayList<>();
    //if more items than space TooManyThingsException - Remove some old items to insert a new item
    LocalDateTime startLease;
    LocalDateTime endLease;
    public ArrayList<Person> renters = new ArrayList<>();

    public ConsumerWarehouse(int space){
        OurDate date = new OurDate();
        this.space = space;
        this.servicePartSpace = (10*space/100);
        this.space -= this.servicePartSpace;
        this.cwid = id++;
        this.startLease = date.getDate();
        this.endLease = date.getDate();
    }


    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    public void extendLease() {
        endLease.plusDays(14);
        System.out.println("Lease extended!");
    }
}
