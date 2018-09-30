package main.java.EvaluationMeasures;


import java.util.HashMap;
import java.util.Map;
import java.util.*;
import  java.lang.Math;

import main.java.util.LuceneConstants;
import main.java.util.LuceneUtil;

public class EvaluationMeasures{

    public Map<String,Map<String,Integer>> qrel_data;

    private Map<String, Double> mean_avg_precison = new HashMap<String, Double>();
    private final static int TOTAL = 20;

    public EvaluationMeasures(){}
    public EvaluationMeasures(Map<String,Map<String,Integer>> qrel)
    {
        qrel_data = qrel;
    }

    private int getQrelRelevancy(String query_id, String doc_id){
        if(qrel_data.containsKey(query_id)){
            Map<String,Integer> temp = qrel_data.get(query_id);
            if(temp.containsKey(doc_id)){
                return temp.get(doc_id);
            }
            return -1;
        }
        return -1;
    }

    private void getAvgPrecision(){
        //calculate average precision of every query

        for (Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPair.entrySet()){

            String queryId = query.getKey();
            Map<String,Integer> docIdRank= query.getValue();
            int query_count = 0;
            int ranking_rel_count = 0;
            double avg_precision = 0.0;

            for(Map.Entry<String,Integer> document: docIdRank.entrySet()){
                query_count = query_count + 1;
                //check if the given paragraph document is relevant for the query or not
                if(getQrelRelevancy(queryId, document.getKey()) == 1){
                    ranking_rel_count = ranking_rel_count + 1;
                    avg_precision = avg_precision + (ranking_rel_count/(double) query_count);
                }

            }
            int rel_docs_count = LuceneUtil.relevancy_count(qrel_data, queryId); //get the total true relevant docs for the query
            if(rel_docs_count == 0){ //if the total true relevant docs are 0
                avg_precision = 0.0;
            }else{
                avg_precision = avg_precision/rel_docs_count;
            }
            mean_avg_precison.put(queryId, avg_precision);
        }
    }

    /**
     * Author- Pooja oza
     * Calculate MAP
     */

    public double calculateMeanAvgPrecision()
    {
        //Calculate the average precision of every query and then take mean
        getAvgPrecision();
        double MAP = 0.0;
        double totalAP = 0.0;
        for (Map.Entry<String, Double> avgPrec : mean_avg_precison.entrySet()){
            totalAP = totalAP + avgPrec.getValue();
        }
        int total_size = mean_avg_precison.size();
        //Take the mean of the APs of the queries
        if(total_size != 0){
            MAP = totalAP/total_size;
        }
        return MAP;

    }

    /**
     * Author- Amith RC
     * Calculate Precision@R
     */

    public double calculatePrecisionAtR()
    {
        //To know the number Queries processed

        int number_of_query_processed=0;

        double pATr = 0.0;


        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet())
        {
            // To have the Precision @ R Computed for each Query.
            double res =0.0;
            //Getting the Key
            String QueryID = Query.getKey();

            // Track of the Queries Processed so Far
            number_of_query_processed+=1;

            //Getting the Relevant count from the Ground Truth
            int relevant_count = LuceneUtil.relevancy_count(qrel_data,QueryID);

            //Inner value which holds the Para_ID
            Map<String,Integer> para_id = Query.getValue();

            //Number of ParaID
            int number_of_para_id = para_id.size();

            //Variable to Break the inner loop when it reaches the Relevant_count
            int BreakLoop=(number_of_para_id > relevant_count ) ? relevant_count: number_of_para_id;

            // Keep the current Iteration

            int counter =0;
            int is_relevant_counter = 0;

            for(Map.Entry<String,Integer> P_ID: para_id.entrySet())
            {
                if(getQrelRelevancy(QueryID,P_ID.getKey()) == 1)
                {
                    is_relevant_counter += 1;

                }
                counter+=1;

                if(counter == BreakLoop)
                {
                    break;
                }

            }
            try
            {
                res = (double) is_relevant_counter / relevant_count;
                pATr+= res;
            }
            catch (ArithmeticException e)
            {
                System.out.println(e.getMessage());
            }

        }
        return (pATr/number_of_query_processed);
    }
    

    /**
     * 
     * Caculate DCG based on the formula from lecture: Sum from i=1 to p of (2^relevancy of i -1)/(log base 2 of (i+1)
     * we are using NDCG at 20 so I have added the at_Value variable to capture this
     * @return
     */
    public double calculateDCG(String queryId,  Map<String,Integer> paraId) {

    	int at_Value = 20;
    	
    	//for (Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPair.entrySet()){

    		Double CGVal = 0.0;
            //Inner value which holds the Para_ID
            
            //Tracking for which paragraph we are at (This is a substitute for i in the formula)
            int currentParagraph = 0;
    		
    		 for(Map.Entry<String,Integer> paragraph: paraId.entrySet()){
    			 currentParagraph++;
    			 
    			 //relevancy of i
    			 int relevancy = getQrelRelevancy(queryId,paragraph.getKey()) == 1 ? 1 : 0;
    			 
    			 //System.out.println("VC - printing relevancy: " + relevancy);
    			 
    			 //System.out.println("VC - Adding to cg: " + (Math.pow(2, relevancy) -1)/(Math.log(currentParagraph+1)/Math.log(2)));
    			 CGVal += (Math.pow(2, relevancy) -1)/(Math.log(currentParagraph+1)/Math.log(2));
    			 
    			 
    			 if(currentParagraph >= at_Value) {
    				 break;
    			 }
    			 
             }
    		 
    		 //System.out.println("VC - " + queryId+' '+CGVal);
    	     return CGVal;
        }
    
    public double calculateiDCG(String query_id, Map.Entry<String, Map<String,Integer>> query) {
    	return 0.0;
    }
    
    
    public double calculateNDCG() {
    		Map<String, Map<String,Integer>> idealQueryDocPair = LuceneUtil.createQrelMap(LuceneConstants.QREL_PATH);
    		
    		double tempndcgVal = 0.0;
    		int count = 0;
    	
    		for(Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPair.entrySet()){
    			 
    			  count++;
		    	  double dcgVal = this.calculateDCG(query.getKey(),query.getValue());
		    	  double idcgVal = this.calculateDCG(query.getKey(), idealQueryDocPair.get(query.getKey()));

		    	  tempndcgVal += dcgVal/idcgVal;

		    	  //System.out.println("VC - NDCG" + query.getKey()+' '+dcgVal/idcgVal);
		    	  
		      }
    		//System.out.println("VC - NDCG Total: " + tempndcgVal/count);

    		return tempndcgVal/count;
    }
    	

    /*Below NDCG Algorithm written by Sepeidh Koofhar, Taken From her branch
    * We had issues merging her branch so I had to commit on her behalf
    * @Author- Sepeidh Koofhar*/



    public Double calculateNDCG20() {
        double NDCG = 0.0;
        int counter = 0;

        for (Map.Entry<String, Map<String, Integer>> Query : LuceneConstants.queryDocPair.entrySet()) {
            NDCG += calculateDCG20(Query) / calculateIDCG20(Query);
            counter++;
        }

        NDCG /= counter;

        return NDCG;

    }

    private Double calculateDCG20(Map.Entry<String, Map<String, Integer>> Query) {
        double DCG = 0.0;
        int counter = 1;
        Map<String, Integer> docIDRank = Query.getValue();
        ArrayList<Integer> grades = new ArrayList<>();

        for (Map.Entry<String, Integer> row : docIDRank.entrySet()) {

            if ((getQrelRelevancy(Query.getKey(), row.getKey())) == 1) {
                grades.add(1);
            } else grades.add(0);

            if (counter <= TOTAL) {
                DCG += (Math.pow(2, grades.get(counter - 1))) / (Math.log(counter + 1));
                counter++;
            } else break;
        }

        return DCG;
    }

    private Double calculateIDCG20(Map.Entry<String, Map<String, Integer>> Query) {

        double IDCG = 0.0;
        int counter = 1;
        Map<String, Integer> docIDRank = Query.getValue();
        ArrayList<Integer> grades = new ArrayList<>();

        for (Map.Entry<String, Integer> row : docIDRank.entrySet()) {

            if ((getQrelRelevancy(Query.getKey(), row.getKey())) == 1) {
                grades.add(1);
            } else grades.add(0);

            Collections.sort(grades, Collections.reverseOrder());

            if (counter <= TOTAL) {
                IDCG += (Math.pow(2, grades.get(counter - 1))) / (Math.log(counter + 1));
                counter++;
            } else break;
        }
        return IDCG;
    }



    public double calculateSpearmanCorrelation(String runFile1, String runFile2)
    {
        double Correlation = 0.0;
        int nQuery=0;

        Map<String, Map<String, Integer>> LuceneDefault = LuceneUtil.readRunFile(runFile1);
        Map<String, Map<String, Integer>> customTFIDF = LuceneUtil.readRunFile(runFile2);

        for (Map.Entry<String, Map<String, Integer>> Query : LuceneDefault.entrySet())
        {

            nQuery++;

            //Summation of d^2
            int dsquare=0;

            //Number of pairs
            int n = 0;

            //Started as the current Document ID for the Default Lucene search.
            int currentDocRank = 0;


            for (Map.Entry<String, Integer> p : Query.getValue().entrySet())
            {
                    int diff=0;

                    currentDocRank++;

                    Integer docRank = LuceneUtil.docRanking(customTFIDF,Query.getKey(),p.getKey());
                    n++;
                    diff = currentDocRank - docRank;
                    dsquare += Math.pow(diff,2);

                    //System.out.println(Query.getKey()+","+p.getKey()+","+val);

//                    if(docRank!=0)
//                    {
//                        n++;
//                        diff = currentDocRank - docRank;
//                        dsquare += Math.pow(diff,2);
//                    }

            }


            double num = 6*dsquare;
            double denom = n *((Math.pow(n,2))-1);

            if(denom == 0)
            {
                continue;
            }

            double result = 1-( num /denom);


            Correlation+= result;
        }

        return (Correlation/nQuery);

    }




}
