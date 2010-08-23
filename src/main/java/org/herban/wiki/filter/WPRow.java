package org.herban.wiki.filter;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 13:12:37
 * To change this template use File | Settings | File Templates.
 */
public class WPRow {
    ArrayList<WPCell> fCells;

    public ArrayList<WPCell> getFCells() {
		return fCells;
	}

	public void setFCells(ArrayList<WPCell> cells) {
		fCells = cells;
	}

	String fParams;

    String fType;

    public WPRow(ArrayList cells) {
        fCells = cells;
        fType = "TD";
    }

    /**
     * @return Returns the params.
     */
    public String getParams() {
        return fParams;
    }

    /**
     * @param params The params to set.
     */
    public void setParams(String params) {
        this.fParams = params;
    }

    /**
     * @param o
     * @return
     */
    public boolean add(Object o) {
        return fCells.add((WPCell) o);
    }

    /**
     * @param index
     * @return
     */
    public Object get(int index) {
        return fCells.get(index);
    }

    /**
     * @return
     */
    public int size() {
        return fCells.size();
    }


    

    /**
     * @return Returns the type.
     */
    public String getType() {
        return fType;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        fType = type;
    }
}

