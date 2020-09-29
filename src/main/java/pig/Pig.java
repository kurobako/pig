package pig;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Pig {
    static final Pattern NON_WORD = Pattern.compile("\\W+");
    static final Pattern DASH = Pattern.compile("-");
    static final Pattern ENDS_WITH_WAY = Pattern.compile("way$");
    static final Pattern FIRST_VOWEL = Pattern.compile("^([aeiou])"); //beginning of the word, any vowel
    static final Pattern FIRST_CONSONANT = Pattern.compile("^([a-z&&[^aeiou]])(.+)"); //beginning of the word, any consonant, then whatever

    static final Character APOSTROPHE = '\'';

    public static void main(String[] args) {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt" +
                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco" +
                " laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in" +
                " voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat" +
                " non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        System.out.println(processText(text));
    }

    /*
    Break text into sentences, process them separately, catenate back. Line delimiters, whitespaces etc. preserved.
     */
    public static String processText(String text) {
        return split(text, BreakIterator.getSentenceInstance()).stream()
                .map(Pig::processSentence)
                .collect(Collectors.joining());
    }

    /*
    Splits provided String using provided BreakIterator, calling BreakIterator.setText in advance.
     */
    static List<String> split(String str, BreakIterator breakIterator) {
        breakIterator.setText(str);
        List<String> result = new ArrayList<>();
        int begin = breakIterator.first();
        int end = breakIterator.next();
        while (BreakIterator.DONE != end) {
            result.add(str.substring(begin, end));
            begin = end;
            end = breakIterator.next();
        }
        return result;
    }

    /*
    Break sentence into words, process them separately, catenate back. Whitespaces, commas etc. are considered a word.
    */
    static String processSentence(String sentence) {
        return split(sentence, BreakIterator.getWordInstance()).stream()
                .map(Pig::processWord)
                .collect(Collectors.joining());
    }

    /*
    If our 'word' is, in fact, a whitespace or punctuation mark, return it unchanged.
    Otherwise, split the input by '-', process each part separately, sew them back and return
     */
    static String processWord(String word) {
        if (NON_WORD.matcher(word).matches()) {
            return word;
        }
        return Arrays.stream(DASH.split(word))
                .map(Pig::pigify)
                .collect(Collectors.joining("-"));
    }

    /*
    Applies most of the rules, see comments
     */
    static String pigify(String word) {
        // First, exclude words ending with -way
        if (ENDS_WITH_WAY.matcher(word).find()) {
            return word;
        }
        //Then, remember where apostrophe was and remove it. Not expecting more than one in a word!
        int apostropheIdx = word.indexOf(APOSTROPHE);
        word = word.replace(APOSTROPHE.toString(), "");
        String lowercase = word.toLowerCase();
        // apply two main rules: -way and -ay
        if (FIRST_VOWEL.matcher(lowercase).find()) {
            lowercase = lowercase + "way";
        } else {
            // regex in replaceAll makes two matched groups switch places, thus moving first consonnant to the end
            lowercase = FIRST_CONSONANT.matcher(lowercase).replaceAll("$2$1ay");
        }
        // look at the original word and capitalize as necessary
        StringBuilder result = new StringBuilder(lowercase);
        for (int i = 0; i < word.length(); i++) {
            if (Character.isUpperCase(word.charAt(i))) {
                result.setCharAt(i, Character.toUpperCase(result.charAt(i)));
            }
        }
        // now, if there was an apostrophe in the original word, insert it
        if (apostropheIdx != -1) {
            result.insert(result.length() - word.length() + apostropheIdx, APOSTROPHE);
        }
        return result.toString();
    }
}
