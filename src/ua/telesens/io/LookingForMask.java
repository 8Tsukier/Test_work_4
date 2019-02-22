package ua.telesens.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LookingForMask implements Runnable {
    final static String SOMEPATH = "D:\\Install\\Android_Studio";
    final static String MASK = "^*.txt";
    public volatile Map<String, BasicFileAttributes> table = new ConcurrentSkipListMap<String, BasicFileAttributes>();
    public static AtomicInteger atomic = new AtomicInteger(0);

    public void showList(File dir) throws IOException{
        Pattern pattern = Pattern.compile(MASK);

        for (File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                Matcher matcher = pattern.matcher(f.getName());
                if(matcher.find()) {
                    BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                    table.put(f.getAbsolutePath(), attr);
                    atomic.incrementAndGet();
                }
            }
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                showList(f);
            }
        }
    }

    @Override
    public void run() {
        try {
            showList(new File(SOMEPATH));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String result = table.entrySet().size() + " files found at \t" + SOMEPATH + " :\n";
        Iterator<Map.Entry<String, BasicFileAttributes>> entries = table.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, BasicFileAttributes> entry = entries.next();
            result += "File Name = " + entry.getKey() + "\t File size = " + entry.getValue().size() + "\n";
        }
        return result;
    }
}