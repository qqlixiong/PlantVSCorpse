package com.lixiong.plantvscorpse.layer;

import android.view.MotionEvent;

import com.lixiong.plantvscorpse.bean.ShowPlant;
import com.lixiong.plantvscorpse.bean.ShowZombies;
import com.lixiong.plantvscorpse.engine.GameController;
import com.lixiong.plantvscorpse.util.CommonUtil;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 对战的layer
 *
 * @author Administrator
 *
 */
public class FightLayer extends BaseLayer {
	private static final String TAG = "FightLayer";
	// 1、地图展示（无法处理touch事件）
	// ①展示对战地图（展示时间）
	// ②加载展示用的僵尸(展示用点位)
	// ③移动地图（停留一段时间）
	// ④展示两个容器：玩家已有植物容器（多）；玩家已选植物容器（少）

	public static final int TAG_CHOSE = 10;

	private CCTMXTiledMap gameMap;
	private List<ShowZombies> showZombiesList;

	private CCSprite chooseContainer;// 玩家已有植物容器（多）
	private CCSprite choseContainer;// 玩家已选植物容器（少）

	private List<ShowPlant> showPlantList;// 玩家已有的植物集合
	private List<ShowPlant> chosePlantList;// 玩家已有的植物集合

	private CCSprite start;

	public FightLayer() {
		init();

	}

	private void init() {
		// setIsTouchEnabled(true);
		loadMap();
		loadShowZombies();
		moveMap();
	}

	private void moveMap() {

		CGPoint pos = CGPoint.ccp(
				gameMap.getPosition().x
						- (gameMap.getContentSize().width - winSize.width),
				gameMap.getPosition().y);
		CCMoveTo moveTo = CCMoveTo.action(1, pos);
		CCSequence sequence = CCSequence.actions(CCDelayTime.action(1), moveTo,
				CCDelayTime.action(0.5f),
				CCCallFunc.action(this, "loadContainer"));
		gameMap.runAction(sequence);

	}

	/**
	 * 加载两个容器：玩家已有植物容器（多）；玩家已选植物容器（少）
	 */
	public void loadContainer() {
		// Log.i(TAG, "loadContainer");
		chooseContainer = CCSprite.sprite("image/fight/chose/fight_choose.png");
		choseContainer = CCSprite.sprite("image/fight/chose/fight_chose.png");

		choseContainer.setAnchorPoint(0, 1);
		choseContainer.setPosition(0, winSize.height);

		chooseContainer.setAnchorPoint(0, 0);

		this.addChild(choseContainer,0,TAG_CHOSE);
		this.addChild(chooseContainer, 1);

		loadPlant();

		// 添加一起摇滚
		start = CCSprite.sprite("image/fight/chose/fight_start.png");
		start.setPosition(chooseContainer.getContentSize().width / 2, 30);
		chooseContainer.addChild(start);
	}

	private int rowNum = 4;

	private void loadPlant() {
		showPlantList = new ArrayList<ShowPlant>();
		chosePlantList = new CopyOnWriteArrayList<ShowPlant>();
		// 加载玩家已有植物信息
		for (int id = 1; id <= 9; id++) {
			ShowPlant plant = new ShowPlant(id);

			CCSprite defaultImg = plant.getDefaultImg();
			defaultImg.setPosition(16 + ((id - 1) % rowNum) * 54,
					175 - ((id - 1) / rowNum) * 59);

			CCSprite bgImg = plant.getBgImg();
			bgImg.setPosition(16 + ((id - 1) % rowNum) * 54,
					175 - ((id - 1) / rowNum) * 59);

			chooseContainer.addChild(defaultImg);
			chooseContainer.addChild(bgImg);

			showPlantList.add(plant);
		}

		setIsTouchEnabled(true);

	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		if (GameController.getInstance().isStart()) {
			// 对战的touch处理
			GameController.getInstance().handlerTouch(event);
		} else {

			if (CommonUtil.isClicke(event, this, choseContainer)) {
				// 取消植物
				boolean isDel = false;
				for (ShowPlant item : chosePlantList) {

					if (CommonUtil.isClicke(event, this, item.getDefaultImg())) {
						CCMoveTo moveTo = CCMoveTo.action(0.2f, item.getBgImg()
								.getPosition());
						item.getDefaultImg().runAction(moveTo);

						chosePlantList.remove(item);
						isDel = true;
					}

					if (isDel) {
						item.getDefaultImg().setPosition(
								item.getDefaultImg().getPosition().x - 53,
								item.getDefaultImg().getPosition().y);
					}
				}
			} else {
				if (CommonUtil.isClicke(event, this, start)) {
					// 3、游戏开始前的准备工作（无法处理touch事件）
					setIsTouchEnabled(false);
					preGame();
				} else {
					// 选择植物,获取所有的植物
					for (ShowPlant item : showPlantList) {
						if (CommonUtil.isClicke(event, this,
								item.getDefaultImg())) {
							CGPoint pos = CGPoint.ccp(
									75 + chosePlantList.size() * 53,
									winSize.height - 65);
							// moveto动作
							CCMoveTo moveTo = CCMoveTo.action(0.3f, pos);
							item.getDefaultImg().runAction(moveTo);

							chosePlantList.add(item);
						}
					}
				}
			}
		}

		return super.ccTouchesBegan(event);
	}

	private void preGame() {
		// ①回收掉玩家已有植物容器
		chooseContainer.removeSelf();

		// 依据玩家已选植物容器找回显示内容

		for (ShowPlant item : chosePlantList) {
			CCSprite plant = item.getDefaultImg();
			plant.setPosition(
					plant.getPosition().x * 0.65f,
					plant.getPosition().y
							+ (CCDirector.sharedDirector().getWinSize().height - plant
							.getPosition().y) * 0.35f);
			plant.setScale(0.65);
			// plant.setAnchorPoint(0, 0.5f);
			this.addChild(plant);
		}

		// 玩家已有植物容器进行缩放（包含所有已经选择的植物信息）
		choseContainer.setScale(0.65f);

		// ②移动地图
		CGPoint pos = CGPoint.ccp(gameMap.getContentSize().width / 2,
				gameMap.getContentSize().height / 2);
		CCMoveTo moveTo = CCMoveTo.action(1, pos);

		CCSequence sequence = CCSequence.actions(moveTo,
				CCCallFunc.action(this, "clearShowZombies"));

		gameMap.runAction(sequence);
	}

	public void clearShowZombies() {
		// ③回收掉展示用僵尸
		for (ShowZombies item : showZombiesList) {
			item.removeSelf();
		}
		showZombiesList.clear();
		showZombiesList = null;

		ready();
	}

	private CCSprite ready;

	private void ready() {
		// ④序列帧播放：准备……安放……植物……

		ready = CCSprite.sprite("image/fight/startready_01.png");

		ready.setPosition(winSize.width / 2, winSize.height / 2);
		this.addChild(ready);
		ready.runAction(CCSequence.actions(CommonUtil.getAnimate(null, 3,
				"image/fight/startready_%02d.png"), CCCallFunc.action(this,
				"go")));
	}

	public void go() {
		ready.removeSelf();
		// 4、对战处理（需要处理touch事件）
		setIsTouchEnabled(true);
		// 游戏业务处理
		GameController.getInstance().start(gameMap, chosePlantList);
	}

	private void loadShowZombies() {
		// 加载展示用僵尸
		showZombiesList = new ArrayList<ShowZombies>();

		List<CGPoint> loadPoint = CommonUtil.loadPoint(gameMap, "zombies");

		for (CGPoint item : loadPoint) {
			ShowZombies showZombies = new ShowZombies();
			showZombies.setPosition(item);
			gameMap.addChild(showZombies);

			showZombiesList.add(showZombies);
		}

	}

	private void loadMap() {
		// 加载地图
		gameMap = CommonUtil.loadMap("image/fight/map_day.tmx");
		this.addChild(gameMap);
	}

	// 2、植物的选择（需要处理touch事件）
	// ①加载玩家已经获取到的植物信息
	// ②选择植物
	// ③取消植物
	// ④点击“一起摇滚”

	// 3、游戏开始前的准备工作（无法处理touch事件）
	// ①回收掉玩家已有植物容器
	// ②移动地图
	// ③回收掉展示用僵尸
	// ④序列帧播放：准备……安放……植物……

	// 4、对战处理（需要处理touch事件）
	// ①对战元素分析+战场划分
	// ②添加僵尸
	// ③添加植物
	// ④僵尸攻击植物
	// ⑤植物攻击僵尸

	// 辅助功能
	// ①阳光管理
	// ②进度控制：植物冻结时间控制，游戏进度控制

}
