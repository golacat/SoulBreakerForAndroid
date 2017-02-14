package com.goldcat.soulbreaker;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

public class SplashLayer extends CCColorLayer {
	
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new SplashLayer(ccColor4B.ccc4(	255, 255, 255, 255));		
		scene.addChild(layer);
		scene.setUserData("SplashLayer");
		return scene;
	}
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	public SplashLayer(ccColor4B color) {
		super(color);
		
		CCBitmapFontAtlas splash_goldcat = CCBitmapFontAtlas.bitmapFontAtlas("GOLDCAT\n PRESENT", "madokaletters.fnt");//		
		splash_goldcat.setScaleX(scaleX);		splash_goldcat.setScaleY(scaleY);
		splash_goldcat.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		splash_goldcat.setColor(SB_Constants.TITLE_TEXT_COLOR);
		splash_goldcat.setOpacity(0);
		addChild(splash_goldcat);
		splash_goldcat.runAction(CCSequence.actions(
				CCFadeIn.action(0.5f),
				CCDelayTime.action(3.0f),
				CCFadeOut.action(0.5f),
				CCCallFuncN.action(this, "GoldCatEnd")
				));
	}
	
	public void GoldCatEnd(Object sender) {
		SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_NOEXIT));
	}
}
