package co.rajat.fulcrum.dal.exceptions;

public class DALException extends Exception
{
    private static final long serialVersionUID = 8881783982053209017L;
    
    public DALException (String message)
    {
        super (message);
    }
    
    public DALException (Throwable cause)
    {
        super (cause);
    }
    
    public DALException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
