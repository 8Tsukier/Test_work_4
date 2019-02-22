package ua.telesens.io;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GettingSpecificAttributes implements Runnable{
    LookingForMask lf;
    final static String requiredCreationTime = "^2018-09-18*";

    public GettingSpecificAttributes(LookingForMask lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
            Iterator<Map.Entry<String, BasicFileAttributes>> entries = lf.table.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry<String, BasicFileAttributes> entry = entries.next();
                Pattern pattern = Pattern.compile(requiredCreationTime);
                Matcher matcher = pattern.matcher(entry.getValue().creationTime().toString());
                if(matcher.find()){
                    System.out.println("File Name = " + entry.getKey() + " File creation time = " + entry.getValue().creationTime());
                }
            }
    }
}