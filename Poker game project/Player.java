/*
 * *****************************************************************
 * Poker Player Class
 * Author: Gabriel Millares Bellido
 * UNI: DGM2148
 * (Skeleton given by teaching staff of 1004COMSCI)
 * *****************************************************************
 */


public class Player {
	
		
    private int bankroll;
    private int bet;

    //you may choose to use more instance variables
		
    public Player(){		
        //create a player here
        bankroll = 100;
        bet = -1;
    }
		
    public void bets(int amt){
        //player makes a bet
        bet = amt;
        bankroll += -amt;
    }

    public void winnings(int odds){
        //adjust bankroll if player wins
        bankroll = bankroll + bet*odds;
    }

    public int getBankroll(){
        //return current balance of bankroll
        return bankroll;
    }

    //you may wish to use more methods here
    

}


