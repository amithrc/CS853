package main.java.LuceneIndex;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.index.IndexWriter;


public class LuceneMain
{

	private static void  usage()
	{
		System.out.println("Please pass the index file absolute path");
		System.exit(-1 );
	}

	private static boolean checkIndexExists(String dest)
	{
		File temp= new File(dest);
		if(temp.isDirectory() && temp.exists())
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public static void main(String[] args) throws IOException
	{
		String dest;
		System.out.println("Please pass the file to be indexed");
		if( args.length < 1 )
		{
			usage();
		}
		else
		{
			dest = System.getProperty("user.dir")+System.getProperty("file.separator")+"indexed_file";
			if(checkIndexExists(dest))
			{
				System.out.println("Files already indexed, proceeding");
				LuceneConstants.setDirectoryName(dest);
			}
			else
			{
				String[] mode_input = new String[] {"paragraphs", args[0]};
				LuceneConstants.setIndexFileName(args[0]);
				LuceneConstants.setDirectoryName(dest);
				LuceneIndexer l = new LuceneIndexer();
				l.setMode(mode_input);
				l.getIndexWriter(dest);
				l.closeIndexWriter();

			}

		}

	}

}