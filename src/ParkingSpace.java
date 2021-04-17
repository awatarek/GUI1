import vehicle.Vehicle;

import java.time.LocalDateTime;

public class ParkingSpace {
    public int parkingId;
    public static int id = 0;
    public int space;
    public boolean ocupated;
    public Person renter;
    public LocalDateTime endOfRent;
    public Vehicle vehicle;

    public ParkingSpace() {
        parkingId = id++;
        space = random_int(2, 3) * random_int(2, 3);
        renter = null;
    }

    public ParkingSpace(Vehicle veh){
        parkingId = id++;
        space = random_int(2, 3)*random_int(2, 3);
        renter = null;
        this.vehicle = veh;
    }

    public ParkingSpace(int id, Vehicle veh){
        parkingId = id;
        space = random_int(2, 3)*random_int(2, 3);
        renter = null;
        this.vehicle = veh;
    }


    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}
