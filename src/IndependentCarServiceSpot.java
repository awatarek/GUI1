public class IndependentCarServiceSpot {
    public int space;
    public boolean ocupated = false;
    static int spots = 0;
    int icssId;

    public IndependentCarServiceSpot(int space){
        this.icssId = spots++;
        this.space = space;
    }

}
