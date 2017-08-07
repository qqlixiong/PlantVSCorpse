package com.lixiong.plantvscorpse.bean.zombies;

import com.lixiong.plantvscorpse.bean.base.BaseElement;
import com.lixiong.plantvscorpse.bean.base.Plant;
import com.lixiong.plantvscorpse.bean.base.Zombies;
import com.lixiong.plantvscorpse.engine.GameController;
import com.lixiong.plantvscorpse.util.CommonUtil;

import java.util.ArrayList;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.util.CGPointUtil;
/**
 * 初级僵尸
 *
 * @author Administrator
 *
 */
public class PrimaryZombies extends Zombies {
	private static ArrayList<CCSpriteFrame> shakeFrames;// 摇晃的序列帧
	private static ArrayList<CCSpriteFrame> moveFrames;
	private static ArrayList<CCSpriteFrame> attackFrame;

	public PrimaryZombies() {
		// 展示用僵尸
		super("image/zombies/zombies_1/walk/z_1_01.png");
		baseAction();
	}

	public PrimaryZombies(CGPoint start, CGPoint end) {
		// 对战用僵尸创建
		super("image/zombies/zombies_1/walk/z_1_01.png");

		this.startPoint = start;
		this.endPoint = end;

		this.setPosition(start);

		move();
	}

	@Override
	public void move() {
		float t = CGPointUtil.distance(getPosition(), this.endPoint) / speed;
		CCMoveTo moveTo = CCMoveTo.action(t, this.endPoint);

		CCSequence sequence = CCSequence.actions(moveTo,
				CCCallFunc.action(this, "gameOver"));
		this.runAction(sequence);

		this.runAction(CommonUtil.getRepeatForeverAnimate(moveFrames, 7,
				"image/zombies/zombies_1/walk/z_1_%02d.png"));

	}

	public void gameOver() {
		destroy();
		GameController.getInstance().gameOver();
	}

	private boolean isAttack = false;// 记录僵尸的攻击状态
	private BaseElement target;// 记录攻击目标

	@Override
	public void attack(BaseElement element) {
		if (!isAttack) {
			isAttack = true;

			target = element;
			// 停止所有的动作
			this.stopAllActions();
			// 播放攻击序列帧
			this.runAction(CommonUtil.getRepeatForeverAnimate(attackFrame, 10,
					"image/zombies/zombies_1/attack/z_1_attack_%02d.png"));

			// 造成植物的持续杀伤
			CCScheduler.sharedScheduler().schedule("attackPlant", this, 0.5f,
					false);

		}

	}

	/**
	 * 持续杀伤
	 *
	 * @param t
	 */
	public void attackPlant(float t) {
		// 处理植物
		if (target instanceof Plant) {
			Plant plant = (Plant) target;
			plant.attacked(attack);

			if (plant.getLife() <= 0) {
				// 停止timer
				CCScheduler.sharedScheduler().unschedule("attackPlant", this);
				// isAttack
				isAttack = false;
				// 停止攻击序列帧
				this.stopAllActions();
				// move
				move();
			}

		}
	}

	private boolean isDie = false;


	private static ArrayList<CCSpriteFrame> headFrame;// 头掉下来
	private static ArrayList<CCSpriteFrame> dieFrame;// 趴着地上
	private static String headRes = "image/zombies/zombies_1/head/z_1_head_%02d.png";
	private static String dieRes = "image/zombies/zombies_1/die/z_1_die_%02d.png";

	@Override
	public void attacked(int attack) {
		life -= attack;
		if (life <= 0 && !isDie) {
			isDie = true;
			// 脑袋掉下
			// 慢慢的趴在地上

			// 脑袋掉下来，慢慢的爬到地上，销毁
			this.stopAllActions();
			CCAnimate head = (CCAnimate) CommonUtil.getAnimate(headFrame, 6,
					headRes);
			CCAnimate die = (CCAnimate) CommonUtil.getAnimate(dieFrame, 6,
					dieRes);
			// 播放僵尸倒下的动画
			this.runAction(CCSequence.actions(head, die,
					CCCallFunc.action(this, "destroy")));

		}

	}

	@Override
	public void baseAction() {
		CCAction repeatForeverAnimate = CommonUtil.getRepeatForeverAnimate(
				shakeFrames, 2, "image/zombies/zombies_1/shake/z_1_%02d.png");
		this.runAction(repeatForeverAnimate);

	}

}
