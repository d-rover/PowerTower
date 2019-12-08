/**
 * @author Aramis Sennyey
 * @author Ruben Tequida
 * 
 * rt update - adding drawCard and shuffle function
 */

package viewable.gameObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import viewable.cards.Card;

public class Deck {
	private List<Card> deck;
	
	public Deck() {
		deck = new ArrayList<Card>();
	}
	
	public Deck(int size) {
		deck = new ArrayList<Card>();
		
	}
	
	public Card drawCard() {
		if(deck.size()==0) {
			return null;
		}
		Card card = deck.get(0);
		deck.remove(0);
		return card;
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public void add(Card card) {
		deck.add(card);
	}
  
	public Boolean isEmpty() {
		return deck.isEmpty();
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	public void empty() {
		deck.removeAll(deck);
	}
	
	public int getSize() {
		return deck.size();
	}
	
	public List<Card> getDeckAsList() {
		return deck;
	}
	
	public String toString() {
		String s = "";
		for (Card c : deck) {
			s += c.getName() + ", ";
		}
		s = s.substring(0, s.length() - 2);
		return s;
	}
	
}