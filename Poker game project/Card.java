
/*
 * ********************************************************************
 * Poker Card Class
 * Author: Gabriel Millares Bellido
 * UNI: DGM2148
 * (Skeleton given by teaching staff of 1004COMSCI)
 * ********************************************************************
 */


public class Card implements Comparable<Card>{
	
    private int suit; //use integers 1-4 to encode the suit
    private int rank; //use integers 1-13 to encode the rank
	
    public Card(int s, int r){
        //make a card with suit s and rank r
        suit = s;
        rank = r;
    }
	
    
    public int compareTo(Card c){
        //use this method to compare cards so they 
        //may be easily sorted
        int compare = this.rank-c.getRank();
        if(compare==0){
            int compare2 = this.suit-c.getSuit(); //What do I do here?
            return compare2;
        }
        else
            return compare; 
    }
	
    public String toString(){
        //use this method to easily print a Card object
        String text = "";
        if(suit==1)
            text = "clubs ";
        if(suit==2)
            text = "diamonds ";
        if(suit==3)
            text = "hearts ";
        if(suit==4)
            text = "spades ";
        if(rank==1||rank>10){
            if(rank==1)
                text += "A";
            if(rank==11)
                text += "J";
            if(rank==12)
                text += "Q";
            if(rank==13)
                text += "K";
        }
        else
            text += rank;
        return text;
    }
    
    //add some more methods here if needed
    
    public int getRank(){
        return rank;
    }
    
    public int getSuit(){
        return suit;
    }
    
    public static Card stringToCard(String text){
        int r = Integer.parseInt(text.substring(1, text.length()));
        String tSuit = text.substring(0, 1);
        int s = -1;
        if(tSuit.equals("c"))
            s = 1;
        if(tSuit.equals("d"))
            s = 2;
        if(tSuit.equals("h"))
            s = 3;
        if(tSuit.equals("s"))
            s = 4;
        Card c = new Card(s, r);
        return c;
    }
}

