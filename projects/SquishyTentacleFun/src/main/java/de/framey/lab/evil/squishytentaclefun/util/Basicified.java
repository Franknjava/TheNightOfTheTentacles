package de.framey.lab.evil.squishytentaclefun.util;

import java.io.IOException;

public interface Basicified {

    default void NOP() {
    }
    
    default void CLS() {
        System.out.print("\033[H\033[2J");
    }
    
    default void PRINT(String text, Object... args) {
        System.out.println(String.format(text, args));
    }
    
    default char INPUT(String text) throws IOException {
        int input;
        System.out.print(text);
        input = System.in.read(); 
        System.in.read();
        return (char) input;
    }
}
