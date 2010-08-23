package org.herban.wiki.filter;

/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 12.03.2009
 * Time: 13:52:47
 * To change this template use File | Settings | File Templates.
 */
public class WPParam extends WPBlock{
    private String markup;
    public String getMarkup() {
		return markup;
	}

	public void setMarkup(String markup) {
		this.markup = markup;
	}

    private String name;
    private String value;

    public WPParam(StringBuffer source, int paramStart, int stop, String markup, int number) {
        super(source, paramStart);
        this.stop=stop;
    	this.markup = markup;
        int number1 = number;
        int i = markup.indexOf("=");

        if (i == -1) {
            setName("" + number);
            value = markup;
        } else {
            setName(markup.substring(0, i).trim());
            value = markup.substring(i + 1);
        }



        value = value.trim();


    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void addParam(WPParam param) {
		// TODO Auto-generated method stub

	}






}
