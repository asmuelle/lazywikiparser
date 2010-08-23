package org.herban.wiki.render.function;

import org.herban.wiki.Handler;
import org.herban.wiki.ParserFunction;
import org.herban.wiki.StandardHandler;
import org.herban.wiki.TemplateParser;

public abstract class AbstractFunction implements ParserFunction{
	 protected final Handler handler;
		public AbstractFunction(Handler handler) {
			this.handler=handler;
		}



    public static   StringBuffer  parse(String value )
     {
    	StringBuffer b=new StringBuffer();
    	Handler h=(Handler) new StandardHandler(b);
    	try {
			new TemplateParser(new StringBuffer(value),h,0).parse();
		} catch (Exception e) {

			e.printStackTrace();
		}
    	return b;
    }
}
