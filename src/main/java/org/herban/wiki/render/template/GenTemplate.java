package org.herban.wiki.render.template;

import org.herban.wiki.Handler;
import org.herban.wiki.filter.WPTemplate;

public interface GenTemplate {

	public abstract void replaceParameters(WPTemplate template, Handler handler) throws Exception;


}