package org.herban.wiki.filter;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author andreas
 *
 */
public class WPTemplate extends WPBlock {

    String name;

    private final ArrayList<WPParam> params = new ArrayList<WPParam>();
    public final HashMap<String, WPParam> paramTable=new HashMap<String, WPParam>();

    public WPTemplate(String name, int start) {
    	super(null, start);
        this.name = name.replaceAll("\n","").replaceAll("<!--.+?-->","");
        this.name=this.name.replaceAll("Vorlage:","");

    }





    /**
     * @param o
     * @return
     */
    public boolean add(Object o) {
    	WPParam p=(WPParam) o;
    	paramTable.put(p.getName(), p);
        return params.add(p);
    }

    /**
     * @param index
     * @return
     */
    public Object get(int index) {
        return params.get(index);
    }

    /**
     * @return
     */
    public int size() {
        return params.size();
    }










	public String getName() {

		return this.name;
	}

	public ArrayList<WPParam> getParameters() {
		return params;
	}

	public String paramValue(String parameterString) {
		try {
		  return paramTable.get(parameterString).getValue();
		} catch (Exception e) {
			return null;
		}
	}





	@Override
	public void addParam(WPParam param) {
		params.add(param);
		paramTable.put(param.getName(), param);

	}


}