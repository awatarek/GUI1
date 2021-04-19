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

    public double random_double(int Min, int Max)
    {
        return (Math.random()*(Max-Min))+Min;
    }
}
