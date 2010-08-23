package org.herban.wiki;

import org.herban.wiki.util.StringUtil;

public abstract class Parser extends WikipediaScanner  {


	/**
	 * The corresponding String for the character source array
	 */
	protected StringBuffer fStringSource;
	/**
	 * The current scanned character
	 */
	protected char fCurrentCharacter;
	/**
	 * The current offset in the character source array
	 */
	protected int fCurrentPosition;

	/**
	 * The current recursion level for this parser
	 */
	protected int fRecursionLevel;
	protected boolean fWhiteStart = false;
	protected int fWhiteStartPosition = 0;

/*    	protected String copyWhite(String text) throws Exception {
	    StringBuffer buffer = new StringBuffer(text.length() + 32);
	    StringUtil.escape(text, buffer);
	    return buffer.toString();
	} */

	public Parser(StringBuffer src) {
		super(src);
	}

	public Parser(StringBuffer src, int start, int stop) {
		super(src, start, stop);
	}

	public final boolean getNextChar(char testedChar) {
	    int temp = fCurrentPosition;
	    try {
	        fCurrentCharacter = charAt(fCurrentPosition++);
	        if (fCurrentCharacter != testedChar) {
	            fCurrentPosition = temp;
	            return false;
	        }
	        return true;

	    } catch (IndexOutOfBoundsException e) {
	        fCurrentPosition = temp;
	        return false;
	    }
	}

	public final int getNextChar(char testedChar1, char testedChar2) {
	    int temp = fCurrentPosition;
	    try {
	        int result;
	        fCurrentCharacter = charAt(fCurrentPosition++);
	        if (fCurrentCharacter == testedChar1)
	            result = 0;
	        else if (fCurrentCharacter == testedChar2)
	            result = 1;
	        else {
	            fCurrentPosition = temp;
	            return -1;
	        }
	        return result;
	    } catch (IndexOutOfBoundsException e) {
	        fCurrentPosition = temp;
	        return -1;
	    }
	}

	public final boolean getNextCharAsDigit() {
	    int temp = fCurrentPosition;
	    try {
	        fCurrentCharacter = charAt(fCurrentPosition++);
	        if (!StringUtil.isDigit(fCurrentCharacter)) {
	            fCurrentPosition = temp;
	            return false;
	        }
	        return true;
	    } catch (IndexOutOfBoundsException e) {
	        fCurrentPosition = temp;
	        return false;
	    }
	}

	public final int getNumberOfChar(char testedChar) {
	    int number = 0;
	    try {
	        while ((fCurrentCharacter = charAt(fCurrentPosition++)) == testedChar) {
	            number++;
	        }
	    } catch (IndexOutOfBoundsException e) {

	    }
	    fCurrentPosition--;
	    return number;
	}

	public boolean getNextCharAsWikiPluginIdentifierPart() {
	    int temp = fCurrentPosition;
	    try {
	        fCurrentCharacter = charAt(fCurrentPosition++);

	        if (!WikipediaFilter.isWikiPluginIdentifierPart(fCurrentCharacter)) {
	            fCurrentPosition = temp;
	            return false;
	        }
	        return true;
	    } catch (IndexOutOfBoundsException e) {
	        fCurrentPosition = temp;
	        return false;
	    }
	}

	protected boolean isStartOfLine() {
	    boolean isLineStart = false;
	    if (fCurrentPosition >= 2) {
	        char beforeChar = charAt(fCurrentPosition - 2);
	        if (beforeChar == '\n') {
	            isLineStart = true;
	        }
	    }
	    if (fCurrentPosition == 1) {
	        isLineStart = true;
	    }
	    return isLineStart;
	}

	protected boolean isEmptyLine() {
	    int temp = fCurrentPosition - 1;
	    char ch;
	    try {
	        while (true) {
	            ch = charAt(temp);
	            if (!StringUtil.isWhitespace(ch)) {
	                return false;
	            }
	            if (ch == '\n') {
	                return true;
	            }
	            temp++;
	        }
	    } catch (IndexOutOfBoundsException e) {
	        // ..
	    }
	    return true;
	}

	public int scanIdentifierOrKeyword(boolean isVariable) {
	    while (getNextCharAsWikiPluginIdentifierPart()) {
	    }
	    return WikipediaFilter.TokenIdentifier;
	}

	/**
	 * read until the string is found
	 *
	 * @return
	 */
	protected boolean readUntilString(String testedString) {
	    int index = fStringSource.indexOf(testedString, fCurrentPosition);
	    if (index != (-1)) {
	        fCurrentPosition = index + testedString.length();
	        return true;
	    }
	    return false;
	}

	/**
	 * read until character is found
	 *
	 * @return
	 */
	protected boolean readUntilChar(char testedChar) {
	    int temp = fCurrentPosition;
	    try {
	        while ((fCurrentCharacter = charAt(fCurrentPosition++)) != testedChar) {
	        }
	        return true;
	    } catch (IndexOutOfBoundsException e) {
	        fCurrentPosition = temp;
	        return false;
	    }
	}

	/**
	 * read until character is found or end-of-line is reached
	 *
	 * @return -1 - for IndexOutOfBoundsException; 0 - for LF found; 1 - for
	 *         testedChar found
	 */
	protected boolean readUntilEOL() {
	    try {
	        while (true) {
	            fCurrentCharacter = charAt(fCurrentPosition++);
	            if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
	                return true;
	            }
	        }
	    } catch (IndexOutOfBoundsException e) {
	        --fCurrentPosition;
	        return true;
	    }
	}

	/**
	 * copy the content in the resulting buffer and escape special html characters
	 * (&lt; &gt; &quot; &amp; &#39;)
	 * @return
	 * @throws Exception
	 */
	public boolean copyWhite( final int whiteStartPosition, final int diff) {
			    if (fWhiteStart) {
			        try {
			            final int len = fCurrentPosition - diff;
			            int currentIndex = whiteStartPosition;
			            int lastIndex = currentIndex;
			            while (currentIndex < len) {
			                switch (fSource.charAt(currentIndex++)) {
			                    case '\r': // special ignore \r - allow only \n
			                        if (lastIndex < (currentIndex - 1)) {
			                        	 append(fSource.substring(lastIndex, currentIndex));
			                             lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                        break;
			                    case '<': // special html escape character
			                        if (lastIndex < (currentIndex - 1)) {
			                        	 append(fSource.substring(lastIndex, currentIndex));
			                             lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                         handler.escape("&lt;");
			                        break;
			                    case '>': // special html escape character
			                        if (lastIndex < (currentIndex - 1)) {
			                             append(fSource.substring(lastIndex, currentIndex));
			                            lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                        handler.escape("&gt;");
			                        break;
			                    case '&': // special html escape character
			                        if (lastIndex < (currentIndex - 1)) {
			                        	 append(fSource.substring(lastIndex, currentIndex));
			                             lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                        handler.escape("&amp;");
			                        break;
			                    case '\'': // special html escape character
			                        if (lastIndex < (currentIndex - 1)) {
			                        	 append(fSource.substring(lastIndex, currentIndex));
			                             lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                        handler.escape("&#39;");
			                        break;
			                    case '\"': // special html escavvvvvvvvvvvvvvpe character
			                        if (lastIndex < (currentIndex - 1)) {
			                        	 handler.escape("\"");
			                             lastIndex = currentIndex;
			                        } else {
			                            lastIndex++;
			                        }
			                        handler.escape("&quot;");
			                        break;
			                }
			            }
			            if (lastIndex < (currentIndex)) {
			               append(fSource.substring(lastIndex,currentIndex));
			            }
			        } finally {
                      fWhiteStart=false;
			        }
			    }
			    return false;
			}

	private void append(String substring) {
		if (substring.contains("\n")) {
			String[] lines=substring.split("\n");
			for(int i=0;i<lines.length;i++) {

	           if (lines[i].length()>0)  {
				handler.handlePlainText(lines[i]);
				if (i<lines.length-1)
					handler.handleNewLine();
	           }
			}
		} else {
		handler.handlePlainText(substring);

	}
	}
}