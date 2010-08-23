package org.herban.wiki;

import java.util.TreeSet;
 
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.render.template.GenTemplate;


public interface TemplateProvider {

	void resolveTemplates(TreeSet<String> treeList, String name, Object target);

	GenTemplate getTemplate(WPTemplate template);


}
