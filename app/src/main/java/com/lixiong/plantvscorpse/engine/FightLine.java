package com.lixiong.plantvscorpse.engine;

import com.lixiong.plantvscorpse.bean.base.AttackPlant;
import com.lixiong.plantvscorpse.bean.base.BaseElement;
import com.lixiong.plantvscorpse.bean.base.Bullet;
import com.lixiong.plantvscorpse.bean.base.Plant;
import com.lixiong.plantvscorpse.bean.base.Zombies;

import org.cocos2d.actions.CCScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行战场
 *
 * @author Administrator
 *
 */
public class FightLine implements DieListener {
	private int lineNum;
	private List<Zombies> zombiesList;// 当前行添加的僵尸集合

	private Map<Integer, Plant> plants;// 当前行添加的植物集合
	private List<AttackPlant> attackPlantList;// 当前行添加的攻击型植物

	public FightLine(int lineNum) {
		super();
		this.lineNum = lineNum;
		zombiesList = new ArrayList<Zombies>();

		plants = new HashMap<Integer, Plant>();

		attackPlantList = new ArrayList<AttackPlant>();

		// 僵尸攻击植物
		// 每隔一个时间，判断当前行是否有僵尸，判断当前行是否有植物，将所有僵尸循环，僵尸的脚下是否有植物
		CCScheduler.sharedScheduler()
				.schedule("attackPlant", this, 0.5f, false);

		// 每隔一个时间，判断当前行是否有僵尸，判断当前行是否有攻击植物，将所有攻击型植物循环，攻击僵尸
		CCScheduler.sharedScheduler().schedule("attackZombies", this, 0.5f,
				false);

		// 每隔一个时间，判断当前行是否有僵尸，判断当前行是否有攻击植物，获取到所有攻击型植物的弹夹，判断所有子弹，是否击中僵尸
		CCScheduler.sharedScheduler()
				.schedule("checkBullet", this, 0.2f, false);

	}

	/**
	 * 检查弹夹
	 *
	 * @param t
	 */
	public void checkBullet(float t) {
		if (zombiesList.size() > 0 && attackPlantList.size() > 0) {
			for (AttackPlant attackPlant : attackPlantList) {
				List<Bullet> bullets = attackPlant.getBullets();

				for (Bullet bullet : bullets) {

					for (Zombies zombies : zombiesList) {
						int left = (int) (zombies.getPosition().x - 20);
						int right = (int) (zombies.getPosition().x + 20);
						int x = (int) bullet.getPosition().x;

						if (x >= left && x <= right) {
//							bullet.destroy();
							zombies.attacked(bullet.getAttack());
						}
					}

				}

			}
		}
	}

	// 添加僵尸（集合）

	public void addZombies(Zombies zombies) {
		zombies.setDieListener(this);
		zombiesList.add(zombies);
	}

	// 添加植物
	// 添加攻击型植物

	public void addPlant(Plant plant) {
		plant.setDieListener(this);
		plants.put(plant.getRow(), plant);

		if (plant instanceof AttackPlant) {
			attackPlantList.add((AttackPlant) plant);
		}
	}

	// 僵尸攻击植物：判断当前所在的块内是否有植物
	public void attackPlant(float t) {
		if (plants.size() > 0 && zombiesList.size() > 0) {
			for (Zombies item : zombiesList) {
				int key = (int) (item.getPosition().x / 46 - 1);
				Plant plant = plants.get(key);

				if (plant != null) {
					item.attack(plant);
				}
			}
		}
	}

	// 植物攻击僵尸：攻击植物的集合+僵尸的集合中是否含有僵尸
	public void attackZombies(float t) {
		if (zombiesList.size() > 0 && attackPlantList.size() > 0) {
			for (AttackPlant item : attackPlantList) {
				item.createBullet();
			}
		}
	}

	// 判断是否可以在该行的某个列上安放植物
	public boolean isAdd(Plant plant) {
		// if(plants.containsKey(plant.getRow()))
		// {
		// if(plant instanceof PlantNGT)
		// {
		// return true;
		// }
		// }
		return !plants.containsKey(plant.getRow());
	}

	@Override
	public void onDie(BaseElement element) {
		if (element instanceof Plant) {
			int key = ((Plant) element).getRow();
			plants.remove(key);

			if (element instanceof AttackPlant) {
				attackPlantList.remove(element);
			}
		} else {
			zombiesList.remove(element);
		}

	}

}
