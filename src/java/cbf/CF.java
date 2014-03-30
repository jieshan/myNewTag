package cbf;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.baseline.BaselineScorer;
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer;
import org.grouplens.lenskit.baseline.UserMeanBaseline;
import org.grouplens.lenskit.baseline.UserMeanItemScorer;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.LenskitRecommenderEngine;
import org.grouplens.lenskit.cursors.Cursors;
import org.grouplens.lenskit.data.dao.EventCollectionDAO;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO;
import org.grouplens.lenskit.data.sql.BasicSQLStatementFactory;
import org.grouplens.lenskit.data.sql.JDBCRatingDAO;
import org.grouplens.lenskit.knn.item.ItemItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer;
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 14-3-7
 * Time: 下午4:58
 * To change this template use File | Settings | File Templates.
 */
public class CF{

    public static HashMap<Integer, String> result = new HashMap<Integer, String>();

    private String delimiter = ",";
    //private File inputFile = new File("D:\\MAC\\CSHonors\\data\\ratings0.csv");
    private File inputFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc.csv");
    private List<Long> users;

    public CF(String[] args) {
        List<Long> users = new ArrayList<Long>();
        for (int i=0; i < args.length; i++) {
            users.add(Long.parseLong(args[i]));
        }
        result = new HashMap<Integer, String>();
        try {
            run(inputFile, ",", users);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void run(File inputFile, String delimiter, List<Long> users) {
        EventDAO base = null;
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
            base = new JDBCRatingDAO(conn,sfac);
        }
        catch( Exception e ){
            e.printStackTrace();
        }
        if(base == null){
            base = new SimpleFileRatingDAO(inputFile, delimiter);
        }
        // We first need to configure the data access.
        // We will use a simple delimited file; you can use something else like
        // a database (see JDBCRatingDAO).
        //EventDAO base = new SimpleFileRatingDAO(inputFile, delimiter);

        // Reading directly from CSV files is slow, so we'll cache it in memory.
        // You can use SoftFactory here to allow ratings to be expunged and re-read
        // as memory limits demand. If you're using a database, just use it directly.
        EventDAO dao = EventCollectionDAO.create(Cursors.makeList(base.streamEvents()));

        // Second step is to create the LensKit configuration...
        LenskitConfiguration config = new LenskitConfiguration();
        // ... configure the data source
        config.bind(EventDAO.class).to(dao);
        // ... and configure the item scorer.  The bind and set methods
        // are what you use to do that. Here, we want an item-item scorer.
        config.bind(ItemScorer.class)
                .to(ItemItemScorer.class);

        // let's use personalized mean rating as the baseline/fallback predictor.
        // 2-step process:
        // First, use the user mean rating as the baseline scorer
        config.bind(BaselineScorer.class, ItemScorer.class)
                .to(UserMeanItemScorer.class);
        // Second, use the item mean rating as the base for user means
        config.bind(UserMeanBaseline.class, ItemScorer.class)
                .to(ItemMeanRatingItemScorer.class);
        // and normalize ratings by baseline prior to computing similarities
        config.bind(UserVectorNormalizer.class)
                .to(BaselineSubtractingUserVectorNormalizer.class);

        // There are more parameters, roles, and components that can be set. See the
        // JavaDoc for each recommender algorithm for more information.

        // Now that we have a factory, build a recommender from the configuration
        // and data source. This will compute the similarity matrix and return a recommender
        // that uses it.

//        EventDAO base = new SimpleFileRatingDAO(inputFile, delimiter);
//        LenskitConfiguration config = new LenskitConfiguration();
//        config.bind(EventDAO.class).to(base);

        Recommender rec = null;
        try {
//            LenskitRecommenderEngine engine = LenskitRecommenderEngine.newLoader()
//                    .addConfiguration(config)
//                    .load(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\CFmodel.bin"));
//                    //.load(new File("D:\\MAC\\CSHonors\\data\\CFmodel.bin"));
            rec = LenskitRecommender.build(config);
        } catch (Exception e) {
            throw new RuntimeException("recommender build failed", e);
        }

        // we want to recommend items
        ItemRecommender irec = rec.getItemRecommender();
        assert irec != null; // not null because we configured one
        // for users
        for (long user: users) {
            int i=0;
            // get 10 recommendation for the user
            List<ScoredId> recs = irec.recommend(user, 10);
            System.out.println(irec.recommend(user).size());
            System.out.format("Recommendations for %d:\n", user);
            for (ScoredId item: recs) {
                System.out.format(item.getScore()+"\t%d=\n", item.getId());
                result.put(i,item.getId()+"");
                i++;
            }
        }
    }

}
