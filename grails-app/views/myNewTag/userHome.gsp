<%--
  Created by IntelliJ IDEA.
  User: Jie Shan
  Date: 13-11-3
  Time: 下午5:51
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<link rel="stylesheet" type="text/css" href="../css/userHome.css" />
<link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
<head>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:require module="tagcloud" />
    <r:layoutResources/>
  <title>Welcome, ${u.username}</title>
</head>
<script>
    $(document).ready(function(){
        $('.letter').on('click',function(){
            //alert(this.id);
            $('.letter').css("color", "#333339");
            $('#'+this.id).css("color", "#ff841d");
            var userId = $('#userId').val();
            var lid = this.id;
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/letterMovie?lid='+lid+'&userid='+userId,
                success: function(data){
                    var results = $('#alphMovieList');
                    results.html(data);
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        //alert("ready");
        $('#popular').on('click',function(){
            //alert("popular");
            var userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/popularity?userid='+userId,
                success: function(data){
                    var results = $('#movies');
                    results.html(data);
                    var popularity = $('#popular');
                    popularity.css("background-color", "#ff841d");
                    popularity.css("color", "#ffffff");
                    popularity.hover(function(){
                        popularity.css("background-color", "#ff841d");
                        popularity.css("color", "#ffffff");
                    },function(){
                        popularity.css("background-color", "#ff841d");
                        popularity.css("color", "#ffffff");
                    });
                    var alphabet = $('#alph');
                    alphabet.css("background-color", "#ffffff");
                    alphabet.css("color", "#ff841d");
                    alphabet.hover(function(){
                        alphabet.css("transition", "all 0.18s ease-out");
                        alphabet.css("background-color","#ffc51f");
                        alphabet.css("color","#ffffff");
                    },function(){
                        alphabet.css("transition", "all 0.18s ease-out");
                        alphabet.css("background-color", "#ffffff");
                        alphabet.css("color", "#ff841d");
                    });
                    var highest = $('#highest');
                    highest.css("background-color", "#ffffff");
                    highest.css("color", "#ff841d");
                    highest.hover(function(){
                        highest.css("transition", "all 0.18s ease-out");
                        highest.css("background-color","#ffc51f");
                        highest.css("color","#ffffff");
                    },function(){
                        highest.css("transition", "all 0.18s ease-out");
                        highest.css("background-color", "#ffffff");
                        highest.css("color", "#ff841d");
                    });
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        $('#highest').on('click',function(){
            //alert("highest");
            var userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/highest?userid='+userId,
                success: function(data){
                    var results = $('#movies');
                    results.html(data);
                    var highest = $('#highest');
                    highest.css("background-color", "#ff841d");
                    highest.css("color", "#ffffff");
                    highest.hover(function(){
                        highest.css("background-color", "#ff841d");
                        highest.css("color", "#ffffff");
                    },function(){
                        highest.css("background-color", "#ff841d");
                        highest.css("color", "#ffffff");
                    });
                    var alphabet = $('#alph');
                    alphabet.css("background-color", "#ffffff");
                    alphabet.css("color", "#ff841d");
                    alphabet.hover(function(){
                        alphabet.css("transition", "all 0.18s ease-out");
                        alphabet.css("background-color","#ffc51f");
                        alphabet.css("color","#ffffff");
                    },function(){
                        alphabet.css("transition", "all 0.18s ease-out");
                        alphabet.css("background-color", "#ffffff");
                        alphabet.css("color", "#ff841d");
                    });
                    var popularity = $('#popular');
                    popularity.css("background-color", "#ffffff");
                    popularity.css("color", "#ff841d");
                    popularity.hover(function(){
                        popularity.css("transition", "all 0.18s ease-out");
                        popularity.css("background-color","#ffc51f");
                        popularity.css("color","#ffffff");
                    },function(){
                        popularity.css("transition", "all 0.18s ease-out");
                        popularity.css("background-color", "#ffffff");
                        popularity.css("color", "#ff841d");
                    });
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        $('#alph').on('click',function(){
            //alert("alph");
            var userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/alphabet?userid='+userId,
                success: function(data){
                    var results = $('#movies');
                    results.html(data);
                    var alphabet = $('#alph');
                    alphabet.css("background-color", "#ff841d");
                    alphabet.css("color", "#ffffff");
                    alphabet.hover(function(){
                        alphabet.css("background-color", "#ff841d");
                        alphabet.css("color", "#ffffff");
                    },function(){
                        alphabet.css("background-color", "#ff841d");
                        alphabet.css("color", "#ffffff");
                    });
                    var highest = $('#highest');
                    highest.css("background-color", "#ffffff");
                    highest.css("color", "#ff841d");
                    highest.hover(function(){
                        highest.css("transition", "all 0.18s ease-out");
                        highest.css("background-color","#ffc51f");
                        highest.css("color","#ffffff");
                    },function(){
                        highest.css("transition", "all 0.18s ease-out");
                        highest.css("background-color", "#ffffff");
                        highest.css("color", "#ff841d");
                    });
                    var popularity = $('#popular');
                    popularity.css("background-color", "#ffffff");
                    popularity.css("color", "#ff841d");
                    popularity.hover(function(){
                        popularity.css("transition", "all 0.18s ease-out");
                        popularity.css("background-color","#ffc51f");
                        popularity.css("color","#ffffff");
                    },function(){
                        popularity.css("transition", "all 0.18s ease-out");
                        popularity.css("background-color", "#ffffff");
                        popularity.css("color", "#ff841d");
                    });
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        var sortMode = $('#mode').val();
        //alert(sortMode)
        var letterBookmark = $('#letterBookmark').val();
        //alert(letterBookmark)
        if(sortMode == 2){
            $("#popular")[0].click();
        }else if(sortMode == 3){
            $("#highest")[0].click();
        }else if(sortMode == 1 && letterBookmark.length>0){
            $("#"+letterBookmark)[0].click();
        }
    });
</script>
<body>
<g:hiddenField value = "${u.userId}" name = "userId" id = "userId"/>
<g:hiddenField value = "${mode}" name = "mode" id = "mode"/>
<g:hiddenField value = "${letterBookmark}" name = "letterBookmark" id = "letterBookmark"/>
<div id="menu-primary">
    <div class="menu">
        <img class="logo" src='../images/logo_small.png'>
        <div id="menu-primary-items">
            <g:link action="home" params="[u:u.username]" method="post" style="color: #ff841d;"><div id="item1">Home</div></g:link>
            <g:if test="${(u.movieRatings.size()>=15 && u.userTagRatings.size()>=45) || u.turker==0}">
                <g:link action="makeRecommendations" params="[u:u.username, mode: 1]" method="post" style="color: #ff841d;"><div id="item2">Recommendations</div></g:link>
            </g:if>
            <g:else>
                <g:link action="makeRecommendations" params="[u:u.username, mode: 1]" method="post" style="color: #ff841d;display: none;"><div id="item2">Recommendations</div></g:link>
            </g:else>
            <g:link action="ratedMovies" params="[u:u.username]" method="post" style="color: #ff841d;"><div id="item3">Rated Movies</div></g:link>
        </div>
    </div>
</div>

<div id="panel">
    <div id="banner">
        <div id="left-banner">
            <div id="alph">Alphabet</div>
            <div id="popular">Popularity</div>
            <div id="highest">Rating</div>
        </div>
        <div id="right-banner">
            <div id="banner-name">${u.username}</div>
            <g:link action="login" style="color: #ffc51f;text-decoration: none;"><div id="banner-logout">(Log out)</div></g:link>
        </div>
        <div id="separator"></div>
    </div>
    <br>
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

    <div id="movies">
        <div class="letters">
            <div class="letter" id="0">#</div>
            <div class="letter" id="A">A</div>
            <div class="letter" id="B">B</div>
            <div class="letter" id="C">C</div>
            <div class="letter" id="D">D</div>
            <div class="letter" id="E">E</div>
            <div class="letter" id="F">F</div>
            <div class="letter" id="G">G</div>
            <div class="letter" id="H">H</div>
            <div class="letter" id="I">I</div>
            <div class="letter" id="J">J</div>
            <div class="letter" id="K">K</div>
            <div class="letter" id="L">L</div>
            <div class="letter" id="M">M</div>
            <div class="letter" id="N">N</div>
            <div class="letter" id="O">O</div>
            <div class="letter" id="P">P</div>
            <div class="letter" id="Q">Q</div>
            <div class="letter" id="R">R</div>
            <div class="letter" id="S">S</div>
            <div class="letter" id="T">T</div>
            <div class="letter" id="U">U</div>
            <div class="letter" id="V">V</div>
            <div class="letter" id="W">W</div>
            <div class="letter" id="X">X</div>
            <div class="letter" id="Y">Y</div>
            <div class="letter" id="Z">Z</div>
        </div>
        <div id="alphMovieList">
        <g:each in="${movies}" var="movie">
            <div class="movie-entry">
                <g:link action="moviePage" id="${movie.movieId}" params="[movieId:movie.movieId, u:u.username, mode:1]" style="text-decoration: none;">
                    <img class="movie-image" style="height: 150px;width: 110px;" src="${movie.imgurl}">
                    <b class="movie-name" style="display: block;">${movie.movieName}</b></g:link>
            </div>
        </g:each>
        </div>
    </div>
</div>
<br>


%{--User: ${u.userId}
<g:form action="makeRecommendations" params="[userId:u.userId]" method="post">
    <div class="buttons">
        <span class="formButton">
            <input type="submit" value="Recommendations" style=" border: solid 1px navy;
            color: navy;
            height: 30px;
            line-height: 30px;
            font-weight: bold;
            font-size: 16px;
            font-family: 'Anaheim', sans-serif;

            -webkit-border-radius: 12px;
            -moz-border-radius: 7px;
            border-radius: 7px;

            background: -webkit-gradient(linear, left top, left bottom,
            color-stop(0%, yellow), color-stop(15%, #ffd700), color-stop(100%, yellow));
            background: -moz-linear-gradient(top, yellow 0%, #ffd700 55%, yellow 130%);">
        </span>
    </div>
</g:form>--}%

%{--<div id="allmovies">Showing All ${movies.size()} Movies</div>
<g:each in="${movies}" var="movie">
    <div class="row">
        <g:link action="moviePage" id="${movie.movieId}" params="[movieId:movie.movieId, userId:u.userId]" style="float:left;margin-left: 15px;text-decoration: none;color:navy; font-size: 18px;">
            <b>${movie.movieName}</b></g:link>
    </div>
</g:each>--}%

</body>
</html>