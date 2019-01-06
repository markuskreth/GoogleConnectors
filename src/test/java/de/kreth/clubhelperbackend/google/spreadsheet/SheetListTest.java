package de.kreth.clubhelperbackend.google.spreadsheet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SheetListTest {

	@Mock
	private JumpHeightSheet sheet1;
	@Mock
	private JumpHeightSheet sheet2;
	@Mock
	private JumpHeightSheet sheet3;
	@Mock
	private JumpHeightSheet custom;
	private SheetList list;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		prpareSheets();
		list = new SheetList();
		list.add(sheet1);
		list.add(sheet2);
		list.add(sheet3);
	}

	private void prpareSheets() {
		when(sheet1.getTitle()).thenReturn("sheet1");
		when(sheet2.getTitle()).thenReturn("sheet2");
		when(sheet3.getTitle()).thenReturn("sheet3");
	}

	@Test
	public void testSize_Add_Remove() {
		assertEquals(3, list.size());
		list.remove(sheet1);
		list.remove(sheet2);
		list.remove(sheet3);
		assertEquals(0, list.size());		
	}

	@Test
	public void testContainsString() {
		assertTrue(list.contains("sheet1"));
		assertTrue(list.contains("sheet2"));
		assertTrue(list.contains("sheet3"));
		assertFalse(list.contains("false"));
	}

	@Test
	public void testGetString() {
		assertSame(sheet1, list.get("sheet1"));
		assertSame(sheet3, list.get("sheet3"));
	}

	@Test
	public void testContainsObject() {
		assertTrue(list.contains(sheet1));
	}

	@Test
	public void testClear() {
		assertFalse(list.isEmpty());
		list.clear();
		assertTrue(list.isEmpty());
	}

	@Test
	public void testSet() {
		list.set(1, custom);
		assertEquals(3, list.size());

		assertSame(sheet1, list.get(0));
		assertSame(custom, list.get(1));
		assertSame(sheet3, list.get(2));
		
	}

}
