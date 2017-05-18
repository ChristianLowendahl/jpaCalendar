package se.prodentus.jpacalendar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import org.junit.After;

public class CalendarRepositoryTest {

    private CalendarRepository calendarRepository;
    private EntityManager entityManager;

    @Before
    public void before() throws Exception {
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
	entityManager = entityManagerFactory.createEntityManager();
	clearCalenderEvents();
	calendarRepository = new CalendarRepository(entityManager);
    }

    private void clearCalenderEvents() {
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	Query query = entityManager.createQuery("SELECT ce FROM CalendarEvent ce");
	List<CalendarEvent> calenderEvents = query.getResultList();
	for(CalendarEvent calenderEvent : calenderEvents) {
	    entityManager.remove(calenderEvent);
	}
	entityTransaction.commit();
    }

    @Test
    public void findAllShouldReturnEmptyList() {
	List<CalendarEvent> calenderEvents = calendarRepository.findAll();
	assertEquals(0, calenderEvents.size());
    }

    @Test
    public void findAllShouldReturnOneEvent() {
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(new CalendarEvent());
	entityTransaction.commit();
	List<CalendarEvent> calenderEvents = calendarRepository.findAll();
	assertEquals(1, calenderEvents.size());
    }
    
    @Test
    public void findAllShouldReturnTwoEvents() {
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(new CalendarEvent());
	calendarRepository.addCalendarEvent(new CalendarEvent());
	entityTransaction.commit();
	List<CalendarEvent> calenderEvents = calendarRepository.findAll();
	assertEquals(2, calenderEvents.size());
    }
    
    @Test
    public void addEventShouldPersistProperties() {
	//Given
	Date startDateTime = new Date();
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.HOUR, 1);
	Date endDateTime = calendar.getTime();
	String description = "descr";
	CalendarEvent calendarEvent = new CalendarEvent();
	calendarEvent.setStartDateTime(startDateTime);
	calendarEvent.setEndDateTime(endDateTime);
	calendarEvent.setDescription(description);
	
	//When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent);
	entityTransaction.commit();
	
	//Then
	CalendarEvent createdCalendarEvent = calendarRepository.findAll().get(0);
	assertEquals(startDateTime, createdCalendarEvent.getStartDateTime());
	assertEquals(endDateTime, createdCalendarEvent.getEndDateTime());
	assertEquals(description, calendarEvent.getDescription());
    }
   
}