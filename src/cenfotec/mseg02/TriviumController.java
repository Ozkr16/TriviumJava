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
 * Controlador del algoritmo de Trivium. Recibe las llamadas para encriptar y desencriptar datos.
 * @author ozkr16
 */
public class TriviumController {
    
    private TriviumBitGenerator bitGenerator; //Instancia del generador de bits aleatorios. 
    
    /**
     * Reconstruye el generador de bits utilizando la llave y vector de inicializacion. 
     * @param key Llave como cadena de bits (booleanos). Debe ser de 80 bits.
     * @param iv Vector de inicializacion como cadena de bits (booleanos). Debe ser de 80 bits.
     */
    private void rebuildBitGeneratorWith(Boolean[] key, Boolean[] iv){
        bitGenerator = new TriviumBitGenerator(key, iv);
    }
    
    /**
     * Encripta los bits que recibe con la llave y el vector recibidos utilizando Trivium.
     * @param dataBits Bits de datos a encriptar como cadena de bits (booleanos)
     * @param key Llave como cadena de bits (booleanos). Debe ser de 80 bits.
     * @param iv Vector de inicializacion como cadena de bits (booleanos). Debe ser de 80 bits.
     * @return Los datos encriptados.
     */
    public Boolean[] encrypt(Boolean[] dataBits, Boolean[] key, Boolean[] iv){

        //Primero reconstruya los registros para la nueva encripcion. 
        rebuildBitGeneratorWith(key, iv);
        
        Boolean[] encryptedData = new Boolean[dataBits.length]; //Objeto de salida que contendra el resultado de la encripcion. 
        
        //Para cada bit de datos genere un bit random usando Trivium y aplique XOR para encriptar.
        for(int i = 0; i < dataBits.length; i++){
            Boolean randomBit = this.bitGenerator.getNextRandomBit();
            encryptedData[i] = Util.XOR(dataBits[i], randomBit);
        }
        
        return encryptedData;
    }
    
    /**
     * Desencripta los bits que recibe con la llave y el vector recibidos utilizando Trivium.
     * @param encryptedData Bits de datos a desencriptar como cadena de bits (booleanos)
     * @param key Llave como cadena de bits (booleanos). Debe ser de 80 bits.
     * @param iv Vector de inicializacion como cadena de bits (booleanos). Debe ser de 80 bits.
     * @return Los datos desencriptados.
     */
    public Boolean[] decrypt(Boolean[] encryptedData, Boolean[] key, Boolean[] iv){
        //La desencripcion es exactamente lo mismo que la encripcion, pero usando los datos encriptados como entrada.
        //Esto es asi porque el XOR es una operacion invertible. 
        return this.encrypt(encryptedData, key, iv);
    }
    
}
