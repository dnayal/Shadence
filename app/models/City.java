package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class City extends Model {

	@Id
	@Required
	@Column(length=100)
	String cityId;

	@Required
	@Column(length=100)
	String name;
	
	@Column(length=100)
	String stateOrCounty;
	
	@Required
	@Column(length=100)
	String country;
	
	Long createTimestamp;
	
	@OneToMany(mappedBy="entityCity")
	@OrderBy("photoOrder asc, createTimestamp desc")
	List<EntityPhoto> flagPhotos;
	
	public static Finder<String, City> find = new Finder<String, City>(String.class, City.class);

	public City() {}
	
	public City(String cityId, String name, String stateOrCounty, String country, Long createTimestamp) {
		this.cityId = cityId;
		this.name = name;
		this.stateOrCounty = stateOrCounty;
		this.country = country;
		this.createTimestamp = createTimestamp;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStateOrCounty() {
		return stateOrCounty;
	}

	public void setStateOrCounty(String stateOrCounty) {
		this.stateOrCounty = stateOrCounty;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Long createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public List<EntityPhoto> getFlagPhotos() {
		return flagPhotos;
	}

	public void setFlagPhotos(List<EntityPhoto> flagPhotos) {
		this.flagPhotos = flagPhotos;
	}

}
