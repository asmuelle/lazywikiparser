package org.herban.wiki.filter;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 13:12:50
 * To change this template use File | Settings | File Templates.
 */
public class WPTable extends WPBlock {
    String fParams;

    final ArrayList<WPRow> fRows;

    public ArrayList<WPRow> getFRows() {
		return fRows;
	}

	public WPTable(ArrayList rows) {
		super(null,0);
        fRows = rows;
        fParams = null;
    }

    /**
     * @return Returns the params.
     */
    public String getParams() {
        return fParams.replaceAll("<!--.*-->", "");
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
        return fRows.add((WPRow) o);
    }

    /**
     * @param index
     * @return
     */
    public Object get(int index) {
        return fRows.get(index);
    }

    /**
     * @return
     */
    public int size() {
        return fRows.size();
    }

	@Override
	public void addParam(WPParam param) {
		 this.fParams = param.getValue();

	}





}

