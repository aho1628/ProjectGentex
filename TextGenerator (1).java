import java.io.*;
import java.util.*;
import java.util.Map.Entry;
public class TextGenerator {

    /**
     * 
     * @param 
     * The first arg represents the k chars string of the Markov objects
     * The second arg represents the number of characters to print out. 
     * The third arg represents the input file to be read.
     */
	
	private static Scanner in;
	  
    public static void main(String[] args) throws IOException {
	int k = 0;
	int M = 0;
	String file = "";
	StringBuilder text = new StringBuilder();
	
    in = new Scanner(new File("TextGenerator.in"));		
    
    k = in.nextInt();	  // Number of chars for the string 
    M = in.nextInt();	  // Number of output chars
    file = in.next();     // Input file name

	FileReader reader = null;
	try {
	    reader = new FileReader(file);
	} catch (FileNotFoundException e) {
	    System.out.println("File not found.");
	    e.printStackTrace();
	}

	MyHashMap<String, Markov> hash = new MyHashMap<String, Markov>();

	Character next = null;
	try {
	    next = (char) reader.read();
	} catch (IOException e1) {
	    System.out.println("IOException in stepping through the file");
	    e1.printStackTrace();
	}

	StringBuilder origFileBuffer = new StringBuilder();
	while (Character.isDefined(next)) {
	    Character.toString(next);
	    origFileBuffer.append(next);
	    try {
		next = (char) reader.read();
	    } catch (IOException e) {
		System.out.println("IOException in stepping through the file");
		e.printStackTrace();
	    }

	}
	String origFile = origFileBuffer.toString();
	String firstSub = origFile.substring(0, k);
	for (int i=0; i<origFile.length()-k; i++) {
	    String sub = origFile.substring(i,i+k);
	    Character suffix = origFile.charAt(i+k);

	    if (hash.containsKey(sub)) {
		Markov marvin = hash.get(sub);
		marvin.add(suffix);
		hash.put(sub, marvin);
	    }
	    else {
		Markov marvin = new Markov(sub, suffix);
		hash.put(sub, marvin);
	    }
	}
	if (M == 0)
	    M = origFile.length();
	for (int i=k; i<M; i++) {
	    if (i==k) {
		text.append(firstSub);
		if (text.length() > k)
		    i=text.length();
	    }
	    String sub = text.substring((i-k),(i));	    
	    Markov tmp = hash.get(sub);
	    if (tmp!=null) {
		Character nextChar = tmp.random();
		text.append(nextChar);
	    }
	    else {
		i = k-1;
	    }
	}
	if (hash.size() < 100) {
	    Iterator<String> keys = hash.keys();
	    while (keys.hasNext()) {
		String hashKey = keys.next();
		Markov hashValue = hash.get(hashKey);
		System.out.print(hashValue.count()+" "+hashKey+":");
		for (Entry<Character, Integer> entry : hashValue.getMap().entrySet()) {
		    char suffix = entry.getKey();
		    int frequencyCount = entry.getValue();
		    System.out.print(" "+frequencyCount+" "+suffix);
		}
		System.out.println();
	    }
	}
	System.out.println(text.toString().substring(0, Math.min(M, text.length())));
    }
}