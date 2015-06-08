package co.rajat.fulcrum.dal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;

import co.rajat.fulcrum.dal.exceptions.DALException;
import co.rajat.fulcrum.dal.exceptions.NoResultFoundException;
import co.rajat.fulcrum.logging.FulcrumLogManager;
import co.rajat.fulcrum.logging.FulcrumLogger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

/**
 * The DataBase Abstraction Layer for MongoDB. Implements the MongoDB 3.0 API
 * for interfacing with the database. It is a dumb DBAL. Takes in a query and
 * returns the result. No formatting involved.
 * 
 * @author Rajat Arora
 * @version 1.0
 * @since 17-May-2015
 * 
 */
public class MongoDAL
{

    private final static FulcrumLogger nLogger  = FulcrumLogManager.createLogger(MongoDAL.class);

    private static volatile MongoDAL   instance = null;

    private MongoClient                client;

    private MongoDAL()
    {
        // Nothing new() for MongoDAL
    }

    /**
     * Returns a singleton instance for a MongoDB connection. A MongoDB client
     * is initialized on first call. Thereafter the same client is reused.
     * Thread safe.
     * 
     * @param none
     * 
     * @return the singleton MongoDAL instance
     * 
     * @throws DALException
     *             when a client could not be initialized
     */
    public static MongoDAL getInstance(Properties config) throws DALException
    {
        if (instance == null)
        {
            synchronized (MongoDAL.class)
            {
                nLogger.debug("Initializing Mongo DAL for the first time.");

                if (instance == null)
                {
                    try
                    {
                        instance = new MongoDAL();

                        nLogger.trace("Getting MongoDB database at " + DALConstants.getConnectionStringKey(ConnectionType.MONGO));
                        MongoClientURI connectionString = new MongoClientURI(config.getProperty(DALConstants.getConnectionStringKey(ConnectionType.MONGO)));

                        instance.client = new MongoClient(connectionString);
                    }

                    catch (Exception e)
                    {
                        throw new DALException(e.getMessage());
                    }
                }
            }
        }

        return instance;
    }

    /**
     * The basic Find Query of a MongoDB database. All fields of the matching
     * document(s) are returned.
     * 
     * @param database
     *            the database to run the query in, not null
     * @param collection
     *            the collection name to run the query in, not null
     * @param filter
     *            the actual query document, can be null
     * 
     * @return the query result
     * 
     * @throws NoResultFoundException
     *             if the query returns less than unity results
     * @throws DALException
     *             if any other exceptions are encountered
     */
    public List<Map<String, Object>> find(String database, String collection, Document filter) throws DALException, NoResultFoundException
    {
        nLogger.trace("Entered : MongoDAL.find(" + database + "," + collection + "," + filter + ")");

        return find(database, collection, filter, null, null, null, null);
    }

    /**
     * The basic Find Query of a MongoDB database. Only the fields in the
     * projection are returned for all matching document(s).
     * 
     * @param database
     *            the database to run the query in, not null
     * @param collection
     *            the collection name to run the query in, not null
     * @param filter
     *            the actual query document, can be null
     * @param projection
     *            the fields to return from the document, can be null
     * 
     * @return the query result
     * 
     * @throws NoResultFoundException
     *             if the query returns less than unity results
     * @throws DALException
     *             if any other exceptions are encountered
     */
    public List<Map<String, Object>> find(String database, String collection, Document filter, Document projection) throws DALException, NoResultFoundException
    {
        nLogger.trace("Entered : MongoDAL.find(" + database + "," + collection + "," + filter + "," + projection + ")");

        return find(database, collection, filter, projection, null, null, null);
    }

    /**
     * The find query of a MongoDB database.
     * 
     * @param database
     *            the database to run the query in, not null
     * @param collection
     *            the collection name to run the query in, not null
     * @param filter
     *            the filter expression, can be null
     * @param projection
     *            the fields to return from the document, can be null
     * @param sort
     *            the sort expression, can be null
     * @param limit
     *            maximum number of results required, can be null
     * @param skip
     *            skips first n records, can be null
     * 
     * @return the query result
     * 
     * @throws NoResultFoundException
     *             if the query returns less than unity results
     * @throws DALException
     *             if any other exceptions are encountered
     */
    public List<Map<String, Object>> find(String database, String collection, Document filter, Document projection, Document sort, Integer limit, Integer skip)
            throws DALException, NoResultFoundException
    {
        nLogger.trace("Entered : MongoDAL.find(" + database + "," + collection + "," + filter + "," + projection + ")");

        List<Map<String, Object>> queryResult = new ArrayList<Map<String, Object>>();
        
        // There's no such thing as a null limit or a null skip!
        limit = (limit == null ? 0 : limit);
        skip = (skip == null ? 0 : skip);
        
        try
        {
            MongoDatabase db = client.getDatabase(database);
            MongoCollection<Document> coll = db.getCollection(collection);

            MongoCursor<Document> queryResultCursor = coll.find(filter).projection(projection).sort(sort).limit(limit).skip(skip).iterator();

            if (!queryResultCursor.hasNext())
            {
                throw new NoResultFoundException("The query returned no results");
            }

            while (queryResultCursor.hasNext())
            {
                queryResult.add(queryResultCursor.next());
            }

        }

        catch (NoResultFoundException e)
        {
            throw e;
        }

        catch (Exception e)
        {
            throw new DALException(e.getMessage());
        }

        return queryResult;
    }

    /**
     * Inserts document(s) to a MongoDB database
     * 
     * @param database
     *            the database to run the query in, not null
     * @param collection
     *            the collection name to run the query in, not null
     * @param documents
     *            the documents to insert, not null
     * 
     * @return true if documents inserted successfully
     * 
     * @throws DALException
     *             in case of any error
     */
    public boolean insert(String database, String collection, List<Document> documents) throws DALException
    {
        nLogger.trace("Entered : MongoDAL.insert(" + database + "," + collection);

        boolean result = true;

        try
        {
            MongoDatabase db = client.getDatabase(database);
            MongoCollection<Document> coll = db.getCollection(collection);

            coll.insertMany(documents);

        }

        catch (Exception e)
        {
            result = false;
            throw new DALException(e.getMessage());
        }

        return result;
    }

    /**
     * @param database
     *            the database to run the query in, not null
     * @param collection
     *            the collection name to run the query in, not null
     * @param filter
     *            the filter expression, can be null
     * @param update
     *            the update expression, can be null
     * @param upsert
     *            whether to insert new documents if none match the filter
     * 
     * @return true if some documents were modified, else false
     * 
     * @throws DALException
     *             in case of any error
     */
    public boolean update(String database, String collection, Document filter, Document update, boolean upsert) throws DALException
    {
        nLogger.trace("Entered : MongoDAL.update(" + database + "," + collection + "," + filter + "," + update + "," + upsert);

        boolean result = true;

        try
        {
            MongoDatabase db = client.getDatabase(database);
            MongoCollection<Document> coll = db.getCollection(collection);

            UpdateResult updateResult = coll.updateMany(filter, update, new UpdateOptions().upsert(upsert));

            if (updateResult.getModifiedCount() < 1)
            {
                result = false;
            }
        }

        catch (Exception e)
        {
            result = false;
            throw new DALException(e.getMessage());
        }

        return result;
    }
}
