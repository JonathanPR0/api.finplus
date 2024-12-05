package com.polarplus.utils;

import java.text.Normalizer;

public class Utils {
    public static String removerAcentos(String str) {
        if (str == null)
            return null;
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static boolean isValidCNPJ(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se a string tem o tamanho correto
        if (cnpj.length() != 14) {
            return false;
        }

        // CNPJ não pode ser uma sequência de números repetidos (ex.: "00000000000000")
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Validação do primeiro dígito verificador
        int[] peso1 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += Integer.parseInt(String.valueOf(cnpj.charAt(i))) * peso1[i];
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;
        if (digito1 != Integer.parseInt(String.valueOf(cnpj.charAt(12)))) {
            return false;
        }

        // Validação do segundo dígito verificador
        int[] peso2 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += Integer.parseInt(String.valueOf(cnpj.charAt(i))) * peso2[i];
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;
        return digito2 == Integer.parseInt(String.valueOf(cnpj.charAt(13)));
    }

    public static String normalizeNumberOnly(String input) {
        if (input == null) {
            return "";
        }
        // Remove todos os caracteres não numéricos
        return input.replaceAll("[^0-9]", "");
    }
}
