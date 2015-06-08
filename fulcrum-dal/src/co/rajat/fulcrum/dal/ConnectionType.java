package co.rajat.fulcrum.dal;

public enum ConnectionType
{
    MONGO("mongo"),
    
    POSTGRES("postgres");
    
    private String Name;
    
    private ConnectionType (final String name)
    {
        this.Name = name;
    }
    
    public String getName()
    {
        return Name;
    }
}
