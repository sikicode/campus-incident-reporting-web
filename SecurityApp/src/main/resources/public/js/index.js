
var arrlocations = [];
var arrMarkers = [];
var geocoder;
var map;

function openForm(){
    document.getElementById("myForm").style.display = "block";

}
function closeForm(){
    document.getElementById("myForm").style.display = "none";
}

// Initialize google map interface
function initMap() {

    // The location of Johns Hopkins
    console.log("init map");
    loadData();
    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(-34.397, 150.644);
    console.log(findimage(3));

    const hopkins = { lat: 39.329903, lng: -76.620522 };

    // The map, centered at Johns Hopkins
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 12,
        center: hopkins,
    });
    heatmap = new google.maps.visualization.HeatmapLayer({
        data: arrlocations,
        map: map,
    });
    document
        .getElementById("toggle-heatmap")
        .addEventListener("click", toggleHeatmap);
    // The marker, positioned at Johns Hopkins
    const marker = new google.maps.Marker({
        position: hopkins,
        map: map,
    });
}

// create markers based on the address provided by user
function codeAddress() {
    if (!checkIfLoggedIn()){
        alert("please log in");
    }
    else{
        console.log("codeAddress1");
        var address = document.getElementById('ship-address').value;
        var description = document.getElementById('description').value;

        console.log("calling code address function");

        // Adding description box for marker
        const contentString =
            '<div id="content">' +
            '<div id="siteNotice">' +
            "</div>" +
            "<p2> Incident </p2>"+
            '<div id="bodyContent">' +
            "<p2> Location of the incident: </p2>" +"<p5>" + address+  "</p5>"  +"<p></p>"+
            "<p3> Description of the incident: </p3>"+ "<p6>" + description + "</p6>"
        "</div>" + "</div>";

        // Finding latitude and longitude based on location
        geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == 'OK') {
                const latitude = results[0].geometry.location.lat();
                const longitude = results[0].geometry.location.lng();
                const position = { lat:latitude, lng: longitude };
                const crimecode = predictCrimeCode();
                console.log(crimecode);
                const image = findimage(crimecode);

                const infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });

                const marker = new google.maps.Marker({
                    map: map,
                    position: position,
                    icon:image,
                    title:"incident",
                    animation: google.maps.Animation.DROP,

                });
                marker.addListener("click", () => {
                    infowindow.open({
                        anchor: marker,
                        map,
                        shouldFocus: false,
                    });
                });
                arrMarkers.push(marker);


                const description = document.getElementById("description").value;
                var date = document.getElementById("date").value;
                date = date.split('-').join('/');
                const address = document.getElementById("ship-address").value;

                //User already logged in
                var userEntity = {};
                userEntity = JSON.parse(sessionStorage.getItem('myUserEntity'));

                const email = userEntity.Email;
                console.log(email);
                // Sending data to backend
                fetch('https://security-jhu-app.herokuapp.com/mainpage?'+ "&date=" + date + "&description=" + description + "&address=" + address + "&latitude=" + latitude+ "&longitude=" + longitude+ "&crimecode=" + crimecode + "&email=" + email, {
                        method: 'POST',
                    }
                ).then();
                console.log("FETCHED");
                console.log(arrMarkers);
                const imagePath = "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m";
                new MarkerClusterer( map, arrMarkers,{imagePath: imagePath} );

            } else {
                alert('Geocode was not successful for the following reason: ' + status);
            }
        });

    }
    document.getElementById("myForm").style.display = "none";


}

function loadData() {


    /*fetch('https://security-jhu-app.herokuapp.com/incidents-today?' +"&picked_day=" + picked_day)
        .then(res => res.json())
        .then(console.log)
        .then(console.log("start processing data"))*/

    fetch('https://security-jhu-app.herokuapp.com/' ,{
            method: 'GET',
        }
    ).then(
        function(data){

            var incidents = [];
            var obj_list2 = document.getElementById("json");
            var content2 = obj_list2.innerHTML;
            console.log(content2);
            var json = JSON.parse(content2);
            console.log(json);

            for (var i = 0; i < json.length;i++) {
                console.log(json[i]);
                const obj = json[i];
                const incident = {
                    lat: obj.latitude,
                    lng: obj.longtitude,
                    address: obj.location,
                    description: obj.description,
                    crimecode: obj.crimeCode,
                };

                const contentString =
                    '<div id="content">' +
                    '<div id="siteNotice">' +
                    "</div>" +
                    // '<h1 id="firstHeading" class="firstHeading">Incident</h1>' +
                    "<p2> Incident </p2>" +
                    '<div id="bodyContent">' +
                    "<p2> Location of the incident: </p2>" + "<p5>" + obj.location + "</p5>" + "<p></p>" +
                    "<p3> Description of the incident: </p3>" + "<p6>" + obj.description + "</p6>"
                "</div>" + "</div>";
                console.log(obj.latitude);
                console.log(obj.longtitude);
                const position = {lat: obj.latitude, lng: obj.longtitude};
                var weight = assignWeight(obj.crimeCode);
                arrlocations.push({location:new google.maps.LatLng(obj.latitude,obj.longtitude),weight:weight});
                const image = findimage(obj.crimeCode);

                const infowindow = new google.maps.InfoWindow({
                    content: contentString,
                });
                console.log("adding markers");
                const marker = new google.maps.Marker({
                    map: map,
                    position: position,
                    icon: image,
                    title: "incident"
                });
                marker.addListener("click", () => {
                    infowindow.open({
                        anchor: marker,
                        map,
                        shouldFocus: false,
                    });
                });
                incidents.push(incident);
                arrMarkers.push(marker);
            }

            console.log("calling marker cluster");
            console.log("arrmarkers",arrMarkers);
            const imagePath = "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m";
            var cluster = new MarkerClusterer( map, arrMarkers,{imagePath: imagePath} );
            // console.log(cluster);

            // c
        }

    );
    console.log("FETCHED");
}


function assignWeight(crimecode){
    var weight;
    if (crimecode == 1){
        weight = 3;
    }
    else if (crimecode == 2){
        weight =
            1.7;
    }
    else if (crimecode ==3) {
        weight =
           1;
    }
    else if (crimecode ==4) {
        weight  = 0.6;
    }
    else if (crimecode ==5) {
        weight =
           1.3;
    }
    else if (crimecode ==6) {
        weight =
           0.8;
    }
    else if (crimecode ==7) {
        weight =
            0.6;
    }
    else if (crimecode ==8) {
        weight =
            2;
    }
    else if (crimecode ==9) {
        weight = 4;
    }
    return weight;
}


//setting color of the marker based on the crime code
function findimage(crimecode){
    var image;
    if (crimecode == 1){
        image =
            "http://maps.google.com/mapfiles/ms/icons/blue-dot.png";
    }
    else if (crimecode == 2){
        image =
            "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
    }
    else if (crimecode ==3) {
        image =
            "http://maps.google.com/mapfiles/ms/icons/pink-dot.png";
    }
    else if (crimecode ==4) {
        image =
            "http://maps.google.com/mapfiles/ms/icons/purple-dot.png";
    }
    else if (crimecode ==5) {
        image =
            "http://maps.google.com/mapfiles/ms/icons/orange-dot.png";
    }
    else if (crimecode ==6) {
        image =
            "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
    }
    else if (crimecode ==7) {
        image =
            "../img/grey-dot.png";
    }
    else if (crimecode ==8) {
        image =
            "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png";
    }
    else if (crimecode ==9) {
        image =
            "http://maps.google.com/mapfiles/ms/micons/ltblue-dot.png";
    }
    return image;
}
function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    var name = String(profile.getName());
    var email= String(profile.getEmail());

    // Sending user details to backend
    fetch('https://security-jhu-app.herokuapp.com/login?'+ "&name=" + name +"&email="+ email, {
        method: 'POST',
    }).then(

        // res=> window.location.href ="https://security-jhu-app.herokuapp.com/"
    );

    document.getElementById("signinB").style.display = "none",
    document.getElementById("signoutB").style.display = "";
    document.getElementById("dropdown01").style.display = "";
    document.getElementById("report").style.display = "";
    document.getElementById("loginmess").style.display = "none";


    console.log("FETCHED");

    var myUserEntity = {};
    myUserEntity.Id = profile.getId();
    console.log(myUserEntity.Id);
    myUserEntity.Name = profile.getName();
    myUserEntity.Email = profile.getEmail();
    //Store the entity object in sessionStorage where it will be accessible from all pages of your site.
    sessionStorage.setItem('myUserEntity',JSON.stringify(myUserEntity));


}
// <a href="#" onclick="signOut();">Sign out</a>
function signOut() {
    console.log("signing out");
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
    document.getElementById("signoutB").style.display = "none";
    document.getElementById("signinB").style.display = "";
    document.getElementById("dropdown01").style.display = "none";


    sessionStorage.clear();
    window.location.href ="https://security-jhu-app.herokuapp.com/"
    document.getElementById("loginmess").style.display = "";
    document.getElementById("report").style.display = "none";
}
//to chek if user is logged in
function onLoad() {
    gapi.load('auth2', function() {
        gapi.auth2.init();
    });
}





//We predict the crime code based on keywords within the description provided by the user
function predictCrimeCode(){
    let description = document.getElementById("description").value;

    if (description.includes("shot") || description.includes("shoot") || description.includes("fired") || description.includes ("gun") || description.includes("bullet"))
    {
        return 9;
    }
    else if (description.includes("murder") || description.includes("homicide") || description.includes("killed") || description.includes("died"))
    {
        return 1;
    }
    else if (description.includes("rape") || description.includes("sexual") || description.includes("inappropriate") ||description.includes("molest") || description.includes("grope"))
    {
        return 2;
    }
    else if ((description.includes("home") || description.includes("house") || description.includes("property") || description.includes("store") || description.includes("office") || description.includes("room")) &&  (description.includes("rob") || description.includes("break")))
    {
        return 5;
    }
    else if (description.includes("set fire") || description.includes("torch") || description.includes("burn") || description.includes("lit"))
    {
        return 8;
    }
    else if (description.includes("bag") || description.includes("wallet") || description.includes("card") ||description.includes("bag") ||description.includes("phone") ||description.includes("snatch") ||description.includes("key") ||description.includes("watch"))
    {
        return 6;
    }
    else if (description.includes("car") || description.includes("bike") || description.includes("van") || description.includes("truck") || description.includes("scooter"))
    {
        return 7;
    }
    else if (description.includes("rob") || description.includes("theft") || description.includes("demand"))
    {
        return 3;
    }
    else if (description.includes("assault") || description.includes("hit") || description.includes("beat") || description.includes("threw") || description.includes("punch") || description.includes("kick"))
    {
        return 4;
    }
    else
    {
        let crimecode = prompt("What was the crime related to? (1. Homicide 2.Rape 3.Robbery 4.Assault 5.Burglary 6.Larceny 7.Auto Theft 8.Arson 9.Shooting","Please enter a number!",10);
        return crimecode;

    }
}

//to check if the user is logged-in
function checkIfLoggedIn()
{
    if(sessionStorage.getItem('myUserEntity') == null){
        //Redirect to login page, no user entity available in sessionStorage
        window.location.href='https://security-jhu-app.herokuapp.com//login';
        return false;
    } else {
        //User already logged in
        var userEntity = {};
        userEntity = JSON.parse(sessionStorage.getItem('myUserEntity'));
        // window.location.href = "https://security-jhu-app.herokuapp.com/";
        return true;
    }
}
function checkIfLoggedIn2()
{
    if(sessionStorage.getItem('myUserEntity') == null){
        document.getElementById("report").style.display = "none";
        document.getElementById("loginmess").style.display = "";

        return false;
    } else {
        //User already logged in
        var userEntity = {};
        userEntity = JSON.parse(sessionStorage.getItem('myUserEntity'));
        document.getElementById("loginmess").style.display = "none";
        document.getElementById("report").style.display = "";


        // window.location.href = "https://security-jhu-app.herokuapp.com/";
        return true;
    }
}

function checkIfLoggedIn3()
{
    if(sessionStorage.getItem('myUserEntity') == null){
        document.getElementById("dropdown01").style.display = "none";
        document.getElementById("signoutB").style.display = "none";
        document.getElementById("signinB").style.display = "";


        return false;
    } else {
        //User already logged in
        var userEntity = {};
        userEntity = JSON.parse(sessionStorage.getItem('myUserEntity'));

        document.getElementById("signinB").style.display = "none";
        document.getElementById("signoutB").style.display = "";
        document.getElementById("dropdown01").style.display = "";


        // window.location.href = "https://security-jhu-app.herokuapp.com/";
        return true;
    }
}
function checkIfLoggedIn4()
{
    if(sessionStorage.getItem('myUserEntity') == null){
        document.getElementById("visbutton").style.display = "none";

        return false;
    } else {
        //User already logged in
        var userEntity = {};
        userEntity = JSON.parse(sessionStorage.getItem('myUserEntity'));

        document.getElementById("visbutton").style.display = "";
        return true;
    }
}





function toggleHeatmap() {
    heatmap.setMap(heatmap.getMap() ? null : map);
}

function changeGradient() {
    const gradient = [
        "rgba(0, 255, 255, 0)",
        "rgba(0, 255, 255, 1)",
        "rgba(0, 191, 255, 1)",
        "rgba(0, 127, 255, 1)",
        "rgba(0, 63, 255, 1)",
        "rgba(0, 0, 255, 1)",
        "rgba(0, 0, 223, 1)",
        "rgba(0, 0, 191, 1)",
        "rgba(0, 0, 159, 1)",
        "rgba(0, 0, 127, 1)",
        "rgba(63, 0, 91, 1)",
        "rgba(127, 0, 63, 1)",
        "rgba(191, 0, 31, 1)",
        "rgba(255, 0, 0, 1)",
    ];

    heatmap.set("gradient", heatmap.get("gradient") ? null : gradient);
}

// function changeRadius() {
//     heatmap.set("radius", heatmap.get("radius") ? null : 20);
// }

// function changeOpacity() {
//     heatmap.set("opacity", heatmap.get("opacity") ? null : 0.2);
// }
let autocomplete;
let address1Field;
let address2Field;
let postalField;
function initAutocomplete() {
    address1Field = document.querySelector("#ship-address");
    address2Field = document.querySelector("#address2");
    postalField = document.querySelector("#postcode");
    // Create the autocomplete object, restricting the search predictions to
    // addresses in the US and Canada.
    autocomplete = new google.maps.places.Autocomplete(address1Field, {
        componentRestrictions: { country: ["us", "ca"] },
        fields: ["address_components", "geometry"],
        types: ["address"],
    });
    address1Field.focus();
    // When the user selects an address from the drop-down, populate the
    // address fields in the form.
    autocomplete.addListener("place_changed", fillInAddress);
}

function fillInAddress() {
    // Get the place details from the autocomplete object.
    const place = autocomplete.getPlace();
    let address1 = "";
    let postcode = "";

    // Get each component of the address from the place details,
    // and then fill-in the corresponding field on the form.
    // place.address_components are google.maps.GeocoderAddressComponent objects
    // which are documented at http://goo.gle/3l5i5Mr
    for (const component of place.address_components) {
        const componentType = component.types[0];

        switch (componentType) {
            case "street_number": {
                address1 = `${component.long_name} ${address1}`;
                break;
            }

            case "route": {
                address1 += component.short_name;
                break;
            }

            case "postal_code": {
                postcode = `${component.long_name}${postcode}`;
                break;
            }

            case "postal_code_suffix": {
                postcode = `${postcode}-${component.long_name}`;
                break;
            }
            case "locality":
                document.querySelector("#locality").value = component.long_name;
                break;
            case "administrative_area_level_1": {
                document.querySelector("#state").value = component.short_name;
                break;
            }
            case "country":
                document.querySelector("#country").value = component.long_name;
                break;
        }
    }

    address1Field.value = address1;
    postalField.value = postcode;
    // After filling the form with address components from the Autocomplete
    // prediction, set cursor focus on the second address line to encourage
    // entry of subpremise information such as apartment, unit, or floor number.
    address2Field.focus();
}








// //to logout the user
// function logout()
// {
//     console.log("logging out");
//
//     //Don't forget to clear sessionStorage when user logs out
//     sessionStorage.clear();
// }