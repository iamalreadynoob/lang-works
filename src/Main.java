import dictionary.LangMaker;

public class Main {
    public static void main(String[] args)
    {
        LangMaker maker = new LangMaker();

        maker.setResource("test.txt", "english");
        maker.setName("sadoian");
        maker.setAlphabet(maker.getDefaultAlphabet());
        maker.setVowels(maker.getDefaultVowels());
        maker.makeDict("1:10,2:40,3:30,4:20",
                new String[]{"1:2;30,3;35,4;35",
                             "2:1;9,2;80,3;10,4;1",
                             "3:1;9,2;80,3;10,4;1",
                             "4:1;9,2;80,3;10,4;1"},
                "1:0,2:1,3:1,4:2");
    }
}