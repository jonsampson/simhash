package com.poisonprose.simhash.test;


import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.poisonprose.simhash.SimHashFactory;

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
	}

}
