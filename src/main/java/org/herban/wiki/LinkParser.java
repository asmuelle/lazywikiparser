package org.herban.wiki;

import java.util.ArrayList;
import java.util.Collection;


import org.herban.wiki.filter.WPParam;
import org.herban.wiki.filter.WPTemplate;

public class LinkParser  {

	public LinkParser(String content) {
		linkList=new ArrayList<String>();
		StringBuffer buf=new StringBuffer(content);
		WikipediaParser parser=new WikipediaParser(buf, new BaseHandler("title", buf){

			@Override
			public void handleWikiLink(ArrayList<String> params) {
				linkList.add(params.get(0).replaceAll(" ", "_") );
			}

		});
		parser.parse();
	}
		
	public Collection<String> getLinks() {
		 
		return linkList;
	}
	
	private ArrayList<String> linkList;
	 
}
