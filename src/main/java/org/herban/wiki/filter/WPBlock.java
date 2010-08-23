package org.herban.wiki.filter;



/**
 * Created by IntelliJ IDEA.
 * User: MUAN104
 * Date: 12.03.2009
 * Time: 09:33:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class WPBlock {
    private final StringBuffer source;
	public StringBuffer getSource() {
		return source;
	}



	public WPBlock(StringBuffer source, int position) {
		super();
		this.source = source;
		this.position = position;
	}



	boolean nested = false;
	protected final int position;
	protected int stop=0;

	public void setStop(int stop) {
		this.stop = stop;
	}

	public boolean isNested() {

		return nested;
	}

	public void setNested(boolean nested) {
		this.nested = nested;
	}




	public int getPosition() {
		return position;
	}

	public int getStop() {
		return stop;
	}

	public abstract void addParam(WPParam param) ;



}
