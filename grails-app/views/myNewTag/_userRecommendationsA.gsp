<g:each in="${sortedRecList}" var="rec">
    <div class="rec-movie">
        <g:link action="moviePageRec" id="${rec.key.movieId}" params="[movieId:rec.key.movieId, u:user.username, mode:1]" style="text-decoration: none;">
            <div class="movie-info">
                <div class="rank">${sortedRecList.findIndexOf {it in rec}+1}</div>
                <div class="movie-name">${rec.key.movieName}</div>
                <img class="movie-img" style="height: 150px;width: 110px;" src=${rec.key.imgurl}>
                <div class="avg">avg.</div>
                <div class="avg-rating">${Math.round(rec.key.avgRating*100)/100.0}</div>
                <div class="popular-tags">
                    <div class="popular-tags-text">Popular Tags</div>
                    <g:each in="${rec.key.tagCounts.sort{it.tagCount*-1}.size()< 6 ? rec.key.tagCounts.sort{it.tagCount*-1} : rec.key.tagCounts.sort{it.tagCount*-1}.subList(0,6)}" var="tc">
                        <div class="tag-name">#${tc.tcTag.tagName}</div><div class="slash">/</div>
                    </g:each>
                </div>
            </div>
        </g:link>
    </div>
</g:each>