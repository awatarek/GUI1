package vehicle;

public class Motorcycle extends Vehicle {

    public Motorcycle(){
        super(vehicle.carType.motorcycle);
        engine = new Engine(100,1500);
        size = 1;
    }

}
