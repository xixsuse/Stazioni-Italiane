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
