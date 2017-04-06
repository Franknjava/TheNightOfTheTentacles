package de.framey.lab.evil.squishytentaclefun.wikipedia;

import java.io.IOException;

public interface Basicified {

    default void NOP() {
    }
    
    default void CLS() {
        System.out.print("\033[H\033[2J");
    }
    
    default void PRINT(String text) {
        System.out.println(text);
    }
    
    default char INPUT(String text) throws IOException {
        int input;
        System.out.print(text);
        input = System.in.read(); 
        System.in.read();
        return (char) input;
    }
}
