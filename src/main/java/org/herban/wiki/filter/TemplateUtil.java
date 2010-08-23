package org.herban.wiki.filter;

import java.util.ArrayList;
import java.util.List;

import org.herban.wiki.util.Utils;

public class TemplateUtil {
	public static StringBuffer replaceTemplateParameters(char[] fSource,
			WPTemplate template) {
		StringBuffer buffer = null;
		int bufferStart = 0;
		int fScannerPosition = 0;
		try {

			char ch;
			int parameterStart = -1;
			StringBuffer recursiveResult;
			while (true) {
				ch = fSource[fScannerPosition++];
				if (ch == '{' && fSource[fScannerPosition] == '{'
						&& fSource[fScannerPosition + 1] == '{') {
					fScannerPosition += 2;
					parameterStart = fScannerPosition;

					int temp[] = findNestedParamEnd(fSource, parameterStart);
					if (temp[0] >= 0) {
						fScannerPosition = temp[0];
						List<String> list = splitByPipe(fSource,
								parameterStart, fScannerPosition - 3, null);
						if (list.size() > 0) {
							String parameterString = list.get(0);
							String value = null;
							if (template != null) {
								value = template.paramValue(parameterString);
							}
							if (value == null && list.size() > 1) {
								value = list.get(1);
							}
							if (value != null) {
								if (buffer == null) {

									buffer = new StringBuffer();
								}
								if (bufferStart < fScannerPosition) {

									buffer.append(fSource, bufferStart,
											parameterStart - bufferStart - 3);
								}



								recursiveResult = replaceTemplateParameters(value.toCharArray(),
												template);
								if (recursiveResult != null) {

									buffer.append(recursiveResult);
								} else {

									buffer.append(value);
								}

								bufferStart = fScannerPosition;
							}
						}
						fScannerPosition = temp[0];
						parameterStart = -1;
					}
				}

			}
		} catch (IndexOutOfBoundsException e) {
			// ignore
		} finally {

		}
		if (buffer != null && bufferStart < fScannerPosition) {
			buffer.append(fSource, bufferStart, fScannerPosition - bufferStart
					- 1);
		}
		return buffer;
	}

	public static List<String> splitByPipe(char[] srcArray, int currOffset, int endOffset, List<String> resultList) {
		if (resultList == null) {
			resultList = new ArrayList<String>();
		}
		char ch;
		String value;
		int[] temp = new int[] { -1, -1 };
		;
		int lastOffset = currOffset;
		try {
			while (currOffset < endOffset) {
				ch = srcArray[currOffset++];
				if (ch == '[' && srcArray[currOffset] == '[') {
					currOffset++;
					temp[0] = findNestedEnd(srcArray, '[', ']', currOffset);
					if (temp[0] >= 0) {
						currOffset = temp[0];
					}
				} else if (ch == '{' && srcArray[currOffset] == '{') {
					currOffset++;
					if (srcArray[currOffset] == '{') {
						currOffset++;
						temp = findNestedParamEnd(srcArray, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						}
					} else {
						temp[0] = findNestedTemplateEnd(srcArray, currOffset);
						if (temp[0] >= 0) {
							currOffset = temp[0];
						}
					}
				} else if (ch == '|') {
					value = Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset - 1));
					resultList.add(value);
					lastOffset = currOffset;
				}
			}

			if (currOffset > lastOffset) {
				resultList.add(Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset)));
			} else if (currOffset == lastOffset) {
				resultList.add("");
			}
		} catch (IndexOutOfBoundsException e) {
			if (currOffset > lastOffset) {
				resultList.add(Utils.trimNewlineRight(new String(srcArray, lastOffset, currOffset - lastOffset)));
			} else if (currOffset == lastOffset) {
				resultList.add("");
			}
		}
		return resultList;
	}

	public static int findNestedEnd(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
		char ch;
		int level = 1;
		int position = startPosition;
		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == startCh && sourceArray[position] == startCh) {
					position++;
					level++;
				} else if (ch == endChar && sourceArray[position] == endChar) {
					position++;
					if (--level == 0) {
						break;
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	public static int findNestedTemplateEnd(final char[] sourceArray, int startPosition) {
		char ch;
		// int len = sourceArray.length;
		int countSingleOpenBraces = 0;
		int position = startPosition;
		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == '{') {
					// if (sourceArray[position] == '{') {
					// position++;
					// if ((len > position) && sourceArray[position] == '{') {
					// // template parameter beginning
					// position++;
					// int[] temp = findNestedParamEnd(sourceArray, position);
					// if (temp[0] < 0) {
					// position--;
					// temp[0] = findNestedTemplateEnd(sourceArray, position);
					// if (temp[0] < 0) {
					// return -1;
					// }
					// }
					// position = temp[0];
					// } else {
					// // template beginning
					// int temp = findNestedTemplateEnd(sourceArray, position);
					// if (temp < 0) {
					// return -1;
					// }
					// position = temp;
					// }
					// } else {
					countSingleOpenBraces++;
					// }
				} else if (ch == '}') {
					if (countSingleOpenBraces > 0) {
						countSingleOpenBraces--;
					} else {
						if (sourceArray[position] == '}') {
							// template ending
							position++;
							break;
						}
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	public static int[] findNestedParamEnd(final char[] sourceArray, int startPosition) {
		char ch;
		int len = sourceArray.length;
		int countSingleOpenBraces = 0;
		int parameterPosition = startPosition;

		try {
			while (true) {
				ch = sourceArray[parameterPosition++];
				if (ch == '{') {
					if (sourceArray[parameterPosition] == '{') {
						parameterPosition++;
						if ((len > parameterPosition) && sourceArray[parameterPosition] == '{') {
							// template parameter beginning
							parameterPosition++;
							int[] temp = findNestedParamEnd(sourceArray, parameterPosition);
							if (temp[0] >= 0) {
								parameterPosition = temp[0];
							} else {
								if (temp[1] >= 0) {
									parameterPosition = temp[1];
								} else {
									return new int[] { -1, -1 };
								}
							}

						} else {
							// template beginning
							int temp = findNestedTemplateEnd(sourceArray, parameterPosition);
							if (temp < 0) {
								return new int[] { -1, -1 };
							}
							parameterPosition = temp;
						}
					} else {
						countSingleOpenBraces++;
					}
				} else if (ch == '}') {
					if (countSingleOpenBraces > 0) {
						countSingleOpenBraces--;
					} else {
						if (sourceArray[parameterPosition] == '}') {
							if (sourceArray[parameterPosition + 1] == '}') {
								// template parameter ending
								return new int[] { parameterPosition + 2, -1 };
							} else {
								return new int[] { -1, parameterPosition + 1 };
							}
						}
					}

				}
			}
		} catch (IndexOutOfBoundsException e) {
			return new int[] { -1, -1 };
		}
	}

	public static int findStackedTemplateEnd(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
		char ch;
		int templateLevel = 0;
		int parameterLevel = 1;
		int len = sourceArray.length;
		int position = startPosition;

		try {
			while (true) {
				ch = sourceArray[position++];
				if (ch == startCh && sourceArray[position] == startCh) {
					position++;
					if ((len > position) && sourceArray[position] == startCh) {
						// template parameter beginning
						position++;
						parameterLevel++;
					} else {
						templateLevel++;
					}
				} else if (ch == endChar && sourceArray[position] == endChar) {
					position++;
					if ((templateLevel == 0) && (len > position) && sourceArray[position] == endChar) {
						// template parameter ending
						position++;
						if (--parameterLevel == 0) {
							break;
						}
					} else {
						templateLevel--;
					}
				}
			}
			return position;
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return -1;
		}
	}


}
