package main.java.LuceneSearch;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.document.Document;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;


public class LuceneSearcher
{
	
	static final String INDEX_DIRECTORY = "Index";

	
	 private IndexSearcher searcher = null;
	    private QueryParser parser = null;

	    /** Creates a new instance of SearchEngine */
	    public LuceneSearcher() throws IOException {
	        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIRECTORY))));
	        parser = new QueryParser("content", new StandardAnalyzer());
	    }

	    public TopDocs performSearch(String queryString, int n)
	    throws IOException, ParseException {
	        Query query = parser.parse(queryString);
	        return searcher.search(query, n);
	    }

	    public Document getDocument(int docId)
	    throws IOException {
	        return searcher.doc(docId);
	    }
	    
	    
}
