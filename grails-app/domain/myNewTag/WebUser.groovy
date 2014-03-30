package myNewTag

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-11-3
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
class WebUser {

    String userId
    String username
    Integer turker
    Integer finishA
    Integer finishB
    String rewardCode
    static hasMany = [movieRatings: MovieRating,userTagRatings: TagRating]

    static constraints = {
        userId(nullable: false)
        turker(nullable: false)
        finishA(nullable: false)
        finishB(nullable: false)
        rewardCode(nullable: false)
    }

}
