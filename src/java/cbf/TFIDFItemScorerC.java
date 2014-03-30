package cbf;

import cbf.dao.PercentageUserTagRatingDAO;
import cbf.dao.TagRating;
import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.pref.Preference;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class TFIDFItemScorerC extends AbstractItemScorer {
    private final UserEventDAO dao;
    //private final PercentageUserTagRatingDAO tagdao;
    private final TFIDFModel model;

    /**
     * Construct a new item scorer.  LensKit's dependency injector will call this constructor and
     * provide the appropriate parameters.
     *
     * @param dao The user-event DAO, so we can fetch a user's ratings when scoring items for them.
     * @param m   The precomputed model containing the item tag vectors.
     */
    @Inject
    public TFIDFItemScorerC(UserEventDAO dao, TFIDFModel m) {
        this.dao = dao;
        //this.tagdao = tagdao;
        model = m;
    }

    /**
     * Generate item scores personalized for a particular user.  For the TFIDF scorer, this will
     * prepare a user profile and compare it to item tag vectors to produce the score.
     *
     * @param user   The user to score for.
     * @param output The output vector.  The contract of this method is that the caller creates a
     *               vector whose possible keys are all items that should be scored; this method
     *               fills in the scores.
     */
    @Override
    public void score(long user, @Nonnull MutableSparseVector output) {
        // Get the user's profile, which is a vector with their 'like' for each tag
        SparseVector userVector = makeUserVector(user);

        // Loop over each item requested and score it.
        // The *domain* of the output vector is the items that we are to score.
        for (VectorEntry e: output.fast(VectorEntry.State.EITHER)) {
            // Score the item represented by 'e'.
            // Get the item vector for this item
            SparseVector iv = model.getItemVector(e.getKey());
            // TODO Compute the cosine of this item and the user's profile, store it in the output vector
            output.set(e.getKey(), iv.dot(userVector)/(iv.norm()*userVector.norm()));
            // TODO And remove this exception to say you've implemented it
            //throw new UnsupportedOperationException("stub implementation");
        }
    }

    private SparseVector makeUserVector(long user) {
        // Get the user's ratings
        List<Rating> userRatings = dao.getEventsForUser(user, Rating.class);
        if (userRatings == null) {
            // the user doesn't exist
            return SparseVector.empty();
        }

        // Create a new vector over tags to accumulate the user profile
        MutableSparseVector profile = model.newTagVector();
        // Fill it with 0's initially - they don't like anything
        profile.fill(0);

        
        Double avg = 0.0;
        for(Rating r: userRatings){
        	Preference p = r.getPreference();
        	avg+=p.getValue();
        }
        avg = avg/userRatings.size();
        
        // Iterate over the user's ratings to build their profile
        for (Rating r: userRatings) {
            // In LensKit, ratings are expressions of preference
            Preference p = r.getPreference();
            // We'll never have a null preference. But in LensKit, ratings can have null
            // preferences to express the user unrating an item
            /*if (p != null && p.getValue() >= 3.5) {
                // The user likes this item!
                // TODO Get the item's vector and add it to the user's profile
            	SparseVector iv = model.getItemVector(p.getItemId());
            	profile.add(iv);
            }*/
            if (p != null) {
            	SparseVector iv = model.getItemVector(p.getItemId());
            	MutableSparseVector miv = iv.mutableCopy();
            	miv.multiply(p.getValue() - avg);
            	profile.add(miv);
            }   
        }
        
        //File f = new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv");
        File f = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv");
        PercentageUserTagRatingDAO tagdao = new PercentageUserTagRatingDAO(f);
        // My own customization
        ArrayList<TagRating> exppre = tagdao.getUserTagRatings(user);
        if(exppre != null){
        	Double weight = 0.0;
            for(TagRating tagr : exppre){
            	weight += tagr.getRating();
            }
            weight /= exppre.size();
            System.out.format("C weight: "+weight);
            for(TagRating tagr : exppre){
            	long tid = model.tagIds.get(tagr.getTid());
                double tr = tagr.getRating();
                for(double i=1; i<=tr; i++){
                    System.out.println("tid "+profile.get(tid));
                    profile.set(tid, profile.get(tid)*(1+1/i));
                    System.out.println("tid "+profile.get(tid));
                }
            }
        }

        // The profile is accumulated, return it.
        // It is good practice to return a frozen vector.
        return profile.freeze();
    }
}
