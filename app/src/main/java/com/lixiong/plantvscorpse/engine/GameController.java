package com.lixiong.plantvscorpse.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;

import android.view.MotionEvent;

import com.lixiong.plantvscorpse.bean.ShowPlant;
import com.lixiong.plantvscorpse.bean.base.Plant;
import com.lixiong.plantvscorpse.bean.plant.PlantNut;
import com.lixiong.plantvscorpse.bean.plant.PlantPease;
import com.lixiong.plantvscorpse.bean.plant.PlantSun;
import com.lixiong.plantvscorpse.bean.zombies.PrimaryZombies;
import com.lixiong.plantvscorpse.layer.FightLayer;
import com.lixiong.plantvscorpse.util.CommonUtil;

/**
 * 游戏控制
 *
 * @author Administrator
 *
 */
public class GameController {
	// 4、对战处理（需要处理touch事件）
	// ①对战元素分析+战场划分
	// 公共信息：创建+销毁+存活过程中得动作+死亡监听
	// ---植物
	// ------攻击型植物
	// ------------普通豌豆
	// ------防御型植物
	// ------------坚果
	// ------生产型植物
	// ------------向日葵
	// ---僵尸
	// ------按等级划分（初级僵尸）
	// ---产物
	// ------子弹
	// ---------普通豌豆
	// ------阳光
	// ------金币

	// 战场划分：分五行进行处理——一行

	// ②添加僵尸
	// ③添加植物
	// ④僵尸攻击植物
	// ⑤植物攻击僵尸

	// 辅助功能
	// ①阳光管理
	// ②进度控制：植物冻结时间控制，游戏进度控制

	private static GameController instance = new GameController();

	public static GameController getInstance() {
		return instance;
	}

	private GameController() {
	}

	private static List<FightLine> fightLines;

	static {
		fightLines = new ArrayList<FightLine>();
		for (int lineNum = 0; lineNum <= 4; lineNum++) {
			FightLine line = new FightLine(lineNum);
			fightLines.add(line);
		}
	}

	private boolean isStart = false;
	private CCTMXTiledMap gameMap;
	private List<ShowPlant> chosePlantList;

	private FightLayer layer;
	private CGPoint[][] towers = new CGPoint[5][9];

	public void start(CCTMXTiledMap gameMap, List<ShowPlant> chosePlantList) {
		isStart = true;
		this.gameMap = gameMap;
		this.chosePlantList = chosePlantList;

		this.layer = (FightLayer) gameMap.getParent();

		loadPlantPos();

		// 添加一个僵尸
		// addZombies();
		// 每隔一个时间段，添加一个僵尸，时间是变动
		CCScheduler.sharedScheduler().schedule("addZombies", this, 5, false);
	}

	/**
	 * 解析安放植物的二维数组
	 */
	private void loadPlantPos() {
		for (int i = 1; i <= 5; i++) {
			CCTMXObjectGroup objectGroupNamed = gameMap.objectGroupNamed(String
					.format("tower0%d", i));
			for (int j = 0; j < objectGroupNamed.objects.size(); j++) {
				HashMap<String, String> item = objectGroupNamed.objects.get(j);
				towers[i - 1][j] = CGPoint.ccp(Integer.parseInt(item.get("x")),
						Integer.parseInt(item.get("y")));
			}
		}
	}

	public void addZombies(float t) {
		Random random = new Random();

		// 在哪一行
		int lineNum = random.nextInt(5);
		// 起点和终点
		int startIndex = lineNum * 2;
		int endIndex = lineNum * 2 + 1;

		List<CGPoint> loadPoint = CommonUtil.loadPoint(gameMap, "road");

		PrimaryZombies zombies = new PrimaryZombies(loadPoint.get(startIndex),
				loadPoint.get(endIndex));
		this.layer.addChild(zombies, 1);

		fightLines.get(lineNum).addZombies(zombies);

	}

	public void gameOver() {
		// isStart = false;
		// 界面切换
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	private ShowPlant currentShowPlant;// 展示用植物：
	private Plant currentPlant;// 需要创建的植物

	public void handlerTouch(MotionEvent event) {

		if (CommonUtil.isClicke(event, layer,
				layer.getChildByTag(FightLayer.TAG_CHOSE))) {
			// 选择植物

			for (ShowPlant item : chosePlantList) {
				if (CommonUtil.isClicke(event, layer, item.getDefaultImg())) {
					currentShowPlant = item;
					currentShowPlant.getDefaultImg().setOpacity(100);

					getCurrentPlant(item);
				}
			}

		} else {
			if (currentShowPlant != null) {
				// 安放植物
				// 第一层判断：安放区域（水平方向1-9；垂直方向1-5）
				if (isBuild(layer.convertTouchToNodeSpace(event))) {
					addPlant();
				}
			} else {
				// for(Sun item:Sun.SUNS)
				// {
				//
				// }
				// 收集阳光（金币）
			}
		}

	}

	private void getCurrentPlant(ShowPlant item) {
		switch (item.getId()) {
            case 4:
                currentPlant = new PlantNut();
                break;
            case 1:
                currentPlant = new PlantPease();
                break;
            case 2:
                currentPlant = new PlantSun();
                break;

			default:
				currentPlant = new PlantNut();
				break;
        }
	}

	/**
	 * 添加植物
	 */
	private void addPlant() {
		int line = currentPlant.getLine();
		FightLine fightLine = fightLines.get(line);
		// 第二层判断：是否可以叠加植物
		if (fightLine.isAdd(currentPlant)) {
			fightLine.addPlant(currentPlant);

			this.layer.addChild(currentPlant);
			currentShowPlant.getDefaultImg().setOpacity(255);
			currentShowPlant = null;
			currentPlant = null;
		}
	}

	private boolean isBuild(CGPoint touchPos) {
		// 安放区域（水平方向1-9；垂直方向1-5）

		int blockRow = (int) (touchPos.x / 46);
		int blockLine = (int) ((CCDirector.sharedDirector().getWinSize().height - touchPos.y) / 54);

		if (blockRow >= 1 && blockRow <= 9) {
			if (blockLine >= 1 && blockLine <= 5) {
				// 植物安放的行和列信息
				int row = blockRow - 1;
				int line = blockLine - 1;

//				if(currentPlant == null)getCurrentPlant(currentShowPlant);
				currentPlant.setLine(line);
				currentPlant.setRow(row);

				currentPlant.setPosition(towers[line][row]);

				return true;
			}
		}

		return false;
	}

}
