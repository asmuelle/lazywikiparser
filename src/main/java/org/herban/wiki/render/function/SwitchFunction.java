package org.herban.wiki.render.function;

import java.util.HashMap;

import org.herban.wiki.Handler;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPParam;

public class SwitchFunction extends AbstractFunction {

	public SwitchFunction(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(WPFunction function) {
		HashMap<String,String> alternatives=new HashMap<String,String>();
		for (WPParam p:function.getParameters()) {
			alternatives.put(p.getName(), p.getValue());
		}
		String retval=alternatives.get(parse(function.getParamValue(0)));
		if (retval==null) retval=alternatives.get("default");
		if (retval==null) retval="";

		  handler.handlePlainText(retval);

	}

}
