package us.supernymo.simhash;

import java.security.MessageDigest;
import java.util.BitSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * As SimHashFactory, but also maintains a map of token values to associate BitSets
 * to speed up calculation.
 * 
 * @author jsampson
 */
public class CachingSimHashFactory extends SimHashFactory {
	
	/**
	 * Token to associated calculated BitSet
	 */
	protected ConcurrentMap<String, BitSet> tokenToBitSet;
	
	public CachingSimHashFactory(String algorithm) {
		super(algorithm);
		tokenToBitSet = new ConcurrentHashMap<String, BitSet>();
	}

	@Override
	protected BitSet getTokenBitSet(MessageDigest messageDigest, String token) {		
		BitSet toReturn = null;
		if(tokenToBitSet.containsKey(token)) {
			toReturn = tokenToBitSet.get(token);
		}
		else {
			toReturn = super.getTokenBitSet(messageDigest, token);
			tokenToBitSet.put(token, toReturn);
		}
		return toReturn;
	}
}
