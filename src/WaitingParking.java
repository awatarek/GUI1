import vehicle.Vehicle;

public class WaitingParking {
    Vehicle vehicle;
    Person owner;

    public WaitingParking(Vehicle vehicle, Person currentUser){
        this.vehicle = vehicle;
        this.owner = currentUser;
    }
}
