public class CarServiceSpot {
    int space;
    public boolean ocupated = false;
    static int spots = 0;
    int cssId;

    public CarServiceSpot(int space){
        this.cssId = spots++;
        this.space=space;
    }
}
