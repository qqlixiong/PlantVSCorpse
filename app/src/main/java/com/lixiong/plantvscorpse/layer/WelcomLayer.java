package com.lixiong.plantvscorpse.layer;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.lixiong.plantvscorpse.util.CommonUtil;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCHide;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.transitions.CCFadeTransition;

import java.util.ArrayList;

/**
 * 欢迎界面
 *
 * @author Administrator
 *
 */
public class WelcomLayer extends BaseLayer {
	private static final String TAG = "WelcomLayer";
	private static final int TAG_START = 10;
	private CCSprite loading;

	// 展示logo，停顿一会，隐藏logo，停顿一会
	// 背景图片加载，进度条处理
	//
	// 耗时操作：访问网络，版本检测，预加载图片，预加载声音文件
	public WelcomLayer() {
		consumeTime();
		init();
	}

	private void consumeTime() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// 耗时操作：访问网络，版本检测，预加载图片，预加载声音文件
				SystemClock.sleep(5000);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				setIsTouchEnabled(true);
				loading.removeSelf();

				WelcomLayer.this.getChildByTag(TAG_START).setVisible(true);
				super.onPostExecute(result);
			}

		}.execute();

	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if (CommonUtil.isClicke(event, this, this.getChildByTag(TAG_START))) {
			// 淡入淡出的切换——切换场景
			CCScene scene = CCScene.node();
			scene.addChild(new MenuLayer());

			// CCTransitionScene

			CCFadeTransition transition = CCFadeTransition.transition(1, scene);

			CCDirector.sharedDirector().replaceScene(transition);

		}
		return super.ccTouchesEnded(event);
	}

	private void init() {
		logo();

	}

	private void logo() {
		// 展示logo，停顿一会，隐藏logo，停顿一会
		CCSprite logo = CCSprite.sprite("image/popcap_logo.png");
		logo.setPosition(winSize.width / 2, winSize.height / 2);

		this.addChild(logo);

		CCSequence sequence = CCSequence.actions(CCDelayTime.action(1),
				CCHide.action(), CCDelayTime.action(0.5f),
				CCCallFunc.action(this, "loadInfo"));

		logo.runAction(sequence);

	}

	public void loadInfo() {
		// 背景图片加载，进度条处理
		// Log.i(TAG, "loadInfo...");

		CCSprite bg = CCSprite.sprite("image/welcome.jpg");
		bg.setAnchorPoint(0, 0);
		this.addChild(bg);

		// 进度条
		loading = CCSprite.sprite("image/loading/loading_01.png");
		loading.setPosition(winSize.width / 2, 25);
		this.addChild(loading);

		ArrayList<CCSpriteFrame> frames = new ArrayList<CCSpriteFrame>();
		String fileName = "image/loading/loading_%02d.png";

		for (int num = 1; num <= 9; num++) {
			frames.add(CCSprite.sprite(String.format(fileName, num))
					.displayedFrame());
		}

		CCAnimation animation = CCAnimation.animation("", 0.2f, frames);
		CCAnimate animate = CCAnimate.action(animation, false);// 参数二：永不停止的序列帧播放（true），如果只想播放一次（false）

		loading.runAction(animate);// 运行一次

		CCSprite startGame = CCSprite.sprite("image/loading/loading_start.png");
		startGame.setPosition(winSize.width / 2, 25);
		startGame.setVisible(false);
		this.addChild(startGame, 0, TAG_START);
	}
}
