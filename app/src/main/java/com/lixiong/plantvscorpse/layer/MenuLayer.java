package com.lixiong.plantvscorpse.layer;

import com.lixiong.plantvscorpse.util.CommonUtil;

import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemSprite;
import org.cocos2d.nodes.CCSprite;
/**
 * 菜单
 *
 * @author Administrator
 *
 */
public class MenuLayer extends BaseLayer {
	public MenuLayer() {
		init();
	}

	private void init() {
		CCSprite bg = CCSprite.sprite("image/menu/main_menu_bg.jpg");
		bg.setAnchorPoint(0, 0);
		this.addChild(bg);

		CCMenu menu = CCMenu.menu();
		CCMenuItemSprite menuItem = CCMenuItemSprite.item(
				CCSprite.sprite("image/menu/start_adventure_default.png"),
				CCSprite.sprite("image/menu/start_adventure_press.png"), this,
				"onClick");

		menu.addChild(menuItem);

		// menu的设置参数
		menu.setScale(0.5f);
		menu.setPosition(winSize.width / 2 - 25, winSize.height / 2 - 110);
		menu.setRotation(4.5f);

		this.addChild(menu);

	}

	public void onClick(Object o) {
		CommonUtil.changeLayer(new FightLayer());
	}
}
