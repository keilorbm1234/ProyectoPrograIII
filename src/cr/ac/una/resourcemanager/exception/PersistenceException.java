package cr.ac.una.resourcemanager.exception;

public class PersistenceException extends RuntimeException{

    public PersistenceException(){
        super();
    }

    public PersistenceException(String message){
        super(message);
    }

    public PersistenceException(String message, Throwable cause){
        super(message,cause);
    }
}
