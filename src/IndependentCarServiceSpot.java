import vehicle.Vehicle;

import java.time.LocalDateTime;

public class IndependentCarServiceSpot {
    public int space;
    public boolean ocupated = false;
    Vehicle vehicle;
    LocalDateTime endTime;
    static int spots = 0;
    int icssId;
    int jobId = 0;

    public IndependentCarServiceSpot(int space){
        this.icssId = spots++;
        this.space = space;
    }

}
