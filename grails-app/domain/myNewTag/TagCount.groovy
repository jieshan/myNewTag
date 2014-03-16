package myNewTag

/**
 * Created with IntelliJ IDEA.
 * User: Jie Shan
 * Date: 13-11-3
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
class TagCount {

    Integer tagCount
    Movie tcMovie
    Tag tcTag

    static constraints = {
        tagCount(nullable: false)
        tcMovie(nullable: false)
        tcTag(nullable: false)
    }

}
