package main.java.LuceneIndex;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.StringField;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import edu.unh.cs.treccar_v2.Data;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LuceneIndexer{
	
	   private IndexWriter indexWriter = null;
	   private String[] mode = null;

	    public IndexWriter getIndexWriter(String relative_path) throws IOException {
	        if (indexWriter == null) {
	            Directory indexDir = FSDirectory.open(Paths.get(relative_path));
	            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	            indexWriter = new IndexWriter(indexDir, config);
	            indexWriter = parseParagraph(indexWriter);
	           
	        }
	        return indexWriter;
	   }
	    
	 public IndexWriter parseParagraph(IndexWriter indexWriter) {
		 if (mode[0].equals("paragraphs")) {
	            String paragraphsFile = mode[1];
	            FileInputStream fileInputStream2 = null;
	            try {
	            	fileInputStream2 = new FileInputStream(new File(paragraphsFile));
	            }catch(FileNotFoundException fnf) {
	            	System.out.println(fnf);
	     
	            }
	            for(Data.Paragraph p: DeserializeData.iterableParagraphs(fileInputStream2))
	            {
	            	  System.out.println("Indexing "+ p.getParaId());
	            	  Document doc = new Document();
	            	  doc.add(new StringField("id", p.getParaId(), Field.Store.YES));
	            	  doc.add(new TextField("body", p.getTextOnly(), Field.Store.YES));
	            	  try {
	            	  indexWriter.addDocument(doc);
	            	  }catch(IOException ioe) {
	            		  System.out.println(ioe.getMessage());
	            	  }
	            	  System.out.println();
	            }
	            
		 }
		 return indexWriter;
		 
	 }

	    public void closeIndexWriter() throws IOException {
	        if (indexWriter != null) {
	            indexWriter.close();
	        }
	   }
	
	   public void setMode(String[] input) {
	    	mode = input;
	   }
	
}