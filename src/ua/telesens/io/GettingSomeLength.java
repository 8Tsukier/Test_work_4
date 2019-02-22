package ua.telesens.io;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class GettingSomeLength implements Runnable{
    LookingForMask lf;
    int requiredFileLength = 1200; //kb

    public GettingSomeLength(LookingForMask lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
            Iterator<Map.Entry<String, BasicFileAttributes>> entries = lf.table.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry<String, BasicFileAttributes> entry = entries.next();
                if(entry.getValue().size() > requiredFileLength){
                    System.out.println("File Name = " + entry.getKey() + " File size = " + entry.getValue().size());
                }
            }
    }
}