	function drawMap(mapElementId, latitude, longitude) {
	    var geocoder = new google.maps.Geocoder();
	    var latlng = new google.maps.LatLng(latitude, longitude);
	    var myOptions = {
	      zoom: 14,
	      center: latlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	    var map = new google.maps.Map(document.getElementById(mapElementId), myOptions);
        map.setCenter(latlng);
        var marker = new google.maps.Marker({
            map: map, 
            position: latlng
        });
	}

	function initMasonry(containerId, itemId) {
		$(containerId).imagesLoaded(function() {
			$(containerId).masonry({
				itemSelector : itemId,
				isFitWidth: true,
        		gutterWidth: 10,
				isResizable: true
			});
		});	
	}