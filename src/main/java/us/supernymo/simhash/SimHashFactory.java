package us.supernymo.simhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;


/**
 * A thread-safe implementation for generating SimHashes.
 * 
 * @author jsampson
 */
public class SimHashFactory {

	/**
	 * Returns a string representation of a BitSet with each bit displayed as 0
	 * or 1.
	 * 
	 * Example:
	 * 
	 * BitSet totalBS = new BitSet(4);
	 * 
	 * now this method would return: 0000
	 * 
	 * totalBS.set(2);
	 * 
	 * now this method would return: 0100
	 * 
	 * @param bitSet
	 * @return
	 */
	public static String toPrettyString(BitSet bitSet) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitSet.size(); i++) {
			if (bitSet.get(i)) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		return sb.toString();
	}

	/**
	 * Algorithm used to generate a MessageDigest object
	 */
	private String algorithm;

	/**
	 * Initialize the factory with an algorithm supported by MessageDigest
	 * 
	 * @param algorithm
	 */
	public SimHashFactory(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Create an array whose positive and negative index values become the basis
	 * for the SimHash
	 * 
	 * @param tokens
	 *            tokens to create a fingerprint from
	 * @return the variegated array
	 */
	private long[] getFingerprint(MessageDigest messageDigest, int bitLength, Iterator<String> tokens) {
		long[] toReturn = initFingerprint(bitLength);
		while (tokens.hasNext()) {
			BitSet tokenBitSet = getTokenBitSet(messageDigest, tokens.next());
			for (int i = 0; i < bitLength; i++) {
				toReturn[i] = tokenBitSet.get(i) ? toReturn[i] + 1 : toReturn[i] - 1;
			}
		}
		return toReturn;
	}

	/**
	 * @param tokens The tokens to use in creating a SimHash
	 * @return a SimHash of the given tokens represented in a BitSet
	 * @throws NoSuchAlgorithmException the Factory was constructed with an unsupported algorithm.
	 */
	public BitSet getSimHash(Iterator<String> tokens) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(this.algorithm);
		int bitLength = messageDigest.getDigestLength() * 8;
		BitSet toReturn = new BitSet(bitLength);
		long[] fingerprint = getFingerprint(messageDigest, bitLength, tokens);
		for (int i = 0; i < bitLength; i++) {
			toReturn.set(i, fingerprint[i] >= 0 ? true : false);
		}
		return toReturn;
	}

	/**
	 * This method digests a string token with, then resets the
	 * <code>messageDigest</code>
	 * 
	 * @param messageDigest
	 *            used to create the return value
	 * @param token
	 *            a token to obtain a bitset for
	 * @return BitSet valueOf the byte array produced by
	 *         <code>messageDigest</code>
	 */
	protected BitSet getTokenBitSet(MessageDigest messageDigest, String token) {
		byte[] tokenHash = messageDigest.digest(token.getBytes());
		messageDigest.reset();
		return BitSet.valueOf(tokenHash);
	}

	/**
	 * A blank slate - a digest-sized array of 0s.
	 * 
	 * @return a bitLength-sized array of 0s
	 */
	private long[] initFingerprint(int bitLength) {
		long[] toReturn = new long[bitLength];
		Arrays.fill(toReturn, 0);
		return toReturn;
	}
}
