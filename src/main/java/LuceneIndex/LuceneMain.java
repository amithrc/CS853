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
		System.out.println("args[0] --> Paragraph CBOR Absolute Path");
		System.out.println("args[1] --> Outlines CBOR Absolute Path");
		System.out.println("args[2] --> Article  Qrel Absolute Path");
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
		if( args.length < 3 )
		{
			usage();
		}
		else
		{
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

				Map<String,Map<String,Integer>> qrel = LuceneUtil.createQrelMap(LuceneConstants.QREL_PATH);
				EvaluationMeasures measures_obj = new EvaluationMeasures(qrel);

				System.out.println("-------------------------------Default Lucene Search---------------------------------");
				System.out.println("MAP ="+ measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+ measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG_20 = " + measures_obj.calculateNDCG());

				LuceneConstants.queryDocPair.clear();

				/*Creates the instance of the Custom scoring function*/
                LuceneSearcher customSearcher = new LuceneSearcher(true);
                customSearcher.writeRankings(p);
				System.out.println("-------------------------------Custom Search----------------------------------------------");
				System.out.println("MAP ="+ measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+ measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG_20 = " + measures_obj.calculateNDCG());

		}

	}

}

