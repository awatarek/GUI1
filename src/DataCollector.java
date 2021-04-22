import vehicle.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;

public class DataCollector {
    public static ArrayList<Person> customers = new ArrayList<Person>();
    public static Person currentUser = null;
    public static ArrayList<ServiceWarehouse> buildings = new ArrayList<ServiceWarehouse>();
    public static ServiceWarehouse currentBuilding;
    public static ConsumerWarehouse currentRoom;

    public DataCollector(boolean test) {
        this.setupDemo();
        this.demoOwner();
    }

    public DataCollector() {
    }

    public void demoOwner() {
        OurDate od = new OurDate();
        for (int i = 0; i < this.buildings.size(); i++) {
            ServiceWarehouse building = this.buildings.get(i);

            for (int c = 0; c < building.storage.size(); c++) {
                if (c % 2 == 0) {
                    building.storage.get(c).renters.add(customers.get(0));
                    building.storage.get(c).startLease = od.getDate();
                    building.storage.get(c).endLease = od.getDate().plusDays(5);
                    building.storage.get(c).items.add(new Item("b", 20));
                    building.storage.get(c).items.add(new Item("b", 200));
                    building.storage.get(c).items.add(new Item("c", 20));
                }
            }

            for (int c = 0; c < building.parking.size(); c++) {
                if (c % 2 == 0) {
                    building.parking.get(c).renter = customers.get(0);
                    building.parking.get(c).ocupated = true;
                    building.parking.get(c).endOfRent = od.getDate().plusDays(5);
                }
            }
        }
        currentUser.firstRent = od.getDate();
    }

    private void setupDemo() {
        customers.add(new Person(0, "admin", "admin"));
        for (int i = 0; i < 20; i++) {
            customers.add(new Person());
        }
        for (int i = 0; i < 3; i++) {
            buildings.add(new ServiceWarehouse());
        }
        currentUser = customers.get(0);
        currentBuilding = buildings.get(0);
        currentRoom = buildings.get(0).storage.get(0);
    }

    public void changeUser(Person user) {
        currentUser = user;
        String info = currentUser.uid + " " + currentUser.firstName + " " + currentUser.lastName + " " + currentUser.pesel;
        System.out.println(info);
    }

    public Person getUser(int uid) {
        return (Person) customers.stream().filter(person -> {
            return person.uid == uid;
        }).findFirst().orElse(null);
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public void showUserData() {
        String info = currentUser.uid + " " + currentUser.firstName + " " + currentUser.lastName + " " + currentUser.pesel;
        System.out.println(info);
        if (currentUser.firstRent == null) {
            try {
                throw new NeverRentException();
            } catch (NeverRentException e) {
                e.printStackTrace();
            }
        }
        showRent();
    }

    public int random_int(int Min, int Max) {
        return (int) (Math.random() * (Max - Min)) + Min;
    }

    public void showRent() {
        ArrayList<ConsumerWarehouse> userStorage = new ArrayList<>();
        ArrayList<ParkingSpace> userParking = new ArrayList<>();

        for (int i = 0; i < this.buildings.size(); i++) {
            for (ConsumerWarehouse cw : buildings.get(i).storage) {
                if (cw.renters != null && cw.renters.size() != 0) {
                    if (cw.renters.get(0).equals(currentUser)) {
                        userStorage.add(cw);
                    }
                }
            }

            for (int b = 0; b < buildings.get(i).parking.size(); b++) {
                for (ParkingSpace pw : buildings.get(i).parking) {
                    if (pw.renter == currentUser) {
                        userParking.add(pw);
                    }
                }
            }
        }

        for (ConsumerWarehouse cw : userStorage) {
            System.out.println("Storage id: " + cw.cwid + " Leas to: " + cw.endLease.toString());
        }

        for (ParkingSpace ps : userParking) {
            System.out.println("Parking id: " + ps.parkingId + " Leas to: " + ps.endOfRent.toString());
        }
    }

    public void showFreeRooms(int buildingID) {

        ServiceWarehouse building = this.buildings.get(buildingID);

        for (ConsumerWarehouse cw : building.storage) {
            if (cw.renters == null || cw.renters.size() == 0) {
                System.out.println("cw" + cw.cwid);
            }
        }

        for (IndependentCarServiceSpot icss : building.icss) {
            if (icss.ocupated == false) {
                System.out.println("icss" + icss.icssId);
            }

        }

        for (CarServiceSpot css : building.css) {
            if (css.ocupated == false) {
                System.out.println("css" + css.cssId);
            }
        }

        for (ParkingSpace ps : building.parking) {
            if (ps.renter == null) {
                System.out.println("ps" + ps.parkingId);
            }
        }


    }

    public void chooseBuilding(int buildingId) {
        currentBuilding = buildings.get(buildingId);
        System.out.println("you choosed building " + currentBuilding.buildingId);
    }

    public void chooseRoom(int roomId) {
        if (currentBuilding == null) {
            System.out.println("Choose Building first!");
            return;
        }
        for (int a = 0; a < currentBuilding.storage.size(); a++) {
            ConsumerWarehouse nowCW = currentBuilding.storage.get(a);
            if (nowCW.cwid == roomId) {
                currentRoom = nowCW;
                System.out.println("you choosed room: " + currentRoom.cwid + " in building " + currentBuilding.buildingId);
                showItems();
                return;
            }
        }
    }

    public void showItems() {
        System.out.println("Items in room: ");
        if (currentRoom.items.size() == 0) {
            System.out.println("No items in room :( ");
            return;
        }
        for (int i = 0; i < currentRoom.items.size(); i++) {
            System.out.println(currentRoom.items.get(i).itemName);
        }
    }

    public void buyRoom() {
        OurDate od = new OurDate();
        if (!(currentRoom.renters.size() == 0)) {
            System.out.println("Somebody already bought this room!");
        } else if (isBadTenant()) {
            String errorMessage = "Osoba " + currentUser.firstName + " " + currentUser.lastName + " posiadała już najem pomieszczeń: \n";
            ArrayList<TenantAlert> alerts = getBadTenantsAlerts();
            for (TenantAlert al : alerts) {
                String type = al.alertType == TenantAlertType.parkingError ? "ps" : "cw";
                errorMessage += "Serwis: " + al.building + " Pomieszczenie: " + type + al.room + " - " + al.price + "\n";
            }

            try {
                throw new ProblematicTenantException(errorMessage);
            } catch (ProblematicTenantException e) {
                e.printStackTrace();
            }
        } else if (getUserTenantAlertsCost() > 1250) {
            System.out.println("Person rented rooms for  " + getUserTenantAlertsCost() + " and cannot buy another one.");
        } else {
            currentRoom.renters.add(currentUser);
            if (currentUser.firstRent == null) {
                currentUser.firstRent = od.getDate();
            }
            currentRoom.startLease = od.getDate();
            currentRoom.endLease = od.getDate().plusDays(5);
            currentUser.addRent(currentRoom.endLease, currentBuilding.buildingId, currentRoom.cwid, roomTypes.warehouse, 250);
            System.out.println("You bought the room!");
        }
    }

    private boolean isBadTenant() {
        ArrayList<TenantAlert> tena = currentUser.tenentsAlerts;
        int errors = 0;
        for (TenantAlert ta : tena) {
            if (ta.alertType == TenantAlertType.parkingError || ta.alertType == TenantAlertType.rentEndError) {
                errors++;
            }
        }
        return errors > 3;
    }

    private ArrayList<TenantAlert> getBadTenantsAlerts() {
        ArrayList<TenantAlert> tena = currentUser.tenentsAlerts;
        ArrayList<TenantAlert> badAlert = new ArrayList<>();
        for (TenantAlert ta : tena) {
            if (ta.alertType == TenantAlertType.parkingError || ta.alertType == TenantAlertType.rentEndError) {
                badAlert.add(ta);
            }
        }

        return badAlert;
    }

    public void addItem(String name, String space) throws Exception {
        int itemSpace = Integer.parseInt(space);
        int currentSpace = 0;
        for (int i = 0; i < currentRoom.items.size(); i++) {
            currentSpace += currentRoom.items.get(i).itemSpace;
        }
        if (currentRoom.space > (itemSpace + currentSpace)) {
            currentRoom.items.add(new Item(name, itemSpace));
            System.out.println("Item " + name + " has been added");
        } else {
            int freeSpace = currentRoom.space - currentSpace;
            throw new Exception("Not enough space for item: " + name + " " + itemSpace + ". Free space: " + freeSpace);
        }
    }

    public void removeItem(String itemId) {
        currentRoom.items.remove(Integer.parseInt(itemId));
        System.out.println("Item with ID: " + itemId + " has been deleted");
    }

    public void showRenters() {
        if (currentRoom.renters.size() == 0) {
            System.out.println("Room has no renters");
        } else {
            int renterId = 0;
            for (Person p : currentRoom.renters) {
                System.out.println(renterId + ": " + p.firstName + " " + p.lastName);
                renterId++;
            }
        }

    }

    public void addRenter(String newRenterID) {
        int clientId = Integer.parseInt(newRenterID);
        if (currentRoom.renters.size() == 0) {
            System.out.println("Room is nobody's property. Please buy it first");
        } else if (currentRoom.renters.get(0) == currentUser) {
            currentRoom.renters.add(customers.get(clientId));
        } else {
            System.out.println("you are not the room owner");
        }

    }

    public void removeRenter(String renterID) {
        if (currentRoom.renters.size() == 0) {
            System.out.println("Room is nobody's property. Please buy it first");
        } else if (currentRoom.renters.get(0) == currentUser) {
            if (Integer.parseInt(renterID) == currentRoom.renters.get(0).uid) {
                System.out.println("You cant remove owner from renters");
            } else {
                currentRoom.renters.remove(renterID);
            }
        } else {
            System.out.println("you are not the room owner");
        }
    }

    public void extendLease() {
        currentRoom.extendLease();
    }

    public int getUserTenantAlertsCost() {
        int costs = 0;
        for (int i = 0; i < this.getCurrentUser().rentInfo.size(); i++) {
            costs += this.getCurrentUser().rentInfo.get(i).price;
        }
        return costs;
    }

    public void showUsersItem() {
        ArrayList<RoomsInfos> userRooms = new ArrayList<>();
        OurDate od = new OurDate();
        for (int i = 0; i < currentUser.rentInfo.size(); i++) {
            TenantAlert ta = currentUser.rentInfo.get(i);
            long rentEndInMiliseconds = ta.end
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long nowInMiliseconds = od.getMilisec();
            if (rentEndInMiliseconds > nowInMiliseconds) {
                userRooms.add(new RoomsInfos(ta.building, ta.room));
            }
        }
        ArrayList<Item> items = new ArrayList<>();
        for (RoomsInfos rooms : userRooms) {
            for (ServiceWarehouse sw : buildings) {
                for (ConsumerWarehouse cw : sw.storage) {
                    if (cw.renters.size() != 0) {
                        if (cw.renters.get(0) == currentUser) {
                            for (Item item : cw.items) {
                                items.add(item);
                            }
                        }
                    }
                }
            }
        }

        for (Item item : items) {
            System.out.println("Item name: " + item.itemName + ", Item space: " + item.itemSpace);
        }

    }

    public void showServices() {
        for (int i = 0; i < buildings.size(); i++) {
            System.out.println("Service: " + i);
            for (IndependentCarServiceSpot icss : buildings.get(i).icss) {
                System.out.println("\t\t Independent Car Servise spot: " + icss.icssId + ", is ocupated: " + icss.ocupated);
            }
            for (CarServiceSpot css : buildings.get(i).css) {
                System.out.println("\t\t Car Servise spot: " + css.cssId + ", is ocupated: " + css.ocupated);
            }
        }
    }

    public void showAll() {
        for (ServiceWarehouse sw : buildings) {
            System.out.println("Service: " + sw.buildingId);
            for (ConsumerWarehouse cw : sw.storage) {
                System.out.println("\t\t Warehouse: " + cw.cwid + ", is ocupated: " + (cw.renters.size() > 0));
            }
            for (CarServiceSpot css : sw.css) {
                System.out.println("\t\t Independent Car Servise spot: " + css.cssId + ", is ocupated: " + css.ocupated);
            }
            for (IndependentCarServiceSpot icss : sw.icss) {
                System.out.println("\t\t Independent Car Servise spot: " + icss.icssId + ", is ocupated: " + icss.ocupated);
            }
            for (ParkingSpace ps : sw.parking) {
                System.out.println("\t\t Parking: " + ps.parkingId + ", ocupated: " + (ps.renter != null));
            }
        }
    }

    public void createServiceJob(String vehicleType, boolean parkingspot, boolean independent) {
        Vehicle vehicle = carGenerator(vehicleType);
        IndependentCarServiceSpot yourICSS = null;
        CarServiceSpot yourCSS = null;
        if (independent) {
            yourICSS = getFreeICCS(currentBuilding.buildingId);
        } else {
            yourCSS = getFreeCSS(currentBuilding.buildingId);
        }
        if (yourCSS != null || yourICSS != null) {
            getVehicleToService(vehicle, parkingspot, independent, yourICSS, yourCSS);
            System.out.println("Car is now being repaired");
        } else {
            currentBuilding.waitingWork.add(new WaitingService(currentUser, vehicle, independent ? WorkJob.icss : WorkJob.css, currentBuilding.buildingId, parkingspot));
            System.out.println("Car added to waiting list");
        }
    }

    public IndependentCarServiceSpot getFreeICCS(int building) {
        IndependentCarServiceSpot yourICSS = null;
        for (IndependentCarServiceSpot icss : buildings.get(building).icss) {
            if (icss.ocupated == false) {
                yourICSS = icss;
                System.out.println("Your car is repaired in: ICSS" + yourICSS.icssId);
                break;
            }
        }

        return yourICSS;
    }

    public CarServiceSpot getFreeCSS(int building) {
        CarServiceSpot yourCSS = null;
        for (CarServiceSpot css : buildings.get(building).css) {
            if (css.ocupated == false) {
                yourCSS = css;
                System.out.println("Your car is repaired in: CSS" + yourCSS.cssId);
                break;
            }
        }
        return yourCSS;
    }

    public void getVehicleToService(Vehicle vehicle, boolean parkingspot, boolean independent, IndependentCarServiceSpot yourICSS, CarServiceSpot yourCSS) {
        OurDate od = new OurDate();
        int repairsDays = random_int(1, 5);
        int price = independent ? (repairsDays * 50) : (repairsDays * 50) + (repairsDays * 60);
        ServiceAction sa = new ServiceAction(vehicle, od.getDate(), od.getDate().plusDays((long) repairsDays), parkingspot, currentBuilding, price);
        TenantAlert ta = new TenantAlert(od.getDate().plusDays(14), currentBuilding.buildingId, price, TenantAlertType.service);

        if (independent) {
            sa.independent = true;
            sa.icss = yourICSS;
            yourICSS.ocupated = true;
            yourICSS.endTime = sa.endDate;
            yourICSS.vehicle = sa.vehicle;
            ta.type = roomTypes.icss;
            ta.room = yourICSS.icssId;
            yourICSS.jobId = sa.serviceID;
        } else {
            sa.independent = false;
            sa.css = yourCSS;
            yourCSS.ocupated = true;
            yourCSS.endTime = sa.endDate;
            yourCSS.vehicle = sa.vehicle;
            ta.room = yourCSS.cssId;
            ta.type = roomTypes.css;
            yourCSS.jobId = sa.serviceID;
        }
        if (parkingspot) {
            ta.price += 150;
        }
        currentUser.serviceActions.add(sa);
        currentUser.rentInfo.add(ta);
    }

    private ParkingSpace searchFreeParking(int buildingId) {
        ParkingSpace freeSpot = null;
        ServiceWarehouse sw1 = null;
        for (ServiceWarehouse sw : buildings) {
            if (sw.buildingId == buildingId) {
                sw1 = sw;
            }
        }
        for (ParkingSpace ps : sw1.parking) {
            if (ps.renter == null) {
                freeSpot = ps;
                break;
            }
        }
        return freeSpot;
    }

    private Vehicle carGenerator(String vehicleType) {
        if (vehicleType.equals("offRoad")) {
            return new OffRoadCar();
        } else if (vehicleType.equals("urban")) {
            return new UrbanCar();
        } else if (vehicleType.equals("motorcycle")) {
            return new Motorcycle();
        } else if (vehicleType.equals("amphibian")) {
            return new Amphibian();
        } else {
            System.out.println("Wrong vehicle: posible errors on saveData");
            return new Vehicle(null) {
            };
        }
    }

    public void checkForParkingFreeSpace() {
        OurDate od = new OurDate();
        for (ServiceWarehouse sw : buildings) {
            if (sw.parkingWait.size() != 0) {
                for (int i = 0; i < sw.parkingWait.size(); i++) {
                    WaitingParking wp = sw.parkingWait.get(i);
                    ParkingSpace freeSpot = searchFreeParking(sw.buildingId);
                    if (freeSpot != null) {
                        freeSpot.ocupated = true;
                        freeSpot.renter = wp.owner;
                        freeSpot.endOfRent = od.getDate().plusDays(14);
                        freeSpot.vehicle = wp.vehicle;
                        sw.parkingWait.remove(wp);
                    }
                }
            }
        }
    }


    public void checkForServiceFreeSpace() {
        OurDate od = new OurDate();
        for (ServiceWarehouse sw : buildings) {
            for (int i = 0; i < sw.waitingWork.size(); i++) {
                WaitingService ws = sw.waitingWork.get(i);
                if (ws.wj == WorkJob.css) {
                    CarServiceSpot css = getFreeCSS(ws.building);
                    if (css != null) {
                        getVehicleToService(ws.vehicle, ws.parkingspot, false, null, css);
                        sw.waitingWork.remove(ws);
                    }
                } else {
                    IndependentCarServiceSpot icss = getFreeICCS(ws.building);
                    if (icss != null) {
                        getVehicleToService(ws.vehicle, ws.parkingspot, true, icss, null);
                        sw.waitingWork.remove(ws);
                    }
                }
            }
        }
    }

    public void removeLatePayments() {
        OurDate od = new OurDate();
        int position = 0;
        int iter = currentUser.tenentsAlerts.size();
        for (int i = 0; i < iter; i++) {
            TenantAlert alert = currentUser.tenentsAlerts.get(position);
            LocalDateTime aT = alert.end;
            LocalDateTime ld = LocalDateTime.of(aT.getYear(), aT.getMonth(), aT.getDayOfMonth(), aT.getHour(), aT.getMinute(), aT.getSecond());
            ld = ld.plusDays(30);
            if (od.getMilisec() < getMilisec(ld)) {
                currentUser.tenentsAlerts.remove(alert);
            } else {
                position++;
            }
        }
    }

    public long getMilisec(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void showAllServices() {
        OurDate od = new OurDate();
        for (ServiceAction sa : currentUser.serviceActions) {
            System.out.println("Service job: " + sa.serviceID + " in building: "
                    + sa.buildingId + " was independent: " + sa.independent + " is still active:" + (od.getMilisec() > getMilisec(sa.endDate)));
        }
    }

    public void showParking() {
        for (ServiceWarehouse sw : buildings) {
            System.out.println("Parking spots in building " + sw.buildingId);
            for (ParkingSpace ps : sw.parking) {
                String parkingSpot = "Spot: " + ps.parkingId + " is ocupated: " + ps.ocupated;
                parkingSpot += ps.ocupated ? ", ends: " + ps.endOfRent.toString() + ", renter: " + ps.renter.firstName + " " + ps.renter.lastName
                        : " ";
                System.out.println("\t" + parkingSpot);

            }
        }
    }

    public void freeParkingSpace(String arg) {
        for (ServiceWarehouse sw : buildings) {
            for (ParkingSpace ps : sw.parking) {
                if (ps.parkingId == Integer.parseInt(arg)) {
                    System.out.println("Vehicle " + ps.vehicle + " has been returend to owner: " + ps.renter.firstName + " " + ps.renter.lastName);
                    ps.ocupated = false;
                    ps.renter = null;
                    ps.endOfRent = LocalDateTime.now();
                    ps.vehicle = null;
                }
            }
        }
    }

    public void checkService(String arg, String arg2) {
        Boolean independent = Boolean.parseBoolean(arg2);
        if (independent) {
            for (IndependentCarServiceSpot icss : currentBuilding.icss) {
                if (icss.icssId == Integer.parseInt(arg)) {
                    System.out.println("icss " + icss.icssId + " is currently: " + icss.ocupated + (icss.ocupated ? " it will be in that state to: " + icss.endTime.toString() : ""));
                }
            }
        } else {
            for (CarServiceSpot css : currentBuilding.css) {
                if (css.cssId == Integer.parseInt(arg)) {
                    System.out.println("icss " + css.cssId + " is currently: " + css.ocupated + (css.ocupated ? " it will be in that state to: " + css.endTime.toString() : ""));
                }
            }
        }
    }

    public void saveWarehouse() {
        String fileMessage = "";
        for (ServiceWarehouse sw : buildings) {
            fileMessage += "Building " + sw.buildingId + ":\n\n";
            ArrayList<ConsumerWarehouse> ourList = new ArrayList<>();
            for (ConsumerWarehouse cw : sw.storage) {
                ourList.add(cw);
            }
            Collections.sort(ourList);
            for (ConsumerWarehouse cw : ourList) {
                ArrayList<Item> ourItems = new ArrayList<>();
                for (Item it : cw.items) {
                    ourItems.add(it);
                }
                Collections.sort(ourItems);

                fileMessage += "\t storage id: " + cw.cwid + ", space: " + cw.space;
                fileMessage += cw.renters.size() != 0 ? ", renter:" + cw.renters.get(0).firstName + " " + cw.renters.get(0).lastName + ", ends in:" + cw.endLease.toString() + "\n" : "\n";
                for (Item it : ourItems) {
                    fileMessage += "\t\t Item name: " + it.itemName + ", item space: " + it.itemSpace + "\n";
                }
            }
        }
        try {
            writeToFile("warehouses.txt", fileMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveServices() {
        String fileMessage = "";
        ArrayList<ServiceAction> saList = getAllServices();
        for (ServiceWarehouse sw : buildings) {
            fileMessage += "\nBuilding " + sw.buildingId + ":\n\n";
            fileMessage += "Independent Car Service Spots: \n";
            for (IndependentCarServiceSpot icss : sw.icss) {
                fileMessage += "\tICSS" + icss.icssId + ":  \n";
                if (icss.ocupated) {
                    fileMessage += "\t\tactive job: " + icss.ocupated + " Car: " + icss.vehicle.vehicleId + ", " + icss.vehicle.carType + " \n";
                }
                fileMessage += "\t\tHistory of jobs\n";
                for (ServiceAction sa : saList) {
                    if (sa.independent && sa.buildingId == sw.buildingId && sa.icss.icssId == icss.icssId && icss.vehicle != sa.vehicle) {
                        fileMessage += "\t\t\tCar: " + sa.vehicle.vehicleId + ", Type:" + sa.vehicle.carType + ", Price:" + sa.price + "\n";
                    }
                }
            }

            fileMessage += "\nCar Service Spots: \n";
            for (CarServiceSpot css : sw.css) {
                fileMessage += "\tCSS" + css.cssId + ":  \n";
                if (css.ocupated) {
                    fileMessage += "\t\tactive job: " + css.ocupated + " Car: " + css.vehicle.vehicleId + ", " + css.vehicle.carType + " \n";
                }
                fileMessage += "\t\tHistory of jobs:\n";
                for (ServiceAction sa : saList) {
                    if (!sa.independent && sa.buildingId == sw.buildingId && sa.css.cssId == css.cssId && css.vehicle != sa.vehicle) {
                        fileMessage += "\t\t\tCar: " + sa.vehicle.vehicleId + ", Type:" + sa.vehicle.carType + ", Price:" + sa.price + "\n";
                    }
                }
            }
        }

        try {
            writeToFile("services.txt", fileMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ServiceAction> getAllServices() {
        ArrayList<ServiceAction> saList = new ArrayList<>();
        for (Person p : customers) {
            for (ServiceAction sa : p.serviceActions) {
                saList.add(sa);
            }
        }
        return saList;
    }

    public void writeToFile(String fileName, String msg) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
        writer.write(msg);
        writer.close();
        System.out.println("File: " + fileName + " has been saved!");
    }
}


class RoomsInfos {
    int buildingNumber;
    int roomNumber;

    public RoomsInfos(int bid, int rid) {
        this.buildingNumber = bid;
        this.roomNumber = rid;
    }
}

