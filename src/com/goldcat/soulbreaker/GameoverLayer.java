package com.goldcat.soulbreaker;

import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

public class GameoverLayer extends CCColorLayer {
	public static CCScene scene(int chr, int stage, int score) {
		CCScene scene = CCScene.node();
		CCLayer layer = new GameoverLayer(ccColor4B.ccc4(	255, 255, 255, 255), chr, stage, score);
		scene.addChild(layer);
		scene.setUserData("GameoverLayer");
		return scene;
	}
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	public GameoverLayer(ccColor4B color, int chr, int stage, int score) {
		super(color);
		this.setIsTouchEnabled(true);
		
		CCSprite cube = CCSprite.sprite("title/title_cube.png");
		cube.setScaleX(scaleX*0.0f);	cube.setScaleY(scaleY*0.0f);
		cube.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
//		cube.setOpacity(125);
		addChild(cube);
		
		cube.runAction(CCScaleTo.action(10.0f, 5.0f*scaleX, 5.0f*scaleY));
		
		
		CCBitmapFontAtlas gameover = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("Gameover\nscore: %d", score), "madokaletters.fnt");
		gameover.setScaleX(scaleX);		gameover.setScaleY(scaleY);
//		gameover.setColor(ccColor3B.ccBLACK);
		switch (chr) {
		case SB_Constants.CHRSEL_MADOKA: gameover.setColor(SB_Constants.CHR_COLOR_MADOKA); break;
		case SB_Constants.CHRSEL_HOMURA: gameover.setColor(SB_Constants.CHR_COLOR_HOMURA); break;
		case SB_Constants.CHRSEL_SAYAKA: gameover.setColor(SB_Constants.CHR_COLOR_SAYAKA); break;
		case SB_Constants.CHRSEL_MAMI: gameover.setColor(SB_Constants.CHR_COLOR_MAMI); break;
		case SB_Constants.CHRSEL_KYOKO: gameover.setColor(SB_Constants.CHR_COLOR_KYOKO); break;
		}
		gameover.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(gameover);
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_EXIT));
		return true;
	}
}
