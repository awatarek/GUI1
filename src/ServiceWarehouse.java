import java.util.ArrayList;

public class ServiceWarehouse {
    public ArrayList<CarServiceSpot> css = new ArrayList<>();
    public ArrayList<IndependentCarServiceSpot> icss = new ArrayList<>();
    public ArrayList<ConsumerWarehouse> storage = new ArrayList<>();
    public int space;
    public int freeSpace;
    public int storageSpace;


    public ServiceWarehouse(int space){
        this.space = space;
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
    }

    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }
}