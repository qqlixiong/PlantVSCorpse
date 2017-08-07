package com.lixiong.plantvscorpse.bean.base;
/**
 * �ӵ�
 */
public abstract class Bullet extends Product {
	protected int attack = 10;// 攻击力
	protected int speed = 60;// 移动速度

	public Bullet(String filepath) {
		super(filepath);
	}

	@Override
	public void baseAction() {

	}
	/**
	 * �ƶ�
	 */
	public abstract void move();

	public int getAttack() {
		return attack;
	}
}
