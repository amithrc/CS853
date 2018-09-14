package main.java.util;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import edu.unh.cs.treccar_v2.Data;

import java.io.*;
import java.util.HashMap;

import java.util.Map;


public class LuceneUtil
{
    static public Map<String, String> readQrel(String filename)
    {
        Map<String,String> data = new HashMap<String,String>();

        FileInputStream qrelStream = null;
        try {
            qrelStream = new FileInputStream(new File(filename));
        } catch (FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());

        }
        for (Data.Page page : DeserializeData.iterableAnnotations(qrelStream)) {
            data.put(page.getPageId(),page.getPageName());
        }
        return data;
    }

    public static int relevancy_count(Map<String,Map<String,Integer>> m , String query_id)
    {

        if(m.containsKey(query_id))
        {
            Map<String,Integer> temp = m.get(query_id);
            return temp.size();
        }
        else
        {
            return 0;
        }

    }

    static public Map<String,Map<String,Integer>> createQrelMap(String filename)
    {
        Map<String,Map<String,Integer>> mp = new HashMap<String, Map<String, Integer>>();

        File fp= new File(filename);
        FileReader fr;
        BufferedReader br = null;


        try {
            fr = new FileReader(fp);
            br = new BufferedReader(fr);

        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

        while(true)
        {
            try
            {
                String line = br.readLine();

                if(line == null )
                {
                    break;
                }

                String[] words =  line.split(" ");
                String outKey = words[0];

                if(mp.containsKey(outKey))
                {
                    Map<String, Integer> extract = mp.get(outKey);
                    String inner_key = words[2];
                    Integer is_relevant= new Integer(words[3]);
                    extract.put(inner_key,is_relevant);
                }
                else
                {

                    String inner_key = words[2];
                    Integer is_relevant= new Integer(words[3]);
                    Map<String,Integer> temp = new HashMap<String, Integer>();
                    temp.put(inner_key,is_relevant);
                    mp.put(outKey,temp);
                }
            }
            catch (NullPointerException n)
            {
                System.out.println(n.getMessage());
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
            }

        }

        /*for (Map.Entry<String, Map<String,Integer>> item : mp.entrySet())
        {
            String outkey = item.getKey();
            Map<String,Integer> temp= item.getValue();

            System.out.println(outkey);
            for(Map.Entry<String,Integer> inside: temp.entrySet())
            {
                System.out.println(inside.getKey()+"  "+ inside.getValue());
            }
            System.out.println("-----------------------------------------------");

        }

        System.out.println(relevancy_count(mp,"enwiki:Activity%20theory"));

        */


        return mp;

    }


}
