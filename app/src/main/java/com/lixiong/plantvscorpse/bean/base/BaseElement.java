package com.lixiong.plantvscorpse.bean.base;

import com.lixiong.plantvscorpse.engine.DieListener;

import org.cocos2d.nodes.CCSprite;
/**
 * 对战元素共性
 *
 * @author Administrator
 *
 */
public abstract class BaseElement extends CCSprite {

	private DieListener dieListener;// 修改那个类身上的数据，让该类实现这个接口

	public void setDieListener(DieListener dieListener) {
		this.dieListener = dieListener;
	}

	public BaseElement(String filepath) {
		super(filepath);
	}

	/**
	 * 原地不动的基本动作
	 */
	public abstract void baseAction();

	/**
	 * 销毁
	 */
	public void destroy() {
		if (dieListener != null) {
			dieListener.onDie(this);
		}
		this.removeSelf();
	}
}
