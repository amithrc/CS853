package main.java.lucenerankingmodels;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.Similarity;

import java.lang.Math;

public class TFIDF
{

    float calculate_score(float termFreq, BasicStats basicStats)
    {
        //System.out.println(termFreq);
        return termFreq;
    }

    private double logarithmicTF(float termFreq)
    {
        double value=1.0;
        if(termFreq>0)
        {
            value = (1+ Math.log10((double)termFreq));
        }

        return value;
    }

    private int booleanTF(float termFreq)
    {
        if(termFreq>0)
        {
            return 1;
        }

        return 0;
    }


    private float augumentedTF(float termFreq)
    {

        return termFreq;
    }

    private double logarithmicIDF(BasicStats basicStats)
    {
        long num_of_docs = basicStats.getNumberOfDocuments();
        long doc_freq = basicStats.getDocFreq();
        if(doc_freq > 0) {
            return Math.log10((double) num_of_docs / (double) doc_freq);
        }
        return 0.0;
    }

    private double probablisticIDF(BasicStats basicStats)
    {
        long num_of_docs = basicStats.getNumberOfDocuments();
        long doc_freq = basicStats.getDocFreq();
        double value = 0.0;
        if(doc_freq > 0) {
            value = Math.log10((double) (num_of_docs - doc_freq) / (double) doc_freq);
        }
        return (Math.max(0.0, value));
    }

    private double noNormalization()
    {
        return 1.0;
    }

    private double cosineNormalization(){

        return 0.0;
    }

}
