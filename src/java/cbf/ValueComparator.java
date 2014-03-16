package cbf;

import myNewTag.Movie;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jie Shan
 * Date: 13-11-5
 * Time: 上午2:20
 * To change this template use File | Settings | File Templates.
 */
public class ValueComparator{

    HashMap<Movie, Number> base;
    public ValueComparator(HashMap<Movie, Number> base) {
        this.base = base;
    }

    public Map sortByComparator() {

        List list = new LinkedList(base.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
