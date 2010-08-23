package org.herban.wiki.util;

 
 
public class StringUtil {
	   public static String str(StringBuffer source, int offset, int count) {
           return source.substring(offset, offset+count);
//         return new String(source.toCharArray(), offset, count);
   }

   public static void append(StringBuffer handler, StringBuffer source, int offset, int count)   {
           handler.append(source.substring(offset, offset+count));
 
   }

   public static boolean isWhitespace(char ch) {
       return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
   }

   public static boolean isLetter(char ch) {
       return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
   }

   public static boolean isDigit(char ch) {
       return ch >= '0' && ch <= '9';
   }

   public static boolean isLetterOrDigit(char ch) {
       return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
               || (ch >= '0' && ch <= '9');
   }

   

   public static String replace(String source, String target,
                   String replacement) {
           StringBuffer result = new StringBuffer();
           int index = source.indexOf(target);
           int lastIndex = 0;
           if (index >= 0) {
                   result.append(source.substring(lastIndex, index));
                   result.append(replacement);
                   lastIndex = index + target.length();
           }
           if (lastIndex < source.length()) {
                   result.append(source.substring(lastIndex));
           }
           return result.toString();
   }

   public static String replaceAll(String source, String target,
                   String replacement) {
           StringBuffer result = new StringBuffer();
           int index = source.indexOf(target);
           int lastIndex = 0;
           while (index >= 0) {
                   result.append(source.substring(lastIndex, index));
                   result.append(replacement);
                   lastIndex = index + target.length();
                   index = source.indexOf(target, lastIndex);
           }
           if (lastIndex < source.length()) {
                   result.append(source.substring(lastIndex));
           }
           return result.toString();
   }

   public static void arraycopy(StringBuffer source, int srcPos, char[] sequence,
                   int dstPos, int length) {
           int j = dstPos;
           for (int i = srcPos; i < (srcPos + length); i++) {
                   sequence[j++] = source.charAt(i);
           }
   }

   public static void arraycopy(String source, int srcPos, char[] sequence,
                   int dstPos, int length) {
           int j = dstPos;
           for (int i = srcPos; i < (srcPos + length); i++) {
                   sequence[j++] = source.charAt(i);
           }
   }

   

   /**
    * Copy the text into the resulting buffer and escape special html
    * characters (&lt; &gt; &quot; &amp; &#39;)
    *
    * @param buffer
    *            add converted text into the resulting buffer
 * @throws Exception 
    */
   public static void escape(String text, StringBuffer buffer)   {
           final int len = text.length();
           int currentIndex = 0;
           int lastIndex = currentIndex;
           while (currentIndex < len) {
                   switch (text.charAt(currentIndex++)) {
                   case '\r': //  
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           }
                           break;
                   case '<': // special html escape character
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           }
                           buffer.append("&lt;");
                           break;
                   case '>': // special html escape character
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           } else {
                                   lastIndex++;
                           }
                           buffer.append("&gt;");
                           break;
                   case '&': // special html escape character
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           } else {
                                   lastIndex++;
                           }
                           buffer.append("&amp;");
                           break;
                   case '\'': // special html escape character
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           } else {
                                   lastIndex++;
                           }
                           buffer.append("&#39;");
                           break;
                   case '\"': // special html escape character
                           if (lastIndex < (currentIndex - 1)) {
                                   buffer.append(text.substring(lastIndex, currentIndex - 1));
                                   lastIndex = currentIndex;
                           } else {
                                   lastIndex++;
                           }
                           buffer.append("&quot;");
                           break;
                   }
           }
           if (lastIndex < (currentIndex)) {
                   buffer.append(text.substring(lastIndex, currentIndex));
           }
   }

public static String escape(String name)   {
	StringBuffer retVal=new StringBuffer();
	escape(name, retVal);
	return retVal.toString();
}

 
 
	
}


