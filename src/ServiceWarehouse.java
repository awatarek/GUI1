import java.util.ArrayList;

public class ServiceWarehouse {
    public ArrayList<CarServiceSpot> css = new ArrayList<>();
    public ArrayList<IndependentCarServiceSpot> icss = new ArrayList<>();
    public ArrayList<ConsumerWarehouse> storage = new ArrayList<>();
    public ArrayList<ParkingSpace> parking = new ArrayList<>();
    public ArrayList<WaitingService> waitingWork = new ArrayList<>();
    public ArrayList<WaitingParking> parkingWait = new ArrayList<WaitingParking>();
    public int space;
    public int freeSpace;
    public int storageSpace;
    static int id = 0;
    int buildingId;

    public ServiceWarehouse(){
        this.buildingId = id++;
        int height = random_int(3,10);
        this.space = random_int(10,20)*random_int(10,20)*height;
        this.storageSpace = (random_int(10,20)*space/100);
        for(int i =0; i<2;i++){
            this.storage.add(new ConsumerWarehouse((this.storageSpace/2)));
        }

        this.freeSpace = (this.space - this.storageSpace);
        icss.add(new IndependentCarServiceSpot(20));
        freeSpace-=20;
        while (freeSpace > 20){
            css.add(new CarServiceSpot(70));
            freeSpace-=70;
        }

        int parkingSpaces = random_int(2,5);
        for(int i = 0; i<parkingSpaces;i++){
            parking.add(new ParkingSpace());
        }
    }

    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}