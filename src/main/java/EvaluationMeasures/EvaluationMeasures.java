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



        for (Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPair.entrySet()){

            String queryId = query.getKey();
            Map<String,Integer> docIdRank= query.getValue();
            int query_count = 0;
            int ranking_rel_count = 0;
            double avg_precision = 0.0;

            for(Map.Entry<String,Integer> document: docIdRank.entrySet()){
                query_count = query_count + 1; //paragraphs present for each query i.e. total documents for a query
                if(getQrelRelevancy(queryId, document.getKey()) == 1){
                    ranking_rel_count = ranking_rel_count + 1; //how many documents are relevant
                    double denominator = (double) ranking_rel_count/query_count;
                    avg_precision = avg_precision + denominator;

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
            System.out.println(avgPrec.getKey()+' '+avgPrec.getValue());
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

        double pATr= 0.0;


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
            //System.out.println(" Query = "+ QueryID + " Relevant count = " +is_relevant_counter + " Actual number of count ="+ relevant_count + " Result = "+ res) ;
        }
        return (pATr/number_of_query_processed);
    }


}
