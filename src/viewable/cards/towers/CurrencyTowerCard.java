/** @author Chris Loper
 * 
 * Contains card id and card name for Currency Towers, also
 * contains an upgrade class for Currency Towers
 * 
 */
package viewable.cards.towers;

import viewable.cards.Card;
import viewable.gameObjects.CurrencyTower;
import viewable.gameObjects.Player;

public class CurrencyTowerCard extends Card {
	private static final int cardId 		= 4;
	private static final String cardName 	= "Currency Tower";
	private static final int cardCost       = 1;
	
	public CurrencyTowerCard() {
		super(cardName, cardId, cardCost);
	}
	
	public void Upgrade(CurrencyTower m) {
		m.setAttack(m.getAttack() + 1);
		m.setRange(m.getRange() + 0);
		m.setAttackSpeed(m.getAttackSpeed() + .1);
	}

	@Override
	public void Ability(Player p) {
		// TODO Auto-generated method stub
	}
}
