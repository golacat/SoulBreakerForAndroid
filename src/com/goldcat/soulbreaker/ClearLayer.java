package com.goldcat.soulbreaker;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCSequence;
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ClearLayer extends CCColorLayer {
	public static CCScene scene(int chr, int stage, int score, int delServant, float soul, int combo) {
		CCScene scene = CCScene.node();
		CCLayer layer = new ClearLayer(ccColor4B.ccc4(255, 255, 255, 255), chr, stage, score, delServant, soul, combo);
		scene.addChild(layer);
		scene.setUserData("ClearLayer");
		return scene;
	}

	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	int touchable = 0;
	
	int score = 0;
	int selChr = 0;
	int selStage = 0;
	int deleteServant = 0;
	float soul = 0.0f;
	int maxCombo = 0;

	ccColor3B fontColor = ccColor3B.ccBLACK;
	CCBitmapFontAtlas bitmapBonus;
	CCBitmapFontAtlas bitmapScore;
	CCBitmapFontAtlas next;
	
	public ClearLayer(ccColor4B color, int chr, int stage, int newScore, int delServant, float remainsoul, int combo) {
		super(color);
		this.setIsTouchEnabled(true);
		
		
		score = newScore;
		selChr = chr;
		selStage = stage;
		deleteServant = delServant;
		soul = 100.0f - remainsoul;
		maxCombo = combo;

		CCSprite background;
		switch (selStage) {
		case 1: background = CCSprite.sprite("play/background/play_stage1_background.png");	break;
		case 2: background = CCSprite.sprite("play/background/play_stage2_background.png");	break;
		default: background = CCSprite.sprite("play/background/play_stage1_background.png");	break;
		}
		background.setScaleX(scaleX);		background.setScaleY(scaleY);
		background.setOpacity(125);
		background.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(background);

		CCSprite ui;
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:
			ui = CCSprite.sprite("clear/clear_madoka.png");
			fontColor = SB_Constants.CHR_COLOR_MADOKA;
			break;
		case SB_Constants.CHRSEL_HOMURA:
			ui = CCSprite.sprite("clear/clear_homura.png");
			fontColor = SB_Constants.CHR_COLOR_HOMURA;
			break;
		case SB_Constants.CHRSEL_SAYAKA:	
			ui = CCSprite.sprite("clear/clear_sayaka.png");
			fontColor = SB_Constants.CHR_COLOR_SAYAKA;
			break;
		case SB_Constants.CHRSEL_MAMI:
			ui = CCSprite.sprite("clear/clear_mami.png");
			fontColor = SB_Constants.CHR_COLOR_MAMI;
			break;
		case SB_Constants.CHRSEL_KYOKO:
			ui = CCSprite.sprite("clear/clear_kyoko.png");
			fontColor = SB_Constants.CHR_COLOR_KYOKO;
			break;
		default:	ui = CCSprite.sprite("clear/clear_madoka.png");		break;
		}
		ui.setScaleX(scaleX);	ui.setScaleY(scaleY);
		ui.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(ui);
		
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("Stage %d Clear", selStage), "madokaletters.fnt");
		stageclear.setScaleX(scaleX);		stageclear.setScaleY(scaleY);
		stageclear.setColor(fontColor);
		stageclear.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/1.5f);
		addChild(stageclear);
		
		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNInitEnd")
				));
	}
	
	public void CallFunNInitEnd(Object sender) {		
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("DEFEATED FAMILIAR %d", deleteServant), "madokaletters.fnt");
		stageclear.setScaleX(scaleX*0.5f);		stageclear.setScaleY(scaleY*0.5f);
		stageclear.setColor(fontColor);
		stageclear.setAnchorPoint(1, 0);
		stageclear.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/1.7f);
		addChild(stageclear);

		this.runAction(CCSequence.actions(
				CCDelayTime.action(0.5f),
				CCCallFuncN.action(this, "CallFunNServantEnd")
				));
	}

	public void CallFunNServantEnd(Object sender) {
		Log.d("SB", "CallFunNServantEnd");
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("REMAINED SOUL %d", (int)soul), "madokaletters.fnt");
		stageclear.setScaleX(scaleX*0.5f);		stageclear.setScaleY(scaleY*0.5f);
		stageclear.setColor(fontColor);
		stageclear.setAnchorPoint(1, 0);
		stageclear.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/1.8f);
		addChild(stageclear);

		this.runAction(CCSequence.actions(
				CCDelayTime.action(0.5f),
				CCCallFuncN.action(this, "CallFunNSoulEnd")
				));
	}
	
	public void CallFunNSoulEnd(Object sender) {
		Log.d("SB", "CallFunNServantEnd");
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("MAX COMBO %d", maxCombo), "madokaletters.fnt");
		stageclear.setScaleX(scaleX*0.5f);		stageclear.setScaleY(scaleY*0.5f);
		stageclear.setColor(fontColor);
		stageclear.setAnchorPoint(1, 0);
		stageclear.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/1.9f);
		addChild(stageclear);

		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNComboEnd")
				));
	}

	public void CallFunNComboEnd(Object sender) {
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("BONUS SCORE"), "madokaletters.fnt");
		stageclear.setScaleX(scaleX*0.8f);		stageclear.setScaleY(scaleY*0.8f);
		stageclear.setColor(fontColor);
		stageclear.setAnchorPoint(1, 0);
		stageclear.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/2.2f);
		addChild(stageclear);
		
		bitmapBonus = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format(""), "madokaletters.fnt");
		bitmapBonus.setScaleX(scaleX*0.8f);		bitmapBonus.setScaleY(scaleY*0.8f);
		bitmapBonus.setColor(fontColor);
		bitmapBonus.setAnchorPoint(1, 0);
		bitmapBonus.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/2.5f);
		addChild(bitmapBonus);		

		this.runAction(CCSequence.actions(
				CCDelayTime.action(0.5f),
				CCCallFuncN.action(this, "CallFunNBonusInitEnd")
				));
	}
	
	public void CallFunNBonusInitEnd(Object sender) {
		bitmapBonus.setString(CCFormatter.format("%d", deleteServant));

		this.runAction(CCSequence.actions(
				CCDelayTime.action(0.5f),
				CCCallFuncN.action(this, "CallFunNBonusServantEnd")
				));		
	}
	
	public void CallFunNBonusServantEnd(Object sender) {
		bitmapBonus.setString(CCFormatter.format("%d", deleteServant*(int)soul));

		this.runAction(CCSequence.actions(
				CCDelayTime.action(0.5f),
				CCCallFuncN.action(this, "CallFunNBonusSoulEnd")
				));
	}

	public void CallFunNBonusSoulEnd(Object sender) {
		bitmapBonus.setString(CCFormatter.format("%d", deleteServant*(int)soul*maxCombo));		

		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNBonusComboEnd")
				));
	}
	
	public void CallFunNBonusComboEnd(Object sender) {
		CCBitmapFontAtlas stageclear = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("TOTAL SCORE"), "madokaletters.fnt");
		stageclear.setScaleX(scaleX*0.9f);		stageclear.setScaleY(scaleY*0.9f);
		stageclear.setColor(fontColor);
		stageclear.setAnchorPoint(1, 0);
		stageclear.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/3.0f);
		addChild(stageclear);
		
		bitmapScore = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("%d", score), "madokaletters.fnt");
		bitmapScore.setScaleX(scaleX*0.9f);		bitmapScore.setScaleY(scaleY*0.9f);
		bitmapScore.setColor(fontColor);
		bitmapScore.setAnchorPoint(1, 0);
		bitmapScore.setPosition(winSize.getWidth()/1.1f, winSize.getHeight()/3.6f);
		addChild(bitmapScore);

		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNScoreInitEnd")
				));
	}
	
	public void CallFunNScoreInitEnd(Object sender) {
		bitmapBonus.setString("0");
		bitmapScore.setString(CCFormatter.format("%d", score+deleteServant*(int)soul*maxCombo));
		score = score+deleteServant*(int)soul*maxCombo;
		
		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNSumScoreEnd")
				));
		
	}
	
	public void CallFunNSumScoreEnd(Object sender) {		
		next = CCBitmapFontAtlas.bitmapFontAtlas(CCFormatter.format("TO NEXT STAGE"), "madokaletters.fnt");
		next.setScaleX(scaleX);		next.setScaleY(scaleY);
		next.setColor(fontColor);
		next.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/7.0f);
		addChild(next);		

		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFunNNextStageEnd")
				));		
	}
	
	public void CallFunNNextStageEnd(Object sender) {
		next.runAction(CCRepeatForever.action(CCBlink.action(1.0f, 1)));
		touchable = 1;		
	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		if (touchable==1) {
			if (selStage<2) {
				SB_Util.ReplaceScene(StageIntroLayer.scene(selChr, selStage+1, score));
			} else {
				SB_Util.ReplaceScene(EndingLayer.scene());
				
				// FaceBook 스샷 올리기 테스트.
//				Message msg = Message.obtain();
//				msg.what = SB_Constants.MSG_FACEBOOKFEED;
//				msg.obj = SB_Util.getScreenCapture();
//
//				Activity activity = CCDirector.sharedDirector().getActivity();
//				SoulBreakerActivity sbAactivity = (SoulBreakerActivity)activity;
//				sbAactivity.GetHandler().sendMessage(msg);				
			}
		}
		return true;
	}
}
