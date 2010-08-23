package org.herban.wiki.filter.tags;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 10:20:42
 * To change this template use File | Settings | File Templates.
 */
public class OpenTagToken extends AbstractTag {

    private String fTagName;

    private String fOpenTag;
    private int start;
    private int stop;



	public OpenTagToken(  int token, String name) {
        super(token);

        fTagName = name;
        fOpenTag = "<" + name + ">";
    }

    public OpenTagToken( int token, String name, String openTag) {
        super(token);

        fTagName = name;

        fOpenTag = openTag;
    }

    public String getTagName() {
        return fTagName;
    }

    public String getOpenTag() {
        return fOpenTag;
    }

    public void setTagName(String name) {
        fTagName = name;
    }

    public void setOpenTag(String openTag) {
        fOpenTag = openTag;
    }

	public void setStart(int start) {
		this.start = start;
	}

	public int getStart() {
		return start;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}

	public int getStop() {
		return stop;
	}


}

