package org.herban.wiki;

import java.util.Map;


import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.util.StringUtil;

/**
 * Created by IntelliJ IDEA. User: MUAN104 Date: 05.03.2009 Time: 10:40:32 To
 * change this template use File | Settings | File Templates.
 */
public class StandardHandler extends BaseHandler {

	protected StringBuffer appender;
	protected final StringBuffer fResultBuffer;


	private static int WIKI_COUNTER = 0;





	public StandardHandler(StringBuffer source) {
		super(null, source);
		fResultBuffer = new StringBuffer();
		appender = fResultBuffer;

	}
	public StandardHandler(StringBuffer source, StringBuffer dest) {
		super(null, source);
		fResultBuffer = dest;

		appender = fResultBuffer;

	}
	public Appendable getFResultBuffer() {
		return fResultBuffer;
	}



	public void setTemplateProvider(TemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	public boolean handleHTTPLink(String name) {
		if (name != null) {
			int index = name.indexOf("http://");

			if (index != -1) {

				String urlString = name.substring(index);

				int pipeIndex = urlString.indexOf(' ');
				String alias = "";
				if (pipeIndex != (-1)) {
					alias = urlString.substring(pipeIndex + 1);
					urlString = urlString.substring(0, pipeIndex);
				} else {
					alias = urlString;
				}

				createExternalLink(urlString, alias);

				return true;
			}

		}
		return false;
	}

	public void createExternalLink(String urlString, String alias) {

		int indx = urlString.lastIndexOf(".");
		if (indx > 0 && indx < (urlString.length() - 3)) {
			String ext = urlString.substring(indx + 1);
			if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png")
					|| ext.equalsIgnoreCase("jpg")
					|| ext.equalsIgnoreCase("jpeg")
					|| ext.equalsIgnoreCase("bmp")) {
				createExternalImage(urlString, alias);
				return;
			}
		}
		fResultBuffer.append("<span class='nobr'>");
		fResultBuffer.append("<a href='");
		StringUtil.escape(urlString, fResultBuffer);
		fResultBuffer.append("\" class=external title='");
		StringUtil.escape(urlString, fResultBuffer);
		fResultBuffer.append("'>");

		StringUtil.escape(alias, fResultBuffer);
		fResultBuffer.append("</a></span>");

	}

	public void createExternalImage(String urlString, String alias) {
		fResultBuffer.append("<span class=image>");
		fResultBuffer.append("<img src='");
		StringUtil.escape(urlString, fResultBuffer);
		fResultBuffer.append("' alt='");
		StringUtil.escape(alias, fResultBuffer);
		fResultBuffer.append("' /></span>");
	}




	public void appendLink(String name, String hash, String view) {
		//String link = fWikiBaseURL;
		String ns = "";
		if (name.indexOf(":") > 0) {
			ns = name.split(":")[0];
			name = name.split(":")[1];
		}
		//link = StringUtil.replace(link, "${title}", name);
		WIKI_COUNTER++;

		fResultBuffer.append("<a class='"  + ns + "' id='w"
				+ WIKI_COUNTER + "' href=\"#");

		fResultBuffer.append(name);
		fResultBuffer.append("'>");
		fResultBuffer.append(view);
		fResultBuffer.append("</a>");
	}

	public boolean handleNamespaceLinks(String name, String view,
			StringBuffer result) {

		return false;
	}

	public boolean handleInterwiki(Map<String, Object> map, String name,
			String view, int interwikiIndex, StringBuffer result)
			throws Exception {
		String interwiki;
		String link;
		String page;
		interwiki = name.substring(0, interwikiIndex);
		link = (String) map.get(interwiki);
		if (link != null) {
			page = name.substring(interwikiIndex + 1);
			fResultBuffer.append("<a href='");
			fResultBuffer.append(link);
			fResultBuffer.append(page);
			fResultBuffer.append("'>");
			fResultBuffer.append(view);
			fResultBuffer.append("</a>");
			return true;
		}
		return false;
	}

	public boolean handleInterwiki(String link, String name, String alias,
			int interwikiIndex, StringBuffer result) throws Exception {
		String page;
		page = name.substring(interwikiIndex + 1);
		fResultBuffer.append("<a href=#>");

		if (alias != null && alias.length() > 0) {
			fResultBuffer.append(alias);
		} else {
			fResultBuffer.append(page);
		}
		fResultBuffer.append("</a>");
		return true;
	}

	/**
	 * append a link to the StringBuffer the name and the view should already
	 * conatin escaped entities for &gt;,&lt;,...
	 */
	public static StringBuffer appendLink(String name, String view,
			String target, StringBuffer buffer) {
		return appendLinkWithRoot(null, name + "#" + target, view, buffer);
	}

	/**
	 * Create a link with a root and a special view. The name will not be url
	 * encoded!
	 */
	public static StringBuffer appendLinkWithRoot(String root, String name,
			String view, StringBuffer buffer) {
		buffer.append("<a href='");
		if (root != null) {
			buffer.append(root);
			buffer.append('/');
		}
		buffer.append(name);
		buffer.append("'>");
		buffer.append(view);
		buffer.append("</a>");
		return buffer;
	}



	@Override
	public void handleGallery(String galleryContent) {
		fResultBuffer.append("<div>");
		String[] fotos = galleryContent.split("\n");
		for (int i = 0; i < fotos.length; i++)
			handleWikipediaLink(fotos[i], "");
		fResultBuffer.append("</div>");
	}

	@Override
	public void handleMap(String mapContent) {
		fResultBuffer.append("<div>");

		handleWikipediaLink(mapContent.split("\n")[0], "");
		fResultBuffer.append("</div>");

	}


	public String toString() {
		return fResultBuffer.toString();
	}

	@Override
	public void handleCloseTag(String closeTag) {
		if (closeTag.equals("ref")) {

			appender = fResultBuffer;

		} else

			fResultBuffer.append(closeTag);

	}



	@Override
	public void newParagraph() {
		appender.append("<p>");
	}





	@Override
	public void handleTemplate(WPTemplate template)  {
       try {
		templateProvider.getTemplate(template).replaceParameters(template, this);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}






	@Override
	public void handleMathWiki(String mathContent) {
		try {
			appender.append("<DIV CLASS='math'>");
			copyMathLTGT(mathContent);
			appender.append("</DIV>");
		} catch (Exception e) {
		}

	}

	public void copyMathLTGT(String text) {
		final int len = text.length();
		int currentIndex = 0;
		int lastIndex = currentIndex;
		while (currentIndex < len) {
			switch (text.charAt(currentIndex++)) {
			case '<': // special html escape character
				if (lastIndex < (currentIndex - 1)) {
					appender
							.append(text.substring(lastIndex, currentIndex - 1));
					lastIndex = currentIndex;
				} else {
					lastIndex++;
				}
				appender.append("&lt;");
				break;
			case '>': // special html escape character
				if (lastIndex < (currentIndex - 1)) {
					appender
							.append(text.substring(lastIndex, currentIndex - 1));
					lastIndex = currentIndex;
				} else {
					lastIndex++;
				}
				appender.append("&gt;");
				break;
			}
		}
		if (lastIndex < (currentIndex)) {
			appender.append(text.substring(lastIndex, currentIndex));
		}
	}










	@Override
	public void handlePlainText(String text) {
		appender.append(text);
	}

}