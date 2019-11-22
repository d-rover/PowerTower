package viewable.gameObjects;

public class GoblinKnight extends Minion {

	private static final int defaultHealth = 10;
	private static final int defaultDamage = 2;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 5;
	
	public GoblinKnight(Integer health, Integer damage, Integer speed, Integer reward) {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward);
	}

}
