
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.net.URI;
import java.lang.String;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

public class weather
{

	
	public static class Map extends Mapper<LongWritable, Text, Text, Text> //MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
    {

        //private HashMap<String, String> StateMap = new HashMap<String, String>();
        private BufferedReader brReader;
        
        private Text txtMapOutputKey = new Text("");
        private Text txtMapOutputValue = new Text("");
        
    	private boolean started = false;
    	
    	private Model entity = new Model();
        

//        protected void setup(Context context) throws IOException, InterruptedException 
//        {
// 
//            Path[] cacheFilesLocal = DistributedCache.getLocalCacheFiles(context.getConfiguration());
// 
//            for (Path eachPath : cacheFilesLocal) 
//            {
//                if (eachPath.getName().equals("WeatherStationLocations.csv")) 
//                {
//                    String strLineRead = "";
//
//                    brReader = new BufferedReader(new FileReader(eachPath.toString()));
//                    strLineRead = brReader.readLine();
//                    
//                    while ((strLineRead = brReader.readLine()) != null) 
//                    {
//                        String stateFieldArray[] = strLineRead.split(",");
//
//                        stateFieldArray[0]=stateFieldArray[0].substring(1, stateFieldArray[0].length()-1);
//                        String state=stateFieldArray[4].substring(1, stateFieldArray[4].length()-1);
//                        String country=stateFieldArray[3].substring(1, stateFieldArray[3].length()-1);
//                        String lat1=stateFieldArray[5].substring(1, stateFieldArray[5].length()-1);
//                        String lon1=stateFieldArray[6].substring(1, stateFieldArray[6].length()-1);
//                        //float lat=Float.parseFloat(lat1);
//                        
//                        
//                        if(country.equals("US"))
//                        {
//                            if (state.equals("") || state.equals(null)) 
//                            {   
//                                state = "ZZ";
//
//                                if (!lon1.isEmpty())
//                                {
//                                    float lon=Float.parseFloat(lon1);
//                                    if (lon > -100)
//                                        state = "Altantic";
//                                    else
//                                        state = "Pacific";
//                                }
//
//                            }
//
//                            //System.err.println("~~"+stateFieldArray[0].trim()+"|"+stateFieldArray[4].trim());
//                            StateMap.put(stateFieldArray[0],state);
//                        }
//                        
//                        //StateMap.put(stateFieldArray[0],stateFieldArray[4]);                      
//                    }
//                    brReader.close();
//                }
//            }
//            if(StateMap.isEmpty())
//            {
//                throw new IOException("MetaDataNotFound");
//            }
//        }
            
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
        {
            try
            {
            	
        		
            	if(value != null)
                {
            		String line=value.toString();                        
                    if(line != null && !line.isEmpty())
                    {
                    	if(line.contains("SLF4J")) {
            				//continue;
            			}
            			if(line.contains("START")) {
            				String parts[] = line.split(" ");
            				entity.setStartTimeStamp(parts[0]);
            				started = true;
            				//continue;
            			}
            			if(line.contains("REPORT")) {
            				started = false;
            				String parts[] = line.split(" ");
            				entity.setEndTimeStamp(parts[0]);
            				entity.setDuration(parts[7]);
            				System.err.println("Entity: "+entity.toString());
            				String finalValue = ""+1+","+entity.getDuration();
            				System.err.println("outValue: "+finalValue);
            				txtMapOutputKey.set(entity.getPath());
                            txtMapOutputValue.set(finalValue);
                            context.write(txtMapOutputKey, txtMapOutputValue);
            				entity = new Model();
            				
            				//continue;
            			}
            			if(started) {
            				if(line.contains("PATH")) {
            					String parts[] = line.split(" ");
            					entity.setPath(parts[2]);
            				}
            				if(line.contains("DATA") || line.contains("WARNING") || line.contains("ERROR")){
            					int index = line.indexOf("DATA"); 
            					if(index!=-1) {
            						 line = line.substring(index);
            						 entity.setData(line.trim());
            					 } else { 
            						 index =line.indexOf("WARNING");
            						 if(index!=-1) {
            							 line = line.substring(index);
            							 entity.setData(line.trim());
            						 } else {
            							 index =line.indexOf("ERROR");
            							 if(index!=-1) {
            								 line = line.substring(index);
            								 entity.setData(line.trim());
            							 }
            							 
            						 }	 
            					 }
            				}
            				if(line.contains("EXECUTION_TIME")) {
            					String parts[] = line.split(" ");
            					entity.setExecutionTime(parts[parts.length-1]);
            				}
            			}

//                        if(!parts[0].contains("STN"))
//                        {
//                            String field=parts[0];
//                            String month=parts[2].substring(4,6);
//                            String temp=parts[3];
//                            String count=parts[4];
//                            String preci=parts[19];
//                                            
//                            String val = month+","+temp+","+count+","+preci;
//                            //System.err.println("~~"+val);
//                            
//                            strStateName = StateMap.get(field); 
//                            
//                            
//                        }
                    }
                }
            }
            catch(Exception e)
            {
                System.err.println(e+"Error inside Map....!!");
            }
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text>//MapReduceBase implements Reducer<Text, Text, Text, Text>
    {
        private Text txtReduceOutputKey = new Text("");
        private Text txtReduceOutputValue = new Text("");
        @Override
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException
        {

          int sum=0;
          int durationTotal=0;
            for(Text val : values) {
            	
            	String parts[] = val.toString().split(",");
            	sum += Integer.parseInt(parts[0]);
            	durationTotal += Integer.parseInt(parts[1]);
            	
            }
            	
            String strOut = ""+sum+" "+durationTotal;
            //String.format("%2.4f",months[minInd]);
            txtReduceOutputKey.set(key.toString());
            txtReduceOutputValue.set(strOut);
            //output.collect(key, new Text(strOut));
            context.write(txtReduceOutputKey, txtReduceOutputValue);
        }
    }

    

//JOB 2 implementaion *****************************************

//    public static class Map2 extends Mapper<Object, Text, FloatWritable, Text> //MapReduceBase implements Mapper<LongWritable, Text, Text, Text>
//    {
//
//        private FloatWritable txtMapOutputKey = new FloatWritable();
//        private Text txtMapOutputValue = new Text("");
//        
//            
//        public void map(Object key, Text value, Context context) throws IOException, InterruptedException
//        {
//            try
//            {
//                    String line=value.toString();                        
//                    String[] parts= line.split("\t");
//
//                        txtMapOutputKey.set(Float.parseFloat(parts[1]));
//                        txtMapOutputValue.set(parts[0]+","+parts[2]+","+parts[3]);
//                        context.write(txtMapOutputKey, txtMapOutputValue);
//            }
//            catch(Exception e)
//            {
//                System.err.println(e+"Error inside Map2....!!");
//            }
//        }
//    }
//
//    public static class Reduce2 extends Reducer<FloatWritable, Text, Text, Text>//MapReduceBase implements Reducer<Text, Text, Text, Text>
//    {
//        private Text txtReduceOutputKey = new Text("");
//        private Text txtReduceOutputValue = new Text("");
//        @Override
//        public void reduce(FloatWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException
//        {
//            String strOut="";
//            String keyOut="";
//
//            for(Text val : values)
//            {
//                String [] strElements = val.toString().split(",");
//                strOut=strElements[2]+"\t"+strElements[1]+"\tDiff: "+key.toString();
//                keyOut=strElements[0];
//            }
//
//            txtReduceOutputKey.set(keyOut);
//            txtReduceOutputValue.set(strOut);
//            //output.collect(key, new Text(strOut));
//            context.write(txtReduceOutputKey, txtReduceOutputValue);
//        }
//    }
  public static void main(String[] args) throws Exception 
  {
	  Configuration conf = new Configuration();

	    Job job = Job.getInstance(conf, "Weather");

	    job.setJarByClass(weather.class);

	    job.setMapperClass(Map.class);

	    job.setCombinerClass(Reduce.class);

	    job.setReducerClass(Reduce.class);

	    job.setOutputKeyClass(Text.class);

	    job.setOutputValueClass(Text.class);

	    FileInputFormat.addInputPath(job, new Path(args[0]));

	    FileOutputFormat.setOutputPath(job, new Path(args[1]));

	    System.exit(job.waitForCompletion(true) ? 0 : 1);
//    Job job = new Job();
//   // Configuration conf = job.getConfiguration();
//    job.setJobName("weather");
//    
//    //DistributedCache.addCacheFile(new URI("/user/asava003/weather/states/WeatherStationLocations.csv"),conf);
//    //DistributedCache.addCacheFile(new URI(args[0]+"/WeatherStationLocations.csv"),conf);
//
//    job.setJarByClass(weather.class);
//    System.out.println("ARGS: "+args.toString());
//    
//    FileInputFormat.addInputPath(job, new Path(args[0]));
//    FileOutputFormat.setOutputPath(job, new Path(args[1]));
//
//    job.setMapperClass(Map.class);
//    job.setReducerClass(Reduce.class);
//
//    job.setOutputKeyClass(Text.class);
//    job.setOutputValueClass(Text.class);
//    
//    
//    //job.setNumReduceTasks(0);
//    job.waitForCompletion(true); 

//job2 configuration ************************************************//
//    Job job2 = new Job();
//    Configuration conf2 = job2.getConfiguration();
//    job2.setJobName("weatherNext");
//    
//    job2.setJarByClass(weather.class);
//    
//    FileInputFormat.addInputPath(job2, new Path(args[2]));
//    FileOutputFormat.setOutputPath(job2, new Path(args[3]));
//
//    job2.setMapperClass(Map2.class);
//    job2.setReducerClass(Reduce2.class);
//
//    job2.setOutputKeyClass(Text.class);
//    job2.setOutputValueClass(Text.class);
//    
//    job2.setMapOutputKeyClass(FloatWritable.class);
//    
//    
//    //job.setNumReduceTasks(0);
//    boolean success2 = job2.waitForCompletion(true); 
    
  }
}