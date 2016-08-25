package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;

public class PropertiesUtil {
	
	private static Properties prop = null;
	private static IRAMDictionary dict = null;
	
	public static void load(String contextPath){
		prop = new Properties();
		InputStream input = null;
	
		try {
			input = new FileInputStream(contextPath+File.separator+"config.properties");
	
			// load a properties file
			prop.load(input);
			setProperty("CONTEXTPATH", contextPath);
			
			System.out.println("");
			
			// initialize wordNet dictionary
			String path = PropertiesUtil.getFilesPath() + 
						PropertiesUtil.getProperty("WORDNET_DICT");
				
			System.out.println(path);
			dict = new RAMDictionary(new File(path), ILoadPolicy.NO_LOAD);
			dict.open();
	
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
		
	public static String getProperty(String propKey){
		return prop.getProperty(propKey);
	}
	
	public static void setProperty(String propKey, String propValue){
		prop.setProperty(propKey, propValue);
	}
	
	public static String getFilesPath(){
		return getProperty("CONTEXTPATH")+File.separator+"files"+File.separator;
	}
	
	public static IRAMDictionary getWordNetDictionary(){
		return dict;
	}
	
}
