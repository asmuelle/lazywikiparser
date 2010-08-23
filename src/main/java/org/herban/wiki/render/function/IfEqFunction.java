package org.herban.wiki.render.function;

import org.herban.wiki.Handler;
import org.herban.wiki.filter.WPFunction;

public class IfEqFunction extends AbstractFunction {


	public IfEqFunction(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(WPFunction function) {

		StringBuffer condString=parse(function.getParamValue(0));
		StringBuffer cond2String= parse(function.getParamValue(1));
		if (condString.equals(cond2String)) {
		  handler.handlePlainText(parse(function.getParamValue(2)).toString());
		} else {
		  handler.handlePlainText(parse(function.getParamValue(3)).toString());
		}
	}

}
