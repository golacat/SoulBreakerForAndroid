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

public class CreditLayer extends CCColorLayer {
	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new CreditLayer(ccColor4B.ccc4(255, 255, 255, 255));
		scene.addChild(layer);
		scene.setUserData("CreditLayer");
		return scene;
	}

	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	public CreditLayer(ccColor4B color) {
		super(color);
		this.setIsTouchEnabled(true);
//		CCLabel lbll = CCLabel.makeLabel("CreditLayer", "DroidSans", 24);
//		lbll.setColor(ccColor3B.ccBLACK);
//		lbll.setPosition(CGPoint.ccp(winSize.getWidth() / 2, winSize.getHeight() / 2 + 100));
//		addChild(lbll, 0);
		
		CCBitmapFontAtlas credit = CCBitmapFontAtlas.bitmapFontAtlas("jongchoel@gmail.com", "madokaletters.fnt");
		credit.setScaleX(scaleX*0.6f);		credit.setScaleY(scaleY*0.6f);
		credit.setColor(ccColor3B.ccBLACK);
		credit.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(credit);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_EXIT));
		return true;
	}
}
