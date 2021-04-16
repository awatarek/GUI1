import java.util.ArrayList;

public class Building {
        public ArrayList<ServiceWarehouse> service = new ArrayList<>();
        public ArrayList<ParkingSpace> parking = new ArrayList<>();
        public ArrayList<WaitingParking> parkingWait = new ArrayList<WaitingParking>();
        static int id = 0;
        int buildingId;
        int area;

        public Building(){
                this.buildingId = id++;
                int height = random_int(3,10);
                this.area = random_int(10,20)*random_int(10,20)*height;
                for(int i = 0; i<4; i++){
                        service.add(new ServiceWarehouse((area/4)));
                }

                int parkingSpaces = random_int(2,5);
                for(int i = 0; i<parkingSpaces;i++){
                        parking.add(new ParkingSpace());
                }
        }

        public int random_int(int Min, int Max)
        {
                return (int) (Math.random()*(Max-Min))+Min;
        }

}

