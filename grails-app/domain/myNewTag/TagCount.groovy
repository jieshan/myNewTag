package myNewTag

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-11-3
 * Time: 下午10:52
 * To change this template use File | Settings | File Templates.
 */
class TagCount {

    Integer tagCount
    //Movie tcMovie
    //Tag tcTag
    String tcMovieId
    String tcTagname

    static constraints = {
        tagCount(nullable: false)
        tcMovieId(nullable: false)
        tcTagname(nullable: false)
    }

    static mapping = {
        tcMovieId column: 'tc_movie_id', index: 'tag_count_tc_movie_id'
        tcTagname column: 'tc_tag_id', index: 'tag_count_tc_tag_name'
    }
}
