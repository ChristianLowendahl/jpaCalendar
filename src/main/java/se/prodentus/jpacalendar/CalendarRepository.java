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
    
    List<CalendarEvent> findAll() {
        Query query = entityManager.createQuery("SELECT c FROM CalendarEvent c");
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

    void addCalendarEvent(CalendarEvent calendarEvent) {
        entityManager.persist(calendarEvent);
    }

    List<CalendarEvent> findEventsInInterval(Date startSearchDateTime, Date endSearchDateTime) {
	String queryText = "SELECT c FROM CalendarEvent c"
		+ " WHERE (c.startDateTime BETWEEN :startSearchDateTime AND :endSearchDateTime)"
		+ " OR (c.endDateTime BETWEEN :startSearchDateTime AND :endSearchDateTime)";
	Query query = entityManager.createQuery(queryText);
	query.setParameter("startSearchDateTime", startSearchDateTime, TemporalType.TIMESTAMP);
	query.setParameter("endSearchDateTime", endSearchDateTime, TemporalType.TIMESTAMP);
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }
    
    List<CalendarEvent> findEventsByCategory(EventCategory searchEventCategory) {
	String queryText = "SELECT c FROM CalendarEvent c"
		+ " WHERE c.eventCategory = :searchEventCategory";
	Query query = entityManager.createQuery(queryText);
	query.setParameter("searchEventCategory", searchEventCategory);
	List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

    List<CalendarEvent> findEventsInIntervalByCategory(Date searchDateTime, Date searchDateTime0, EventCategory searchEventCategory) {
	return new ArrayList<>();
    }

}