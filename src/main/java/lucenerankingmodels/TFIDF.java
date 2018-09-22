package main.java.lucenerankingmodels;
import java.lang.Math;

public class TFIDF
{

    float calculate_score(float termFreq)
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







}
