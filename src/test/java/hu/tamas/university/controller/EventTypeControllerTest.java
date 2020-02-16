package hu.tamas.university.controller;

import com.google.common.collect.Lists;
import hu.tamas.university.dto.EventTypeDto;
import hu.tamas.university.entity.EventType;
import hu.tamas.university.repository.EventTypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventTypeControllerTest {

	@Mock
	private EventTypeRepository eventTypeRepository;
	@Mock
	private EventType eventType1;
	@Mock
	private EventType eventType2;

	private EventTypeController eventTypeController;

	@Before
	public void setUp() {
		eventTypeController = new EventTypeController(eventTypeRepository);
	}

	@Test
	public void testGetAllType() {
		when(eventTypeRepository.findAll()).thenReturn(Lists.newArrayList(eventType1, eventType2));
		when(eventType1.getType()).thenReturn("type1");
		when(eventType2.getType()).thenReturn("type2");

		final List<EventTypeDto> result = eventTypeController.getAllType();

		assertEquals(2, result.size());
		assertEquals("type1", result.get(0).getType());
		assertEquals("type2", result.get(1).getType());
	}
}
