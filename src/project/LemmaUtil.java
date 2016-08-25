package project;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

/*
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
*/

public class LemmaUtil {
	
	private static IRAMDictionary dict = PropertiesUtil.getWordNetDictionary();
	
	public static List<String> getLemmaLowerCaseTokens(String s){
		s = s.toLowerCase();
		List<String> results = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(s);
		IIndexWord idxWord = null;
	    while (st.hasMoreTokens()) {
	    	String word = st.nextToken();
	    	for(POS pos: POS.values()){
		    	idxWord = dict.getIndexWord(word, pos);	  
		    	if(idxWord != null){
		    		results.add(idxWord.getLemma());
		    		break;
		    	}
	    	}
	    }
	    System.out.println("lemma results="+results);
	    return results;
	}
	
	/**
	 * Check if this string is a valid word. 
	 * Query from wordNet dictionary and see if it exists.
	 * @param s	the word we want to check valid
	 * @return	is a word or not
	 */
	public static boolean isWord(String s){		
		IIndexWord idxWord = dict.getIndexWord(s, POS.NOUN);
		return idxWord != null? true:false;
	}
	
	public static void getHypernyms(String s) {

		// get the synset
		IIndexWord idxWord = dict.getIndexWord(s, POS.NOUN);
		IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
		IWord word = dict.getWord(wordID);
		ISynset synset = word.getSynset();

		// get the hypernyms
		List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);

		// print out each hypernyms id and synonyms
		List<IWord> words;
		for (ISynsetID sid : hypernyms) {
			words = dict.getSynset(sid).getWords();
			System.out.print(sid + " {");
			for (Iterator<IWord> i = words.iterator(); i.hasNext();) {
				System.out.print(i.next().getLemma());
				if (i.hasNext())
					System.out.print(", ");
			}
			System.out.println("}");
		}
	}
	
	/*
	
	static StanfordCoreNLP pipeline;
	static Properties props;
	static{
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
	}
	
    public static List<String> lemmatize(String documentText){    	
    	List<String> lemmas = new LinkedList<String>();

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);

        // run all Annotators on this text
        pipeline.annotate(document);

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }

        return lemmas;
    }
    */
}
