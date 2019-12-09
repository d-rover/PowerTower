package viewable.gameObjects;

import java.io.File;
import java.io.IOException;

public class Boss extends Minion {
	
	private static final int defaultHealth = 40;
	private static final int defaultDamage = 10;
	private static final int defaultSpeed = 1;
	private static final int defaultReward = 100;

	public Boss() {
		super(defaultHealth, defaultDamage, defaultSpeed, defaultReward);
	}

	@Override
	public String getResource() {
		try {
			return (new File("./resources/images/MonsterSprites/Boss.png")).getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

}
