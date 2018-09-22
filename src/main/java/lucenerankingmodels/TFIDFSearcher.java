package main.java.lucenerankingmodels;

import main.java.LuceneSearch.LuceneSearcher;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

import java.io.IOException;

public class TFIDFSearcher extends LuceneSearcher
{
    private String ddd;
    private String qqq;


    public  TFIDFSearcher() throws IOException
    {
        super();
        TFIDFSimilarityBase tf = new TFIDFSimilarityBase();
        searcher.setSimilarity(tf);
        methodName= "tf_idf";
        output_file_name="lnn.txt";
        System.out.println("is it calling this =?");
    }

    public TFIDFSearcher(String document, String query) throws IOException
    {
        this();
        this.ddd =document;
        this.qqq =query;
    }




    private class TFIDFSimilarityBase extends SimilarityBase
    {

        @Override
        protected float score(BasicStats basicStats, float v, float v1)
        {

            TFIDF t = new TFIDF();
            float answer = t.calculate_score(v);

            return v;

        }

        @Override
        public String toString() {
            return null;
        }

    }


}
