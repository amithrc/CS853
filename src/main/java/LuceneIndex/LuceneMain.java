package main.java.LuceneIndex;

import main.java.LuceneIndex.LuceneIndexer;
import main.java.LuceneSearch.LuceneSearcher;
import main.java.EvaluationMeasures.EvaluationMeasures;

import java.io.IOException;
import main.java.util.LuceneUtil;
import java.util.Map;

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
				/*
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
                */

				//args[0] --> Paragraph file index
				//args[1] --> Outlines CBOR file
				//args[2] --> Article Qrels


				String[] mode_input = new String[] {"paragraphs", args[0]};

				dest = System.getProperty("user.dir")+System.getProperty("file.separator")+"indexed_file";

				//Sets the file directory that the corpus is coming from

				LuceneConstants.setIndexFileName(args[0]);
				LuceneConstants.setDirectoryName(dest);

				LuceneConstants.setOutlineCbor(args[1]);
				LuceneConstants.setQrelPath(args[2]);

				//Create the new lucene Index
				LuceneIndexer l = new LuceneIndexer();

				l.setMode(mode_input);
				l.getIndexWriter(dest);
				l.closeIndexWriter();


				/* Reading outline file and it returns the PageID as the key and pageName as its value*/

				Map<String,String> p = LuceneUtil.readOutline(LuceneConstants.OUTLINE_CBOR);

				 /*Creates the instance of the basic search*/
                LuceneSearcher basicSearcher = new LuceneSearcher(false);
                basicSearcher.writeRankings(p);

				/*Creates the instance of the Custom scoring function*/

                LuceneSearcher customSearcher = new LuceneSearcher(true);
                customSearcher.writeRankings(p);

                /*Returns the Map of Ground Truth from the Qrel file
				/*Key QueryID ParaID= InnerKey IsRelevant Value for the InnnerKey */


			      Map<String,Map<String,Integer>> qrel = LuceneUtil.createQrelMap(LuceneConstants.QREL_PATH);

			      //Evaluation Measure
			      EvaluationMeasures measures_obj = new EvaluationMeasures(qrel);
			      System.out.println( "MAP = " + measures_obj.calculateMeanAvgPrecision());
			      System.out.println("P@R = "+measures_obj.calculatePrecisionAtR());

		}

	}

}

