package main.java.EvaluationMeasures;

import java.util.Map;

import main.java.LuceneIndex.LuceneConstants;
import main.java.util.LuceneUtil;

public class EvaluationMeasures{

    public Map<String,Map<String,Integer>> qrel_data;
    private Map<String, Double> mean_avg_precison;

    public EvaluationMeasures(Map<String,Map<String,Integer>> qrel){
        qrel_data = qrel;

    }

    private int getQrelRelevancy(String query_id, String doc_id){
        if(LuceneConstants.queryDocPair.containsKey(query_id)){
            Map<String,Integer> temp = LuceneConstants.queryDocPair.get(query_id);
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
            Integer query_count = 0;
            Integer ranking_rel_count = 0;
            Double avg_precision = 0.0;

            for(Map.Entry<String,Integer> document: docIdRank.entrySet()){
                query_count = query_count + 1;
                if(getQrelRelevancy(queryId, document.getKey()) == 1){
                    ranking_rel_count = ranking_rel_count + 1;
                    avg_precision = avg_precision + (ranking_rel_count/query_count);
                }

            }
            Integer rel_docs_count = LuceneUtil.relevancy_count(LuceneConstants.queryDocPair, queryId);
            if(rel_docs_count == 0){
                avg_precision = 0.0;
            }else{
                avg_precision = avg_precision/rel_docs_count;
            }
            mean_avg_precison.put(queryId, avg_precision);
        }
    }

    public double calculateMeanAvgPrecision(){
        Double MAP = 0.0;
        Double totalAP = 0.0;
        for (Map.Entry<String, Double> avgPrec : mean_avg_precison.entrySet()){
            totalAP = totalAP + avgPrec.getValue();
        }
        Integer total_size = mean_avg_precison.size();
        if(total_size != 0){
            MAP = totalAP/total_size;
        }
        return MAP;

    }
}
