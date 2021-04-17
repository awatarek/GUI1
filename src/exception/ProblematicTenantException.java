package exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class ProblematicTenantException extends Throwable{
    public ProblematicTenantException(String error) {
        super(error);
    }
}