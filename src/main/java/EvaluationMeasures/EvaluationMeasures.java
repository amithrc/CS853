package main.java.EvaluationMeasures;


import java.util.HashMap;
import java.util.Map;

import main.java.LuceneIndex.LuceneConstants;
import main.java.util.LuceneUtil;

public class EvaluationMeasures{

    public Map<String,Map<String,Integer>> qrel_data;

    private Map<String, Double> mean_avg_precison = new HashMap<String, Double>();

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

        for (Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPairRead.entrySet()){

            String queryId = query.getKey();
            Map<String,Integer> docIdRank= query.getValue();
            int query_count = 0;
            int ranking_rel_count = 0;
            double avg_precision = 0.0;

            for(Map.Entry<String,Integer> document: docIdRank.entrySet()){
                query_count = query_count + 1;
                if(getQrelRelevancy(queryId, document.getKey()) == 1){
                    ranking_rel_count = ranking_rel_count + 1;
                    avg_precision = avg_precision + (double)(ranking_rel_count/query_count);
                }

            }
            int rel_docs_count = LuceneUtil.relevancy_count(qrel_data, queryId);
            if(rel_docs_count == 0){
                avg_precision = 0.0;
            }else{
                avg_precision = avg_precision/rel_docs_count;
            }
            mean_avg_precison.put(queryId, avg_precision);
        }
    }

    public double calculateMeanAvgPrecision()
    {
        getAvgPrecision();
        double MAP = 0.0;
        double totalAP = 0.0;
        for (Map.Entry<String, Double> avgPrec : mean_avg_precison.entrySet()){
            totalAP = totalAP + avgPrec.getValue();
        }
        int total_size = mean_avg_precison.size();
        if(total_size != 0){
            MAP = totalAP/total_size;
        }
        return MAP;

    }

    public double calculatePrecisionAtR()
    {
        //To know the number Queries processed

        int number_of_query_processed=0;

        double pATr = 0.0;


        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPairRead.entrySet())
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
           //  System.out.println(" Query = "+ QueryID + " Relevant count = " +is_relevant_counter + " Actual number of count ="+ relevant_count + " Result = "+ res) ;
//            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------");
        }
        //System.out.println("P@R across Queries = "+ (pATr/number_of_query_processed));
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
    	
    public double calcMAP()
    {
        double MAP=0.0;
        for(Map.Entry<String,Map<String,Integer>> outer: LuceneConstants.queryDocPairRead.entrySet())
        {
            String queryID = outer.getKey();
            int is_rel=0;
            int para_processed_so_far=0;
            double AP=0.0;
            int paraC= outer.getValue().size();
            for(Map.Entry<String,Integer> inner: outer.getValue().entrySet())
            {
                para_processed_so_far+=1;
                if(getQrelRelevancy(queryID,inner.getKey())==1)
                {
                    is_rel+=1;
                    double num =(double) is_rel/para_processed_so_far;
                    AP+= num;

                }

            }
            AP = AP / paraC;
            MAP+=AP;
            AP=0;
        }
        MAP = MAP /LuceneConstants.queryDocPairRead.size();
        return MAP;
    }


}
