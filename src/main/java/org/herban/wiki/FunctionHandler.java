package org.herban.wiki;

import org.herban.wiki.filter.WPFunction;

public interface FunctionHandler {

	  void handleFunction(WPFunction function);

	 void addFunction(String string, ParserFunction flowFunction);

}