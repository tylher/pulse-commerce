package com.damoladev.pulsecommerce.utils;

import java.util.UUID;

public class FilenameSanitizer {
    public static String sanitize(String filename){
        if(filename==null||filename.isBlank()){
            return UUID.randomUUID().toString();
        }

        String extension = "";
        int dotIndex = filename.lastIndexOf('.');

        if(dotIndex>0){
            extension = filename.substring(dotIndex).toLowerCase();
        }

        String sanitizedText = filename.trim()
                .replaceAll("\\.\\.+","")
                .replaceAll("[^0-9a-zA-Z-_.]","_");

        return UUID.randomUUID()+extension;
    }
}
