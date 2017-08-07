package com.lixiong.plantvscorpse.bean.plant;

import com.lixiong.plantvscorpse.bean.base.AttackPlant;
import com.lixiong.plantvscorpse.bean.base.Bullet;
import com.lixiong.plantvscorpse.bean.product.CommonPease;
import com.lixiong.plantvscorpse.util.CommonUtil;

import java.util.ArrayList;

import org.cocos2d.nodes.CCSpriteFrame;

/**
 * 攻击植物：豌豆射手
 *
 * @author Administrator
 *
 */
public class PlantPease extends AttackPlant {
	private static ArrayList<CCSpriteFrame> shakeFrames;// 摇晃

	public PlantPease() {
		super("image/plant/pease/p_2_01.png");
		baseAction();
	}

	@Override
	public Bullet createBullet() {
		if (bullets.size() < 1) {
			CommonPease pease = new CommonPease();
			pease.setPosition(ccp(getPosition().x + 20, getPosition().y + 35));

			pease.setDieListener(this);

			bullets.add(pease);

			this.getParent().addChild(pease);

			pease.move();
		}

		return null;

	}

	@Override
	public void baseAction() {
		this.runAction(CommonUtil.getRepeatForeverAnimate(shakeFrames, 8,
				"image/plant/pease/p_2_%02d.png"));

	}

}
