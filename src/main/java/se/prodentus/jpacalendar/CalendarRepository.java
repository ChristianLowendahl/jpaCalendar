package se.prodentus.jpacalendar;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CalendarRepository {
    
    private EntityManager entityManager;

    CalendarRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    List<CalendarEvent> findAll() {
        Query query = entityManager.createQuery("SELECT ce FROM CalendarEvent ce");
        List<CalendarEvent> calenderEvents = query.getResultList();
        return calenderEvents;
    }

    void addCalendarEvent(CalendarEvent calendarEvent) {
        entityManager.persist(calendarEvent);
    }

}