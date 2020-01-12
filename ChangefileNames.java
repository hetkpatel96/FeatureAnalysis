package org.Logs.Handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ChangefileNames {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		FileReader fr = null;
		BufferedWriter bufferedWriter = null;
		Writer writer = null;
		try {
			
			//br = new BufferedReader(new FileReader(FILENAME));
			File dir = new File("C:/Users/Het Patel/Desktop/HET/FINAL PROJECT/logs/newlogs");
			int i= Integer.parseInt(args[0]);
			for(File file : dir.listFiles()){
				System.out.println("reading file name : " +file.getName());
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				File myFile = new File("C:/Users/Het Patel/Desktop/HET/FINAL PROJECT/logs/temp/"+"log"+i+".txt");
				i++;
				if (!myFile.exists()) {
					myFile.createNewFile();
				}
				writer = new FileWriter(myFile);
				bufferedWriter = new BufferedWriter(writer);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					bufferedWriter.write(sCurrentLine);
					bufferedWriter.write("\n");

				}
				br.close();
				fr.close();
				bufferedWriter.close();
				writer.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
