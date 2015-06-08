package co.rajat.fulcrum.dal;

public class DALConstants
{   
    public static final String URL             = "url";
    public static final String USERNAME        = "username";
    public static final String PASSWORD        = "password";
    
    public static final String DATABASE        = "db";
    
    public static final String KEY_SEPARATOR   = ".";
    public static final String VALUE_SEPARATOR = "_";

    public static String getConnectionStringKey(ConnectionType connType)
    {
        return DATABASE + KEY_SEPARATOR + connType.getName() + KEY_SEPARATOR + URL;
    }
}
