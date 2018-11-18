package main.java.RankLib;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import main.java.LM.LMSearcher;
import main.java.lucenerankingmodels.TFIDFSearcher;
import main.java.util.LuceneUtil;
import main.java.util.LuceneConstants;

public class LearningToRank {

	//Old: Map<Ranking Function, query, doc, vector and ranking float
	//New: Map<Query Function, doc, ranking, vector and ranking float
    private Map<String, Map<String, Map<String, float[]>>> ranking_pairs = new LinkedHashMap<String, Map<String, Map<String, float[]>>>();
    private Map<String,Map<String,Integer>> qrel_data;

    /**
     * Constructor that sets the training data
     * @param qrel
     */
    public LearningToRank(Map<String,Map<String,Integer>> qrel){
        qrel_data = qrel;
    }

    /**
     * Created the rankings
     * @throws IOException
     */
    public void generateRanklibFile() throws IOException{
        getRankings();
        writeRankingDoc("rankingDoc.txt");
    }
    
    /**
     * Pulls the qrel relevancy from the qrel data for a given doc associated with the given query
     * @param query_id
     * @param doc_id
     * @return
     */
    private int getQrelRelevancy(String query_id, String doc_id){
        if(qrel_data.containsKey(query_id)){
            Map<String,Integer> temp = qrel_data.get(query_id);
            //System.out.println("Qrel Data: " + temp);
            if(temp.containsKey(doc_id)){
                return temp.get(doc_id);
            }
            return -1;
        }
        return -1;
    }

    /**
     * Receives the ranking function, query and rank for each document, generating a ranking pair for the document/function
     * @param function_key
     * @param query_key
     * @param doc_id
     * @param rank
     */
    private void createRankingPair(String function_key, String query_key, String doc_id, float[] rank)
    {
    	
    	   if(ranking_pairs.containsKey(query_key))// query_key
           {
               Map<String, Map<String, float[]>> extract = ranking_pairs.get(query_key); //query_key
               if(extract.containsKey(doc_id)){ //function_key //doc_id
                   Map<String, float[]> function_extract = extract.get(doc_id);//function_key //doc_id
                   if(!function_extract.containsKey(function_key)) { //function_key
                       //Map<String, float[]> temp = new LinkedHashMap<String, float[]>();
                       function_extract.put(function_key, rank); //function_key
                       System.out.println("first section adding: " + function_key);
                       extract.put(doc_id, function_extract);//function_key //doc_id
                       ranking_pairs.put(query_key, extract);
                   }else {
                	   System.out.println("Error");
                   }
               }
               else{
                   //Map<String, Map<String, float[]>> query_temp = new LinkedHashMap<String, Map<String, float[]>>();
                   Map<String, float[]> function_temp = new LinkedHashMap<String, float[]>();
                   System.out.println("Middle section adding: " + function_key);
                   function_temp.put(function_key, rank);//function_key and function_temp
                   extract.put(doc_id, function_temp);//function_key //doc_id and function_temp
                   //extract.put(function_key, query_temp);
                   ranking_pairs.put(query_key, extract);
               }

           }
           else
           {
               Map<String, Map<String, float[]>> doc_temp = new LinkedHashMap<String, Map<String, float[]>>();
               Map<String, float[]> function_temp = new LinkedHashMap<String, float[]>(); // function_temp
               System.out.println("Last section adding: " + function_key);
               function_temp.put(function_key, rank);//function_temp function_key
               doc_temp.put(doc_id, function_temp);//function_key // doc_id doc_temp
               ranking_pairs.put(query_key, doc_temp);//query_key //doc_temp
           }
    }

    /**
     * Group rankings by each ranking method
     * @throws IOException
     */
    private void getRankings() throws IOException {
        Map<String,String> p = LuceneUtil.readOutline(LuceneConstants.OUTLINE_CBOR);

        // create Ranking pair for TFIDF : LNC

        TFIDFSearcher LNC = new TFIDFSearcher("LNC");
        LNC.setLNC();
        LNC.writeRankings(p);

        callcreateRankingPair("LNC");

        //create Ranking pair for TFIdf : bnn

        TFIDFSearcher BNN = new TFIDFSearcher("BNN");
        BNN.setBNN();
        BNN.writeRankings(p);

        callcreateRankingPair("BNN");

        // create Ranking pair fot Unigram Language model: Laplace

        LMSearcher laplace = new LMSearcher("laplace");
        laplace.setLaplace();
        laplace.writeRankings(p);

        callcreateRankingPair("laplace");

        //create Ranking pair for Unigram Language model : JM

        LMSearcher jmSmoothing = new LMSearcher("JMSmoothing");
        jmSmoothing.setJMSmoothing();
        jmSmoothing.writeRankings(p);

        callcreateRankingPair("JMSmoothing");

        // create Ranking pair for Unigram Language model : Dirichlet

        LMSearcher dirichletSmoothing = new LMSearcher("DirichletSmoothing");
        dirichletSmoothing.setDirichletSmoothing();
        dirichletSmoothing.writeRankings(p);

        callcreateRankingPair("DirichletSmoothing");
        }
    
    /**
     * Collects the function, queryID and relevancy to process each document ranking pair using the create ranking pair function
     * @param function_key
     */
    private void callcreateRankingPair(String function_key){

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()) {
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document : docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0);
                float docval= (float)1.0 /document.getValue();
               createRankingPair(function_key, queryID, document.getKey(), new float[]{docval, relevancy});

            }
        }
    }
    
    
    private void writeRankingDoc(String fileName) throws IOException {
      Path path = Paths.get(fileName);
      
	try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
		
		//Null check
			if(this.ranking_pairs!= null && this.ranking_pairs.size() >0) {
				
				//New: Map<Query Function, map <doc, Map< ranking, {vector and ranking float}>>>
				
				//Iterate through ranking pairs
				for(String qid: ranking_pairs.keySet()) {
					//Start ranklib format doc
					
					for(String doc: ranking_pairs.get(qid).keySet()){
						boolean targetSet = false;
						String line = " qid:" + qid + " ";
						
						for(String rankingFunction: ranking_pairs.get(qid).get(doc).keySet()) {
							//System.out.println(ranking_pairs.get(qid).get(doc).keySet());
							float[] f = ranking_pairs.get(qid).get(doc).get(rankingFunction);
							
							//If we didnt initialize the target, put it at the front
							if(!targetSet) {
								targetSet = true;
								int targetVal = (int) f[1];
								line = targetVal + line;
							}
							
							//<target> qid:<qid> <feature>:<value> <feature>:<value> ... <feature>:<value> # <info>
							line += rankingFunction + ":" + f[0] + " ";
							
						}
						line += "#" + doc;
						writer.write(line);
						writer.newLine();
						
					}
				
					
					
				}
    		
			}
			//writer.write(ranking_pairs.toString());

      }
    }
}


