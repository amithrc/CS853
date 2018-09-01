package main.java.LuceneIndex;

    /* Lucene Dependent Files*/
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;

 /* Trec car Dependent Files*/
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import edu.unh.cs.treccar_v2.Data;


    /* Java Dependent Files*/
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


class LuceneIndexer
{


    public IndexWriter createIndex(String relative_path) throws IOException
    {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        Directory indexDir = FSDirectory.open(Paths.get(relative_path));
        return new IndexWriter(indexDir,conf);
    }

    private Document createDoc(Data.Paragraph temp)
    {
        Document doc = new Document();
        doc.add(new StringField("paraID", temp.getParaId(), Field.Store.YES));
        doc.add(new TextField("paraBody", temp.getTextOnly(), Field.Store.YES));
        return doc;
    }



    public void writeIndex(IndexWriter i)
    {
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(new File(LuceneConstants.FILE_NAME));
        }
        catch(FileNotFoundException fnf)
        {
            System.out.println("Exception raised");
        }

        for(Data.Paragraph p: DeserializeData.iterableParagraphs(fstream))
        {
                System.out.println("Indexing "+ p.getParaId());
                Document temp = createDoc(p);
                try
                {
                    i.addDocument(temp);
                }
                catch (IOException io)
                {
                    System.out.println(io.getMessage());
                }
        }

           try
           {
               i.close();
           }
           catch (IOException io)
           {
               System.out.println(io.getMessage());
           }

    }

}
