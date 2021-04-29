package de.framedev.essentialsmini.utils;

public class TextUtils {

    public String replaceAndToParagraph(String text) {
        if(text.contains("&"))
            return text.replace('&','§');
        return text;
    }

    public String replaceObject(String text,String key,String value) {
        if(text.contains(key))
            return text.replace(key,value);
        return text;
    }
}
