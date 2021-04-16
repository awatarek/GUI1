import vehicle.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

public class DataCollector {
    public static ArrayList<Person> customers = new ArrayList<Person>();
    public static Person currentUser = new Person(0, "admin", "admin");
    public static ArrayList<Building> buildings = new ArrayList<Building>();
    public static Building currentBuilding;
    public static ConsumerWarehouse currentRoom;

    public DataCollector() {
        this.setupDemo();
        this.demoOwner();
    }

    public void demoOwner() {
        for (int i = 0; i < this.buildings.size(); i++) {
            Building building = this.buildings.get(i);
            for(int a = 0; a< building.service.size(); a++)
            {
                for (int b = 0; b < building.service.get(a).storage.size(); b++) {
                    if (b % 2 == 0) {
                        building.service.get(a).storage.get(b).renters.add(customers.get(0));
                    }
                }
            }

            for (int c = 0; c < building.parking.size(); c++) {
                if (c % 2 == 0) {
                    building.parking.get(c).renter = customers.get(0);
                }
            }
        }
        buildings.get(0).service.get(0).storage.get(0).renters.add(currentUser);
    }

    private void setupDemo() {
        for (int i = 0; i < 20; i++) {
            customers.add(new Person());
        }
        for (int i = 0; i < 3; i++) {
            buildings.add(new Building());
        }
    }

    public void changeUser(Person user) {
        currentUser = user;
    }

    public Person getUser(int uid) {
        System.out.println(uid);
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
            Building building = this.buildings.get(i);
            for(int a = 0; a < building.service.size();a++){
                for (int b = 0; b < building.service.get(a).storage.size(); b++) {
                    ConsumerWarehouse cw = building.service.get(a).storage.stream().filter(storage -> {
                        if(storage.renters.size() == 0){
                            return false;
                        }
                        return storage.renters.get(0).equals(this.getCurrentUser());
                    }).findFirst().orElse(null);
                    if (cw != null) {
                        userStorage.add(cw);
                    }
                }
            }
        }
    }

    public void showFreeRooms(int buildingID) {

        ArrayList<ConsumerWarehouse> freeStorage = new ArrayList<>();
        ArrayList<ServiceWarehouse> freeService = new ArrayList<>();
        ArrayList<ParkingSpace> freeParking = new ArrayList<>();

        Building building = this.buildings.get(buildingID);
        for(int i = 0; i < building.service.size(); i++){
            for (int a = 0; a < building.service.get(i).storage.size(); a++) {
                ConsumerWarehouse cw = building.service.get(i).storage.stream().filter(storage -> {
                    return storage.renters.size() == 0;
                }).findFirst().orElse(null);
                if (cw != null) {
                    freeStorage.add(cw);
                }
            }
        }

        for (int c = 0; c < building.parking.size(); c++) {
            ParkingSpace ps = building.parking.stream().filter(parking -> {
                return parking.renter == null;
            }).findFirst().orElse(null);
            if (ps != null) {
                freeParking.add(ps);
            }
        }

        for(ConsumerWarehouse cw: freeStorage){
            System.out.println("cw"+cw.cwid);
        }

        for(ParkingSpace ps: freeParking){
            System.out.println("ps"+ps.parkingId);
        }
    }

    public void chooseBuilding(int buildingId) {
        currentBuilding = buildings.get(buildingId);
        System.out.println("you choosed building "+currentBuilding);
    }

    public void chooseRoom(int roomId) {
            if(currentBuilding == null){
                System.out.println("Choose Building first!");
                return;
            }
            for(int i = 0; i< currentBuilding.service.size(); i++){
                for(int a = 0; a < currentBuilding.service.get(i).storage.size(); a++){
                    ConsumerWarehouse nowCW = currentBuilding.service.get(i).storage.get(a);
                    if(nowCW.cwid == roomId){
                        currentRoom = nowCW;
                        System.out.println("you choosed room: "+currentRoom+" in building "+currentBuilding);
                        showItems();
                        return;
                    }
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
        if(!(currentRoom.renters.size()==0)){
            System.out.println("Somebody allready bought this room!");
        } else {
            if(getUserTenantAlertsCost()>1250){
                System.out.println("Person renter rooms for  "+getUserTenantAlertsCost()+" and cannot buy another one.");
            } else {
                currentRoom.renters.add(currentUser);
                LocalDateTime now = LocalDateTime.now();
                now.plusDays(5);
                currentUser.addRent(now, currentBuilding.buildingId, currentRoom.cwid, roomTypes.warehouse, 250);
                System.out.println("You bought the room!");
            }
        }
    }

    public void addItem(String name,String space) throws Exception {
        int itemSpace = Integer.parseInt(space);
        int currentSpace = 0;
        for(int i = 0; i < currentRoom.items.size();i++){
            currentSpace += currentRoom.items.get(i).itemSpace;
        }
        if(currentRoom.space < (itemSpace+currentSpace)){
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
            int roomid = rooms.roomNumber-1;
            int buildingid = rooms.buildingNumber-1;
            for(int i = 0; i < buildings.get(buildingid).service.size(); i++){
                ServiceWarehouse sw =  buildings.get(buildingid).service.get(i);
                for(int a = 0; i < sw.storage.size(); i++){
                    for(Item item: sw.storage.get(a).items){
                        items.add(item);
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
            System.out.println("Building: "+i);
            for(int x = 0; x < buildings.get(i).service.size(); x++){
                System.out.println("\tService: "+x);
                for(int y =0; y < buildings.get(i).service.get(x).css.size(); y++){
                    CarServiceSpot css = buildings.get(i).service.get(x).css.get(y);
                    System.out.println("\t\t Car Servise spot: "+y+", is ocupated: "+css.ocupated);
                }

                for(int y =0; y < buildings.get(i).service.get(x).icss.size(); y++){
                    IndependentCarServiceSpot icss = buildings.get(i).service.get(x).icss.get(y);
                    System.out.println("\t\t Independent Car Servise spot: "+y+", is ocupated: "+icss.ocupated);
                }
            }
        }
    }

    public void showAll() {
        for(int i =0; i < buildings.size(); i++){
            System.out.println("Building: "+i);
            for(int x = 0; x < buildings.get(i).service.size(); x++){
                System.out.println("\tService: "+x);
                for(int y =0; y < buildings.get(i).service.get(x).css.size(); y++){
                    CarServiceSpot css = buildings.get(i).service.get(x).css.get(y);
                    System.out.println("\t\t Car Servise spot: "+y+", is ocupated: "+css.ocupated);
                }

                for(int y = 0; y < buildings.get(i).service.get(x).icss.size(); y++){
                    IndependentCarServiceSpot icss = buildings.get(i).service.get(x).icss.get(y);
                    System.out.println("\t\t Independent Car Servise spot: "+y+", is ocupated: "+icss.ocupated);
                }
                for(int y = 0; y < buildings.get(i).service.get(x).storage.size(); y++){
                    ConsumerWarehouse cw = buildings.get(i).service.get(x).storage.get(y);
                    System.out.println("\t\t Warehouse: "+cw.cwid+", is ocupated: "+(cw.renters.size()>0));
                }
            }
            System.out.println("\tParking:");
            for(int x = 0; x < buildings.get(i).parking.size(); x++){
                ParkingSpace ps = buildings.get(i).parking.get(x);
                System.out.println("\t\t Parking: "+ps.parkingId+", ocupated: "+(ps.renter != null));
            }
        }
    }

    public void createServiceJob(String vehicleType, boolean parkingspot, boolean Independent) {
        Vehicle vehicle = carGenerator(vehicleType);
        if(Independent){
            IndependentCarServiceSpot yourICSS;
            outerloop2:
            for(ServiceWarehouse sw: currentBuilding.service){
                for(IndependentCarServiceSpot icss: sw.icss){
                    if(icss.ocupated == false){
                        System.out.println("Your car is repaired in: "+icss.icssId);
                        yourICSS = icss;
                        break outerloop2;
                    }
                }
            }
        } else {
            CarServiceSpot yourCSS;
            outerloop1:
            for(ServiceWarehouse sw: currentBuilding.service){
                for(CarServiceSpot css: sw.css){
                    if(css.ocupated == false){
                        System.out.println("Your car is repaired in: "+css.cssId);
                        yourCSS = css;
                        break outerloop1;
                    }
                }
            }
            OurDate od = new OurDate();
            ServiceAction sa = new ServiceAction(vehicle, od.getDate(), od.getDate().plusDays((long) random_int(1,5)), parkingspot, currentBuilding);
            ParkingSpace parking = null;
            if(parkingspot){
                parking = searchFreeParking();
            }
            if(parking.parkingId < 1000){
                sa.parkingSpotId = parking.parkingId;
            } else if (parking.parkingId > 1000){
                System.out.println("There are no free parking, your vehicle will be added to parking wait list");
                currentBuilding.parkingWait.add(new WaitingParking(vehicle, currentUser));
            }
            currentUser.serviceActions.add(sa);
        }
    }

    private ParkingSpace searchFreeParking() {
        ParkingSpace freeSpot = null;
        for(ParkingSpace ps : currentBuilding.parking){
            if(ps.renter == null) {
                freeSpot = ps;
                break;
            }
        }
        if(freeSpot == null){
            freeSpot = new ParkingSpace(1002);
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
}


class RoomsInfos{
    int buildingNumber;
    int roomNumber;
    public RoomsInfos(int bid, int rid){
        this.buildingNumber = bid;
        this.roomNumber = rid;
    }
}
