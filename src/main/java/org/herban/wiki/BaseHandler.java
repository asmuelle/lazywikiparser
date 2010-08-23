package org.herban.wiki;

import java.util.ArrayList;

import org.herban.wiki.filter.ImageFormat;
import org.herban.wiki.filter.WPCell;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPList;
import org.herban.wiki.filter.WPListElement;
import org.herban.wiki.filter.WPRow;
import org.herban.wiki.filter.WPTable;
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.filter.tags.OpenTagToken;
import org.herban.wiki.render.function.FunctionHandlerImpl;

/**
 * @author Andreas Mueller
 * @version $ID:$
 */
public class BaseHandler implements Handler {

	protected String fImageLocale = "Bild";
	protected final String fTitle;
	protected final StringBuffer fSource;
	protected final FunctionHandler functionHandler;
	protected TemplateProvider templateProvider;

	public BaseHandler(String title, StringBuffer source) {
		fTitle=title;
		fSource=source;
		this.functionHandler = new FunctionHandlerImpl(this);

	}

	public TemplateProvider getTemplateProvider() {
		return templateProvider;
	}

	public void setTemplateProvider(TemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	@Override
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

	private void createExternalLink(String link, String link2) {
		// TODO Auto-generated method stub

	}

	protected void addImage(String imageName, String imageSrc,
			ImageFormat imgFormat) {

	}

	@Override
	public void handleGallery(String galleryContent) {

	}

	@Override
	public void handleMap(String mapContent) {

	}

	@Override
	public void handleCloseTag(String closeTag) {

	}

	@Override
	public void newParagraph() {

	}

	@Override
	public void handleList(WPList list) {

		startList(list.getTag());
		for (WPListElement entry : list.getFListElements()) {
			startListElement(entry.getLevel());
			//parseRecursive(new StringBuffer(entry.getValue()));
			endListElement();
		}
		endList(list.getTag());

	}

	protected void endList(String tag) {
		// TODO Auto-generated method stub

	}

	protected void endListElement() {
		// TODO Auto-generated method stub

	}

	protected void startListElement(int level) {
		// TODO Auto-generated method stub

	}

	protected void startList(String tag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleFunction(WPFunction function) {
		try {
		functionHandler.handleFunction(function);
		} catch (Exception e) {
			System.out.println("Colud not handle Function:"+function.getName());
		}

	}

	@Override
	public void handleTemplate(WPTemplate template) {

		try {
			templateProvider.getTemplate(template).replaceParameters(template, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void handleTable(WPTable table) {
		startTable(table.getParams());
		for (WPRow row : table.getFRows()) {
			startRow(row.getParams());
			for (WPCell cell : row.getFCells()) {
				handleCell(cell);
			}
			endRow();

		}
		endTable();

	}

	protected void startTable(String params) {
		// TODO Auto-generated method stub

	}

	protected void startRow(String params) {
		// TODO Auto-generated method stub

	}

	protected void endRow() {
		// TODO Auto-generated method stub

	}

	protected void endTable() {
		// TODO Auto-generated method stub

	}

	protected void handleCell(WPCell cell) {

			startCell(cell.getType(), cell.getParams() );
		parseRecursive(cell.getSource(), cell.getStartPos(), cell.getEndPos()-2);
		endCell(cell.getType());
	}

	protected void endCell(String type) {
		// TODO Auto-generated method stub

	}

	protected void parseRecursive(StringBuffer source, int start, int stop) {

		try {
			WikipediaParser parser = new WikipediaParser(source, this);

			parser.parse(start, stop);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void parseRecursive(StringBuffer source) {

		parseRecursive(source, 0, source.length());
	}

	protected void startCell(String type, String params) {

	}

	@Override
	public void copyMathLTGT(String sourceContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void escape(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleBBCode(String name, StringBuffer bbCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleComment(String htmlCommentContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDefinition(String trim, int levelHeader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDefinitionList(String head) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEOF() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEnd(String tagName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHorizontalRule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMathWiki(String mathContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNewLine() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleOpenTag(OpenTagToken token, int start, int stop) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePlainText(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleReference(String name, String group, int start, int stop,
			StringBuffer source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleStart(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleSection(String head, int level, int start, int stop,
			StringBuffer source) {
		parseRecursive(new StringBuffer(head));
		parseRecursive(source, start, stop);
	}



	@Override
	public final void handleRawLink(ArrayList<String> params) {
		if (!params.get(0).contains(":")) {
			handleWikiLink(params);
		} else if (params.get(0).matches("(Bild|Image|Datei|File):.*")) {
			handleImage(params);
		} else if (params.get(0).matches("(Category|Kategorie):.*")) {
			handleCategory(params.get(0));

		} else if (params.get(0).matches("(de|en|fr):.*")) {
			handleLanglink(params.get(0));

		}

	}

	protected void handleLanglink(String string) {
		// TODO Auto-generated method stub

	}

	protected void handleWikiLink(ArrayList<String> params) {
		// TODO Auto-generated method stub

	}

	protected void handleCategory(String string) {
	 
	}

	protected void handleImage(ArrayList<String> params) {
		 

	}

	@Override
	public void handleWikipediaLink(String linkText, String suffix) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startBold() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startEm() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startItalic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startStrikeThrough() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopBold() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopEm() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopItalic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopStrikeThrough() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopStrong() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startStrong() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTitle() {

		return fTitle;
	}

}