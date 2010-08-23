package org.herban.wiki;

import org.herban.wiki.filter.EntityTable;
import org.herban.wiki.filter.WPBlock;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPTable;
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.util.StringUtil;



public class TemplateParser extends Parser {

   public TemplateParser(StringBuffer stringSource,  Handler handler,  int recursionLevel) {
        super(stringSource);
        fStringSource = stringSource;
        fRecursionLevel = recursionLevel;
        this.handler=handler;
    }



    protected int getNextToken() throws Exception {
        fWhiteStartPosition = 0;
        fWhiteStart = true;
        try {

            while (true) {
                fCurrentCharacter = charAt(fCurrentPosition++); // charAt(fCurrentPosition++);
                switch (fCurrentCharacter) {
                    case '{':
                            setPosition(fCurrentPosition - 1);
                            WPBlock table = wpTable();
                            if (table != null) {
                                copyWhite(  fWhiteStartPosition, 1 );

                                if (table instanceof WPTable) {
                        			handler.handleTable((WPTable) table);
                        		}

                        		else if (table instanceof WPTemplate) {
                        			handler.handleTemplate((WPTemplate) table);

                        		} else if (table instanceof WPFunction) {
                        			handler.handleFunction((WPFunction) table);
                        		}
                                fCurrentPosition = getPosition();
                                continue;
                            }

                        break;
             }


            }
            // -----------------end switch while try--------------------
        } catch (IndexOutOfBoundsException e) {
            // end of scanner text
        }
        try {
            copyWhite(  fWhiteStartPosition, 1  );
        } catch (IndexOutOfBoundsException e) {
            // end of scanner text
        }
        return WikipediaFilter.TokenEOF;
    }
    public WPBlock wpTable() {


        if (fScannerPosition < 0) {
            fScannerPosition = 0;
        }

        if  (positionStartsWith("{{")) {
            fScannerPosition++;
            fScannerPosition++;
            return createTemplate();
        }




    return null;
}


  public void parse()   {


        try {
            while (getNextToken()!= WikipediaFilter.TokenEOF) {

            }
        } catch (Exception e) {
            //
        }


           }



}
