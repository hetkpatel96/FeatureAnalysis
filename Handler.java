package org.Logs.Handler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Handler {
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bufferedWriter = null;
		Writer writer = null;
		try {
			File myFile = new File("C:/Users/Het Patel/Desktop/HET/FINAL PROJECT/logs/live.txt");

			if (!myFile.exists()) {
				myFile.createNewFile();
			}
			writer = new FileWriter(myFile);
			bufferedWriter = new BufferedWriter(writer);
			
			//br = new BufferedReader(new FileReader(FILENAME));
			File dir = new File("C:/Users/Het Patel/Desktop/HET/FINAL PROJECT/logs/temp");
			for(File file : dir.listFiles()){
				System.out.println("reading file name : " +file.getName());
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.length()>0) {
						String parts[] = sCurrentLine.split(" +");
						if(parts.length==0) {
							parts = new String[1];
							parts[0] = "NO TIMESTAMP";
						}
						ArrayList<String> arr = new ArrayList<String>(Arrays.asList(parts));
						if(arr.contains("PATH")){
							if(!arr.get(arr.size()-1).equals("PATH"))
								System.out.println("not valid entry");

						}
						if(arr.contains("EXECUTION_TIME")) 
							if(!arr.get(arr.size()-1).equals("EXECUTION_TIME")) {
								arr.add("NULL");
						}
					}
				
					
					bufferedWriter.write(sCurrentLine);
					bufferedWriter.write("\n");
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();
				if (bufferedWriter != null)
					bufferedWriter.close();
				if (writer != null)
					writer.close();
			

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	} 
}
