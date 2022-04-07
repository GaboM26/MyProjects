Sources: office hours
WordLists readMe

***WordLists***
Only three instance variables used. I copied the dictionary into an ArrayList
because scanner will scan the whole file, get to the bottom, and then other
methods that try and use the scan.hasNext method do not work. This is due
to Scanner getting to the bottom of the dictionary.txt file and staying there
(source: Deji's office hours). The fix for this is having an ArrayList with
all the words and one can access all of them through an enhanced for loop. 
All four required file names have the name of their respective method on them,
but that can be changed.

lengthN method: First checks if n is more than 0. A user could place a negative
n, thus throwing a IllegalArgument exception, because there are no words with
negative length. I only do this in this method, because endsWith and 
containsLetter method call on lengthN method anyways. If length passes this
test, then it simply checks all letters one by one to see if they have the 
provided length. 

endsWith method: Calls on lengthN method to only work on the n length words. 
This simplifies the code a lot, and the rest is simple. The one interesting 
detail is the "i--;" that is included in the if statement. This is due to the 
remove(i) method moving all elements to the right of i one to the left. This
means that the index i is now different to what it was before the remove(). 
You also want to check this word, so the i-- is put in there to ensure code
checks it. The reason I didn't just create a new ArrayList is to save memory
and optimize the code. There is really no reason to create a new ArrayList
when you can just remove elements from the local one. 

containsLetter method: this is just a generalization of the endsWith method.
In fact, to check this I put the parameter with same length as endsWith and
simply chose the final index. It starts by calling lengthN, just as before. 
Then it picks every single word from the lengthN words arrayList and
gets the char from the given index. After this it works exactly like
the endsWith method. 

multiLetter method: This method has to check every single letter in every
single word in the dictionary file. This immediately made me realize I needed
nested for loops. The outer for loop fetches a given word, and the inner for 
loop checks every single character (letter) in the word. There is also a
counter that keeps track of how many times we find a given letter. After
it checks all letters in a word, it checks if the counter is equal to
the number of letters in a word we are trying to find. 

static makeFile method: This is a static method because it doesn't really
need any of the instance variables, but is a useful way of printing the 
ArrayLists returned by the previous methods. This was done to make the
WordTest neater, and only needs an ArrayList and a name as parameters

***WordTest***

try: It instantiates the WordList (possible FileNotFoundException)
and an ArrayList into which every method can return its respective
ArrayList to. This is all the preparation needed to print and test all other
methods. Test file then proceeds to call on a single method and putting
it into the ArrayList that is instantiated. Then it puts it back into the
makeFile method. It does this 4 times with the 4 different methods, and calling
on the static method 4 times. 

catch: I decided to try and catch three different errors. The first one is
FileNotFoundException, which is the one Java requires us to handle. However,
I decided to check on possible negative integers placed as Word Lengths. The
final check I decided to make is one error that I actually made. It is possible
that a user wants a word with length n and also asks for index n. A common 
mistake is to look for index n in a length n word. However, the last character 
in a string has index n-1. This could cause program to crash, so I decided to 
also add a catch on this.