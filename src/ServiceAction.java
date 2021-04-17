import vehicle.Vehicle;

import java.time.LocalDateTime;

public class ServiceAction {
    Vehicle vehicle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    boolean parkingSpot;
    int parkingSpotId;
    int buildingId;
    boolean independent;
    CarServiceSpot css = null;
    IndependentCarServiceSpot icss = null;
    static int serviceInt = 0;
    int serviceID;


    public ServiceAction(Vehicle vehicle, LocalDateTime date, LocalDateTime plusDays, boolean parkingspot, ServiceWarehouse currentBuilding) {
        this.serviceID = ++serviceInt;
        this.vehicle = vehicle;
        this.startDate = date;
        this.endDate = plusDays;
        this.parkingSpot = parkingspot;
        this.buildingId = currentBuilding.buildingId;
    }
}

