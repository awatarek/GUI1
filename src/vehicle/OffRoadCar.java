package vehicle;

public class OffRoadCar extends Vehicle {
    boolean fourXfour;

    public OffRoadCar() {
        super(carType.offRoad);
        engine = new Engine(300,2500);
        fourXfour = Math.random() < 0.5 ? true : false;
        size = (int) (random_double(1,3)*random_double(1,3)*1.5);
    }
}
