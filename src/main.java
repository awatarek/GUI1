import java.util.*;

public class main {

    public static void main(String args[]){
        DataCollector data = new DataCollector();
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
                System.out.println("\t check [id] \t Check service status");
                System.out.println("\t create [vechicleType] [parking True/False] [intependent True/False] \t create new service job");
            System.out.println("parking:");
                System.out.println("\t show \t shows all cars in parking");
                System.out.println("\t return [id] \t returns car [parking place id] to owner");
            System.out.println("showAll: \t shows all buildings/rooms data");

        } else if(args[0].equals("saveData")){
            /* Stan magazynu powinien być zapisany do pliku warehouses.txt, zaś pomieszczenia wewnątrz piku powinny być posortowane rosnąco według rozmiaru pomieszczenia.
            • Zawartość każdego z pomieszczeń powinno być posortowane malejąco według rozmiaru
            przedmiotu, a jeśli jest taki sam to według nazwy.
            • Informacje dot. serwisu samochodowego powinny być zapisane do pliku services.txt z
            wyszczególnieniem aktualnych pojazdów w serwisie oraz historii napraw dla każdego z
            serwisów (wraz z kosztem sumarycznym naprawy każdego z samochodów - wyliczenie na
            podstawie liczby dni i kosztu jednego dnia serwisu dla danego sposobu - samodzielna/przez
            mechanika).
            • Nie wolno korzystać z żadnego rodzaju serializacji
            */
        } else if(args[0].equals("user")) {
            if(args[1].equals("choose")){
                Person user = dataCollector.getUser(Integer.parseInt(args[2]));
                dataCollector.changeUser(user);
            } else if(args[1].equals("show")){
                dataCollector.showUserData();
                dataCollector.showUsersItem();
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
                /*przedmiotów pamiętając, aby nie przepełnić miejsca park*/
                /* wyjęcia przedmiotów*/
        } else if(args[0].equals("service")){
            if(args[1].equals("show")){
                System.out.println("\t show \t shows all active services");
                dataCollector.showServices();
            } else if(args[1].equals("check")) {
                //dataCollector.removeItem(args[2]);
                System.out.println("\t check [id] \t Check service status - but first building choose");
            } else if(args[1].equals("creat")) {
                dataCollector.createServiceJob(args[2], Boolean.getBoolean(args[3]), Boolean.getBoolean(args[4]));
            }
        } else if(args[0].equals("showAll")){
            dataCollector.showAll();
        } else {
            System.out.println("some error ocured! Check your command");
        }
    }
    /*
    /* sprawdzanie czy odpowiednia ilośc argumentów
    /* nie ma wyspecjalizowanych typów samochodów gotowych

    /*
    W ramach najmu powierzchni serwisu jest możliwość również dodatkowego najmu miejsca
parkingowego na czas po naprawie (w przypadku braku możliwości terminowego odbioru samochodu po naprawie, jednak czas ten nie może być dłuższy niż 14 dni.

     parkowanie tez dodaje do limitu kosztu
     jeśli opłacone w ciągu 30dni znika Tenant Alert

     Parkingi jeśli pojazd dłużej niż rent danej osoby to wtedy TenantAlert o odcholowaniu -- usunięciu pojazdu;
     NeverRentException gdy show user
     Jeśli najem będzie chciała rozpocząć osoba z więcej niż trzema zadłużeniami (co najmniej
3 obiekty typu Info
ProblematicTenantException z komunikatem – „Osoba X posiadała już najem pomieszczeń: lista_pomieszczeń - wysokość_zadłużenia”, gdzie X to imię i nazwisko danej osoby,
lista_pomieszczeń definiuje wynajmowane pomieszczenia, zaś wysokość_zadłużenia definiuje sumaryczną wysokość zadłużenia za to pomieszczenie.

W ramach serwisu osoba oczywiście może również zgłaszać potrzebę serwisową dot. swojego
pojazdu. Dla uproszczenia zakładamy, ze zgłoszenie serwisowe jest jednoznaczne ze wstawieniem
pojazdu do wyznaczonego miejsca serwisowego (w zależności od sposobu naprawy pojazdu - naprawa przez mechanika lub samodzielna, których ceny są dowolne). W przypadku, gdy wszystkie
miejsca serwisowe danego typu są zajęte, klient zostaje wpisany na listę oczekujących, samochód odstawiony na miejsce parkingowe (za darmo) i po uwolnieniu się miejsca serwisowego
automatycznie samochód zostanie przyjęty do serwisu.



        //jesli samochod na miejscu parkingowym samochod odcholowany i dajemy TenantAlert
        //jesli osoba ma trzy TenantAlert
     */

}
