package main.java.LuceneIndex;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import main.java.LuceneIndex.LuceneIndexer;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;
import main.java.LuceneSearch.LuceneSearcher;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;



public class LuceneMain
{
	
	//static final String INDEX_DIRECTORY = "C:\\Users\\VaughanCoder\\CS853\\test200.v2.0\\test200\\test200-train\\train.pages.cbor-paragraphs";
 
	
	public static void SimpleCase() {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();

	}
	
	 public static void usage() {
	        System.out.println("Command line parameters: (pages|outlines|paragraphs) FILE");
	        System.exit(-1);
	    }
	
    public static void main(String[] args)throws Exception
    {
		LuceneIndexer index = new LuceneIndexer();
		String[] mode_input = new String[] {"paragraphs",
				"/home/poojaoza/Documents/projects/train.test200.cbor.paragraphs"};
		index.setMode(mode_input);
		index.getIndexWriter();
		index.closeIndexWriter();
        LuceneSearcher searcher = new LuceneSearcher();
        searcher.getRankingDocuments();
    }
}
