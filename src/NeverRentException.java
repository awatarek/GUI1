public class NeverRentException extends Throwable{
    public NeverRentException() {
        super("This user never rented storage");
    }
}
