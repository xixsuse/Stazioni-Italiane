package it.federicomagnani.stazioniitaliane;

public class Utility {

    public static String ucWords(String sentence) {
        StringBuffer sb = new StringBuffer();
        sentence = sentence.toLowerCase().replace(".", ". ").replace("  ", " ").trim();
        if (sentence.length() == 0) {
            return "";
        }
        for (CharSequence word: sentence.split(" ")) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.subSequence(1, word.length())).append(" ");
            } else {
                sb.append(word).append(" ");
            }
        }
        return sb.toString().trim();
    }

    public static String formaData(int ore, int minuti) {
        String data = "";
        if (ore < 10) {
            data += "0";
        }
        data += ore+":";
        if (minuti < 10) {
            data += "0";
        }
        data += minuti+"";
        return data;
    }

}
