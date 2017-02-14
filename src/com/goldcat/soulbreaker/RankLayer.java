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

public class RankLayer extends CCColorLayer {
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new RankLayer(ccColor4B.ccc4(	255, 255, 255, 255));
		scene.addChild(layer);
		scene.setUserData("RankLayer");
		return scene;
	}
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	public RankLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
		
		CCBitmapFontAtlas rank = CCBitmapFontAtlas.bitmapFontAtlas("Rank", "madokaletters.fnt");
		rank.setScaleX(scaleX);		rank.setScaleY(scaleY);
		rank.setColor(ccColor3B.ccBLACK);
		rank.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(rank);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_EXIT));
		return true;
	}
}
