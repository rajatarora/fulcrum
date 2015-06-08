package co.rajat.fulcrum.logging;

public class FulcrumLogManager
{
    public static FulcrumLogger createLogger(Class<?> cls)
    {
        return new FulcrumLogger(cls);
    }
}
