package ua.telesens.io;

import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GettingSomeText  implements Runnable{
    LookingForMask lf;
    final static String textWeAreLooking = "the";

    public GettingSomeText(LookingForMask lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        Pattern pattern = Pattern.compile(textWeAreLooking);
        Matcher matcher = pattern.matcher(" ");
            Iterator<Map.Entry<String, BasicFileAttributes>> entries = lf.table.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry<String, BasicFileAttributes> entry = entries.next();

                try {
                    FileReader fr = new FileReader(entry.getKey());
                    BufferedReader br = new BufferedReader(fr);
                    String s = br.readLine();

                    if(s != null)
                        matcher = pattern.matcher(s);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }

                if(matcher.find()){
                    System.out.println("File Name that contain text = " + entry.getKey());
                }
            }
    }
}
