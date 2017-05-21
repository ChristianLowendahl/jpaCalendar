package se.prodentus.jpacalendar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="calendar_event")
class CalendarEvent implements Serializable {
    
    @Column(name="id") // Not necessary of course.
    @Id
    @GeneratedValue
    private long id;
    
    @Column(name="start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    
    @Column(name="end_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    
    @Column(name="description")
    private String description;
    
    @Column(name="event_category")
    private EventCategory eventCategory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
    
    public Date getStartDateTime() {
	return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
	this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
	return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
	this.endDateTime = endDateTime;
    }

    public EventCategory getEventCategory() {
	return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
	this.eventCategory = eventCategory;
    }
 
}