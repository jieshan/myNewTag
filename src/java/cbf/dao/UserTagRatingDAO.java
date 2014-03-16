package cbf.dao;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.DataAccessException;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.util.DelimitedTextCursor;

import com.google.common.collect.ImmutableSet;

public class UserTagRatingDAO implements UserEventDAO{
	
	private final File userTagRatingFile;
    private transient volatile Long2ObjectMap<ArrayList<TagRating>> userTagRatingCache;
    //private transient volatile Set<String> vocabCache;

    @Inject
    public UserTagRatingDAO(@UserTagRatingFile File userTagRatings) {
        super();
        userTagRatingFile = userTagRatings;
    }

    private void ensureTagCache() {
        if (userTagRatingCache == null) {
            synchronized (this) {
                if (userTagRatingCache == null) {
                    userTagRatingCache = new Long2ObjectOpenHashMap<ArrayList<TagRating>>();
                    ImmutableSet.Builder<String> vocabBuilder = ImmutableSet.builder();
                    Cursor<String[]> lines = null;
                    try {
                        lines = new DelimitedTextCursor(userTagRatingFile, ",");
                    } catch (FileNotFoundException e) {
                        throw new DataAccessException("cannot open file", e);
                    }
                    try {
                        for (String[] line: lines) {
                        	long uid = Long.parseLong(line[0]);
                        	ArrayList<TagRating> tagRatings = userTagRatingCache.get(uid);
                        	if(tagRatings == null){
                        		tagRatings = new ArrayList<TagRating>();
                        	}
                            tagRatings.add(new TagRating(line[2],line[1],Long.parseLong(line[3])));
                        	userTagRatingCache.put(uid, tagRatings);
                        }
                    } finally {
                        lines.close();
                    }
                }
            }
        }
    }

    public ArrayList<TagRating> getUserTagRatings(long uid){
    	if(userTagRatingCache==null) ensureTagCache();
    	return userTagRatingCache.get(uid);
    }
    
	@Override
	public Cursor<UserHistory<Event>> streamEventsByUser() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public <E extends Event> Cursor<UserHistory<E>> streamEventsByUser(Class<E> eClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public UserHistory<Event> getEventsForUser(long user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends Event> UserHistory<E> getEventsForUser(long user,
			Class<E> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
