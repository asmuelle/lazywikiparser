package org.herban.wiki.filter.tags;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 10:20:32
 * To change this template use File | Settings | File Templates.
 */
public class ListToken extends AbstractTag {
    final int fLevel;

    public ListToken(int token, int level) {
        super(token);
        fLevel = level;
    }

    public int getLevel() {
        return fLevel;
    }
}
 
