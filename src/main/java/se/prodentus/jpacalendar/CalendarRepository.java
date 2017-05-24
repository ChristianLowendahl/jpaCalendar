package se.prodentus.jpacalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class CalendarRepository {
    
    private EntityManager entityManager;

    CalendarRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public List<CalendarEvent> findAll() {
        Query query = entityManager.createQuery("SELECT c FROM CalendarEvent c");
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

    public void addCalendarEvent(CalendarEvent calendarEvent) {
        entityManager.persist(calendarEvent);
    }

    public List<CalendarEvent> findEventsInInterval(Date startDateTime, Date endDateTime) {
	String queryText = "SELECT c FROM CalendarEvent c"
		+ " WHERE c.startDateTime BETWEEN :startDateTime AND :endDateTime"
		+ " OR c.endDateTime BETWEEN :startDateTime AND :endDateTime";
	Query query = entityManager.createQuery(queryText);
	// Could define TemporalType for parameter if needed for the query.
	query.setParameter("startDateTime", startDateTime);
	query.setParameter("endDateTime", endDateTime);
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }
    
    public List<CalendarEvent> findEventsByCategory(EventCategory eventCategory) {
	String queryText = "SELECT c FROM CalendarEvent c"
		+ " WHERE c.eventCategory = :eventCategory";
	Query query = entityManager.createQuery(queryText);
	query.setParameter("eventCategory", eventCategory);
	List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

    public List<CalendarEvent> findEventsInIntervalByCategory(Date startDateTime, Date endDateTime, EventCategory eventCategory) {
	String queryText = "SELECT c FROM CalendarEvent c"
		+ " WHERE (c.startDateTime BETWEEN :startDateTime AND :endDateTime"
		+ " OR c.endDateTime BETWEEN :startDateTime AND :endDateTime)"
		+ " AND c.eventCategory = :eventCategory";
	Query query = entityManager.createQuery(queryText);
	query.setParameter("startDateTime", startDateTime);
	query.setParameter("endDateTime", endDateTime);
	query.setParameter("eventCategory", eventCategory);
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

}