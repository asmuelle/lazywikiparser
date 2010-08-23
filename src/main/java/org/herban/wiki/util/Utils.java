package org.herban.wiki.util;

public class Utils {

	   /**
     * Trims specified string from left and right and stops at <code>\n</code>
     * character on the right
     *
     * @param s
     */
    public static String trimNewlineRight(String s) {
            if (s == null) {
                    return null;
            }
            int leftIndex = 0;
            int len = s.length();

            while (leftIndex < len && " ".equals(s.charAt(leftIndex))) {

                    leftIndex++;
            }
            if (leftIndex >= len) {
                    return "";
            }
            // return (leftIndex >= len) ? "" : s.substring(leftIndex);

            int rightIndex = len;

            while (rightIndex > 0 && " ".equals(s.charAt(rightIndex - 1))) {
                    rightIndex--;
                    if (s.charAt(rightIndex) == '\n') {
                            rightIndex++;
                            break;
                    }
            }
            if (rightIndex <= 0) {
                    return "";
            }
            if ((leftIndex == 0) && rightIndex == len) {
                    return s;
            }
            return s.substring(leftIndex, rightIndex);

    }


}
