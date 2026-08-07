package marcar_v.auth_api.services.exceptions;

public class EmailDeliveryException extends RuntimeException {
    public EmailDeliveryException(String message, Throwable cause){
        super(message, cause);
    }
}
