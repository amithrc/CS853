package main.java.util;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import edu.unh.cs.treccar_v2.Data;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
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
            //System.out.println(page.getPageId());
            //System.out.println(page.getPageName());
            data.put(page.getPageId(),page.getPageName());
        }
        return data;
    }

}
