import java.util.*;

public class TrieNode {
	String value;
	LinkedHashMap<Character, TrieNode> children;
	ArrayList<String> words;
	
	TrieNode(){
		value = null;
		children = new LinkedHashMap<Character, TrieNode>();
		words = new ArrayList<String>();
	}
}
