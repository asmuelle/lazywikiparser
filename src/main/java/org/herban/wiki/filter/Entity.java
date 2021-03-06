package org.herban.wiki.filter;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 04.03.2009
 * Time: 09:41:56
 * To change this template use File | Settings | File Templates.
 */

/**
 * HTML ISO entity.
 *
 * @author Dave Raggett <a href="mailto:dsr@w3.org">dsr@w3.org </a>
 * @author Andy Quick <a
 *         href="mailto:ac.quick@sympatico.ca">ac.quick@sympatico.ca </a>
 *         (translation to Java)
 * @author Fabrizio Giustina
 * @version $Revision: 1.2 $ ($Author: axelcl $)
 */
public class Entity {

    /**
     * entity name.
     */
    private final String name;

    /**
     * entity code.
     */
    private final short code;

    /**
     * instantiates a new entity.
     *
     * @param name entity name
     * @param code entity code (will be casted to short)
     */
    public Entity(String name, int code) {
        this.name = name;
        this.code = (short) code;
    }

    /**
     * Getter for <code>code</code>.
     *
     * @return Returns the code.
     */
    public short getCode() {
        return this.code;
    }

    /**
     * Getter for <code>name</code>.
     *
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
    }


}
