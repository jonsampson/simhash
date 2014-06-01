package us.supernymo.simhash.test;


import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import us.supernymo.simhash.SimHashFactory;
import us.supernymo.tokenizer.NGramTokenizerFactory;
import us.supernymo.tokenizer.TokenizerFactory;

/**
 * 
 * 
 * @author jsampson
 */
public class SimHashTest {
	
	/**
	 * SimHash instance to test.
	 */
	private SimHashFactory simHashFactory;
	
	/**
	 * First set of test tokens
	 */
	private List<String> valueA;
	
	/**
	 * Second set of test tokens
	 */
	private List<String> valueB;
	
	private Iterator<String> valueC;
	
	private List<String> valueD;
	
	private Iterator<String> valueE;
	
	private List<String> valueF;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.simHashFactory = new SimHashFactory("MD5");
		
		this.valueA = new ArrayList<String>();
		this.valueA.add("a");
		this.valueA.add("distinct");
		this.valueA.add("pleasure");
		
		this.valueB = new ArrayList<String>();
		this.valueB.add("a");
		this.valueB.add("dubious");
		this.valueB.add("pleasure");
		
		TokenizerFactory tokenizer = new NGramTokenizerFactory();
		this.valueC = tokenizer.getTokens("1 B Street, New York, NY");
		
		this.valueD = new ArrayList<String>();
		this.valueD.add("1");
		this.valueD.add("B");
		this.valueD.add("Street,");
		this.valueD.add("New");
		this.valueD.add("York,");
		this.valueD.add("NY");

		this.valueE = tokenizer.getTokens("1 Bay Street, New Carlisle, NJ");
		
		this.valueF = new ArrayList<String>();
		this.valueF.add("1");
		this.valueF.add("Bay");
		this.valueF.add("Street,");
		this.valueF.add("New");
		this.valueF.add("Carlisle,");
		this.valueF.add("NJ");
	}

	@Test
	public void test() throws NoSuchAlgorithmException {
			BitSet valueABitSet = this.simHashFactory.getSimHash(valueA.iterator());
			assertEquals("MD5 based BitSets should be a size of 128 bits;", valueABitSet.size(), 128);
			assertEquals("valueABitSet should look like the following;", SimHashFactory.toPrettyString(valueABitSet),"00110000111000110011110111111101110000101001111100101101010101010100111011000011001010010001011101010110011001010110010110110110");
			BitSet valueBBitSet = this.simHashFactory.getSimHash(valueB.iterator());
			assertEquals("MD5 based BitSets should be a size of 128 bits;", valueBBitSet.size(), 128);
			assertEquals("valueBBitSet should look like the following;", SimHashFactory.toPrettyString(valueBBitSet),"00100000111100100010010110111101010000011000101100101110010001000001111011000010100010010001010101010110101011100010010110010110");
			valueABitSet.xor(valueBBitSet);
			assertEquals("XOR of test values should contain 28 set bits", valueABitSet.cardinality(), 28);
			
			BitSet valueCBitSet = this.simHashFactory.getSimHash(this.valueC);
			BitSet valueDBitSet = this.simHashFactory.getSimHash(this.valueD.iterator());
			BitSet valueEBitSet = this.simHashFactory.getSimHash(this.valueE);
			BitSet valueFBitSet = this.simHashFactory.getSimHash(this.valueF.iterator());
			BitSet xorC = (BitSet) valueCBitSet.clone();
			xorC.xor(valueDBitSet);
			System.out.println("Cardinality of C & D: " + xorC.cardinality());
			valueCBitSet.xor(valueEBitSet);
			System.out.println("Cardinality of C & E: " + valueCBitSet.cardinality());
			valueDBitSet.xor(valueFBitSet);
			System.out.println("Cardinality of D & F: " + valueDBitSet.cardinality());
	}

}
