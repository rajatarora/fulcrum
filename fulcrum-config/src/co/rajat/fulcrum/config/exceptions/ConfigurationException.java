package co.rajat.fulcrum.config.exceptions;

public class ConfigurationException extends Exception
{
    private static final long serialVersionUID = 4205795646545223462L;

    public ConfigurationException (String message)
    {
        super (message);
    }
    public ConfigurationException (Throwable cause)
    {
        super (cause);
    }
    
    public ConfigurationException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
