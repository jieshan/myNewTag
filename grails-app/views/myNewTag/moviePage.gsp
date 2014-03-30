<%--
  Created by IntelliJ IDEA.
  User: Jie Shan
  Date: 13-11-3
  Time: 下午9:27
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'moviePage.css')}" />
    <link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:require module="tagcloud" />
    <r:layoutResources/>
  <title>${movie.movieName}</title>
</head>
<script type="text/javascript" src="http://chir.ag/projects/ntc/ntc.js"></script>
<script>
    var canvasBoo = 1;
    var tagResponse = 0;
    var clickedId = "";
    var clickedColor = "";
    var defaultColor = "black";
    var currentSelectedColor = "navy";
    var upvotedColor = "orange";
    var downvotedColor = "silver";
    function tpu(s) {
        tagResponse = 1;
        //alert("clicked");
        if(canvasBoo){
            $('#myCanvas').tagcanvas("tagtofront", {id: s.id, time:500});
            canvasBoo = 0;
            if(document.getElementById(s.id)){
                //alert(document.getElementById(s.id).style.color);
                clickedColor = document.getElementById(s.id).style.color;
                document.getElementById(s.id).style.color = currentSelectedColor;
                document.getElementById('clickedTag').innerHTML = s.innerHTML;
                document.getElementById('panelTagId').innerHTML = s.id.substring(5);
                if(clickedColor == upvotedColor){
                    document.getElementById('response').innerHTML = "You upvoted this tag.";
                    document.getElementById('response').style.display = "";
                }else if(clickedColor == downvotedColor){
                    document.getElementById('response').innerHTML = "You downvoted this tag.";
                    document.getElementById('response').style.display = "";
                }else{
                    //document.getElementById('response').style.display = "none";
                }
                //alert(document.getElementById('panelTagId').innerHTML);
                document.getElementById('tagPanel').style.display = "";
                //alert(document.getElementById(s.id).style.color);
                $('#myCanvas').tagcanvas("reload");
                //document.getElementsByClassName("tagcloud").style.pointerEvents = "none";
                //document.getElementsByClassName("tagcloud").blockUI();
                //document.getElementsById(s.id).unblockUI();
                clickedId = s.id;
            }
            setTimeout(pauseCanvas,510);
        }else{
            if(s.id == clickedId){
                $('#myCanvas').tagcanvas("resume");
                //alert(document.getElementById(s.id).style.color);
                document.getElementById(clickedId).style.color = clickedColor;
                document.getElementById('clickedTag').innerHTML = "";
                document.getElementById('tagPanel').style.display = "none";
                //alert(document.getElementById(s.id).style.color);
                $('#myCanvas').tagcanvas("reload");
                canvasBoo = 1;
            }else{
                $('#myCanvas').tagcanvas("resume");
                document.getElementById(clickedId).style.color = clickedColor;
                document.getElementById('clickedTag').innerHTML = "";
                document.getElementById('tagPanel').style.display = "none";
                //alert(s.id);
                //alert(clickedId);
                $('#myCanvas').tagcanvas("tagtofront", {id: s.id, time:500});
                canvasBoo = 0;
                if(document.getElementById(s.id)){
                    clickedColor = document.getElementById(s.id).style.color;
                    document.getElementById(s.id).style.color = currentSelectedColor;
                    document.getElementById('clickedTag').innerHTML = s.innerHTML;
                    document.getElementById('panelTagId').innerHTML = s.id.substring(5);
                    if(clickedColor == upvotedColor){
                        document.getElementById('response').innerHTML = "You upvoted this tag.";
                        document.getElementById('response').style.display = "";
                    }else if(clickedColor == downvotedColor){
                        document.getElementById('response').innerHTML = "You downvoted this tag.";
                        document.getElementById('response').style.display = "";
                    }else{
                        //alert(clickedColor);
                        document.getElementById('response').innerHTML = "You haven't rated this tag.";
                        document.getElementById('response').style.display = "";
                        //document.getElementById('response').style.display = "none";
                    }
                    document.getElementById('tagPanel').style.display = "";
                    $('#myCanvas').tagcanvas("reload");
                    clickedId = s.id;
                }
                setTimeout(pauseCanvas,510);
            }
        }
        return false;
    }
    function pauseCanvas(){
        $('#myCanvas').tagcanvas("pause");
    }
    function saveCanvas(){
        if(canvasBoo == 0 && tagResponse == 0){
            $('#myCanvas').tagcanvas("resume");
            document.getElementById(clickedId).style.color = clickedColor;
            $('#myCanvas').tagcanvas("reload");
            document.getElementById('clickedTag').innerHTML = "";
            document.getElementById('tagPanel').style.display = "none";
            //alert("safetynet");
        }else if(canvasBoo == 0 && tagResponse == 1){
            //alert("do nothing");
            tagResponse = -1;
        }else if(canvasBoo == 0 && tagResponse == -1){
            //alert("setting 0");
            tagResponse = 0;
        }
    }
    $(document).ready(function(){
        divs  = $('.userTagRatings');
        //alert(divs.size());
        for(var i=0;i<divs.size();i++){
            div = divs[i];
            //alert(div.innerHTML.substring(div.innerHTML.length-4));
            if(div.innerHTML.substring(div.innerHTML.length-4)>0){
                document.getElementById("cloud"+div.id.substring(13)).style.color = upvotedColor;
            }else if(div.innerHTML.substring(div.innerHTML.length-4)<0){
                document.getElementById("cloud"+div.id.substring(13)).style.color = downvotedColor;
            }
        }
        TagCanvas.weight = true;
        TagCanvas.weightFrom = 'data-weight';
        $('#myCanvas').tagcanvas({
            textFont : 'Verdana',
            textColour : null,
            dragControl: true,
            outlineThickness : 1,
            maxSpeed : 0.04,
            depth : 0.75
        });
        $('#myCanvas').on('click',function(){
            setTimeout(saveCanvas,600);
        });
        $('#rateMovie').on('click',function(){
            var rating;
            var hasRating = 1;
            if(document.getElementById('0').checked) {
                rating = 0;
                //alert('0');
            }else if(document.getElementById('0.5').checked) {
                rating = 0.5;
                //alert('0.5');
            }else if(document.getElementById('1').checked) {
                rating = 1;
                //alert('1');
            }else if(document.getElementById('1.5').checked) {
                rating = 1.5;
                //alert('1.5');
            }else if(document.getElementById('2').checked) {
                rating = 2;
                //alert('2');
            }else if(document.getElementById('2.5').checked) {
                rating = 2.5;
                //alert('2.5');
            }else if(document.getElementById('3').checked) {
                rating = 3;
                //alert('3');
            }else if(document.getElementById('3.5').checked) {
                rating = 3.5;
                //alert('3.5');
            }else if(document.getElementById('4').checked) {
                rating = 4;
                //alert('4');
            }else if(document.getElementById('4.5').checked) {
                rating = 4.5;
                //alert('4.5');
            }else if(document.getElementById('5').checked) {
                rating = 5;
                //alert('5');
            }else{
                alert("Please specify a rating value first.");
                hasRating = 0;
            }
            if(hasRating){
                //alert("rating="+ rating);
                var userId = $('#userId').val();
                var movieId = $('#movieId').val();
                $.ajax({
                    url: 'http://localhost:8080/myNewTag/myNewTag/rateMovie?rating='+rating+'&userid='+userId+'&movieid='+movieId,
                    success: function(data){
                        var results = $('#historyMovieRating');
                        results.html(data);
                    },
                    error: function() {
                        alert("fail");
                    }
                });
            }
        });
        $('.upvote').on('click',function(){
            var tagId = $(this).attr("id");
            var userId = $('#userId').val();
            var movieId = $('#movieId').val();
            //alert("plus "+tagId);
            var tagr = tagId+"tagrating";
            //alert(tagr);
            try{
                var x=document.getElementById(tagr);
                var r = x.getAttribute("class");
                //alert(r);
                if(r>0){
                    alert("You have already upvoted this tag.");
                }else{
                    $.ajax({
                        url: 'http://localhost:8080/myNewTag/myNewTag/upvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                        success: function(data){
                            //alert("success");
                            var results = $('#'+tagId+'tagrating');
                            //results.setAttribute("class","0.0");
                            results.attr("class",parseInt(results.attr("class"))+1);
                            //alert(results.attr("class"));
                            results.html(data);
                        },
                        error: function() {
                            alert("fail");
                        }
                    });
                }
            }catch(err) {
                //alert("not upvoted yet.");
                $.ajax({
                    url: 'http://localhost:8080/myNewTag/myNewTag/upvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                    success: function(data){
                        //alert(data);
                        var sel = "#"+tagId+"container";
                        $(sel).append("<div style=\"color: blue\" id=\""+tagId+"tagrating\" class=\"1.0\">"+data+"</div>");
                    },
                    error: function() {
                        alert("fail");
                    }
                });
            }
        });
        $('#newUpvote').on('click',function(){
            var tagId = document.getElementById('panelTagId').innerHTML;
            //alert(tagId);
            var userId = $('#userId').val();
            var movieId = $('#movieId').val();;
            if(clickedColor == upvotedColor){
                alert("You have already upvoted this tag.");
            }else{
                $.ajax({
                    url: 'http://localhost:8080/myNewTag/myNewTag/upvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                    success: function(data){
                        if(clickedColor == downvotedColor){
                            document.getElementById('response').innerHTML = "You haven't rated this tag.";
                            //document.getElementById('response').style.display = "none";
                            clickedColor = defaultColor;
                        }else if(clickedColor == defaultColor){
                            document.getElementById('response').innerHTML = "You upvoted this tag.";
                            document.getElementById('response').style.display = "";
                            clickedColor = upvotedColor;
                        }
                    },
                    error: function() {
                        alert("fail");
                    }
                });
            }
        });
        $('#newDownvote').on('click',function(){
            var tagId = document.getElementById('panelTagId').innerHTML;
            //alert(tagId);
            var userId = $('#userId').val();
            var movieId = $('#movieId').val();;
            if(clickedColor == downvotedColor){
                alert("You have already downvoted this tag.");
            }else{
                $.ajax({
                    url: 'http://localhost:8080/myNewTag/myNewTag/downvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                    success: function(data){
                        if(clickedColor == upvotedColor){
                            document.getElementById('response').innerHTML = "You haven't rated this tag.";
                            //document.getElementById('response').style.display = "none";
                            clickedColor = defaultColor;
                        }else if(clickedColor == defaultColor){
                            document.getElementById('response').innerHTML = "You downvoted this tag.";
                            document.getElementById('response').style.display = "";
                            clickedColor = downvotedColor;
                        }
                    },
                    error: function() {
                        alert("fail");
                    }
                });
            }
        });
        $('.downvote').on('click',function(){
            var tagId = $(this).attr("id");
            var userId = $('#userId').val();
            var movieId = $('#movieId').val();
            //alert("minus "+tagId);
            var tagr = tagId+"tagrating";
            //alert(tagr);
            try{
                var x=document.getElementById(tagr);
                var r = x.getAttribute("class");
                //alert(r);
                if(r<0){
                    alert("You have already downvoted this tag.");
                }else{
                    $.ajax({
                        url: 'http://localhost:8080/myNewTag/myNewTag/downvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                        success: function(data){
                            //alert(data);
                            var sel = "#"+tagId+"tagrating";
                            var results = $(sel);
                            //results.setAttribute("class","0.0");
                            results.attr("class",parseInt(results.attr("class"))-1);
                            //alert(results.attr("class"));
                            results.html(data);
                        },
                        error: function() {
                            alert("fail");
                        }
                    });
                }
            }catch(err) {
                //alert("not downvoted yet.");
                $.ajax({
                    url: 'http://localhost:8080/myNewTag/myNewTag/downvote?tagid='+tagId+'&userid='+userId+'&movieid='+movieId,
                    success: function(data){
                        //alert(data);
                        var sel = "#"+tagId+"container";
                        $(sel).append("<div style=\"color: blue\" id=\""+tagId+"tagrating\" class=\"-1.0\">"+data+"</div>");
                    },
                    error: function() {
                        alert("fail");
                    }
                });
            }
        });
    });
</script>
<body>
<g:hiddenField value = "${user.userId}" name = "userId" id = "userId"/>
<g:hiddenField value = "${movie.movieId}" name = "movieId" id="movieId"/>
<div id="menu-primary">
    <div class="menu">
        <img class="logo" src="${resource(dir:'images',file:'logo_small.png')}">
        <div id="menu-primary-items">
            <g:link action="home" params="[u:user.username, mode:mode, movie:movie.movieName]" method="post" style="color: #ff841d;"><div id="item1">Home</div></g:link>
            <g:if test="${(user.movieRatings.size()>=15 && user.userTagRatings.size()>=45) || user.turker==0}">
                <g:link action="makeRecommendations" params="[u:user.username,mode: 1]" method="post" style="color: #ff841d;"><div id="item2">Recommendations</div></g:link>
            </g:if>
            <g:else>
                <g:link action="makeRecommendations" params="[u:user.username,mode: 1]" method="post" style="color: #ff841d;display:none;"><div id="item2">Recommendations</div></g:link>
            </g:else>
            <g:link action="ratedMovies" params="[u:user.username]" method="post" style="color: #ff841d;"><div id="item3">Rated Movies</div></g:link>
        </div>
    </div>
</div>

<div id="panel">
    <div id="banner">
        <div id="left-banner">
            <g:link action="home" params="[u:user.username, mode:mode, movie:movie.movieName]"><div id="back">Back to All Movies</div></g:link>
        </div>
        <div id="right-banner">
            <div id="banner-name">${user.username}</div>
            <g:link action="login" style="color: #ffc51f;text-decoration: none;"><div id="banner-logout">(Log out)</div></g:link>
        </div>
        <div id="separator"></div>
    </div>

    <g:if test="${user.turker && user.movieRatings.size()>=15 && user.userTagRatings.size()>=45}">
        <div id="reward">
            <g:if test="${user.finishA && user.finishB}">
                Your reward code is <a style="color: #ff841d;">${user.rewardCode}</a>. Thank you!
            </g:if>
            <g:elseif test="${user.finishA && Integer.parseInt(user.rewardCode)%2 == 1}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${user.finishA && Integer.parseInt(user.rewardCode)%2 == 0}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${user.finishB && Integer.parseInt(user.rewardCode)%2 == 1}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:elseif test="${user.finishB && Integer.parseInt(user.rewardCode)%2 == 0}">
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:elseif>
            <g:else>
                <div class="rwdmsg">
                    <div class="rwdmsgtxt">Please provide feedback on</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:1]" method="post"><div class="rwdrec">Recommender X</div></g:link>
                    <div class="rwdmsgtxt">and</div>
                    <g:link action="makeRecommendations" params="[u:user.username, mode:3]" method="post"><div class="rwdrec">Recommender Y</div></g:link>
                    <div class="rwdmsgtxt">to get your reward code. Thank you!</div>
                </div>
            </g:else>
        </div>
    </g:if>

    <div id="info">
        <div id="movie-name">${movie.movieName}</div>
        <div id="basic-info">
            <img id="movie-img" style="height: 150px;width: 110px;" src=${movie.imgurl}>
            <div>
                <div id="avg-rating">${avg2}</div>
                <div id="avg-text">avg. rating from ${count} user(s)</div>
                <br>
                <div id="historyMovieRating">
                    <g:if test="${foundMovieRating.equals("true")}">
                        ${movieRating.movieRating}
                    </g:if>
                    <g:else>
                        <div>-.--</div>
                    </g:else>
                </div>
                <div id="you-text">rating from you</div>
            </div>
        </div>
        <div id="separator2"></div>
        <div id="rating-area">
            <div id="give">Give a new rating</div>
            <g:submitButton  id="rateMovie" name="Submit"/>
            <div id="radio">
                <input id="0" type="radio" name="group1" value=0 style="display: none">
                <input id="0.5" type="radio" name="group1" value=0.5><label for="0.5">0.5</label>
                <input id="1" type="radio" name="group1" value=1><label for="1">1</label>
                <input id="1.5" type="radio" name="group1" value=1.5><label for="1.5">1.5</label>
                <input id="2" type="radio" name="group1" value=2><label for="2">2</label>
                <input id="2.5" type="radio" name="group1" value=2.5><label for="2.5">2.5</label>
                <input id="3" type="radio" name="group1" value=3><label for="3">3</label>
                <input id="3.5" type="radio" name="group1" value=3.5><label for="3.5">3.5</label>
                <input id="4" type="radio" name="group1" value=4><label for="4">4</label>
                <input id="4.5" type="radio" name="group1" value=4.5><label for="4.5">4.5</label>
                <input id="5" type="radio" name="group1" value=5><label for="5">5</label>
            </div>
        </div>
    </div>
    <br>
    <div id="tag-area">
        <div id="myCanvasContainer">
            <canvas width="900" height="600" id="myCanvas">
                <p>Anything in here will be replaced on browsers that support the canvas element</p>
                <ul>
                    <g:each in="${tagging.keySet()}" var="tag">
                        <li><a id="${"cloud"+tag.id}" class="cloudtag" style="color: black" onclick="return tpu(this)" data-weight="${tagging.get(tag).tagCount+10}">${tag.tagName}</a></li>
                    </g:each>
                </ul>
            </canvas>
            <div id="tag-panel-super">
            <div id="tagPanel" style="display:none;">
                <div id="clickedTag">
                    clicked tag
                </div>
                <div id="newUpvote">
                    <img id="upvimg" src="${resource(dir:'images',file:'upvote.png')}">
                </div>
                <div id="newDownvote">
                    <img id="downvimg" src="${resource(dir:'images',file:'downvote.png')}">
                </div>
                <div id="response">
                    You haven't rated this tag.
                </div>
                <div id="panelTagId" style="display: none;"> -1</div>
            </div>
            </div>
        </div>
    </div>
</div>

<g:each in="${tagRatings}" var="tagRating">
        <div class="userTagRatings" style="color: blue; display:none;" id=${"userTagRating"+tagRating.trTag.id} class=${tagRating.tagRating}>${tagRating.trTag.tagName} : ${tagRating.tagRating}</div>
</g:each>

%{--<g:each in="${movie.tagCounts}" var="tagCount">
    <div id=${tagCount.tcTag.id+"container"}>
    <div class="tagarea" style="clear: right">${tagCount.tcTag.tagName} : ${tagCount.tagCount}
        <button  class="upvote" style="margin-left: 10px; margin-right: 5px;color:#ffffff;background-color:green;font-weight: bold;"
                 id=${tagCount.tcTag.id}>+</button>
        <button class="downvote" style="color:#ffffff;background-color:red;font-weight: bold;" id=${tagCount.tcTag.id}>-</button>
    </div>

        <g:each in="${tagRatings}" var="tagRating">
            <g:if test="${tagRating.trTag.tagName.equals(tagCount.tcTag.tagName)}">
                <div style="color: blue" id=${tagCount.tcTag.id+"tagrating"} class=${tagRating.tagRating}>${tagRating.trTag.tagName} : ${tagRating.tagRating}</div>
            </g:if>
        </g:each>
    </div>
</g:each>--}%
<r:layoutResources/>
</body>
</html>