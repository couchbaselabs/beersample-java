$(document).ready(function() {
    var latlng = new google.maps.LatLng(37.3863, -122.076);
    var opts = {
      zoom: 9,
      center: latlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    var map = new google.maps.Map(document.getElementById("map"), opts);

    $.get("/breweries/forMap", function(data) {
        console.log(data);
    });
});