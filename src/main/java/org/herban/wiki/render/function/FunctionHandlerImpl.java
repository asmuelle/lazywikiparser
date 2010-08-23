package org.herban.wiki.render.function;

import java.util.HashMap;


import org.herban.wiki.FunctionHandler;
import org.herban.wiki.Handler;
import org.herban.wiki.ParserFunction;
import org.herban.wiki.filter.WPFunction;

public class FunctionHandlerImpl implements FunctionHandler {
    private final Handler handler;
    private final HashMap<String, ParserFunction> functions;
	public FunctionHandlerImpl(Handler standardHandler) {
		handler=standardHandler;
		functions=new HashMap<String, ParserFunction>();
		//functions.put("map", new MapFunction(handler));
		functions.put("if", new IfFunction(handler));
		functions.put("ifexpr", new IfExprFunction(handler));
		functions.put("ifeq", new IfEqFunction(handler));
		functions.put("expr", new ExprFunction(handler));
		functions.put("ifexist", new IfExistFunction(handler));
		functions.put("rel2abs", new Rel2AbsFunction(handler));
		functions.put("switch", new SwitchFunction(handler));
		functions.put("titleparts", new TitlePartsFunction(handler));
		functions.put("time", new TimeFunction(handler));
		functions.put("timel", new TimelFunction(handler));


	}
	/* (non-Javadoc)
	 * @see org.herban.wiki.FunctionHandler#handleFunction(org.herban.wiki.filter.WPFunction)
	 */
	public void handleFunction(WPFunction function) {
		ParserFunction func=functions.get(function.getName());
		try {
		   func.execute(function);
		} catch (Exception e) {
			System.out.println(function.getName()+":"+function.getPosition()+"-"+function.getStop());
			e.printStackTrace();
			handler.handlePlainText(function.getName()+":"+e.getMessage());
		}

	}

	public void addFunction(String name, ParserFunction logic) {
		functions.put(name, logic);
	}

}
