package org.herban.wiki;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.herban.wiki.filter.tags.AbstractTag;
import org.herban.wiki.filter.tags.CloseTagToken;
import org.herban.wiki.filter.tags.OpenTagToken;
import org.herban.wiki.filter.tags.SpecialTagToken;
import org.herban.wiki.filter.tags.TagParsingException;

import org.herban.wiki.filter.EntityTable;
import org.herban.wiki.filter.TokenStack;
import org.herban.wiki.filter.WPBlock;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPList;
import org.herban.wiki.filter.WPTable;
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.util.StringUtil;

public class WikipediaParser extends Parser {

	public static final HashSet<String> STOP_TAGS_SET = new HashSet<String>();
	public static int WIKI_COUNTER = 0;
	public static final String[] STOP_TAGS = { "p", "pre", "dl", "dd", "ul",
			"ol", "li", "hr", "h1", "h2", "h3", "h4", "h5", "h6", "table",
			"caption", "th", "tr", "td", };

	static {
		STOP_TAGS_SET.addAll(Arrays.asList(STOP_TAGS));
	}

	private final TokenStack fTokenStack;

	private final boolean fUseBBCodes = false;
    private int fLastPosition;

	public WikipediaParser(StringBuffer stringSource, Handler handler) {
		super(stringSource);
		fStringSource = stringSource;
		fRecursionLevel = 0;
		fTokenStack = new TokenStack();
		this.handler = handler;
		fCurrentPosition = 0;
		fLastPosition = fStringSource.length();

	}

	/**
	 * Render the HTML token which are defined in the OPEN_TAGS and CLOSE_TAGS
	 * map
	 *
	 * @return
	 * @throws Exception
	 */
	protected int getHTMLToken()   {
		int currentHtmlPosition = fCurrentPosition;
		try {
			if (getNextChar('/')) {

				currentHtmlPosition++;

				if (!readTag()) {
					return WikipediaFilter.TokenNotFound;
				}
				String closeTagString = StringUtil.str(fSource,
						currentHtmlPosition,
						fCurrentPosition - currentHtmlPosition - 1)
						.toLowerCase();

				fCurrentPosition--;
				if (charAt(fCurrentPosition) != '>') {
					fWhiteStart = copyWhite(fWhiteStartPosition,
							0);
					if (STOP_TAGS_SET.contains(closeTagString)) {
						reduceTokenStack();
					}
					return WikipediaFilter.TokenIgnore;
				}
				fCurrentPosition++;
				try {
					CloseTagToken token = (CloseTagToken) WikipediaFilter.CLOSE_TAGS
							.get(closeTagString);
					if (token == null) {
						return WikipediaFilter.TokenNotFound;
					}
					if (!fTokenStack.isEmpty()) {
						Object topToken = fTokenStack.peek();
						if (topToken instanceof OpenTagToken
								&& ((OpenTagToken) topToken).getTagName()
										.equals(token.getTagName())) {
							fTokenStack.pop();

							copyWhite(
									fWhiteStartPosition, 3 + closeTagString
											.length());
							if (STOP_TAGS_SET.contains(closeTagString)) {
								reduceTokenStack();
							}
							handler.handleCloseTag(token.getTagName());

							return WikipediaFilter.TokenIgnore;
						}
					}
					copyWhite(fWhiteStartPosition,
							0);
					if (STOP_TAGS_SET.contains(closeTagString)) {
						reduceTokenStack();
					}
					return WikipediaFilter.TokenIgnore;
				} catch (NoSuchElementException e) {
					return WikipediaFilter.TokenNotFound;
				}

			} else {
				// start tag
				String tokenString;
				int tagNameStart = fCurrentPosition;
				int tokenLength = 0;
				while (StringUtil.isLetter(charAt(fCurrentPosition))) {
					fCurrentPosition++;
					tokenLength++;
				}
				try {
					tokenString = StringUtil.str(fSource, tagNameStart,
							fCurrentPosition - tagNameStart);

					OpenTagToken token = (OpenTagToken) WikipediaFilter.OPEN_TAGS
							.get(tokenString);
					if (token == null) {

						return WikipediaFilter.TokenNotFound;
					}

					copyWhite(fWhiteStartPosition,
							(fCurrentPosition - tagNameStart) + 1							);
					if (STOP_TAGS_SET.contains(tokenString)) {
						reduceTokenStack();
					}

					if (token instanceof SpecialTagToken) {

						while (StringUtil
								.isWhitespace(charAt(fCurrentPosition))) {
							fCurrentPosition++;
						}
						if (charAt(fCurrentPosition) == '/') {
							fCurrentPosition++;
						}
						if (charAt(fCurrentPosition) == '>') {
							fCurrentPosition++;
							fWhiteStartPosition = fCurrentPosition;
							// insert the special tag :
							handler.escape(token.getOpenTag());
							return WikipediaFilter.TokenIgnore;
						}

					} else {


						fCurrentPosition = token.scanHTMLAttributes(
								fSource, fCurrentPosition);

						if (tokenString.equals("ref")
								|| tokenString.equals("references")) {
							int start = fCurrentPosition;
							readUntilString("</ref>");
							fWhiteStart = false;
							handler.handleReference(token.attributes
									.get("name"),
									token.attributes.get("group"), start+1,
									fCurrentPosition - 6, fSource);

						} else {
							fTokenStack.push(token);

							handler.handleOpenTag(token, tagNameStart, fCurrentPosition);

						}

						return WikipediaFilter.TokenIgnore;
					}

					return WikipediaFilter.TokenNotFound;
				} catch (NoSuchElementException e) {
					return WikipediaFilter.TokenNotFound;
				}
			}
		} catch (TagParsingException e) {

			fTokenStack.pop();
		} catch (IndexOutOfBoundsException e) {

		}
		fCurrentPosition = currentHtmlPosition;
		return WikipediaFilter.TokenNotFound;
	}

	protected int getNextToken()  {
		fWhiteStartPosition = fCurrentPosition;
		fWhiteStart = true;
		try {

			while (fCurrentPosition <= fLastPosition) {
				fCurrentCharacter = charAt(fCurrentPosition++); // charAt(fCurrentPosition++);
				switch (fCurrentCharacter) {
				case '{':
					setPosition(fCurrentPosition - 1);
					WPBlock table = wpTable();
					if (table != null) {
						copyWhite(fWhiteStartPosition, 1);
						reduceTokenStack();
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

				case '=': // wikipedia header ?
					if (isStartOfLine()) {
						int levelHeader = getNumberOfChar('=') + 1;
						copyWhite(
								fWhiteStartPosition, levelHeader);
						int startHeadPosition = fCurrentPosition;

						if (readUntilString(WikipediaFilter.HEADER_STRINGS[levelHeader - 1])) {
							reduceTokenStack();
							String head = StringUtil
									.str(fSource, startHeadPosition,
											fCurrentPosition
													- startHeadPosition
													- (levelHeader));
							// head = copyWhite(head);

							boolean isEOF = readUntilEndOfsection(levelHeader);

							if (isEOF) {
								handler
										.handleSection(head, levelHeader,
												fSource.indexOf("\n",startHeadPosition)+1
														 ,
												fLastPosition, fSource);

								return WikipediaFilter.TokenEOF;
							}
							fCurrentPosition-=1;
							handler.handleSection(head, levelHeader,
									fSource.indexOf("\n",startHeadPosition),
									fCurrentPosition, fSource);

							continue;

						}

					}
					break;
				case '*': // <ul> list
				case '#': // <ol> list

					if (isStartOfLine()) {
						setPosition(fCurrentPosition - 2);
						WPList list = wpList(fCurrentCharacter);
						if (list != null && list.size() > 0) {
							copyWhite(									fWhiteStartPosition, 1);

							reduceTokenStack();
							fCurrentPosition = getPosition() - 1;
							handler.handleList(list);
							continue;
						}
					}
					break;
				case ':':
					if (isStartOfLine()) {

						copyWhite(								fWhiteStartPosition, 1);

						int levelHeader = getNumberOfChar(':') + 1;
						int startHeadPosition = fCurrentPosition;
						if (readUntilEOL()) {

							reduceTokenStack();
							String head = StringUtil.str(fSource,
									startHeadPosition, fCurrentPosition
											- startHeadPosition);

							handler.handleDefinition(head.trim(), levelHeader);

							continue;
						}
					}
					break;
				case ';':
					if (isStartOfLine()) {
						copyWhite(fWhiteStartPosition, 1);

						int startHeadPosition = fCurrentPosition;
						if (readUntilEOL()) {
							reduceTokenStack();
							// TODO not correct - improve this
							String head = StringUtil.str(fSource,
									startHeadPosition, fCurrentPosition
											- startHeadPosition);

							int index = head.indexOf(" : ");
							if (index > 0) {
								handler.handleDefinitionList(head);
							} else {
								index = head.indexOf(":");
								if (index > 0) {
									/*
									 * handler.append("<dl><dt>");
									 * handler.append
									 * (ArticleParser.filterParser(
									 * head.substring(0, index).trim(),
									 * fRecursionLevel));
									 * handler.append("</dt><dd>");
									 * handler.append
									 * (ArticleParser.filterParser(
									 * head.substring(index + 1).trim(),
									 * fRecursionLevel));
									 * handler.append("\n</dd></dl>");
									 */
								} else {
									/*
									 * handler.append("<dl><dt>");
									 * handler.append
									 * (ArticleParser.filterParser(head.trim(),
									 * fRecursionLevel));
									 * handler.append("&nbsp;</dt></dl>");
									 */
								}
							}
							continue;
						}
					}
					break;
				case '-':
					if (isStartOfLine()) {
						int tempCurrPosition = fCurrentPosition;
						try {
							if (charAt(tempCurrPosition++) == '-'
									&& charAt(tempCurrPosition++) == '-'
									&& charAt(tempCurrPosition++) == '-') {
								if (charAt(tempCurrPosition) == '\n') {
									copyWhite(fWhiteStartPosition, 4);
									reduceTokenStack();
									fCurrentPosition = tempCurrPosition;
									handler.handleHorizontalRule();
									fWhiteStart = false;
									continue;
								} else if (charAt(tempCurrPosition++) == '\r'
										&& charAt(tempCurrPosition++) == '\n') {
									copyWhite(
											fWhiteStartPosition, 6
											);
									reduceTokenStack();
									fCurrentPosition = tempCurrPosition - 1;
									handler.handleHorizontalRule();
									fWhiteStart = false;
									continue;
								}
							}
						} catch (IndexOutOfBoundsException e) {

						}
						fCurrentPosition = tempCurrPosition;
					}
					break;
				case ' ': // pre-formatted text?
				case '\t':
					if (isStartOfLine() && !isEmptyLine()) {
						if (fTokenStack.size() == 0
								|| !fTokenStack.get(0).equals(
										WikipediaFilter.HTML_PRE_OPEN)) {
							copyWhite(
									fWhiteStartPosition, 2);
							reduceTokenStack();
							handler.handleStart("pre");
							// handler.append(fCurrentCharacter);
							fTokenStack.push(WikipediaFilter.HTML_PRE_OPEN);
						}
						continue;
					}
					break;

				}

				if (isStartOfLine() && fRecursionLevel == 1) {

					if (fTokenStack.size() > 0
							&& fTokenStack.get(0).equals(
									WikipediaFilter.HTML_P_OPEN)) {
						if (isEmptyLine()) {
							copyWhite(fWhiteStartPosition, 2);
							reduceTokenStack();
						}
					} else {
						if (!isEmptyLine()) {
							copyWhite(fWhiteStartPosition, 2);
							reduceTokenStack();
							handler.newParagraph();
							fTokenStack.push(WikipediaFilter.HTML_P_OPEN);
						}
					}
				}

				// ---------Identify the next token-------------
				switch (fCurrentCharacter) {
				case '[':
					int startLinkPosition = fCurrentPosition;
					if (getNextChar('[')) { // wikipedia link style
						startLinkPosition = fCurrentPosition;
						copyWhite(fWhiteStartPosition, 2);

                            ArrayList<String> params=findWikiLinkEnd();
							if (params!=null) {
								handler.handleRawLink(params);

								continue;
							}

					} else {
						copyWhite(fWhiteStartPosition, 1);
						if (readUntilChar(']')) {
							String name = StringUtil.str(fSource,
									startLinkPosition, fCurrentPosition
											- startLinkPosition - 1);

							if (fUseBBCodes) {
								if (name.length() > 0) {
									StringBuffer bbCode = new StringBuffer(name
											.length());
									char ch = name.charAt(0);
									if ('a' <= ch && ch <= 'z') {
										// first character must be a letter
										bbCode.append(ch);
										if (handler.handleBBCode(name, bbCode)) {
											continue;
										}
									}
								}
							}
							// check bbcode end

							if (handler.handleHTTPLink(name)) {
								continue;
							}
							fCurrentPosition = startLinkPosition;
						}
					}
					break;

				  case '\'':
                      if (getNextChar('\'')) {
                              if (!fTokenStack.isEmpty()) {
                                      Object topToken = fTokenStack.peek();
                                      if (topToken instanceof AbstractTag && ((AbstractTag) topToken).getToken() == WikipediaFilter.TokenITALIC) {
                                              copyWhite(fWhiteStartPosition, 2);
                                              return WikipediaFilter.TokenITALIC;
                                      }
                              }
                              if (getNextChar('\'')) {
                                      copyWhite(fWhiteStartPosition, 3);
                                      return WikipediaFilter.TokenBOLD;
                              }
                              copyWhite(fWhiteStartPosition, 2);
                              return WikipediaFilter.TokenITALIC;
                      }
                      break;


				/*case 'h': // http(s)://
					int urlStartPosition = fCurrentPosition;
					boolean foundUrl = false;
					int diff = 7;
					try {
						String urlString = fStringSource.substring(
								fCurrentPosition - 1, fCurrentPosition + 3);
						if (urlString.equals("http")) {
							fCurrentPosition += 3;
							fCurrentCharacter = charAt(fCurrentPosition++);
							if (fCurrentCharacter == 's') { // optional
								fCurrentCharacter = charAt(fCurrentPosition++);
								diff++;
							}

							if (fCurrentCharacter == ':'
									&& charAt(fCurrentPosition++) == '/'
									&& charAt(fCurrentPosition++) == '/') {
								copyWhite( fWhiteStartPosition, diff);

								foundUrl = true;
								while (WikipediaFilter
										.isUrlIdentifierPart(charAt(fCurrentPosition++))) {
								}
							}
						}
					} catch (IndexOutOfBoundsException e) {
						if (!foundUrl) {
							// rollback work :-)
							fCurrentPosition = urlStartPosition;
						}
					}
					if (foundUrl) {
						String urlString = StringUtil.str(fSource,
								urlStartPosition - 1, fCurrentPosition
										- urlStartPosition);

						fCurrentPosition--;
						handler.createExternalLink(urlString, urlString);
						continue;
					}
					break;
*/
				case '&':
					//int ampersandStart = fCurrentPosition - 1;
					if (getNextChar('#')) {
						try {
							StringBuffer num = new StringBuffer(5);
							char ch = charAt(fCurrentPosition++);
							while (StringUtil.isDigit(ch)) {
								num.append(ch);
								ch = charAt(fCurrentPosition++);
							}
							if (num.length() > 0 && ch == ';') {
								Short i = Short.valueOf(num.toString());
								EntityTable table = EntityTable.getInstance();
								String name = table.entityName(i);

								copyWhite(
										fWhiteStartPosition, 3 + num.length());

								if (name != null) {
									handler.escape("&" + name + ";");

								} else {
									handler.escape("unknown");
									// StringUtil.append(handler.getResultBuffer(),
									// fSource, ampersandStart, fCurrentPosition
									// - ampersandStart);
								}
								continue;
							}
						} catch (IndexOutOfBoundsException e) {
							// ignore exception
						} catch (NumberFormatException e) {
							// ignore exception
						}
					} else {
						try {
							StringBuffer entity = new StringBuffer(10);
							entity.append('&');
							char ch = charAt(fCurrentPosition++);
							while (StringUtil.isLetterOrDigit(ch)) {
								entity.append(ch);
								ch = charAt(fCurrentPosition++);
							}
							if (entity.length() > 0 && ch == ';') {
								EntityTable table = EntityTable.getInstance();
								int code = table.entityCode(entity.toString());
								if (code != 0) {
									copyWhite(  fWhiteStartPosition,
											2 + entity.length() - 1 );

									handler.escape("&n.a.;");
									// StringUtil.append(handler.getResultBuffer(),
									// fSource, ampersandStart, fCurrentPosition
									// - ampersandStart);
									continue;
								}
							}
						} catch (IndexOutOfBoundsException e) {
							// ignore exception
						} catch (NumberFormatException e) {
							// ignore exception
						}
					}
					break;
				case '<':
                    int htmlStartPosition = fCurrentPosition;
                    try {
                        switch (fStringSource.charAt(fCurrentPosition)) {
                        case '!': // <!-- html comment -->

                            String htmlCommentString = fStringSource
                                    .substring(fCurrentPosition - 1,
                                            fCurrentPosition + 3);

                            if (htmlCommentString.equals("<!--")) {
                                //fCurrentPosition += 3;
                                if (readUntilString("-->")) {

                                    String htmlCommentContent = StringUtil
                                            .str(
                                                    fSource,
                                                    htmlStartPosition + 3,
                                                    fCurrentPosition
                                                            - htmlStartPosition
                                                            - 6);

                                    if (htmlCommentContent != null) {
                                          copyWhite(
                                                fWhiteStartPosition,
                                                fCurrentPosition
                                                        - htmlStartPosition
                                                        + 1 );

                                        handler
                                                .handleComment(htmlCommentContent);
                                        continue;
                                    }
                                }
                            }

                            break;
                        case 'n': // nowiki
                            if (fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 7)
                                    .equals("<nowiki>")) {
                                fCurrentPosition += 7;
                                if (readUntilString("</nowiki>")) {
                                    String nowikiContent = StringUtil.str(
                                            fSource, htmlStartPosition + 7,
                                            fCurrentPosition
                                                    - htmlStartPosition
                                                    - 16);

                                    if (nowikiContent != null) {
                                        copyWhite(
                                                fWhiteStartPosition,
                                                fCurrentPosition
                                                        - htmlStartPosition
                                                        + 1 );

                                        handler
                                                .handlePlainText(nowikiContent);
                                        continue;
                                    }
                                }
                            } else if (fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 10).equals(
                                    "<noinclude>")) {
                                fCurrentPosition += 10;
                                readUntilString("</noinclude>");
                                continue;

                            }

                            break;
                        case 'g': // gallery
                            String galleryString = fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 8);
                            if (galleryString.equals("<gallery>")) {
                                fCurrentPosition += 8;
                                if (readUntilString("</gallery>")) {
                                    String galleryContent = StringUtil.str(
                                            fSource, htmlStartPosition + 8,
                                            fCurrentPosition
                                                    - htmlStartPosition
                                                    - 15);
                                    copyWhite(
                                            fWhiteStartPosition,
                                            fCurrentPosition
                                                    - htmlStartPosition + 1 );
                                    fWhiteStart = false;

                                    handler.handleGallery(galleryContent);
                                    continue;
                                }
                            }
                            break;
                        case 'i': // image map
                            String mapString = fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 9);

                            if (mapString.equals("<imagemap>")) {
                                fCurrentPosition += 9;
                                if (readUntilString("</imagemap>")) {
                                    String mapContent = StringUtil.str(
                                            fSource, htmlStartPosition + 9,
                                            fCurrentPosition
                                                    - htmlStartPosition
                                                    - 16);
                                    handler.handleMap(mapContent);
                                    continue;
                                }
                            }
                            break;
                        case 'm': // math
                            String mathString = fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 5);

                            if (mathString.equals("<math>")) {
                                fCurrentPosition += 5;
                                if (readUntilString("</math>")) {
                                    String mathContent = StringUtil.str(
                                            fSource, htmlStartPosition + 5,
                                            fCurrentPosition
                                                    - htmlStartPosition
                                                    - 12);

                                    if (mathContent != null) {
                                        copyWhite(
                                                fWhiteStartPosition,
                                                fCurrentPosition
                                                        - htmlStartPosition
                                                        + 1 );
                                        fWhiteStart = false;
                                        handler.handleMathWiki(mathContent);
                                        continue;
                                    }
                                }
                            }
                            break;
                        case 's': // source
                            String sourceString = fStringSource.substring(
                                    fCurrentPosition - 1,
                                    fCurrentPosition + 7);

                            if (sourceString.equals("<source>")) {
                                fCurrentPosition += 7;
                                if (readUntilString("</source>")) {
                                    String sourceContent = StringUtil.str(
                                            fSource, htmlStartPosition + 7,
                                            fCurrentPosition
                                                    - htmlStartPosition
                                                    - 16);
                                    if (sourceContent != null) {
                                        copyWhite(
                                                fWhiteStartPosition,
                                                fCurrentPosition
                                                        - htmlStartPosition
                                                        + 1 );
                                        // <textarea> looks better if
                                        // JavaScript is enabled, <pre>
                                        // looks better if JavaScript is
                                        // disabled
                                        // handler.append("<pre name=\"code\" class=\"java\">");
                                        handler.copyMathLTGT(sourceContent);
                                        // handler.append("</pre>");
                                        continue;
                                    }
                                }
                            }
                            break;

                        }
                    } catch (IndexOutOfBoundsException e) {
                        // do nothing
                    }
                    fCurrentPosition = htmlStartPosition;
                    // detect special html tags
                    int htmlToken = getHTMLToken();
                    if (htmlToken == WikipediaFilter.TokenIgnore) {
                        continue;
                    }
                    fCurrentPosition = htmlStartPosition;
                    break;
				}
				if (!fWhiteStart) {
					fWhiteStart = true;
					fWhiteStartPosition = fCurrentPosition - 1;
				}

			}
			// -----------------end switch while try--------------------
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		try {
			copyWhite(  fWhiteStartPosition, 1 );
		} catch (IndexOutOfBoundsException e) {
			// end of scanner text
		}
		return WikipediaFilter.TokenEOF;
	}

	private boolean readUntilEndOfsection(int levelHeader) {

		int followingLevel = 100;
		while (levelHeader < followingLevel) {

			if (!readUntilString("\n==")) {

				return true;
			}
			followingLevel = getNumberOfChar('=')+2 ;

		}
		fCurrentPosition -= 3;
		return false;
	}

	/**
	 * read until character is found or end-of-line is reached
	 *
	 * @return -1 - for IndexOutOfBoundsException; 0 - for LF found; 1 - for
	 *         testedChar found
	 */
	private boolean readTag() {
		int temp = fCurrentPosition;
		try {
			if (!StringUtil.isLetter(charAt(fCurrentPosition++))) {
				fCurrentPosition = temp;
				return false;
			}
			while (StringUtil.isLetterOrDigit(charAt(fCurrentPosition++))) {
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			fCurrentPosition = temp;
			return false;
		}
	}

	public void parse() {
		parse(fCurrentPosition, fSource.length());
	}

	public void parse(int startPos, int stop) {
		int token;
		fCurrentPosition = startPos;
        int start = startPos;
		fLastPosition = stop;

			while (fCurrentPosition <= stop
					&& (token = getNextToken()) != WikipediaFilter.TokenEOF) {
				switch (token) {
				case WikipediaFilter.TokenBOLD:

					if (!fTokenStack.isEmpty()
							&& fTokenStack.peek() == WikipediaFilter.BOLD) {
						fTokenStack.pop();
						handler.stopBold();
					} else {
						fTokenStack.push(WikipediaFilter.BOLD);
						handler.startBold();
					}
					break;
				case WikipediaFilter.TokenITALIC:
					if (!fTokenStack.isEmpty()
							&& fTokenStack.peek() == WikipediaFilter.ITALIC) {
						fTokenStack.pop();
						handler.stopItalic();
					} else {
						fTokenStack.push(WikipediaFilter.ITALIC);
						handler.startItalic();
					}
					break;
				case WikipediaFilter.TokenSTRONG:
					if (!fTokenStack.isEmpty()
							&& fTokenStack.peek() == WikipediaFilter.STRONG) {
						fTokenStack.pop();
						handler.stopStrong();
					} else {
						fTokenStack.push(WikipediaFilter.STRONG);
						handler.startStrong();
					}
					break;
				case WikipediaFilter.TokenEM:
					if (!fTokenStack.isEmpty()
							&& fTokenStack.peek() == WikipediaFilter.EM) {
						fTokenStack.pop();
						handler.stopEm();
					} else {
						fTokenStack.push(WikipediaFilter.EM);
						handler.startEm();
					}
					break;
				case WikipediaFilter.TokenSTRIKETHROUGH:
					if (!fTokenStack.isEmpty()
							&& fTokenStack.peek() == WikipediaFilter.STRIKETHROUGH) {
						fTokenStack.pop();
						handler.stopStrikeThrough();
					} else {
						fTokenStack.push(WikipediaFilter.STRIKETHROUGH);
						handler.startStrikeThrough();
					}
					break;

				}
			}

		reduceTokenStack();
		handler.handleEOF();
	}

	/**
	 * @throws Exception
	 *
	 */
	private void reduceTokenStack() {
		if (fTokenStack.isEmpty()) {
			return;
		}
		// clear rest of stack if necessary (case of error in syntax!?)
		AbstractTag tok;
		while (!fTokenStack.isEmpty()) {
			tok = (AbstractTag) fTokenStack.pop();

			handler.handleEnd(tok.getTagName());

		}
	}

	protected final ArrayList<String> findWikiLinkEnd() {

		int level = 1;

		int lastposition=fCurrentPosition;
		boolean pipeSymbolFound = false;
		ArrayList<String> retval=new ArrayList<String>();
		  while (fCurrentPosition<fLastPosition) {

				if (fSource.indexOf("|", fCurrentPosition)==fCurrentPosition) {
					pipeSymbolFound = true;
					retval.add(fSource.substring(lastposition, fCurrentPosition));
					lastposition=fCurrentPosition;
				} else if (fSource.indexOf("[[", fCurrentPosition)==fCurrentPosition) {
					if (pipeSymbolFound) {
						level++;

					} else {
						return null;
					}
				} else if (fSource.indexOf("]]", fCurrentPosition)==fCurrentPosition) {

					if (--level == 0) {
						break;
					}
				}
				fCurrentPosition++;
			}

			retval.add(fSource.substring(lastposition, fCurrentPosition));
			fCurrentPosition+=2;
			return retval;

	}

}
