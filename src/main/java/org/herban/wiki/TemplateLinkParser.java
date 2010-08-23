package org.herban.wiki;

import java.util.ArrayList;
import java.util.Collection;


import org.herban.wiki.filter.WPParam;
import org.herban.wiki.filter.WPTemplate;

public class TemplateLinkParser {

	private ArrayList<String> linkList;
	public Collection<String> getLinks() {
		 
		return linkList;
	}
	
	public TemplateLinkParser(String content) {
		linkList=new ArrayList<String>();
		StringBuffer buf=new StringBuffer(content);
		WikipediaParser parser=new WikipediaParser(buf, new BaseHandler("title", buf){

			@Override
			public void handleTemplate(WPTemplate template) {
				for(WPParam param:template.getParameters()) {
				   if(param.getValue()!=null /*&& param.getName().equals("Paradigma")*/) {
					   
					   linkList.addAll(new LinkParser(param.getValue()).getLinks());
				   }
				}
				
			}});
		parser.parse();
		
	}
}
