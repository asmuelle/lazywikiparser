package org.herban.wiki.filter.tags;


import java.util.HashMap;

import org.herban.wiki.util.StringUtil;


/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 10:21:31
 * To change this template use File | Settings | File Templates.
 */
public class    AbstractTag {
    StringBuffer fSource;
    public HashMap<String, String> attributes=new HashMap<String, String>();
    int fScannerPosition;

    char fChar;

   // StringBuffer fBuffer;
    public boolean isEmptyTag=false;


    int attrStartPosition;

    int attrEndPosition;

    int valueStartPosition;

    int valueEndPosition;

    private final int fToken;

    public AbstractTag(int token) {
        fToken = token;

    }

    public final char charAt(int index) {
        if (index >= fSource.length()) {
            throw new IndexOutOfBoundsException("Required position: " + index
                    + " is greater than AbstractTag#fSource length: " + fSource.length());
        }
        return fSource.charAt(index);
    }

    public int scanHTMLAttributes(StringBuffer buffer, int currPos)
    throws TagParsingException
    {
        isEmptyTag=false;
        attributes=new HashMap<String, String>();
        fSource = buffer;
        fScannerPosition = currPos;
        fChar = charAt(fScannerPosition);
        if (fChar == '>') {
            return ++fScannerPosition;
        }

        try {
            while (fChar != '>') {

                scanWhiteSpace();
                fChar = charAt(fScannerPosition++);
                if (fChar=='/') isEmptyTag=true;
                else if (StringUtil.isLetter(fChar)) {
                    scanSingleAttribute();
                } else {
                    if (fChar != '>' && fChar!='/') {
                        throw new TagParsingException();
                    }
                }
                scanWhiteSpace();
            }
            return fScannerPosition;
        } catch (IndexOutOfBoundsException e) {

        } finally {

        }
        return fScannerPosition;
    }



    public int scanWPTableAttributes(StringBuffer buffer, StringBuffer source,
                                     int currPos) throws TagParsingException {
        fSource = source;
        fScannerPosition = currPos;
        fChar = charAt(fScannerPosition);
        if (fChar == '|') {
            return ++fScannerPosition;
        }
       // fBuffer = buffer;
        try {
            while (fChar != '|') {
                scanWhiteSpace();
                fChar = charAt(fScannerPosition++);
                if (StringUtil.isLetter(fChar)) {
                    scanSingleAttribute();
                } else {
                    if (fChar != '|') {
                        throw new TagParsingException();
                    }
                }
                scanWhiteSpace();
            }
            return fScannerPosition;
        } catch (IndexOutOfBoundsException e) {
            throw new TagParsingException();
        }

    }

    private void scanSingleAttribute() {

        String name=scanAttributeName();

        scanWhiteSpace();
        fChar = charAt(fScannerPosition++);
        if (fChar != '=') {
            return;
        }
        scanWhiteSpace();
        fChar = charAt(fScannerPosition++);

        if (fChar != '\"') {
            fScannerPosition--;

            scanAttributeValue();
        } else {

            valueStartPosition = fScannerPosition;
            fChar = charAt(fScannerPosition++);
            while (fChar != '\"') {
                fChar = charAt(fScannerPosition++);
            }
            valueEndPosition = --fScannerPosition;
        }

        attributes.put(name,fSource.substring(valueStartPosition,valueEndPosition).replaceAll("/",""));
        if (fChar == '\"') {
            fScannerPosition++;
        }
        fChar = charAt(fScannerPosition);
    }



    public void scanWhiteSpace() {
    	 while (StringUtil.isWhitespace(charAt(fScannerPosition++))) {
        }
        --fScannerPosition;
    }

    public String  scanAttributeName() {
        attrStartPosition = fScannerPosition - 1;
        while (StringUtil.isLetter(charAt(fScannerPosition++))) {
        }

        attrEndPosition = --fScannerPosition;
        return fSource.substring(attrStartPosition, attrEndPosition);
    }

     public void sAttributeName() {
        attrStartPosition = fScannerPosition - 1;
        while (StringUtil.isLetter(charAt(fScannerPosition++))) {
        }

        attrEndPosition = --fScannerPosition;

    }

    public boolean checkValueText() {
        return true;
    }

    public boolean generate(int srcPtr, int srcLength) throws Exception {
        return false;
    }

    public String scanAttributeValue() {
        valueStartPosition = fScannerPosition;
        char ch = charAt(fScannerPosition++);
        if (ch == '/') isEmptyTag=true;
        int lastSpace = -1;
        while (ch != '\n' && ch != '>' && ch != '<' && ch != '|' && ch != '['
                && ch != '\'' && ch != ';' && ch != '=' && ch != '\"') {
            if (ch == '/') isEmptyTag=true;
            if (ch == ' ') {
                lastSpace = fScannerPosition;
            }
            ch = charAt(fScannerPosition++);
        }
        if (ch == '=' && lastSpace != (-1)
                && lastSpace < (fScannerPosition - 1)) {
            // assuming start of another attribute
            fScannerPosition = lastSpace + 1;
        }
        // }
        valueEndPosition = --fScannerPosition;
          return fSource.substring(valueStartPosition, valueEndPosition);

    }

    /*
    * (non-Javadoc)
    *
    * @see java.lang.Object#equals(java.lang.Object)
    */
    public boolean equals(Object obj) {
        return (obj instanceof AbstractTag)
                && fToken == ((AbstractTag) obj).fToken;
    }
    public int hashCode() {
    	return fToken;
    }
    /**
     * @return
     */
    public int getToken() {
        return fToken;
    }

    public String getTagName() {
        return "";
    }



}

