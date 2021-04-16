package vehicle;

public abstract class Vehicle {
    public Engine engine;
    public int size;
    public enum carType{
        offRoad,
        urban,
        motorcycle,
        amphibian
    }

    public Vehicle(carType ct){

    }
}
