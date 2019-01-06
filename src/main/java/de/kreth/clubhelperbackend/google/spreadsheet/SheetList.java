package de.kreth.clubhelperbackend.google.spreadsheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SheetList implements List<JumpHeightSheet>{

	private final List<JumpHeightSheet> values = new ArrayList<>();

	public int size() {
		return values.size();
	}

	public boolean contains(String title) {
		boolean retVal = false;
		for(JumpHeightSheet s: values) {
			if(s.getTitle().equals(title)) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}
	
	public JumpHeightSheet get(String title) {

		for(JumpHeightSheet s: values) {
			if(s.getTitle().equals(title)) {
				return s;
			}
		}
		return null;
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}

	public boolean contains(Object o) {
		return values.contains(o);
	}

	public Iterator<JumpHeightSheet> iterator() {
		return values.iterator();
	}

	public Object[] toArray() {
		return values.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return values.toArray(a);
	}

	public boolean add(JumpHeightSheet e) {
		return values.add(e);
	}

	public boolean remove(Object o) {
		return values.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return values.containsAll(c);
	}

	public boolean addAll(Collection<? extends JumpHeightSheet> c) {
		return values.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends JumpHeightSheet> c) {
		return values.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return values.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return values.retainAll(c);
	}


	public void clear() {
		values.clear();
	}

	public boolean equals(Object o) {
		return values.equals(o);
	}

	public int hashCode() {
		return values.hashCode();
	}

	public JumpHeightSheet get(int index) {
		return values.get(index);
	}

	public JumpHeightSheet set(int index, JumpHeightSheet element) {
		return values.set(index, element);
	}

	public void add(int index, JumpHeightSheet element) {
		values.add(index, element);
	}


	public JumpHeightSheet remove(int index) {
		return values.remove(index);
	}


	public int indexOf(Object o) {
		return values.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return values.lastIndexOf(o);
	}

	public ListIterator<JumpHeightSheet> listIterator() {
		return values.listIterator();
	}

	public ListIterator<JumpHeightSheet> listIterator(int index) {
		return values.listIterator(index);
	}

	public List<JumpHeightSheet> subList(int fromIndex, int toIndex) {
		return values.subList(fromIndex, toIndex);
	}

}
