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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author ozkr16
 */
public class Util {

    private final static int LETTER_BIT_WIDTH = 8;

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
    
    public static Boolean[] ConvertBytesToBitArray(byte[] bytes){
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
    
    public static byte[] ConvertBitArrayToBytes(Boolean[] data){
        int index = 0;
        byte[] bytes = new byte[data.length/8];
        
        int asciiValue = 0;
        for (boolean item : data) {
            if(item){
                asciiValue += Math.pow(2, LETTER_BIT_WIDTH - (index  % LETTER_BIT_WIDTH) -1);
            }
            index++;
            if(index % LETTER_BIT_WIDTH == 0){
                byte b = (byte) asciiValue;
                int i2 = b & 0xFF;
                bytes[(index / 8) - 1] = (byte) i2;
                asciiValue = 0;
            }
        }
 
        return bytes;
    }
    
    public static String PrintableStringFrom(Boolean[] array){
        StringBuilder printable = new StringBuilder();
        for(boolean bit : array){
            printable.append((bit)?"1":"0");
        }
        return printable.toString();
    }
    
    public static void WriteContentsToFile(String planeTextFilePath, byte[] encryptedData) throws IOException {
        Files.write(Paths.get(planeTextFilePath), encryptedData);
    }
    
    public static void WriteContentsToFile(String planeTextFilePath, String hexaString) throws IOException {
        try(  PrintWriter out = new PrintWriter(planeTextFilePath)  ){
            out.println( hexaString );
        }
    }

    public static byte[] ReadFileContents(String planeTextFilePath) throws IOException, IllegalArgumentException {
        byte[] data;
        if(planeTextFilePath != null){
            data = Files.readAllBytes(Paths.get(planeTextFilePath));
            return data;
        }else{
            throw new IllegalArgumentException("Filepath is invalid!");
        }
    }
    
    public static String ReadFileAsString(String planeTextFilePath) throws IOException, IllegalArgumentException {
        return new Scanner(new File(planeTextFilePath)).useDelimiter("\\Z").next();
    }
    
    public static String BytesToHexString(byte[] data){
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    private static Boolean[] InvertBitsInByte(Boolean[] booleanByte){
        int dataLenght = booleanByte.length;
        Boolean[] invertedBytes = new Boolean[dataLenght];
        int index = 0;
        for(boolean bit : booleanByte){
            invertedBytes[dataLenght - index -1] = bit;
            ++index;
        }
        return invertedBytes;
    }
    
    public static Boolean[] InvertBytes(Boolean[] dataToInvert){
        Boolean[] fakeByte = new Boolean[8];
        Boolean[] result = new Boolean[dataToInvert.length];
        Boolean[] fakeByteReversed; 
        int k = 0;
        for(int i = 0; i <= dataToInvert.length; ++i){
            if(i % 8 == 0 && i != 0){
                fakeByteReversed = InvertBitsInByte(fakeByte);
                int positionWhithinByte = 0;
                for(Boolean bit : fakeByteReversed){
                    result[k*8 + positionWhithinByte] = bit;
                    ++positionWhithinByte;
                }
                fakeByteReversed = null;
                ++k;
            }
            if(i < dataToInvert.length){
                fakeByte[(i % 8)] = dataToInvert[i];
            }
        }
        return result;
    }
    
    public static Boolean[] ZeroOneStringToBooleanArray(String data){
        char[] zeroOneChars = data.trim().replace(" ", "").toCharArray();
        Boolean[] booleanResult = new Boolean[zeroOneChars.length];
        int i = 0;
        for(char bit : zeroOneChars){
            booleanResult[i] = (bit == '1');
            ++i;
        }
        return booleanResult;
    }
}
