package main.java.EvaluationMeasures;

<<<<<<< HEAD
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
=======
import java.util.HashMap;
>>>>>>> 9978467f3b5a0813cc438de806bbe1638dea7137
import java.util.Map;

import main.java.LuceneIndex.LuceneConstants;
import main.java.util.LuceneUtil;

public class EvaluationMeasures{

<<<<<<< HEAD
    public Map<String,Map<String,Integer>> qrel_data;
    private Map<String, Double> mean_avg_precison;
    private static int ITERATIONS = 20 ;
=======
    public Map<String,Map<String,Integer>> qrel_data = new HashMap<String, Map<String, Integer>>();
    private Map<String, Double> mean_avg_precison = new HashMap<String, Double>();

>>>>>>> 9978467f3b5a0813cc438de806bbe1638dea7137
    public EvaluationMeasures(Map<String,Map<String,Integer>> qrel){
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
            Integer rel_docs_count = LuceneUtil.relevancy_count(qrel_data, queryId);
            if(rel_docs_count == 0){
                avg_precision = 0.0;
            }else{
                avg_precision = avg_precision/rel_docs_count;
            }
            mean_avg_precison.put(queryId, avg_precision);
        }
    }

    public double calculateMeanAvgPrecision(){
        getAvgPrecision();
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
    public Iterable<Double> calculateNDCG(){
        List<Double> NDCGList = new ArrayList<>();
        for (Map.Entry<String, Map<String,Integer>> query : LuceneConstants.queryDocPair.entrySet()) {
            NDCGList.add(DCG(query) / IDCG(query));
        }
        return NDCGList;
    }
    private Double DCG(Map.Entry<String, Map<String,Integer>> query){
            int grade = 0;
            Double DCG = 0.0;
            String queryId = query.getKey();
            Map<String,Integer> docIdRank= query.getValue();
            String[] docId = docIdRank.keySet().toArray(new String[docIdRank.size()]);
            for(int i = 1; i <= ITERATIONS; i++){
                grade = getQrelRelevancy(queryId, docId[i]);
                DCG += (Math.pow(2, grade)) / (Math.log(i + 1));
            }
            return DCG;
        }

    private double IDCG(Map.Entry<String, Map<String,Integer>> query){
        int counter = 1;
        int grade = 0;
        Double IDCG = 0.0;
        String queryId = query.getKey();
        Map<String,Integer> docIdRank= query.getValue();
        for(Map.Entry<String,Integer> document: docIdRank.entrySet()){
            if(counter <= ITERATIONS){
                if((grade = getQrelRelevancy(queryId, document.getKey())) == 1){
                    IDCG += (Math.pow(2, grade)) / (Math.log(++counter));
                }
            } else break;
        }
        return IDCG;
    }

}
