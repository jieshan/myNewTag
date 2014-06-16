import cbf.TFIDFItemScorerC
import cbf.dao.CSVItemTagDAO
import cbf.dao.MOOCRatingDAO
import cbf.dao.MOOCUserDAO
import cbf.dao.PercentageUserTagRatingDAO
import cbf.dao.RatingFile
import cbf.dao.TagFile
import cbf.dao.TitleFile
import cbf.dao.UserFile
import cbf.dao.UserTagRatingFile
import myNewTag.Movie
import myNewTag.MovieRating
import myNewTag.Tag
import myNewTag.TagCount
import myNewTag.TagRating
import myNewTag.WebUser
import myNewTag.WebUser
import org.grouplens.lenskit.ItemScorer
import org.grouplens.lenskit.baseline.BaselineScorer
import org.grouplens.lenskit.baseline.ItemMeanRatingItemScorer
import org.grouplens.lenskit.baseline.UserMeanBaseline
import org.grouplens.lenskit.baseline.UserMeanItemScorer
import org.grouplens.lenskit.core.LenskitConfiguration
import org.grouplens.lenskit.core.LenskitRecommenderEngine
import org.grouplens.lenskit.core.LenskitRecommenderEngineBuilder
import org.grouplens.lenskit.core.ModelDisposition
import org.grouplens.lenskit.data.dao.EventDAO
import org.grouplens.lenskit.data.dao.ItemDAO
import org.grouplens.lenskit.data.dao.ItemEventDAO
import org.grouplens.lenskit.data.dao.SimpleFileRatingDAO
import org.grouplens.lenskit.data.dao.UserDAO
import org.grouplens.lenskit.knn.item.ItemItemScorer
import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer

class BootStrap {

    def init = { servletContext ->

        long startTime = System.currentTimeMillis();

        /*System.out.println("start bootstraping CF Model")
        String delimiter = ",";
        File inputFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc.csv");
        //File inputFile = new File("D:\\MAC\\CSHonors\\data\\ratings0.csv");
        LenskitConfiguration configCF = new LenskitConfiguration();
        configCF.bind(ItemScorer.class)
                .to(ItemItemScorer.class);
        configCF.bind(BaselineScorer.class, ItemScorer.class)
                .to(UserMeanItemScorer.class);
        configCF.bind(UserMeanBaseline.class, ItemScorer.class)
                .to(ItemMeanRatingItemScorer.class);
        configCF.bind(UserVectorNormalizer.class)
                .to(BaselineSubtractingUserVectorNormalizer.class);
        LenskitRecommenderEngineBuilder builder = LenskitRecommenderEngine.newBuilder();
        builder.addConfiguration(configCF);
        for(int i=1; i<=100; i++){
            inputFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc"+i+".csv");
            EventDAO base = new SimpleFileRatingDAO(inputFile, delimiter);
            LenskitConfiguration dataConfig = new LenskitConfiguration();
            dataConfig.bind(EventDAO.class).to(base);
            builder.addConfiguration(dataConfig,ModelDisposition.EXCLUDED);
        }
        *//*EventDAO base = new SimpleFileRatingDAO(inputFile, delimiter);
        LenskitConfiguration dataConfig = new LenskitConfiguration();
        dataConfig.bind(EventDAO.class).to(base);
        builder.addConfiguration(dataConfig,ModelDisposition.EXCLUDED);*//*
        System.out.println("done setting up CF config")
        LenskitRecommenderEngine engine = builder.build();
        engine.write(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\CFmodel.bin"));
        //engine.write(new File("D:\\MAC\\CSHonors\\data\\CFmodel.bin"));
        Long doneCF = System.currentTimeMillis();
        System.out.println("CF Bootstraping Time (min): "+(doneCF-startTime)/(1000.0*60));
        System.out.println("done bootstraping CF Model")
        System.out.println(" ")


        System.out.println("start bootstraping Tag Model")
        LenskitConfiguration configTag = new LenskitConfiguration();
        configTag.bind(ItemScorer.class)
                .to(TFIDFItemScorerC.class);
        LenskitConfiguration dataConfigTag = new LenskitConfiguration();
        dataConfigTag.bind(ItemDAO.class)
                .to(CSVItemTagDAO.class);
        dataConfigTag.set(TagFile.class)
                //.to(new File("D:\\MAC\\CSHonors\\data\\movie-tags.csv"));
                  .to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\movieTagNewSrc.csv"));
        dataConfigTag.set(TitleFile.class)
                //.to(new File("D:\\MAC\\CSHonors\\data\\movie-titles.csv"));
                  .to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\moviesNewSrc.csv"));
        dataConfigTag.bind(UserDAO.class)
                .to(MOOCUserDAO.class);
        dataConfigTag.set(UserFile.class)
                //.to(new File("D:\\MAC\\CSHonors\\data\\users.csv"));
                  .to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\usersSrc.csv"));
        dataConfigTag.bind(ItemEventDAO.class)
                .to(PercentageUserTagRatingDAO.class);
        dataConfigTag.set(UserTagRatingFile.class)
                //.to(new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv"));
                  .to(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv"));
        *//*dataConfigTag.bind(EventDAO.class)
                .to(MOOCRatingDAO.class);
        dataConfigTag.set(RatingFile.class)
        //.to(new File("D:\\MAC\\CSHonors\\data\\ratings0.csv"));
                .to( new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc.csv"));*//*
        builder = LenskitRecommenderEngine.newBuilder();
        builder.addConfiguration(configTag);
        builder.addConfiguration(dataConfigTag,ModelDisposition.EXCLUDED);
        for(int i=1; i<=100; i++){
            dataConfigTag = new LenskitConfiguration();
            dataConfigTag.bind(EventDAO.class)
                    .to(MOOCRatingDAO.class);
            dataConfigTag.set(RatingFile.class)
                    //.to(new File("D:\\MAC\\CSHonors\\data\\ratings0.csv"));
                    .to( new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewSrc"+i+".csv"));
            builder.addConfiguration(dataConfigTag,ModelDisposition.EXCLUDED);
        }
        System.out.println("done setting up Tag config")
        engine = builder.build();
        engine.write(new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\Tagmodel.bin"));
        //engine.write(new File("D:\\MAC\\CSHonors\\data\\Tagmodel.bin"));
        Long doneTag = System.currentTimeMillis();
        System.out.println("Tag Bootstraping Time (min): "+(doneTag - doneCF)/(1000.0*60));
        System.out.println("done bootstraping Tag Model")
        System.out.println(" ")*/


        System.out.println("start bootstraping users.csv");
        def csv = new File("D:\\MAC\\CSHonors\\data\\users.csv")
        //def csv = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\usersSrc.csv")
        long tenpercent = 765;
        def anotherUser
        Double lineCount = 0.0;
        csv.splitEachLine(',') { row ->
            if(WebUser.findByUserId(row[0])==null){
                anotherUser = new WebUser(
                        userId: row[0],
                        username: row[1],
                        turker: Integer.parseInt(row[2]),
                        finishA:Integer.parseInt(row[3]),
                        finishB: Integer.parseInt(row[4]),
                        rewardCode: row[5]
                )
                anotherUser.save(failOnError: true,flush: true)
            }else{
                def existingUser = WebUser.findByUserId(row[0]);
                existingUser.username = row[1];
                existingUser.turker = Integer.parseInt(row[2]);
                existingUser.finishA = Integer.parseInt(row[3]);
                existingUser.finishB = Integer.parseInt(row[4]);
                existingUser.rewardCode = row[5];
                existingUser.save(failOnError: true,flush: true)
            }
            lineCount+=1;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneUser = System.currentTimeMillis();
        System.out.println("users.csv Bootstraping Time (min): "+(doneUser-startTime)/(1000.0*60));
        System.out.println("users.csv per line (sec): "+(doneUser-startTime)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping users.csv");
        System.out.println(" ");


        System.out.println("start bootstraping movie-titles.csv");
        //def csv2 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\moviesNewSrc.csv")
        def csv2 = new File("D:\\MAC\\CSHonors\\data\\movie-titles.csv")
        lineCount = 0.0;
        tenpercent = 1015;
        def anotherMovie
        csv2.splitEachLine(",") { row ->
        //csv2.splitEachLine(",\"|\",") { row ->
            anotherMovie = new Movie(
                    movieId: row[0],
                    movieName: row[1],
                    year: row[2],
                    imgurl: row[3],
            )
            anotherMovie.save(failOnError: true,flush: true)
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneMovie = System.currentTimeMillis();
        System.out.println("movie-titles.csv Bootstraping Time (min): "+(doneMovie-doneUser)/(1000.0*60));
        System.out.println("movie-titles.csv per line (sec): "+(doneMovie-doneUser)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping movie-titles.csv");
        System.out.println(" ");


        System.out.println("start bootstraping movie-tags.csv")
        def csv3 = new File("D:\\MAC\\CSHonors\\data\\tag_s.csv")
        //def csv3 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\tag_l.csv")
        lineCount = 0.0;
        tenpercent = 1504;
        long tagTime = 0;
        long tagcountTime = 0;
        def anotherTag
        csv3.splitEachLine(',') { row ->
                anotherTag = new Tag(
                        tagName: row[0]
                )
                anotherTag.save(failOnError: true,flush: true)
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneMovieTag = System.currentTimeMillis();
        System.out.println("movie-tags.csv Bootstraping Time (min): "+(doneMovieTag-doneMovie)/(1000.0*60));
        System.out.println("movie-tags.csv per line (sec): "+(doneMovieTag-doneMovie)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping movie-tags.csv")
        System.out.println(" ")


        /*System.out.println("start bootstraping movie-tags.csv")
        def csv3 = new File("D:\\MAC\\CSHonors\\data\\movie-tags.csv")
        //def csv3 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\movieTagNewSrc.csv")
        lineCount = 0.0;
        tenpercent = 8851;
        long tagTime = 0;
        long tagcountTime = 0;
        def anotherTag
        csv3.splitEachLine(',') { row ->
            long startRow = System.currentTimeMillis();
            if (Tag.findByTagName(row[1])==null){
                anotherTag = new Tag(
                        tagName: row[1]
                )
                anotherTag.save(failOnError: true,flush: true)
            }else{
                anotherTag = Tag.findByTagName(row[1])
            }
            long tagDone = System.currentTimeMillis();
            tagTime += (tagDone-startRow);

            String movieId = row[0]
            Movie m = Movie.findByMovieId(movieId)
            def anotherTagCount
            if(TagCount.findByTcMovieAndTcTag(m,anotherTag) == null){
                anotherTagCount = new TagCount(
                        tagCount: 1,
                        tcMovie: m,
                        tcTag: anotherTag
                )
                anotherTagCount.save(failOnError: true,flush: true)
            }else{
                anotherTagCount = TagCount.findByTcMovieAndTcTag(m,anotherTag)
                anotherTagCount.tagCount++
                anotherTagCount.save(failOnError: true,flush: true)
            }
            long tagcountDone = System.currentTimeMillis();
            tagcountTime += (tagcountDone-tagDone);
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneMovieTag = System.currentTimeMillis();
        System.out.println("movie-tags.csv Bootstraping Time (min): "+(doneMovieTag-doneMovie)/(1000.0*60));
        System.out.println("movie-tags.csv per line (sec): "+(doneMovieTag-doneMovie)/(1000.0*lineCount));
        //System.out.println("tag cost per line (sec): "+(tagTime)/(1000.0*lineCount));
        //System.out.println("tagcount cost per line (sec): "+(tagcountTime)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping movie-tags.csv")
        System.out.println(" ")*/


        System.out.println("start bootstraping tagcount_s.csv")
        def csv7 = new File("D:\\MAC\\CSHonors\\data\\tagcount_s.csv")
        //def csv7 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\tagcount_l.csv")
        lineCount = 0.0;
        tenpercent = 6615;
        def anotherTagCount
        csv7.splitEachLine(',') { row ->
                anotherTagCount = new TagCount(
                        tagCount: Integer.parseInt(row[2]),
                        tcMovieId: row[0],
                        tcTagname: row[1]
                )
                anotherTagCount.save(failOnError: true,flush: true)
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneTagcounts = System.currentTimeMillis();
        System.out.println("tsgcount.csv Bootstraping Time (min): "+(doneTagcounts-doneMovieTag)/(1000.0*60));
        System.out.println("tsgcount.csv per line (sec): "+(doneTagcounts-doneMovieTag)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping tsgcount.csv")
        System.out.println(" ")


        System.out.println("start bootstraping newratings0.csv")
        def csv4 = new File("D:\\MAC\\CSHonors\\data\\newratings0.csv")
        //def csv4 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewUsers.csv")
        lineCount = 0.0;
        tenpercent = 924461;
        def anotherMovieRating
        csv4.splitEachLine(',') { row ->
            anotherMovieRating = new MovieRating(
                    movieRating: Double.parseDouble(row[2]),
                    movie: Movie.findByMovieId(row[1]),
                    user: WebUser.findByUserId(row[0])
            )
            anotherMovieRating.save(failOnError: true,flush: true)

            WebUser user = WebUser.findByUserId(row[0])
            user.addToMovieRatings(anotherMovieRating)
            user.save(failOnError: true,flush: true)
            Movie movie = Movie.findByMovieId(row[1])
            movie.addToUserMovieRatings(anotherMovieRating)
            movie.save(failOnError: true,flush: true)
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneRatings = System.currentTimeMillis();
        System.out.println("ratings.csv Bootstraping Time (min): "+(doneRatings-doneTagcounts)/(1000.0*60));
        System.out.println("ratings.csv per line (sec): "+(doneRatings-doneTagcounts)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping newratings0.csv")
        System.out.println(" ")


        System.out.println("start bootstraping user-tagratings.csv")
        def csv5 = new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv")
        //def csv5 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv")
        lineCount = 0.0;
        def anotherUserTagRating
        csv5.splitEachLine(',') { row ->
            anotherUserTagRating = new TagRating(
                    tagRating: Double.parseDouble(row[3]),
                    trMovie: Movie.findByMovieId(row[1]),
                    trUser: WebUser.findByUserId(row[0]),
                    trTag: Tag.findByTagName(row[2])
            )
            anotherUserTagRating.save(failOnError: true,flush: true)

            Movie movie =  Movie.findByMovieId(row[1])
            movie.addToUserTagRatings(anotherUserTagRating)
            movie.save(failOnError: true,flush: true)
            WebUser user = WebUser.findByUserId(row[0])
            user.addToUserTagRatings(anotherUserTagRating)
            user.save(failOnError: true,flush: true)
            Tag tag = Tag.findByTagName(row[2])
            tag.addToTagRatings(anotherUserTagRating)
            tag.save(failOnError: true,flush: true)
            lineCount++;
        }
        Long doneTagRatings = System.currentTimeMillis();
        System.out.println("tagratings.csv Bootstraping Time (min): "+(doneTagRatings-doneRatings)/(1000.0*60));
        System.out.println("tagratings.csv per line (sec): "+(doneTagRatings-doneRatings)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping user-tagratings.csv")
        System.out.println(" ")


        System.out.println("start bootstraping avgmov.csv")
        def csv6 = new File("D:\\MAC\\CSHonors\\data\\avgmov.csv")
        //def csv6 = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\avgmovSrc.csv")
        lineCount = 0.0;
        tenpercent = 1015;
        csv6.splitEachLine(',') { row ->
            Movie movie = Movie.findByMovieId(row[0])
            movie.avgRating = Double.parseDouble(row[1]);
            movie.previousCount = (Integer) Double.parseDouble(row[2]);
            movie.save(failOnError: true,flush: true)
            lineCount++;
            if(lineCount%tenpercent == 0){
                System.out.println(lineCount/tenpercent);
            }
        }
        Long doneAvgRatings = System.currentTimeMillis();
        System.out.println("avgmov.csv Bootstraping Time (min): "+(doneAvgRatings-doneTagRatings)/(1000.0*60));
        System.out.println("avgmov.csv per line (sec): "+(doneAvgRatings-doneTagRatings)/(1000.0*lineCount));
        System.out.println("line count = "+lineCount);
        System.out.println("done bootstraping avgmov.csv")
        System.out.println(" ")


        long endTime = System.currentTimeMillis();
        System.out.println("Total Bootstraping Time (min): "+(endTime-startTime)/(1000.0*60));
    }
    def destroy = {
    }
}
