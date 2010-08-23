package org.herban.wiki.filter.tags;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 10:20:05
 * To change this template use File | Settings | File Templates.
 */
public class CloseTagToken extends AbstractTag {
    private String fTagName;

    private String fCloseTag;

    public CloseTagToken(int token, String name) {
        super(token);
        fTagName = name;
        fCloseTag = "</" + name + ">";
    }

    public CloseTagToken(int token, String name, String closeTag) {
        super(token);
        fTagName = name;

        fCloseTag = closeTag;
    }

    public String getTagName() {
        return fTagName;
    }

    public String getCloseTag() {
        return fCloseTag;
    }

    public void setTagName(String name) {
        fTagName = name;
    }

    public void setCloseTag(String closeTag) {
        fCloseTag = closeTag;
    }

}
 
