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
public class TriviumBitGenerator {
    
    private final int REG_A_SIZE = 93;
    private final int REG_B_SIZE = 84;
    private final int REG_C_SIZE = 111;
    
    private final int REG_A_FF_OFFSET = 66;
    private final int REG_A_FB_OFFSET = 69;
    private final int REG_B_FF_OFFSET = 68;
    private final int REG_B_FB_OFFSET = 77;
    private final int REG_C_FF_OFFSET = 65;
    private final int REG_C_FB_OFFSET = 86;
    
    private final BooleanRegister regA = new BooleanRegister(REG_A_SIZE, REG_A_FF_OFFSET, REG_A_FB_OFFSET);
    private final BooleanRegister regB = new BooleanRegister(REG_B_SIZE, REG_B_FF_OFFSET, REG_B_FB_OFFSET);
    private final BooleanRegister regC = new BooleanRegister(REG_C_SIZE, REG_C_FF_OFFSET, REG_C_FB_OFFSET);

    private final int KEY_SIZE = 80;
    private final int IV_SIZE = 80;
    
    private Boolean[] keyArray = new Boolean[KEY_SIZE];
    private Boolean[] ivArray = new Boolean[IV_SIZE];
    
    private final int INITIALIZATION_ROUNDS = 1152;
        
    public TriviumBitGenerator(Boolean[] key, Boolean[] iv){
        if(key.length != KEY_SIZE){
            throw new IllegalArgumentException("Key should have a size of " + KEY_SIZE);
        }
        if(iv.length != IV_SIZE){
            throw new IllegalArgumentException("Initialization vector should have a size of " + IV_SIZE);
        }
        
        this.keyArray = key;
        this.ivArray = iv;
        
        Boolean[] regAStorage = new Boolean[REG_A_SIZE];
        for(int i = 0; i < REG_A_SIZE; ++i){
            if(REG_A_SIZE - i > KEY_SIZE){
                regAStorage[i] = false;
            }else{
                regAStorage[i] = this.keyArray[(REG_A_SIZE - i) % KEY_SIZE];
            }
        }
        regA.setStorage(regAStorage);
        
        Boolean[] regBStorage = new Boolean[REG_B_SIZE];
        for(int i = 0; i < REG_B_SIZE; ++i){
            if(REG_B_SIZE - i > IV_SIZE){
                regBStorage[i] = false;
            }else{
                regBStorage[i] = this.ivArray[(REG_A_SIZE - i) % IV_SIZE];
            }
        }
        regB.setStorage(regBStorage);
        
        Boolean[] regCStorage = new Boolean[REG_C_SIZE];
        for(int i = 0; i < REG_C_SIZE; ++i){
            regCStorage[i] = false;
        }
        regCStorage[0] = true;
        regCStorage[1] = true;
        regCStorage[2] = true;
        regC.setStorage(regCStorage);
        
        warmUpBitGeneratorEngine();
    }
    
    public Boolean getNextRandomBit(){
        
        Boolean regAFinalOutput = Util.XOR(regA.getOutput(), regA.getFeedforwardOffset());
        Boolean regBFinalOutput = Util.XOR(regB.getOutput(), regB.getFeedforwardOffset());
        Boolean regCFinalOutput = Util.XOR(regC.getOutput(), regC.getFeedforwardOffset());
        
        Boolean zOutput = Util.XOR(Util.XOR(regAFinalOutput, regBFinalOutput), regCFinalOutput);
        
        Boolean regAAndResult = regA.getFirstAndInputValue() && regA.getSecondAndInputValue();
        Boolean regBAndResult = regB.getFirstAndInputValue() && regB.getSecondAndInputValue();
        Boolean regCAndResult = regC.getFirstAndInputValue() && regC.getSecondAndInputValue(); 
        
        Boolean regBInput = Util.XOR(Util.XOR(regAFinalOutput, regAAndResult), regB.getFeedbackOffset());
        Boolean regCInput = Util.XOR(Util.XOR(regBFinalOutput, regBAndResult), regC.getFeedbackOffset());
        Boolean regAInput = Util.XOR(Util.XOR(regCFinalOutput, regCAndResult), regA.getFeedbackOffset());
        
        regA.shift();
        regB.shift();
        regC.shift();
        
        regA.pushInput(regAInput);
        regB.pushInput(regBInput);
        regC.pushInput(regCInput);
        
        return zOutput;
    }
    
    /**
     * @return the key
     */
    public Boolean[] getKey() {
        return keyArray;
    }

    /**
     * @param keyArray the key to set
     */
    public void setKey(Boolean[] keyArray) {
        this.keyArray = keyArray;
    }

    /**
     * @return the ivArray
     */
    public Boolean[] getIV() {
        return ivArray;
    }

    /**
     * @param ivArray the iv to set
     */
    public void setIV(Boolean[] ivArray) {
        this.ivArray = ivArray;
    }
    
    private void warmUpBitGeneratorEngine(){
        for(int i = 0; i < INITIALIZATION_ROUNDS; ++i){
            this.getNextRandomBit(); //Ignore the first rounds to warm up the algorithm.
        }
    }
}
