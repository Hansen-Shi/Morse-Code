import java.util.*;

public class Trie {
	TrieNode root;
	
	Trie(){
		root = new TrieNode();
	}

	public void insert(String morse, String english) {
		TrieNode current = root;

		for (int i = 0; i < morse.length(); i++) {
			if (!current.children.containsKey(morse.charAt(i))) {
				current.children.putIfAbsent(morse.charAt(i), new TrieNode());
			}
			current = current.children.get(morse.charAt(i));
		}
		current.words.add(english);
	}

	public boolean contains(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			if (current.children.containsKey(word.charAt(i))) {
				current = current.children.get(word.charAt(i));
			} 
			else {
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<String> findWords(String word) {
		TrieNode current = root;
		for (int i = 0; i < word.length(); i++) {
			if (current.children.containsKey(word.charAt(i))) {
				current = current.children.get(word.charAt(i));
			} 
		}
		return current.words;
	}
	
	
	public List<String> getAll(TrieNode root){
		TrieNode current = root;
		List<String> allWords = new ArrayList<String>();
		List<Object> childVals = Arrays.asList(current.children.keySet().toArray());
		
		if (!current.words.isEmpty()) {
			for (String str : root.words) {
				allWords.add(str);
			}
		}
		
		for (int i = 0; i < childVals.size(); i++) {
			List<String> childWords = getAll(current.children.get(childVals.get(i)));
			allWords.addAll(childWords);
		}
		return allWords;
	}
}
