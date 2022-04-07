/* *****************************************
 * Spell Checker class
 * Name: David Gabriel Millares Bellido
 * Date: July 31, 2021
 * UNI: DGM2148
 * *****************************************
 */

import java.util.*;
import java.io.*;

public class SpellChecker implements SpellCheckerInterface{
    
    // Instance Variables
    HashSet<String> wordset;
    char[] alphabet = new char[26];
    // Constructor
    
    public SpellChecker(String fileName){
        try{
            File words = new File(fileName);
            wordset = new HashSet<String>();
            Scanner scan = new Scanner(words);
            while(scan.hasNext()){ // fills in HashSet with dictionary words
                String now = scan.next();
                StringBuilder sb = new StringBuilder("");
                for(int i=0; i<now.length(); i++){
                    char test = now.charAt(i);
                    if(!isPunctuation(test)){
                        if(!isNumber(test)){
                            test = Character.toLowerCase(test);
                        }
                        sb.append(test);
                    }
                }
                wordset.add(sb.toString());
            } // end of loop
            alphabetFiller();
        }
        catch(FileNotFoundException e){
            System.out.println("Bummer! I can't find the file. Try again!");
        }
    } // end of Constructor
    
    // Interface Methods
    
    public List<String> getIncorrectWords(String filename){
        ArrayList<String> incorrect = new ArrayList<String>();
        try{
            File theFile = new File(filename);
            Scanner scan = new Scanner(theFile);
            while(scan.hasNext()){
                String test = scan.nextLine();
                ArrayList<String> thisLine = (ArrayList<String>) checkWords(test);
                for(String element : thisLine){
                    incorrect.add(element);
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Bummer! I can't find the file. Try again!");
        }
        finally{
            return incorrect;
        }
    } // end of getIncorrectWords method
    
	public Set<String> getSuggestions(String word){
        if(word.length()<1){
            return new HashSet<String>();
        }

        HashSet<String> add = addSuggestions(word);
        HashSet<String> remove = removeSuggestions(word);
        HashSet<String> swap = swapSuggestions(word);
        
        return setMerge(add, remove, swap);
    } // end of getSuggestions method
    
    /* 
     * *********************************************
     * My Helper Methods
     * *********************************************
     */
    
    // getIncorrectWords helper Methods
    
    private List<String> checkWords(String line){
        ArrayList<String> check = new ArrayList<String>();
        StringBuilder sb = new StringBuilder("");
        for(int i=0; i<line.length(); i++){
            char test = line.charAt(i);
            if(i==line.length()-1 && test !=' '&&!isPunctuation(test)){
                sb.append(test);
                test = ' ';
            }
            if(test==' '){
                if(!isValid(sb.toString())){
                    check.add(sb.toString());
                }
                sb.delete(0, sb.length());
            }
            else if(!isPunctuation(test)){
                if(!isNumber(test)){
                    test = Character.toLowerCase(test);
                }
                sb.append(test);
            }
        }
        
        return check;
    } // end of checkWords method
    
    private boolean isPunctuation(char c){
        //pp stands for possible punctuation
        boolean pp = c=='q'||c=='w'||c=='e'||c=='r'||c=='t'||c=='y'||c=='u'||
            c=='i'||c=='o'||c=='p'||c=='a'||c=='s'||c=='d'||c=='f'||c=='g'||
            c=='h'||c=='j'||c=='k'||c=='l'||c=='z'||c=='x'||c=='c'||c=='v'||
            c=='b'||c=='n'||c=='m'||
            c=='Q'||c=='W'||c=='E'||c=='R'||c=='T'||c=='Y'||c=='U'||
            c=='I'||c=='O'||c=='P'||c=='A'||c=='S'||c=='D'||c=='F'||c=='G'||
            c=='H'||c=='J'||c=='K'||c=='L'||c=='Z'||c=='X'||c=='C'||c=='V'||
            c=='B'||c=='N'||c=='M'||isNumber(c);
        
        return !pp;
    } // end of isPunctuation method
    
    private boolean isNumber(char c){
        return c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||
            c=='8'||c=='9'||c=='0';
    } // end of isNumber method
    
    public boolean isValid(String test){
        return wordset.contains(test);
    } // end of isValid method
    
    // getSuggestions helper Methods
    
    private void alphabetFiller(){
        String ab = "abcdefghijklmnopqrstuvwxyz";
        for(int i=0; i<ab.length(); i++){
            alphabet[i] = ab.charAt(i);
        }
    } // end of alphabetFiller method
    
    private HashSet<String> addSuggestions(String word){
        HashSet<String> temp = new HashSet<String>();
        StringBuilder sb = new StringBuilder();
        for(int add=0; add<=word.length(); add++){
            for(int i=0; i<alphabet.length; i++){
                boolean charIn = false;
                for(int k=0; k<=word.length(); k++){
                    if(add==k){
                        sb.append(alphabet[i]);
                        charIn = true;
                    }
                    else if(charIn){
                        sb.append(word.charAt(k-1));
                    }
                    else{
                        sb.append(word.charAt(k));
                    }
                } // end of third for loop, gets actual added string
                if(isValid(sb.toString())){
                    temp.add(sb.toString());
                } // checks to see if suggestion is valid
                sb.delete(0, sb.length());
            } // end of second for loop, gets each letter in alphabet
        
        } // end of first for loop, tells where to add character
        return temp;
    } // end of addSuggestions method
    
    private HashSet<String> removeSuggestions(String word){
        HashSet<String> temp = new HashSet<String>();
        for(int i=0; i<word.length(); i++){
            StringBuilder sb = new StringBuilder();
            for(int k=0; k<word.length(); k++){
                if(k!=i){
                    sb.append(word.charAt(k));
                }
            }
            System.out.println(sb);
            if(isValid(sb.toString())){
                temp.add(sb.toString());
            }
            sb.delete(0, sb.length());
        }
        return temp;
    }
    
    private HashSet<String> swapSuggestions(String word){
        HashSet<String> temp = new HashSet<String>();
        for(int i=1; i<word.length(); i++){
            StringBuilder sb = new StringBuilder();
            for(int j=0; j<word.length(); j++){
                if(j+1==i){
                    sb.append(word.charAt(j+1));
                    sb.append(word.charAt(j));
                    j++;
                }
                else{
                    sb.append(word.charAt(j));
                }
            }
            if(isValid(sb.toString())){
                temp.add(sb.toString());
            }
            sb.delete(0, sb.length());
        }
        return temp;
    }
    
    private HashSet<String> setMerge(Set<String> add,Set<String> rem, Set<String> swap){
        HashSet<String> sugg = new HashSet<String>();
        for(String element : add){
            sugg.add(element);
        }
        for(String element : rem){
            sugg.add(element);
        }
        for(String element : swap){
            sugg.add(element);
        }
        return sugg;
    }
    
} // end of class