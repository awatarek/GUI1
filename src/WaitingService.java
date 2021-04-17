import vehicle.Vehicle;

public class WaitingService {
    Person owner;
    Vehicle vehicle;
    boolean parkingspot;
    WorkJob wj;
    int building;
    public WaitingService(Person owner, Vehicle vehicle,WorkJob wj, int building, boolean parkingspot){
        this.owner = owner;
        this.vehicle = vehicle;
        this.wj = wj;
        this.building = building;
        this.parkingspot = parkingspot;
    }
}

enum WorkJob{
    icss,
    css
}