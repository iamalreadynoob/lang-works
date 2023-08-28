package dictionary;

import fileReading.DataReading;
import fileWriting.TextWriting;

import java.util.ArrayList;
import java.util.Random;

public class LangEditor
{
    private final String from, to, path;
    private ArrayList<String> fromWords, toWords;
    private ArrayList<ArrayList<String>> wordPieces;

    public LangEditor(String path)
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        from = reading.getHeaders().get(0);
        to = reading.getHeaders().get(1);
        this.path = path;

        fromWords = reading.getColumn(from);
        toWords = reading.getColumn(to);

        for (int i = 0; i < fromWords.size(); i++)
        {
            ArrayList<String> temp = new ArrayList<>();

            for (int j = 2; j < reading.getHeaders().size(); j++)
                temp.add(reading.getColumn(reading.getHeaders().get(j)).get(i));

            wordPieces.add(temp);
        }


        wordPieces = new ArrayList<>();
    }

    public void add(String wordLang, String word, String equivalent, ArrayList<String> pieces)
    {
        if (wordLang.equals(from) && !fromWords.contains(word) && !toWords.contains(equivalent))
        {
            fromWords.add(word);
            toWords.add(equivalent);
            wordPieces.add(pieces);
        }
        else if (wordLang.equals(to) && !toWords.contains(word) && !fromWords.contains(equivalent))
        {
            toWords.add(word);
            fromWords.add(equivalent);
        }
    }
    public void remove(String wordLang, String word)
    {
        if (wordLang.equals(from) && fromWords.contains(word))
        {
            int index = fromWords.indexOf(word);

            fromWords.remove(index);
            toWords.remove(index);
            wordPieces.remove(index);
        }
        else if (wordLang.equals(to) && toWords.contains(word))
        {
            int index = toWords.indexOf(word);

            fromWords.remove(index);
            toWords.remove(index);
            wordPieces.remove(index);
        }
    }

    public void edit(String keyLang, String key, String newValue, ArrayList<String> pieces)
    {

        if (keyLang.equals(from) && fromWords.contains(key))
        {
            int index = fromWords.indexOf(key);
            toWords.set(index, newValue);
            wordPieces.set(index, pieces);
        }

        else if (keyLang.equals(to) && toWords.contains(key))
        {
            int index = toWords.indexOf(key);
            fromWords.set(index, newValue);
        }

    }

    public void generate(String keyLang, String key, int syllabusAmount, int letterAmount, int vowelLimit, ArrayList<String> alphabet, ArrayList<String> vowels)
    {
        boolean flag = false;

        if ((keyLang.equals(from) && fromWords.contains(key)) || (keyLang.equals(to) && toWords.contains(key)))
            flag = true;

        if (flag)
        {
            String equal = null;
            String[] pieces = new String[syllabusAmount];
            for (int j = 0; j < pieces.length; j++) pieces[j] = "-";

            ArrayList<String> equivalent = new ArrayList<>();
            if (keyLang.equals(from)) equivalent = fromWords;
            else if (keyLang.equals(to)) equivalent = toWords;

            do
            {
                for (int j = 0; j < syllabusAmount; j++)
                {
                    int vowelUsed = 0;

                    String syll;

                    do
                    {
                        syll = null;

                        for (int k = 0; k < letterAmount; k++)
                        {
                            int destiny = new Random().nextInt(alphabet.size());
                            String l = alphabet.get(destiny);

                            if (vowels.contains(l)) vowelUsed++;

                            if (syll == null) syll = l;
                            else syll += l;
                        }
                    }
                    while (vowelUsed < vowelLimit);

                    pieces[j] = syll;

                    if (equal == null) equal = syll;
                    else equal += syll;
                }

            }
            while (equivalent.contains(equal));

            ArrayList<String> temp = new ArrayList<>();
            for (String p: pieces) temp.add(p);
            wordPieces.add(temp);

            equivalent.add(equal);
            wordPieces.add(temp);
        }
    }

    public void save()
    {
        ArrayList<String> lines = new ArrayList<>();

        String header = from + "," + to;
        for (int i = 0; i < wordPieces.get(0).size(); i++) header += "," + "syllable-" + i;
        lines.add(header);

        for (int i = 0; i < fromWords.size(); i++)
        {
            String line = fromWords.get(i) + "," + toWords.get(i);
            for (String p: wordPieces.get(i)) line += "," + p;
            lines.add(line);
        }

        TextWriting.write(path, lines);
    }

}
