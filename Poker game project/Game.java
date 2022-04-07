/*
 * *********************************************************
 * Poker Game Class
 * Author: Gabriel Millares Bellido
 * UNI: DGM2148
 * (Skeleton given by teaching staff of 1004COMSCI)
 * *********************************************************
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
	
    private Player p;
    private Deck cards;
    private ArrayList<Card> hand;
    // you may want some more here
	private Scanner input;
    private boolean replay;
    String[] testCards;
	
    public Game(String[] testHand){
        // This constructor is to help test your code.
        // use the contents of testHand to
        // make a hand for the player
        // you must use the following encoding for cards
        // c = clubs
        // d = diamonds
        // h = hearts
        // s = spades
        // 1-13 correspond to ace-king
        // example: s1 = ace of spades
        // example: testhand = {s1, s13, s12, s11, s10} = royal flush
		p = new Player();
        cards = new Deck();
        hand = new ArrayList<Card>();
        input = new Scanner(System.in);
        replay = true;
        testCards = testHand;
    }
	
    public Game(){
		
        // This no-argument constructor is to actually play a normal game
		p = new Player();
        cards = new Deck();
        hand = new ArrayList<Card>();
        input = new Scanner(System.in);
        replay = true;
    }
	
    public void play(){
        
        // this method should play the game
        System.out.println("**************************************************************************");
        System.out.println("Welcome to Gabriel's *-*ULTIMATE VIDEOPOKER GAME*-*");
        System.out.println("**************************************************************************\n");
        System.out.println("You start with *** 100 TOKENS ***");
        System.out.println("**************************************************************************\n");            
        while(replay){
            hand.clear();
            cards.shuffle();
            System.out.println("Please insert your bet (between 1 and 5 token(s)): \n");
            int bet = input.nextInt();
            while(!validBet(bet)){
                System.out.println("I admire a risk taker! but lets keep it low stakes for now");
                System.out.println("******************************************************************");
                System.out.println("Please insert a number between 1 and 5 token(s): \n");
                bet = input.nextInt();
            }
            p.bets(bet);
            dealer();
            changeHand();
            handPrinter();
            p.winnings(checkHand());
            playAgain();
        }
        System.out.println("It was great playing some games of Poker with you! Please come back to...");
        System.out.println("\n**************************************************************************");
        System.out.println("Gabriel's *-*ULTIMATE VIDEOPOKER GAME*-*");
        System.out.println("**************************************************************************");
    }
    
    public void testPlay(){
        // this method is used for testing the checkHand method
        // it should evaluate the hand that was passed
        // as a command-line argument and print the result
        System.out.println("**************************************************************************");
        System.out.println("Welcome to Gabriel's *-*ULTIMATE VIDEOPOKER GAME TESTER*-*");
        System.out.println("**************************************************************************\n");
        dealer(testCards);
        handPrinter();
        int k = checkHand();
        System.out.println("The multiplier associated with this hand is: " + k);
        System.out.println("Come back anytime to test different Hands!!!!");
        System.out.println("**************************************************************************");
    }
	
	public int checkHand(){
        // this method should take an ArrayList of five Card objects as input
		// evaluate the hand
		// print the result to the console so the user can see it. 
        // return the multiplier that the bet should be multiplied by
        // you'll want to break this up into a bunch of helper methods  
        System.out.println("You got...");
        if(royalFlush()){
            System.out.println("ROYAL FLUSH!!!!!!! I can't believe my eyes :o!!!!");
            return 250;
        }
        if(straightFlush()){
            System.out.println("STRAIGHT FLUSH! Never had any doubts :)");
            return 50;
        }
        if(fourKind()){
            System.out.println("FOUR OF A KIND! Haven't seen one of these in years...");
            return 25;
        }
        if(fullHouse()){
            System.out.println("FULL HOUSE! This is VERY difficult. Congratulations!");
            return 6;
        }
        if(flush()){
            System.out.println("FLUSH! The best name for a hand. Well done!");
            return 5;
        }
        if(straight()){
            System.out.println("STRAIGHT! Dont you think Straight's just look amazing?");
            return 4;
        }
        if(threeKind()){
            System.out.println("THREE OF A KIND! Thought this was just a pair for a second. Well Done!");
            return 3; 
        }
        if(twoPairs()){
            System.out.println("TWO PAIRS! It might seem not good, but think of it as almost a four of a Kind");
            return 2;
        }
        if(pair()){
            System.out.println("PAIR! Breaking even is not bad. NEVER LOSE HOPE!!!");
            return 1;
        }
        System.out.println("HIGH CARD: " + hand.get(4) + ". You can bounce back from this!");
        return 0;
    }
	
    
    // My Helper Methods
    
    private boolean validBet(int bet){
        boolean valid = false;
        if(bet>0 && bet<=5)
            valid = true;
        return valid; 
    }
    
    private void dealer(){
        for(int i=0; i<5; i++){
            hand.add(cards.deal());
        }
    }
    
    private void handPrinter(){
        Collections.sort(hand);
        System.out.println("\nYour hand now is: \n");
        int i=1;
        for(Card element : hand){
            System.out.println(i + ". " + element);
            i++;
        }
    }
    
    private void changeHand(){
        handPrinter();
        boolean cont = true;
        int counter = 0;
        while(cont){
            System.out.println("Please input the card index you wish to change (1-5) or (0) to continue");
            int change = input.nextInt()-1;
            if(change==-1)
                cont = false;
            else{
                hand.remove(change);
                handPrinter();
                counter++;
            }
        }
        
        for(int i=0; i<counter; i++){
            hand.add(cards.deal());
        }
    }
    
    private boolean royalFlush(){
        boolean rFlush = true;
        int r=10;
        int s = hand.get(0).getSuit();
        for(int i=1; i<hand.size(); i++){
            if(hand.get(i).getRank()!=r || s!=hand.get(i).getSuit())
                rFlush = false;
            r++;
            }
        if(hand.get(0).getRank()!=1){
            rFlush = false;
        }
        return rFlush;
    }
    
    private boolean straightFlush(){
        return straight() && flush();
    }
    
    private boolean fourKind(){
        Card sample = hand.get(2);
        int counter = 0;
        for(Card element:hand){
            if(sample.getRank()==element.getRank())
                counter++;
        }
        return counter==4;
    }
    
    private boolean fullHouse(){
        Card sample1 = hand.get(0), sample2 = hand.get(4);
        int counter1 = 0, counter2 = 0;
        for(Card element : hand){
            if(sample1.getRank()==element.getRank())
                counter1++;
            if(sample2.getRank()==element.getRank())
                counter2++;
        }
        return (counter1==3&&counter2==2) || (counter1==2&&counter2==3);
    }
    
    private boolean flush(){
        int s = hand.get(0).getSuit();
        boolean flush = true;
        for(int i=0; i<hand.size(); i++){
            if(s!=hand.get(i).getSuit())
                flush = false;
        }
        return flush;
    }
    
    private boolean straight(){
        int r = hand.get(0).getRank();
        boolean straight = true;
        for(int i=0; i<hand.size(); i++){
            if(r!= hand.get(i).getRank())
                straight = false;
            r++;
        }
        r = hand.get(0).getRank();
        if(!straight && r==1){
            straight = true;
            int test = 10;
            for(int i=1; i<hand.size(); i++){
                if(test!=hand.get(i).getRank())
                    straight = false;
                test++;
            }
        }
        return straight;
    }
    
    private boolean threeKind(){
        int counter = 0;
        Card sample = hand.get(2);
        for(Card element : hand){
            if(sample.getRank() == element.getRank())
                counter++;
        }
        return counter==3;
    }
    
    private boolean twoPairs(){
        Card sample1 = hand.get(1), sample2 = hand.get(3);
        int counter1 = 0, counter2 = 0;
        for(Card element : hand){
            if(sample1.getRank() == element.getRank())
                counter1++;
            if(sample2.getRank() == element.getRank())
                counter2++;
        }
        return counter1==2 && counter2==2;
        
    }
    
    private boolean pair(){
        boolean pair = false;
        for(int i=1; i<hand.size(); i++){
            if(hand.get(i).getRank() == hand.get(i-1).getRank())
                pair = true;
        }
        return pair;
    }
    
    private void playAgain(){
        int difference = p.getBankroll()-100;
        System.out.println("You now have: " + p.getBankroll() + "tokens.");
        if(p.getBankroll()<=100)
            System.out.println("Honestly, i've seen much worse." + 
                               " Do you want to try and do better? Im sure you will! (y/n)");
        else
            System.out.println("You're doing amazing! Don't you want to keep playing? (y/n)");
        boolean yn = false;
        input.nextLine();
        while(!yn){
            String repeat = input.nextLine();
            if(!repeat.equals("y")){
                if(repeat.equals("n")){
                    yn = true;
                    replay =false;
                }
                else{
                    System.out.println("I don't quite understand... Please enter a 'y' to continue or 'n' to quit.");
                }
            }
            else
                yn = true;
        }
    }
    
    private void dealer(String[] test){
        for(int i=0; i<test.length; i++){
            Card temp = Card.stringToCard(test[i]);
            hand.add(temp);
        }
    }
}

