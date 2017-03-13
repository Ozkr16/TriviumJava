/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            if(REG_A_SIZE - KEY_SIZE > 0){
                regAStorage[i] = false;
            }else{
                regAStorage[i] = this.keyArray[i - KEY_SIZE];
            }
        }
        regA.setStorage(regAStorage);
        
        Boolean[] regBStorage = new Boolean[REG_B_SIZE];
        for(int i = 0; i < REG_B_SIZE; ++i){
            if(REG_B_SIZE - IV_SIZE > 0){
                regBStorage[i] = false;
            }else{
                regBStorage[i] = this.ivArray[i - IV_SIZE];
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
        
        Boolean regAPartialOutput = regA.shiftAndOutput();
        Boolean regAFinalOutput = Logic.XOR(regAPartialOutput, regA.getFeedforwardOffset());
        Boolean regAAndResult = regA.getFirstAndInputValue() && regA.getSecondAndInputValue();
        
        Boolean regBPartialOutput = regB.shiftAndOutput();
        Boolean regBFinalOutput = Logic.XOR(regBPartialOutput, regB.getFeedforwardOffset());
        Boolean regBAndResult = regB.getFirstAndInputValue() && regB.getSecondAndInputValue(); 
        Boolean regBInput = Logic.XOR(Logic.XOR(regAFinalOutput, regAAndResult), regB.getFeedbackOffset());
        regB.pushInput(regBInput);
        
        Boolean regCPartialOutput = regC.shiftAndOutput();
        Boolean regCFinalOutput = Logic.XOR(regCPartialOutput, regC.getFeedforwardOffset());
        Boolean regCAndResult = regC.getFirstAndInputValue() && regC.getSecondAndInputValue(); 
        Boolean regCInput = Logic.XOR(Logic.XOR(regBFinalOutput, regBAndResult), regC.getFeedbackOffset());
        regC.pushInput(regCInput);
        
        Boolean regAInput = Logic.XOR(Logic.XOR(regCFinalOutput, regCAndResult), regA.getFeedbackOffset());
        regA.pushInput(regAInput);
        
        Boolean outputBit = Logic.XOR(Logic.XOR(regAFinalOutput, regBFinalOutput), regCFinalOutput);
        
        return outputBit;
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
