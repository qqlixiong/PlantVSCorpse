package com.lixiong.plantvscorpse.bean;

import com.lixiong.plantvscorpse.util.CommonUtil;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
/**
 * 展示用僵尸
 *
 * @author Administrator
 *
 */
public class ShowZombies extends CCSprite {
	private static ArrayList<CCSpriteFrame> shakeFrames;//摇晃的序列帧
	public ShowZombies() {
		super("image/zombies/zombies_1/shake/z_1_01.png");
		setScale(0.45);
		setAnchorPoint(0.5f, 0);
		shake();
	}
	private void shake() {
		CCAction repeatForeverAnimate = CommonUtil.getRepeatForeverAnimate(shakeFrames, 2, "image/zombies/zombies_1/shake/z_1_%02d.png");
		this.runAction(repeatForeverAnimate);
	}


}
