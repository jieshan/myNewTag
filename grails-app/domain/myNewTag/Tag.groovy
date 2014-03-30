package myNewTag

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-11-3
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
class Tag {

    String tagName
    static hasMany = [tagCounts: TagCount, tagRatings: TagRating]

    static constraints = {
        tagName(nullable: false)
    }

}
