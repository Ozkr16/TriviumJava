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
public class TriviumController {
    private final int KEY_SIZE = 80;
    private final int IV_SIZE = 80;
    
    private final int REG_A_SIZE = 80;
    private final int REG_B_SIZE = 80;
    private final int REG_C_SIZE = 80;
    
    private Boolean[] keyArray = new Boolean[KEY_SIZE];
    private Boolean[] ivArray = new Boolean[IV_SIZE];
    
    private Register regA = new Register(REG_A_SIZE);
    private Register regB = new Register(REG_B_SIZE);
    private Register regC = new Register(REG_C_SIZE);
    
    public TriviumController(){
        initializeDefaultArrays();
    }
    
    private void initializeDefaultArrays(){
        for(int i = 0; i < KEY_SIZE; ++i ){
            keyArray[i] = false;
            ivArray[i] = false;
        }
        
        for(int i = 0; i < REG_A_SIZE; ++i ){
            keyArray[i] = false;
            ivArray[i] = false;
        }
        
        for(int i = 0; i < REG_B_SIZE; ++i ){
            keyArray[i] = false;
            ivArray[i] = false;
        }
        
        for(int i = 0; i < REG_C_SIZE; ++i ){
            keyArray[i] = false;
            ivArray[i] = false;
        }
        
    }

    /**
     * @return the keyArray
     */
    public Boolean[] getKeyArray() {
        return keyArray;
    }

    /**
     * @param keyArray the keyArray to set
     */
    public void setKeyArray(Boolean[] keyArray) {
        this.keyArray = keyArray;
    }

    /**
     * @return the ivArray
     */
    public Boolean[] getIvArray() {
        return ivArray;
    }

    /**
     * @param ivArray the ivArray to set
     */
    public void setIvArray(Boolean[] ivArray) {
        this.ivArray = ivArray;
    }
}
