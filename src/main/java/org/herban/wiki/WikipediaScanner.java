package org.herban.wiki;


import java.util.ArrayList;
import java.util.HashMap;

import org.herban.wiki.filter.tags.TagParsingException;

import org.herban.wiki.filter.WPBlock;
import org.herban.wiki.filter.WPCell;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPList;
import org.herban.wiki.filter.WPListElement;
import org.herban.wiki.filter.WPParam;
import org.herban.wiki.filter.WPRow;
import org.herban.wiki.filter.WPTable;
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.util.StringUtil;



/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 11:09:08
 * To change this template use File | Settings | File Templates.
 */
public class WikipediaScanner {
    public final static String TAG_NAME = "$TAG_NAME";

    final protected StringBuffer fSource;

    protected int fScannerPosition;
    protected final int fLastScannerPosition;
    protected   Handler handler;
    public WikipediaScanner(StringBuffer src) {

        this(src, 0, src.length());

    }

    public WikipediaScanner(StringBuffer src, int start, int stop) {
        fSource = src;
        fScannerPosition = start;
        fLastScannerPosition=stop;

    }

    public final char charAt(int index) {
        if (index >= fSource.length()) {
            throw new IndexOutOfBoundsException("Required position: " + index
                    + " is greater than WikipediaScanner#fSource length: " + fSource.length());
        }
        return fSource.charAt(index);
    }

    public int getPosition() {
        return fScannerPosition;
    }

    public void setPosition(int newPos) {
        fScannerPosition = newPos;
    }

    public WPTable htmlTable() {
        return null;
    }

    public WPBlock wpTable() {


            if (fScannerPosition < 0) {
                fScannerPosition = 0;
            }

            if (positionStartsWith("{|")) {
                fScannerPosition++;
                fScannerPosition++;

                return createTable();
            } else if (positionStartsWith("{{")) {
                fScannerPosition++;
                fScannerPosition++;
                return createTemplate();
            }
            fScannerPosition++;



        return null;
    }

    private WPBlock createTable() {

        ArrayList<WPRow> rows = new ArrayList<WPRow>();
        WPBlock table = new WPTable(rows);
        int startPos = fScannerPosition;
        // read params until end of line
        nextNewline();
        ((WPTable) table).setParams(StringUtil.str(fSource, startPos, fScannerPosition
                - startPos));

        ArrayList<WPCell> cells = new ArrayList<WPCell>();
        WPRow row = new WPRow(cells);
        WPCell cell = null;

        char ch;

        while (true) {
            ch = charAt(fScannerPosition++);
            switch (ch) {
                case '\n':
                    ch = charAt(fScannerPosition++);
                    switch (ch) {
                        case '|': // "\n|"
                            if (cell != null) {
                                cell.setEndPos(fScannerPosition);
                                cell = null;
                            }

                            ch = charAt(fScannerPosition++);
                            switch (ch) {
                                case '-': // new row - "\n|-"
                                    ((WPTable) table).add(row);
                                    cells = new ArrayList<WPCell>();
                                    row = new WPRow(cells);
                                    startPos = fScannerPosition;
                                    nextNewline();
                                    row.setParams(StringUtil.str(fSource, startPos,
                                            fScannerPosition - startPos));

                                    break;
                                case '+': // new row - "\n|+"
                                    ((WPTable) table).add(row);
                                    cells = new ArrayList<WPCell>();
                                    row = new WPRow(cells);
                                    row.setType(WPCell.CAPTION);
                                    cell = new WPCell(fSource, fScannerPosition);
                                    cell.setType(WPCell.CAPTION);
                                    cells.add(cell);
                                    nextNewline();
                                    cell.setEndPos(fScannerPosition+2);
                                    cell = null;

                                    ((WPTable) table).add(row);
                                    cells = new ArrayList<WPCell>();
                                    row = new WPRow(cells);
                                    break;
                                case '}': // end of table - "\n|}"
                                    ((WPTable) table).add(row);

                                    return table;
                                default:
                                    fScannerPosition--;
                                    cell = new WPCell(fSource, fScannerPosition);
                                    cells.add(cell);
                            }

                            break;
                        case '!': // "\n!"
                            if (cell != null) {
                                cell.setEndPos(fScannerPosition );
                                cell = null;
                            }
                            ch = charAt(fScannerPosition++);
                            cell = new WPCell(fSource, fScannerPosition - 1);
                            cell.setType(WPCell.TH);
                            cells.add(cell);

                            break;
                        case '{': // "\n{"
                        	 if (charAt(fScannerPosition) == '{') {

                                 fScannerPosition = indexEndOfNested("{{","}}");

                             } else if (charAt(fScannerPosition) == '|') {
                                 // nested table
                             	table.setNested(true);
                                 fScannerPosition = indexEndOfNested("\n{|", "\n|}");


                             }
                            break;
                        default:
                            fScannerPosition--;
                    }
                    break;
                case '|':
                    ch = charAt(fScannerPosition++);
                    if (ch == '|') {
                        if (cell != null) {
                            cell.setEndPos(fScannerPosition);
                            cell = null;
                        }
                        cell = new WPCell(fSource, fScannerPosition);
                        cells.add(cell);
                    } else {
                        fScannerPosition--;
                    }
                    break;
                case '!':
                    ch = charAt(fScannerPosition++);
                    if (ch == '!') {
                        if (cell != null) {
                            cell.setEndPos(fScannerPosition);
                            cell = null;
                        }
                        cell = new WPCell(fSource, fScannerPosition);
                        cell.setType(WPCell.TH);
                        cells.add(cell);
                    } else {
                        fScannerPosition--;
                    }
                    break;

            }
        }
    }


	protected WPBlock createTemplate() {


        int startPos = fScannerPosition;


        WPBlock table = null;

        char ch;
        int paramStart = startPos;
        int nestingLevel = 0;
        int params = 1;
        while (true) {
            ch = charAt(fScannerPosition++);
            switch (ch) {
                case '[':
                    nestingLevel++;
                    break;
                case ']':
                    nestingLevel--;
                    break;
                case '|':

                    if (nestingLevel == 0) {
                        if (table == null) {
                            String tname = StringUtil.str(fSource, startPos, fScannerPosition - 1 - startPos).trim();
                            if (tname.startsWith("#")) {
                            	table = new WPFunction(tname, fSource, fScannerPosition);

                            }
                            else {
                            	table = new WPTemplate(tname, fScannerPosition);
                            }
                            paramStart = fScannerPosition;

                        } else {
                            WPParam param = new WPParam(fSource, paramStart, fScannerPosition-1, StringUtil.str(fSource, paramStart, fScannerPosition - 1 - paramStart).trim(), params++);

                              table.addParam(param);
                            paramStart = fScannerPosition;
                        }
                    }
                    break;
                case '}':
                    if (charAt(fScannerPosition) == '}') {
                        if (table == null) {
                        	 String tname = StringUtil.str(fSource, startPos, fScannerPosition - 1 - startPos).trim();
                             if (tname.startsWith("#")) {
                             	table = new WPFunction(tname, fSource, fScannerPosition);
                             }
                             else {
                             	table = new WPTemplate(tname, fScannerPosition);
                             }
                        } else {
                        	 WPParam param = new WPParam(fSource, paramStart, fScannerPosition-1, StringUtil.str(fSource, paramStart, fScannerPosition - 1 - paramStart).trim(), params++);
                              table.addParam(param);
                        }
                        table.setStop(fScannerPosition-2);
                        fScannerPosition++;
                        table.setStop(fScannerPosition);
                        return table;
                    }

                    break;
                case '{':
                    if (charAt(fScannerPosition) == '{') {

                        fScannerPosition = indexEndOfNested("{{","}}");

                    } else if (charAt(fScannerPosition) == '|') {
                        if (table!=null)
                    	table.setNested(true);
                        fScannerPosition = indexEndOfNested("\n{|", "\n|}");


                    }
                    break;


            }
        }
    }

    public WPList wpList(char currentCharacter) {
    	 
        WPList list = null;
        WPListElement listElement = null;
        try {
            char ch;
            if (fScannerPosition < 0) {
                // simulate newline
                fScannerPosition = 0;
                ch = '\n';
            } else {
                ch = charAt(fScannerPosition++);
            }

            int startPos;
            char[] sequence;
            list = new WPList(currentCharacter=='*'?"ul":"ol");
            int count;
            int type;
            while (fScannerPosition<fLastScannerPosition) {
                if (ch == '\n' || fScannerPosition == 0) {
                    if (listElement != null) {
                        listElement.setEndPos(fScannerPosition - 1);
                        list.add(listElement);
                        listElement = null;
                    }
                    ch = charAt(fScannerPosition++);
                    switch (ch) {
                        case '*':
                            count = 1;
                            while (charAt(fScannerPosition) == '*'
                                    || charAt(fScannerPosition) == '#') {
                                count++;
                                fScannerPosition++;
                            }
                            sequence = new char[count];
                            StringUtil.arraycopy(fSource, fScannerPosition - count,
                                    sequence, 0, count);
                            // last character determines type of list
                            if (charAt(fScannerPosition - 1) == '#') {
                                type = WPListElement.OL;
                            } else {
                                type = WPListElement.UL;
                            }

                            while (fScannerPosition<fLastScannerPosition) {
                                ch = charAt(fScannerPosition++);
                                if (!StringUtil.isWhitespace(ch)) {
                                    startPos = fScannerPosition - 1;
                                    listElement = new WPListElement(type, count,
                                            fSource, startPos);
                                    break;
                                }
                                if (ch == '\n') {
                                    fScannerPosition--; // to detect next row
                                    startPos = fScannerPosition;
                                    listElement = new WPListElement(type, count,
                                            fSource, startPos);
                                    listElement.setEndPos(startPos);
                                    list.add(listElement);
                                    listElement = null;
                                    break;
                                }
                            }

                            break;
                        case '#':
                            count = 1;
                            while (charAt(fScannerPosition) == '*'
                                    || charAt(fScannerPosition) == '#') {
                                count++;
                                fScannerPosition++;
                            }
                            sequence = new char[count];
                            StringUtil.arraycopy(fSource, fScannerPosition - count,
                                    sequence, 0, count);
                            // last character determines type of list
                            if (charAt(fScannerPosition - 1) == '#') {
                                type = WPListElement.OL;
                            } else {
                                type = WPListElement.UL;
                            }

                            while (fScannerPosition<fLastScannerPosition) {
                                ch = charAt(fScannerPosition++);
                                if (!StringUtil.isWhitespace(ch)) {
                                    startPos = fScannerPosition - 1;
                                    listElement = new WPListElement(type, count,
                                            fSource, startPos);
                                    break;
                                }
                                if (ch == '\n') {
                                    fScannerPosition--; // to detect next row
                                    startPos = fScannerPosition;
                                    listElement = new WPListElement(type, count,
                                            fSource, startPos);
                                    listElement.setEndPos(startPos);
                                    list.add(listElement);
                                    listElement = null;
                                    break;
                                }
                            }

                            break;
                        default:
                            return list;
                    }
                }
                ch = charAt(fScannerPosition++);
            }
        } catch (IndexOutOfBoundsException e) {
            // e.printStackTrace();
        }
        if (list != null) {
            if (listElement != null) {
                listElement.setEndPos(fScannerPosition - 1);
                list.add(listElement);
                listElement = null;
            }
            return list;
        }
        return null;
    }

    public  void nextNewline() {
        fScannerPosition=fSource.indexOf("\n",fScannerPosition);
    }

    public int indexEndOfComment() {

        try {
            while (!positionStartsWith("-->")) {
                fScannerPosition++;

            }
            return fScannerPosition + 2;
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    public int indexEndOfNowiki() {

        try {
            while (!positionStartsWith("</nowiki>")) {
                fScannerPosition++;

            }
            return fScannerPosition + 9;

        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    public int indexEndOfNested(String start,String stop) {
        // check nowiki and html comments?
        int count = 1;

        try {
            while (fScannerPosition<fLastScannerPosition) {

                if (positionStartsWith("<!--")) {
                    fScannerPosition = indexEndOfComment();
                    if (fScannerPosition == (-1)) {
                        return -1;
                    }
                } else if (positionStartsWith("<nowiki>")) {
                    fScannerPosition = indexEndOfNowiki();
                    if (fScannerPosition == (-1)) {
                        return -1;
                    }
                } else if (positionStartsWith(stop)) {
                    count--;
                    if (count == 0) {
                        return fScannerPosition + 2;
                    } else {
                    	fScannerPosition++;
                    }
                } else if (positionStartsWith(start)) {

                    count++;
                }
                fScannerPosition++;
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }



    public int indexOf(char ch) {
        try {
            while (fScannerPosition<fLastScannerPosition) {
                if (charAt(fScannerPosition++) == ch) {
                    return fScannerPosition - 1;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    public int indexOf(char ch, char stop) {
        try {
            char c;
            while (fScannerPosition<fLastScannerPosition) {
                c = charAt(fScannerPosition);
                if (c == ch) {
                    return fScannerPosition;
                }
                if (c == stop) {
                    return -1;
                }
                fScannerPosition++;
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    /**
     * Scan the attributes of a wiki table cell
     *
     * @return
     */
    public int  indexOfAttributes() {
        try {
            // int start = fScannerPosition;
            char ch = charAt(fScannerPosition);
            while (fScannerPosition<fLastScannerPosition) {



                // scan for Wiki links, which probably contain '|' character
                if (ch == '[') {
                    int countBrackets = 1;
                    fScannerPosition++;
                    while (countBrackets > 0) {
                        ch = charAt(fScannerPosition++);
                        if (ch == '[') {
                            ++countBrackets;
                        } else if (ch == ']') {
                            --countBrackets;
                        }
                    }
                    continue;
                } else if (ch == '{') {
                    int countBrackets = 1;
                    fScannerPosition++;
                    while (countBrackets > 0) {
                        ch = charAt(fScannerPosition++);
                        if (ch == '{') {
                            ++countBrackets;
                        } else if (ch == '}') {
                            --countBrackets;
                        }
                    }
                    continue;
                }
                if (ch == '|') {
                    if (charAt(fScannerPosition + 1) == '|') {
                        return -1;
                    }
                    return fScannerPosition;
                }

                if (charAt(fScannerPosition) == '\n') {
                    return -1;
                }
                ch = charAt(++fScannerPosition);
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    public int scanHTMLTag(HashMap<String, String> map, int currPos) throws TagParsingException {
        // inserts the tagname with key WikipediaScanner.TAG_NAME into the map
        int end = scanHTMLStarttag(map, currPos);
        // inserts the attributes as key, value pairs into the map
        return scanHTMLAttributes(map, end);
    }

    public int scanHTMLStarttag(HashMap<String, String> map, int currPos)
            throws TagParsingException {
        fScannerPosition = currPos;
        try {
            char ch = charAt(fScannerPosition);
            if (ch == '<') {
                ++fScannerPosition;
            }
            int attrStartPosition = fScannerPosition;
            while (StringUtil.isLetterOrDigit(charAt(fScannerPosition++))) {
            }
            int attrEndPosition = --fScannerPosition;
            map.put(TAG_NAME, StringUtil.str(fSource, attrStartPosition,
                    attrEndPosition - attrStartPosition));

            return fScannerPosition;
        } catch (IndexOutOfBoundsException e) {
            throw new TagParsingException();
        }
    }

    public int scanHTMLAttributes(HashMap<String, String> map, int currPos)
            throws TagParsingException {
        fScannerPosition = currPos;
        try {
            char ch = charAt(fScannerPosition);
            if (ch == '>') {
                return ++fScannerPosition;
            }

            while (ch != '>') {
                scanWhiteSpace();
                ch = charAt(fScannerPosition++);
                if (StringUtil.isLetter(ch)) {
                    scanSingleAttribute(map);
                } else {
                    if (ch == '>') {
                        return fScannerPosition;
                    }
                    throw new TagParsingException();
                }
                scanWhiteSpace();
            }
            return fScannerPosition;
        } catch (IndexOutOfBoundsException e) {
            throw new TagParsingException();
        }
    }

    private void scanSingleAttribute(HashMap<String, String> map) {

        int attrStartPosition = fScannerPosition - 1;
        while (StringUtil.isLetterOrDigit(charAt(fScannerPosition++))) {
        }
        int attrEndPosition = --fScannerPosition;

        scanWhiteSpace();
        char ch = charAt(fScannerPosition++);
        if (ch != '=') {
            return;
        }
        scanWhiteSpace();
        ch = charAt(fScannerPosition++);

        if (ch != '\"') {
            fScannerPosition--;

        }

        int valueStartPosition = fScannerPosition;
        ch = charAt(fScannerPosition++);

        int lastSpace = -1;
        while (ch != '\n' && ch != '>' && ch != '<' && ch != '|' && ch != '['
                && ch != '\'' && ch != ';' && ch != '=' && ch != '\"') {
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

        int valueEndPosition = --fScannerPosition;
        map.put(StringUtil.str(fSource, attrStartPosition, attrEndPosition
                - attrStartPosition), StringUtil.str(fSource,
                valueStartPosition, valueEndPosition - valueStartPosition));


    }

    public void scanWhiteSpace() {
        while (StringUtil.isWhitespace(charAt(fScannerPosition++))) {
        }
        --fScannerPosition;
    }

    protected boolean positionStartsWith(String seq) {
        int pos = fScannerPosition;
        for (char ch : seq.toCharArray()) {
            if (charAt(pos++) != ch) return false;
        }
        return true;
    }

}
