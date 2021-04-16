import java.util.Date;

public class ParkingSpace {
    public int parkingId;
    public static int id = 0;
    public int space;
    public boolean ocupated;
    public Person renter;
    public Date endOfRent;

    public ParkingSpace(){
        parkingId = id++;
        space = random_int(2, 3)*random_int(2, 3);
        renter = null;
    }

    public ParkingSpace(int id){
        parkingId = id;
        renter = null;
    }

    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}
