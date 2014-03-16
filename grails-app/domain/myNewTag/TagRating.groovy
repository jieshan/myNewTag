package myNewTag

/**
 * Created with IntelliJ IDEA.
 * User: Jie Shan
 * Date: 13-11-4
 * Time: 上午12:56
 * To change this template use File | Settings | File Templates.
 */
class TagRating {

    Double tagRating
    User trUser
    Movie trMovie
    Tag trTag

    static constraints = {
        tagRating(nullable: true)
        trUser(nullable: true)
        trMovie(nullable: true)
        trTag(nullable: true)
    }
}
