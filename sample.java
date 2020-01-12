import java.io.*;


public class sample {

	public static void main(String[] args) throws IOException {
		
		File dir = new File("logs");
		int c =0 ;
		File myFile= new File("sorted.txt");
		Writer writer = new FileWriter(myFile);
		BufferedWriter bufferedWriter = new BufferedWriter(writer);
		for(File file: dir.listFiles()) {
			BufferedReader br = null;
			FileReader fr = null;
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
	
			boolean started = false;
			
			Model entity = new Model();
			while ((line = br.readLine()) != null) {
				
				
				if(line.contains("SLF4J")) {
					continue;
				}
				if(line.contains("START")) {
					String parts[] = line.split(" ");
					entity.setStartTimeStamp(parts[0]);
					started = true;
					continue;
				}
				if(line.contains("REPORT")) {
					started = false;
					String parts[] = line.split(" ");
					entity.setEndTimeStamp(parts[0]);
					entity.setDuration(parts[7]);
					System.out.println(c+":  "+entity.toString());
					bufferedWriter.write(entity.toString());
					bufferedWriter.write("\n");
					c++;
					entity = new Model();
					continue;
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
			}
			br.close();
			fr.close();
		}
		bufferedWriter.close();
		writer.close();
		
	}
}
