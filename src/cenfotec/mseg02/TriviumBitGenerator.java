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
 * TriviumBitGenerator se encarga de la generacion de bits pseudorandom para la encripcion de datos. 
 * Coordina los 3 registros, realiza el calentamiento y produce bits.
 * @author ozkr16
 */
public class TriviumBitGenerator {
    
    private final int REG_A_SIZE = 93;
    private final int REG_B_SIZE = 84;
    private final int REG_C_SIZE = 111;
    
    private final int REG_A_FF_OFFSET = 66; //Feed Forward del registro A
    private final int REG_A_FB_OFFSET = 69; //Feed Back del registro A
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
    
    /**
     * Construye un generador Trivium, realiza la inicializacion y ejecuta el calentamiento.
     * @param key Llave como cadena de bits (booleanos). Debe ser de 80 bits.
     * @param iv Vector de inicializacion como cadena de bits (booleanos). Debe ser de 80 bits.
     */
    public TriviumBitGenerator(Boolean[] key, Boolean[] iv){
        if(key.length != KEY_SIZE){
            throw new IllegalArgumentException("Key should have a size of " + KEY_SIZE);
        }
        if(iv.length != IV_SIZE){
            throw new IllegalArgumentException("Initialization vector should have a size of " + IV_SIZE);
        }
        
        this.keyArray = key;
        this.ivArray = iv;
        
        //Llama la inicializacion de los registros.
        initializeRegisters();
        
        //Corre las rondas de calentamiento y descarta los resultados de esas rondas.
        warmUpBitGeneratorEngine();
    }

    /**
     * Inicializa los 3 registros de Trivium con los datos apropiados. 
     */
    private void initializeRegisters() {
        //Registro A: En los primeros 80 bits, coloca la llave. Lo demas en ceros.
        Boolean[] regAStorage = new Boolean[REG_A_SIZE];
        for(int i = 0; i < REG_A_SIZE; ++i){
            if(i < KEY_SIZE){
                regAStorage[i] = this.keyArray[i];
            }else{
                regAStorage[i] = false;
            }
        }
        regA.setStorage(regAStorage);
        
        //Registro B: En los primeros 80 bits, coloca el vector. Lo demas en ceros.
        Boolean[] regBStorage = new Boolean[REG_B_SIZE];
        for(int i = 0; i < REG_B_SIZE; ++i){
            if(i < IV_SIZE){
                regBStorage[i] = this.ivArray[i];
            }else{
                regBStorage[i] = false;
            }
        }
        regB.setStorage(regBStorage);
        
        //Register C: Los ultimso 3 bits en 1. Lo demas en ceros. 
        Boolean[] regCStorage = new Boolean[REG_C_SIZE];
        for(int i = 0; i < REG_C_SIZE; ++i){
            regCStorage[i] = false;
        }
        regCStorage[REG_C_SIZE - 1] = true;
        regCStorage[REG_C_SIZE - 2] = true;
        regCStorage[REG_C_SIZE - 3] = true;
        regC.setStorage(regCStorage);
    }
    
     /**
     * Fase de calentamiento del algoritmo. Realiza 1152 rondas y descarta su resultado.
     */
    private void warmUpBitGeneratorEngine(){
        for(int i = 0; i < INITIALIZATION_ROUNDS; ++i){
            this.getNextRandomBit(); //Ignore the first rounds to warm up the algorithm.
        }
    }
    
    /**
     * Genera el siguiente bit pseudoaleatorio de la secuencia de Trivium y realiza los shift necesarios 
     * para preparar al generador para el proximo bit.
     * @return El nuevo bit generado. 
     */
    public Boolean getNextRandomBit(){
        
        //Obtiene salida y feedforward de registro A
        Boolean outA = regA.getOutput();
        Boolean ffA = regA.getFeedforwardOffset();
        
        //Obtiene salida y feedforward de registro B
        Boolean outB = regB.getOutput();
        Boolean ffB = regB.getFeedforwardOffset();
       
        //Obtiene salida y feedforward de registro C
        Boolean outC = regC.getOutput();
        Boolean ffC = regC.getFeedforwardOffset();
        
        //Calcula los XORs entre salida y feedforward de cada registro.
        Boolean regAFinalOutput = Util.XOR(outA, ffA);
        Boolean regBFinalOutput = Util.XOR(outB, ffB);
        Boolean regCFinalOutput = Util.XOR(outC, ffC);
        
        //Calcula el bit de salida como el XOR de las salidas anteriors de los registros.
        Boolean zOutput = Util.XOR(Util.XOR(regAFinalOutput, regBFinalOutput), regCFinalOutput);
        
        //Prepara los AND de cada registro para el calculo del input siguiente.
        Boolean regAAndResult = regA.getFirstAndInputValue() && regA.getSecondAndInputValue();
        Boolean regBAndResult = regB.getFirstAndInputValue() && regB.getSecondAndInputValue();
        Boolean regCAndResult = regC.getFirstAndInputValue() && regC.getSecondAndInputValue(); 
        
        //Calcula las entradas para cada registro.
        Boolean regBInput = Util.XOR(Util.XOR(regAFinalOutput, regAAndResult), regB.getFeedbackOffset());
        Boolean regCInput = Util.XOR(Util.XOR(regBFinalOutput, regBAndResult), regC.getFeedbackOffset());
        Boolean regAInput = Util.XOR(Util.XOR(regCFinalOutput, regCAndResult), regA.getFeedbackOffset());
        
        //Ejecuta los movimientos de cada registro.
        regA.shift();
        regB.shift();
        regC.shift();
        
        //Inserta las nuevas entradas en los registros.
        regA.pushInput(regAInput);
        regB.pushInput(regBInput);
        regC.pushInput(regCInput);
        
        //Retorna la salida previamente calculada.
        return zOutput;
    }
}
