<%--
  Created by IntelliJ IDEA.
  User: Jie Shan
  Date: 13-11-3
  Time: 下午4:59
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<link rel="stylesheet" type="text/css" href="../css/userLogIn.css" />
<link href='http://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Marko+One' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Yesteryear|Marcellus|Rock+Salt' rel='stylesheet' type='text/css'>
<head>
  <title>Kalu - A Tagging Based Movie Recommender</title>
</head>
<body style="background-image: url(../images/projector.jpg)">
<img class="logo" src='../images/logo4.png'>
%{--<div class="slogan">Find your new favorite movies.</div>--}%
<div class="form" style="padding-top: 40px;">
    <g:form action="doLogin" method="post">
        <div class="dialog">
        <div class="inforow">
                    <div class="info1">
                        <input id="userId" type='text' name='userId' placeholder="Your user Id" />
                        <span id="icon1">id</span>
                    </div>
                    <div class="info2">
                        <input id="username" type='text' name='username' placeholder="Your username" />
                        <span id="icon2" ></span>
                    </div>
        <div class="warning">
            <a style="visibility: hidden">a</a><g:if test="${params.again}"><a style="padding-left: 10px;color: orangered;">Wrong information. Please check and try again. </a></g:if>
        </div>
        </div>
        <div class="buttons">
            <span class="formButton">
                <input id="loginButton" type="submit" value="LOG IN AND KALU!"> </input>
            </span>
        </div>
        </div>
    </g:form>
</div>

</body>
</html>