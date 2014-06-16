package cbf;

import cbf.dao.*;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.core.LenskitRecommenderEngine;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Simple hello-world program.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class CBFMainC {
    private static final Logger logger = LoggerFactory.getLogger(CBFMainC.class);

    public static HashMap<String,TreeMap<Number, Number>> results = new HashMap<String,TreeMap<Number, Number>>();

    public CBFMainC(String[] args){
        try {
            results = new HashMap<String,TreeMap<Number, Number>>();
            mainRun(args);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void mainRun(String[] args) throws Exception {
        LenskitConfiguration config = configureRecommender();

        logger.info("building recommender");
        /*LenskitRecommenderEngine engine = LenskitRecommenderEngine.newLoader()
                .addConfiguration(config)
                .load(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\Tagmodel.bin"));
                //.load(new File("D:\\MAC\\CSHonors\\data\\Tagmodel.bin"));*/
        Recommender rec = LenskitRecommender.build(config);

        if (args.length == 0) {
            logger.error("No users specified; provide user IDs as command line arguments");
        }

        // we automatically get a useful recommender since we have a scorer
        ItemRecommender irec = rec.getItemRecommender();
        assert irec != null;
        try {
            // Generate 5 recommendations for each user
            for (String user: args) {
                TreeMap<Number,Number> tree = new TreeMap<Number, Number>();
                long uid;
                try {
                    uid = Long.parseLong(user);
                } catch (NumberFormatException e) {
                    logger.error("cannot parse user {}", user);
                    continue;
                }
                logger.info("searching for recommendations for user {}", user);
                List<ScoredId> recs = irec.recommend(uid, 10);
                if (recs.isEmpty()) {
                    logger.warn("no recommendations for user {}, do they exist?", uid);
                }
                System.out.format("recommendations for user %d:\n", uid);
                for (ScoredId id: recs) {
                    System.out.format("=======  %d: %.4f\n", id.getId(), id.getScore());
                    tree.put(id.getId(),id.getScore());
                }
                results.put(user,tree);
            }
        } catch (UnsupportedOperationException e) {
            if (e.getMessage().equals("stub implementation")) {
                System.out.println("Congratulations, the stub builds and runs!");
            }
        }
    }

    /**
     * Create the LensKit recommender configuration.
     * @return The LensKit recommender configuration.
     */
    // LensKit configuration API generates some unchecked warnings, turn them off
    @SuppressWarnings("unchecked")
    private static LenskitConfiguration configureRecommender() {
        LenskitConfiguration config = new LenskitConfiguration();
        // configure the rating data source
        config.bind(EventDAO.class)
              .to(MOOCRatingDAO.class);
        config.set(RatingFile.class)
              .to(new File("D:\\MAC\\CSHonors\\data\\ratings0.csv"));
              //.to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc.csv"));

        // use custom item and user DAOs
        // specify item DAO implementation with tags
        config.bind(ItemDAO.class)
              .to(CSVItemTagDAO.class);
        // specify tag file
        config.set(TagFile.class)
              .to(new File("D:\\MAC\\CSHonors\\data\\movie-tags.csv"));
              //.to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\movieTagNewSrc.csv"));
        // and title file
        config.set(TitleFile.class)
              .to(new File("D:\\MAC\\CSHonors\\data\\movie-titles.csv"));
              //.to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\moviesNewSrc.csv"));

        // our user DAO can look up by user name
        config.bind(UserDAO.class)
              .to(MOOCUserDAO.class);
        config.set(UserFile.class)
              .to(new File("D:\\MAC\\CSHonors\\data\\users.csv"));
              //.to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\usersSrc.csv"));
        
        config.bind(ItemEventDAO.class)
        	  .to(PercentageUserTagRatingDAO.class);
        config.set(UserTagRatingFile.class)
              .to(new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv"));
              //.to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv"));

        // use the TF-IDF scorer you will implement to score items
        config.bind(ItemScorer.class)
              .to(TFIDFItemScorerC.class);

        return config;
    }
}