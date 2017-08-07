package com.lixiong.plantvscorpse.bean.plant;

import com.lixiong.plantvscorpse.bean.base.DefancePlant;
import com.lixiong.plantvscorpse.util.CommonUtil;

import java.util.ArrayList;

import org.cocos2d.nodes.CCSpriteFrame;
/**
 * 坚果
 *
 * @author Administrator
 *
 */
public class PlantNut extends DefancePlant {
	private static ArrayList<CCSpriteFrame> shakeFrames;// 摇晃序列帧

	public PlantNut() {
		super("image/plant/nut/p_3_01.png");
		baseAction();
	}

	@Override
	public void baseAction() {
		this.runAction(CommonUtil.getRepeatForeverAnimate(shakeFrames, 11,
				"image/plant/nut/p_3_%02d.png"));
	}

}
