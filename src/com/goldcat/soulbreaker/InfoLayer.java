package com.goldcat.soulbreaker;

import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;

public class InfoLayer extends CCColorLayer {
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new InfoLayer(ccColor4B.ccc4(	255, 255, 255, 255));
		scene.addChild(layer);
		scene.setUserData("InfoLayer");
		return scene;
	}
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	public InfoLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		
		CCBitmapFontAtlas info = CCBitmapFontAtlas.bitmapFontAtlas("Info", "madokaletters.fnt");
		info.setScaleX(scaleX);		info.setScaleY(scaleY);
		info.setColor(ccColor3B.ccBLACK);
		info.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(info);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_EXIT));
		return true;
	}
}
