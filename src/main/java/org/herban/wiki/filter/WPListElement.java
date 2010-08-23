package org.herban.wiki.filter;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 13:12:20
 * To change this template use File | Settings | File Templates.
 */





public class WPListElement {
    private final int fStartPos;
    private final StringBuffer fSource;
    int fEndPos;

    public static final int OL = 2;

    public static final int UL = 1;

    char[] fSequence;

    int fType;

    private final int fLevel;

    public WPListElement(int type, int level, StringBuffer source, int start) {
        fType = type;
        fLevel = level;
        fSource=source;
        fStartPos = start;
    }

    /**
     * @return Returns the endPos.
     */
    public int getEndPos() {
        return fEndPos;
    }

    /**
     * @param endPos The endPos to set.
     */
    public void setEndPos(int endPos) {
        fEndPos = endPos;
    }

    /**
     * @return Returns the startPos.
     */
    public int getStartPos() {
        return fStartPos;
    }



    /**
     * @return Returns the type.
     */
    public int getType() {
        return fType;
    }

    /**
     * @param type The type to set.
     */
    public void setType(int type) {
        fType = type;
    }

    /**
     * @return Returns the level.
     */
    public int getLevel() {
        return fLevel;
    }



    /**
     * @return Returns the sequence.
     */
    public char[] getSequence() {
        return fSequence;
    }

    /**
     * @param sequence The sequence to set.
     */
    public void setSequence(char[] sequence) {
        fSequence = sequence;
    }

	public String getValue() {

		return fSource.substring(fStartPos,fEndPos);
	}
}

