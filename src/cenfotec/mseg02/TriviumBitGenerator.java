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
    private final int REG_C_SIZE = 110;
    
    private BooleanRegister regA = new BooleanRegister(REG_A_SIZE, 66, 69);
    private BooleanRegister regB = new BooleanRegister(REG_B_SIZE, 68, 77);
    private BooleanRegister regC = new BooleanRegister(REG_C_SIZE, 65, 86);
    
}
