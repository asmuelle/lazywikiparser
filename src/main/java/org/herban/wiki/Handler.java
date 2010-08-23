package org.herban.wiki;

import java.util.ArrayList;

import org.herban.wiki.filter.tags.OpenTagToken;

import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPList;
import org.herban.wiki.filter.WPTable;
import org.herban.wiki.filter.WPTemplate;

/**
 * @author andreas mueller
 *
 */
public interface Handler  {

	String getTitle();
    /**
     * @param name
     * @return
     */
    boolean handleHTTPLink(String name)  ;

    /**
     * @param linkText
     * @param suffix
     */
    void handleWikipediaLink(String linkText, String suffix)  ;
    /**
     * @param mapContent
     */
    void handleMap(String mapContent)  ;
	/**
	 * @param galleryContent
	 */
	void handleGallery(String galleryContent);

	/**
	 * @param token
	 * @param fCurrentPosition
	 * @param tagNameStart
	 */
	void handleOpenTag(OpenTagToken token, int tagNameStart, int fCurrentPosition);
	/**
	 * @param closeTag
	 */
	void handleCloseTag(String closeTag);
    /**
     * @param text
     */
    void handlePlainText(String text);
	/**
	 *
	 */
    void handleEOF();
	/**
	 *
	 */
	void handleNewLine();
    /**
     *
     */
	void handleHorizontalRule();

	/**
	 *
	 */
	void newParagraph();
    /**
     * @param template
     */
    void handleTemplate(WPTemplate template);
    /**
     * @param table
     */
    void handleTable(WPTable table);
    /**
     * @param list
     */
    void handleList(WPList list);
	/**
	 * @param htmlCommentContent
	 */
	void handleComment(String htmlCommentContent);
	/**
	 * @param mathContent
	 */
	void handleMathWiki(String mathContent);
	/**
	 * @param name
	 * @param bbCode
	 * @return
	 */
	boolean handleBBCode(String name, StringBuffer bbCode);
	/**
	 * @param sourceContent
	 */
	void copyMathLTGT(String sourceContent);

	/**
	 * @param tagName
	 */
	void handleEnd(String tagName);
	/**
	 * @param string
	 */
	void handleStart(String string);

	/**
	 * @param trim
	 * @param levelHeader
	 */
	void handleDefinition(String trim, int levelHeader);
	/**
	 * @param head
	 */
	void handleDefinitionList(String head);
	/**
	 * @param name
	 * @param group
	 * @param start
	 * @param stop
	 * @param source
	 */
	void handleReference(String name, String group, int start,
			int stop, StringBuffer source);
	/**
	 * @param function
	 */
	void handleFunction(WPFunction function);
	/**
	 * @param string
	 */
	void escape(String string);
	/**
	 * @param head
	 * @param level
	 * @param start
	 * @param stop
	 * @param source
	 */
	void handleSection(String head, int level, int start, int stop,
			StringBuffer source);


	void handleRawLink(ArrayList<String> params);

	void startBold();

	void stopBold();

	void startItalic();

	void stopStrong();

	void stopItalic();

	void stopEm();

	void startEm();

	void stopStrikeThrough();

	void startStrikeThrough();

	void startStrong();
}
