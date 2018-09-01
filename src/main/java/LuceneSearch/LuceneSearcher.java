package main.java.LuceneSearch;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.document.Document;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.LuceneIndex.LuceneConstants;


public class LuceneSearcher
{
	
	private final String[] QUERY = {"power nap benefits",
			"whale vocalization production of sound",
			"pokemon puzzle league"};

	
	 private IndexSearcher searcher = null;
	 private QueryParser parser = null;
	 private Query queryObj = null;

	    /** Creates a new instance of SearchEngine */
	    public LuceneSearcher() throws IOException {
	        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(LuceneConstants.DIRECTORY_NAME))));
	        SimilarityBase sb = new SimilarityBase() {
				@Override
				protected float score(BasicStats basicStats, float v, float v1) {
					return v;
				}

				@Override
				public String toString() {
					return null;
				}
			};
	        searcher.setSimilarity(sb);
	        parser = new QueryParser("body", new StandardAnalyzer());
	    }

	    private TopDocs performSearch(String queryString, int n)
	    throws IOException, ParseException {

	    	queryObj = parser.parse(queryString);
	        return searcher.search(queryObj, n);
	    }

	    private List getDocument(TopDocs documents)
	    throws IOException {

			ScoreDoc[] scoringDocuments = documents.scoreDocs;

			List<String[]> rankingDocuments = new ArrayList<>();

			for(int ind=0; ind<scoringDocuments.length; ind++){

				ScoreDoc scoringDoc = scoringDocuments[ind];
				Document rankedDoc = searcher.doc(scoringDoc.doc);
//				System.out.println(searcher.explain(queryObj, scoringDoc.doc));

				String docScore = String.valueOf(scoringDoc.score);
				String paraId = rankedDoc.getField("id").stringValue();
				String paraBody = rankedDoc.getField("body").stringValue();
				String paraRank = String.valueOf(ind+1);
				System.out.println(paraId+" "+docScore+" "+paraBody);

				rankingDocuments.add(new String[] {paraId, paraBody, docScore, paraRank});
			}
	        return rankingDocuments;
	    }

	    public List getRankingDocuments(){

			List resultDocs = null;

	    	for(int query_ind = 0; query_ind<QUERY.length; query_ind++){

	    		try{
					TopDocs searchDocs = performSearch(QUERY[query_ind], 10);
					resultDocs = getDocument(searchDocs);


					/*for(int r=0; r<resultDocs.size(); r++){
						System.out.println(resultDocs.get(r));
					}*/
				}
				catch (IOException ioe){
					System.out.println(ioe.getMessage());
				}
				catch (ParseException pe){
					System.out.println(pe.getMessage());
				}

			}
			return resultDocs;

		}
	    
	    
}
