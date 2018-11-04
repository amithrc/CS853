package main.java.LuceneIndex;

import java.io.IOException;

import main.java.util.LuceneConstants;
import main.java.util.LuceneUtil;
import java.util.Map;
import main.java.lucenerankingmodels.TFIDFSearcher;
import main.java.EvaluationMeasures.EvaluationMeasures;
import main.java.LM.LMSearcher;

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
		String dest1;
		String dest2;

		if( args.length < 3 )
		{
			usage();
		}

		else
		{
				dest1 = System.getProperty("user.dir")+System.getProperty("file.separator")+"unigram_Index";
				dest2 = System.getProperty("user.dir")+System.getProperty("file.separator")+"bigram_index";

				LuceneConstants.setIndexFileName(args[0]);

				LuceneConstants.setUnigram(dest1);
			    LuceneConstants.setBigram(dest2);

				LuceneConstants.setOutlineCbor(args[1]);
				LuceneConstants.setQrelPath(args[2]);
				Map<String,Map<String,Integer>> qrel = LuceneUtil.createQrelMap(LuceneConstants.QREL_PATH);

				//Create the new lucene Index for uigram
				LuceneIndexer Index1 = new LuceneIndexer();
				Index1.getIndexWriter(false);

				//create the new lucene Index for bigram
			    LuceneIndexer Index2 = new LuceneIndexer();
			    Index2.getIndexWriter(true);

				Map<String,String> p = LuceneUtil.readOutline(LuceneConstants.OUTLINE_CBOR);

			    LMSearcher laplace = new LMSearcher("laplace");
			    laplace.setLaplace();
			    laplace.writeRankings(p);

				//Map<String,Map<String,Integer>> qrel = LuceneUtil.createQrelMap(LuceneConstants.QREL_PATH);
				//Evaluation Measure
				EvaluationMeasures measures_obj = new EvaluationMeasures(qrel);
				System.out.println( "MAP = " + measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG :" + measures_obj.calculateNDCG());

				LMSearcher bigram = new LMSearcher("Bigram");
				bigram.setBigram();
			    bigram.writeRankings(p);

				System.out.println( "MAP = " + measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG :" + measures_obj.calculateNDCG());
				/*TFIDFSearcher defaultSearcher = new TFIDFSearcher("DefaultSearch");
				defaultSearcher.setDefaultLucene();
				defaultSearcher.writeRankings(p);


				 TFIDFSearcher LNC = new TFIDFSearcher("LNC");
				 LNC.setLNC();
				 LNC.writeRankings(p);

				 TFIDFSearcher BNN = new TFIDFSearcher("BNN");
				 BNN.setBNN();
				 BNN.writeRankings(p);

				 TFIDFSearcher ANC = new TFIDFSearcher("ANC");
				 ANC.setANC();
				 ANC.writeRankings(p);*/


			/*     System.out.println("---------------------------Page Name----------------------------------------");
				 EvaluationMeasures t = new EvaluationMeasures();
				 double val = t.calculateSpearmanCorrelation("output_DefaultSearch_ranking.txt","output_LNC_ranking.txt");
				 System.out.println("SpearMan coefficient (LNC) ="+val);

				 val = t.calculateSpearmanCorrelation("output_DefaultSearch_ranking.txt","output_BNN_ranking.txt");
				 System.out.println("SpearMan coefficient (BNN) ="+val);

				 val = t.calculateSpearmanCorrelation("output_DefaultSearch_ranking.txt","output_ANC_ranking.txt");
				 System.out.println("SpearMan coefficient (ANC) ="+val);

				 System.out.println("-----------------------------------------------------------------------------");

				  Map<String,String> psec = LuceneUtil.readOutlineSectionPath(LuceneConstants.OUTLINE_CBOR);

				  TFIDFSearcher defaultSectionPath = new TFIDFSearcher("DefaultSearchSectionPath");
				  defaultSectionPath.setDefaultLucene();
				  defaultSectionPath.writeRankings(psec);

				  TFIDFSearcher LNCsec = new TFIDFSearcher("LNCSectionPath");
				  LNCsec.setLNC();
				  LNCsec.writeRankings(psec);

				  TFIDFSearcher BNNsec = new TFIDFSearcher("BNNSectionPath");
				  BNNsec.setBNN();
				  BNNsec.writeRankings(psec);

				  TFIDFSearcher ANCsec = new TFIDFSearcher("ANCSectionPath");
				  ANCsec.setANC();
				  ANCsec.writeRankings(psec);

				    System.out.println("---------------------------Section Path----------------------------------------");
					val = t.calculateSpearmanCorrelation("output_DefaultSearchSectionPath_ranking.txt","output_LNCSectionPath_ranking.txt");
					System.out.println("SpearMan coefficient (LNC) SectionPath ="+val);

					val = t.calculateSpearmanCorrelation("output_DefaultSearchSectionPath_ranking.txt","output_BNNSectionPath_ranking.txt");
					System.out.println("SpearMan coefficient (BNN) SectionPath ="+val);

					val = t.calculateSpearmanCorrelation("output_DefaultSearchSectionPath_ranking.txt","output_ANCSectionPath_ranking.txt");
					System.out.println("SpearMan coefficient (ANC) SectionPath="+val);*/


				LMSearcher jmSmoothing = new LMSearcher("JMSmoothing");
				jmSmoothing.setJMSmoothing();
				jmSmoothing.writeRankings(p);

				System.out.println( "MAP = " + measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG = "+measures_obj.calculateNDCG());

			    System.out.println("-----------------------------------------------------------------------------");

			    LMSearcher dirichletSmoothing = new LMSearcher("DirichletSmoothing");
			    dirichletSmoothing.setDirichletSmoothing();
			    dirichletSmoothing.writeRankings(p);
			    
				System.out.println( "MAP = " + measures_obj.calculateMeanAvgPrecision());
				System.out.println("P@R = "+measures_obj.calculatePrecisionAtR());
				System.out.println("NDCG = "+measures_obj.calculateNDCG());
			    
		}

	}

}

