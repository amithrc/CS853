package main.java.util;


import java.util.LinkedHashMap;
import java.util.Map;

public class LuceneConstants
{


    public static String FILE_NAME="";
    public static String UNIGRAM_DIRECTORY="";
    public static String BIGRAM_DIRECTORY="";
    public static Map<String, Map<String,Integer>> queryDocPair = new LinkedHashMap<String, Map<String, Integer>>();

    public static String OUTLINE_CBOR="";
    public static String QREL_PATH="";

    public static float lambda = 0.9f;

    public static Map<String, Map<String,Integer>> queryDocPairRead;
    public static void setIndexFileName(String s)
    {
        FILE_NAME = s;
    }
    public static void setUnigram(String d) { UNIGRAM_DIRECTORY = d; }
    public static void setBigram(String d) {BIGRAM_DIRECTORY = d;}
    public static void setOutlineCbor(String d)
    {
        OUTLINE_CBOR= d;
    }
    public static void setQrelPath(String d){ QREL_PATH= d;}



}
