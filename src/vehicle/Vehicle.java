package vehicle;

public abstract class Vehicle {
    public static int id = 0;
    public int vehicleId;
    public Engine engine;
    public int size;
    public carType carType;


    public Vehicle(carType ct){
        vehicleId = id++;
        this.carType = ct;
    }

    public double random_double(int Min, int Max)
    {
        return (Math.random()*(Max-Min))+Min;
    }
}

enum carType{
    offRoad,
    urban,
    motorcycle,
    amphibian
}