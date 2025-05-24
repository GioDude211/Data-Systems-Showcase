import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


//***************************************************************
//
//  Class:        ReduceForWordCount
// 
//  Description:  Reduces the input from the mapper to simplify the data further. Extends
//				  Class Reducer. Iterates through the key-value pairs and sums up each value to 
//				  a single unique key (which is the word).
//
//  Parameterized: Yes - Text, IntWritable, Text, IntWritable
//
//  Returns:      None - Writes out the sum of the values for each key.
//
//**************************************************************

public class ReduceForWordCount extends Reducer<Text, IntWritable, Text, IntWritable>
	{
		
	//***************************************************************
    //
    //  Method:       reduce
    // 
    //  Description:  Reduce method sums each value for unique keys from Mapper.
    //
    //  Parameters:   3 - Text, Iterable<IntWritable>, Context
    //
    //  Returns:      N/A - Writes the sum of values for each unique key.
    //
    //**************************************************************
		@Override
		public void reduce(Text word, Iterable<IntWritable> values, Context con)
		{
			try{
				int sum = 0;
				for (IntWritable value : values)
				{
					sum += value.get();
				}
				
				con.write(word, new IntWritable(sum)); //Can throw IOException or InterruptedException.
			}
			catch(InterruptedException | IOException e){
				System.err.println("InterruptedException or IOException has occured during con.write(word, new IntWritable(sum)), line 28." + e.getMessage());
			}
			catch(NumberFormatException i){
				System.err.println("Exception @ Reducer: Inappropriate Format conversion dring calculation." + i.getMessage());
			}
			catch(ArithmeticException f){
				System.err.println("Exception @ Reducer: Arithmetic Exception during calculation." + f.getMessage());
			}
		}
	}