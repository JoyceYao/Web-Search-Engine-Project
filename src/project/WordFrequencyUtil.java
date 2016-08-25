package project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordFrequencyUtil {
	
	private static String filePath = PropertiesUtil.getFilesPath()+PropertiesUtil.getProperty("WORD_FREQ_FILE");
	private static int freqRank = Integer.parseInt(PropertiesUtil.getProperty("FREQ_RANK"));
	
	/**
	 * Create a frequency word set
	 * @param freqRank	The size of frequency word
	 * @return	A set
	 */
	public static Set<String> buildFreqWords(){
		Set<String> mostFreqWords = new HashSet<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		    String line = br.readLine();

		    for(int i=0; i<freqRank; i++){
			    String[] lines = line.split(" ");
			    mostFreqWords.add(lines[0]);
			    line = br.readLine();
		    }
		    
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//System.out.println(mostFreqWords);
		return mostFreqWords;
	}
}
