package main.java.LuceneIndex;

import java.io.IOException;

import main.java.util.LuceneConstants;
import main.java.util.LuceneUtil;
import java.util.Map;
import main.java.lucenerankingmodels.TFIDFSearcher;
import main.java.EvaluationMeasures.EvaluationMeasures;

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
		if( args.length < 3 )
		{
			usage();
		}

		else
		{
				dest = System.getProperty("user.dir")+System.getProperty("file.separator")+"indexed_file";
				LuceneConstants.setIndexFileName(args[0]);
				LuceneConstants.setDirectoryName(dest);

				LuceneConstants.setOutlineCbor(args[1]);
				LuceneConstants.setQrelPath(args[2]);

				//Create the new lucene Index
				LuceneIndexer l = new LuceneIndexer();
				l.getIndexWriter();

				 Map<String,String> p = LuceneUtil.readOutline(LuceneConstants.OUTLINE_CBOR);
//
////
//				TFIDFSearcher defaultSearcher = new TFIDFSearcher("DefaultSearch");
//				defaultSearcher.setDefaultLucene();
//				defaultSearcher.writeRankings(p);
//
//
				 TFIDFSearcher r = new TFIDFSearcher("TFIDF");
				 r.setLNC();
				 r.writeRankings(p);
////
//				 TFIDFSearcher BNN = new TFIDFSearcher("BNN");
//				 BNN.setBNN();
//				 BNN.writeRankings(p);

				EvaluationMeasures t= new EvaluationMeasures();
				double val = t.calculateSpearmanCorrelation("output_DefaultSearch_ranking.txt","output_TFIDF_ranking.txt");
				System.out.println(val);

		}

	}

}

