
import java.util.*;
public class SCtest{
    
    public static void main(String[] args){
        SpellChecker sc = new SpellChecker("words.txt");
        System.out.println(sc.getIncorrectWords("test.txt"));
        HashSet<String> hs = (HashSet<String>) sc.getSuggestions("arian");
        System.out.println(hs);
    }
}