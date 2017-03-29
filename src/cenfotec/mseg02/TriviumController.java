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
    
    public void rebuildBitGeneratorWith(Boolean[] key, Boolean[] iv){
        this.keyArray = key;
        this.ivArray = iv;
        bitGenerator = new TriviumBitGenerator(keyArray, ivArray);
    }
    
    public Boolean[] encrypt(Boolean[] dataBits, Boolean[] key, Boolean[] iv){

        rebuildBitGeneratorWith(key, iv);
        
        Boolean[] encryptedData = new Boolean[dataBits.length];
        for(int i = 0; i < dataBits.length; i++){
            Boolean randomBit = this.bitGenerator.getAlternateNextRandomBit();
            encryptedData[i] = Util.XOR(dataBits[i], randomBit);
        }
        
        return encryptedData;
    }
    
    public Boolean[] decrypt(Boolean[] encryptedData, Boolean[] key, Boolean[] iv){
        //The decryption operation is exactly the same as the encryption operation, but using the encrypted data as input.
        return this.encrypt(encryptedData, key, iv);
    }
    
}
