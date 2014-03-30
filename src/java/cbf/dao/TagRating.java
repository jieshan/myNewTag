package cbf.dao;

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-10-26
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class TagRating {

    public String tid;
    public String mid;
    public double rating;

    public TagRating(String tid, String mid, double rating){
        this.tid = tid;
        this.mid = mid;
        this.rating = rating;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
