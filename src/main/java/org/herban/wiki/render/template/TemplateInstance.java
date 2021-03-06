package org.herban.wiki.render.template;

import java.util.ArrayList;
import java.util.List;

import org.herban.wiki.Handler;
import org.herban.wiki.TemplateProvider;
import org.herban.wiki.WikipediaParser;
import org.herban.wiki.filter.WPBlock;
import org.herban.wiki.filter.WPFunction;
import org.herban.wiki.filter.WPParam;
import org.herban.wiki.filter.WPTemplate;
import org.herban.wiki.util.StringUtil;

public class TemplateInstance implements GenTemplate {
	protected final StringBuffer fSource;
	private int fScannerPosition;
	private final int fLastPosition;
	private final int start;
    private final TemplateProvider templateProvider;
	public TemplateInstance(StringBuffer buf, int start, int stop, TemplateProvider templateProvider) {
		this.fSource = buf;
		this.templateProvider=templateProvider;
		this.start = start;
		this.fLastPosition = stop;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.herban.wiki.render.template.GenTemplate#replaceParameters
	 * (org.herban.wiki.filter.WPTemplate,
	 * org.herban.wiki.Handler)
	 */
	public void replaceParameters(WPTemplate template, Handler handler) throws Exception {
	 	StringBuffer tempBuf = replaceParameters(template);
		String tmp=tempBuf.toString();
		tmp=tmp.replaceAll("\\{\\{!\\}\\}", "|");
		tmp=tmp.replaceAll("\\{\\{!!\\}\\}", "||");
   	    tmp=tmp.replaceAll("\\{\\{!-\\}\\}", "|-");
		tmp=tmp.replaceAll("\\{\\{!+\\}\\}", "|+");
        System.out.println(tmp);

		WikipediaParser stage2 = new WikipediaParser(new StringBuffer(tmp), handler);
		stage2.parse();
	}

	public StringBuffer replaceParameters(WPTemplate template) throws Exception {
	 	if (start>=fLastPosition) return new StringBuffer("");
		fScannerPosition = fSource.indexOf("{{", start);

		StringBuffer buffer = new StringBuffer();

		int bufferStart = start;

		try {

			while (fScannerPosition > -1 && fScannerPosition <= fLastPosition) {

				fScannerPosition = fSource.indexOf("{{", fScannerPosition);
				if (fScannerPosition > -1) {
					if (fSource.indexOf("{{{", fScannerPosition) == fScannerPosition&& fScannerPosition+3<fLastPosition) {
						fScannerPosition+=3;
						StringBuffer tempBuf = handleParam(bufferStart,
								fScannerPosition, template);
						if (tempBuf != null)
							buffer.append(tempBuf);
					    fScannerPosition+=3 ;
						bufferStart = fScannerPosition--;

					} else if (fSource.indexOf("{{#if:", fScannerPosition) == fScannerPosition&& fScannerPosition+6<fLastPosition) {

						buffer.append(fSource, bufferStart, fScannerPosition);
                        fScannerPosition+=6;

						buffer.append(handleFunctionIf(bufferStart,
								fScannerPosition, template));

						fScannerPosition+=2 ;
						bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{#ifeq:", fScannerPosition) == fScannerPosition&& fScannerPosition+8<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						fScannerPosition+=8;
						buffer.append(handleFunctionIfeq(bufferStart,
								fScannerPosition, template));
						fScannerPosition += 2;
						bufferStart = fScannerPosition--;

					} else if (fSource.indexOf("{{#switch:", fScannerPosition) == fScannerPosition&& fScannerPosition+10<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						fScannerPosition+=10;
						buffer.append(handleFunctionSwitch(bufferStart,
								fScannerPosition , template));
						fScannerPosition +=2;
						bufferStart = fScannerPosition--;

					} else if (fSource.indexOf("{{NAMESPACE}}",
							fScannerPosition) == fScannerPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						buffer.append("PAGE");
						fScannerPosition += 13;
						bufferStart = fScannerPosition--;



					} else if (fSource.indexOf("{{uc:", fScannerPosition) == fScannerPosition&& fScannerPosition+5<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						int end = findNestedTemplateEnd(fScannerPosition+5 );
						String value = new TemplateInstance(fSource,
								fScannerPosition + 5, end, templateProvider).replaceParameters(
								template).toString();

						buffer.append(value.toUpperCase());

						fScannerPosition = end + 2;
						bufferStart = fScannerPosition;
					} else if (fSource.indexOf("{{lc:", fScannerPosition) == fScannerPosition&& fScannerPosition+5<fLastPosition) {
							buffer.append(fSource, bufferStart, fScannerPosition);
							int end = findNestedTemplateEnd(fScannerPosition + 5);
							String value = new TemplateInstance(fSource,
									fScannerPosition + 5, end, templateProvider).replaceParameters(
									template).toString();

							buffer.append(value.toUpperCase());

							fScannerPosition = end ;
							bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{formatnum:", fScannerPosition) == fScannerPosition&& fScannerPosition+5<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						int end = findNestedTemplateEnd(fScannerPosition + 5);
						String value = new TemplateInstance(fSource,
								fScannerPosition + 12, end,templateProvider).replaceParameters(
								template).toString();

						buffer.append(value );

						fScannerPosition = end + 2;
						bufferStart = fScannerPosition;
					} else if (fSource.indexOf("{{#tag:", fScannerPosition) == fScannerPosition&& fScannerPosition+5<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						int end = findNestedTemplateEnd(fScannerPosition + 5);
						String value = new TemplateInstance(fSource,
								fScannerPosition + 5, end,templateProvider).replaceParameters(
								template).toString();

						//buffer.append(value.toUpperCase());

						fScannerPosition = end + 2;
						bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{#expr:", fScannerPosition) == fScannerPosition&& fScannerPosition+8<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);
						int end = findNestedTemplateEnd(fScannerPosition + 8);
						String value = new TemplateInstance(fSource,
								fScannerPosition + 8, end,templateProvider).replaceParameters(
								template).toString();

						buffer.append(value.toUpperCase());

						fScannerPosition = end + 2;
						bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{NAMESPACE}}", fScannerPosition) == fScannerPosition&& fScannerPosition+8<fLastPosition) {
						buffer.append("PAGE");

						fScannerPosition += 13;
						bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{ns:0}}", fScannerPosition) == fScannerPosition&& fScannerPosition+8<fLastPosition) {
							buffer.append("PAGE");

							fScannerPosition += 13;
							bufferStart = fScannerPosition--;
					} else if (fSource.indexOf("{{!", fScannerPosition) == fScannerPosition&& fScannerPosition+2<fLastPosition) {

					} else if (fSource.indexOf("{{", fScannerPosition) == fScannerPosition&& fScannerPosition+2<fLastPosition) {
						buffer.append(fSource, bufferStart, fScannerPosition);

						fScannerPosition+=2;
						int templateEnd=findNestedTemplateEnd(fScannerPosition);

						buffer.append(handleTemplate(templateEnd));
						fScannerPosition=templateEnd+2;
						bufferStart=fScannerPosition--;
					}
					  fScannerPosition++;
				}

			}




		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		if (buffer != null && bufferStart < fLastPosition) {
			buffer.append(fSource, bufferStart, Math.min(fLastPosition, fSource.length() ));
		}

		return buffer;
	}

	private StringBuffer handleTemplate(int templateEnd) throws Exception {
		WPTemplate template=createTemplate();
		try {
		TemplateInstance templateInstance=(TemplateInstance) this.templateProvider.getTemplate(template);
		return templateInstance.replaceParameters(template);
		} catch (Exception e) {
			return new StringBuffer(" NNN ");
		}

	}

	protected WPTemplate createTemplate() {


        int startPos = fScannerPosition;


        WPTemplate table = null;

        char ch;
        int paramStart = startPos;
        int nestingLevel = 0;
        int params = 1;
        while (true) {
            ch = fSource.charAt(fScannerPosition++);
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

                            	table = new WPTemplate(tname, fScannerPosition);

                            paramStart = fScannerPosition;
                        } else {
                            WPParam param = new WPParam(fSource, paramStart, fScannerPosition-1, StringUtil.str(fSource, paramStart, fScannerPosition - 1 - paramStart).trim(), params++);

                              table.addParam(param);
                            paramStart = fScannerPosition;
                        }
                    }
                    break;
                case '}':
                    if (fSource.charAt(fScannerPosition) == '}') {
                        if (table == null) {
                        	 String tname = StringUtil.str(fSource, startPos, fScannerPosition - 1 - startPos).trim();

                             	table = new WPTemplate(tname, fScannerPosition);

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
                    if (fSource.charAt(fScannerPosition) == '{') {

                        fScannerPosition = indexEndOfNested("{{","}}");

                    } else if (fSource.charAt(fScannerPosition) == '|') {
                        if (table!=null)
                    	table.setNested(true);
                        fScannerPosition = indexEndOfNested("\n{|", "\n|}");


                    }
                    break;


            }
        }
    }

	private StringBuffer handleFunctionIf(int bufferStart, int parameterStart,
			WPTemplate template) throws Exception {
		int paramEnd = findNestedTemplateEnd(parameterStart);

		StringBuffer buffer = null;
		if (paramEnd >= 0) {
			fScannerPosition = paramEnd;
			List<Interval> list = new ArrayList<Interval>();

			splitByPipe(parameterStart, paramEnd, list);
			if (list.size() > 0) {
				String condString = new TemplateInstance(fSource, list.get(0)
						.getStart(), list.get(0).getStop(),templateProvider).replaceParameters(
						template).toString().trim();
				String value = "";
				if (condString != null && condString.length() > 0
						&& list.size() > 1) {
					try {
						value =  new TemplateInstance(fSource, list.get(1)
								.getStart(), list.get(1).getStop(),templateProvider )
								.replaceParameters(template).toString();

					} catch (Exception e) {

						value =  fSource.substring(list.get(1).getStart(), list
								.get(1).getStop() );
					}
				} else if (list.size() == 3) {
					try {

						value =  new TemplateInstance(fSource, list.get(2)
								.getStart(), list.get(2).getStop(),templateProvider )
								.replaceParameters(template).toString();

					} catch (Exception e) {

						value =  fSource.substring(list.get(2).getStart(), list
								.get(2).getStop() );
					}

				}
				if (value != null) {
					if (buffer == null) {
						buffer = new StringBuffer();
					}

					buffer.append(value);

				}
			}
			fScannerPosition = list.get(list.size()-1).getStop();

		}
		return buffer;
	}

	private StringBuffer handleParam(int bufferStart, int parameterStart,
			WPTemplate template) {
		int paramEnd = findNestedTemplateEnd(parameterStart);
		StringBuffer buffer = null;
		if (paramEnd >= 0) {
			fScannerPosition = paramEnd;
			List<Interval> list = new ArrayList<Interval>();

			splitByPipe(parameterStart, paramEnd, list);
			if (list.size() > 0) {

				String parameterString = fSource.substring(list.get(0)
						.getStart(), list.get(0).getStop());
				String value = null;

				value = template.paramValue(parameterString);

				if (value == null && list.size() > 1) {
					try {
						value = new TemplateInstance(fSource, list.get(1)
								.getStart(), list.get(1).getStop(),templateProvider)
								.replaceParameters(template).toString();
					} catch (Exception e) {

						value = fSource.substring(list.get(1).getStart(), list
								.get(1).getStop());
					}
				} else if(value.contains("{{")) {
					try {
						value = new TemplateInstance(new StringBuffer(value), 0, value.length(),templateProvider)
								.replaceParameters(template).toString();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (value != null) {
					if (buffer == null) {
						buffer = new StringBuffer( /* todo */128);
					}
					if (bufferStart < fScannerPosition) {
						try {

							buffer.append(fSource, bufferStart,
									parameterStart - 3);

						} catch (Exception e) {

						}
					}
					buffer.append(value);

				}
			}
			fScannerPosition =  list.get(list.size()-1).getStop();

		}
		return buffer;
	}

	public final int findNestedEnd(final char startCh, final char endChar,
			int startPosition) {

		int level = 1;
		int position = startPosition;

		while (position < fLastPosition) {

			if (fSource.charAt(position)==startCh) {
				position++;
				level++;
			} else if (fSource.charAt(position)==endChar) {
				position++;
				if (--level == 0) {
					break;
				}
			} else
			position++;
		}
		return Math.min(position-1, fLastPosition);

	}

	private final int findNestedTemplateEnd(int startPosition) {
		return findNestedEnd('{', '}', startPosition);

	}



	public void splitByPipe(int paramsStart, int paramsStop,
			List<Interval> resultList) {

		int lastOffset = paramsStart;
		int currOffset = lastOffset;

		try {
			while (currOffset < paramsStop) {

				if (fSource.indexOf("[[", currOffset) == currOffset) {
					currOffset = findNestedEnd('[', ']', currOffset + 2);

				} else if (fSource.indexOf("{{{", currOffset) == currOffset) {
					currOffset = findNestedTemplateEnd(currOffset + 3);

				} else if (fSource.indexOf("{{", currOffset) == currOffset) {
					currOffset = findNestedTemplateEnd(currOffset + 2);

				} else if (fSource.indexOf("|", currOffset) == currOffset) {

					resultList.add(new Interval(Math.max(paramsStart,lastOffset), Math.min(paramsStop,currOffset)));
 					lastOffset = ++currOffset;
				} else
				currOffset++;
			}

		} finally {

				resultList.add(new Interval(Math.max(paramsStart,lastOffset), Math.min(paramsStop,currOffset)));


		}

	}



	private StringBuffer handleFunctionSwitch(int bufferStart,
			int parameterStart, WPTemplate template) throws Exception {
		int paramEnd = findNestedTemplateEnd(parameterStart);

		StringBuffer buffer = null;
		if (paramEnd >= 0) {

			List<Interval> list = new ArrayList<Interval>();
			splitByPipe(parameterStart, paramEnd, list);
			if (list.size() > 0) {
				String condString = new TemplateInstance(fSource, list.get(0)
						.getStart(), list.get(0).getStop(),templateProvider).replaceParameters(
						template).toString().trim();
				String value = "";
				if (condString != null && condString.length() > 0
						&& list.size() > 1) {

					for (Interval interval : list) {
						String line = fSource.substring(interval.getStart(),
								interval.getStop());
						if (line.split("=")[0].trim().equals(condString)) {
							value = line.split("=")[1];
						}
					}

				}
				if (value != null) {
					if (buffer == null) {
						buffer = new StringBuffer();
					}

					buffer.append(value.replaceAll("\n"," "));

				}
			}
			fScannerPosition = paramEnd;

		}
		if (fScannerPosition>fLastPosition)
			throw new Exception("Template overrun");
		return buffer;
	}

	private StringBuffer handleFunctionIfeq(int bufferStart,
			int parameterStart, WPTemplate template) throws Exception {
		int paramEnd = findNestedTemplateEnd(parameterStart);

		StringBuffer buffer = null;
		if (paramEnd >= 0) {

			List<Interval> list = new ArrayList<Interval>();
			splitByPipe(parameterStart, paramEnd, list);

			if (list.size() > 2) {
				String condString1 = new TemplateInstance(fSource, list.get(0)
						.getStart(), list.get(0).getStop(),templateProvider).replaceParameters(
						template).toString().trim();
				String condString2 = new TemplateInstance(fSource, list.get(1)
						.getStart(), list.get(1).getStop(),templateProvider).replaceParameters(
						template).toString().trim();

				String value = "";
				if (condString1.equalsIgnoreCase(condString2)) {
					try {
						value = new TemplateInstance(fSource, list.get(2)
								.getStart(), list.get(2).getStop(),templateProvider)
								.replaceParameters(template).toString();
					} catch (Exception e) {

						value = fSource.substring(list.get(2).getStart(), list
								.get(2).getStop());
					}
				} else if (list.size() == 4) {
					try {
						value = new TemplateInstance(fSource, list.get(3)
								.getStart(), list.get(3).getStop(),templateProvider)
								.replaceParameters(template).toString();
					} catch (Exception e) {

						value = fSource.substring(list.get(3).getStart(), list
								.get(3).getStop());
					}

				}
				if (value != null) {
					if (buffer == null) {
						buffer = new StringBuffer();
					}

					buffer.append(value);

				}
			}
			fScannerPosition = paramEnd;

		}
		return buffer;
	}

	 public int indexEndOfNested(String start,String stop) {
	        // check nowiki and html comments?
	        int count = 1;

	        try {
	            while (true) {

	                if (fSource.indexOf("<!--", fScannerPosition)==fScannerPosition) {
	                    fScannerPosition = indexEndOfComment();
	                    if (fScannerPosition == (-1)) {
	                        return -1;
	                    }
	                } else if (fSource.indexOf("<nowiki>", fScannerPosition)==fScannerPosition) {
	                    fScannerPosition = indexEndOfNowiki();
	                    if (fScannerPosition == (-1)) {
	                        return -1;
	                    }
	                } else if (fSource.indexOf(stop,fScannerPosition)==fScannerPosition) {
	                    count--;
	                    if (count == 0) {
	                        return fScannerPosition + 2;
	                    }
	                } else if (fSource.indexOf(start,fScannerPosition)==fScannerPosition) {

	                    count++;
	                }
	                fScannerPosition++;
	            }
	        } catch (IndexOutOfBoundsException e) {
	            // ..
	        }
	        return -1;
	    }
	  public int indexEndOfNowiki() {

	        try {
	            while (fSource.indexOf("</nowiki>", fScannerPosition)!=fScannerPosition) {
	                fScannerPosition++;

	            }
	            return fScannerPosition + 9;

	        } catch (IndexOutOfBoundsException e) {
	            // ..
	        }
	        return -1;
	    }

	  public int indexEndOfComment() {

	        try {
	            while (fSource.indexOf("-->", fScannerPosition)!=fScannerPosition) {
	                fScannerPosition++;

	            }
	            return fScannerPosition + 2;

	        } catch (IndexOutOfBoundsException e) {
	            // ..
	        }
	        return -1;
	    }

}
