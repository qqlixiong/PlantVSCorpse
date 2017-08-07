package com.lixiong.plantvscorpse.bean.product;

import com.lixiong.plantvscorpse.bean.base.Plant;
import com.lixiong.plantvscorpse.bean.base.Product;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCBezierTo;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.types.CCBezierConfig;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sun extends Product {

	private Plant plant;
	public static List<Sun> SUNS = new CopyOnWriteArrayList<Sun>();
	public static int TOTLE_SUN = 50;

	public Sun(Plant plant) {
		super("image/product/sun.png");
		this.plant = plant;
		setScale(.4);
		if (plant != null) {
			float x = plant.getPosition().x;
			float y = plant.getPosition().y;
			setPosition(ccp(x, y + 35));
		}

		plant.getParent().addChild(this);
		move();
		SUNS.add(this);
	}

	@Override
	public void baseAction() {
		runAction(CCRepeatForever.action(CCRotateBy.action(1, 180)));
	}

	public void move() {
		if (plant != null) {
			float x = plant.getPosition().x;
			float y = plant.getPosition().y;
			CCBezierConfig c = new CCBezierConfig();
			c.controlPoint_1 = ccp(x, y + 35);
			c.controlPoint_2 = ccp(x + 20, y + 55);
			c.endPosition = ccp(x + 30, y);
			
			CCBezierTo bezier = CCBezierTo.action(0.5f, c);
			this.runAction(CCSequence.actions(bezier, CCDelayTime.action(3),
					CCCallFunc.action(this, "destroy")));
			this.runAction(CCRepeatForever.action(CCRotateBy.action(1, 15)));
		}
	}
	
	@Override
	public void destroy() {
		SUNS.remove(this);
		this.removeSelf();
	}
}
