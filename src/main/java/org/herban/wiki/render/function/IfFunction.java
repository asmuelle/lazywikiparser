package org.herban.wiki.render.function;

import org.herban.wiki.Handler;
import org.herban.wiki.filter.WPFunction;

public class IfFunction extends AbstractFunction {


	public IfFunction(Handler handler) {
		super(handler);

	}

	@Override
	public void execute(WPFunction function) {

		StringBuffer condString=parse(function.getParamValue(0));

		if (condString.length()>0) {
		  handler.handlePlainText(parse(function.getParamValue(1)).toString());
		} else {
		  handler.handlePlainText(parse(function.getParamValue(2)).toString());
		}
	}

}
