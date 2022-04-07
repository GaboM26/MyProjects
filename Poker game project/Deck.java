/*
 * *****************************************************************
 * Poker Deck Class
 * Author: Gabriel Millares Bellido
 * UNI: DGM2148
 * (Skeleton given by teaching staff of 1004COMSCI)
 * *****************************************************************
 */

import java.util.Random;
public class Deck {
	
    private Card[] cards;
    private int top; //the index of the top of the deck 

    //add more instance variables if needed
    
    public Deck(){
        //make a 52 card deck here
        cards = new Card[52];
        fillDeck();
        top = 0;
    }
	
    public void shuffle(){
        //shuffle the deck here
        Random rng = new Random();
        for(int i=0; i<cards.length; i++){
            int shuffler = rng.nextInt(52);
            Card temp = cards[i];
            cards[i] = cards[shuffler];
            cards[shuffler] = temp;
        }
        top = 0;
    }
    
    public Card deal(){
        // deal a single cared here
        Card c = cards[top];
        newTop();
        return c;
    }
	
    //add more methods here if needed
    
    private void fillDeck(){
        int i = 0;
        for(int s=1; s<5; s++){
            for(int r=1; r<14; r++){
                Card filler = new Card(s,r);
                cards[i] = filler;
                i++;
            } // end of inner for loop
        } //end of outer for loop
    } // end of fillDeck method
    
    public void print(){
        for(int i=0; i<cards.length; i++)
            System.out.println(cards[i]);
        
    }
    
    private void newTop(){
        top++;
    }
    
}
