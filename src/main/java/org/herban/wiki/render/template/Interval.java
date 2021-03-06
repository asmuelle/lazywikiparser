package org.herban.wiki.render.template;

public class Interval {
  private int start;
  private int stop;
  private String name;
  private GenTemplate defaultValue;

public void setStart(int start) {
	this.start = start;
}
public int getStart() {
	return start;
}
public void setStop(int stop) {
	this.stop = stop;
}
public int getStop() {
	return stop;
}
public void setName(String name) {
	this.name = name;
}
public String getName() {
	return name;
}
public void setDefaultValue(GenTemplate defaultValue) {
	this.defaultValue = defaultValue;
}
public GenTemplate getDefaultValue() {
	return defaultValue;
}
public Interval(int start, int stop) {
	super();
	this.start = start;
	this.stop = stop;
}

}
