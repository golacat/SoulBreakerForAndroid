package com.goldcat.soulbreaker;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import android.view.MotionEvent;

public class StageIntroLayer extends CCColorLayer {

	public static CCScene scene(int chr, int stage, int score) {
		CCScene scene = CCScene.node();
		CCLayer layer = new StageIntroLayer(ccColor4B.ccc4(	255, 255, 255, 255), chr, stage, score);
		scene.addChild(layer);
		scene.setUserData("StageIntroLayer");
		return scene;
	}
	
	int score = 0;
	int selChr = 0;
	int selStage = 0;
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	CCBitmapFontAtlas intro;
	CCSprite stageintro;
	
	public StageIntroLayer(ccColor4B color, int chr, int stage, int newScore) {
		super(color);
		this.setIsTouchEnabled(true);
		
		score = newScore;
		selChr = chr;
		selStage = stage;

		CCSprite background;
		
		switch (selStage) {
		case 1: background = CCSprite.sprite("play/background/play_stage1_background.png");	break;
		case 2: background = CCSprite.sprite("play/background/play_stage2_background.png");	break;
		default: background = CCSprite.sprite("play/background/play_stage1_background.png");	break;
		}
		background.setScaleX(scaleX);		background.setScaleY(scaleY);
//		background.setOpacity(125);
		background.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(background);
		
		intro = CCBitmapFontAtlas.bitmapFontAtlas("Stage "+String.valueOf(selStage), "madokalettersoutline.fnt");
		intro.setScaleX(0.0f);		intro.setScaleY(0.0f);
		intro.setColor(ccColor3B.ccWHITE);
		intro.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/1.2f);
		addChild(intro);
		
		switch (selStage) {
		case 1: stageintro = CCSprite.sprite("stageintro/stageintro_1.png"); break;
		case 2: stageintro = CCSprite.sprite("stageintro/stageintro_2.png"); break;
		default: stageintro = CCSprite.sprite("stageintro/stageintro_1.png"); break;
		}
//		stageintro.setScaleX(scaleX);		stageintro.setScaleY(scaleY);
		stageintro.setScaleX(0.0f);		stageintro.setScaleY(0.0f);
		stageintro.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(stageintro);
		
		intro.runAction(CCScaleTo.action(1.5f, scaleX, scaleY));		
		stageintro.runAction(CCSequence.actions(
					CCSpawn.actions(
						CCScaleTo.action(1.5f, scaleX, scaleY),
						CCRotateTo.action(1.5f, 2880.0f)),
						CCDelayTime.action(1.0f),
						CCCallFuncN.action(this, "CallFuncNIntroEnd")
				));
	}

//	@Override
//	public boolean ccTouchesBegan(MotionEvent event) {
//		intro.setScaleX(scaleX);		intro.setScaleY(scaleY);
//		stageintro.setScaleX(scaleX);	stageintro.setScaleY(scaleY);
//		stageintro.setRotation(0.0f);
//		this.runAction(CCSequence.actions(
//				CCDelayTime.action(1.0f),
//				CCCallFuncN.action(this, "CallFuncNIntroEnd")
//				));
//		return true;
//	}
	
	public void CallFuncNIntroEnd(Object sender) {
		SB_Util.ReplaceScene(PlayLayer.scene(selChr, selStage, score));
	}
}
