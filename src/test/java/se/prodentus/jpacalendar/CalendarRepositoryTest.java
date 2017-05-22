package se.prodentus.jpacalendar;

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
	CalendarEvent calendarEvent = new CalendarEvent();
	Date startDateTime = new Date();
	calendarEvent.setStartDateTime(startDateTime);
	Calendar calendar = Calendar.getInstance();
	calendar.add(Calendar.HOUR, 1);
	Date endDateTime = calendar.getTime();
	calendarEvent.setEndDateTime(endDateTime);
	String description = "This is a description of the calendar event.";
	calendarEvent.setDescription(description);
	EventCategory eventCategory = EventCategory.EDUCATION;
	calendarEvent.setEventCategory(eventCategory);
	
	//When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent);
	entityTransaction.commit();
	
	//Then
	CalendarEvent createdCalendarEvent = calendarRepository.findAll().get(0);
	assertEquals(startDateTime, createdCalendarEvent.getStartDateTime());
	assertEquals(endDateTime, createdCalendarEvent.getEndDateTime());
	assertEquals(description, createdCalendarEvent.getDescription());
	assertEquals(eventCategory, createdCalendarEvent.getEventCategory());
    }
    
    @Test
    public void findEventsInIntervalShouldReturnEmptyList() {
	Date startSearchDateTime = new Date();
	Date endSearchDateTime = new Date();
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsInInterval(startSearchDateTime, endSearchDateTime);
	assertEquals(0, calenderEvents.size());
    }
    
    @Test
    public void findEventsInIntervalShouldReturnOneEvent() {
	// Given
	Date startDateTime1 = makeDateTime(2017, 07, 01, 10, 0);
	Date endDateTime1 = makeDateTime(2017, 07, 01, 12, 0);
	CalendarEvent calendarEvent1 = new CalendarEvent(startDateTime1, endDateTime1);
	Date startDateTime2 = makeDateTime(2017, 07, 02, 13, 0);
	Date endDateTime2 = makeDateTime(2017, 07, 02, 15, 0);
	CalendarEvent calendarEvent2 = new CalendarEvent(startDateTime2, endDateTime2);
	
	// When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent1);
	calendarRepository.addCalendarEvent(calendarEvent2);
	entityTransaction.commit();
	
	// Then
	Date startSearchDateTime = makeDateTime(2017, 07, 01, 0, 0);
	Date endSearchDateTime = makeDateTime(2017, 07, 02, 0, 0);
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsInInterval(startSearchDateTime, endSearchDateTime);
	assertEquals(1, calenderEvents.size());
    }
    
    @Test
    public void findEventsInIntervalShouldReturnTwoEvents() {
	// Given
	Date startDateTime1 = makeDateTime(2017, 07, 01, 10, 0);
	Date endDateTime1 = makeDateTime(2017, 07, 01, 12, 0);
	CalendarEvent calendarEvent1 = new CalendarEvent(startDateTime1, endDateTime1);
	Date startDateTime2 = makeDateTime(2017, 07, 01, 13, 0);
	Date endDateTime2 = makeDateTime(2017, 07, 01, 15, 0);
	CalendarEvent calendarEvent2 = new CalendarEvent(startDateTime2, endDateTime2);
	
	// When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent1);
	calendarRepository.addCalendarEvent(calendarEvent2);
	entityTransaction.commit();
	
	// Then
	Date startSearchDateTime = makeDateTime(2017, 07, 01, 0, 0);
	Date endSearchDateTime = makeDateTime(2017, 07, 02, 0, 0);
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsInInterval(startSearchDateTime, endSearchDateTime);
	assertEquals(2, calenderEvents.size());
    }
    
    private Date makeDateTime(int year, int month, int day, int hourOfDay, int minute) {
	Calendar calendar = Calendar.getInstance();
	calendar.set(year, month, day, hourOfDay, minute);
	return calendar.getTime();
    }
    
    @Test
    public void findEventsByCategoryShouldReturnEmptyList() {
	EventCategory eventCategory = EventCategory.PRIVATE;
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsByCategory(eventCategory);
	assertEquals(0, calenderEvents.size());
    }
    
    @Test
    public void findEventsByCategoryShouldReturnOneElement() {
	// Given
	Date startDateTime1 = makeDateTime(2017, 07, 01, 10, 0);
	Date endDateTime1 = makeDateTime(2017, 07, 01, 12, 0);
	String description1 = "Text1";
	EventCategory eventCategory1 = EventCategory.PRIVATE;
	CalendarEvent calendarEvent1 = new CalendarEvent(startDateTime1, endDateTime1, description1, eventCategory1);
	Date startDateTime2 = makeDateTime(2017, 07, 02, 13, 0);
	Date endDateTime2 = makeDateTime(2017, 07, 02, 15, 0);
	String description2 = "Text2";
	EventCategory eventCategory2 = EventCategory.WORK;
	CalendarEvent calendarEvent2 = new CalendarEvent(startDateTime2, endDateTime2, description2, eventCategory2);
	
	// When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent1);
	calendarRepository.addCalendarEvent(calendarEvent2);
	entityTransaction.commit();
	
	// Then
	EventCategory searchEventCategory = EventCategory.PRIVATE;
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsByCategory(searchEventCategory);
	assertEquals(1, calenderEvents.size());
    }
    
     @Test
    public void findEventsByCategoryShouldReturnTwoElements() {
	// Given
	Date startDateTime1 = makeDateTime(2017, 07, 01, 10, 0);
	Date endDateTime1 = makeDateTime(2017, 07, 01, 12, 0);
	String description1 = "Text1";
	EventCategory eventCategory1 = EventCategory.EDUCATION;
	CalendarEvent calendarEvent1 = new CalendarEvent(startDateTime1, endDateTime1, description1, eventCategory1);
	Date startDateTime2 = makeDateTime(2017, 07, 02, 13, 0);
	Date endDateTime2 = makeDateTime(2017, 07, 02, 15, 0);
	String description2 = "Text2";
	EventCategory eventCategory2 = EventCategory.EDUCATION;
	CalendarEvent calendarEvent2 = new CalendarEvent(startDateTime2, endDateTime2, description2, eventCategory2);
	
	// When
	EntityTransaction entityTransaction = entityManager.getTransaction();
	entityTransaction.begin();
	calendarRepository.addCalendarEvent(calendarEvent1);
	calendarRepository.addCalendarEvent(calendarEvent2);
	entityTransaction.commit();
	
	// Then
	EventCategory searchEventCategory = EventCategory.EDUCATION;
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsByCategory(searchEventCategory);
	assertEquals(2, calenderEvents.size());
    }
    
    @Test
    public void findEventsInIntervalByCategoryShouldReturnEmptyList() {
	Date searchDateTime = new Date();
	EventCategory searchEventCategory = EventCategory.EDUCATION;
	List<CalendarEvent> calenderEvents = calendarRepository.findEventsInIntervalByCategory(searchDateTime, searchDateTime, searchEventCategory);
	assertEquals(0, calenderEvents.size());
    }
   
}