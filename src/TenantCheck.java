import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TenantCheck extends Thread{
    public static int everyTime = 10000;

    @Override
    public void run(){
        Timer timer = new Timer();
        timer.schedule(new CheckTenant(),0, everyTime);
    }
}

class CheckTenant extends TimerTask {
    OurDate ourDate = new OurDate();

    public void run(){
        ArrayList<Person> clients = DataCollector.customers;
        long now = ourDate.getMilisec();
        for(int i = 0; i<clients.size();i++){
            Person client = clients.get(i);
            for(int x =0;i<clients.get(i).rentInfo.size();x++){
                LocalDateTime time = clients.get(i).rentInfo.get(x).end;
                long tenentTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long diff = tenentTime - now;
                if(diff<0){
                    TenantAlert info = clients.get(i).rentInfo.get(x);
                    /*wyslil info o bledzie*/
                    boolean activeAlert = false;
                    for(TenantAlert ta: clients.get(i).tenentsAlerts){
                        if(ta.uid == info.uid){
                            activeAlert = true;
                        }
                    }
                    if(!activeAlert){
                        clients.get(i).tenentsAlerts.add(new TenantAlert(ourDate.getDate(), info.building, info.room, info.type,50, TenantAlertType.rentEndError, info.uid));
                    } else {
                        long datePLus = time.plusDays(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        if(datePLus < ourDate.getMilisec()){
                            clients.get(i).rentInfo.remove(info);
                            for(ServiceWarehouse sw: DataCollector.buildings.get(info.building).service){
                                for(ConsumerWarehouse cw: sw.storage){
                                    if(cw.renters.get(0).uid == client.uid){
                                        cw.renters = null;
                                        cw.items = null;
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
    }
}
