package vehicle;

public class Motorcycle extends Vehicle {
    int wheels_count;
    public Motorcycle(){
        super(vehicle.carType.motorcycle);
        wheels_count = random_double(10, 20) > 15 ? 2 : 3;
        engine = new Engine(100,1500);
        size = 1;
    }

}
