package org.herban.wiki;

import java.util.HashSet;


import org.herban.wiki.filter.WPParam;
import org.herban.wiki.filter.WPTemplate;

public class TemplateSetBuilder extends BaseHandler{

	public TemplateSetBuilder( ) {
		super(null, null);
		// TODO Auto-generated constructor stub
	}






	public HashSet<String> getTemplateSet() {
		return templateSet;
	}

	private final HashSet<String> templateSet= new HashSet<String>();






	@Override
	public void handleTemplate(WPTemplate template) {
		templateSet.add(template.getName());
		for (WPParam param:template.getParameters()) {
			parseRecursive(param.getSource(), param.getPosition(), param.getStop());
		}

	}
	@Override
	protected void parseRecursive(StringBuffer source, int start, int stop) {

		try {
			WikipediaParser parser = new WikipediaParser(source, this);

			parser.parse(start, stop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
