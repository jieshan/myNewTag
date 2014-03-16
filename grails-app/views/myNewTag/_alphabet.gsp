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
    });
</script>
<div class="letters">
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