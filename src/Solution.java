import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.String;

public class Solution {

	//read the morsecode alphabet into a hashmap where k,v = (morse, English)
	public static LinkedHashMap<String, String> morseCode(String string) {
		List<String> x = new ArrayList<String>();
		LinkedHashMap<String, String> morseCode = new LinkedHashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(string));
			String code;
			while ((code = reader.readLine()) != null) {
				x = Arrays.asList(code.split(" "));
				if (x.size() == 2) {
					morseCode.put(x.get(1), x.get(0));
					}
				
				//this is just to deal with the pesky extra space on line 37
				else if (x.size() == 3) {
					morseCode.put(x.get(2), x.get(0));
				}
			}
			reader.close();
		} catch (Exception e) {

		}
		return morseCode;

	}

	//make a hashset of all the words in the dictionary
	public static LinkedHashSet<String> dictionaryHash(String string) {
		LinkedHashSet<String> dic = new LinkedHashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(string));
			String word;
			while ((word = reader.readLine()) != null) {
				dic.add(word);
			}
			reader.close();
		} catch (Exception e) {

		}
		return dic;
	}
	
	//make another hashmap of the morse alphabet where k,v = (English, morse)
	public static LinkedHashMap<String, String> codeMorse(String string) {
		List<String> x = new ArrayList<String>();
		LinkedHashMap<String, String> morseCode = new LinkedHashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(string));
			String code;
			while ((code = reader.readLine()) != null) {
				x = Arrays.asList(code.split(" "));
				morseCode.put(x.get(0), x.get(1));
			}
			reader.close();
		} catch (Exception e) {

		}
		return morseCode;
	}

	//make the dictionary into a hashmap where k,v = (English, morse)
	public static LinkedHashMap<String, String> morseDic(String string, LinkedHashMap<String, String> letterToMorse) {
		List<String> x = new ArrayList<String>();
		LinkedHashMap<String, String> dic = new LinkedHashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(string));
			String word;
			while ((word = reader.readLine()) != null) {
				x = Arrays.asList(word.split(""));
				String morse = "";
				for (String str: x) {
					morse = morse.concat(letterToMorse.get(str));
				}
				dic.put(word, morse);
			}
			reader.close();
		} catch (Exception e) {
			
		}
		return dic;
	}
	
	//make a hashmap that is the reverse of the dictionary hashmap above, k, v = (morse, English)
	public static LinkedHashMap<String, List<String>> dicMorse(LinkedHashMap<String, String> morseDic) {
		LinkedHashMap<String, List<String>> dicMorse = new LinkedHashMap<String, List<String>>();
		morseDic.forEach((k, v) ->{
			if (dicMorse.containsKey(v)) {
				dicMorse.get(v).add(k);
			}
			else {
				dicMorse.putIfAbsent(v, new ArrayList<String>());
				dicMorse.get(v).add(k);
			}
		});
		return dicMorse;
	}
	
	//The recrusive method used for backtracking in handleWord
	//takes in the morsed string, the hashmap of the alphabet, the dictionary hashset, an arraylist storing words, and a string that is the current state of the word
	public static void backTrackHelper(String morsed, LinkedHashMap<String, String> codes, LinkedHashSet<String> dic,
			ArrayList<String> words, String word) {
		
		
		String current;
		String potentialLetter;
		String letter;
		String newWord;

		// if the morsed string has no more potential letters to check
		if (morsed.isEmpty()) {

			// check if the word is in the dictionary, add it to the arraylist of words if
			// it exists in dic
			if (dic.contains(word)) {
				words.add(word);
			}
		}

		// if the morsed string has more periods/dashes
		else {

			// loop through all of the morse code alphabet
			for (String s : codes.keySet()) {

				// if the morse code is too long to be part of the string, ignore it
				if (s.length() > morsed.length()) {
					continue;
				}

				// if the morse code is a potential candidate
				else {

					// set the current word equal to whatever morse was passed in
					current = word;

					// the potential canidate letter being checked
					potentialLetter = s;

					// substring part of the input morse to match the length of the candidate
					letter = morsed.substring(0, potentialLetter.length());

					// check if the candidate matches the part cut from the morse input
					if (potentialLetter.equals(letter)) {

						// if there's a match, cut that letter out of the morse input
						newWord = morsed.substring(potentialLetter.length());

						// add the letter to the current word
						current = current.concat(codes.get(s));

						// recurse with the now shorter morse input and longer current word
						backTrackHelper(newWord, codes, dic, words, current);
					}
				}
			}
		}
	}
	
	// recursive helper function to help print out stuff for part 3
	// takes in an output string s, a list of lists with all the words, and an
	// integer for iterating
	public static void printStuff(String s, List<List<String>> stuff, int n) {

		// if we are at the end of the list of lists
		if (n == stuff.size() - 1) {

			// loop through all the elements in that last list
			for (int i = 0; i < stuff.get(n).size(); i++) {

				// combine it all into one output string
				String output = s + " " + stuff.get(n).get(i);

				// trim the extra spaces and print
				output = output.trim();
				System.out.println(output);
			}
		}

		// if we're not at the last list in the list of lists
		else {

			// add the stuff to a new string sentence and recurse with it
			for (String str : stuff.get(n)) {
				String sentence = s + " " + str;
				printStuff(sentence, stuff, n + 1);
			}
		}
	}
	
	//modified version of the backtracking method used in part 2
	//takes in the morsed string, a hashmap of the dictionary of words, a trie, an arraylist of sentences and the current sentence string
	public static void backTrackSentence(String morsed, LinkedHashMap<String, List<String>> morseDic, Trie tree,
			ArrayList<String> sentences, String sentence) {
		
		String oldSentence;
		String currentSentence;
		String potentialWord;
		String word;
		String newSentence;

		// if the morsed string has no more potential words add the sentence to the arraylist
		if (morsed.isEmpty()) {
			
			// trim the string because it has an extra space at the end
			sentence = sentence.trim();
			sentences.add(sentence);
		}

		// if morsed still has potential words
		else {

			// loop through all of the morsed words in the dictionary hashmap
			for (String s : morseDic.keySet()) {

				// if the morse code is too long to be part of the string, ignore it
				if (s.length() > morsed.length()) {
					continue;
				}

				// if the morse code is a potential candidate
				else {

					// set the current sentence equal to whatever sentence was passed in
					currentSentence = sentence;
					
					// this keeps the old sentence which is needed below
					oldSentence = sentence;
					
					//loop through all the words that this morse could represent
					for (int i = 0; i < tree.findWords(s).size(); i++) {
						
						//reset the currentSentence to the original sentence passed in
						currentSentence = oldSentence;
						
						// the potential canidate word being checked
						potentialWord = s;
						
						// make a substring from morsed equal in length to the candidate
						word = morsed.substring(0, potentialWord.length());
						
						// check if the candidate matches the part cut from the morse input
						if (potentialWord.equals(word)) {

							// if there's a match, cut that portion out of the morse input
							newSentence = morsed.substring(potentialWord.length());

							// add the word at index i to the current sentence
							currentSentence = currentSentence.concat(morseDic.get(s).get(i) + " ");

							// recurse with the now shorter morse input and longer current sentence
							backTrackSentence(newSentence, morseDic, tree, sentences, currentSentence);
						}
					}
				}
			}
		}
	}
	
	public static void otherBackTrackSentence(String morsed, Trie tree, String sentence, ArrayList<String> sentences) {
		
		
		char[] morseChars;
		TrieNode current;
		String newSentence;
		String currentSentence;
		
		// if there's no more possible words in morsed
		if (morsed.isEmpty()) {
			sentences.add(sentence);
			}
		
		// if there are more possible words
		else {
			
			// set the current node equal to the root of the trie
			current = tree.root;
			
			// convert the morsecode into an array of characters
			morseChars = morsed.toCharArray();
			
			// loop through all the chars in the array
			for (int i = 0; i < morseChars.length; i++) {
				
				// if the current node has a child with the current char in the morse
				if (current.children.containsKey(morseChars[i])) {
					
					// current is set to that child
					current = current.children.get(morseChars[i]);
					
					// if current is the end of a word
					if (current.words.size() > 0) {
						
						// loop through all the words at current
						for (int j = 0; j < current.words.size(); j++) {
							
							// cuts off that part of the morse string
							newSentence = morsed.substring(i + 1);
							
							// adds the word to the sentence
							currentSentence = sentence + current.words.get(j) + " ";
							
							//recurses with the shorter morse and longer current sentence
							otherBackTrackSentence(newSentence, tree, currentSentence, sentences);
						}
					}
				}
			}
		}
	}

	public static void handleSpacedLetters(String morsed, LinkedHashMap<String, String> codes,
			LinkedHashSet<String> dic) {
		
		
		String output = "";
		List<String> letters = Arrays.asList(morsed.split(" "));

		for (String str : letters) {
			output = output.concat(codes.get(str));
		}

		if (!dic.contains(output)) {
			output = "";
		}
		
		System.out.println(output);
	}

	public static void handleWord(String morsed, LinkedHashMap<String, String> codes, LinkedHashSet<String> dic) {
		
		//initializing an empty arraylist for storing words
		//and an empty string to pass in
		String word = "";
		ArrayList<String> words = new ArrayList<String>();
		
		backTrackHelper(morsed, codes, dic, words, word);
		
		for (String s : words) {
			System.out.println(s);
		}
	}

	public static void handleSpacedWords(String morsed, Trie tree) {

		// split the morse code into separate words
		String[] x = morsed.split(" ");
		
		// a list of lists with all the words from each index
		List<List<String>> stuff = new ArrayList<List<String>>();
		
		// populate the list of lists
		for (String str : x) {
			List<String> list = new ArrayList<String>();
			list.addAll(tree.findWords(str));
			stuff.add(list);
		}

		// if the list of lists is of size one
		if (stuff.size() == 1) {
			
			//print one line with all the stuff in that list
			for (int i = 0; i < stuff.get(0).size(); i++) {
				System.out.println(stuff.get(0).get(i));
			}
		}
		
		// if the list of lists has more than 1 list to loop through call the recursive function
		else if (stuff.size() > 1) {
			printStuff("", stuff, 0);
		}
	}
	
	public static void handleSentence(String morsed, Trie tree, LinkedHashMap<String, String> dic, LinkedHashMap<String, List<String>> morseDic) {

		String sentence = "";
		ArrayList<String> sentences = new ArrayList<String>();
		ArrayList<String> bestSentences = new ArrayList<String>();
		
		int shortest = Integer.MAX_VALUE;

		// this one works and passes all the tests, but it doesn't utilize the trie at all
		// it's more or less the same as the method used for part 2
		backTrackSentence(morsed, morseDic, tree, sentences, sentence);
		
		// this function utilizes the trie but it doesn't pass all the test cases
		// it gives really strange outputs for the last few tests and I couldn't figure out the issue
		// for example, for the 'get on zoom' case, it finds 'zoom too' which literally shouldn't be possible
		//
		//otherBackTrackSentence(morsed, tree, sentence, sentences);
		
		
		//find the length of the shortest sentence(s) in the arraylist
		for (String s : sentences) {
			String[] x = s.split(" ");
			if (x.length < shortest) {
				shortest = x.length; 
			}
		}
		
		//find the shortest sentences and add them to the better arraylist
		for (String str : sentences) {
			String[] x = str.split(" ");
			if (x.length == shortest) {
				
				bestSentences.add(str);
			}
		}
		
		//sort the strings in alphabetical order
		Collections.sort(bestSentences);
		
		//print em, yay
		for (String words : bestSentences) {
			System.out.println(words);
		}
		
	}

	public static void main(String[] args) {
		// Get input from stdin
		Scanner scanner = new Scanner(System.in);
		String command = scanner.nextLine();
		scanner.close();
		// Parse the style and morsed code value
		String[] parts = command.split(":");
		String style = parts[0].trim();
		String morsed = parts[1].trim();
		
		
		LinkedHashMap<String, String> morseToLetter = morseCode("morse.txt");
		LinkedHashMap<String, String> letterToMorse = codeMorse("morse.txt");
		LinkedHashSet<String> dic = dictionaryHash("dictionary.txt");
		LinkedHashMap<String, String> morseDic = morseDic("dictionary.txt", letterToMorse);
		LinkedHashMap<String, List<String>> dicMorse = dicMorse(morseDic);
		
		Trie tree = new Trie();
		morseDic.forEach((k, v) ->{
			tree.insert(v, k);
		});

		switch (style) {
		case "Spaced Letters":
			handleSpacedLetters(morsed, morseToLetter, dic);
			break;
		case "Word":
			handleWord(morsed, morseToLetter, dic);
			break;
		case "Spaced Words":
			handleSpacedWords(morsed, tree);
			break;
		case "Sentence":
			handleSentence(morsed, tree, morseDic, dicMorse);
			break;
		}

	}

}
