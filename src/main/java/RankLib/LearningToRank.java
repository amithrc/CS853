package main.java.RankLib;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import main.java.LM.LMSearcher;
import main.java.lucenerankingmodels.TFIDFSearcher;
import main.java.util.LuceneUtil;
import main.java.util.LuceneConstants;

public class LearningToRank {

    private Map<String, Map<String, Map<String, float[]>>> ranking_pairs = new LinkedHashMap<String, Map<String, Map<String, float[]>>>();
    private Map<String,Map<String,Integer>> qrel_data;

    public LearningToRank(Map<String,Map<String,Integer>> qrel){
        qrel_data = qrel;
    }

    public void generetaeRanklibFile() throws IOException{
        getRankings();
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

    private void createRankingPair(String function_key, String query_key, String doc_id, float[] rank)
    {
        if(ranking_pairs.containsKey(function_key))
        {
            Map<String, Map<String, float[]>> extract = ranking_pairs.get(function_key);
            if(extract.containsKey(query_key)){
                Map<String, float[]> query_extract = extract.get(query_key);
                if(!query_extract.containsKey(doc_id)) {
                    Map<String, float[]> temp = new LinkedHashMap<String, float[]>();
                    temp.put(doc_id, rank);
                    extract.put(query_key, temp);
                }
            }
            else{
                Map<String, Map<String, float[]>> query_temp = new LinkedHashMap<String, Map<String, float[]>>();
                Map<String, float[]> doc_temp = new LinkedHashMap<String, float[]>();
                doc_temp.put(doc_id, rank);
                extract.put(query_key, doc_temp);
                //extract.put(function_key, query_temp);
                //ranking_pairs.put(function_key, query_temp);
            }

        }
        else
        {
            Map<String, Map<String, float[]>> query_temp = new LinkedHashMap<String, Map<String, float[]>>();
            Map<String, float[]> doc_temp = new LinkedHashMap<String, float[]>();
            doc_temp.put(doc_id, rank);
            query_temp.put(query_key, doc_temp);
            ranking_pairs.put(function_key, query_temp);
        }
    }


    private void getRankings() throws IOException {
        Map<String,String> p = LuceneUtil.readOutline(LuceneConstants.OUTLINE_CBOR);

        // create Ranking pair for TFIDF : LNC

        TFIDFSearcher LNC = new TFIDFSearcher("LNC");
        LNC.setLNC();
        LNC.writeRankings(p);

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()){
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document: docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0) ;
                createRankingPair("LNC", queryID, document.getKey(),new float[]{1 / document.getValue(), relevancy});

            }
        }

        //create Ranking pair for TFIdf : bnn

        TFIDFSearcher BNN = new TFIDFSearcher("BNN");
        BNN.setBNN();
        BNN.writeRankings(p);

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()){
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document: docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0) ;
                createRankingPair("BNN", queryID, document.getKey(),new float[]{1 / document.getValue(), relevancy});

            }
        }

        // create Ranking pair fot Unigram Language model: Laplace

        LMSearcher laplace = new LMSearcher("laplace");
        laplace.setLaplace();
        laplace.writeRankings(p);

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()){
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document: docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0) ;
                createRankingPair("laplace", queryID, document.getKey(),new float[]{1 / document.getValue(), relevancy});

            }
        }

        //create Ranking pair for Unigram Language model : JM

        LMSearcher jmSmoothing = new LMSearcher("JMSmoothing");
        jmSmoothing.setJMSmoothing();
        jmSmoothing.writeRankings(p);

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()){
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document: docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0) ;
                createRankingPair("JMSmoothing", queryID, document.getKey(),new float[]{1 / document.getValue(), relevancy});

            }
        }

        // create Ranking pair for Unigram Language model : Dirichlet

        LMSearcher dirichletSmoothing = new LMSearcher("DirichletSmoothing");
        dirichletSmoothing.setDirichletSmoothing();
        dirichletSmoothing.writeRankings(p);

        for (Map.Entry<String, Map<String,Integer>> Query : LuceneConstants.queryDocPair.entrySet()){
            String queryID = Query.getKey();
            Map<String, Integer> docIDRank = Query.getValue();
            for (Map.Entry<String, Integer> document: docIDRank.entrySet()) {

                int relevancy = (getQrelRelevancy(queryID, document.getKey()) == 1 ? 1 : 0) ;
                createRankingPair("DirichletSmoothing", queryID, document.getKey(),new float[]{1 / document.getValue(), relevancy});

            }
        }

    }
}
