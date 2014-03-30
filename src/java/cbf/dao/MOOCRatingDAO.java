package cbf.dao;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventCollectionDAO;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.data.dao.SortOrder;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.sql.BasicSQLStatementFactory;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;

import javax.inject.Inject;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Customized rating DAO for MOOC ratings.  This just wraps some standard LensKit DAOs in an
 * easy-to-configure interface.
 *
 * @see org.grouplens.lenskit.data.dao.EventCollectionDAO
 * @see org.grouplens.lenskit.data.dao.SimpleFileRatingDAO
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class MOOCRatingDAO implements EventDAO {
    private EventDAO csvDao;
    private transient volatile EventCollectionDAO cache;

    @Inject
    public MOOCRatingDAO(@RatingFile File file) {
        try{
            BasicSQLStatementFactory sfac = new BasicSQLStatementFactory();
            //sfac.setTableName("offline_rating_s");
            sfac.setTableName("offline_rating");
            sfac.setTimestampColumn(null);
            sfac.setUserColumn("\"user\"");
            sfac.setItemColumn("item");
            sfac.setRatingColumn("rating");
            Class.forName( "org.postgresql.Driver" );
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "ohmyhonor");
            System.out.println( "connected" );
            csvDao = new JDBCRatingDAO(conn,sfac);
            System.out.println("csvDao connected with db");
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        if(csvDao == null){
            csvDao = new SimpleFileRatingDAO(file, ",");
            System.out.println("csvDao connected with file");
        }
    }

    /**
     * Pre-fetch the ratings into memory if we haven't done so already.
     */
    private void ensureRatingCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = (EventCollectionDAO) EventCollectionDAO.create(Cursors.makeList(csvDao.streamEvents()));
                }
            }
        }
    }

    @Override
    public Cursor<Event> streamEvents() {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents();
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type) {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents(type);
    }

    @Override
    public <E extends Event> Cursor<E> streamEvents(Class<E> type, SortOrder order) {
        // delegate to the cached event collection DAO
        ensureRatingCache();
        return cache.streamEvents(type, order);
    }
}
