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
import java.io.PrintWriter;
import java.nio.file.*;

/**
 *
 * @author ozkr16
 */
public class TriviumController {
    private final int KEY_SIZE = 80;
    private final int IV_SIZE = 80;
    
    private Boolean[] keyArray = new Boolean[KEY_SIZE];
    private Boolean[] ivArray = new Boolean[IV_SIZE];
    
    private TriviumBitGenerator bitGenerator;
    
    public TriviumController(){
        for(int i = 0; i < KEY_SIZE; ++i ){
            keyArray[i] = false;
            ivArray[i] = false;
        }
        
        bitGenerator = new TriviumBitGenerator(keyArray, ivArray);
    }

    public TriviumController(String key, String iv){
        this.keyArray = Util.ConvertStringToBitArray(key);
        this.ivArray = Util.ConvertStringToBitArray(iv);
        bitGenerator = new TriviumBitGenerator(keyArray, ivArray);
    }
    
    public void rebuildBitGeneratorWith(String key, String iv){
        this.keyArray = Util.ConvertStringToBitArray(key);
        this.ivArray = Util.ConvertStringToBitArray(iv);
        bitGenerator = new TriviumBitGenerator(keyArray, ivArray);
    }
    
    public String encrypt(String planeTextFilePathString, String key, String iv){
        
        try{
            rebuildBitGeneratorWith(key, iv);
            
            Paths.get(planeTextFilePathString);
            byte[] data = Files.readAllBytes(Paths.get(planeTextFilePathString));
            
            Boolean[] dataBits = Util.ConvertBytesToBitArray(data);
            Boolean[] encryptedData = new Boolean[dataBits.length];
            for(int i = 0; i < dataBits.length; i++){
                encryptedData[i] = Util.XOR(dataBits[i], this.bitGenerator.getNextRandomBit());
            }
            String result = Util.ConvertBitArrayToString(encryptedData);
            Files.write(Paths.get(planeTextFilePathString + ".encrypted"), result.getBytes(), StandardOpenOption.CREATE);
            
            return result;
        }catch(Exception ex){
            return ex.toString();
        }
    }
    
    public String decrypt(String encryptedFilePath, String key, String iv){
        
           try{
            rebuildBitGeneratorWith(key, iv);
            
            Paths.get(encryptedFilePath);
            byte[] data = Files.readAllBytes(Paths.get(encryptedFilePath));
            
            Boolean[] dataBits = Util.ConvertBytesToBitArray(data);
            Boolean[] planeText = new Boolean[dataBits.length];
            for(int i = 0; i < dataBits.length; i++){
                planeText[i] = Util.XOR(dataBits[i], this.bitGenerator.getNextRandomBit());
            }
            
            String result = Util.ConvertBitArrayToString(planeText);
       
            Files.write(Paths.get(encryptedFilePath + ".plane"), result.getBytes(), StandardOpenOption.CREATE);
            
            return result;
        }catch(Exception ex){
            return ex.toString();
        }
    }
}
