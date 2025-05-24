//***************************************************************
//
//  Developer:    Giovanni Vecchione
//
//  Project #:    Project Three
//
//  File Name:    Project3A.java
//
//  Course:       COSC 3365 Distributed Databases Using Hadoop 
//
//  Due Date:     2/28/25
//
//  Instructor:   Prof. Fred Kumi 
//
//  Description:  WordCount Program to identify each word in a file and return a key value
//				  pair showing the total number of instances of that specific word. Uses a 
//				  Mapper and Reducer.
//
//***************************************************************

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class Project3A
{
	//***************************************************************
    //
    //  Method:       main
    // 
    //  Description:  The main method of the program. Calls Mapper and Reducer also.
    //
    //  Parameters:   String array
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	public static void main(String[] args)
	{
		try{
			if (args.length != 2)
			{
				System.err.println("Usage: WordCount <input path> <output path>");
				System.exit(-1);
			}
			
			Configuration conf = new Configuration();
			String[] files = new GenericOptionsParser(conf, args).getRemainingArgs(); // IOException
			Path input = new Path(files[0]);
			Path output = new Path(files[1]);
			
			Job job = Job.getInstance(conf, "wordcount"); // IOException
			job.setJarByClass(Project3A.class);
			job.setMapperClass(MapForWordCount.class);
			job.setReducerClass(ReduceForWordCount.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			
			FileInputFormat.addInputPath(job, input); //IOException
			FileOutputFormat.setOutputPath(job, output);
			System.exit(job.waitForCompletion(true) ? 0 : 1); //ClassNotFoundException | InterruptedException
		}
		catch(IOException | ClassNotFoundException | InterruptedException i){
			System.err.println("Exception thrown in Project3A Main: IOException thrown from lines 57, 61 or 68. OR InterruptedException/ClassNotFoundException in line 70" + i.getMessage());
		}
	}
}

