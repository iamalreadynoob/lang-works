package dictionary;

import fileReading.DataReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LangComm
{

    private String path;

    public LangComm(String path) {this.path = path;}

    public ArrayList<ArrayList<String>> get()
    {
        ArrayList<ArrayList<String>> columns = new ArrayList<>();

        DataReading reading = new DataReading();
        reading.scan(path);

        ArrayList<String> headers = reading.getHeaders();
        for (String h: headers) columns.add(reading.getColumn(h));

        return columns;
    }

    public ArrayList<String> getThem(String column)
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        return reading.getColumn(column);
    }

    public String getThat(String keyLang, String keyword)
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        String equivalent = null;

        int index = -1;
        if (reading.getHeaders().get(0).equals(keyLang)) index = 0;
        else if (reading.getHeaders().get(1).equals(keyLang)) index = 1;

        if (index > -1)
        {
            ArrayList<String> column = reading.getColumn(reading.getHeaders().get(index));

            if (column.contains(keyword))
            {
                int subIndex = column.indexOf(keyword);

                if (index == 0) equivalent = reading.getColumn(reading.getHeaders().get(1)).get(subIndex);
                else equivalent = reading.getColumn(reading.getHeaders().get(0)).get(subIndex);
            }
        }

        return equivalent;
    }

    public String[] getLangs()
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        return new String[] {reading.getHeaders().get(0), reading.getHeaders().get(1)};
    }

    public String[] getColumns()
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        ArrayList<String> headers = reading.getHeaders();

        String[] columns = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) columns[i] = reading.getHeaders().get(i);

        return columns;
    }

    public Map<String, String> getMap()
    {
        DataReading reading = new DataReading();
        reading.scan(path);

        ArrayList<String> fromWords = reading.getColumn(reading.getHeaders().get(0));
        ArrayList<String> toWords = reading.getColumn(reading.getHeaders().get(1));

        Map<String, String> dictionary = new HashMap<>();
        for (int i = 0; i < fromWords.size(); i++) dictionary.put(fromWords.get(i), toWords.get(i));

        return dictionary;
    }

    public void setPath(String path) {this.path = path;}

}
