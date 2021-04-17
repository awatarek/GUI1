import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Person {
    public int uid;
    public static int uidList;
    public ArrayList<ServiceAction> serviceActions = new ArrayList<>();
    public ArrayList<TenantAlert> tenentsAlerts = new ArrayList<>();
    public ArrayList<TenantAlert> rentInfo = new ArrayList<>();
    //if 3x Info ProblematicTenantException "
    // Osoba Imie Nazwisko posiadała już najem pomieszczeń: lista_pomieszczeń - wysokość_zadłużenia”
    public String firstName;
    public String lastName;
    public int pesel;
    public Address address;
    public Date dateOfBirth;
    public Date firstRent;
    //if null while reading send NeverRentException;

    public Person(){
        this.uid = ++uidList;
        this.address = new Address();
        this.firstRent = null;
        this.generateRandomName();
    }

    public Person(int num, String firstName, String lastName){
        this.uid = 0;
        this.address = new Address();
        this.firstRent = null;
        this.firstName= firstName;
        this.lastName = lastName;
    }

    public void generateRandomName(){
        String[] secondNames = new String[]{"Antoni","Jan","Franciszek","Szymon","Bartosz", "Aleksander", "Jakub","Mateusz","Michał","Piotr","Sebastian"};
        String[] firstNames = new String[]{"Nowak","Kowalski","Wiśniewski","Wójcik","Kowalczyk","Kamiński","Lewandowski","Zieliński","Szymański","Woźniak","Krupa"};
        this.firstName = firstNames[(int) (Math.random()*11)];
        this.lastName = secondNames[(int) (Math.random()*11)];
    }

    public void addRent(LocalDateTime end, int building, int room, roomTypes type, int price) {
        this.rentInfo.add(new TenantAlert(end, building, room, type, price, TenantAlertType.rented));
    }
}
