package dictionary;

import fileReading.TextReading;
import fileWriting.TextWriting;

import java.util.ArrayList;
import java.util.Random;

public class LangMaker
{
    //TODO: ADD EXCEPTIONS!!!
    private ArrayList<String> words, alphabet, vowels;
    private String name, langName;

    public LangMaker()
    {
        words = new ArrayList<>();
        alphabet = new ArrayList<>();
        vowels = new ArrayList<>();
        name = "custom";
    }

    public void setName(String name) {this.name = name;}

    public void setResource(String path, String langName)
    {
        ArrayList<String> lines = TextReading.read(path);
        for (String s: lines) if (!words.contains(s)) words.add(s);

        this.langName = langName;
    }

    public void setResource(ArrayList<String> path, String langName) {for (String p: path) setResource(p, langName);}

    public void setAlphabet(ArrayList<String> alphabet) {this.alphabet = alphabet;}
    public void setAlphabet(String[] alphabet) {this.alphabet.clear(); for (String a: alphabet) this.alphabet.add(a);}

    public void setVowels(ArrayList<String> vowels) {this.vowels = vowels;}
    public void setVowels(String[] vowels) {this.vowels.clear(); for (String v: vowels) this.vowels.add(v);}

    public ArrayList<String> getDefaultAlphabet()
    {
        ArrayList<String> alphabet = new ArrayList<>();
        alphabet.add("a");  alphabet.add("b");  alphabet.add("c");  alphabet.add("d");
        alphabet.add("e");  alphabet.add("f");  alphabet.add("g");  alphabet.add("h");
        alphabet.add("i");  alphabet.add("j");  alphabet.add("k");  alphabet.add("l");
        alphabet.add("m");  alphabet.add("n");  alphabet.add("o");  alphabet.add("p");
        alphabet.add("q");  alphabet.add("r");  alphabet.add("s");  alphabet.add("t");
        alphabet.add("u");  alphabet.add("v");  alphabet.add("w");  alphabet.add("x");
        alphabet.add("y");  alphabet.add("z");

        return alphabet;
    }

    public ArrayList<String> getDefaultVowels()
    {
        ArrayList<String> vowels = new ArrayList<>();

        vowels.add("a");    vowels.add("e");    vowels.add("i");    vowels.add("o");    vowels.add("u");

        return vowels;
    }

    public void filter(Instructions instruction)
    {
        switch (instruction)
        {
            case TRIM: trim(); break;
            case TO_LOWER: toLower(); break;
            case TO_UPPER: toUpper(); break;
            case NON_NUMBERS: nonNumbers(); break;
            case NON_SYMBOLS: nonSymbols(); break;
            case NON_WHITE_SPACE: nonWhiteSpace(); break;
            case ONLY_VALID_ALPHABET: onlyValidAlphabet(); break;
        }
    }

    public void filter(String rule)
    {
        int index = rule.indexOf(":");
        String instruction = rule.substring(0, index);
        String input = rule.substring(index+1);

        if (instruction.equals("&")) mustChar(input);
        else if (instruction.equals("!")) notChar(input);
        else if (instruction.equals("i")) includeMust(input);
        else if (instruction.equals("e")) excludeMust(input);
    }

    public void filter(Instructions[] instructions) {for (Instructions i: instructions) filter(i);}
    public void filter(String[] rules) {for (String r: rules) filter(r);}
    public void filter(Instructions instruction, String rule) {filter(instruction); filter(rule);}
    public void filter(Instructions[] instructions, String rule) {filter(instructions); filter(rule);}
    public void filter(Instructions instructions, String[] rules) {filter(instructions); filter(rules);}
    public void filter(Instructions[] instructions, String[] rules) {filter(instructions); filter(rules);}

    public void makeDict(String syllabus, String[] letter, String minVowel)
    {
        ArrayList<Integer> syllabusAmounts = new ArrayList<>();
        ArrayList<Integer> syllabusPercentages = new ArrayList<>();

        ArrayList<ArrayList<Integer>> letterAmounts = new ArrayList<>();
        ArrayList<ArrayList<Integer>> letterPercentages = new ArrayList<>();

        ArrayList<Integer> vowelLimits = new ArrayList<>();

        //param - 1
        String[] syllabuses = syllabus.split(",");
        for (String s: syllabuses)
        {
            String[] temp = s.split(":");

            syllabusAmounts.add(Integer.parseInt(temp[0]));
            syllabusPercentages.add(Integer.parseInt(temp[1]));
        }

        //param - 2
        for (int i = 0; i < syllabusAmounts.size(); i++)
        {
            letterAmounts.add(new ArrayList<>());
            letterPercentages.add(new ArrayList<>());
            vowelLimits.add(null);
        }

        for (String line: letter)
        {
            String[] firstTier = line.split(":");

            int size = Integer.parseInt(firstTier[0]);
            int index = Integer.MIN_VALUE;

            for (int i = 0; i < syllabusAmounts.get(i); i++)
                if (syllabusAmounts.get(i) == size)
                {
                    index = i;
                    break;
                }

            ArrayList<Integer> letterTemp = new ArrayList<>();
            ArrayList<Integer> percentageTemp = new ArrayList<>();

            String[] secondTier = firstTier[1].split(",");

            for (String s: secondTier)
            {
                String[] thirdTier = s.split(";");

                letterTemp.add(Integer.parseInt(thirdTier[0]));
                percentageTemp.add(Integer.parseInt(thirdTier[1]));
            }

            letterAmounts.set(index, letterTemp);
            letterPercentages.set(index, percentageTemp);
        }

        //param - 3
        String[] vowelPieces = minVowel.split(",");

        for (String s: vowelPieces)
        {
            String[] piece = s.split(":");

            int size = Integer.parseInt(piece[0]);
            int index = Integer.MIN_VALUE;

            for (int i = 0; i < syllabusAmounts.get(i); i++)
                if (syllabusAmounts.get(i) == size)
                {
                    index = i;
                    break;
                }

            vowelLimits.set(index, Integer.parseInt(piece[1]));
        }

        //real process
        ArrayList<String> equivalent = new ArrayList<>();
        ArrayList<ArrayList<String>> wordPieces = new ArrayList<>();

        for (int i = 0; i < words.size(); i++)
        {
            String equal = null;
            String[] pieces = new String[syllabusAmounts.get(syllabusAmounts.size() - 1)];
            for (int j = 0; j < pieces.length; j++) pieces[j] = "-";

            do
            {
                int syllabusAmount = Integer.MIN_VALUE;
                int index = Integer.MIN_VALUE;
                int destiny = new Random().nextInt(100);

                int total = 0;
                for (int j = 0; j < syllabusAmounts.size(); j++)
                {
                    total += syllabusPercentages.get(j);
                    if (destiny < total)
                    {
                        syllabusAmount = syllabusAmounts.get(j);
                        index = j;
                        break;
                    }
                }

                for (int j = 0; j < syllabusAmount; j++)
                {
                    int letterAmount = Integer.MIN_VALUE;
                    int vowelLimit = Integer.MAX_VALUE;
                    int vowelUsed = 0;
                    destiny = new Random().nextInt(100);

                    total = 0;

                    for (int k = 0; k < letterAmounts.get(index).size(); k++)
                    {
                        total += letterPercentages.get(index).get(k);

                        if (destiny < total)
                        {
                            letterAmount = letterAmounts.get(index).get(k);
                            vowelLimit  = vowelLimits.get(k);
                            break;
                        }
                    }

                    String syll;

                    do
                    {
                        syll = null;

                        for (int k = 0; k < letterAmount; k++)
                        {
                            destiny = new Random().nextInt(alphabet.size());
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

        }

        createCsv(equivalent, wordPieces);
    }


    public enum Instructions
    {
        NON_WHITE_SPACE, TRIM, NON_SYMBOLS, NON_NUMBERS, TO_UPPER, TO_LOWER, ONLY_VALID_ALPHABET
    }

    private void nonWhiteSpace()
    {
        int loc = 0;

        while (loc < words.size())
        {
            if (words.get(loc).contains(" ")) words.remove(loc);
            else loc++;
        }
    }

    private void trim() {for (int i = 0; i < words.size(); i++) words.set(i, words.get(i).trim());}

    private void nonSymbols()
    {
        int loc = 0;

        while (loc < words.size())
        {
            if (words.get(loc).contains("\"") || words.get(loc).contains("!") || words.get(loc).contains("'") ||
                    words.get(loc).contains("^") || words.get(loc).contains("#") || words.get(loc).contains("+") ||
                    words.get(loc).contains("{") || words.get(loc).contains("%") || words.get(loc).contains("&") ||
                    words.get(loc).contains("/") || words.get(loc).contains("(") || words.get(loc).contains("[") ||
                    words.get(loc).contains(")") || words.get(loc).contains("]") || words.get(loc).contains("=") ||
                    words.get(loc).contains("}") || words.get(loc).contains("?") || words.get(loc).contains("*") ||
                    words.get(loc).contains("\\") || words.get(loc).contains("-") || words.get(loc).contains("_") ||
                    words.get(loc).contains("@") || words.get(loc).contains("¨") || words.get(loc).contains("~") ||
                    words.get(loc).contains("´") || words.get(loc).contains(",") || words.get(loc).contains(";") ||
                    words.get(loc).contains("`") || words.get(loc).contains("<") || words.get(loc).contains(">") ||
                    words.get(loc).contains("|") || words.get(loc).contains(".") || words.get(loc).contains(":"))
                words.remove(loc);
            else loc++;
        }
    }

    private void nonNumbers()
    {
        int loc = 0;

        while (loc < words.size())
        {
            if (words.get(loc).contains("0") || words.get(loc).contains("1") || words.get(loc).contains("2") ||
                    words.get(loc).contains("3") || words.get(loc).contains("4") || words.get(loc).contains("5") ||
                    words.get(loc).contains("6") || words.get(loc).contains("7") || words.get(loc).contains("8") ||
                    words.get(loc).contains("9")) words.remove(loc);
            else loc++;
        }
    }

    private void toUpper() {for (int i = 0; i < words.size(); i++) words.set(i, words.get(i).toUpperCase());}

    private void toLower() {for (int i = 0; i < words.size(); i++) words.set(i, words.get(i).toLowerCase());}

    private void onlyValidAlphabet()
    {
        int loc = 0;

        while (loc < words.size())
        {
            boolean flag = true;

            for (char c: words.get(loc).toCharArray())
                if (!alphabet.contains(Character.toString(c)))
                {
                    flag = false;
                    break;
                }

            if (!flag) words.remove(loc);
            else loc++;
        }
    }

    private void mustChar(String input)
    {
        ArrayList<String> list = new ArrayList<>();
        for (char c: input.toCharArray()) list.add(Character.toString(c));

        int loc = 0;

        while (loc < words.size())
        {
            boolean flag = true;

            for (char c: words.get(loc).toCharArray())
                if (!list.contains(Character.toString(c)))
                {
                    flag = false;
                    break;
                }

            if (flag) loc++;
            else words.remove(loc);

        }
    }

    private void notChar(String input)
    {
        ArrayList<String> list = new ArrayList<>();
        for (char c: input.toCharArray()) list.add(Character.toString(c));

        int loc = 0;

        while (loc < words.size())
        {
            boolean flag = true;

            for (char c: words.get(loc).toCharArray())
                if (list.contains(Character.toString(c)))
                {
                    flag = false;
                    break;
                }

            if (flag) loc++;
            else words.remove(loc);

        }
    }

    private void includeMust(String input)
    {
        String[] texts = input.split(",");

        int loc = 0;

        while (loc < words.size())
        {
            boolean flag = false;

            for (String s: texts)
                if (words.get(loc).contains(s))
                {
                    flag = true;
                    break;
                }

            if (flag) loc++;
            else words.remove(loc);
        }
    }

    private void excludeMust(String input)
    {
        String[] texts = input.split(",");

        int loc = 0;

        while (loc < words.size())
        {
            boolean flag = true;

            for (String s: texts)
                if (words.get(loc).contains(s))
                {
                    flag = false;
                    break;
                }

            if (flag) loc++;
            else words.remove(loc);
        }
    }

    private void createCsv(ArrayList<String> equivalents, ArrayList<ArrayList<String>> wordPieces)
    {
        ArrayList<String> lines = new ArrayList<>();

        String header = langName + "," + name;
        for (int i = 0; i < wordPieces.get(0).size(); i++) header += "," + "syllable-" + i;
        lines.add(header);

        for (int i = 0; i < equivalents.size(); i++)
        {
            String line = words.get(i) + "," + equivalents.get(i);
            for (String p: wordPieces.get(i)) line += "," + p;
            lines.add(line);
        }

        TextWriting.write(langName + "_to_" + name + ".csv", lines);
    }

}
