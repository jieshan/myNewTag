<%--
  Created by IntelliJ IDEA.
  User: Jie Shan
  Date: 13-11-5
  Time: 上午12:25
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="${resource(dir: 'css', file: 'recommendation.css')}" />
    <link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:layoutResources/>
  <title>Recommendations for ${user.userId}</title>
</head>
<script>
    $(document).ready(function(){
        //alert("ready");
        $('#C').on('click',function(){
            document.getElementById('surveyrec').innerHTML = "Recommender Y";
            var userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/makeRecommendationsC?userid='+userId,
                success: function(data){
                    var results = $('#recs');
                    results.html(data);
                    var C = $('#C');
                    C.css("background-color", "#ff841d");
                    C.css("color", "#ffffff");
                    C.hover(function(){
                        C.css("background-color", "#ff841d");
                        C.css("color", "#ffffff");
                    },function(){
                        C.css("background-color", "#ff841d");
                        C.css("color", "#ffffff");
                    });
                    var A = $('#A');
                    A.css("background-color", "#ffffff");
                    A.css("color", "#ff841d");
                    A.hover(function(){
                        A.css("transition", "all 0.18s ease-out");
                        A.css("background-color","#ffc51f");
                        A.css("color","#ffffff");
                    },function(){
                        A.css("transition", "all 0.18s ease-out");
                        A.css("background-color", "#ffffff");
                        A.css("color", "#ff841d");
                    });
                    var B = $('#B');
                    B.css("background-color", "#ffffff");
                    B.css("color", "#ff841d");
                    B.hover(function(){
                        B.css("transition", "all 0.18s ease-out");
                        B.css("background-color","#ffc51f");
                        B.css("color","#ffffff");
                    },function(){
                        B.css("transition", "all 0.18s ease-out");
                        B.css("background-color", "#ffffff");
                        B.css("color", "#ff841d");
                    });
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        $('#A').on('click',function(){
            document.getElementById('surveyrec').innerHTML = "Recommender X";
            var userId = $('#userId').val();
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/makeRecommendationsA?userid='+userId,
                success: function(data){
                    var results = $('#recs');
                    results.html(data);
                    var A = $('#A');
                    A.css("background-color", "#ff841d");
                    A.css("color", "#ffffff");
                    A.hover(function(){
                        A.css("background-color", "#ff841d");
                        A.css("color", "#ffffff");
                    },function(){
                        A.css("background-color", "#ff841d");
                        A.css("color", "#ffffff");
                    });
                    var C = $('#C');
                    C.css("background-color", "#ffffff");
                    C.css("color", "#ff841d");
                    C.hover(function(){
                        C.css("transition", "all 0.18s ease-out");
                        C.css("background-color","#ffc51f");
                        C.css("color","#ffffff");
                    },function(){
                        C.css("transition", "all 0.18s ease-out");
                        C.css("background-color", "#ffffff");
                        C.css("color", "#ff841d");
                    });
                    var B = $('#B');
                    B.css("background-color", "#ffffff");
                    B.css("color", "#ff841d");
                    B.hover(function(){
                        B.css("transition", "all 0.18s ease-out");
                        B.css("background-color","#ffc51f");
                        B.css("color","#ffffff");
                    },function(){
                        B.css("transition", "all 0.18s ease-out");
                        B.css("background-color", "#ffffff");
                        B.css("color", "#ff841d");
                    });
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        $('#submitSurvey').on('click',function(){
            var rec = document.getElementById('surveyrec').innerHTML;
            rec = rec.substring(rec.length-1)
            //alert(rec)
            var surveyAns = new Array();
            var userId = $('#userId').val();
            for(var j=0; j<5; j++){
                var radios = document.getElementsByName('group'+ (j+1));
                var missbool = true;
                for (var i = 0, length = radios.length; i < length; i++) {
                    if (radios[i].checked) {
                        //alert(radios[i].value);
                        surveyAns[j] = radios[i].value;
                        missbool = false;
                        break;
                    }
                }
                if(missbool){
                    alert('Please complete Question '+ (j+1)+'.')
                    return;
                }
            }
            var cmts = $('#comments').val();
            //alert(cmts)
            surveyAns[5] = cmts
            $.ajax({
                url: 'http://localhost:8080/myNewTag/myNewTag/collectFeedback?userid='+userId+'&rec='+rec
                        +'&ans1='+surveyAns[0]+'&ans2='+surveyAns[1]+'&ans3='+surveyAns[2]+'&ans4='+surveyAns[3]+'&ans5='+surveyAns[4]+'&ans6='+surveyAns[5],
                success: function(data){
                    alert("Thank you for your feedback!")
                    document.location.reload(true)
                },
                error: function() {
                    alert("fail");
                }
            });
        });
        var sortMode = $('#mode').val();
        //alert(sortMode)
        if(sortMode == 2){
            $("#B")[0].click();
        }else if(sortMode == 3){
            $("#C")[0].click();
        }
    });
</script>
<body>
<g:hiddenField value = "${user.userId}" name = "userId" id = "userId"/>
<g:hiddenField value = "${mode}" name = "mode" id = "mode"/>
<div id="menu-primary">
    <div class="menu">
        <img class="logo" src="${resource(dir:'images',file:'logo_small.png')}">
        <div id="menu-primary-items">
            <g:link action="home" params="[u:user.username]" method="post" style="color: #ff841d;"><div id="item1">Home</div></g:link>
            <g:if test="${(user.movieRatings.size()>=15 && user.userTagRatings.size()>=45) || user.turker==0}">
                <g:link action="makeRecommendations" params="[u:user.username, mode:1]" method="post" style="color: #ff841d;"><div id="item2">Recommendations</div></g:link>
            </g:if>
            <g:else>
                <g:link action="makeRecommendations" params="[u:user.username, mode:1]" method="post" style="color: #ff841d;display: none;"><div id="item2">Recommendations</div></g:link>
            </g:else>
            <g:link action="ratedMovies" params="[u:user.username]" method="post" style="color: #ff841d;"><div id="item3">Rated Movies</div></g:link>
        </div>
    </div>
</div>

<div id="panel">
    <div id="banner">
        <div id="left-banner">
            <div id="A">Recommender X</div>
            <div id="C">Recommender Y</div>
        </div>
        <div id="right-banner">
            <div id="banner-name">${user.username}</div>
            <g:link action="login" style="color: #ffc51f;text-decoration: none;"><div id="banner-logout">(Log out)</div></g:link>
        </div>
        <div id="separator"></div>
    </div>
    <br>
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
    <div id="recs">
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
                        <g:each in="${tcs.get(rec.key.movieId).sort{it.tagCount*-1}.size()< 6 ? tcs.get(rec.key.movieId).sort{it.tagCount*-1} : tcs.get(rec.key.movieId).sort{it.tagCount*-1}.subList(0,6)}" var="tc">
                            <div class="tag-name">#${tc.tcTagname}</div><div class="slash">/</div>
                        </g:each>
                    </div>
                </div>
            </g:link>
            </div>
        </g:each>
    </div>
    <div id="separator2"></div>
    <div id="survey">
        <div id="surveyhead">
            <div id="surveytitle">Love to hear your feed back on</div>
            <div id="surveyrec">Recommender X</div>
            <div id="exc">!</div>
        </div>
        <div id="surveyintro">Please evaluate the following statements on a scale of <a style="color: #ff841d;">1 (Strongly Disagree)</a> to <a style="color: #ff841d;">5 (Strongly Agree)</a></div>
    <div id="surveypanel">
        <div class="Qs" id="Q1">
            <div id="QQ1"><a style="color: #ff841d;">1.</a> The movies recommended to me matched my interests.</div>
            <div class="radios" id="radio1">
                <input class="radio" id="Q11" type="radio" name="group1" value=1><label for="Q11">1</label>
                <input class="radio" id="Q12" type="radio" name="group1" value=2><label for="Q12">2</label>
                <input class="radio" id="Q13" type="radio" name="group1" value=3><label for="Q13">3</label>
                <input class="radio" id="Q14" type="radio" name="group1" value=4><label for="Q14">4</label>
                <input class="radio" id="Q15" type="radio" name="group1" value=5><label for="Q15">5</label>
            </div>
        </div>
        <div class="Qs" id="Q2">
            <div id="QQ2"><a style="color: #ff841d;">2.</a> The recommendations I received better fit my interests than what I may receive from a friend/movie critic/blogger. </div>
            <div class="radios" id="radio2">
                <input class="radio" id="Q21" type="radio" name="group2" value=1><label for="Q21">1</label>
                <input class="radio" id="Q22" type="radio" name="group2" value=2><label for="Q22">2</label>
                <input class="radio" id="Q23" type="radio" name="group2" value=3><label for="Q23">3</label>
                <input class="radio" id="Q24" type="radio" name="group2" value=4><label for="Q24">4</label>
                <input class="radio" id="Q25" type="radio" name="group2" value=5><label for="Q25">5</label>
            </div>
        </div>
        <div class="Qs" id="Q3">
            <div id="QQ3"><a style="color: #ff841d;">3.</a> The movies recommended to me took my personal context requirements into consideration. </div>
            <div class="radios" id="radio3">
                <input class="radio" id="Q31" type="radio" name="group3" value=1><label for="Q31">1</label>
                <input class="radio" id="Q32" type="radio" name="group3" value=2><label for="Q32">2</label>
                <input class="radio" id="Q33" type="radio" name="group3" value=3><label for="Q33">3</label>
                <input class="radio" id="Q34" type="radio" name="group3" value=4><label for="Q34">4</label>
                <input class="radio" id="Q35" type="radio" name="group3" value=5><label for="Q35">5</label>
            </div>
        </div>
        <div class="Qs" id="Q4">
            <div id="QQ4"><a style="color: #ff841d;">4.</a> If a recommender such as this exists and is updated in time, I will use it to find movies to watch. </div>
            <div class="radios" id="radio4">
                <input class="radio" id="Q41" type="radio" name="group4" value=1><label for="Q41">1</label>
                <input class="radio" id="Q42" type="radio" name="group4" value=2><label for="Q42">2</label>
                <input class="radio" id="Q43" type="radio" name="group4" value=3><label for="Q43">3</label>
                <input class="radio" id="Q44" type="radio" name="group4" value=4><label for="Q44">4</label>
                <input class="radio" id="Q45" type="radio" name="group4" value=5><label for="Q45">5</label>
            </div>
        </div>
        <div class="Qs" id="Q5">
            <div id="QQ5"><a style="color: #ff841d;">5.</a> Overall, I am satisfied with this recommender. </div>
            <div class="radios" id="radio5">
                <input class="radio" id="Q51" type="radio" name="group5" value=1><label for="Q51">1</label>
                <input class="radio" id="Q52" type="radio" name="group5" value=2><label for="Q52">2</label>
                <input class="radio" id="Q53" type="radio" name="group5" value=3><label for="Q53">3</label>
                <input class="radio" id="Q54" type="radio" name="group5" value=4><label for="Q54">4</label>
                <input class="radio" id="Q55" type="radio" name="group5" value=5><label for="Q55">5</label>
            </div>
        </div>
        <div class="Qs" id="Q6">
            <div id="QQ6"><a style="color: #ff841d;">6.</a> Other comments? </div>
            <textarea id="comments" name="comments" cols="50" rows="5" style="margin-top: 10px;"></textarea>
        </div>
        <g:submitButton  id="submitSurvey" name="Submit"/>
    </div>
    </div>
</div>

%{--Recommendations for ${user.userId}

<div style="height: 40px"></div>

<g:each in="${sortedRecList}" var="rec">
    <div  style="clear: right"><div>${rec.key.movieName} : ${rec.value}</div>
    </div>
</g:each>--}%
</body>
</html>