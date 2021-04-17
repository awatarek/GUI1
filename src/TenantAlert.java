import java.time.LocalDateTime;

public class TenantAlert {
    public LocalDateTime end;
    public static int id = 0;
    public int uid;
    public int building;
    public int room;
    public String info;
    public int price;
    public roomTypes type;
    public TenantAlertType alertType;

    public TenantAlert(LocalDateTime end, int building, int room, roomTypes type, int price, TenantAlertType alertType) {
        this.uid = id++;
        this.end = end;
        this.building = building;
        this.room = room;
        this.type = type;
        this.price = price;
        this.alertType = alertType;
    }

    public TenantAlert(LocalDateTime end, int building, int room, roomTypes type, int price, TenantAlertType alertType, int id) {
        this.uid = id;
        this.end = end;
        this.building = building;
        this.room = room;
        this.type = type;
        this.price = price;
        this.alertType = alertType;
    }

    public TenantAlert(LocalDateTime end, int building, int price, TenantAlertType alertType) {
        this.uid = id++;
        this.end = end;
        this.building = building;
        this.room = room;
        this.type = type;
        this.price = price;
        this.alertType = alertType;
    }

    public TenantAlert(LocalDateTime end, TenantAlertType alertType) {
        this.uid = id++;
        this.end = end;
        this.alertType = alertType;
    }

}

enum roomTypes{
    parking,
    warehouse,
    service,
    icss,
    css,
}

enum TenantAlertType{
    rented,
    rentEndError,
    service,
    serviceEnd,
    parkingRentet,
    parkingError,
}