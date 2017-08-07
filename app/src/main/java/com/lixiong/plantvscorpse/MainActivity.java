package com.lixiong.plantvscorpse;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;

import com.lixiong.plantvscorpse.layer.FightLayer;

public class MainActivity extends Activity {
	private CCDirector director;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		director=CCDirector.sharedDirector();
		CCGLSurfaceView surfaceView=new CCGLSurfaceView(this);
		setContentView(surfaceView);
		
		director.attachInView(surfaceView);
		
		
		director.setDisplayFPS(true);
		director.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
		
		director.setScreenSize(480, 320);
		
		CCScene scene=CCScene.node();
		scene.addChild(new FightLayer());
		
		director.runWithScene(scene);
	}
	
	
	@Override
	protected void onResume() {
		director.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		director.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		director.end();
		super.onDestroy();
	}
	

}
