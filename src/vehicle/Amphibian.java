package vehicle;

public class Amphibian extends Vehicle {
    int maxKn;
    public Amphibian(){
        super(vehicle.carType.amphibian);
        maxKn = (int) random_double(2, 10);
        engine = new Engine(600,6500);
        size = (int) (random_double(1,3)*random_double(1,3)*1.5);
    }
}
