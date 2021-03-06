package main.java.LuceneIndex;
import main.java.util.LuceneConstants;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
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
		private IndexWriter indexWriter;

		LuceneIndexer() {
			indexWriter = null;
		}

	   /**
	    * Prepares the indexwriter for use in searching later
	    * @return gets indexwriter, if it has previously been created it will return the old index writer
	    * 		 if its hasn't been created we parse the paragraph and pass back
	    * @throws IOException
	    */

	    public void getIndexWriter(boolean isBigram) throws IOException {

	    	//If we haven't created and indexwriter yet
	        if (indexWriter == null)
	        {

	        	//Get the path of the index

	            //Create the configuration for the index
				if(isBigram){

					Directory indexDir = FSDirectory.open(Paths.get(LuceneConstants.BIGRAM_DIRECTORY));
					IndexWriterConfig config = new IndexWriterConfig(new ShingleAnalyzerWrapper(2, 2));
					config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

					//Create the IndexWriter
					indexWriter = new IndexWriter(indexDir, config);

					//Parse the paragraphs and return the indexwriter with the corpus indexed
					parseParagraph(indexWriter);

				}else {

					Directory indexDir = FSDirectory.open(Paths.get(LuceneConstants.UNIGRAM_DIRECTORY));
					IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
					config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

					//Create the IndexWriter
					indexWriter = new IndexWriter(indexDir, config);

					//Parse the paragraphs and return the indexwriter with the corpus indexed
					parseParagraph(indexWriter);
				}
	        }
	   }
	    
	 /**
	  * Actually parses the paragraph from the mode parameters
	  * @param indexWriter generated indexwriter to add doc to
	  * @return indexwriter with docs added
	  */
	 private void parseParagraph(IndexWriter indexWriter)
	 {
		 
				// this function shoudl take care of the Reading the CBOR file and indexing it
	            FileInputStream fileInputStream2 = null;
	            try {
	            	fileInputStream2 = new FileInputStream(new File(LuceneConstants.FILE_NAME));
	            }catch(FileNotFoundException fnf) {
	            	System.out.println(fnf.getMessage());
	     
	            }

	            int increment=0;
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
						  increment++;

						  //commit the Data after 50 paragraph

						  if(increment % 50 ==0)
						  {
								indexWriter.commit();
						  }
	            	  }catch(IOException ioe) {
	            		  System.out.println(ioe.getMessage());
	            	  }
	            }
			closeIndexWriter();
	            
		 }



	 /**
	  * Closes the indexwriter so that we can use it in searching
	  * @throws IOException
	  */
	 private void closeIndexWriter()
	 {
	        if (indexWriter != null)
	        {
	        	try
				{
					indexWriter.close();
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}

	        }
	   }

}