import java.util.*;

public class main {

    public static void main(String args[]){
        DataCollector data = new DataCollector(true);
        OurDate date = new OurDate();
        MyThread myThread = new MyThread();
        myThread.start();
        TenantCheck tenantCheck = new TenantCheck();
        tenantCheck.start();

        System.out.println("Hello Admin!");
        System.out.println("For command manual send: help");
        boolean userExited = false;
        Scanner in = new Scanner(System.in);
        while(!userExited){
            String string = in.nextLine();
            String[] innerArgs = string.split(" ");
            if(innerArgs[0].equals("exit")){
                break;
            }  else {
                commandLisiner(innerArgs, data);
            }
        }
    }

    public static void commandLisiner(String args[], DataCollector dataCollector){
        if(args[0].equals("help")){
            System.out.println("saveData: \t Saves database to files");
            System.out.println("user:");
                System.out.println("\t choose [id] \t Chooses your user in system");
                System.out.println("\t show \t shows information about user");
                System.out.println("latePayment: \t removes all TenantAlert from 30 days");
            System.out.println("building:");
                System.out.println("\t choose [id] \t Chooses building in system");
                System.out.println("\t freeRooms [id] \t shows information about building [id]");
            System.out.println("room:");
                System.out.println("\t choose [id] \t Chooses your room");
                System.out.println("\t buy \t buy new room");
                System.out.println("\t extend \t extend leas");
                System.out.println("\t items \t shows items in room");
                System.out.println("\t choose [id] \t Chooses your user in system");
                System.out.println("\t addItem \t adds item to room");
                System.out.println("\t removeItem \t removes item from room");
                System.out.println("\t addRenter \t adds renter to room ");
                System.out.println("\t removeRenter \t removes renter from room");
            System.out.println("service:");
                System.out.println("\t show \t shows all services and theyrs status");
                System.out.println("\t showYour \t shows current user service");
                System.out.println("\t check [id] \t Check service status");
                System.out.println("\t create [vechicleType] [parking True/False] [intependent True/False] \t create new service job");
            System.out.println("parking:");
                System.out.println("\t show \t shows all cars in parking");
                System.out.println("\t return [id] \t returns car [parking place id] to owner");
            System.out.println("showAll: \t shows all buildings/rooms data");

        } else if(args[0].equals("saveData")){
            dataCollector.saveWarehouse();
            dataCollector.saveServices();
        } else if(args[0].equals("user")) {
            if(args[1].equals("choose")){
                Person user = dataCollector.getUser(Integer.parseInt(args[2]));
                dataCollector.changeUser(user);
            } else if(args[1].equals("show")){
                dataCollector.showUserData();
                dataCollector.showUsersItem();
            } else if(args[1].equals("latePayment")){
                dataCollector.removeLatePayments();
            }
        }else if(args[0].equals("building")){
                if(args[1].equals("freeRooms")) {
                    dataCollector.showFreeRooms(Integer.parseInt(args[2]));
                } else if (args[1].equals("choose")){
                    dataCollector.chooseBuilding(Integer.parseInt(args[2]));
                }
        } else if(args[0].equals("room")){
            if(args[1].equals("choose")) {
                dataCollector.chooseRoom(Integer.parseInt(args[2]));
            } else if(args[1].equals("buy")) {
                dataCollector.buyRoom();
            } else if (args[1].equals("extend")) {
                dataCollector.extendLease();
            }else if(args[1].equals("items")){
                dataCollector.showItems();
            } else if(args[1].equals("addItem")){
                try {
                    dataCollector.addItem(args[2], args[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if(args[1].equals("removeItem")) {
                dataCollector.removeItem(args[2]);
            } else if(args[1].equals("addRenter")) {
                dataCollector.addRenter(args[2]);
            } else if(args[1].equals("removeRenter")) {
                dataCollector.removeRenter();
            }
        } else if(args[0].equals("parking")){
            if(args[1].equals("show")){
                dataCollector.showParking();
            } else if (args[1].equals("return")){
                dataCollector.freeParkingSpace(args[1]);
            }
        } else if(args[0].equals("service")){
            if(args[1].equals("show")){
                dataCollector.showServices();
            } else if (args[1].equals("showYour")){
                dataCollector.showAllServices();
            } else if(args[1].equals("check")) {
                dataCollector.checkService(args[2], args[3]);
            } else if(args[1].equals("create")) {
                Boolean arg1 = args[3].equals("true") ? true : false;
                Boolean arg2 = args[3].equals("true") ? true : false;
                dataCollector.createServiceJob(args[2], arg1, arg2);
            }
        } else if(args[0].equals("showAll")){
            dataCollector.showAll();
        } else {
            System.out.println("some error ocured! Check your command");
        }
    }
    // FIXME:
    //  sprawdzanie czy odpowiednia ilośc argumentów
    //  nie ma wyspecjalizowanych typów samochodów gotowych

}

