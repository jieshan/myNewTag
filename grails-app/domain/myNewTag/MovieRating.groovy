package myNewTag

/**
 * Created with IntelliJ IDEA.
 * User: Jie Shan
 * Date: 13-11-3
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
class MovieRating {

    Double movieRating
    User user
    Movie movie

    static hasOne = [movie:Movie]

    static constraints = {
        movieRating(nullable: false)
        movie(nullable: false)
        user(nullable: false)
    }

}
