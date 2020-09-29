package pig;

import org.junit.Test;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PigTest {

    @Test
    public void testSplitSentences() {
        String txt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ullamcorper elit porttitor sapien rhoncus, scelerisque iaculis nibh sagittis.";
        List<String> expected = new ArrayList<>() {{
           add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
           add("Nulla ullamcorper elit porttitor sapien rhoncus, scelerisque iaculis nibh sagittis.");
        }};
        assertEquals(expected, Pig.split(txt, BreakIterator.getSentenceInstance()));
    }

    @Test
    public void testSplitWords() {
        String txt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";
        List<String> expected = new ArrayList<>() {{
            add("Lorem");
            add(" ");
            add("ipsum");
            add(" ");
            add("dolor");
            add(" ");
            add("sit");
            add(" ");
            add("amet");
            add(",");
            add(" ");
            add("consectetur");
            add(" ");
            add("adipiscing");
            add(" ");
            add("elit");
            add(".");
            add(" ");
        }};
        assertEquals(expected, Pig.split(txt, BreakIterator.getWordInstance()));
    }

    @Test
    public void testSplitWordsApostrophe() {
        String txt = "O'Neil";
        List<String> expected = new ArrayList<>() {{
           add("O'Neil");
        }};
        assertEquals(expected, Pig.split(txt, BreakIterator.getWordInstance()));
    }

    @Test
    public void testProcessWord() {
        assertEquals(".", Pig.processWord("."));
        assertEquals(" ", Pig.processWord(" "));
        assertEquals("Ollsray-Oyceray", Pig.processWord("Rolls-Royce"));
        assertEquals("histay-hingtay", Pig.processWord("this-thing"));
    }

    @Test
    public void testPigify() {
        assertEquals("Ellohay", Pig.pigify("Hello"));
        assertEquals("appleway", Pig.pigify("apple"));
        assertEquals("stairway", Pig.pigify("stairway"));
        assertEquals("antca'y", Pig.pigify("can't"));
        assertEquals("CcLoudmay", Pig.pigify("McCloud"));
        assertEquals("CdOnaldsma'y", Pig.pigify("McDonald's"));
    }

    @Test
    public void testProcessSentence() {
        String txt = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        String expected = "Oremlay ipsumway olorday itsay ametway, onsecteturcay adipiscingway elitway.";
        assertEquals(expected, Pig.processSentence(txt));
    }
}
