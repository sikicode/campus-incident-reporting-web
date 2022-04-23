function filterYear() {

    var picked_year = document.getElementById("yearpicker").value;

    fetch('https://security-jhu-app.herokuapp.com/incidents-annual?' + "&picked_year=" + picked_year, {
        method: 'POST',
    }).then(res => window.location.reload = window.location.reload(true));

}