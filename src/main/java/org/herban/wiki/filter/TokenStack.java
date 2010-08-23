package org.herban.wiki.filter;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 13:11:19
 * To change this template use File | Settings | File Templates.
 */


import java.util.ArrayList;

import org.herban.wiki.filter.tags.AbstractTag;



public class TokenStack extends ArrayList {

    private static final long serialVersionUID = 7377721039394435077L;

    public TokenStack() {
        super();
    }

    
    /**
     * @return
     */
    public AbstractTag peek() {
        return (AbstractTag) get(size() - 1);
    }

    /**
     * @return
     */
    public Object pop() {
        return remove(size() - 1);
    }

    /**
     * @param item
     * @return
     */
    public boolean push(AbstractTag item) {
        return add(item);
    }

}

