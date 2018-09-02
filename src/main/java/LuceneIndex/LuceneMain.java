package main.java.LuceneIndex;

import main.java.LuceneIndex.LuceneIndexer;
import main.java.LuceneSearch.LuceneSearcher;

import java.io.IOException;

/**
 * Main Class to handle the running of the method
 * @author Team 3
 *
 */
public class LuceneMain
{
	/**
	 * Lets user know they did not use the correct file path
	 */
	private static void  usage()
	{
		System.out.println("Please pass the index file absolute path");
		System.exit(-1 );
	}

	/**
	 * Run the queries
	 * @param args file path for the corpus
	 * @throws IOException if things go wrong
	 */
	public static void main(String[] args) throws IOException
	{
		String dest;
		System.out.println("Please pass the file to be indexed");
		if( args.length < 1 )
		{
			usage();
		}
		else
		{
				dest = System.getProperty("user.dir")+System.getProperty("file.separator")+"indexed_file";
				String[] mode_input = new String[] {"paragraphs", args[0]};
				
				//Sets the file directory that the corpus is coming from
				LuceneConstants.setIndexFileName(args[0]);
				LuceneConstants.setDirectoryName(dest);
				
				//Create the new lucene Index
				LuceneIndexer l = new LuceneIndexer();
				l.setMode(mode_input);
				l.getIndexWriter(dest);
				l.closeIndexWriter();
				
				System.out.println();
                System.out.println("Starting the basic search...");
                
                //Run basic search
                LuceneSearcher basicSearcher = new LuceneSearcher(false);
                basicSearcher.getRankingDocuments();
                
                System.out.println();
				System.out.println("Starting the Custom search...");
				
				//Run advanced search
                LuceneSearcher customSearcher = new LuceneSearcher(true);
                customSearcher.getRankingDocuments();
                
                



		}

		}

}

