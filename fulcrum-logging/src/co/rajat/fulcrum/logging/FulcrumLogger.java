package co.rajat.fulcrum.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FulcrumLogger
{   
    private Logger nLogger = null;
    
    FulcrumLogger(Class<?> cls)
    {
        nLogger = LogManager.getLogger(cls);
    }
    
    public void trace(String message)
    {
        if (nLogger.isTraceEnabled())
        {
            nLogger.trace(message);
        }
    }
    
    public void debug(String message)
    {
        if (nLogger.isDebugEnabled())
        {
            nLogger.debug(message);
        }
    }
    
    public void info(String message)
    {
        if (nLogger.isInfoEnabled())
        {
            nLogger.info(message);
        }
    }
    
    public void warn(String message)
    {
        if (nLogger.isWarnEnabled())
        {
            nLogger.warn(message);
        }
    }
    
    public void error(String message)
    {
        if (nLogger.isErrorEnabled())
        {
            nLogger.error(message);
        }
    }
    
    public void fatal(String message)
    {
        if (nLogger.isFatalEnabled())
        {
            nLogger.fatal(message);
        }
    }
}
