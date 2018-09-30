package main.java.lucenerankingmodels;



import main.java.LuceneSearch.LuceneSearcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.TopDocs;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import java.io.IOException;


/**
 * @author : Amith RC
 */
public class TFIDFSearcher extends LuceneSearcher
{

    private TFIDFSearcher() throws IOException
    {
        super();
    }

    public TFIDFSearcher(String methodName) throws IOException
    {
        this();
        this.methodName= "tf_idf_"+methodName;
        output_file_name = "output_"+ methodName+"_ranking.txt";
    }

    /**
     *
     * @param tfidf_variant
     * 1-LNC
     * 2-BNN
     * 3-ANC
     * Any other integer should not alter the Searching Method.
     */

    private void  setSearcherSimilarityBase(int tfidf_variant)
    {

        SimilarityBase sb;
        switch(tfidf_variant)
        {
            //Case 1 lnc
            case 1:
                        sb = new SimilarityBase()
                        {
                        @Override
                        protected float score(BasicStats stats, float freq, float docLen)
                        {
                            float TF = (1+ (float) Math.log10(freq));
                            float IDF =(float) Math.log10(((float) stats.getNumberOfDocuments()/ (float) stats.getDocFreq()));
                            return ((TF)/(float)Math.sqrt(docLen));
                        }

                        @Override
                        public String toString()
                        {
                            return null;
                        }
                     };
                    this.searcher.setSimilarity(sb);
                    break;
            //Case 2 bnn
            case 2:
                        sb = new SimilarityBase()
                        {
                        @Override
                        protected float score(BasicStats stats, float freq, float docLen)
                        {
                            if(freq > 0)
                            {
                                return 1;
                            }
                            return 0;
                        }

                        @Override
                        public String toString() {
                            return null;
                            }
                        };
                        this.searcher.setSimilarity(sb);
                        break;

            //Case 1 ANC
            case 3:
                           sb = new SimilarityBase() {
                            @Override
                            protected float score(BasicStats stats, float freq, float docLen) {
                                double res = (0.5 + ((0.5 * freq)/ stats.getTotalTermFreq()) ) / Math.sqrt(docLen);
                                return (float) res;
                            }

                            @Override
                            public String toString() {
                                return null;
                            }
                        };
                        this.searcher.setSimilarity(sb);
                        break;

            default:
                         System.out.println("Default Lucene already set by the Super constructor");
                         break;
        }

    }

    /**
     * Wrapper Function to change the Searching Behavior. This overrides the score function
     * Sets to type LNC
     */

    public void setLNC()
    {
        System.out.println(this.methodName+" is being called");
        setSearcherSimilarityBase(1);
    }

    /**
     * Wrapper Function to change the Searching Behavior. This overrides the score function
     * Sets to type BNN
     */

    public void setBNN()
    {
        System.out.println(this.methodName+" is being called");
        setSearcherSimilarityBase(2);
    }

    /**
     * Wrapper Function to change the Searching Behavior. This overrides the score function
     * Sets to type ANC
     */

    public void setANC()
    {
        System.out.println(this.methodName+" is being called");
        setSearcherSimilarityBase(3);
    }

    /**
     * Wrapper Function to change the Searching Behavior. This overrides the score function
     * Sets to type Default
     */

    public void setDefaultLucene()
    {
        System.out.println(this.methodName+" is being called");        //Calling 4 because it sets the default search of the Lucene so we can compare against it
        setSearcherSimilarityBase(4);
    }

    @Override
    protected TopDocs performSearch(String queryString, int n)
            throws IOException
    {
        try
        {
            queryObj = parser.parse(queryString);
        }
        catch(ParseException e)
        {
            System.out.println(e.getMessage());
        }

        BoostQuery Q = new BoostQuery(queryObj,2);
        return searcher.search(Q, n);
    }


}
