package com.lixiong.plantvscorpse.bean.plant;

import com.lixiong.plantvscorpse.bean.base.ProductPlant;
import com.lixiong.plantvscorpse.bean.product.Sun;
import com.lixiong.plantvscorpse.util.CommonUtil;

import java.util.ArrayList;

import org.cocos2d.actions.CCScheduler;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCSpriteFrame;

/**
 * 向日葵
 *
 * @author Administrator
 *
 */
public class PlantSun extends ProductPlant {
	private static final String resPath = "image/plant/sunflower/p_1_%02d.png";

	public PlantSun() {
		super("image/plant/sunflower/p_1_01.png");
		life = 100;
		baseAction();
		create();// 每隔一个时间段产生一个阳光
	}

	// 静止帧
	protected static ArrayList<CCSpriteFrame> motionlessFrames;// 静止帧集合
	protected CCAnimation motionlessAnimation;// 静止的帧
	protected CCAnimate motionlessAnimate;// 静止动作

	@Override
	public void baseAction() {
		this.runAction(CommonUtil.getRepeatForeverAnimate(motionlessFrames, 8,
				resPath));
	}

	@Override
	public void create() {
		CCScheduler.sharedScheduler().schedule("create", this, 1, false);
	}

	public void create(float t) {
		new Sun(this);
	}

}
