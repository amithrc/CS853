package main.java.LuceneIndex;

public class LuceneConstants
{
    public static String FILE_NAME="";
    public static String DIRECTORY_NAME="E:";

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
