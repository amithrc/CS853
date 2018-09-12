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

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import main.java.LuceneIndex.LuceneConstants;

/**
 * Implementation of the searching algorithms for basic search and term frequency search
 * @author Pooja
 * @LastMod Vaughan - 9/2 Just adding comments
 */

public class LuceneSearcher
{
	//The hard coded queries
	private final String[] QUERY = {"power nap benefits",
			"whale vocalization production of sound",
			"pokemon puzzle league"};
	
	 private final String teamName = "Team 3";

	//Our searcher, parser, and query object initialization
	 private IndexSearcher searcher = null;
	 private QueryParser parser = null;
	 private Query queryObj = null;
	 private String methodName = null;

	    /** 
	     * Creates a new instance of index searcher for basic search and custom search
	     */
	 public LuceneSearcher(Boolean isCustomSearch) throws IOException {
		 
		 
		 if(isCustomSearch) {
			 
		 //Create the searcher object from Lucene constants to get the directory name in the constants
	        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(LuceneConstants.DIRECTORY_NAME))));
	        SimilarityBase2 sb = new SimilarityBase2(); 
	        searcher.setSimilarity(sb);
	        parser = new QueryParser("body", new StandardAnalyzer());
	        methodName = "Custom";
	        
		 }else {
			 
			//Create basic searcher 
		    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(LuceneConstants.DIRECTORY_NAME))));
	        parser = new QueryParser("body", new StandardAnalyzer());
	        methodName = "Standard";
		 }
	    }

	    /**
	     * 
	     * @param queryString
	     * @param n 
	     * @return Top documents for this search
	     * @throws IOException
	     * @throws ParseException
	     */
	    private TopDocs performSearch(String queryString, int n)
	    throws IOException, ParseException {

	    	queryObj = parser.parse(queryString);
	        return searcher.search(queryObj, n);
	    }

	    /**
	     * Get Documents using searcher
	     * @param documents
	     * @return
	     * @throws IOException
	     */
	    private List<String[]> getDocument(TopDocs documents)
	    throws IOException {

	    	//Create a Score document array
			ScoreDoc[] scoringDocuments = documents.scoreDocs;

			//Rank the documents
			List<String[]> rankingDocuments = new ArrayList<>();

			//Loop through scoring documents and process their ranking
			for(int ind=0; ind<scoringDocuments.length; ind++){

				//Get the scoring document
				ScoreDoc scoringDoc = scoringDocuments[ind];

				//Create the rank document from searcher
				Document rankedDoc = searcher.doc(scoringDoc.doc);
//				System.out.println(searcher.explain(queryObj, scoringDoc.doc));

				//Print out the results from the rank document
				String docScore = String.valueOf(scoringDoc.score);
				String paraId = rankedDoc.getField("id").stringValue();
				String paraBody = rankedDoc.getField("body").stringValue();
				String paraRank = String.valueOf(ind+1);
				System.out.println(paraId+" "+docScore+" "+paraBody);

				rankingDocuments.add(new String[] {paraId, paraBody, docScore, paraRank});
			}
	        return rankingDocuments;
	    }
	  

	    /**
	     * Gets the top ranking documents (we currently use top 10)
	     * @return
	     */
	    public List<String[]> getRankingDocuments(){

	    	List<String[]> resultDocs = null;

	    	for(int query_ind = 0; query_ind<QUERY.length; query_ind++){

	    		System.out.println("Searching for: " + QUERY[query_ind]);
	    		
	    		try{
	    			//Query the top 10 documents for this query
					TopDocs searchDocs = performSearch(QUERY[query_ind], 10);
					 resultDocs = getDocument(searchDocs);
						
					
					for(int ind=0; ind< resultDocs.size(); ind++){
						
						
					}

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
	    		System.out.println();

			}
			return resultDocs;

		}
	    
	    
	    /**
	     * 
	     */
	    private List<String> getRankings(ScoreDoc[] scoreDocs, String queryId)
	    	    throws IOException {
	    	
	    	List<String> rankings = new ArrayList<String>();
	    	for(int ind=0; ind<scoreDocs.length; ind++){

				//Get the scoring document
				ScoreDoc scoringDoc = scoreDocs[ind];

				//Create the rank document from searcher
				Document rankedDoc = searcher.doc(scoringDoc.doc);
//				System.out.println(searcher.explain(queryObj, scoringDoc.doc));

				//Print out the results from the rank document
				String docScore = String.valueOf(scoringDoc.score);
				String paraId = rankedDoc.getField("id").stringValue();
				//String paraBody = rankedDoc.getField("body").stringValue();
				String paraRank = String.valueOf(ind+1);
				rankings.add(queryId + " Q0 " + paraId + " " + paraRank + " " + docScore + " "+teamName + "-" + methodName);

			}
	    	
	    	
	    	return rankings;
	    }
	    
	    /**
	     * Output the rankings for Assignment 2
	     * @param p Map containing the query Id and the query value
	     */
	    public void writeRankings(Map<String,String> p)
		{
			for(Map.Entry<String,String> m:p.entrySet())
			{
				try {
					System.out.println(m.getValue());
					TopDocs searchDocs = this.performSearch(m.getValue(), 100);
					
					ScoreDoc[] scoringDocuments = searchDocs.scoreDocs;
					List<String> formattedRankings = this.getRankings(scoringDocuments, m.getKey());
					
					Path file = Paths.get("output.txt");
					Files.write(file, formattedRankings, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
					
				}catch (ParseException e)
				{
					System.out.println(e.getMessage());
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}

			}
		}
	    
	    
	    /**
	     * Private implementation of SimilarityBase for use in advanced search
	     *
	     */
	    private class SimilarityBase2 extends SimilarityBase{
	    	
			@Override
			protected float score(BasicStats basicStats, float v, float v1) {
				return v;
			}

			@Override
			public String toString() {
				return null;
			}
		};

	   
}
