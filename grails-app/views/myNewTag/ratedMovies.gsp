<%--
  Created by IntelliJ IDEA.
  User: Jie Shan
  Date: 14-1-29
  Time: 下午8:41
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'ratedMovie.css')}" />
    <link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:layoutResources/>
  <title>Welcome, ${u.username}</title>
</head>
<body>
<g:hiddenField value = "${u.userId}" name = "userId" id = "userId"/>
<div id="menu-primary">
    <div class="menu">
        <img class="logo" src="${resource(dir:'images',file:'logo_small.png')}">
        <div id="menu-primary-items">
            <g:link action="home" params="[u:u.username]" method="post" style="color: #ff841d;"><div id="item1">Home</div></g:link>
            <g:if test="${(u.movieRatings.size()>=15 && u.userTagRatings.size()>=45) || u.turker==0}">
                <g:link action="makeRecommendations" params="[u:u.username, mode: 1]" method="post" style="color: #ff841d;"><div id="item2">Recommendations</div></g:link>
            </g:if>
            <g:else>
                <g:link action="makeRecommendations" params="[u:u.username, mode: 1]" method="post" style="color: #ff841d;display:none;"><div id="item2">Recommendations</div></g:link>
            </g:else>
            <g:link action="ratedMovies" params="[u:u.username]" method="post" style="color: #ff841d;"><div id="item3">Rated Movies</div></g:link>
        </div>
    </div>
</div>

<div id="panel">
    <div id="banner">
        <div id="left-banner">
            <div>All Rated Movies</div>
        </div>
        <div id="right-banner">
            <div id="banner-name">${u.username}</div>
            <g:link action="login" style="color: #ffc51f;text-decoration: none;"><div id="banner-logout">(Log out)</div></g:link>
        </div>
        <div id="separator"></div>
    </div>

    <g:if test="${u.turker && u.movieRatings.size()>=15 && u.userTagRatings.size()>=45}">
        <div id="reward">
            <g:if test="${u.finishA && u.finishB}">
                Your reward code is <a style="color: #ff841d;">${u.rewardCode}</a>. Thank you!
            </g:if>
            <g:elseif test="${u.finishA && Integer.parseInt(u.rewardCode)%2 == 1}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${u.finishA && Integer.parseInt(u.rewardCode)%2 == 0}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${u.finishB && Integer.parseInt(u.rewardCode)%2 == 1}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${u.finishB && Integer.parseInt(u.rewardCode)%2 == 0}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:else>
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">and</div>
                    <g:link action="makeRecommendations" params="[u:u.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:else>
        </div>
    </g:if>

    <br>
    <div id="movies">
        <g:each in="${mrs}" var="mr">
            <div class="movie-entry">
                <g:link action="moviePageRated" id="${mr.movie.movieId}" params="[movieId:mr.movie.movieId, u:u.username, mode:1]" style="text-decoration: none;">
                    <img class="movie-image" style="height: 150px;width: 110px;" src="${mr.movie.imgurl}">
                    <b class="movie-rating-count" style="display: block;">${mr.movieRating}</b>
                    <div class="ratings">from you</div>
                    <div class="avg">avg.</div>
                    <div class="avg-rating">${Math.round(mr.movie.avgRating*100)/100.0}</div>
                    <b class="movie-name" style="display: block;">${mr.movie.movieName}</b></g:link>
            </div>
        </g:each>
    </div>
</div>

</body>
</html>