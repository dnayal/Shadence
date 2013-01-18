package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import utils.Util;

@Entity
public class Venue extends Model {

	@Id
	@Column(length=100)
	String venueId;

	@ManyToOne
	@JoinColumn(name="city_id")
	@Column(length=100)
	City city;
	
	@Required
	@Column(length=100)
	String name;

	@Column(length=100)
	String address;
	
	// Name of the city, in case the venue is outside the main city
	@Column(length=100)
	String venueCity;
	
	@Column(length=100)
	String postcode;

	@Column(length=100)
	String latitude;

	@Column(length=100)
	String longitude;
	
	Long createTimestamp;
	
	public static Finder<String, Venue> find = new Finder<String, Venue>(String.class, Venue.class);

	public Venue() {}
	
	public Venue(String venueId, String name, String address, String venueCity, String postcode, String latitude, 
														String longitude, Long createTimestamp) {
		this.venueId = venueId;
		this.name = name;
		this.address = address;
		this.venueCity = venueCity;
		this.postcode = postcode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createTimestamp = createTimestamp;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getName() {
		return Util.getString(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return Util.getString(address);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVenueCity() {
		return Util.getString(venueCity);
	}

	public void setVenueCity(String venueCity) {
		this.venueCity = venueCity;
	}

	public String getPostcode() {
		return Util.getString(postcode);
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	
}
