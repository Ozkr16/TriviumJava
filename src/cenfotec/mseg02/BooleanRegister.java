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
 * Clase que representa un registro de bits. Los shifs son logicos, por lo que 
 * los datos no se mueven dentro de storage. Solamente se utilizan indices para
 * indicar el inicio y fin del registro.
 * @author ozkr16
 */
class BooleanRegister {
    private final int registerSize;
    private int startIndex = 0;
    private int endIndex = 0;
    private final int feedbackOffset;
    private final int feedforwardOffset;
    private Boolean[] storage;
    
    /**
     * Inicializa el registro con los datos proveidos. 
     * @param regSize Tamaño del registro.
     * @param ffOffset Desde el principio, cuantas posiciones hacia la derecha para alcanzar la posicion de feedfordward
     * @param fbOffset Desde el principio, cuantas posiciones hacia la derecha para alcanzar la posicion de feedback.
     */
    public BooleanRegister(int regSize, int ffOffset, int fbOffset) {
        if( regSize < 3){
            throw new IllegalArgumentException("Register size cannot be less than 3.");
        }
        if(ffOffset < 1 || ffOffset >= regSize){
            throw new IllegalArgumentException("Feedforward offset must be within the bounds of the register size.");
        }
        
        if(fbOffset < 1 || fbOffset >= regSize){
            throw new IllegalArgumentException("Feedback offset must be within the bounds of the register size.");
        }
        
        this.registerSize = regSize;
        endIndex = registerSize - 1;
        storage = new Boolean[registerSize];
        this.initializeEmptyStorage();
        feedbackOffset = fbOffset - 1; //To keep array math you should use zero based offsets.
        feedforwardOffset = ffOffset - 1; //To keep array math you should use zero based offsets.
    }
    
    /**
     * Causa un shift logico hacia la derecha. Retorna el valor de salida.
     * @return 
     */
    public Boolean shift(){
        Boolean output = this.getOutput();
        this.internalShiftRight();
        return output;
    }
    
    public Boolean shiftLeft(){
        Boolean output = this.getOutput();
        this.internalShiftLeft();
        return output;
    }
    
    public void internalShiftRight(){
        startIndex = (startIndex - 1 < 0)? registerSize-1 : startIndex -1;
        endIndex = (endIndex - 1 < 0)? registerSize-1 : endIndex -1;
    }
    
    public void internalShiftLeft(){
        startIndex = (startIndex + 1 >= registerSize)? 0 : startIndex +1;
        endIndex = (endIndex + 1 >= registerSize)? 0 : endIndex +1;
    }
    
    public Boolean getFirstAndInputValue(){
        return storage[endIndex-1 > 0? endIndex-1: registerSize-1];
    }
    
    public Boolean getSecondAndInputValue(){
        return storage[endIndex-2 > 0? endIndex-2: registerSize-2];
    }

    public void pushInput(Boolean input){
        storage[startIndex] = input;
    }
    
    /**
     * @return the register output
     */
    public Boolean getOutput(){
        return storage[endIndex];
    }

    /**
     * @return the feedback output
     */
    public Boolean getFeedbackOffset() {
        return storage[(startIndex + feedbackOffset) % registerSize];
    }

    /**
     * @return the feedforward output
     */
    public Boolean getFeedforwardOffset() {
        return storage[(startIndex + feedforwardOffset) % registerSize];
    }

    private void initializeEmptyStorage() {
        for(int i = 0; i < registerSize; ++i){
            storage[i] = false;
        }
    }
    
    public void setStorage(Boolean[] data){
        if(data.length != registerSize){
            throw new IllegalArgumentException("Data size is different from register size.");
        }
        storage = data;
    }
}
