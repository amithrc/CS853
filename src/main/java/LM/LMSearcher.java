package main.java.LM;
import main.java.LuceneSearch.LuceneSearcher;
import main.java.util.LuceneConstants;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LMSearcher extends LuceneSearcher{
    protected IndexSearcher searcher = null;

    private LMSearcher() throws IOException{

        super();
    }
    public LMSearcher(String methodName) throws IOException{
        this();
        if(methodName == "Bigram"){
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(LuceneConstants.BIGRAM_DIRECTORY))));
            parser = new QueryParser("body", new ShingleAnalyzerWrapper(2, 2));
        }
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
                        return (float)Math.log(numerator / denominator);
                    }

                    @Override
                    public String toString() {
                        return null;
                    }
                };
                System.out.println(super.searcher);
                super.searcher.setSimilarity(sb);
                break;
            case 2:
                sb = new SimilarityBase() {
                    @Override
                    protected float score(BasicStats basicStats, float freq, float docLn) {
                        float numerator = freq + 1;
                        float denominator = docLn;
                        return numerator / denominator;
                    }

                    @Override
                    public String toString() {
                        return null;
                    }
                };

            case 4:
                sb = new SimilarityBase() {
                    @Override
                    protected float score(BasicStats basicStats, float freq, float docLn) {
                        float numerator = freq + 1;
                        Long vocabSize = new Long(basicStats.getNumberOfFieldTokens());
                        float denominator = docLn + vocabSize.floatValue();
                        return (float)Math.log(numerator / denominator);
                    }

                    @Override
                    public String toString() {
                        return null;
                    }
                };
                System.out.println(this.searcher);
                searcher.setSimilarity(sb);
                break;
            }
        }




     public void setLaplace(){

        System.out.println(this.methodName + " is being called");
         setSearchSimilarityBase(1);

     }

     public void setBigram(){

         System.out.println(this.methodName + " is being called");
         setSearchSimilarityBase(4);
     }

}