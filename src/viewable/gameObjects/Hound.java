package viewable.gameObjects;

public class Hound extends Minion {
	
	private static final int defaultHealth = 5;
	private static final int defaultDamage = 1;
	private static final int defaultSpeed = 2;
	private static final int defaultReward = 2;

	public Hound() {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward);
	}

	@Override
	public String getResource() {
		// TODO Auto-generated method stub
		return null;
	}

}
