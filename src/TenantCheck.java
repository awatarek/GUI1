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
    DataCollector dc = new DataCollector();

    public void run() {
        ArrayList<Person> customers = dc.customers;
        long now = ourDate.getMilisec();

        for(ServiceWarehouse sw : dc.buildings){
            for(ConsumerWarehouse cw : sw.storage){
                if(cw.endLease != null && cw.renters != null && cw.renters.size() != 0){
                    if(getMilisec(cw.endLease) < now){
                        boolean hasAlert = false;
                        for(TenantAlert ta : cw.renters.get(0).tenentsAlerts){
                            if(ta.room == cw.cwid && cw.endLease == ta.end){
                                hasAlert = true;
                            }
                        }
                        if(!hasAlert){
                            System.out.println("test");
                            cw.renters.get(0).tenentsAlerts.add(new TenantAlert(cw.endLease, sw.buildingId, cw.cwid,roomTypes.warehouse ,200, TenantAlertType.rentEndError));
                        }
                        LocalDateTime time = LocalDateTime.of(cw.endLease.getYear(), cw.endLease.getMonth(), cw.endLease.getDayOfMonth(), cw.endLease.getHour(), cw.endLease.getMinute());
                        if(getMilisec(time.plusDays(30)) < now){
                            cw.renters.clear();
                            cw.items.clear();
                        }
                    }
                }
            }
            for(IndependentCarServiceSpot icss : sw.icss){
                if(icss.endTime != null){
                    if(getMilisec(icss.endTime) < now){
                        ServiceAction ac = getServiceAction(icss.jobId);
                        if(ac.parkingSpot){
                            getVehicleToParking(ac, getCustomer(icss.jobId));
                        }
                        icss.jobId = 0;
                        icss.ocupated = false;
                    }
                }
            }
            for(CarServiceSpot css : sw.css){
                if(css.endTime != null) {
                    if (getMilisec(css.endTime) < now) {
                        ServiceAction ac = getServiceAction(css.jobId);
                        if (ac.parkingSpot) {
                            getVehicleToParking(ac, getCustomer(css.jobId));
                        }
                        css.jobId = 0;
                        css.ocupated = false;
                    }
                }
            }
            for(ParkingSpace ps : sw.parking){
                if(ps.endOfRent != null && ps.renter != null){
                    if(getMilisec(ps.endOfRent) < now){
                        ps.renter.tenentsAlerts.add(new TenantAlert(ps.endOfRent,sw.buildingId, 50, TenantAlertType.parkingError));
                        ps.renter = null;
                        ps.ocupated = false;
                        ps.vehicle = null;
                    }
                }
            }
        }
        for(Person client : customers){
            for(int x =0;x<client.rentInfo.size();x++){
                LocalDateTime time = client.rentInfo.get(x).end;
                long tenentTime = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long diff = tenentTime - now;
                if(diff<0){
                    TenantAlert info = client.rentInfo.get(x);
                    /*wyslil info o bledzie*/
                    boolean activeAlert = false;
                    for(TenantAlert ta: client.tenentsAlerts){
                        if(ta.uid == info.uid){
                            activeAlert = true;
                        }
                    }
                    if(!activeAlert){
                        client.tenentsAlerts.add(new TenantAlert(ourDate.getDate(), info.building, info.room, info.type,50, TenantAlertType.rentEndError, info.uid));
                    } else {
                        long datePLus = time.plusDays(30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        if(datePLus < ourDate.getMilisec()){
                            client.rentInfo.remove(info);
                            for(ConsumerWarehouse cw: DataCollector.buildings.get(info.building).storage){
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

    public long getMilisec(LocalDateTime ldt){
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public ServiceAction getServiceAction(int saId){
        ServiceAction SeA = null;
        for(Person customer: dc.customers){
            for(ServiceAction sa : customer.serviceActions){
                System.out.println(sa.serviceID);
                if(sa.serviceID == saId){
                    SeA = sa;
                }
            }
        }
        return SeA;
    }

    public Person getCustomer(int saId){
        Person user = null;
        for(Person customer: dc.customers){
            for(ServiceAction sa : customer.serviceActions){
                if(sa.serviceID == saId){
                    customer = customer;
                }
            }
        }
        return user;
    }

    public void getVehicleToParking(ServiceAction sa, Person customer){
        DataCollector dc = new DataCollector();
        ParkingSpace parking = null;
        if(sa.parkingSpot){
            parking = searchFreeParking(sa.buildingId);
        }
        if(parking.parkingId < 1000){
            sa.parkingSpotId = parking.parkingId;
            parking.renter = getCustomer(sa.serviceID);
            parking.ocupated = true;
            parking.endOfRent = sa.endDate;
        } else if (parking == null){
            System.out.println("There are no free parking, your vehicle will be added to parking wait list");
            dc.buildings.get(sa.buildingId).parkingWait.add(new WaitingParking(sa.vehicle, getCustomer(sa.buildingId)));
        }

        ParkingSpace ps = dc.buildings.get(sa.buildingId).parking.get(sa.parkingSpotId);
        ps.endOfRent = ourDate.getDate().plusDays(14);
        ps.renter = customer;
        ps.ocupated = true;

    }

    private ParkingSpace searchFreeParking(int buildingId) {
        ParkingSpace freeSpot = null;
        ServiceWarehouse sw = (ServiceWarehouse) dc.buildings.stream().filter(building -> {
            return building.buildingId == buildingId;
        });
        for(ParkingSpace ps : sw.parking){
            if(ps.renter == null) {
                freeSpot = ps;
                break;
            }
        }
        return freeSpot;
    }

}
