package main.java.LuceneIndex;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import edu.unh.cs.treccar_v2.Data;

import java.io.IOException;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Creates the index for the given corpus
 * @author Team 3!
 *
 */
class LuceneIndexer
{


	   private IndexWriter indexWriter = null;
	   private String[] mode = null;

	   /**
	    * Prepares the indexwriter for use in searching later
	    * @param relative_path
	    * @return gets indexwriter, if it has previously been created it will return the old index writer
	    * 		 if its hasn't been created we parse the paragraph and pass back
	    * @throws IOException
	    */
	    IndexWriter getIndexWriter(String relative_path) throws IOException {
	    	
	    	//If we haven't created and indexwriter yet
	        if (indexWriter == null) {
	        	
	        	//Get the path of the index
	            Directory indexDir = FSDirectory.open(Paths.get(relative_path));
	            
	            //Create the configuration for the index
	            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	            
	            //Create the IndexWriter
	            indexWriter = new IndexWriter(indexDir, config);
	            
	            //Parse the paragraphs and return the indexwriter with the corpus indexed
	            indexWriter = parseParagraph(indexWriter);
	           
	        }
	        return indexWriter;
	   }
	    
	 /**
	  * Actually parses the paragraph from the mode parameters
	  * @param indexWriter generated indexwriter to add doc to
	  * @return indexwriter with docs added
	  */
	 private IndexWriter parseParagraph(IndexWriter indexWriter) {
		 
		 //Preparation for future changes based on trec-car github examples
		 if (mode[0].equals("paragraphs")) {
			 
			 //We added the paragraphs in the main so that we can process
	            String paragraphsFile = mode[1];
	            
	            //We are trying to process the input from the fileinputstream
	            FileInputStream fileInputStream2 = null;
	            try {
	            	fileInputStream2 = new FileInputStream(new File(paragraphsFile));
	            }catch(FileNotFoundException fnf) {
	            	System.out.println(fnf.getMessage());
	     
	            }
	            
	            //For each of the paragraphs from the deserialized inputstream
	            for(Data.Paragraph p: DeserializeData.iterableParagraphs(fileInputStream2))
	            {
	            	
	            	//We create a document
	            	  System.out.println("Indexing "+ p.getParaId());
	            	  Document doc = new Document();
	            	  
	            	  //Then we add the paragraph id and the paragraph body for searching
	            	  doc.add(new StringField("id", p.getParaId(), Field.Store.YES));
	            	  doc.add(new TextField("body", p.getTextOnly(), Field.Store.YES));
	            	  
	            	  //From here we add the document to the indexwriter
	            	  try {
	            		  indexWriter.addDocument(doc);
	            	  }catch(IOException ioe) {
	            		  System.out.println(ioe.getMessage());
	            	  }
	            }
	            
		 }
		 //we return the indexwriter with the paragraphs added
		 return indexWriter;
		 
	 }

	 /**
	  * Closes the indexwriter so that we can use it in searching
	  * @throws IOException
	  */
	 void closeIndexWriter() throws IOException {
	        if (indexWriter != null) {
	            indexWriter.close();
	        }
	   }
	
	    /**
	     * Sets the corpus for the index writer
	     * @param input
	     */
	   void setMode(String[] input)
	   {
	    	mode = input;
	   }
	
}