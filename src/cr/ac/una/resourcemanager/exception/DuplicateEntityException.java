package cr.ac.una.resourcemanager.exception;

public class DuplicateEntityException extends Exception{

    public DuplicateEntityException(){
        super();
    }

    public DuplicateEntityException(String message){
        super(message);
    }

    public DuplicateEntityException(String message,Throwable cause){
        super(message, cause);
    }
}
