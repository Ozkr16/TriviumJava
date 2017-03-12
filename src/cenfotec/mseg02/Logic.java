/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cenfotec.mseg02;

import java.util.Objects;

/**
 *
 * @author ozkr16
 */
public class Logic {
    //Cannot believe Java does not support operator overloading. 
    public static Boolean XOR(Boolean A, Boolean B){
        if(A == null || B == null){
            throw new IllegalArgumentException("Null value provided in XOR comparition.");
        }
        return !Objects.equals(A, B); //XOR basically returns true if the booleans are different (at least one will be true, but not both)
    }
}
