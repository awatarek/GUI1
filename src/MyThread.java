import java.util.Timer;
import java.util.TimerTask;

public class MyThread extends Thread{
    public static int everyTime = 5000;

    @Override
    public void run(){
        Timer timer = new Timer();
        timer.schedule(new ChangeDate(),0, everyTime);
    }
}

class ChangeDate extends TimerTask {
    OurDate ourDate = new OurDate();
    DataCollector dc = new DataCollector();
    public void run() {
        ourDate.addDay();
        dc.checkForParkingFreeSpace();
        dc.checkForServiceFreeSpace();
    }

}
