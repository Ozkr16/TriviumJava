/*
 * Copyright 2017 ozkr16.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cenfotec.mseg02;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ozkr16
 */
public class UtilTest {
    
    public UtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of InvertByteBits method, of class Util.
     */
    @Test
    public void testInvertByteBits() {
        System.out.println("InvertBitsInByte");
        Boolean[] booleanByte = new Boolean[]{true, true, false, false, true, false, false, true};
        Boolean[] expResult = new Boolean[]{true, false, false, true, false, false, true, true};
        Boolean[] result = Util.InvertBitsInByte(booleanByte);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testInvertBytes() {
        System.out.println("InvertBytes");
        Boolean[] booleanByte = new Boolean[]{true, true, false, false, true, false, false, true,true, true, false, false, true, false, false, true};
        Boolean[] expResult = new Boolean[]{true, false, false, true, false, false, true, true,true, false, false, true, false, false, true, true};
        Boolean[] result = Util.InvertBytes(booleanByte);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testZeroOneStringToBooleanArray() {
        System.out.println("ZeroOneStringToBooleanArray");
        String input = "10100111";
        Boolean[] expResult = new Boolean[]{true, false, true, false, false, true, true, true};
        Boolean[] result = Util.ZeroOneStringToBooleanArray(input);
        assertArrayEquals(expResult, result);
    }
    
    @Test
    public void testConvertBitArrayToBytes() {
        System.out.println("ConvertBitArrayToBytes");
        Boolean[] input = new Boolean[]{true, false, true, false, false, true, true, true};
        byte[] partialResult = Util.ConvertBitArrayToBytes(input);
        Boolean[] actualResult = Util.ConvertBytesToBitArray(partialResult);
        
        assertArrayEquals(input, actualResult);
    }
    
    @Test
    public void testBitArrayToString() {
        System.out.println("BitArrayToString");
        Boolean[] input = new Boolean[]{false, true, false, false, false, false, false, true,false, true, false, false, false, false, true, false};
        String actualResult = Util.BitArrayToString(input);
        
        assertTrue("AB".equals(actualResult));
    }
}
