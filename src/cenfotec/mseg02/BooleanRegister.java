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
class BooleanRegister {
    private final int registerSize;
    private int startIndex = 0;
    private int endIndex = 0;
    private final int feedbackOffset;
    private final int feedforwardOffset;
    private Boolean[] storage;
    
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
    
    public Boolean pushInputShiftAndOutput(Boolean newInput){
        Boolean output = this.getOutput();
        this.shiftRight();
        this.pushInput(newInput);
        return output;
    }
    
    public void shiftRight(){
        startIndex = (startIndex - 1 < 0)? registerSize : startIndex -1;
        endIndex = (endIndex - 1 < 0)? registerSize : endIndex -1;
    }
    
    public Boolean getFirstAndInputValue(){
        return storage[endIndex-1];
    }
    
    public Boolean getSecondAndInputValue(){
        return storage[endIndex-2];
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
    
}
