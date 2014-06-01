/**
 * 
 */
package us.supernymo.tokenizer;

import java.util.Iterator;

/**
 * 
 * @author jsampson
 *
 */
public interface TokenizerFactory {
	/**
	 * @param value
	 * @return
	 */
	Iterator<String> getTokens(String value);
}
