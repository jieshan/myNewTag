package myNewTag

import cbf.CBFMainB
import cbf.CBFMainC
import cbf.CF
import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * WebUser: Jie Shan
 * Date: 13-10-15
 * Time: 下午7:23
 * To change this template use File | Settings | File Templates.
 */
class MyNewTagController {

    def dataSource

    def index ={
        redirect(action: 'login')
    }

    def login = {
        /*String[] users = ["602","99","1","9999"];
        CBFMain rec = new CBFMain(users);
        HashMap<String, TreeMap<Number,Number>> results = rec.results;
        String s = "";
        for(String e : results.keySet()){
            s += e+" \n";
            for(Number entry : results.get(e).keySet()){
                s+=results.get(e).get(entry)+":"+entry+"\n";
            }
        }*/
        render(view: 'userLogIn');
    }

    def doLogin={
        if (WebUser.findWhere(userId:params['userId'],username: params['username'])!=null){
            def user=WebUser.findWhere(userId: params['userId'],username: params['username'])
            def movies = Movie.findAll()
            movies = movies.findAll{it.movieName.startsWith("A")}
            render(view:"userHome", model:[u:user, movies:movies])
        }
        else{
            System.out.println(params['username'] + params['username'])
            def again = true
            redirect(action: "login", params: [again:again])
        }
    }

    def home ={
        def user=WebUser.findByUsername(params['u'] as String)
        def movies = Movie.findAll()
        movies = movies.findAll{it.movieName.startsWith("A")}
        def mode = params['mode']
        def lbm = params['movie'].toString().substring(0,1)
        if(!lbm.matches("[a-zA-Z]")){
            lbm = "0";
        }
        System.out.println(mode)
        render(view:"userHome", model:[u:user, movies:movies, mode:mode, letterBookmark:lbm])
    }

    def moviePage={
            System.out.println("movie page")
            WebUser user = WebUser.findByUsername(params['u'] as String)
            System.out.println(params['u'] as String)
            Movie movie = Movie.findByMovieId(params['movieId'] as String)
            System.out.println(params['movieId'] as String)
            def mode = params['mode']
            System.out.println(mode)
            MovieRating movieRating = MovieRating.findByMovieAndUser(movie, user)
            String foundMovieRating = "false";
            if(movieRating != null){
                foundMovieRating = "true";
            }
            List<TagRating> tagRatings = TagRating.findAllByTrUserAndTrMovie(user, movie)
            /*List<MovieRating> allRatings = MovieRating.findAllByMovie(movie);
            int count = allRatings.size();
            double sum = 0.0;
            for(int i=0; i<count; i++){
                sum+=allRatings.get(i).movieRating;
            }*/
            double avg = movie.avgRating;
            double avg2 = Math.round(avg*100)/100.0;
            int count = movie.previousCount+MovieRating.findAllByMovie(movie).size();
            ArrayList<TagCount> tcs = TagCount.findAllByTcMovieId(movie.movieId);
            HashMap<Tag,TagCount> tagging = new HashMap<Tag,TagCount>();
            for(TagCount tc: tcs){
                tagging.put(Tag.findByTagName(tc.tcTagname),tc);
            }
            render(view:"moviePage", model:[tagging:tagging, mode:mode, count:count, avg2:avg2, user:user, movie:movie, movieRating:movieRating, foundMovieRating:foundMovieRating, tagRatings:tagRatings])
    }

    def moviePageRec={
        System.out.println("movie page")
        WebUser user = WebUser.findByUsername(params['u'] as String)
        System.out.println(params['u'] as String)
        Movie movie = Movie.findByMovieId(params['movieId'] as String)
        System.out.println(params['movieId'] as String)
        def mode = params['mode']
        System.out.println(mode)
        MovieRating movieRating = MovieRating.findByMovieAndUser(movie, user)
        String foundMovieRating = "false";
        if(movieRating != null){
            foundMovieRating = "true";
        }
        List<TagRating> tagRatings = TagRating.findAllByTrUserAndTrMovie(user, movie)
        /*List<MovieRating> allRatings = MovieRating.findAllByMovie(movie);
        int count = allRatings.size();
        double sum = 0.0;
        for(int i=0; i<count; i++){
            sum+=allRatings.get(i).movieRating;
        }*/
        double avg = movie.avgRating;
        double avg2 = Math.round(avg*100)/100.0;
        int count = movie.previousCount+MovieRating.findAllByMovie(movie).size();
        ArrayList<TagCount> tcs = TagCount.findAllByTcMovieId(movie.movieId);
        HashMap<Tag,TagCount> tagging = new HashMap<Tag,TagCount>();
        for(TagCount tc: tcs){
            tagging.put(Tag.findByTagName(tc.tcTagname),tc);
        }
        render(view:"moviePageRec", model:[tagging:tagging, mode:mode, count:count, avg2:avg2, user:user, movie:movie, movieRating:movieRating, foundMovieRating:foundMovieRating, tagRatings:tagRatings])
    }

    def moviePageRated={
        System.out.println("movie page")
        WebUser user = WebUser.findByUsername(params['u'] as String)
        System.out.println(params['u'] as String)
        Movie movie = Movie.findByMovieId(params['movieId'] as String)
        System.out.println(params['movieId'] as String)
        def mode = params['mode']
        System.out.println(mode)
        MovieRating movieRating = MovieRating.findByMovieAndUser(movie, user)
        String foundMovieRating = "false";
        if(movieRating != null){
            foundMovieRating = "true";
        }
        List<TagRating> tagRatings = TagRating.findAllByTrUserAndTrMovie(user, movie)
        /*List<MovieRating> allRatings = MovieRating.findAllByMovie(movie);
        int count = allRatings.size();
        double sum = 0.0;
        for(int i=0; i<count; i++){
            sum+=allRatings.get(i).movieRating;
        }*/
        double avg = movie.avgRating;
        double avg2 = Math.round(avg*100)/100.0;
        int count = movie.previousCount+MovieRating.findAllByMovie(movie).size();
        ArrayList<TagCount> tcs = TagCount.findAllByTcMovieId(movie.movieId);
        HashMap<Tag,TagCount> tagging = new HashMap<Tag,TagCount>();
        for(TagCount tc: tcs){
            tagging.put(Tag.findByTagName(tc.tcTagname),tc);
        }
        render(view:"moviePageRated", model:[tagging:tagging, mode:mode, count:count, avg2:avg2, user:user, movie:movie, movieRating:movieRating, foundMovieRating:foundMovieRating, tagRatings:tagRatings])
    }

    /*def makeRecommendations={
        WebUser user = WebUser.findByUsername(params['u'] as String)
        String[] users = new String[1];
        users[0] = user.userId
        def mode = params['mode']
        def movies = Movie.findAll()
        for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }
        CBFMain rec = new CBFMain(users);
        HashMap<String, TreeMap<Number,Number>> results = rec.results;
        HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
        for(String e : results.keySet()){
            for(Number entry : results.get(e).keySet()){
                Movie movie = Movie.findByMovieId(entry as String)
                recList.put(movie, results.get(e).get(entry))
            }
        }
        Map sortedRecList = recList.sort { a, b ->
            -a.value <=> -b.value
        }
        render(view:"userRecommendations", model:[user:user, sortedRecList:sortedRecList, mode:mode])
    }*/

    def makeRecommendations={
        WebUser user = WebUser.findByUsername(params['u'] as String)
        String[] users = new String[1];
        users[0] = user.userId
        def mode = params['mode']
        def movies = Movie.findAll()
        /*for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        Long start = System.currentTimeMillis();
        Map sortedRecList;
        if(!user.turker){
            CF rec = new CF(users);
            HashMap<Integer, String> results = rec.result;
            HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
            for (Integer e : results.keySet()) {
                Movie movie = Movie.findByMovieId(results.get(e));
                recList.put(movie, e);
            }
            if(recList.size() < 5){
                recList = new  HashMap<Movie, Number>()
                movies =  movies.sort{it.avgRating*-1}
                int count = 0;
                for(m in movies){
                    if(m.previousCount >= 20){
                        recList.put(m, m.avgRating*-1);
                        count ++;
                    }
                    if (count >= 10){
                        break;
                    }
                }
            }
            sortedRecList = recList.sort { a, b ->
                a.value <=> b.value
            }
        }else{
            if(Integer.parseInt(user.rewardCode)%2){
                CF rec = new CF(users);
                HashMap<Integer, String> results = rec.result;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for (Integer e : results.keySet()) {
                    Movie movie = Movie.findByMovieId(results.get(e));
                    recList.put(movie, e);
                }
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating*-1);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    a.value <=> b.value
                }
            }else{
                CBFMainC rec = new CBFMainC(users);
                HashMap<String, TreeMap<Number,Number>> results = rec.results;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for(String e : results.keySet()){
                    for(Number entry : results.get(e).keySet()){
                        Movie movie = Movie.findByMovieId(entry as String)
                        recList.put(movie, results.get(e).get(entry))
                    }
                }
                System.out.println("Tag rec length: "+recList.size());
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    -a.value <=> -b.value
                }
            }
        }
        HashMap<String,ArrayList<TagCount>> tcs = new HashMap<String,ArrayList<TagCount>>();
        for(Movie m : sortedRecList.keySet()){
            tcs.put(m.movieId,TagCount.findAllByTcMovieId(m.movieId));
        }
        Long end = System.currentTimeMillis();
        System.out.println("rec time (sec): "+(end-start)/(1000.0));
        render(view:"userRecommendations", model:[tcs:tcs, user:user, sortedRecList:sortedRecList, mode:mode])
    }

    def makeRecommendationsA={
        def userId = params.userid;
        WebUser user = WebUser.findByUserId(userId)
        String[] users = new String[1];
        users[0] = userId
        def movies = Movie.findAll()
        /*for (Movie m in movies) {
            if (m.userMovieRatings.size() == 0) {
                m.avgRating = 0.0;
                m.save(failOnError: true);
            } else {
                def sum = 0;
                for (MovieRating r in m.userMovieRatings) {
                    sum += r.movieRating;
                }
                def avg = sum / m.userMovieRatings.size();
                avg = Math.round(avg * 100) / 100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        Long start = System.currentTimeMillis();
        Map sortedRecList;
        if(!user.turker){
            CF rec = new CF(users);
            HashMap<Integer, String> results = rec.result;
            HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
            for (Integer e : results.keySet()) {
                Movie movie = Movie.findByMovieId(results.get(e));
                recList.put(movie, e);
            }
            if(recList.size() < 5){
                recList = new  HashMap<Movie, Number>()
                movies =  movies.sort{it.avgRating*-1}
                int count = 0;
                for(m in movies){
                    if(m.previousCount >= 20){
                        recList.put(m, m.avgRating*-1);
                        count ++;
                    }
                    if (count >= 10){
                        break;
                    }
                }
            }
            sortedRecList = recList.sort { a, b ->
                a.value <=> b.value
            }
        }else{
            if(Integer.parseInt(user.rewardCode)%2){
                CF rec = new CF(users);
                HashMap<Integer, String> results = rec.result;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for (Integer e : results.keySet()) {
                    Movie movie = Movie.findByMovieId(results.get(e));
                    recList.put(movie, e);
                }
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating*-1);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    a.value <=> b.value
                }
            }else{
                CBFMainC rec = new CBFMainC(users);
                HashMap<String, TreeMap<Number,Number>> results = rec.results;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for(String e : results.keySet()){
                    for(Number entry : results.get(e).keySet()){
                        Movie movie = Movie.findByMovieId(entry as String)
                        recList.put(movie, results.get(e).get(entry))
                    }
                }
                System.out.println("Tag rec length: "+recList.size());
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    -a.value <=> -b.value
                }
            }
        }
        HashMap<String,ArrayList<TagCount>> tcs = new HashMap<String,ArrayList<TagCount>>();
        for(Movie m : sortedRecList.keySet()){
            tcs.put(m.movieId,TagCount.findAllByTcMovieId(m.movieId));
        }
        Long end = System.currentTimeMillis();
        System.out.println("rec time (sec): "+(end-start)/(1000.0));
        render(template: "userRecommendationsA", model: [tcs:tcs, user: user, sortedRecList: sortedRecList])
    }

    def makeRecommendationsB={
        def userId = params.userid;
        WebUser user = WebUser.findByUserId(userId)
        String[] users = new String[1];
        users[0] = userId
        def movies = Movie.findAll()
        for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }
        CBFMainB rec = new CBFMainB(users);
        HashMap<String, TreeMap<Number,Number>> results = rec.results;
        HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
        for(String e : results.keySet()){
            for(Number entry : results.get(e).keySet()){
                Movie movie = Movie.findByMovieId(entry as String)
                recList.put(movie, results.get(e).get(entry))
            }
        }
        Map sortedRecList = recList.sort { a, b ->
            -a.value <=> -b.value
        }
        render(template:"userRecommendationsB", model:[user:user, sortedRecList:sortedRecList])
    }

    def makeRecommendationsC={
        def userId = params.userid;
        WebUser user = WebUser.findByUserId(userId)
        String[] users = new String[1];
        users[0] = userId
        def movies = Movie.findAll()
        /*for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        Long start = System.currentTimeMillis();
        Map sortedRecList;
        if(!user.turker){
            CBFMainC rec = new CBFMainC(users);
            HashMap<String, TreeMap<Number,Number>> results = rec.results;
            HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
            for(String e : results.keySet()){
                for(Number entry : results.get(e).keySet()){
                    Movie movie = Movie.findByMovieId(entry as String)
                    recList.put(movie, results.get(e).get(entry))
                }
            }
            System.out.println("Tag rec length: "+recList.size());
            if(recList.size() < 5){
                recList = new  HashMap<Movie, Number>()
                movies =  movies.sort{it.avgRating*-1}
                int count = 0;
                for(m in movies){
                    if(m.previousCount >= 20){
                        recList.put(m, m.avgRating);
                        count ++;
                    }
                    if (count >= 10){
                        break;
                    }
                }
            }
            sortedRecList = recList.sort { a, b ->
                -a.value <=> -b.value
            }
        }else{
            if(Integer.parseInt(user.rewardCode)%2){
                CBFMainC rec = new CBFMainC(users);
                HashMap<String, TreeMap<Number,Number>> results = rec.results;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for(String e : results.keySet()){
                    for(Number entry : results.get(e).keySet()){
                        Movie movie = Movie.findByMovieId(entry as String)
                        recList.put(movie, results.get(e).get(entry))
                    }
                }
                System.out.println("Tag rec length: "+recList.size());
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    -a.value <=> -b.value
                }
            }else{
                CF rec = new CF(users);
                HashMap<Integer, String> results = rec.result;
                HashMap<Movie, Number> recList = new  HashMap<Movie, Number>()
                for (Integer e : results.keySet()) {
                    Movie movie = Movie.findByMovieId(results.get(e));
                    recList.put(movie, e);
                }
                if(recList.size() < 5){
                    recList = new  HashMap<Movie, Number>()
                    movies =  movies.sort{it.avgRating*-1}
                    int count = 0;
                    for(m in movies){
                        if(m.previousCount >= 20){
                            recList.put(m, m.avgRating*-1);
                            count ++;
                        }
                        if (count >= 10){
                            break;
                        }
                    }
                }
                sortedRecList = recList.sort { a, b ->
                    a.value <=> b.value
                }
            }
        }
        HashMap<String,ArrayList<TagCount>> tcs = new HashMap<String,ArrayList<TagCount>>();
        for(Movie m : sortedRecList.keySet()){
            tcs.put(m.movieId,TagCount.findAllByTcMovieId(m.movieId));
        }
        Long end = System.currentTimeMillis();
        System.out.println("rec time (sec): "+(end-start)/(1000.0));
        render(template:"userRecommendationsC", model:[tcs:tcs, user:user, sortedRecList:sortedRecList])
    }

    def rateMovie = {
        def rating = params.rating
        //System.out.println("rating: " + rating)
        def userId = params.userid
        def movieId = params.movieid

        if(MovieRating.findByUserAndMovie(WebUser.findByUserId(userId),  Movie.findByMovieId(movieId))==null){

            def movieRating = new MovieRating(
                    movieRating: rating,
                    movie: Movie.findByMovieId(movieId),
                    user: WebUser.findByUserId(userId)
            )
            movieRating.save(failOnError: true)

            WebUser user = WebUser.findByUserId(userId)
            user.addToMovieRatings(movieRating)
            user.save(failOnError: true)
            Movie movie = Movie.findByMovieId(movieId)
            movie.addToUserMovieRatings(movieRating)
            movie.save(failOnError: true)
            //FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\newratings0.csv", true);
            FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewUsers.csv", true);
            pw.append(userId+","+movieId+","+rating+"\n");
            pw.flush();
            pw.close();

            def sql = Sql.newInstance(dataSource);
            def exist = sql.execute("insert into offline_rating (item,rating,\"user\") values (?,?,?)",
                    [Long.parseLong(movieId),Double.parseDouble(rating),Long.parseLong(userId)]);
            //def exist = sql.execute("insert into offline_rating_s (item,rating,\"user\") values (?,?,?)",
            //       [Long.parseLong(movieId),Double.parseDouble(rating),Long.parseLong(userId)]);

        }else{
            try {
                String lineToRemove = userId+","+movieId;
                //File inFile = new File("D:\\MAC\\CSHonors\\data\\newratings0.csv");
                File inFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\ratingsNewUsers.csv");
                if (!inFile.isFile()) {
                    System.out.println("Parameter is not an existing file");
                    return;
                }
                //Construct the new file that will later be renamed to the original filename.
                File tempFile = new File(inFile.getAbsolutePath() + ".csv");

                BufferedReader br = new BufferedReader(new FileReader(inFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                String line = null;

                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {
                    if (!line.trim().contains(lineToRemove)) {
                        pw.println(line);
                        //System.out.println(line);
                        pw.flush();
                    }else{
                        pw.println(lineToRemove+","+rating);
                        System.out.println(lineToRemove+","+rating);
                        pw.flush();
                    }
                }
                pw.close();
                br.close();
                //Delete the original file
                if (!inFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inFile))
                    System.out.println("Could not rename file");
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            def updatedMovieRating = MovieRating.findByUserAndMovie(WebUser.findByUserId(userId),  Movie.findByMovieId(movieId));
            updatedMovieRating.movieRating = Double.parseDouble(rating);
            updatedMovieRating.save(failOnError: true);

            def r = Double.parseDouble(rating);
            def longm = Long.parseLong(movieId);
            def longu = Long.parseLong(userId);
            def sql = Sql.newInstance(dataSource);
            def exist = sql.execute("update offline_rating set rating=$r where item=$longm and \"user\"=$longu");
            //def exist = sql.execute("update offline_rating_s set rating=$r where item=$longm and \"user\"=$longu");
        }
        render(template:'movieRatingArea',model: [rating:rating])
    }

    def upvote={
        def tagId = params.tagid
        //System.out.println("rating: " + rating)
        def tag = Tag.findById(tagId);
        def userId = params.userid
        def user = WebUser.findByUserId(userId);
        def movieId = params.movieid
        def movie = Movie.findByMovieId(movieId)
        def s = "";

        if(TagRating.findByTrMovieAndTrUserAndTrTag(movie, user, tag)==null){
            def tagRating = new TagRating(
                    trTag: tag,
                    trUser: user,
                    trMovie: movie,
                    tagRating: 1
            )
            tagRating.save(failOnError: true)

            user.addToUserTagRatings(tagRating);
            user.save(failOnError: true);
            movie.addToUserTagRatings(tagRating);
            movie.save(failOnError: true);
            tag.addToTagRatings(tagRating);
            tag.save(failOnError: true);
            //FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\user-tagratings.csv", true);
            FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv", true);
            pw.append(userId+","+movieId+","+tag.tagName+",1\n");
            pw.flush();
            pw.close();
            s =tag.tagName+" : "+tagRating.tagRating;
        }else{
            // rating was -1
            try {
                String lineToRemove = userId+","+movieId+","+tag.tagName;
                //File inFile = new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv");
                File inFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv");
                if (!inFile.isFile()) {
                    System.out.println("Parameter is not an existing file");
                    return;
                }
                //Construct the new file that will later be renamed to the original filename.
                File tempFile = new File(inFile.getAbsolutePath() + ".csv");

                BufferedReader br = new BufferedReader(new FileReader(inFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                String line = null;

                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {
                    if (!line.trim().contains(lineToRemove)) {
                        pw.println(line);
                        //System.out.println(line);
                        pw.flush();
                    }
                }
                pw.close();
                br.close();
                //Delete the original file
                if (!inFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inFile))
                    System.out.println("Could not rename file");
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            def tagRating = TagRating.findByTrMovieAndTrUserAndTrTag(movie, user, tag);
            user.removeFromUserTagRatings(tagRating);
            user.save(failOnError: true);
            movie.removeFromUserTagRatings(tagRating);
            movie.save(failOnError: true);
            tag.removeFromTagRatings(tagRating);
            tag.save(failOnError: true);
        }

        render(template:'userTagRating',model: [s:s])
    }

    def downvote={
        def tagId = params.tagid
        //System.out.println("rating: " + rating)
        def tag = Tag.findById(tagId);
        def userId = params.userid
        def user = WebUser.findByUserId(userId);
        def movieId = params.movieid
        def movie = Movie.findByMovieId(movieId)
        def s = "";

        if(TagRating.findByTrMovieAndTrUserAndTrTag(movie, user, tag)==null){
            def tagRating = new TagRating(
                    trTag: tag,
                    trUser: user,
                    trMovie: movie,
                    tagRating: -1
            )
            tagRating.save(failOnError: true)

            user.addToUserTagRatings(tagRating);
            user.save(failOnError: true);
            movie.addToUserTagRatings(tagRating);
            movie.save(failOnError: true);
            tag.addToTagRatings(tagRating);
            tag.save(failOnError: true);
            //FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\user-tagratings.csv", true);
            FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv", true);
            pw.append(userId+","+movieId+","+tag.tagName+",-1\n");
            pw.flush();
            pw.close();
            s =tag.tagName+" : "+tagRating.tagRating;
        }else{
            // rating was 1
            try {
                String lineToRemove = userId+","+movieId+","+tag.tagName;
                System.out.println(lineToRemove);
                //File inFile = new File("D:\\MAC\\CSHonors\\data\\user-tagratings.csv");
                File inFile = new File("D:\\MAC\\CSHonors\\data\\ml-10M100K\\userTagRatingsSrc.csv");
                if (!inFile.isFile()) {
                    System.out.println("Parameter is not an existing file");
                    return;
                }
                //Construct the new file that will later be renamed to the original filename.
                File tempFile = new File(inFile.getAbsolutePath() + ".csv");

                BufferedReader br = new BufferedReader(new FileReader(inFile));
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                String line = null;

                //Read from the original file and write to the new
                //unless content matches data to be removed.
                while ((line = br.readLine()) != null) {
                    if (!line.trim().contains(lineToRemove)) {
                        pw.println(line);
                        //System.out.println(line);
                        pw.flush();
                    }
                }
                pw.close();
                br.close();
                //Delete the original file
                if (!inFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                //Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(inFile))
                    System.out.println("Could not rename file");
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            def tagRating = TagRating.findByTrMovieAndTrUserAndTrTag(movie, user, tag);
            user.removeFromUserTagRatings(tagRating);
            user.save(failOnError: true);
            movie.removeFromUserTagRatings(tagRating);
            movie.save(failOnError: true);
            tag.removeFromTagRatings(tagRating);
            tag.save(failOnError: true);
        }

        render(template:'userTagRating',model: [s:s])
    }

    def popularity={
        def userId = params.userid;
        def user = WebUser.findByUserId(userId);
        def movies = Movie.findAll()
        /*for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        int len = 0;
        if(movies.size()<200){
            len = movies.size()-1;
        }else{
            len = 199;
        }
        movies = movies.sort{(it.userMovieRatings.size()+it.previousCount)*-1}[0..len]
        render(template:'popularity', model:[movies: movies, u:user])
    }

    def highest={
        def userId = params.userid;
        def user = WebUser.findByUserId(userId);
        def movies = Movie.findAll()
        /*for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        int len = 0;
        if(movies.size()<200){
            len = movies.size()-1;
        }else{
            len = 199;
        }
        movies = movies.sort{-it.avgRating}[0..len]
        render(template:'highest', model:[movies: movies, u:user])
    }

    def alphabet={
        def userId = params.userid;
        def user = WebUser.findByUserId(userId);
        def movies = Movie.findAll()
        movies = movies.findAll{it.movieName.startsWith("A")}
        render(template:'alphabet', model:[movies: movies, u:user])
    }

    def collectFeedback={
        def userId = params.userid;
        def user = WebUser.findByUserId(userId);
        //FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\feedback.csv", true);
        FileWriter pw = new FileWriter("D:\\MAC\\CSHonors\\data\\ml-10M100K\\feedback.csv", true);
        String[] surveyAns = new String[6];
        surveyAns[0] = params.ans1 as String
        surveyAns[1] = params.ans2 as String
        surveyAns[2] = params.ans3 as String
        surveyAns[3] = params.ans4 as String
        surveyAns[4] = params.ans5 as String
        surveyAns[5] = params.ans6 as String
        surveyAns[5] = "comments"+surveyAns[5];
        String recommender = "";
        if(!user.turker){
            if(params.rec.toString().equals('X')){
                recommender = 'A';
            }else if(params.rec.toString().equals('Y')){
                recommender = 'B';
            }
        }else{
            if(params.rec.toString().equals('X') && Integer.parseInt(user.rewardCode)%2 == 1){
                recommender = 'A';
            }else if(params.rec.toString().equals('Y') && Integer.parseInt(user.rewardCode)%2 == 1){
                recommender = 'B';
            }else if(params.rec.toString().equals('X') && Integer.parseInt(user.rewardCode)%2 == 0){
                recommender = 'B';
            }else if(params.rec.toString().equals('Y') && Integer.parseInt(user.rewardCode)%2 == 0){
                recommender = 'A';
            }
        }
        for(int i=0; i<6; i++){
            def line = userId+","+recommender+","+(i+1)+","+surveyAns[i]+"\n";
            pw.append(line);
            System.out.println(line);
            pw.flush();
        }
        pw.close();
        if(recommender.equals('A')){
            user.finishA = 1;
            user.save(failOnError: true);
        }else if(recommender.equals('B')){
            user.finishB = 1;
            user.save(failOnError: true);
        }
        //pw = new FileWriter("D:\\MAC\\CSHonors\\data\\users.csv", true);
        pw = new FileWriter("D:\\MAC\\CSHonors\\data\\ml-10M100K\\usersSrc.csv", true);
        def line = userId+","+user.username+","+user.turker+","+user.finishA+","+user.finishB+","+user.rewardCode+"\n";
        pw.append(line);
        pw.flush();
        pw.close();
        render(template:'movieRatingArea', model:[r:userId])
    }

    def letterMovie={
        def userId = params.userid;
        def user = WebUser.findByUserId(userId);
        def letter = params.lid;
        def movies = Movie.findAll()
        if(!letter.equals("0")){
            movies = movies.findAll{it.movieName.startsWith(letter)}
        }else{
            movies = movies.findAll{it.movieName.matches("([^a-zA-Z]).*")}
        }
        render(template:'letterMovie', model:[movies: movies, u:user])
    }

    def ratedMovies={
        WebUser user = WebUser.findByUsername(params['u'] as String)
        System.out.println(params['u'] as String)
        def movies = Movie.findAll()
        /*for(Movie m in movies){
            if(m.userMovieRatings.size() == 0){
                m.avgRating = 0.0;
                m.save(failOnError: true);
            }else{
                def sum = 0;
                for(MovieRating r in m.userMovieRatings){
                    sum += r.movieRating;
                }
                def avg = sum/m.userMovieRatings.size();
                avg = Math.round(avg*100)/100.0;
                m.avgRating = avg;
                m.save(failOnError: true);
            }
        }*/
        def mrs = user.movieRatings;
        mrs = mrs.sort{-it.movieRating}
        render(view:"ratedMovies", model:[u:user, mrs:mrs])
    }
}
