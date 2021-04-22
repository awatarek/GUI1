import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

public class Person {
    public int uid;
    public static int uidList;
    public ArrayList<ServiceAction> serviceActions = new ArrayList<>();
    public ArrayList<TenantAlert> tenentsAlerts = new ArrayList<>();
    public ArrayList<TenantAlert> rentInfo = new ArrayList<>();
    public String firstName;
    public String lastName;
    public int pesel;
    public Address address;
    public LocalDateTime dateOfBirth;
    public LocalDateTime firstRent;

    public Person() {
        this.uid = ++uidList;
        this.address = new Address();
        this.firstRent = null;
        this.generateRandomName();
        this.generatePesel();
    }

    private void generatePesel() {
        LocalDateTime localDateTime = LocalDateTime.of(random_int(1920, 2021), random_int(1, 12), random_int(1, 28), random_int(1, 24), random_int(1, 60));
        dateOfBirth = localDateTime;
        String year = localDateTime.getYear() + "";
        String month = localDateTime.getMonthValue() + "";
        String day = localDateTime.getDayOfMonth() + "";
        String pesel = year.charAt(2) + "" + year.charAt(3);
        if (Integer.parseInt(month) > 9) {
            if (Integer.parseInt(year.charAt(0) + "") == 2) {
                pesel += "" + (Integer.parseInt(month) + 10);
            } else {
                pesel += "" + month;
            }
        } else {
            if (Integer.parseInt(year.charAt(0) + "") == 2) {
                pesel += (Integer.parseInt(month) + 10);
            } else {
                pesel += "0" + month;
            }
        }

        if (Integer.parseInt(day) > 9) {
            pesel += "" + day;
        } else {
            pesel += "0" + day;
        }

        pesel += "" + random_int(100, 999);
        int[] maleNumbers = new int[]{1, 3, 5, 7, 9};
        pesel += "" + maleNumbers[(int) (Math.random() * 4)];

        int[] peselWeight = new int[]{1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int validationSum = 0;
        int multiplyResult = 0;
        for (int index = 0; index < 10; index++) {
            multiplyResult += Integer.parseInt(pesel.charAt(index) + "") * peselWeight[index];
        }
        pesel += (multiplyResult % 10) + "";
        this.pesel = Integer.parseInt(pesel);
    }

    public Person(int num, String firstName, String lastName) {
        this.uid = 0;
        this.address = new Address();
        this.firstRent = null;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void generateRandomName() {
        String[] secondNames = new String[]{"Antoni", "Jan", "Franciszek", "Szymon", "Bartosz", "Aleksander", "Jakub", "Mateusz", "Michał", "Piotr", "Sebastian"};
        String[] firstNames = new String[]{"Nowak", "Kowalski", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zieliński", "Szymański", "Woźniak", "Krupa"};
        this.firstName = firstNames[(int) (Math.random() * 11)];
        this.lastName = secondNames[(int) (Math.random() * 11)];
    }

    public void addRent(LocalDateTime end, int building, int room, roomTypes type, int price) {
        this.rentInfo.add(new TenantAlert(end, building, room, type, price, TenantAlertType.rented));
    }

    public int random_int(int Min, int Max) {
        return (int) (Math.random() * (Max - Min)) + Min;
    }

}
