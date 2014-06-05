/**
 * 
 */
package us.supernymo.simhash.test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import us.supernymo.simhash.CachingSimHashFactory;
import us.supernymo.tokenizer.NGramTokenizerFactory;
import us.supernymo.tokenizer.TokenizerFactory;

/**
 * @author jsampson
 * 
 */
public class RealDataSimulation {

	/**
	 * CachingSimHash instance to test.
	 */
	private CachingSimHashFactory simHashFactory;

	private List<String> addresses;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.simHashFactory = new CachingSimHashFactory("MD5");
		this.addresses = new ArrayList<String>();

		Scanner scanner = new Scanner(getClass().getResourceAsStream("/addresses.txt"));
		while (scanner.hasNextLine()) {
			this.addresses.add(scanner.nextLine());
		}
		scanner.close();
	}

	@Test
	public void test() throws NoSuchAlgorithmException {
		TokenizerFactory tokenizer = new NGramTokenizerFactory();
		SortedMap<BitSet, Set<String>> bitSetToAddresses = new TreeMap<BitSet, Set<String>>(new Comparator<BitSet>() {
			@Override
			public int compare(BitSet o1, BitSet o2) {
				return new BigInteger(1, o1.toByteArray()).compareTo(new BigInteger(1, o2.toByteArray()));
			}
		});
		for (String address : this.addresses) {
			BitSet fingerprint = this.simHashFactory.getSimHash(tokenizer.getTokens(address));
			if (bitSetToAddresses.containsKey(fingerprint)) {
				bitSetToAddresses.get(fingerprint).add(address);
			} else {
				Set<String> addresses = new HashSet<String>();
				addresses.add(address);
				bitSetToAddresses.put(fingerprint, addresses);
			}
		}
		
		List<List<String>> likeAddresses = new ArrayList<List<String>>();
		for(int i = 0; i < 128; i++) {
			likeAddresses.addAll(getLikeAddresses(bitSetToAddresses, i));
			BitSet[] bitSets = bitSetToAddresses.keySet().toArray(new BitSet[0]);
			for(int j = 0; j < bitSets.length; j++ ) {
				Set<String> addresses = bitSetToAddresses.remove(bitSets[j]);
				rotate(bitSets[j]);
				bitSetToAddresses.put(bitSets[j], addresses);
			}
		}

		for(List<String> likeAddressPair : likeAddresses) {
			for(String likeAddress : likeAddressPair) {
				System.out.println(likeAddress);
			}
			System.out.println();
		}
		System.out.println(likeAddresses.size());
	}
	
	public List<List<String>> getLikeAddresses(SortedMap<BitSet, Set<String>> bitSetToAddresses, int iteration) {
		List<List<String>> toReturn = new ArrayList<List<String>>();
		Set<BitSet> forgettable = new HashSet<BitSet>();
		BitSet[] bitSets = bitSetToAddresses.keySet().toArray(new BitSet[0]);
		for(int i = 0; i < bitSets.length-1; i++) {
			BitSet comparisonClone = (BitSet) bitSets[i].clone();
			comparisonClone.xor(bitSets[i+1]);
			if(comparisonClone.cardinality() < 12) {
				List<String> likeAddresses = new ArrayList<>();
				likeAddresses.addAll(bitSetToAddresses.get(bitSets[i]));
				likeAddresses.addAll(bitSetToAddresses.get(bitSets[i+1]));
				likeAddresses.add(comparisonClone.cardinality() + " " + iteration);
				toReturn.add(likeAddresses);
				forgettable.add(bitSets[i]);
				forgettable.add(bitSets[i+1]);
			}
		}
		for(BitSet bitSet : forgettable) {
			bitSetToAddresses.remove(bitSet);
		}
		return toReturn;
	}

	public void rotate(BitSet bitSet) {
		boolean msb = bitSet.get(0);
		for (int i = 0; i < bitSet.size() - 1; i++) {
			bitSet.set(i, bitSet.get(i + 1));
		}
		bitSet.set(bitSet.size() - 1, msb);
	}

}
