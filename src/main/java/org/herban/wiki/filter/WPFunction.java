package org.herban.wiki.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: MUAN104 Date: 04.03.2009 Time: 13:12:50 To
 * change this template use File | Settings | File Templates.
 */
public class WPFunction extends WPBlock {

	private final String name;
	private ArrayList<WPParam> params;
	private HashMap<String, String> paramMap;

	public WPFunction(String name, StringBuffer source, int position) {
		super(source, position);
		if (name.contains(":")) {
			String[] param01=name.split(":");
			this.name = param01[0].substring(1);
			this.params = new ArrayList<WPParam>();
			paramMap = new HashMap<String, String>();

			if (param01.length==1) {
				addParam(new WPParam(source, 0, 0, "", 0));
			} else {

			  addParam(new WPParam(source, 0, 0, param01[1], 0));
			}
		} else {
			this.name = name.substring(1);
		}
	}



	public String getName() {

		return this.name;
	}

	public List<WPParam> getParameters() {
		return params;
	}

	public String getParamValue(int i) {
		WPParam param = params.get(i);
		if (param != null)
			return param.getValue();
		return null;
	}

	public void addParam(WPParam param) {
		paramMap.put(param.getName(), param.getValue());
		params.add(param);
	}

	public WPParam getParam(String key) {
		WPParam param = null;
		for (WPParam p : params) {
			if (p.getName().equals(key)) {
				param = p;
			}
		}
		return param;
	}

	public String getParamValue(String key) {

		return paramMap.get(key);
	}

	public WPParam getParam(int i) {

		return params.get(i);
	}



}