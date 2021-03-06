package de.kreth.googleconnectors.calendar;

import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.kreth.googleconnectors.calendar.CalendarResource;
import de.kreth.googleconnectors.calendar.CalendarResource.CalendarKonfig;

public class LoadCalendarResource {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetResources() throws Exception {
		CalendarResource res = new CalendarResource();
		Collection<CalendarKonfig> configs = res.getConfigs();
		assertFalse(configs.isEmpty());
	}

}
