import vehicle.Vehicle;

import java.time.LocalDateTime;

public class ServiceAction {
    Vehicle vehicle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean parkingSpot;
    int parkingSpotId;
    int buildingId;


    public ServiceAction(Vehicle vehicle, LocalDateTime date, LocalDateTime plusDays, boolean parkingspot, Building currentBuilding) {
        this.vehicle = vehicle;
        this.startDate = date;
        this.endDate = plusDays;
        this.parkingSpot = parkingspot;
        this.buildingId = currentBuilding.buildingId;
    }
}
