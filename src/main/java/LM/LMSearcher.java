package main.java.LM;
import main.java.LuceneSearch.LuceneSearcher;
import main.java.util.LuceneConstants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import java.io.IOException;

/**
 * A language model approach for Lucene Searcher using unigram model approaches
 *
 */

public class LMSearcher extends LuceneSearcher{

    private LMSearcher() throws IOException{

        super();
    }
    
    /**
     * Language Model Searcher Constructor
     * @param methodName
     * @throws IOException
     */
    public LMSearcher(String methodName) throws IOException{
        this();
        this.methodName = methodName;
        output_file_name = "output_"+ methodName+"_ranking.txt";
    }
    
    /**
     * Sets the similarity base for the searcher
     * @param LM_varient 1- Laplace 2- JM smoothing 3- Dirichlet
     */
     private void setSearchSimilarityBase(int LM_varient){

        SimilarityBase sb;

        switch(LM_varient) {

            //case 1 : laplace smoothing
            case 1:
                sb = new SimilarityBase() {
                    @Override
                    protected float score(BasicStats basicStats, float freq, float docLn) {
                        float numerator = freq + 1;
                        Long vocabSize = new Long(basicStats.getNumberOfFieldTokens());
                        float denominator = docLn + vocabSize.floatValue();
                        return numerator / denominator;
                    }

                    @Override
                    public String toString() {
                        return "Laplace Smoothing";
                    }
                };
                this.searcher.setSimilarity(sb);
                break;
            //Case 2 JM
            case 2:
                sb = new SimilarityBase() {
                    @Override
                    protected float score(BasicStats basicStats, float freq, float docLn) {
                        float prob_term_doc = ((LuceneConstants.lambda*(freq/docLn))+(1-LuceneConstants.lambda)*(basicStats.getNumberOfFieldTokens()));
                        return prob_term_doc;
                    }

                    @Override
                    public String toString() {
                        return "JM Smoothing";
                    }
                };
                this.searcher.setSimilarity(sb);
                break;
            //Case 3 - Dirichlet
            case 3:
            	sb = new SimilarityBase() { 
            		@Override
            		protected float score(BasicStats bs, float freq, float docln) {
            			float prob_term_doc = docln/ (docln + LuceneConstants.Mu);
            			return prob_term_doc;
            		}
            		  @Override
                      public String toString() {
                          return "Dirichlet Smoothing";
                      }
            	};
            	this.searcher.setSimilarity(sb);
            	break;
            }
        }



     //Set Laplace smoothing as the smoothing method
     public void setLaplace(){

        System.out.println(this.methodName + " is being called");
         setSearchSimilarityBase(1);

     }

    //Set Jelinek Mercer as the smoothing method
    public void setJMSmoothing(){

        System.out.println(this.methodName + " is being called");
        setSearchSimilarityBase(2);

    }
    
    //Set Dirichlet smoothing as the smoothing method
    public void setDirichletSmoothing(){

        System.out.println(this.methodName + " is being called");
        setSearchSimilarityBase(2);

    }

}