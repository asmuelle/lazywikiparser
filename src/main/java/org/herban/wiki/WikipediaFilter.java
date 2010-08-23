package org.herban.wiki;


import java.util.HashMap;

import org.herban.wiki.filter.tags.AbstractTag;
import org.herban.wiki.filter.tags.CloseTagToken;
import org.herban.wiki.filter.tags.DivTag;
import org.herban.wiki.filter.tags.FontTag;
import org.herban.wiki.filter.tags.OpenTagToken;
import org.herban.wiki.filter.tags.SpanTag;
import org.herban.wiki.filter.tags.SpecialTagToken;
import org.herban.wiki.filter.tags.TableTag;


/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 10:16:26
 * To change this template use File | Settings | File Templates.
 */
public class WikipediaFilter {

    public static class InvalidInputException extends Exception {

        private static final long serialVersionUID = 2961405226776613896L;

        public InvalidInputException() {
            super();
        }

        public InvalidInputException(String s) {
            super(s);
        }
    }


    public final static String HEADER_STRINGS[] = {"=", "==", "===", "====", "=====", "======"};

    public final static int TokenNotFound = -2;
    public final static int TokenIgnore = -1;
    final static int TokenSTART = 0;
    public final static int TokenEOF = 1;
    final static int TokenERROR = 2;
    public final static int TokenBOLD = 3;
    public final static int TokenITALIC = 4;
    public final static int TokenSTRIKETHROUGH = 5;
    public final static int TokenSTRONG = 8;
    public final static int TokenEM = 9;

    final static int TokenCOMMA = 134;

    final static int TokenDOLLAR_LBRACE = 127;

    final static int TokenLIST_UL_START = 50;
    final static int TokenLIST_OL_START = 51;

    final static int TokenLIST_UL_END = 75;
    final static int TokenLIST_OL_END = 76;

    final static int TokenHTML_BR_OPEN = 190;
    final static int TokenHTML_HR_OPEN = 191;

    final static int TokenHTML_BOLD_OPEN = 200;
    final static int TokenHTML_BOLD_CLOSE = 201;
    final static int TokenHTML_ITALIC_OPEN = 202;
    final static int TokenHTML_ITALIC_CLOSE = 203;
    final static int TokenHTML_UNDERLINE_OPEN = 204;
    final static int TokenHTML_UNDERLINE_CLOSE = 205;
    final static int TokenHTML_STRIKE_OPEN = 206;
    final static int TokenHTML_STRIKE_CLOSE = 207;
    final static int TokenHTML_PARAGRAPH_OPEN = 208;
    final static int TokenHTML_PARAGRAPH_CLOSE = 209;
    final static int TokenHTML_PRE_OPEN = 210;
    final static int TokenHTML_PRE_CLOSE = 211;
    final static int TokenHTML_BLOCKQUOTE_OPEN = 212;
    final static int TokenHTML_BLOCKQUOTE_CLOSE = 213;
    final static int TokenHTML_SUB_OPEN = 216;
    final static int TokenHTML_SUB_CLOSE = 217;
    final static int TokenHTML_SUP_OPEN = 218;
    final static int TokenHTML_SUP_CLOSE = 219;

    final static int TokenHTML_H1_CLOSE = 221;

    final static int TokenHTML_H1_OPEN = 220;
    final static int TokenHTML_H2_CLOSE = 223;
    final static int TokenHTML_H2_OPEN = 222;
    final static int TokenHTML_H3_CLOSE = 225;
    final static int TokenHTML_H3_OPEN = 224;
    final static int TokenHTML_H4_CLOSE = 227;
    final static int TokenHTML_H4_OPEN = 226;
    final static int TokenHTML_H5_CLOSE = 229;
    final static int TokenHTML_H5_OPEN = 228;
    final static int TokenHTML_H6_CLOSE = 231;
    final static int TokenHTML_H6_OPEN = 230;
    final static int TokenHTML_EM_OPEN = 240;
    final static int TokenHTML_EM_CLOSE = 241;


    final static int TokenHTML_STRONG_OPEN = 242;
    final static int TokenHTML_STRONG_CLOSE = 243;

    final static int TokenHTML_VAR_OPEN = 245;
    final static int TokenHTML_VAR_CLOSE = 246;
    final static int TokenHTML_CODE_OPEN = 247;
    final static int TokenHTML_CODE_CLOSE = 248;

    final static int TokenHTML_S_OPEN = 249;
    final static int TokenHTML_S_CLOSE = 250;
    final static int TokenHTML_SMALL_OPEN = 251;
    final static int TokenHTML_SMALL_CLOSE = 252;
    final static int TokenHTML_BIG_OPEN = 253;
    final static int TokenHTML_BIG_CLOSE = 254;
    final static int TokenHTML_DEL_OPEN = 255;
    final static int TokenHTML_DEL_CLOSE = 256;

    final static int TokenHTML_MATH_OPEN = 400;
    final static int TokenHTML_MATH_CLOSE = 401;

    final static int TokenHTML_TABLE_OPEN = 500;
    final static int TokenHTML_TABLE_CLOSE = 501;
    final static int TokenHTML_CAPTION_OPEN = 502;
    final static int TokenHTML_CAPTION_CLOSE = 503;
    final static int TokenHTML_TH_OPEN = 504;
    final static int TokenHTML_TH_CLOSE = 505;
    final static int TokenHTML_TR_OPEN = 506;
    final static int TokenHTML_TR_CLOSE = 507;
    final static int TokenHTML_TD_OPEN = 508;
    final static int TokenHTML_TD_CLOSE = 509;

    final static int TokenHTML_FONT_OPEN = 520;
    final static int TokenHTML_FONT_CLOSE = 521;
    final static int TokenHTML_CENTER_OPEN = 522;
    final static int TokenHTML_CENTER_CLOSE = 523;
    final static int TokenHTML_TT_OPEN = 524;
    final static int TokenHTML_TT_CLOSE = 525;
    final static int TokenHTML_DIV_OPEN = 526;
    final static int TokenHTML_DIV_CLOSE = 527;
    final static int TokenHTML_SPAN_OPEN = 528;
    final static int TokenHTML_SPAN_CLOSE = 529;
    final static int TokenHTML_P_OPEN = 530;
    final static int TokenHTML_P_CLOSE = 531;

    public final static int TokenIdentifier = 138;
    final static int TokenLBRACKET = 132;

    final static int TokenLPAREN = 128;

    final static int TokenPLUGIN_IDENTIFIER = 130;
    final static int TokenRBRACE = 131;
    final static int TokenRBRACKET = 133;
    final static int TokenRPAREN = 129;
    final static int Token_REFERENCE_OPEN = 600;
    final static int Token_REFERENCE_CLOSE = 601;
    final static int Token_REF_OPEN = 604;
    final static int Token_REF_CLOSE = 605;
    final static int Token_GALLERY_OPEN = 606;
    final static int Token_GALLERY_CLOSE = 607;
    final static int TokenHTML_IMAGEMAP_OPEN = 602;
    final static int TokenHTML_IMAGEMAP_CLOSE = 603;
    final static int TokenHTML_ANCHOR_OPEN = 604;
    final static int TokenHTML_ANCHOR_CLOSE = 605;


    public final static AbstractTag BOLD = new AbstractTag(TokenBOLD);
    public final static AbstractTag ITALIC = new AbstractTag(TokenITALIC);
    public final static AbstractTag STRONG = new AbstractTag(TokenSTRONG);
    public final static AbstractTag EM = new AbstractTag(TokenEM);

    final static AbstractTag HTML_BR_OPEN = new SpecialTagToken(TokenHTML_BR_OPEN, "br", "<br />");
    final static AbstractTag HTML_HR_OPEN = new SpecialTagToken(TokenHTML_HR_OPEN, "hr", "<hr />");

    final static AbstractTag HTML_A_CLOSE = new CloseTagToken(TokenHTML_ANCHOR_CLOSE, "a");
    final static AbstractTag HTML_A_OPEN = new OpenTagToken(TokenHTML_ANCHOR_OPEN, "a");
    final static AbstractTag HTML_EM_CLOSE = new CloseTagToken(TokenHTML_EM_CLOSE, "em");
    final static AbstractTag HTML_EM_OPEN = new OpenTagToken(TokenHTML_EM_OPEN, "em");
    final static AbstractTag HTML_H1_CLOSE = new CloseTagToken(TokenHTML_H1_CLOSE, "h1");
    final static AbstractTag HTML_H1_OPEN = new OpenTagToken(TokenHTML_H1_OPEN, "h1");
    final static AbstractTag HTML_H2_CLOSE = new CloseTagToken(TokenHTML_H2_CLOSE, "h2");
    final static AbstractTag HTML_H2_OPEN = new OpenTagToken(TokenHTML_H2_OPEN, "h2");
    final static AbstractTag HTML_H3_CLOSE = new CloseTagToken(TokenHTML_H3_CLOSE, "h3");
    final static AbstractTag HTML_H3_OPEN = new OpenTagToken(TokenHTML_H3_OPEN, "h3");
    final static AbstractTag HTML_H4_CLOSE = new CloseTagToken(TokenHTML_H4_CLOSE, "h4");
    final static AbstractTag HTML_H4_OPEN = new OpenTagToken(TokenHTML_H4_OPEN, "h4");
    final static AbstractTag HTML_H5_CLOSE = new CloseTagToken(TokenHTML_H5_CLOSE, "h5");
    final static AbstractTag HTML_H5_OPEN = new OpenTagToken(TokenHTML_H5_OPEN, "h5");
    final static AbstractTag HTML_H6_CLOSE = new CloseTagToken(TokenHTML_H6_CLOSE, "h6");
    final static AbstractTag HTML_H6_OPEN = new OpenTagToken(TokenHTML_H6_OPEN, "h6");
    final static AbstractTag HTML_ITALIC_CLOSE = new CloseTagToken(TokenHTML_ITALIC_CLOSE, "i");
    final static AbstractTag HTML_ITALIC_OPEN = new OpenTagToken(TokenHTML_ITALIC_OPEN, "i");
    final static AbstractTag HTML_BOLD_CLOSE = new CloseTagToken(TokenHTML_BOLD_CLOSE, "b");
    final static AbstractTag HTML_BOLD_OPEN = new OpenTagToken(TokenHTML_BOLD_OPEN, "b");

    //
    final static AbstractTag HTML_PARAGRAPH_CLOSE = new CloseTagToken(TokenHTML_PARAGRAPH_CLOSE, "p", "\n</p>");
    final static AbstractTag HTML_PARAGRAPH_OPEN = new OpenTagToken(TokenHTML_PARAGRAPH_OPEN, "p");
    final static AbstractTag HTML_PRE_CLOSE = new CloseTagToken(TokenHTML_PRE_CLOSE, "pre");
    public final static AbstractTag HTML_PRE_OPEN = new OpenTagToken(TokenHTML_PRE_OPEN, "pre");
    final static AbstractTag HTML_BLOCKQUOTE_CLOSE = new CloseTagToken(TokenHTML_BLOCKQUOTE_CLOSE, "blockquote");
    final static AbstractTag HTML_BLOCKQUOTE_OPEN = new OpenTagToken(TokenHTML_BLOCKQUOTE_OPEN, "blockquote");
    final static AbstractTag HTML_STRIKE_CLOSE = new CloseTagToken(TokenHTML_STRIKE_CLOSE, "strike");
    final static AbstractTag HTML_STRIKE_OPEN = new OpenTagToken(TokenHTML_STRIKE_OPEN, "strike");
    final static AbstractTag HTML_STRONG_CLOSE = new CloseTagToken(TokenHTML_STRONG_CLOSE, "strong");
    final static AbstractTag HTML_STRONG_OPEN = new OpenTagToken(TokenHTML_STRONG_OPEN, "strong");
    final static AbstractTag HTML_UNDERLINE_CLOSE = new CloseTagToken(TokenHTML_UNDERLINE_CLOSE, "u");
    final static AbstractTag HTML_UNDERLINE_OPEN = new OpenTagToken(TokenHTML_UNDERLINE_OPEN, "u");
    final static AbstractTag HTML_SUB_CLOSE = new CloseTagToken(TokenHTML_SUB_CLOSE, "sub");
    final static AbstractTag HTML_SUB_OPEN = new OpenTagToken(TokenHTML_SUB_OPEN, "sub");
    final static AbstractTag HTML_SUP_CLOSE = new CloseTagToken(TokenHTML_SUP_CLOSE, "sup");
    final static AbstractTag HTML_SUP_OPEN = new OpenTagToken(TokenHTML_SUP_OPEN, "sup");
    final static AbstractTag REF_OPEN = new OpenTagToken(Token_REF_OPEN, "ref");
    final static AbstractTag REF_CLOSE = new CloseTagToken(Token_REF_CLOSE, "ref");

    final static AbstractTag HTML_CENTER_OPEN = new OpenTagToken(TokenHTML_CENTER_OPEN, "center");
    final static AbstractTag HTML_CENTER_CLOSE = new CloseTagToken(TokenHTML_CENTER_CLOSE, "center");
    final static AbstractTag HTML_TT_OPEN = new OpenTagToken(TokenHTML_TT_OPEN, "tt");
    final static AbstractTag HTML_TT_CLOSE = new CloseTagToken(TokenHTML_TT_CLOSE, "tt");

    final static AbstractTag HTML_MATH_OPEN = new OpenTagToken(TokenHTML_MATH_OPEN, "math");
    final static AbstractTag HTML_MATH_CLOSE = new CloseTagToken(TokenHTML_MATH_CLOSE, "math");

    final static AbstractTag HTML_TABLE_OPEN = new TableTag(TokenHTML_TABLE_OPEN, "table", "<table>");
    final static AbstractTag HTML_TABLE_CLOSE = new CloseTagToken(TokenHTML_TABLE_CLOSE, "table");
    final static AbstractTag HTML_CAPTION_OPEN = new OpenTagToken(TokenHTML_CAPTION_OPEN, "caption");
    final static AbstractTag HTML_CAPTION_CLOSE = new CloseTagToken(TokenHTML_CAPTION_CLOSE, "caption");
    final static AbstractTag HTML_TH_OPEN = new OpenTagToken(TokenHTML_TH_OPEN, "th");
    final static AbstractTag HTML_TH_CLOSE = new CloseTagToken(TokenHTML_TH_CLOSE, "th");
    final static AbstractTag HTML_TR_OPEN = new OpenTagToken(TokenHTML_TR_OPEN, "tr");
    final static AbstractTag HTML_TR_CLOSE = new CloseTagToken(TokenHTML_TR_CLOSE, "tr");
    final static AbstractTag HTML_TD_OPEN = new OpenTagToken(TokenHTML_TD_OPEN, "td");
    final static AbstractTag HTML_TD_CLOSE = new CloseTagToken(TokenHTML_TD_CLOSE, "td");
    final static AbstractTag HTML_FONT_OPEN = new FontTag(TokenHTML_FONT_OPEN, "font", "<font>");
    final static AbstractTag HTML_FONT_CLOSE = new CloseTagToken(TokenHTML_FONT_CLOSE, "font");
    final static AbstractTag HTML_DIV_OPEN = new DivTag(TokenHTML_DIV_OPEN, "div", "<div>");
    final static AbstractTag HTML_DIV_CLOSE = new CloseTagToken(TokenHTML_DIV_CLOSE, "div");
    final static AbstractTag HTML_SPAN_OPEN = new SpanTag(TokenHTML_SPAN_OPEN, "span", "<span>");
    final static AbstractTag HTML_SPAN_CLOSE = new CloseTagToken(TokenHTML_SPAN_CLOSE, "span");
    public final static AbstractTag HTML_P_OPEN = new DivTag(TokenHTML_P_OPEN, "p", "<p>");
    final static AbstractTag HTML_P_CLOSE = new CloseTagToken(TokenHTML_P_CLOSE, "p", "\n</p>");

    final static AbstractTag LIST_OL_START = new AbstractTag(TokenLIST_OL_START);
    final static AbstractTag LIST_UL_START = new AbstractTag(TokenLIST_UL_START);

    final static AbstractTag HTML_VAR_OPEN = new OpenTagToken(TokenHTML_VAR_OPEN, "var");
    final static AbstractTag HTML_VAR_CLOSE = new CloseTagToken(TokenHTML_VAR_CLOSE, "var");
    final static AbstractTag HTML_CODE_OPEN = new OpenTagToken(TokenHTML_CODE_OPEN, "code");
    final static AbstractTag HTML_CODE_CLOSE = new CloseTagToken(TokenHTML_CODE_CLOSE, "code");


    final static AbstractTag HTML_S_OPEN = new OpenTagToken(TokenHTML_S_OPEN, "s");
    final static AbstractTag HTML_S_CLOSE = new CloseTagToken(TokenHTML_S_CLOSE, "s");

    final static AbstractTag HTML_SMALL_OPEN = new OpenTagToken(TokenHTML_SMALL_OPEN, "small");
    final static AbstractTag HTML_SMALL_CLOSE = new CloseTagToken(TokenHTML_SMALL_CLOSE, "small");

    final static AbstractTag HTML_BIG_OPEN = new OpenTagToken(TokenHTML_BIG_OPEN, "big");
    final static AbstractTag HTML_BIG_CLOSE = new CloseTagToken(TokenHTML_BIG_CLOSE, "big");

    final static AbstractTag HTML_DEL_OPEN = new OpenTagToken(TokenHTML_DEL_OPEN, "del");
    final static AbstractTag HTML_DEL_CLOSE = new CloseTagToken(TokenHTML_DEL_CLOSE, "del");
    final static AbstractTag HTML_IMAGEMAP_OPEN = new OpenTagToken(TokenHTML_IMAGEMAP_OPEN, "div");
    final static AbstractTag HTML_IMAGEMAP_CLOSE = new CloseTagToken(TokenHTML_IMAGEMAP_CLOSE, "div");


    public final static HashMap<String, AbstractTag> OPEN_TAGS = new HashMap<String, AbstractTag>();
    public final static HashMap<String, AbstractTag> CLOSE_TAGS = new HashMap<String, AbstractTag>();

	public static final AbstractTag REFERENCE_OPEN = new SpecialTagToken(Token_REFERENCE_OPEN,"references", "<div class=references/>");

	public static final AbstractTag REFERENCE_CLOSE = new CloseTagToken(Token_REFERENCE_CLOSE,"ref");
	public static final AbstractTag GALLERY_OPEN = new OpenTagToken(Token_GALLERY_OPEN,"gallery");

	public static final AbstractTag GALLERY_CLOSE = new CloseTagToken(Token_GALLERY_CLOSE,"gallery");


    static {

        OPEN_TAGS.put("br", HTML_BR_OPEN);
        OPEN_TAGS.put("hr", HTML_HR_OPEN);

        OPEN_TAGS.put("h1", HTML_H1_OPEN);
        OPEN_TAGS.put("h2", HTML_H2_OPEN);
        OPEN_TAGS.put("h3", HTML_H3_OPEN);
        OPEN_TAGS.put("h4", HTML_H4_OPEN);
        OPEN_TAGS.put("h5", HTML_H5_OPEN);
        OPEN_TAGS.put("h6", HTML_H6_OPEN);

        CLOSE_TAGS.put("h1", HTML_H1_CLOSE);
        CLOSE_TAGS.put("h2", HTML_H2_CLOSE);
        CLOSE_TAGS.put("h3", HTML_H1_CLOSE);
        CLOSE_TAGS.put("h4", HTML_H2_CLOSE);
        CLOSE_TAGS.put("h5", HTML_H1_CLOSE);
        CLOSE_TAGS.put("h6", HTML_H2_CLOSE);

        OPEN_TAGS.put("em", HTML_EM_OPEN);
        CLOSE_TAGS.put("em", HTML_EM_CLOSE);

        OPEN_TAGS.put("a", HTML_A_OPEN);
        CLOSE_TAGS.put("a", HTML_A_CLOSE);

        OPEN_TAGS.put("i", HTML_ITALIC_OPEN);
        CLOSE_TAGS.put("i", HTML_ITALIC_CLOSE);
        OPEN_TAGS.put("b", HTML_BOLD_OPEN);
        CLOSE_TAGS.put("b", HTML_BOLD_CLOSE);
        OPEN_TAGS.put("strong", HTML_STRONG_OPEN);
        CLOSE_TAGS.put("strong", HTML_STRONG_CLOSE);
        OPEN_TAGS.put("u", HTML_UNDERLINE_OPEN);
        CLOSE_TAGS.put("u", HTML_UNDERLINE_CLOSE);
        OPEN_TAGS.put("p", HTML_PARAGRAPH_OPEN);
        CLOSE_TAGS.put("p", HTML_PARAGRAPH_CLOSE);

        OPEN_TAGS.put("pre", HTML_PRE_OPEN);
        CLOSE_TAGS.put("pre", HTML_PRE_CLOSE);
        OPEN_TAGS.put("blockquote", HTML_BLOCKQUOTE_OPEN);
        CLOSE_TAGS.put("blockquote", HTML_BLOCKQUOTE_CLOSE);

        OPEN_TAGS.put("var", HTML_VAR_OPEN);
        CLOSE_TAGS.put("var", HTML_VAR_CLOSE);
        OPEN_TAGS.put("code", HTML_CODE_OPEN);
        CLOSE_TAGS.put("code", HTML_CODE_CLOSE);
        OPEN_TAGS.put("s", HTML_S_OPEN);
        CLOSE_TAGS.put("s", HTML_S_CLOSE);
        OPEN_TAGS.put("small", HTML_SMALL_OPEN);
        CLOSE_TAGS.put("small", HTML_SMALL_CLOSE);
        OPEN_TAGS.put("big", HTML_BIG_OPEN);
        CLOSE_TAGS.put("big", HTML_BIG_CLOSE);
        OPEN_TAGS.put("del", HTML_DEL_OPEN);
        CLOSE_TAGS.put("del", HTML_DEL_CLOSE);

        OPEN_TAGS.put("sub", HTML_SUB_OPEN);
        CLOSE_TAGS.put("sub", HTML_SUB_CLOSE);
        OPEN_TAGS.put("sup", HTML_SUP_OPEN);
        CLOSE_TAGS.put("sup", HTML_SUP_CLOSE);
        OPEN_TAGS.put("strike", HTML_STRIKE_OPEN);
        CLOSE_TAGS.put("strike", HTML_STRIKE_CLOSE);

        OPEN_TAGS.put("math", HTML_MATH_OPEN);
        CLOSE_TAGS.put("math", HTML_MATH_CLOSE);

        OPEN_TAGS.put("table", HTML_TABLE_OPEN);
        CLOSE_TAGS.put("table", HTML_TABLE_CLOSE);
        OPEN_TAGS.put("th", HTML_TH_OPEN);
        CLOSE_TAGS.put("th", HTML_TH_CLOSE);
        OPEN_TAGS.put("tr", HTML_TR_OPEN);
        CLOSE_TAGS.put("tr", HTML_TR_CLOSE);
        OPEN_TAGS.put("td", HTML_TD_OPEN);
        CLOSE_TAGS.put("td", HTML_TD_CLOSE);
        OPEN_TAGS.put("caption", HTML_CAPTION_OPEN);
        CLOSE_TAGS.put("caption", HTML_CAPTION_CLOSE);

        OPEN_TAGS.put("font", HTML_FONT_OPEN);
        CLOSE_TAGS.put("font", HTML_FONT_CLOSE);
        OPEN_TAGS.put("center", HTML_CENTER_OPEN);
        CLOSE_TAGS.put("center", HTML_CENTER_CLOSE);
        OPEN_TAGS.put("tt", HTML_TT_OPEN);
        CLOSE_TAGS.put("tt", HTML_TT_CLOSE);
        OPEN_TAGS.put("div", HTML_DIV_OPEN);
        CLOSE_TAGS.put("div", HTML_DIV_CLOSE);
        OPEN_TAGS.put("span", HTML_SPAN_OPEN);
        CLOSE_TAGS.put("span", HTML_SPAN_CLOSE);
        OPEN_TAGS.put("p", HTML_P_OPEN);
        CLOSE_TAGS.put("p", HTML_P_CLOSE);
        OPEN_TAGS.put("ref", REF_OPEN);
        CLOSE_TAGS.put("ref", REF_CLOSE);
        OPEN_TAGS.put("references", REFERENCE_OPEN);
        CLOSE_TAGS.put("references", REFERENCE_CLOSE);

        OPEN_TAGS.put("imagemap", HTML_IMAGEMAP_OPEN);
        CLOSE_TAGS.put("imagemap", HTML_IMAGEMAP_CLOSE);
        OPEN_TAGS.put("gallery", GALLERY_OPEN);
        CLOSE_TAGS.put("gallery", GALLERY_CLOSE);
    }

    /**
     * Limits the recursive call of this filter to a depth of RECURSION_LIMIT
     */
    public final static int RECURSION_LIMIT = 5;

    //  final static AbstractTag START = new AbstractTag(TokenSTART);

    public final static AbstractTag STRIKETHROUGH = new AbstractTag(TokenSTRIKETHROUGH);

    /**
     * Determines if the specified character may be part of a url
     */
    public static boolean isUrlIdentifierPart(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        final String test = "-_.!~*';/?:@#&=+$,%";
        return test.indexOf(ch) != (-1);
    }

    /**
     * Determines if the specified character may be part of a wiki plugin identifier as other than the first character
     */
    public static boolean isWikiPluginIdentifierPart(char ch) {
        return Character.isLetterOrDigit(ch) || (ch == '_');
    }

    /**
     * Determines if the specified character may be part the first character of a wiki plugin identifier
     */
    public static boolean isWikiPluginIdentifierStart(char ch) {
        return Character.isLetter(ch);
    }




    public Parser fParser;





}
