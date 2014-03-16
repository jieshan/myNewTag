<g:each in="${sortedRecList}" var="rec">
    <div class="rec-movie">
        <g:link action="moviePageRec" id="${rec.key.movieId}" params="[movieId:rec.key.movieId, u:user.username, mode:2]" style="text-decoration: none;">
            <div class="rank">${sortedRecList.findIndexOf {it in rec}+1}</div>
            <img class="movie-img" style="height: 150px;width: 110px;" src=${rec.key.imgurl}>
            <div class="movie-info">
                <div class="movie-name">${rec.key.movieName}</div>
                <div class="avg">avg.</div>
                <div class="avg-rating">${rec.key.avgRating}</div>
            </div>
            <div class="popular-tags">
                <div class="popular-tags-text">Popular Tags</div>
                <g:each in="${rec.key.tagCounts.sort{it.tagCount*-1}.size()< 6 ? rec.key.tagCounts.sort{it.tagCount*-1} : rec.key.tagCounts.sort{it.tagCount*-1}.subList(0,6)}" var="tc">
                    <div class="tag-name">#${tc.tcTag.tagName}</div><div class="slash">/</div>
                </g:each>
            </div>
        </g:link>
    </div>
</g:each>