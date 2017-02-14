package com.goldcat.soulbreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.cocos2d.actions.CCActionManager;
import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseOut;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;
import org.cocos2d.utils.CCFormatter;

import android.view.MotionEvent;

public class PlayLayer extends CCColorLayer {
	public static CCScene scene(int chr, int stage, int score) {
		CCScene scene = CCScene.node();
		CCLayer layer = new PlayLayer(ccColor4B.ccc4(255, 255, 255, 255), chr, stage, score);
		scene.addChild(layer);
		scene.setUserData("PlayLayer");
		return scene;
	}
	
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	CCSprite[] arrSoulGem = new CCSprite[128];
	CCSprite[] arrDel = new CCSprite[64];
	Random rand = new Random();
		
	int touchable = 0;
	
	int selectedSoulGem = -1;		// First Select;
	int selectedSoulGem2 = -1;		// Second Select;
	
	int SyncSoulGemMoveEnd = 0;
	int SyncSoulGemDeleteEnd = 0;
	int SyncSoulGemDownEnd = 0;
	
	CCBitmapFontAtlas bitmapScore;
	CCBitmapFontAtlas bitmapCombo;
	CCBitmapFontAtlas bitmapSkillCharged;
	CCSprite playerInfoSoulgem;
	CCSprite playerInfoSoulgemBlackTex;
	CCSprite playerInfoSoulgemBlack;
	ccColor3B playerInfoSoulgemColor;
	
	CCSprite witch;
	CCSprite witchHpBar;
	float witchHp = 100.0f;

	int servantNum=0;
	int deleteServant=0;
	
	int selChr = 0;
	float soul = 0;
	float skill = 0;
	
	int score = 0;
	int combo = 0;
	int maxCombo = 1;
	
	int selStage = 0;
	
	CCSprite skillCutBackground;
	CCSprite skillCut;
	CCAnimation skillAni;
	
	int timeStopped = 0;
	
	CCLabel debugLabel;
	
	public PlayLayer(ccColor4B color, int chr, int stage, int newScore) {
		super(color);
		setIsTouchEnabled(true);
		
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
		background.setOpacity(125);
		background.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		addChild(background);
			
		for (int j=8; j<16; j++) {
			for (int i=0; i<8; i++) {
				CCSprite soulgem = CreateNewSoulGem(rand);
				soulgem.setPosition(GetRealPositionFromIndex(i, j, soulgem.getContentSize()));
				soulgem.setTag(MakeTag(i, j));
				arrSoulGem[soulgem.getTag()] = soulgem;
				addChild(soulgem);
			}
		}
		
		// player ui
		CCSprite playerinfo;
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:	playerinfo = CCSprite.sprite("play/player_info/play_info_madoka.png");		break;
		case SB_Constants.CHRSEL_HOMURA:	playerinfo = CCSprite.sprite("play/player_info/play_info_homura.png");		break;
		case SB_Constants.CHRSEL_SAYAKA:		playerinfo = CCSprite.sprite("play/player_info/play_info_sayaka.png");		break;
		case SB_Constants.CHRSEL_MAMI:		playerinfo = CCSprite.sprite("play/player_info/play_info_mami.png");		break;
		case SB_Constants.CHRSEL_KYOKO:		playerinfo = CCSprite.sprite("play/player_info/play_info_kyoko.png");		break;
		default:	playerinfo = CCSprite.sprite("play/player_info/play_info_madoka.png");		break;
		}
		playerinfo.setScaleX(scaleX);	playerinfo.setScaleY(scaleY);
		playerinfo.setPosition(winSize.getWidth()/2.0f, playerinfo.getContentSize().getHeight()*scaleY/2.0f);
		addChild(playerinfo);
				
		bitmapScore = CCBitmapFontAtlas.bitmapFontAtlas(String.valueOf(score), "madokaletters.fnt");
		bitmapScore.setScaleX(scaleX*0.5f);		bitmapScore.setScaleY(scaleY*0.5f);
		bitmapScore.setColor(ccColor3B.ccBLACK);
		bitmapScore.setAnchorPoint(0,0);
		bitmapScore.setPosition(350*scaleX, 110*scaleY);
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA: bitmapScore.setPosition(220*scaleX, 110*scaleY); break;
		case SB_Constants.CHRSEL_HOMURA: bitmapScore.setPosition(170*scaleX, 90*scaleY); break;
		case SB_Constants.CHRSEL_SAYAKA: bitmapScore.setPosition(180*scaleX, 105*scaleY); break;
		case SB_Constants.CHRSEL_MAMI: bitmapScore.setPosition(170*scaleX, 60*scaleY); break;
		case SB_Constants.CHRSEL_KYOKO: bitmapScore.setPosition(180*scaleX, 100*scaleY); break;
		}		
		addChild(bitmapScore);
		
		bitmapCombo = CCBitmapFontAtlas.bitmapFontAtlas(String.valueOf(combo), "madokalettersoutline.fnt");
		bitmapCombo.setScaleX(scaleX*0.8f);		bitmapCombo.setScaleY(scaleY*0.8f);
		bitmapCombo.setPosition(280*scaleX, 50*scaleY);
		bitmapCombo.setOpacity(0);		
		addChild(bitmapCombo, 2);
		
		bitmapSkillCharged = CCBitmapFontAtlas.bitmapFontAtlas("Skill\nReady", "madokaletters.fnt");
		bitmapSkillCharged.setScaleX(scaleX*0.5f);		bitmapSkillCharged.setScaleY(scaleY*0.5f);
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA: bitmapSkillCharged.setPosition(430*scaleX, 90*scaleY);	break;
		case SB_Constants.CHRSEL_HOMURA: bitmapSkillCharged.setPosition(412*scaleX, 110*scaleY);	break;
		case SB_Constants.CHRSEL_SAYAKA: bitmapSkillCharged.setPosition(382*scaleX, 78*scaleY);	break;
		case SB_Constants.CHRSEL_MAMI: bitmapSkillCharged.setPosition(387*scaleX, 85*scaleY);	break;
		case SB_Constants.CHRSEL_KYOKO: bitmapSkillCharged.setPosition(430*scaleX, 100*scaleY);	break;
		}
		bitmapSkillCharged.setOpacity(0);
		addChild(bitmapSkillCharged, 2);
		
		CCSprite player;
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA: player = CCSprite.sprite("play/player_info/play_madoka.png");	break;
		case SB_Constants.CHRSEL_HOMURA: player = CCSprite.sprite("play/player_info/play_homura.png");	break;
		case SB_Constants.CHRSEL_SAYAKA: player = CCSprite.sprite("play/player_info/play_sayaka.png");	break;
		case SB_Constants.CHRSEL_MAMI: player = CCSprite.sprite("play/player_info/play_mami.png");		break;
		case SB_Constants.CHRSEL_KYOKO: player = CCSprite.sprite("play/player_info/play_kyoko.png");		break;
		default: player = CCSprite.sprite("play/play_madoka.png");	break;
		}
		player.setScaleX(scaleX);		player.setScaleY(scaleY);
		player.setPosition(85*scaleX, player.getContentSize().getHeight()*scaleY/2.0f);
		addChild(player, 1);
		
		CCSprite sprite;
		skillAni = CCAnimation.animation("skillAni", 0.1f);
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:
			playerInfoSoulgem = CCSprite.sprite("play/player_info/play_info_soulgem_madoka.png");
			playerInfoSoulgem.setPosition(430*scaleX, 95*scaleY);
			playerInfoSoulgemBlackTex = CCSprite.sprite("play/player_info/play_info_soulgem_madoka_black.png");
			playerInfoSoulgemBlack = CCSprite.sprite(playerInfoSoulgemBlackTex.getTexture(), CGRect.make(0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f));
			playerInfoSoulgemBlack.setAnchorPoint(1, 0);
			playerInfoSoulgemBlack.setPosition(457*scaleX, 50*scaleY);
			for (int i=0; i<9; i++) {
				sprite = CCSprite.sprite(CCFormatter.format("play/skill/madoka/play_madoka_skillcut_%d.jpg", i+1));
				skillAni.addFrame(sprite.getTexture());
			}
			break;
		case SB_Constants.CHRSEL_HOMURA:
			playerInfoSoulgem = CCSprite.sprite("play/player_info/play_info_soulgem_homura.png");
			playerInfoSoulgem.setPosition(415*scaleX, 95*scaleY);
			playerInfoSoulgemBlackTex = CCSprite.sprite("play/player_info/play_info_soulgem_homura_black.png");
			playerInfoSoulgemBlack = CCSprite.sprite(playerInfoSoulgemBlackTex.getTexture(), CGRect.make(0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f));
			playerInfoSoulgemBlack.setAnchorPoint(1, 0);
			playerInfoSoulgemBlack.setPosition(454*scaleX, 50*scaleY);
			for (int i=0; i<9; i++) {
				sprite = CCSprite.sprite(CCFormatter.format("play/skill/homura/play_homura_skillcut_%d.jpg", i+1));
				skillAni.addFrame(sprite.getTexture());
			}
			break;
		case SB_Constants.CHRSEL_SAYAKA:
			playerInfoSoulgem = CCSprite.sprite("play/player_info/play_info_soulgem_sayaka.png");
			playerInfoSoulgem.setPosition(380*scaleX, 70*scaleY);
			playerInfoSoulgemBlackTex = CCSprite.sprite("play/player_info/play_info_soulgem_sayaka_black.png");
			playerInfoSoulgemBlack = CCSprite.sprite(playerInfoSoulgemBlackTex.getTexture(), CGRect.make(0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f));
			playerInfoSoulgemBlack.setAnchorPoint(1, 0);
			playerInfoSoulgemBlack.setPosition(465*scaleX, 19*scaleY);
			for (int i=0; i<9; i++) {
				sprite = CCSprite.sprite(CCFormatter.format("play/skill/sayaka/play_sayaka_skillcut_%d.jpg", i+1));
				skillAni.addFrame(sprite.getTexture());
			}
			break;
		case SB_Constants.CHRSEL_MAMI:
			playerInfoSoulgem = CCSprite.sprite("play/player_info/play_info_soulgem_mami.png");
			playerInfoSoulgem.setPosition(385*scaleX, 95*scaleY);
			playerInfoSoulgemBlackTex = CCSprite.sprite("play/player_info/play_info_soulgem_mami_black.png");
			playerInfoSoulgemBlack = CCSprite.sprite(playerInfoSoulgemBlackTex.getTexture(), CGRect.make(0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f));
			playerInfoSoulgemBlack.setAnchorPoint(1, 0);
			playerInfoSoulgemBlack.setPosition(405*scaleX, 74*scaleY);
			for (int i=0; i<9; i++) {
				sprite = CCSprite.sprite(CCFormatter.format("play/skill/mami/play_mami_skillcut_%d.jpg", i+1));
				skillAni.addFrame(sprite.getTexture());
			}
			break;
		case SB_Constants.CHRSEL_KYOKO:
			playerInfoSoulgem = CCSprite.sprite("play/player_info/play_info_soulgem_kyoko.png");
			playerInfoSoulgem.setPosition(430*scaleX, 100*scaleY);
			playerInfoSoulgemBlackTex = CCSprite.sprite("play/player_info/play_info_soulgem_kyoko_black.png");
			playerInfoSoulgemBlack = CCSprite.sprite(playerInfoSoulgemBlackTex.getTexture(), CGRect.make(0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f));
			playerInfoSoulgemBlack.setAnchorPoint(1, 0);
			playerInfoSoulgemBlack.setPosition(461*scaleX, 17*scaleY);
			for (int i=0; i<9; i++) {
				sprite = CCSprite.sprite(CCFormatter.format("play/skill/kyoko/play_kyoko_skillcut_%d.jpg", i+1));
				skillAni.addFrame(sprite.getTexture());
			}
			break;
		default:
			playerInfoSoulgem = CCSprite.sprite("play/play_info_soulgem_madoka.png");
			playerInfoSoulgem.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
			break;
		}
		playerInfoSoulgem.setScaleX(scaleX*0.5f);		playerInfoSoulgem.setScaleY(scaleY*0.5f);
		playerInfoSoulgemColor = playerInfoSoulgem.getColor();
		addChild(playerInfoSoulgem, 1);

		playerInfoSoulgemBlack.setOpacity(125);
		playerInfoSoulgemBlack.setScaleX(scaleX*0.5f);		playerInfoSoulgemBlack.setScaleY(scaleY*0.5f);		
		addChild(playerInfoSoulgemBlack, 1);
		
		sprite = null;
		
		// witch ui
		CCSprite witchinfo = CCSprite.sprite("play/witch_info/play_witch_info.png");
		witchinfo.setScaleX(scaleX);		witchinfo.setScaleY(scaleY);
		witchinfo.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()-witchinfo.getContentSize().getHeight()*scaleY/2.0f);
		addChild(witchinfo, 1);
		
		CCBitmapFontAtlas witchName = CCBitmapFontAtlas.bitmapFontAtlas(GetWitchName(selStage), "madokarunes.fnt");
		witchName.setScaleX(scaleX*0.5f);		witchName.setScaleY(scaleY*0.5f);
		witchName.setColor(ccColor3B.ccBLACK);
		witchName.setAnchorPoint(0, 0);
		witchName.setPosition(40*scaleX, winSize.getHeight()-63*scaleY);
		addChild(witchName, 1);		

		CCBitmapFontAtlas bitmapWitchHp = CCBitmapFontAtlas.bitmapFontAtlas("HP", "madokarunes.fnt");
		bitmapWitchHp.setScaleX(scaleX*0.66f);		bitmapWitchHp.setScaleY(scaleY*0.66f);
		bitmapWitchHp.setColor(ccColor3B.ccBLACK);
		bitmapWitchHp.setAnchorPoint(0, 0);
		bitmapWitchHp.setPosition(40*scaleX, winSize.getHeight()-113*scaleY);
		addChild(bitmapWitchHp, 1);

		CCSprite witchHpBarSub = CCSprite.sprite("play/play_bar.png");
		witchHpBarSub.setScaleX(scaleX*200.0f*witchHp/100.0f);		witchHpBarSub.setScaleY(scaleY*30.0f);
		witchHpBarSub.setAnchorPoint(0,0);
		witchHpBarSub.setColor(ccColor3B.ccRED);
		witchHpBarSub.setPosition(130*scaleX, winSize.getHeight()-105*scaleY);
		addChild(witchHpBarSub, 1);
		
		witchHpBar = CCSprite.sprite("play/play_bar.png");
		witchHpBar.setScaleX(scaleX*200.0f*witchHp/100.0f);		witchHpBar.setScaleY(scaleY*30.0f);
		witchHpBar.setAnchorPoint(0,0);
		witchHpBar.setColor(ccColor3B.ccYELLOW);
		witchHpBar.setPosition(130*scaleX, winSize.getHeight()-105*scaleY);
		addChild(witchHpBar, 1);
		
		switch (selStage) {
		case 1: witch = CCSprite.sprite("play/witch_info/play_witch_1.png"); break;
		case 2: witch = CCSprite.sprite("play/witch_info/play_witch_2.png"); break;
		default: witch = CCSprite.sprite("play/witch_info/play_witch_1.png"); break;
		}
		witch.setScaleX(scaleX*130.0f/200.0f);		witch.setScaleY(scaleY*130.0f/200.0f);
		witch.setPosition(390.0f*scaleX, winSize.getHeight()-90*scaleY);
		addChild(witch, 1);

//		debugLabel = CCLabel.makeLabel(CCFormatter.format("servantNum: %d", servantNum), "DroidSans", 30);
//		debugLabel.setScaleX(scaleX);	debugLabel.setScaleY(scaleY);
//		debugLabel.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
//		debugLabel.setColor(ccColor3B.ccBLACK);
//		addChild(debugLabel, 1);
		
		// 초기 자리 잡고 삭제 처리 시작
		this.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFuncNInitEnd")));
		
		// 일정 시간 마다 마녀가 공격
		schedule("ScheduleWitchAttack", 2);
//		schedule("Debug", 0.1f);
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		if (touchable==1) {
			if (skill==100 && isSpriteTouched(event.getX(), event.getY(), playerInfoSoulgem)) {
				// 스킬 사용
				UseSkill(selChr);
			} else {
				SoulGemTouchBegan(event);
			}
		}
		return true;
	}
	
	public void CallFuncNSkillCutEnd(Object sender) {
		SetWitchHp(witchHp-20.0f);
		this.removeChild(skillCutBackground, true);	
		this.removeChild(skillCut, true);
		this.resumeSchedulerAndActions();
		for (int i=0; i<arrSoulGem.length; i++) {
			if (arrSoulGem[i]!=null)
				arrSoulGem[i].resumeSchedulerAndActions();
		}
//		touchable = 1;
		
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:	UseSkillMadoka();	break;
		case SB_Constants.CHRSEL_HOMURA:	UseSkillHomura();	break;
		case SB_Constants.CHRSEL_SAYAKA:		UseSkillSayaka();		break;
		case SB_Constants.CHRSEL_MAMI:		UseSkillMami();		break;
		case SB_Constants.CHRSEL_KYOKO:		UseSkillKyoko();		break;
		default: break;
		}
	}
	
//	@Override 
//	public boolean ccTouchesMoved(MotionEvent event) {
//		return true;
//	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if (touchable==1) {
			CCSprite soulgem2 = getTouchedSoulGem(event.getX(), event.getY());
			if (selectedSoulGem!=-1&&soulgem2!=null) {
				CCSprite soulgem1 = arrSoulGem[selectedSoulGem];
				if (soulgem1!=soulgem2) {
					// 거리가 1인 경우 판단
					int tag1 = soulgem1.getTag();
					int tag2 = soulgem2.getTag();
					if (Math.abs(tag1-tag2)==1
							|| Math.abs(tag1-tag2)==8) {
						// 바꾼다.
						selectedSoulGem2 = soulgem2.getTag();
//						Log.d("SB", "touchable = 0 ended");
						touchable = 0;
						soulgem1.runAction(CCSequence.actions(
								CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, scaleX),
								CCMoveTo.action(SB_Constants.PLAY_MOVE_VEL, soulgem2.getPosition()),
								CCCallFuncN.action(this, "CallFuncNSoulGemMoveEnd")));
						SyncSoulGemMoveEnd++;
						soulgem2.runAction(CCSequence.actions(
								CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, scaleX),
								CCMoveTo.action(SB_Constants.PLAY_MOVE_VEL, soulgem1.getPosition()),
								CCCallFuncN.action(this, "CallFuncNSoulGemMoveEnd")));
						SyncSoulGemMoveEnd++;
									
						soulgem1.setTag(selectedSoulGem2);
						soulgem2.setTag(selectedSoulGem);
						arrSoulGem[selectedSoulGem] = soulgem2;
						arrSoulGem[selectedSoulGem2] = soulgem1;
						selectedSoulGem = -1;
						selectedSoulGem2 = -1;
						SetSoul(soul+0.1f);
					}
				}
			}
		}
		return true;
	}
	
	protected void UseSkill(int chr) {
		skill = 0.0f;
		bitmapSkillCharged.setOpacity(0);
//		SetWitchHp(witchHp-20.0f);
		this.pauseSchedulerAndActions();
		for (int i=0; i<arrSoulGem.length; i++) {
			if (arrSoulGem[i]!=null)
				arrSoulGem[i].pauseSchedulerAndActions();
		}
		
		skillCutBackground = CCSprite.sprite("play/play_bar.png");
		skillCutBackground.setScaleX(winSize.getWidth());		skillCutBackground.setScaleY(winSize.getHeight());
		skillCutBackground.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		skillCutBackground.setColor(ccColor3B.ccBLACK);
		skillCutBackground.setOpacity(125);
		addChild(skillCutBackground, 2);
		
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:	skillCut = CCSprite.sprite("play/skill/madoka/play_madoka_skillcut_1.jpg");	break;
		case SB_Constants.CHRSEL_HOMURA:	skillCut = CCSprite.sprite("play/skill/homura/play_homura_skillcut_1.jpg");		break;
		case SB_Constants.CHRSEL_SAYAKA: 	skillCut = CCSprite.sprite("play/skill/sayaka/play_sayaka_skillcut_1.jpg");		break;
		case SB_Constants.CHRSEL_MAMI:		skillCut = CCSprite.sprite("play/skill/mami/play_mami_skillcut_1.jpg");		break;
		case SB_Constants.CHRSEL_KYOKO:		skillCut = CCSprite.sprite("play/skill/kyoko/play_kyoko_skillcut_1.jpg");		break;
		default: skillCut = CCSprite.sprite("play/skill/play_madoka_skillcut_1.png");	break;
		}
		skillCut.setScaleX(scaleX);		skillCut.setScaleY(scaleY);
		skillCut.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		
		
//		CCAnimation ani = CCAnimation.animation("skill", 0.1f);
//		for (int i=0; i<9; i++) {
//			CCSprite sprite;
//			switch (selChr) {
//			case SB_Constants.CHRSEL_MADOKA:	sprite = CCSprite.sprite(CCFormatter.format("play/skill/madoka/play_madoka_skillcut_%d.jpg", i+1));	break;
//			case SB_Constants.CHRSEL_HOMURA:	sprite = CCSprite.sprite(CCFormatter.format("play/skill/homura/play_homura_skillcut_%d.jpg", i+1));	break;
//			case SB_Constants.CHRSEL_SAYAKA: 	sprite = CCSprite.sprite(CCFormatter.format("play/skill/sayaka/play_sayaka_skillcut_%d.jpg", i+1));		break;
//			case SB_Constants.CHRSEL_MAMI:		sprite = CCSprite.sprite(CCFormatter.format("play/skill/mami/play_mami_skillcut_%d.jpg", i+1));			break;
//			case SB_Constants.CHRSEL_KYOKO:		sprite = CCSprite.sprite(CCFormatter.format("play/skill/kyoko/play_kyoko_skillcut_%d.jpg", i+1));		break;
//			default: sprite = CCSprite.sprite(CCFormatter.format("play/skill/play_madoka_skillcut_%d.png", i+1));	break;
//			}
//			ani.addFrame(sprite.getTexture());
//		}
		
		skillCut.runAction(CCSequence.actions(
				CCAnimate.action(skillAni),
//				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallFuncNSkillCutEnd")));
		addChild(skillCut, 2);

		touchable = 0;
	}
	
	protected void SoulGemTouchBegan(MotionEvent event) {
		CCSprite soulgem = getTouchedSoulGem(event.getX(), event.getY());
		if (selectedSoulGem==-1 && soulgem!=null) {
			selectedSoulGem = soulgem.getTag();
			soulgem.runAction(CCScaleTo.action(0.1f, scaleX*1.5f));
		} else if (selectedSoulGem2==-1 && soulgem!=null) {
			selectedSoulGem2 = soulgem.getTag();
			CCSprite soulgem1 = arrSoulGem[selectedSoulGem];
			CCSprite soulgem2 = arrSoulGem[selectedSoulGem2];
			if (soulgem1!=null && soulgem2!=null && soulgem1!=soulgem2) {
//				Log.d("SB", "touchable = 0 Began");
				touchable = 0;
				soulgem1.runAction(CCSequence.actions(
						CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, scaleX),
						CCMoveTo.action(SB_Constants.PLAY_MOVE_VEL, soulgem2.getPosition()),
						CCCallFuncN.action(this, "CallFuncNSoulGemMoveEnd")));
				SyncSoulGemMoveEnd++;
				soulgem2.runAction(CCSequence.actions(
						CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, scaleX),
						CCMoveTo.action(SB_Constants.PLAY_MOVE_VEL, soulgem1.getPosition()),
						CCCallFuncN.action(this, "CallFuncNSoulGemMoveEnd")));
				SyncSoulGemMoveEnd++;
							
				soulgem1.setTag(selectedSoulGem2);
				soulgem2.setTag(selectedSoulGem);
				arrSoulGem[selectedSoulGem] = soulgem2;
				arrSoulGem[selectedSoulGem2] = soulgem1;
				selectedSoulGem = -1;
				selectedSoulGem2 = -1;
				SetSoul(soul+0.5f);
			} else {
				selectedSoulGem2 = -1;
			}
		} else if (selectedSoulGem2!=-1 && soulgem==null) {
			CCSprite sprite = arrSoulGem[selectedSoulGem2];
			sprite.runAction(CCScaleTo.action(0.1f, scaleX));
			selectedSoulGem2 = -1;
		} else if (selectedSoulGem!=-1 && soulgem==null) {
			CCSprite sprite = arrSoulGem[selectedSoulGem];
			sprite.runAction(CCScaleTo.action(0.1f, scaleX));
			selectedSoulGem = -1;
		}
	}
	
	protected CCSprite getTouchedSoulGem(float x, float y) {
		CCSprite result = null;
		for (int i=0; i<arrSoulGem.length; i++) {
			CCSprite item = arrSoulGem[i];
			if (item!=null && item.getUserData()!=SB_Constants.PLAY_SOULGEM_SERVANT) {
				CGPoint pos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x, y));					
				if (item.getBoundingBox().contains(pos.x, pos.y)) {
					result = item;
				}
			}
		}
		return result;
	}
	
	protected boolean isSpriteTouched(float x, float y, CCSprite sprite) {
		CGPoint pos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x, y));
		if (sprite.getBoundingBox().contains(pos.x, pos.y))
			return true;
		else
			return false;
	}
	
	protected void UpdateSoulGem(int num) {
		if (timeStopped==1) {
			touchable = 1;
		}
		else {
			int delNum = CheckSoulGemDelete(num);
//			Log.d("SB", CCFormatter.format("UpdateSoulGem. delNum: %d", delNum));
			if (delNum>0) {
//				Log.d("SB", "touchable = 0 UpdateSoulgem");
				touchable = 0;
				for (int i=0; i<arrDel.length; i++) {
					CCSprite sprite = arrDel[i];
					if (sprite!=null) {
						sprite.setUserData(SB_Constants.PLAY_SOULGEM_DEAD);
						sprite.runAction(CCSequence.actions(
								CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, 0.0f),
								CCCallFuncN.action(this, "CallFuncNSoulGemDeleteEnd")
								));
						SyncSoulGemDeleteEnd++;
					}
					arrDel[i]=null;
				}
				SetCombo(++combo);
				SetScore(score+delNum*combo*(int)(soul+1)*SB_Constants.PLAY_SCORE_PER_SOULGEM);
				SetSkill(skill+delNum+combo);
			} else {
//				Log.d("SB", CCFormatter.format("touchable = 1"));
				touchable = 1;
				SetCombo(0);
			}
		}
	}
	
	protected int CheckSoulGemDelete(int num) {		
		for (int tag=0; tag<arrSoulGem.length; tag++) {
			int x=tag%8;
			int y=tag/8;
			String soulgemStr = GetSoulGemString(x, y);
			if (soulgemStr!=null) {
				int[] arrTag = new int[64];
				int index = 0;
				arrTag[tag] = 1;
				index++;
				// 좌
				for (int i=x-1; i>=0; i--) {
					String str = GetSoulGemString(i, y);
					if (str!=null && soulgemStr == str) {
						arrTag[MakeTag(i,y)] = 1;
						index++;
					} else break;
				}
				// 우
				for (int i=x+1; i<8; i++) {
					String str = GetSoulGemString(i, y);
					if (str!=null && soulgemStr == str) {
						arrTag[MakeTag(i,y)] = 1;
						index++;
					} else break;
				}
				
				// 개수 체크
				if (index>=num) {
					for (int i=0; i<arrTag.length; i++) {
						if (arrTag[i]==1) {						
							arrDel[i] = arrSoulGem[i];
						}
					}
				}
				index = 0;
				for (int i=0; i<arrTag.length; i++) arrTag[i] = 0;
				
				arrTag[tag] = 1;
				index++;
				
				// 위
				for (int j=y+1; j<8; j++) {
					String str = GetSoulGemString(x, j);
					if (str!=null && soulgemStr == str) {
						arrTag[MakeTag(x,j)] = 1;
						index++;
					} else break;
				}
				
				// 아래
				for (int j=y-1; j>=0; j--) {
					String str = GetSoulGemString(x, j);
					if (str!=null && soulgemStr == str) {
						arrTag[MakeTag(x,j)] = 1;
						index++;
					} else break;
				}
	
				// 개수 체크
				if (index>=num) {
					for (int i=0; i<arrTag.length; i++) {
						if (arrTag[i]==1) {
							arrDel[i] = arrSoulGem[i];
						}
					}		
				}
			}
			
			for (int i=0; i<arrDel.length; i++) {
				if (arrDel[i]!=null && arrDel[i].getUserData()!=SB_Constants.PLAY_SOULGEM_SERVANT) {
					// 위
					if (i+8<64) {
						CCSprite sprite = arrSoulGem[i+8];
						if (sprite.getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT) {
							arrDel[i+8] = arrSoulGem[i+8];
						}
					}
					// 아래
					if (i-8>=0) {
						CCSprite sprite = arrSoulGem[i-8];
						if (sprite.getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT) {
							arrDel[i-8] = arrSoulGem[i-8];
						}
					}
					// 좌
					if (i-1>=0) {
						CCSprite sprite = arrSoulGem[i-1];
						if (sprite.getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT) {
							arrDel[i-1] = arrSoulGem[i-1];
						}
					}
					// 우
					if (i+1<64) {
						CCSprite sprite = arrSoulGem[i+1];
						if (sprite.getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT) {
							arrDel[i+1] = arrSoulGem[i+1];
						}
					}
				}
			}		
		}

		int delServant = 0;
		int ret=0;
		
		for (int i=0; i<arrDel.length; i++) {
			if (arrDel[i]!=null) {
				ret++;
				if (arrDel[i].getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT)
					delServant++;
			}
		}

		servantNum -= delServant;
		deleteServant += delServant;
		if (servantNum<0) servantNum = 0;
		SetSoul(soul-delServant*10.0f);
		return ret;
	}
	
	public void CallFuncNInitEnd(Object sender) {
		UpdateSoulGemDown();
	}
	
	public void CallFuncNSoulGemMoveEnd(Object sender) {
		if (sender instanceof CCSprite) {
			SyncSoulGemMoveEnd--;
			
			if (SyncSoulGemMoveEnd==0) {
				UpdateSoulGem(SB_Constants.PLAY_SOULGEM_NUM);
			}
		}
	}
	
	public void CallFuncNSoulGemDeleteEnd(Object sender) {
		if (sender instanceof CCSprite) {
			CCSprite sprite = (CCSprite)sender;
			
			CCParticleSystem deadeffect = CCParticleSoulgemDead.node(selChr, scaleX, scaleY);
			deadeffect.setPosition(sprite.getPosition());
			addChild(deadeffect, 1);
			
			arrSoulGem[sprite.getTag()].removeFromParentAndCleanup(true);
			arrSoulGem[sprite.getTag()] = null;
			
			// 죽으면 새로운 소울젬 종류를 만들어서 새로 위쪽에 붙인다.
			SetWaitSoulGem(sprite, CreateNewSoulGem(rand));
			SyncSoulGemDeleteEnd--;
			
//			Log.d("SB", CCFormatter.format("CallFuncNSoulGemDeleteEnd. SyncSoulGemDeleteEnd: %d", SyncSoulGemDeleteEnd));
			// 모두 삭제 되었으면 내린다.
			if (SyncSoulGemDeleteEnd==0) {
				UpdateSoulGemDown();
			}
			sprite.removeFromParentAndCleanup(true);
		}
	}
	
	protected void UpdateSoulGemDown() {
		int[] arrMove = new int[128];
		for (int i=0; i<arrSoulGem.length; i++) {
			CCSprite sprite = arrSoulGem[i];
			if (sprite!=null) {
				// 자기 아래에 빈칸이 몇개 인지 구함.
				int tag = sprite.getTag();
				int x = tag%8;
				int y = tag/8;
				
				for (int j=y; j>=0; j--) {
					if (arrSoulGem[MakeTag(x,j)]==null) {
						arrMove[tag]++;
					}
				}
			}
		}		
		
		for (int i=0; i<arrSoulGem.length; i++) {
			CCSprite sprite = arrSoulGem[i];
			if (sprite!=null) {
				int tag = sprite.getTag();
				int x = tag%8;
				int y = tag/8;
								
				int num = arrMove[i];
				if (num>0) {
					sprite.setTag(MakeTag(x, y-num));
					sprite.runAction(
							CCSequence.actions(
									CCEaseOut.action(CCMoveTo.action(
											SB_Constants.PLAY_DOWN_VEL*num,
											GetRealPositionFromIndex(x, y-num, sprite.getContentSize())), SB_Constants.PLAY_DOWN_VEL*5.0f),
									CCCallFuncN.action(this, "CallFuncNSoulGemDownEnd")));
					SyncSoulGemDownEnd++;
					arrSoulGem[sprite.getTag()] = sprite;
					if (i>63)
						arrSoulGem[i] = null;
				}
			}
		}
	}
	
	public void CallFuncNSoulGemDownEnd(Object sender) {
		SyncSoulGemDownEnd--;
//		Log.d("SB", CCFormatter.format("CallFuncNSoulGemDownEnd. SyncSoulGemDownEnd: %d", SyncSoulGemDownEnd));
		if (SyncSoulGemDownEnd==0)
			UpdateSoulGem(SB_Constants.PLAY_SOULGEM_NUM);
	}
	
	protected String GetSoulGemString(int x, int y) {
		String soulgemStr = null;
		int tag = MakeTag(x, y);
		CCSprite sprite = arrSoulGem[tag];
		if (sprite!=null) {
			Object userData = sprite.getUserData();
			if( userData!=null && userData instanceof String && userData!=SB_Constants.PLAY_SOULGEM_SERVANT) {
				soulgemStr = (String)userData;
			}
		}
		return soulgemStr;
	}
	
	protected String GetSoulGemString(CCSprite sprite) {
		String soulgemStr = null;		
		Object userData = sprite.getUserData();
		if( userData!=null && userData instanceof String) {
			soulgemStr = (String)userData;
		}
		return soulgemStr;		
	}
	
	protected CCSprite CreateNewSoulGem(Random rand) {
		CCSprite soulgem = null;
		int rnd = rand.nextInt(5);
		switch(rnd) {		
		case 0: soulgem = CCSprite.sprite("play/soulgem/play_soulgem_madoka.png");		soulgem.setUserData(SB_Constants.PLAY_SOULGEM_MADOKA);	break;
		case 1: soulgem = CCSprite.sprite("play/soulgem/play_soulgem_homura.png");		soulgem.setUserData(SB_Constants.PLAY_SOULGEM_HOMURA);	break;
		case 2: soulgem = CCSprite.sprite("play/soulgem/play_soulgem_sayaka.png");		soulgem.setUserData(SB_Constants.PLAY_SOULGEM_SAYAKA);	break;
		case 3: soulgem = CCSprite.sprite("play/soulgem/play_soulgem_mami.png");			soulgem.setUserData(SB_Constants.PLAY_SOULGEM_MAMI);		break;
		case 4: soulgem = CCSprite.sprite("play/soulgem/play_soulgem_kyoko.png");		soulgem.setUserData(SB_Constants.PLAY_SOULGEM_KYOKO);	break;
		}				
		soulgem.setScaleX(scaleX);		soulgem.setScaleY(scaleY);
		return soulgem;
	}
	
	protected void SetWaitSoulGem(CCSprite oldsprite, CCSprite newsprite) {
		int x = oldsprite.getTag()%8;		
		for (int j=8; j<16; j++) {
			int tag = MakeTag(x,j);
			CCSprite temp = arrSoulGem[tag];
			if (temp==null) {
				newsprite.setTag(tag);
				arrSoulGem[tag] = newsprite;
				newsprite.setPosition(GetRealPositionFromIndex(x, j, newsprite.getContentSize()));
				addChild(newsprite, 0);
				break;
			}
		}
	}
	
	protected int MakeTag(int x, int y) {
		return y*8+x;
	}
	
	public void ScheduleWitchAttack(float dt) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<64; i++) {
			CCSprite sprite = arrSoulGem[i];
			if (sprite!=null
					&& sprite.getUserData()!=SB_Constants.PLAY_SOULGEM_DEAD 
					&& sprite.getUserData()!=SB_Constants.PLAY_SOULGEM_SERVANT
					&& CCActionManager.sharedManager().numberOfRunningActions(sprite)==0
					&& selectedSoulGem!=i) {
					list.add(i);
			}
		}			
		if (list.size()!=0) {
			int rnd = rand.nextInt(list.size());
			int tag = list.get(rnd);
			arrSoulGem[tag].removeFromParentAndCleanup(true);
			arrSoulGem[tag] = null;

			int rnd2 = rand.nextInt(2)+2*(selStage-1);
			CCSprite sprite;
			switch (rnd2) {
			case 0: sprite = CCSprite.sprite("play/servant/play_servant_1_1.png"); break;
			case 1: sprite = CCSprite.sprite("play/servant/play_servant_1_2.png"); break;
			case 2: sprite = CCSprite.sprite("play/servant/play_servant_2_1.png"); break;
			case 3: sprite = CCSprite.sprite("play/servant/play_servant_2_2.png"); break;
			default: sprite = CCSprite.sprite("play/servant/play_servant_1_1.png"); break;
			}
			
			CCSprite witch = CCSprite.sprite(sprite.getTexture(), CGRect.make(0.0f, 0.0f, 60.0f, 60.0f));			
			witch.setScaleX(scaleX);		witch.setScaleY(scaleY);
			witch.setTag(tag);
			
			CCAnimation ani = CCAnimation.animation("servantRot", 0.5f);
			ani.addFrame(sprite.getTexture(), CGRect.make(0.0f, 0.0f, 60.0f, 60.0f));
			ani.addFrame(sprite.getTexture(), CGRect.make(60.0f, 0.0f, 60.0f, 60.0f));
			witch.runAction(CCRepeatForever.action(CCAnimate.action(ani)));
			
			int i = tag%8;
			int j = tag/8;
			witch.setPosition(GetRealPositionFromIndex(i, j, witch.getContentSize()));
			witch.setUserData(SB_Constants.PLAY_SOULGEM_SERVANT);
			addChild(witch);
			arrSoulGem[tag] = witch;
			servantNum++;
		}
		SetSoul(soul+1.0f+servantNum*1.0f);
	}
	
	public void Debug(float dt) {
		debugLabel.setString(CCFormatter.format("servantNum: %d", servantNum));
	}
	
	protected void SetWitchHp(float newHp) {
		if (newHp <= 0.0f) {
			witchHp = 0.0f;
//			SoulBreakerActivity.ReplaceScene(ClearLayer.scene(selChr, selStage, score));
			touchable=0;
//			Log.d("SB", "touchable = 0 SetWitchHP");

			skillCutBackground = CCSprite.sprite("play/play_bar.png");
			skillCutBackground.setScaleX(winSize.getWidth());		skillCutBackground.setScaleY(winSize.getHeight());
			skillCutBackground.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
			skillCutBackground.setColor(ccColor3B.ccBLACK);
			skillCutBackground.setOpacity(125);
			addChild(skillCutBackground, 2);
			
			this.pauseSchedulerAndActions();
			
			removeChild(witch, false);
			addChild(witch, 2);
			
			witch.runAction(CCSequence.actions(
					CCDelayTime.action(1.5f),
					CCSpawn.actions(
							CCMoveTo.action(1.0f, CGPoint.ccp(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f)),
							CCScaleTo.action(1.0f, scaleX*130.0f/200.0f*2.0f, scaleY*130.0f/200.0f*2.0f),
							CCRotateTo.action(1.0f, 1440.0f)
							),
					CCSpawn.actions(
							CCRotateTo.action(1.0f, 1440.0f),
							CCScaleTo.action(1.0f, scaleX*130.0f/200.0f, scaleY*130.0f/200.0f)),
					CCSpawn.actions(
							CCRotateTo.action(1.0f, 1440.0f),
							CCScaleTo.action(1.0f, scaleX*130.0f/200.0f*2.0f, scaleY*130.0f/200.0f*2.0f)),
					CCSpawn.actions(
							CCRotateTo.action(1.0f, 1440.0f),
							CCScaleTo.action(1.0f, 0.0f),
							CCTintTo.action(1.0f, ccColor3B.ccBLACK)),
					CCDelayTime.action(0.5f),
					CCCallFuncN.action(this, "CallFunNWitchDeadEnd")
					));
		} else {
			witchHp = newHp;
		}
		witchHpBar.runAction(CCScaleTo.action(1.0f, scaleX*200.0f*witchHp/100.0f, scaleY*30.0f));
	}
	
	public void CallFunNWitchDeadEnd(Object sender) {
		skillAni = null;
		SB_Util.ReplaceScene(ClearLayer.scene(selChr, selStage, score, deleteServant, soul, maxCombo));
	}
	
	protected void SetSkill(float newSkill) {
//		Log.d("SB", "SetSkill. skill: "+String.valueOf(skill)+", newSkill: "+String.valueOf(newSkill));
		if (newSkill>=100.0f && skill < 100.0f) {
			skill = 100.0f;
			bitmapSkillCharged.runAction(CCFadeIn.action(1.0f));
		} else if (skill<100.0f) {
			skill = newSkill;
		}
	}
	
	protected void SetSoul(float newSoul) {
		if (newSoul>=100.0f) {
			soul = 100.0f;
//			SoulBreakerActivity.ReplaceScene(GameoverLayer.scene(selChr, selStage, score));
			this.pauseSchedulerAndActions();

			skillCutBackground = CCSprite.sprite("play/play_bar.png");
			skillCutBackground.setScaleX(winSize.getWidth());		skillCutBackground.setScaleY(winSize.getHeight());
			skillCutBackground.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
			skillCutBackground.setColor(ccColor3B.ccBLACK);
			skillCutBackground.setOpacity(125);
			addChild(skillCutBackground, 2);
			
			removeChild(playerInfoSoulgemBlack, true);
			removeChild(playerInfoSoulgem, false);
			addChild(playerInfoSoulgem, 2);
			
			playerInfoSoulgem.runAction(CCSequence.actions(
					CCTintTo.action(0.5f, ccColor3B.ccGRAY),
					CCSpawn.actions(
							CCMoveTo.action(1.0f, CGPoint.ccp(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f)),
							CCScaleTo.action(1.0f, scaleX*1.0f, scaleY*1.0f)
							),
					CCScaleTo.action(1.0f, scaleX*0.5f, scaleY*0.5f),
					CCScaleTo.action(1.0f, scaleX*1.0f, scaleY*1.0f),
					CCSpawn.actions(							
							CCScaleTo.action(1.0f, 0.0f),
							CCTintTo.action(1.0f, ccColor3B.ccBLACK)
							),
					CCDelayTime.action(0.5f),
					CCCallFuncN.action(this, "CallFunNSoulFullEnd")
					));
		} else 	if (newSoul<0.0f) {
			soul = 0.0f;
		} else {
			soul = newSoul;
		}
		playerInfoSoulgemBlack.runAction(CCTextureRectTo.action(1.0f, 0, playerInfoSoulgemBlackTex.getContentSize().getHeight()-playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlackTex.getContentSize().getWidth(), playerInfoSoulgemBlackTex.getContentSize().getHeight()*soul/100.0f, playerInfoSoulgemBlack));
	}
	
	public void CallFunNSoulFullEnd(Object sender) {
		skillAni = null;
		SB_Util.ReplaceScene(GameoverLayer.scene(selChr, selStage, score));
	}
	
	protected void SetScore(int newScore) {
		score = newScore;
		bitmapScore.setString(String.valueOf(score));
	}
	
	protected void SetCombo(int newCombo) {		
		combo = newCombo;
		if (combo>1) {
			bitmapCombo.setString(String.valueOf(combo)+" Combo");
			bitmapCombo.setOpacity(255);
		} else if (bitmapCombo.getOpacity()==255) {
			bitmapCombo.runAction(CCFadeOut.action(0.5f));
		}
		if (combo>maxCombo)
			maxCombo = combo;
	}
	
	CGPoint GetRealPositionFromIndex(int x, int y, CGSize contentSize) {
		CGPoint pos = CGPoint.zero();
		
		pos.set(winSize.getWidth()/2.0f+contentSize.getWidth()*scaleX*(x-4)+scaleX*30.0f, 
				winSize.getHeight()/2.0f+contentSize.getHeight()*scaleY*(y-4)+scaleY*28.0f);
		
		return pos;
	}
	
	protected void UseSkillMadoka() {
		// 화면에 사역마 모두 삭제.
		int delServant = 0;
		for (int i=0; i<arrSoulGem.length; i++) {
			if (arrSoulGem[i]!=null && arrSoulGem[i].getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT) {
				arrDel[i] = arrSoulGem[i];
				delServant++;
			}		
		}
		
		if (delServant>0) {
//			Log.d("SB", "touchable = 0 UseSkillMadoka");
			touchable = 0;
			for (int i=0; i<arrDel.length; i++) {
				CCSprite sprite = arrDel[i];
				if (sprite!=null) {
					sprite.setUserData(SB_Constants.PLAY_SOULGEM_DEAD);
					sprite.runAction(CCSequence.actions(
							CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, 0.0f),
							CCCallFuncN.action(this, "CallFuncNSoulGemDeleteEnd")
							));
					SyncSoulGemDeleteEnd++;
				}
				arrDel[i]=null;
			}
			SetCombo(++combo);
			SetScore(score+delServant*combo*(int)(soul+1)*SB_Constants.PLAY_SCORE_PER_SOULGEM);
			SetSkill(skill+delServant+combo);
		} else {
//			Log.d("SB", CCFormatter.format("touchable = 1"));
			touchable = 1;
			SetCombo(0);
		}

		servantNum -= delServant;
		deleteServant += delServant;
		if (servantNum<0) servantNum = 0;
		SetSoul(soul-delServant*10.0f);
	}
	
	protected void UseSkillHomura() {
		// 시간을 10초간 멈춘다.
		// 애니메이션이 멈추고
		// 3개가 모여도 소울젬이 사라지지 않고
		// 공격이 오지 않는다.
		this.pauseSchedulerAndActions();
		for (int i=0; i<arrSoulGem.length; i++) {
			if (arrSoulGem[i]!=null)
				arrSoulGem[i].pauseSchedulerAndActions();
		}		
		this.runAction(CCSequence.actions(
				CCDelayTime.action(10.0f),
				CCCallFuncN.action(this, "CallFuncNHomuraSkillEnd")
				));
		timeStopped = 1;
		touchable = 1;
	}
	
	public void CallFuncNHomuraSkillEnd(Object sender) {
		if (touchable==0) {
			this.runAction(CCSequence.actions(
					CCDelayTime.action(0.1f),
					CCCallFuncN.action(this, "CallFuncNHomuraSkillEnd")
					));
		} else {
			touchable = 0;
			timeStopped = 0;				
			UpdateSoulGem(SB_Constants.PLAY_SOULGEM_NUM);
			this.resumeSchedulerAndActions();
			for (int i=0; i<arrSoulGem.length; i++) {
				if (arrSoulGem[i]!=null)
					arrSoulGem[i].resumeSchedulerAndActions();
			}
		}
	}
	
	protected void UseSkillSayaka() {
		// 가운데 중심 십자 모양 사역마를 없앤다.
		int delServant = 0;
		for (int i=0; i<arrSoulGem.length; i++) {
			int x=i%8;
			int y=i/8;
//			Log.d("SB", "UseSkillMami(). i: "+String.valueOf(i) + ", x: "+String.valueOf(x));
			if (arrSoulGem[i]!=null && (x==3 || y==4)) {
//				Log.d("SB", "UseSkillMami(). i: "+String.valueOf(i) + ", x: "+String.valueOf(x));
				arrDel[i] = arrSoulGem[i];
				if (arrSoulGem[i].getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT)
					delServant++;
			}
		}
		
//		Log.d("SB", "touchable = 0 UseSkillMami");
		touchable = 0;
		for (int i=0; i<arrDel.length; i++) {
			CCSprite sprite = arrDel[i];
			if (sprite!=null) {
				sprite.setUserData(SB_Constants.PLAY_SOULGEM_DEAD);
				sprite.runAction(CCSequence.actions(
						CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, 0.0f),
						CCCallFuncN.action(this, "CallFuncNSoulGemDeleteEnd")
						));
				SyncSoulGemDeleteEnd++;
			}
			arrDel[i]=null;
		}
		SetCombo(++combo);
		SetScore(score+16*combo*(int)(soul+1)*SB_Constants.PLAY_SCORE_PER_SOULGEM);
		SetSkill(skill+16+combo);

		servantNum -= delServant;
		deleteServant += delServant;
		if (servantNum<0) servantNum = 0;
		SetSoul(soul-delServant*10.0f);
	}
	
	protected void UseSkillMami() {
		// 중앙 2x8 소울젬+사역마를 없앤다.
		int delServant = 0;
		for (int i=0; i<arrSoulGem.length; i++) {
			int x=i%8;
//			Log.d("SB", "UseSkillMami(). i: "+String.valueOf(i) + ", x: "+String.valueOf(x));
			if (arrSoulGem[i]!=null && (x==3 || x==4)) {
//				Log.d("SB", "UseSkillMami(). i: "+String.valueOf(i) + ", x: "+String.valueOf(x));
				arrDel[i] = arrSoulGem[i];
				if (arrSoulGem[i].getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT)
					delServant++;
			}
		}
		
//		Log.d("SB", "touchable = 0 UseSkillMami");
		touchable = 0;
		for (int i=0; i<arrDel.length; i++) {
			CCSprite sprite = arrDel[i];
			if (sprite!=null) {
				sprite.setUserData(SB_Constants.PLAY_SOULGEM_DEAD);
				sprite.runAction(CCSequence.actions(
						CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, 0.0f),
						CCCallFuncN.action(this, "CallFuncNSoulGemDeleteEnd")
						));
				SyncSoulGemDeleteEnd++;
			}
			arrDel[i]=null;
		}
		SetCombo(++combo);
		SetScore(score+16*combo*(int)(soul+1)*SB_Constants.PLAY_SCORE_PER_SOULGEM);
		SetSkill(skill+16+combo);

		servantNum -= delServant;
		deleteServant += delServant;
		if (servantNum<0) servantNum = 0;
		SetSoul(soul-delServant*10.0f);
	}
	
	protected void UseSkillKyoko() {
		// 중앙 4x4 소울젬+사역마를 없앤다.
		int delServant = 0;
		for (int i=0; i<arrSoulGem.length; i++) {
			int x=i%8;
			int y=i/8;
			if (arrSoulGem[i]!=null && (x>=2 && x<=5) && (y>=2 && y<=5)) {
//				Log.d("SB", "UseSkillKyoko(). i: "+String.valueOf(i) + ", x: "+String.valueOf(x) + ", y: "+String.valueOf(y));
				arrDel[i] = arrSoulGem[i];
				if (arrSoulGem[i].getUserData()==SB_Constants.PLAY_SOULGEM_SERVANT)
					delServant++;  
			}
		}
		
//		Log.d("SB", "touchable = 0 UseSkillKyoko");
		touchable = 0;
		for (int i=0; i<arrDel.length; i++) {
			CCSprite sprite = arrDel[i];
			if (sprite!=null) {
				sprite.setUserData(SB_Constants.PLAY_SOULGEM_DEAD);
				sprite.runAction(CCSequence.actions(
						CCScaleTo.action(SB_Constants.PLAY_SCALE_VEL, 0.0f),
						CCCallFuncN.action(this, "CallFuncNSoulGemDeleteEnd")
						));
				SyncSoulGemDeleteEnd++;
			}
			arrDel[i]=null;
		}
		SetCombo(++combo);
		SetScore(score+16*combo*(int)(soul+1)*SB_Constants.PLAY_SCORE_PER_SOULGEM);
		SetSkill(skill+16+combo);

		servantNum -= delServant;
		deleteServant += delServant;
		if (servantNum<0) servantNum = 0;
		SetSoul(soul-delServant*10.0f);
	}
	
	protected String GetWitchName(int stage) {
		String name;
		switch (stage) {
		case 1: name = SB_Constants.PLAY_WITCH_NAME_1; break;
		case 2: name = SB_Constants.PLAY_WITCH_NAME_2; break;
		default : name = SB_Constants.PLAY_WITCH_NAME_1; break;
		}
		
		return name;
	}
//	public void menuCallbackClear(Object sender) {
//		SB_Util.ReplaceScene(ClearLayer.scene(selChr, selStage, score, deleteServant, soul, maxCombo));
//	}
//	public void menuCallbackGameover(Object sender) {
//		SB_Util.ReplaceScene(GameoverLayer.scene(selChr, selStage, score));
//	}
}
