package org.herban.wiki;

import java.util.HashMap;
import java.util.Map;



public class WordListBuilder extends BaseHandler{
	private Map<String, Integer> words;
 	public Map<String, Integer> getWords() {
		return words;
	}









	public WordListBuilder(StringBuffer source) {
		super(null, source);
		// TODO Auto-generated constructor stub
	}









	public void setWords(Map<String, Integer> words) {
		this.words = words;
	}













	@Override
	public void handlePlainText(String text) {
		System.out.println(text);
		for (String word:text.split("[._/ ]")) {
			addWord(word.toUpperCase());
		}
	}



	private void addWord(String word){
		Integer wordCount=words.get(word);
		if (wordCount==null) {
		  wordCount=new Integer(1);

		} else {
		  wordCount++;
		}
		words.put(word, wordCount);
	}




	public WordListBuilder( ) {
        super(null, null);
		words=new HashMap<String, Integer>();

	}
}
