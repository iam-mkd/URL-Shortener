package com.example.urlshortener.util;

public class Base62Encoder {

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(long id) {
        if ( id == 0 ) return "0";
        StringBuilder sb = new StringBuilder();
        while( id > 0 ) {
            int rem = (int) (id % 62);
            sb.append(BASE62_CHARS.charAt(rem));
            id /= 62;
        }
        return sb.reverse().toString();
    }
    
}
