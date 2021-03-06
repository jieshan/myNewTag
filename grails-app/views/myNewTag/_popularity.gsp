<div class="infotop">Top 200 Movies In Popularity</div>
<g:each in="${movies}" var="movie">
    <div class="movie-entry">
        <g:link action="moviePage" id="${movie.movieId}" params="[movieId:movie.movieId, u:u.username, mode:2]" style="text-decoration: none;">
            <img src="${movie.imgurl}" style="float:left;margin-left: 30px;height: 150px;width: 110px;">
            <b class="movie-rating-count" style="display: block;">${movie.userMovieRatings.size()+movie.previousCount}</b>
            <div class="ratings">ratings</div>
            <div class="avg">avg.</div>
            <div class="avg-rating">${Math.round(movie.avgRating*100)/100.0}</div>
            <b class="movie-name" style="display: block;margin-top: 20px;">${movie.movieName}</b>
        </g:link>
    </div>
</g:each>