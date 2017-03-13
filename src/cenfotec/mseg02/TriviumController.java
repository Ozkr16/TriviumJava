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
    
    public String encrypt(String data, String key, String iv){
        rebuildBitGeneratorWith(key, iv);
        
        Boolean[] dataBits = Util.ConvertStringToBitArray(data);
        for(Boolean bit : dataBits){
            
        }
        return "";
    }
    
    public String encrypt(String data){
        
        Boolean[] dataBits = Util.ConvertStringToBitArray(data);
        Boolean[] encryptedData = new Boolean[dataBits.length];
        for(int i = 0; i < dataBits.length; i++){
            encryptedData[i] = Util.XOR(dataBits[i], this.bitGenerator.getNextRandomBit());
        }
        return Util.ConvertBitArrayToString(encryptedData);
    }
    
    public String decrypt(String encryptedData){
        
        return Util.PrintableStringFrom(Util.ConvertStringToBitArray(encryptedData));
    }
    
    public String decrypt(String encryptedData, String key, String iv){
        rebuildBitGeneratorWith(key, iv);
        return Util.PrintableStringFrom(Util.ConvertStringToBitArray(encryptedData));
    }
}
