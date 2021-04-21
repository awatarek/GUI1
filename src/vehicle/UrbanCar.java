package vehicle;

public class UrbanCar extends Vehicle {
    boolean aiAssistance;

    public UrbanCar(){
        super(vehicle.carType.urban);
        engine = new Engine(150,1500);
        aiAssistance = Math.random() < 0.5 ? true : false;
        size = (int) (random_double(1,3)*random_double(1,3)*1.5);
    }
}
