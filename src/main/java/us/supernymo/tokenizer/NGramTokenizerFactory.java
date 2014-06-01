package us.supernymo.tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 * @author jsampson
 */
public class NGramTokenizerFactory implements TokenizerFactory {
	
	private int largestNGram;
	
	public NGramTokenizerFactory() {
		this(3);
	}
	
	public NGramTokenizerFactory(int largestNGram) {
		this.largestNGram = largestNGram;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * us.supernymo.simhash.tokenizer.TokenizerFactory#getTokens(java.lang.String
	 * )
	 */
	@Override
	public Iterator<String> getTokens(String value) {
		NGramTokenizer gramTokenizer = new NGramTokenizer(Version.LUCENE_47, new StringReader(value), 1, largestNGram);
		CharTermAttribute charTermAttribute = gramTokenizer.addAttribute(CharTermAttribute.class);
		List<String> tokens = new ArrayList<String>();
		try {
			gramTokenizer.reset();
			while (gramTokenizer.incrementToken()) {
				tokens.add(charTermAttribute.toString());
			}
			gramTokenizer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return tokens.iterator();
	}

}
