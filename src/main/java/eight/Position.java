package eight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Position {
    final String FILEPATH = "e:\\pos.txt";
    private static Position single = null;
    public int pos = 0;

    public static Position getInstance() {
        if (single == null) {
            single = new Position();
        }
        return single;
    }

    private Position() {
    }

    public int getAvaiablePos() {
        pos = readFileByLines(FILEPATH);
        writeFile(FILEPATH, "" + (pos + 1));
        return pos;
    }

    private int readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        int val = -1;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                try {
                    val = Integer.parseInt(tempString);
                    break;
                } catch (NumberFormatException e2) {
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return val;
    }

    private void writeFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
