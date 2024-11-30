package com.polarplus.utils;

import java.text.Normalizer;

public class Utils {
    public static String removerAcentos(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
