import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//***************************************************************
//
//Class:        MapForWordCount
//
//Description:  Mapper - Initial process to parse the data and group key-value pairs.
//
//Parameterized: Yes - LongWritable, Text, Text, IntWritable
//
//Returns:      None - Writes the output key and output value. Reducer will receive this.
//
//**************************************************************

public class MapForWordCount extends Mapper<LongWritable, Text, Text, IntWritable>
	{
	
	//***************************************************************
    //
    //  Method:       map
    // 
    //  Description:  The map method of the program, creates key-value pairs.
    //
    //  Parameters:   3 - LongWritable, Text, Context
    //
    //  Returns:      None - Writes the initial output key and output value.
    //
    //**************************************************************
		@Override
		public void map(LongWritable key, Text value, Context con)
		{
			try{
				//If value is null throws an exception
				if (value == null){
					throw new IllegalArgumentException("Input value cannot be null.");
				}
				
				String line = value.toString();
				
				//If input line is empty, throws an exception
				if (line.trim().isEmpty()){
					throw new IllegalArgumentException("Input line is empty.");
				}
				
				String[] words = line.split(",");
				
				for (String word : words)
				{
					Text outputKey = new Text(word.toUpperCase().trim());
					IntWritable outputValue = new IntWritable(1);
					
					//Expecting Interrupted Exception and IO Exception.
					con.write(outputKey, outputValue);
			}
			}
			catch(NullPointerException prse){
				System.err.println("Exception in Mapper: Null value encountered during parsing." + prse.getMessage());
			}
			catch(IllegalArgumentException i){
				System.err.println("Exception in Mapper: Invalid input data: " + i.getMessage());
			}
			catch(InterruptedException | IOException e){
				System.err.println("Mapper Exception thrown @ con.write" + e.getMessage());
			}
			
			
		}
	}
