package com.goldcat.soulbreaker;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseInOut;
import org.cocos2d.actions.ease.CCEaseOut;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.instant.CCPlace;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCTintTo;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.view.MotionEvent;

public class CharSelLayer extends CCColorLayer {

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new CharSelLayer(ccColor4B.ccc4(255, 255, 255, 255));
		scene.addChild(layer);
		scene.setUserData("CharSelLayer");
		return scene;
	}
	
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	
	CCSprite title;
	CCSprite blade;
	int selChr = SB_Constants.CHRSEL_MADOKA;
	
	CCSprite madoka;
	CCSprite homura;
	CCSprite sayaka;
	CCSprite mami;
	CCSprite kyoko;
	CCSprite pre;
	CCSprite next;
	CCSprite start;
	CCSprite back;
	
	CGPoint startPos = CGPoint.ccp(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		
	public CharSelLayer(ccColor4B color) {
		super(color);		
		this.setIsTouchEnabled(true);
		
		SetFrame();
		
		CCSprite gear = CCSprite.sprite("chrsel/chrsel_gear.png");
		gear.setScaleX(scaleX);	gear.setScaleY(scaleY);
		gear.setOpacity(125);
		gear.setPosition(winSize.getWidth()/2, winSize.getHeight()-gear.getContentSize().getHeight()*scaleY/2.0f);
		addChild(gear);
		
		CCSprite table = CCSprite.sprite("chrsel/chrsel_table.png");
		table.setScaleX(scaleX);	table.setScaleY(scaleY);
		table.setOpacity(125);
		table.setPosition(winSize.getWidth()/2, table.getContentSize().getHeight()*scaleX);
		addChild(table);
		
		blade = CCSprite.sprite("chrsel/chrsel_blade.png");
		blade.setScaleX(scaleX);		blade.setScaleY(scaleY);
		blade.setOpacity(30);
		blade.setPosition(winSize.getWidth()/2, winSize.getHeight());
		blade.setAnchorPoint(0.5f, 1.0f);
		blade.runAction(CCSequence.actions(
				CCEaseOut.action(CCRotateBy.action(1.5f, 45.0f), 1.5f),
				CCCallFuncN.action(this, "BladeRotate")
				));				
		addChild(blade);
		
		title = CCSprite.sprite("chrsel/chrsel_title.png");
		title.setScaleX(scaleX);	title.setScaleY(scaleY);
		title.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/1.1f);
		title.setColor(SB_Constants.CHR_COLOR_MADOKA);
		addChild(title);
		
		madoka = CCSprite.sprite("chrsel/chrsel_madoka.png");
		madoka.setScaleX(scaleX);		madoka.setScaleY(scaleY);
		madoka.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/1.8f);
		addChild(madoka);
		
		homura = CCSprite.sprite("chrsel/chrsel_homura.png");
		homura.setScaleX(scaleX);		homura.setScaleY(scaleY);
		homura.setPosition(winSize.getWidth()/2.0f + homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX, winSize.getHeight()/1.8f);
		addChild(homura);
		
		sayaka = CCSprite.sprite("chrsel/chrsel_sayaka.png");
		sayaka.setScaleX(scaleX);		sayaka.setScaleY(scaleY);
		sayaka.setPosition(winSize.getWidth()/2.0f + 2.0f*(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f);
		addChild(sayaka);
		
		mami = CCSprite.sprite("chrsel/chrsel_mami.png");
		mami.setScaleX(scaleX);		mami.setScaleY(scaleY);
		mami.setPosition(winSize.getWidth()/2.0f - 2.0f*(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f);
		addChild(mami);
		
		kyoko = CCSprite.sprite("chrsel/chrsel_kyoko.png");
		kyoko.setScaleX(scaleX);		kyoko.setScaleY(scaleY);
		kyoko.setPosition(winSize.getWidth()/2.0f - (homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f);
		addChild(kyoko);
				
		start = CCSprite.sprite("chrsel/chrsel_start.png");
		start.setScaleX(scaleX*1.2f);		start.setScaleY(scaleY*1.2f);
		start.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/6.5f);
		start.setColor(SB_Constants.CHR_COLOR_MADOKA);
		addChild(start);
		
		back = CCSprite.sprite("chrsel/chrsel_back.png");
		back.setScaleX(scaleX);		back.setScaleY(scaleY);
		back.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/15.5f);
		back.setColor(SB_Constants.CHR_COLOR_MADOKA);
		addChild(back);
	}	

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		if (getSpriteTouched(event.getX(), event.getY(), start)) {
			SB_Util.ReplaceScene(StageIntroLayer.scene(selChr, 1, 0));
		} else if (getSpriteTouched(event.getX(), event.getY(), back)) {
			SB_Util.ReplaceScene(TitleLayer.scene(SB_Constants.TITLE_MENU_EXIT));
		} else {
			startPos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		}
		return true;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		return true;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint pos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		float disX = startPos.x - pos.x;
		if (disX > 30.0f) {
			madoka.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(-(madoka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), 0.0f)));
			homura.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(-(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), 0.0f)));
			sayaka.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(-(sayaka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), 0.0f)));
			mami.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(-(mami.getContentSize().getWidth()*scaleX + 10.0f*scaleX), 0.0f)));
			kyoko.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(-(kyoko.getContentSize().getWidth()*scaleX + 10.0f*scaleX), 0.0f)));
			
			selChr++;
			
		} else if (disX < -30.0f) {
			madoka.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(madoka.getContentSize().getWidth()*scaleX + 10.0f*scaleX, 0.0f)));
			homura.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX, 0.0f)));
			sayaka.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(sayaka.getContentSize().getWidth()*scaleX + 10.0f*scaleX, 0.0f)));
			mami.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(mami.getContentSize().getWidth()*scaleX + 10.0f*scaleX, 0.0f)));
			kyoko.runAction(CCMoveBy.action(0.2f, CGPoint.ccp(kyoko.getContentSize().getWidth()*scaleX + 10.0f*scaleX, 0.0f)));
			
			selChr--;
		}
		
		if (selChr < SB_Constants.CHRSEL_MADOKA) selChr = SB_Constants.CHRSEL_KYOKO;
		else if (selChr > SB_Constants.CHRSEL_KYOKO) selChr = SB_Constants.CHRSEL_MADOKA;
		
		switch (selChr) {
		case SB_Constants.CHRSEL_MADOKA:
			if (disX > 30.0f)	{
				sayaka.stopAllActions();
				sayaka.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f + 2.0f*(sayaka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			} else if (disX < -30.0f) {
				mami.stopAllActions();
				mami.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f - 2.0f*(mami.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));				
			}
			title.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MADOKA));
			start.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MADOKA));
			back.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MADOKA));
			break;
		case SB_Constants.CHRSEL_HOMURA:
			if (disX > 30.0f)	{
				mami.stopAllActions();
				mami.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f + 2.0f*(mami.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			} else if (disX < -30.0f) {
				kyoko.stopAllActions();
				kyoko.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f - 2.0f*(kyoko.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			}
			title.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_HOMURA));
			start.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_HOMURA));
			back.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_HOMURA));
			break;
		case SB_Constants.CHRSEL_SAYAKA:
			if (disX > 30.0f)	{
				kyoko.stopAllActions();
				kyoko.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f + 2.0f*(kyoko.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			} else if (disX < -30.0f) {
				madoka.stopAllActions();
				madoka.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f - 2.0f*(madoka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			}
			title.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_SAYAKA));
			start.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_SAYAKA));
			back.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_SAYAKA));
			break;
		case SB_Constants.CHRSEL_MAMI:
			if (disX > 30.0f)	{
				madoka.stopAllActions();
				madoka.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f + 2.0f*(madoka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			} else if (disX < -30.0f) {
				homura.stopAllActions();
				homura.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f - 2.0f*(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			}
			title.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MAMI));
			start.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MAMI));
			back.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_MAMI));
			break;
		case SB_Constants.CHRSEL_KYOKO:			
			if (disX > 30.0f)	{
				homura.stopAllActions();
				homura.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f + 2.0f*(homura.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			}
			else if (disX < -30.0f) {
				sayaka.stopAllActions();
				sayaka.runAction(CCPlace.action(CGPoint.ccp(winSize.getWidth()/2.0f - 2.0f*(sayaka.getContentSize().getWidth()*scaleX + 10.0f*scaleX), winSize.getHeight()/1.8f)));
			}
			title.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_KYOKO));
			start.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_KYOKO));
			back.runAction(CCTintTo.action(0.2f, SB_Constants.CHR_COLOR_KYOKO));
			break;
		}
		
		
		startPos = pos;
		return true;
	}
	
	public boolean getSpriteTouched(float x, float y, CCSprite sprite) {
		CGPoint pos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x, y));
		if (sprite.getBoundingBox().contains(pos.x, pos.y)) {
			return true;
		}
		return false;
	}
	
	public void BladeRotate(Object sender) {
		blade.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCEaseInOut.action(CCRotateBy.action(3.0f, -90.0f), 3.0f),
						CCEaseInOut.action(CCRotateBy.action(3.0f, 90.0f), 3.0f)
						)));
	}
	
	protected void SetFrame() {		
		CCSprite frame1 = CCSprite.sprite("chrsel/chrsel_frame_1.png");
		frame1.setScaleX(scaleX);	frame1.setScaleY(scaleY);
		frame1.setOpacity(200);
		frame1.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f+100.0f*scaleY);
		frame1.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(1.5f),
						CCMoveBy.action(4.0f, CGPoint.ccp(-winSize.getWidth()/15.0f, 0.0f)),
						CCDelayTime.action(1.5f),
						CCMoveBy.action(4.0f, CGPoint.ccp(+winSize.getWidth()/15.0f, 0.0f))
						)));
		addChild(frame1);

		CCSprite frame2 = CCSprite.sprite("chrsel/chrsel_frame_2.png");
		frame2.setScaleX(scaleX);	frame2.setScaleY(scaleY);
		frame2.setOpacity(200);
		frame2.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
		frame2.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(1.0f),
						CCMoveBy.action(5.0f, CGPoint.ccp(winSize.getWidth()/10.0f, 0.0f)),
						CCDelayTime.action(1.0f),
						CCMoveBy.action(5.0f, CGPoint.ccp(-winSize.getWidth()/10.0f, 0.0f))
						)));
		addChild(frame2);		

		CCSprite frame3 = CCSprite.sprite("chrsel/chrsel_frame_3.png");
		frame3.setScaleX(scaleX);	frame3.setScaleY(scaleY);
		frame3.setOpacity(200);
		frame3.setPosition(winSize.getWidth()/2.0f-120.0f*scaleX, winSize.getHeight()/2.0f+50.0f*scaleY);
		frame3.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(1.8f),
						CCMoveBy.action(3.5f, CGPoint.ccp(winSize.getWidth()/13.0f, 0.0f)),
						CCDelayTime.action(1.3f),
						CCMoveBy.action(3.5f, CGPoint.ccp(-winSize.getWidth()/13.0f, 0.0f))
						)));
		addChild(frame3);		

		CCSprite frame4 = CCSprite.sprite("chrsel/chrsel_frame_4.png");
		frame4.setScaleX(scaleX);	frame4.setScaleY(scaleY);
		frame4.setOpacity(200);
		frame4.setPosition(winSize.getWidth()/2.0f+110.0f*scaleX, winSize.getHeight()/2.0f+30.0f*scaleY);
		frame4.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(2.1f),
						CCMoveBy.action(2.5f, CGPoint.ccp(winSize.getWidth()/19.0f, 0.0f)),
						CCDelayTime.action(1.4f),
						CCMoveBy.action(2.5f, CGPoint.ccp(-winSize.getWidth()/19.0f, 0.0f))
						)));
		addChild(frame4);

		CCSprite frame5 = CCSprite.sprite("chrsel/chrsel_frame_5.png");
		frame5.setScaleX(scaleX);	frame5.setScaleY(scaleY);
		frame5.setOpacity(200);
		frame5.setPosition(winSize.getWidth()/2.0f-210.0f*scaleX, winSize.getHeight()/2.0f+80.0f*scaleY);
		frame5.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(0.6f),
						CCMoveBy.action(1.9f, CGPoint.ccp(winSize.getWidth()/24.0f, 0.0f)),
						CCDelayTime.action(3.4f),
						CCMoveBy.action(1.9f, CGPoint.ccp(-winSize.getWidth()/24.0f, 0.0f))
						)));
		addChild(frame5);

		CCSprite frame6 = CCSprite.sprite("chrsel/chrsel_frame_6.png");
		frame6.setScaleX(scaleX);		frame6.setScaleY(scaleY);
		frame6.setOpacity(200);
		frame6.setPosition(winSize.getWidth()/2.0f+220.0f*scaleX, winSize.getHeight()/2.0f+95.0f*scaleY);
		frame6.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(0.9f),
						CCMoveBy.action(2.3f, CGPoint.ccp(winSize.getWidth()/18.0f, 0.0f)),
						CCDelayTime.action(2.1f),
						CCMoveBy.action(2.3f, CGPoint.ccp(-winSize.getWidth()/18.0f, 0.0f))
						)));
		addChild(frame6);

		CCSprite frame7 = CCSprite.sprite("chrsel/chrsel_frame_7.png");
		frame7.setScaleX(scaleX);		frame7.setScaleY(scaleY);
		frame7.setOpacity(200);
		frame7.setPosition(winSize.getWidth()/2.0f-180.0f*scaleX, winSize.getHeight()/2.0f+156.0f*scaleY);
		frame7.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(0.2f),
						CCMoveBy.action(2.1f, CGPoint.ccp(winSize.getWidth()/19.0f, 0.0f)),
						CCDelayTime.action(1.8f),
						CCMoveBy.action(2.1f, CGPoint.ccp(-winSize.getWidth()/19.0f, 0.0f))
						)));
		addChild(frame7);

		CCSprite frame8 = CCSprite.sprite("chrsel/chrsel_frame_8.png");
		frame8.setScaleX(scaleX);	frame8.setScaleY(scaleY);
		frame8.setOpacity(200);
		frame8.setPosition(winSize.getWidth()/2.0f-18.0f*scaleX, winSize.getHeight()/2.0f+208.0f*scaleY);
		frame8.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(1.1f),
						CCMoveBy.action(2.2f, CGPoint.ccp(-winSize.getWidth()/22.0f, 0.0f)),
						CCDelayTime.action(1.9f),
						CCMoveBy.action(2.2f, CGPoint.ccp(+winSize.getWidth()/22.0f, 0.0f))
						)));
		addChild(frame8);

		CCSprite frame9 = CCSprite.sprite("chrsel/chrsel_frame_9.png");
		frame9.setScaleX(scaleX);	frame9.setScaleY(scaleY);
		frame9.setOpacity(200);
		frame9.setPosition(winSize.getWidth()/2.0f+155.0f*scaleX, winSize.getHeight()/2.0f+206.0f*scaleY);
		frame9.runAction(CCRepeatForever.action(
				CCSequence.actions(
						CCDelayTime.action(0.5f),
						CCMoveBy.action(2.6f, CGPoint.ccp(winSize.getWidth()/29.0f, 0.0f)),
						CCDelayTime.action(1.1f),
						CCMoveBy.action(2.6f, CGPoint.ccp(-winSize.getWidth()/29.0f, 0.0f))
						)));
		addChild(frame9);
	}
}
