package org.herban.wiki.filter;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA. User: MUAN104 Date: 04.03.2009 Time: 13:12:09 To
 * change this template use File | Settings | File Templates.
 */
public class WPList {

	final ArrayList<WPListElement> fListElements;

	public ArrayList<WPListElement> getFListElements() {
		return fListElements;
	}

	final String tag;

	boolean isOrdered() {
		return tag.equals("ol");
	}

	public WPList(String tag) {
		fListElements = new ArrayList<WPListElement>();
		this.tag = tag;
	}

	/**
	 * @param o
	 * @return
	 */
	public boolean add(WPListElement o) {
		return fListElements.add(o);
	}

	/**
	 * @param index
	 * @return
	 */
	public Object get(int index) {
		return fListElements.get(index);
	}

	/**
	 * @return
	 */
	public int size() {
		return fListElements.size();
	}


	public String getTag() {

		return tag;
	}

}