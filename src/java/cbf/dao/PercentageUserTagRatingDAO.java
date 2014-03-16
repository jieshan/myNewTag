package cbf.dao;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.ItemEventCollection;

public class PercentageUserTagRatingDAO extends UserTagRatingDAO implements ItemEventDAO{

	@Inject
	public PercentageUserTagRatingDAO(File userTagRatings) {
		super(userTagRatings);
	}

    @Override
    public Cursor<ItemEventCollection<Event>> streamEventsByItem() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <E extends Event> Cursor<ItemEventCollection<E>> streamEventsByItem(Class<E> eClass) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
	public List<Event> getEventsForItem(long item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E extends Event> List<E> getEventsForItem(long item, Class<E> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LongSet getUsersForItem(long item) {
		// TODO Auto-generated method stub
		return null;
	}

}
