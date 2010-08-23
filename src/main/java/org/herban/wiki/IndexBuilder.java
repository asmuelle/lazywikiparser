package org.herban.wiki;

import java.util.ArrayList;
import java.util.List;


import org.herban.wiki.filter.ImageFormat;
import org.herban.wiki.filter.WPTemplate;

public class IndexBuilder extends BaseHandler{
	private List<String> categories;
	private List<WPTemplate> templateInstances;
	private List<String> wikilinks;


	public IndexBuilder() {
		super(null,null);
		categories=new ArrayList<String>();
		templateInstances= new ArrayList<WPTemplate>();
		wikilinks= new ArrayList<String>();
	}

	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<WPTemplate> getTemplateInstances() {
		return templateInstances;
	}
	public void setTemplateInstances(List<WPTemplate> templateInstances) {
		this.templateInstances = templateInstances;
	}
	public List<String> getWikilinks() {
		return wikilinks;
	}
	public void setWikilinks(List<String> wikilinks) {
		this.wikilinks = wikilinks;
	}



	@Override
	public void handleTemplate(WPTemplate template) {
		templateInstances.add(template);
	}

	@Override
	protected void addImage(String imageName, String imageSrc,
			ImageFormat imgFormat) {

	}





	@Override
	public void handleReference(String name, String group, int start, int stop,
			StringBuffer source) {
		System.out.println("ref:"+start+":"+stop);
	}




}
