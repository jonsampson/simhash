/**
 * 
 */
package us.supernymo.tokenizer.test;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import us.supernymo.tokenizer.NGramTokenizerFactory;
import us.supernymo.tokenizer.TokenizerFactory;

/**
 * @author jsampson
 *
 */
public class NGramTokenizerTest {

	private TokenizerFactory tokenizer;
	
	private String testString;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.tokenizer = new NGramTokenizerFactory();
		this.testString = "1 B Street New York, NY";
	}

	/**
	 * Test method for {@link us.supernymo.tokenizer.NGramTokenizerFactory#getTokens(java.lang.String)}.
	 */
	@Test
	public void testGetTokens() {
		Iterator<String> tokens = this.tokenizer.getTokens(this.testString);
		while(tokens.hasNext()) {
			String token = tokens.next();
			System.out.print(token + "|");
		}
	}

}
