package vehicle;

public class Amphibian extends Vehicle {
    public Amphibian(){
        super(carType.amphibian);
        engine = new Engine(600,6500);
        size = (int) (random_double(1,3)*random_double(1,3)*1.5);
    }
}
