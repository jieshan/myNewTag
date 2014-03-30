package myNewTag

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-11-3
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
class Movie {

    String movieId
    String movieName
    String year
    String imgurl
    Double avgRating
    Integer previousCount
    static hasMany = [userTagRatings: TagRating, userMovieRatings: MovieRating, tagCounts:TagCount]

    static constraints = {
        movieId(nullable: false)
        movieName(nullable: false)
        year(nullable: false)
        imgurl(nullable: false)
        avgRating(nullable: true)
        previousCount(nullable: true)
    }

    static mapping = {
        movieId column: 'movie_id', index: 'Movie_Movie_Id_Idx'
    }

}
