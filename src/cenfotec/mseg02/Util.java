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

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.Objects;

/**
 *
 * @author ozkr16
 */
public class Util {
    //Cannot believe Java does not support operator overloading. 
    public static Boolean XOR(Boolean A, Boolean B){
        if(A == null || B == null){
            throw new IllegalArgumentException("Null value provided in XOR comparition.");
        }
        return !Objects.equals(A, B); //XOR basically returns true if the booleans are different (at least one will be true, but not both)
    }
    
    public static Boolean[] ConvertStringToBitArray(String data){
        byte[] bytes = data.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            } 
        }
        char[] binaryChars = binary.toString().toCharArray();
        Boolean[] output = new Boolean[binaryChars.length];
        int index = 0;
        for (char digit : binaryChars) {
            output[index] = (digit == '1');
            ++index;
        }
        return output;
    }
    
    public static String ConvertBitArrayToString(Boolean[] data){
        int index = 0;
        StringBuilder planeText = new StringBuilder();
        int asciiValue = 0;
        for (boolean item : data) {
            if(item){
                asciiValue += Math.pow(2, 8 - (index  % 8));
            }
            index++;
            if(index % 8 == 0){
                planeText.append((char)asciiValue);
                asciiValue = 0;
            }
        }
 
        return planeText.toString();
    }
    
    public static String PrintableStringFrom(Boolean[] array){
        StringBuilder printable = new StringBuilder();
        for(boolean bit : array){
            printable.append((bit)?"1":"0");
        }
        return printable.toString();
    }
}
