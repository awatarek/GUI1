import vehicle.Vehicle;

import java.time.LocalDateTime;

public class CarServiceSpot {
    int space;
    public boolean ocupated = false;
    static int spots = 0;
    int cssId;
    Vehicle vehicle;
    LocalDateTime endTime;
    int jobId = 0;

    public CarServiceSpot(int space){
        this.cssId = spots++;
        this.space=space;
    }
}
