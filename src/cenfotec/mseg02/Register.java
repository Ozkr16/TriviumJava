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
class Register {
    private final int registerSize;
    private int startIndex = 0;
    private int endIndex = 0;
    public Register(int registerSize) {
        if( registerSize < 1){
            throw new IllegalArgumentException("Register size cannot be zero or negative.");
        }
        
        this.registerSize = registerSize;
        endIndex = registerSize - 1;
    }
    
}
