public class Address {
    public String city;
    public String code;
    public String street;
    public String number;

    public Address() {
        city = "Warszawa";
        code =  random_int(01,99)+"-"+random_int(01,999);
        String[] street = new String[]{"Polna ","Leśna","Słoneczna","Krótka","Szkolna", "Ogrodowa", "Lipowa"};
        this.street = street[(int) (Math.random()*7)];
        number = ""+random_int(5,20);
    }

    public int random_int(int Min, int Max)
    {
        return (int) (Math.random()*(Max-Min))+Min;
    }

}
