<g:each in="${movies}" var="movie">
    <div class="movie-entry">
        <g:link action="moviePage" id="${movie.movieId}" params="[movieId:movie.movieId, u:u.username, mode:1]" style="text-decoration: none;">
            <img class="movie-image" style="height: 150px;width: 110px;" src="${movie.imgurl}">
            <b class="movie-name" style="display: block;">${movie.movieName}</b></g:link>
    </div>
</g:each>