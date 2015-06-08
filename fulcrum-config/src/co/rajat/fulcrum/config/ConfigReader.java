package co.rajat.fulcrum.config;

import java.io.InputStream;
import java.util.Properties;

import co.rajat.fulcrum.config.exceptions.ConfigurationException;
import co.rajat.fulcrum.logging.FulcrumLogManager;
import co.rajat.fulcrum.logging.FulcrumLogger;

/**
 * Class to read configuration from various sources (only .properties files
 * supported yet!)
 * 
 * TODO Use Apache Commons Configuration TODO Add support for XML sources TODO
 * Make a single interface for every type of configuration
 * 
 * @author Rajat Arora
 * @version 1.0
 * @since 30-May-2015
 */
public class ConfigReader
{
    private final static FulcrumLogger nLogger = FulcrumLogManager.createLogger(ConfigReader.class);

    /**
     * Reads configuration properties from a given file path.
     * 
     * @param propFilePath
     *            path of the properties file, not null
     *            
     * @return the Properties object, can be null
     * 
     * @throws ConfigurationException
     *             when properties can't be read
     */
    public Properties read(String propFilePath) throws ConfigurationException
    {
        nLogger.trace("Entered : NummusConfigReader.read()");

        Properties prop = null;

        try
        {
            prop = new Properties();

            InputStream inputStream = getClass().getResourceAsStream(propFilePath);

            if (inputStream != null)
            {
                prop.load(inputStream);
            }
            else
            {
                nLogger.error("Configuration file not found - " + propFilePath);
                throw new ConfigurationException("Configuration file not found - " + propFilePath);
            }
        }

        catch (Exception e)
        {
            prop = null;
            nLogger.error("Exception while reading config properties. " + e);
            throw new ConfigurationException("Exception while reading config properties from " + propFilePath + ".");
        }

        return prop;
    }
}
