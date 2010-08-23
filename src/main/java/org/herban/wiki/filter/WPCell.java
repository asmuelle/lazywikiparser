package org.herban.wiki.filter;

import org.herban.wiki.WikipediaScanner;

public class  WPCell {
    public static final String CAPTION = "CAPTION";

	public static final String TH = "TH";

	int fStartPos;

    int fEndPos;
    final StringBuffer fSource;
    String style;
    String params;
    String fType;

    public WPCell(StringBuffer source, int start) {
        fStartPos = start;
        fType = "TD";
        fSource=source;
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
        filter();
    }

    /**
     * @return Returns the startPos.
     */
    public int getStartPos() {
        return fStartPos;
    }

    /**
     * @param startPos The startPos to set.
     */
    public void setStartPos(int startPos) {
        fStartPos = startPos;
    }


     public void filter( ) {
        if (fEndPos > fStartPos) {

              params = null;
            WikipediaScanner scan = new WikipediaScanner(fSource, fStartPos, fEndPos);

            int index = scan.indexOfAttributes();
            if (index == (-1) || index >= fEndPos) {


            } else {


                params = fSource.substring(fStartPos, index).replaceAll("<!--.*-->", "");
                fStartPos=index+1;

            }


        }
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return fType;
    }

    /**
     * @param type The type to set.
     */
    public void setType(String type) {
        fType = type;
    }

    public String getValue() {
    	return fSource.substring(fStartPos,fEndPos);
    }

	public StringBuffer getSource() {

		return fSource;
	}

	public String getParams() {

		return params;
	}
}

