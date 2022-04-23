package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "incidents")
public class Incident {
    @DatabaseField(generatedId = true, columnName = "id")
    private Integer id;
    @DatabaseField//(canBeNull = false)
    private Float longtitude;
    @DatabaseField//(canBeNull = false)
    private Float latitude;
    @DatabaseField//(canBeNull = false)
    private String description;
    @DatabaseField//(canBeNull = false)
    private int crimeCode;
    @DatabaseField//(canBeNull = false)
    private String dateAndTime;
    @DatabaseField//(canBeNull = false)
    private String location;
    @DatabaseField(foreign = true)
    private int userid;

    public Incident() {
    }

    public Incident(Float longtitude, Float latitude, String description, int crimeCode, String dateAndTime, String location, int userid) {
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.description = description;
        this.crimeCode = crimeCode;
        this.dateAndTime = dateAndTime;
        this.location = location;
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public Float getLongtitude() {
        return longtitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public String getDescription() {
        return description;
    }
    public int getCrimeCode() {
        return crimeCode;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getLocation() {
        return location;
    }

    public int getUserid() {
        return userid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLongtitude(Float longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setCrimeCode(int crimeCode) {
        this.crimeCode=crimeCode;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUser(int userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Incident incident = (Incident) o;
        return id == incident.id && longtitude == incident.longtitude && incident.latitude == latitude
                && description.equals(incident.description) && incident.crimeCode==crimeCode && incident.dateAndTime==dateAndTime
                && location.equals(incident.location) && Objects.equals(userid, incident.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, longtitude, latitude, description, crimeCode, dateAndTime, location, userid);
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", crimecode='"+crimeCode+'\''+
                ", dateAndTime=" + dateAndTime +
                ", location='" + location + '\'' +
                ", userid=" + userid +
                '}';
    }
}