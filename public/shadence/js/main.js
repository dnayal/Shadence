	function drawMap(mapElementId, latitude, longitude) {
	    var geocoder = new google.maps.Geocoder();
	    var latlng = new google.maps.LatLng(latitude, longitude);
	    var myOptions = {
	      zoom: 14,
	      center: latlng,
	      scrollwheel: false,
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
				isAnimated: false,
        		gutterWidth: 10,
				isResizable: true
			});
		});	
	}
	
	
	function reloadMasonry(containerId) {
		$(containerId).imagesLoaded(function() {
			$(containerId).masonry('reload');
		});	
	}
	
	
	function getPriceText(value) {
		switch(value) {
    		case 0: return "Free";
    		case 1: return "Inexpensive";
    		case 2: return "Moderate";
    		case 3: return "Expensive";
    		case 4:	return "High End";
    		default: return "High End";	
		}
	}


	function getDurationText(value) {
		switch(value) {
    		case 1: return "1 hour";
    		case 2: return "2 hours";
    		case 3: return "3 hours";
    		case 4:	return "Half day";
    		case 5: return "One day";	
    		default: return "One day";	
		}
	}

	
	function rescaleGalleria(galleriaId, galleriaContainer) {
	    Galleria.ready(function(options) {
	    	this.bind("rescale", function(event){
		    	$(galleriaContainer).height($(galleriaId).height());
	    	});
	    });
	}
	