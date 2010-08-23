package org.herban.wiki.render.function;

import java.util.HashMap;

import org.herban.wiki.render.template.GenTemplate;
import org.herban.wiki.render.template.TemplateInstance;

public class TemplateCache {
    /*
     * we distinguish between Inline and extra View templates
     * inline templates are rendered syncronous, extra view templates asyncronous
     * inline templates may be placed in html body or later resource bundles
     * extra view templates will be loaded on demand
     */
	private final HashMap<String, TemplateInstance> cache=new HashMap<String, TemplateInstance>();
	public   GenTemplate get(String name) {
		 
		return cache.get(name);
	}
	
	public void put(String name, TemplateInstance t){
		cache.put(name, t);
	}

}
