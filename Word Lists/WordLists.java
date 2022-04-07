//*************************************
//
//  WordLists.java
//
//  Class to aid with Scrabble
//  Programming Project 5, COMS W1004
//
//
//  Your Name: David Gabriel Millares Bellido
//  Your Uni: DGM2148
//  
//  Coded by DGM148, skeleton and instructions provided by 
//  1004 teaching staff.
//**************************************

import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class WordLists{
    // Instance variables
    private File dict; 
    private Scanner scan;
    private ArrayList<String> wordset; //file provided is copied in here
    
    //Methods 
    
    public WordLists(String fileName) throws FileNotFoundException{
        // your code here
        dict = new File(fileName);
        scan = new Scanner(dict);
        wordset = new ArrayList<String>();
        while(scan.hasNext()){ //loop that copies file into wordset ArrayList
            wordset.add(scan.next());
        } //end of while loop
    } // end of constructor method


    public ArrayList<String> lengthN(int n){  
        // your code here
        if(n<0){
            throw new IllegalArgumentException();
        } //check for a negative length 
        ArrayList<String> words =  new ArrayList<String>();
        String test = "";
        for(String element : wordset){ //enhanced for loop
            if(element.length() == n){
                words.add(element);
            } // checks if words are of N length
        } //end of for loop
        return words;
    } //end of lengthN method


    public ArrayList<String> endsWith(char lastLetter, int n){
        // your code here
        ArrayList<String> words = lengthN(n); //Calls on lengthN method
        for(int i=0; i<words.size(); i++){ //works only on the lengthN words
            int l = words.get(i).length()-1; //gets index of last letter
            char test = words.get(i).charAt(l); //gets char at last letter
            //checks if given last letter is the same as actual last letter
            if(test != lastLetter){ 
                words.remove(i); 
                i--; 
                //this i-- is due to remove() method moving all objects
                //down. If this were not included, some words would never 
                //be checked
            }
        } //end of LastLetter test loop
        return words;
    } // end of endsWith method

    public ArrayList<String> containsLetter(char included, int index, int n){
        // your code here
        ArrayList<String> words = lengthN(n); //call on lengthN method
        for(int i=0; i<words.size(); i++){ //evaluates only lengthN words
            char test = words.get(i).charAt(index); //gets char at given index
            // checks if given letter is same as letter at given index
            if(test != included){ 
                words.remove(i);
                i--;
                //same i-- concept as method before
            }
        } //end of test loop
        return words;
    } // end of containsLetter method

 
    public ArrayList<String> multiLetter(int m, char included){
        // your code here
        ArrayList<String> words = new ArrayList<String>();
        //must check all words in dictionary (wordset)
        for(String element : wordset){ 
            int counter = 0; //keeps track of how many times letter appears
            //takes a word from wordset and checks every letter
            for(int i=0; i<element.length(); i++){
                char letter = element.charAt(i);
                if(letter == included){
                    //increases counter if letter is the given one
                    counter++;
                }
            }//end of inner for loop
            if(counter==m){
                words.add(element);
                // if letter appears m times, it is added to words
            }
        } //end of outer for loop
        return words;
    } // end of multiLetter method
    
    // My methods
    
    //static method that helps print a given ArrayList to avoid repeated code
    public static void makeFile(String name, ArrayList<String> contents) 
        throws FileNotFoundException{
        PrintWriter copy = new PrintWriter(name);
        for(String element : contents){
            copy.println(element);
        }
        copy.close();
    } // end of makeFile method
} // end of class
