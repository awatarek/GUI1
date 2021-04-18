import vehicle.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

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
                }
            }

            for (int c = 0; c < building.parking.size(); c++) {
                if (c % 2 == 0) {
                    building.parking.get(c).renter = customers.get(0);
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
        if(currentUser.firstRent == null){
            try {
                throw new NeverRentException();
            } catch (NeverRentException e) {
                e.printStackTrace();
            }
        }
        showRent();
    }

    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    public void showRent() {
        ArrayList<ConsumerWarehouse> userStorage = new ArrayList<>();
        ArrayList<ParkingSpace> userParking = new ArrayList<>();

        for (int i = 0; i < this.buildings.size(); i++) {
            for (ConsumerWarehouse cw: buildings.get(i).storage) {
                if(cw.renters != null && cw.renters.size() != 0){
                    if(cw.renters.get(0).equals(currentUser)){
                        userStorage.add(cw);
                    }
                }
            }

            for (int b = 0; b < buildings.get(i).parking.size(); b++) {
                for(ParkingSpace pw : buildings.get(i).parking){
                    if(pw.renter == currentUser){
                        userParking.add(pw);
                    }
                }
            }
        }

        for(ConsumerWarehouse cw : userStorage){
            System.out.println("Storage id: "+cw.cwid+" Leas to: "+cw.endLease.toString());
        }

        for(ParkingSpace ps : userParking){
            System.out.println("Parking id: "+ps.parkingId+" Leas to: "+ps.endOfRent.toString());
        }
    }

    public void showFreeRooms(int buildingID) {

        ServiceWarehouse building = this.buildings.get(buildingID);

        for(ConsumerWarehouse cw: building.storage){
            if (cw.renters == null || cw.renters.size() == 0) {
                System.out.println("cw"+cw.cwid);
            }
        }

        for(IndependentCarServiceSpot icss: building.icss){
            if (icss.ocupated == false) {
                System.out.println("icss"+icss.icssId);
            }

        }

        for(CarServiceSpot css: building.css){
            if (css.ocupated == false) {
                System.out.println("css"+css.cssId);
            }
        }

        for(ParkingSpace sp : building.parking){
            if (sp.renter == null) {
                System.out.println("sp"+sp.parkingId);
            }
        }


    }

    public void chooseBuilding(int buildingId) {
        currentBuilding = buildings.get(buildingId);
        System.out.println("you choosed building "+currentBuilding.buildingId);
    }

    public void chooseRoom(int roomId) {
            if(currentBuilding == null){
                System.out.println("Choose Building first!");
                return;
            }
            for(int a = 0; a < currentBuilding.storage.size(); a++){
                ConsumerWarehouse nowCW = currentBuilding.storage.get(a);
                if(nowCW.cwid == roomId){
                    currentRoom = nowCW;
                    System.out.println("you choosed room: "+currentRoom.cwid+" in building "+currentBuilding.buildingId);
                    showItems();
                    return;
                }
            }
    }

    public void showItems() {
        System.out.println("Items in room: ");
        if(currentRoom.items.size() == 0){
            System.out.println("No items in room :( ");
            return;
        }
        for(int i =0; i<currentRoom.items.size(); i++){
            System.out.println(currentRoom.items.get(i).itemName);
        }
    }

    public void buyRoom() {
        OurDate od = new OurDate();
        if(!(currentRoom.renters.size()==0)){
            System.out.println("Somebody already bought this room!");
        } else if (isBadTenant()){
            String errorMessage = "Osoba "+currentUser.firstName+ ""+currentUser.lastName+" posiadała już najem pomieszczeń: \n";
            ArrayList<TenantAlert> alerts = getBadTenantsAlerts();
            for(TenantAlert al : alerts){
                    String type = al.alertType == TenantAlertType.parkingError ? "ps" : "cw";
                    errorMessage += "Serwis: "+al.building+" Pomieszczenie: "+type+al.room+" - "+al.price+"\n";
            }

            try {
                throw new ProblematicTenantException(errorMessage);
            } catch (ProblematicTenantException e) {
                e.printStackTrace();
            }
        } else if(getUserTenantAlertsCost()>1250){
                System.out.println("Person rented rooms for  "+getUserTenantAlertsCost()+" and cannot buy another one.");
        } else {
            currentRoom.renters.add(currentUser);
            if(currentUser.firstRent == null){
                currentUser.firstRent = od.getDate();
            }
            currentRoom.startLease = od.getDate();
            currentRoom.endLease = od.getDate().plusDays(5);
            currentUser.addRent(currentRoom.endLease , currentBuilding.buildingId, currentRoom.cwid, roomTypes.warehouse, 250);
            System.out.println("You bought the room!");
        }
    }

    private boolean isBadTenant() {
        ArrayList<TenantAlert> tena = currentUser.tenentsAlerts;
        int errors = 0;
        for(TenantAlert ta : tena){
            if(ta.alertType == TenantAlertType.parkingError || ta.alertType == TenantAlertType.rentEndError){
                errors++;
            }
        }
        return errors>3;
    }

    private ArrayList<TenantAlert> getBadTenantsAlerts() {
        ArrayList<TenantAlert> tena = currentUser.tenentsAlerts;
        ArrayList<TenantAlert> badAlert = new ArrayList<>();
        for(TenantAlert ta : tena){
            if(ta.alertType == TenantAlertType.parkingError || ta.alertType == TenantAlertType.rentEndError){
                badAlert.add(ta);
            }
        }

        return badAlert;
    }

    public void addItem(String name,String space) throws Exception {
        int itemSpace = Integer.parseInt(space);
        int currentSpace = 0;
        for(int i = 0; i < currentRoom.items.size();i++){
            currentSpace += currentRoom.items.get(i).itemSpace;
        }
        if(currentRoom.space > (itemSpace+currentSpace)){
            currentRoom.items.add(new Item(name,itemSpace));
            System.out.println("Item "+name+" has been added");
        } else {
            int freeSpace = currentRoom.space - currentSpace;
            throw new Exception("Not enough space for item: "+name+" "+itemSpace+". Free space: "+freeSpace);
        }
    }

    public void removeItem(String itemId) {
        currentRoom.items.remove(Integer.parseInt(itemId));
        System.out.println("Item with ID: "+itemId+" has been deleted");
    }

    public void addRenter(String newRenterID) {
        int clientId = Integer.parseInt(newRenterID);
        if(currentRoom.renters.get(0) == currentUser){
            currentRoom.renters.add(customers.get(clientId));
        } else {
            System.out.println("you are not the room owner");
        }

    }

    public void removeRenter() {
        if(currentRoom.renters.get(0) == currentUser){
            currentRoom.renters.remove(currentUser);
        } else {
            System.out.println("you are not the room owner");
        }
    }

    public void extendLease() {
        currentRoom.extendLease();
    }

    public int getUserTenantAlertsCost(){
        int costs = 0;
        for(int i = 0; i<this.getCurrentUser().rentInfo.size(); i++){
            costs += this.getCurrentUser().rentInfo.get(i).price;
        }
        return costs;
    }

    public void showUsersItem() {
        ArrayList<RoomsInfos> userRooms = new ArrayList<>();
        OurDate od = new OurDate();
        for(int i = 0;i<currentUser.rentInfo.size();i++){
            TenantAlert ta = currentUser.rentInfo.get(i);
            long rentEndInMiliseconds = ta.end
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long nowInMiliseconds = od.getMilisec();
            if(rentEndInMiliseconds > nowInMiliseconds){
                userRooms.add(new RoomsInfos(ta.building, ta.room));
            }
        }
        ArrayList<Item> items = new ArrayList<>();
        for(RoomsInfos rooms: userRooms){
            for(ServiceWarehouse sw: buildings){
                for(ConsumerWarehouse cw: sw.storage){
                    if(cw.renters.get(0) == currentUser){
                        for(Item item: cw.items){
                            items.add(item);
                        }
                    }
                }
            }
        }

        for(Item item:items){
            System.out.println("Item name: "+item.itemName+", Item space: "+item.itemSpace);
        }

    }

    public void showServices(){
        for(int i =0; i < buildings.size(); i++){
            System.out.println("Service: "+i);
            for(IndependentCarServiceSpot icss: buildings.get(i).icss){
                System.out.println("\t\t Independent Car Servise spot: "+icss.icssId+", is ocupated: "+icss.ocupated);
            }
            for(CarServiceSpot css: buildings.get(i).css){
                System.out.println("\t\t Car Servise spot: "+css.cssId+", is ocupated: "+css.ocupated);
            }
        }
    }

    public void showAll() {
        for(ServiceWarehouse sw: buildings){
            System.out.println("Service: "+sw.buildingId);
            for(ConsumerWarehouse cw: sw.storage) {
                System.out.println("\t\t Warehouse: "+cw.cwid+", is ocupated: "+(cw.renters.size()>0));
            }
            for(CarServiceSpot css: sw.css) {
                System.out.println("\t\t Independent Car Servise spot: "+css.cssId+", is ocupated: "+css.ocupated);
            }
            for(IndependentCarServiceSpot icss: sw.icss) {
                System.out.println("\t\t Independent Car Servise spot: "+icss.icssId+", is ocupated: "+icss.ocupated);
            }
            for(ParkingSpace ps: sw.parking) {
                System.out.println("\t\t Parking: "+ps.parkingId+", ocupated: "+(ps.renter != null));
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
        if(yourCSS != null || yourICSS != null){
            getVehicleToService(vehicle, parkingspot, independent, yourICSS, yourCSS);
            System.out.println("Car is now being repaired");
        } else {
            currentBuilding.waitingWork.add(new WaitingService(currentUser, vehicle, independent ? WorkJob.icss : WorkJob.css, currentBuilding.buildingId, parkingspot));
        }
    }

    public IndependentCarServiceSpot getFreeICCS(int building){
        IndependentCarServiceSpot yourICSS = null;
        for (IndependentCarServiceSpot icss : buildings.get(building).icss) {
            if (icss.ocupated == false) {
                System.out.println("Your car is repaired in: " + icss.icssId);
                yourICSS = icss;
                break;
            }
        }

        return yourICSS;
    }

    public CarServiceSpot getFreeCSS(int building){
        CarServiceSpot yourCSS = null;
        for (CarServiceSpot css : buildings.get(building).css) {
            if (css.ocupated == false) {
                System.out.println("Your car is repaired in: " + css.cssId);
                yourCSS = css;
                break;
            }
        }
        return yourCSS;
    }

    public void getVehicleToService(Vehicle vehicle, boolean parkingspot, boolean independent, IndependentCarServiceSpot yourICSS, CarServiceSpot yourCSS){
        OurDate od = new OurDate();
        int repairsDays = random_int(1, 5);
        ServiceAction sa = new ServiceAction(vehicle, od.getDate(), od.getDate().plusDays((long) repairsDays), parkingspot, currentBuilding);
        TenantAlert ta = new TenantAlert(od.getDate().plusDays(14), currentBuilding.buildingId, (repairsDays * 50), TenantAlertType.service);

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
        System.out.println(currentUser.serviceActions.get(0).serviceID);
        currentUser.rentInfo.add(ta);
    }

    private ParkingSpace searchFreeParking() {
        ParkingSpace freeSpot = null;
        for(ParkingSpace ps : currentBuilding.parking){
            if(ps.renter == null) {
                freeSpot = ps;
                break;
            }
        }
        return freeSpot;
    }

    private ParkingSpace searchFreeParking(int buildingId) {
        ParkingSpace freeSpot = null;
        ServiceWarehouse sw = (ServiceWarehouse) buildings.stream().filter(building -> {
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


    private Vehicle carGenerator(String vehicleType) {
        if(vehicleType == "offRoad") {
            return new OffRoadCar();
        } else if(vehicleType == "urban") {
            return new UrbanCar();
        } else if(vehicleType == "motorcycle") {
            return new Motorcycle();
        } else if(vehicleType == "amphibian") {
            return new Amphibian();
        } else {
            return null;
        }
    }

    public void checkForParkingFreeSpace(){
        OurDate od = new OurDate();
        for (ServiceWarehouse sw : buildings){
            for(WaitingParking wp : sw.parkingWait){
                ParkingSpace freeSpot = searchFreeParking(sw.buildingId);
                if(freeSpot != null){
                    freeSpot.ocupated = true;
                    freeSpot.renter = wp.owner;
                    freeSpot.endOfRent = od.getDate().plusDays(14);
                    freeSpot.vehicle = wp.vehicle;
                    sw.parkingWait.remove(wp);
                }
            }
        }
    }

    public void checkForServiceFreeSpace(){
        OurDate od = new OurDate();
        for (ServiceWarehouse sw : buildings){
            for(WaitingService ws : sw.waitingWork){
                if(ws.wj == WorkJob.css){
                    CarServiceSpot css = getFreeCSS(ws.building);
                    if(css != null){
                        getVehicleToService(ws.vehicle, ws.parkingspot, false, null, css);
                    }
                } else {
                    IndependentCarServiceSpot icss = getFreeICCS(ws.building);
                    if(icss != null){
                        getVehicleToService(ws.vehicle, ws.parkingspot, true, icss, null);
                    }
                }
            }
        }
    }

    public void removeLatePayments() {
        OurDate od = new OurDate();
        for(TenantAlert alert : currentUser.tenentsAlerts){
            LocalDateTime aT = alert.end;
            LocalDateTime ld = LocalDateTime.of(aT.getYear(), aT.getMonth(), aT.getDayOfMonth(), aT.getHour(),aT.getMinute(),aT.getSecond());
            ld = ld.plusDays(30);
            if(od.getMilisec()<getMilisec(ld)){
                currentUser.tenentsAlerts.remove(alert);
            }
        }
    }

    public long getMilisec(LocalDateTime ldt){
        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void showAllServices() {
        OurDate od = new OurDate();
        for(ServiceAction sa : currentUser.serviceActions){
            System.out.println("Service job: "+sa.serviceID+" in building: "
                    +sa.buildingId+" was independent: "+sa.independent+" is still active:"+(od.getMilisec()>getMilisec(sa.endDate)));
        }
    }
}


class RoomsInfos{
    int buildingNumber;
    int roomNumber;
    public RoomsInfos(int bid, int rid){
        this.buildingNumber = bid;
        this.roomNumber = rid;
    }
}

