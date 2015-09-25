package org.beatific.flow.map;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MultiMapTests {

	private MultiMap<String, String> mm = new MultiMap<String, String>();
	private List<String>r1 = new ArrayList<String>();
	private List<String>r2 = new ArrayList<String>();
	
	@Before
	public void setUp() {
		mm.put("1", "1");
		mm.put("1", "2");
		mm.put("1", "3");
		mm.put("1", "4");
		mm.put("1", "5");
		mm.put("2", "6");
		mm.put("2", "7");
		mm.put("2", "8");
		mm.put("2", "9");
		mm.put("2", "0");
		
		r1.add("1");
		r1.add("2");
		r1.add("3");
		r1.add("4");
		r1.add("5");
		
		r2.add("6");
		r2.add("7");
		r2.add("8");
		r2.add("9");
		r2.add("0");
	}
	
	@Test
	public void testGet() {
		
		List<String> l1 = mm.get("1");
		assertThat(r1, is(l1));
		
		List<String> l2 = mm.get("2");
		assertThat(r2, is(l2));
	}
}
