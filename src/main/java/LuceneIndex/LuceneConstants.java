package main.java.LuceneIndex;

import java.util.HashMap;
import java.util.Map;

public class LuceneConstants
{
    public static String FILE_NAME="";
    public static String DIRECTORY_NAME="";
    public static Map<String, Map<String,Integer>> queryDocPair = new HashMap<String, Map<String, Integer>>();

    public static String getIndexFileName()
    {
        return FILE_NAME;
    }

    public static void setIndexFileName(String s)
    {
        FILE_NAME = s;
    }
    public static void setDirectoryName(String d)
    {
        DIRECTORY_NAME= d;
    }
}
