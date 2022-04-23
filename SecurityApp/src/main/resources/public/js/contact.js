window.onload = function()
{
    var date = new Date();
    var dayOfWeek = date.getDay();
    var hour = date.getHours()

    var open = 8;
    var close = 22;

    var status = document.getElementById("status");

    if(dayOfWeek == 0 || dayOfWeek == 6 || hour < open || hour > close)
    {
        status.innerHTML = "Closed";
        status.style.color = "red";
    }else{
        status.innerHTML = "Open";
        status.style.color = "green";
    }
}