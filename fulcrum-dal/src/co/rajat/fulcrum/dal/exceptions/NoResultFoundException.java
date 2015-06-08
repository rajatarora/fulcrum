package co.rajat.fulcrum.dal.exceptions;

public class NoResultFoundException extends Exception
{
    private static final long serialVersionUID = 881433552144936799L;
    
    public NoResultFoundException (String message)
    {
        super (message);
    }
    
    public NoResultFoundException (Throwable cause)
    {
        super (cause);
    }
    
    public NoResultFoundException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
