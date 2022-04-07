//*************************************
//
//  WordTest.java
//
//  Test class for WordLists.java
//
//
//  Your Name:David Gabriel Millares Bellido
//  Your Uni: DGM2148
//**************************************

import java.util.ArrayList;
import java.io.*;

public class WordTest{
    
    public static void main(String[] args){
        try{
            char letter = 'n'; //placeholder variable for testing ease
            //instantiates WordList and ArrayList 
            WordLists text = new WordLists("dictionary.txt"); 
            ArrayList<String> list = new ArrayList<String>(); 
            //calls on 4 methods and prints 4 files
            //***************************************************
            //1st File
            list = text.lengthN(3);
            WordLists.makeFile("lengthN.txt", list);
            //2nd File
            list = text.endsWith(letter, 8);
            WordLists.makeFile("endsWith.txt", list);
            //3rd File
            list = text.containsLetter(letter, 2, 5);
            WordLists.makeFile("containsLetter.txt", list);
            //4th File
            list = text.multiLetter(3, letter);
            WordLists.makeFile("multiLetter.txt", list);
        }
        catch(FileNotFoundException f){
            System.out.println("I don't seem to find this file... " +
                              " are you sure the name is right?");
            //checks if given file is in folder
        }
        catch(IllegalArgumentException e){
            System.out.println("There is no negative word size!");
            //checks if n is not negative
        }
        catch(StringIndexOutOfBoundsException e){
            System.out.println("The index provided does not exist. Try and "+
                              "increase length of words or " +
                              "decrease index number");
            //checks if index in containsLetter exists with given length
        }
    } // end of main method
} // end of class








