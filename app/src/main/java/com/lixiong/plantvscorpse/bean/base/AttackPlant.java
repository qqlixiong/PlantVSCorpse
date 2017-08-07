package com.lixiong.plantvscorpse.bean.base;

import com.lixiong.plantvscorpse.engine.DieListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ������ֲ��
 * 
 * @author Administrator
 * 
 */
public abstract class AttackPlant extends Plant implements DieListener {
	// 弹夹
	protected List<Bullet> bullets = new ArrayList<Bullet>();

	public AttackPlant(String filepath) {
		super(filepath);
	}

	/**
	 * �������ڹ������ӵ�
	 * 
	 * @return
	 */
	public abstract Bullet createBullet();

	public List<Bullet> getBullets() {
		return bullets;
	}

	@Override
	public void onDie(BaseElement element) {

		if (element instanceof Bullet) {
			bullets.remove(element);
		}
	}

}
