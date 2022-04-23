function filterMonth() {

    var picked_month = document.getElementById("picked_month").value;
    picked_month = picked_month.split('-').join('/');

    fetch('https://security-jhu-app.herokuapp.com/incidents-monthly?' + "&picked_month=" + picked_month, {
        method: 'POST',
    }).then(res => window.location.reload = window.location.reload(true));

}