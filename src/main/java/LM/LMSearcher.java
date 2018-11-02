package main.java.LM;
import main.java.LuceneSearch.LuceneSearcher;
import main.java.util.LuceneConstants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import java.io.IOException;

public class LMSearcher extends LuceneSearcher{

    private LMSearcher() throws IOException{

        super();
    }
    public LMSearcher(String methodName) throws IOException{
        this();
        this.methodName = methodName;
        output_file_name = "output_"+ methodName+"_ranking.txt";
    }
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
                        return null;
                    }
                };
                this.searcher.setSimilarity(sb);
                break;
            case 2:
                sb = new SimilarityBase() {
                    @Override
                    protected float score(BasicStats basicStats, float freq, float docLn) {
                        float prob_term_doc = ((LuceneConstants.lambda*(freq/docLn))+(1-LuceneConstants.lambda)*(basicStats.getNumberOfFieldTokens()));
                        return prob_term_doc;
                    }

                    @Override
                    public String toString() {
                        return null;
                    }
                };
                this.searcher.setSimilarity(sb);
                break;
            }
        }




     public void setLaplace(){

        System.out.println(this.methodName + " is being called");
         setSearchSimilarityBase(1);

     }

    public void setJMSmoothing(){

        System.out.println(this.methodName + " is being called");
        setSearchSimilarityBase(2);

    }

}