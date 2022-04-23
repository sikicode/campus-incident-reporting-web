function filterDate() {

    var picked_day = document.getElementById("picked_day").value;
    picked_day = picked_day.split('-').join('/');

    fetch('https://security-jhu-app.herokuapp.com/incidents-today?' + "&picked_day=" + picked_day, {
        method: 'POST',
    }).then(res => window.location.reload = window.location.reload(true));

}


