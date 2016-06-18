package it.federicomagnani.stazioniitaliane;

public class Utility {

    public static String ucWords(String sentence) {
        StringBuffer sb = new StringBuffer();
        sentence = sentence.toLowerCase().replace("  ", " ").trim();
        for (CharSequence word: sentence.split(" ")) {
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.subSequence(1, word.length())).append(" ");
        }
        return sb.toString().trim();
    }

}
