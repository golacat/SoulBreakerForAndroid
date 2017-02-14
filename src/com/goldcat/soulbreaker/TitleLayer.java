package com.goldcat.soulbreaker;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.config.ccMacros;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.MotionEvent;

public class TitleLayer extends CCColorLayer {

	public static CCScene scene(int iMenu) {
		CCScene scene = CCScene.node();
		CCLayer layer = new TitleLayer(ccColor4B.ccc4(	255, 255, 255, 255), iMenu);
		scene.addChild(layer);
		scene.setUserData("TitleLayer");
		return scene;
	}
	
	CGSize winSize = CCDirector.sharedDirector().displaySize();
	float scaleX = SB_Util.GetScaleX();
	float scaleY = SB_Util.GetScaleY();
	ArrayList<CCBitmapFontAtlas> listMenu = new ArrayList<CCBitmapFontAtlas>();
	
	CCBitmapFontAtlas title_touch;
	CCSprite title_cube;
	CCSprite title_cube2;
	
	int isMenu = 0;
	int selectedMenu = 1;
	
	public TitleLayer(ccColor4B color, int iMenu) {
		super(color);
		isMenu = iMenu;
		
		// 배경 원 그리기
		SetTitleCircle(50);
		
		// 버전 표시
		Context context = CCDirector.sharedDirector().getActivity().getApplicationContext();
		String version = new String();
		try { 
		PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		version = i.versionName; 
		} catch(NameNotFoundException e) { }

		CCLabel lbll = CCLabel.makeLabel("ver. "+version, "DroidSans", 24);
		lbll.setScaleX(scaleX);	lbll.setScaleY(scaleY);
		lbll.setPosition(winSize.getWidth()-lbll.getContentSize().getWidth()*scaleX, lbll.getContentSize().getHeight()*scaleY);
		lbll.setColor(ccColor3B.ccBLACK);
		addChild(lbll, 0);
		

		// Soul Breaker 이미지
		CCSprite title_sb = CCSprite.sprite("title/title_soulbreaker.png");
		title_sb.setScaleX(scaleX);		title_sb.setScaleY(scaleY);
		title_sb.setPosition(winSize.getWidth() / 2.0f, winSize.getHeight() / 1.3f);
		addChild(title_sb);
		
		if (isMenu==0) {			
			title_sb.setScaleX(scaleX*0.5f*1.8f);		title_sb.setScaleY(scaleY*0.5f*1.8f);
			title_sb.setOpacity(0);
			title_sb.runAction(CCSequence.actions(
					CCSpawn.actions(
							CCScaleTo.action(3.0f, scaleX*0.5f),
							CCFadeIn.action(3.0f)),
					CCCallFuncN.action(this, "CallBackTitleScaleEnd")
					));
		} else {
			this.setIsTouchEnabled(true);
			title_sb.setScaleX(scaleX*0.5f);		title_sb.setScaleY(scaleY*0.5f);
			SetMenu();
		}
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CCBitmapFontAtlas menu = getMenuItem(event.getX(), event.getY());
		if (menu!=null) {
			if (selectedMenu != menu.getTag()) {
				selectedMenu = menu.getTag();
				SetCubeMove(menu.getPosition());				
				for (CCBitmapFontAtlas bitmap : listMenu) {
					if (bitmap == menu) {
						menu.runAction(CCScaleTo.action(0.1f, scaleX*1.2f));
					} else {
						bitmap.runAction(CCScaleTo.action(0.1f, scaleX));
					}
				}
			} else {
				switch(menu.getTag()) {
				case 1: SB_Util.ReplaceScene(CharSelLayer.scene()); break;
				case 2: {
					Activity activity = CCDirector.sharedDirector().getActivity();
					SoulBreakerActivity sbAactivity = (SoulBreakerActivity)activity;
					sbAactivity.GetHandler().sendEmptyMessage(SB_Constants.MSG_FACEBOOKFEED);
//					SoulBreakerActivity.ReplaceScene(RankLayer.scene());
					break;
				}
				case 3: {
					Activity activity = CCDirector.sharedDirector().getActivity();
					SoulBreakerActivity sbAactivity = (SoulBreakerActivity)activity;
					sbAactivity.GetHandler().sendEmptyMessage(SB_Constants.MSG_FACEBOOKLOGIN);
//					SoulBreakerActivity.ReplaceScene(InfoLayer.scene());
					break;
				}
				case 4: SB_Util.ReplaceScene(CreditLayer.scene()); break;
				case 5: {
					// 끝내는 부분.
					Activity activity = CCDirector.sharedDirector().getActivity();
					SoulBreakerActivity sbAactivity = (SoulBreakerActivity)activity;
					sbAactivity.GetHandler().sendEmptyMessage(SB_Constants.MSG_EXIT);
					break;
				}
				default: break;
				}
			}			
		}
		return true;
	}
	
	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		CCBitmapFontAtlas menu = getMenuItem(event.getX(), event.getY());
		if (menu!=null) {
			if (selectedMenu != menu.getTag()) {
				selectedMenu = menu.getTag();
				SetCubeMove(menu.getPosition());				
				for (CCBitmapFontAtlas bitmap : listMenu) {
					if (bitmap == menu) {
						menu.runAction(CCScaleTo.action(0.1f, scaleX*1.2f));
					} else {
						bitmap.runAction(CCScaleTo.action(0.1f, scaleX));
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		if (isMenu==0) {
			//  touch the screen 없애고 menu 만듬.
			title_touch.runAction(CCSequence.actions(
					CCFadeOut.action(0.1f),
					CCCallFuncN.action(this, "CallBackTouchImageFadeOutEnd")
					));
			isMenu = 1;
		} else {
			for (CCBitmapFontAtlas bitmap : listMenu) {
				if (bitmap.getTag() == selectedMenu) {
					bitmap.runAction(CCScaleTo.action(0.1f, scaleX*1.1f));
					break;
				}
			}
		}
		return true;
	}
	
//	@Override
//	public boolean ccTouchesCancelled(MotionEvent event) {
//		return true;
//	}
	
	public CCBitmapFontAtlas getMenuItem(float x, float y) {
		CCBitmapFontAtlas result = null;
		for( CCBitmapFontAtlas item : listMenu) {
			CGPoint pos = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(x, y));
			if (item.getBoundingBox().contains(pos.x, pos.y)) {
				result = item;
			}
		}
		return result;
	}
	
	public void CallBackTouchImageFadeOutEnd(Object sender) {
		removeChild(title_touch, true);
		SetMenu();
	}
	
	public void CallBackTitleScaleEnd(Object sender) {
		this.setIsTouchEnabled(true);
		// Touch the Screen 이미지
//		title_touch = CCSprite.sprite("title/title_touchthescreen.png");
		title_touch = CCBitmapFontAtlas.bitmapFontAtlas("TOUCH THE SCREEN", "madokaletters.fnt");
		title_touch.setScaleX(scaleX*0.7f);		title_touch.setScaleY(scaleY*0.7f);
		title_touch.setColor(SB_Constants.TITLE_TEXT_COLOR);
		title_touch.setPosition(CGPoint.ccp(winSize.getWidth() / 2.0f, winSize.getHeight() / 2.0f));
		title_touch.runAction(CCSequence.actions(
				CCDelayTime.action(1.0f),
				CCCallFuncN.action(this, "CallBackTouchImageBlink")));
		addChild(title_touch);
	}
	
	public void CallBackTouchImageBlink(Object sender) {
		title_touch.runAction(CCRepeatForever.action(CCBlink.action(1.0f, 1)));
	}
	
	protected void SetMenu() {
		CCBitmapFontAtlas Start	= CCBitmapFontAtlas.bitmapFontAtlas("Start", "madokaletters.fnt");		Start.setScaleX(scaleX);		Start.setScaleY(scaleY);		Start.setColor(SB_Constants.TITLE_TEXT_COLOR);	Start.setTag(1);		listMenu.add(Start);		addChild(Start);
//		CCBitmapFontAtlas Rank	= CCBitmapFontAtlas.bitmapFontAtlas("Rank", "madokaletters.fnt");		Rank.setScaleX(scaleX);		Rank.setScaleY(scaleY);		Rank.setColor(SB_Constants.TITLE_TEXT_COLOR);	Rank.setTag(2);		listMenu.add(Rank);		addChild(Rank);
//		CCBitmapFontAtlas Info	= CCBitmapFontAtlas.bitmapFontAtlas("Info", "madokaletters.fnt");		Info.setScaleX(scaleX);		Info.setScaleY(scaleY);		Info.setColor(SB_Constants.TITLE_TEXT_COLOR);	Info.setTag(3);		listMenu.add(Info);		addChild(Info);
		CCBitmapFontAtlas Credit	= CCBitmapFontAtlas.bitmapFontAtlas("Credit", "madokaletters.fnt");	Credit.setScaleX(scaleX);	Credit.setScaleY(scaleY);		Credit.setColor(SB_Constants.TITLE_TEXT_COLOR);	Credit.setTag(4);		listMenu.add(Credit);	addChild(Credit);
		CCBitmapFontAtlas Exit		= CCBitmapFontAtlas.bitmapFontAtlas("Exit", "madokaletters.fnt");		Exit.setScaleX(scaleX);		Exit.setScaleY(scaleY);		Exit.setColor(SB_Constants.TITLE_TEXT_COLOR);	Exit.setTag(5);		listMenu.add(Exit);		addChild(Exit);
		
		Start.setScaleX(scaleX*1.1f);		Start.setScaleY(scaleY*1.1f);
		Start.setPosition(winSize.getWidth()/2.0f, winSize.getHeight()/2.0f);
//		Rank.setPosition(winSize.getWidth()/2.0f, Start.getPosition().y-Start.getContentSize().getHeight()*scaleY-winSize.getHeight()/100.0f*scaleY);
//		Info.setPosition(winSize.getWidth()/2.0f, Rank.getPosition().y-Rank.getContentSize().getHeight()*scaleY-winSize.getHeight()/100.0f*scaleY);
		Credit.setPosition(winSize.getWidth()/2.0f, Start.getPosition().y-Start.getContentSize().getHeight()*scaleY-winSize.getHeight()/100.0f*scaleY);
		Exit.setPosition(winSize.getWidth()/2.0f, Credit.getPosition().y-Credit.getContentSize().getHeight()*scaleY-winSize.getHeight()/100.0f*scaleY);
				
		title_cube = CCSprite.sprite("title/title_cube.png");		title_cube.setScaleX(scaleX*0.5f);	title_cube.setScaleY(scaleY*0.5f);	addChild(title_cube);
		title_cube2 = CCSprite.sprite("title/title_cube.png");		title_cube2.setScaleX(scaleX*0.5f);	title_cube2.setScaleY(scaleY*0.5f);	addChild(title_cube2);
		CGPoint pos = Start.getPosition();
		title_cube.setPosition(pos.x-winSize.getWidth()/3.0f, pos.y);
		title_cube2.setPosition(pos.x+winSize.getWidth()/3.0f, pos.y);
		
		CCSprite bgm = CCSprite.sprite("title/title_bgmicon.png");
		bgm.setScaleX(scaleX*0.5f);		bgm.setScaleY(scaleY*0.5f);
		bgm.setAnchorPoint(1, 0);
		bgm.setPosition(winSize.getWidth()-20*scaleX, 20*scaleY);
		addChild(bgm);
	}
	
	protected void SetCubeMove(CGPoint pos) {		
		title_cube.runAction(CCMoveTo.action(0.1f, CGPoint.ccp(pos.x-winSize.getWidth()/3.0f, pos.y)));
		title_cube2.runAction(CCMoveTo.action(0.1f, CGPoint.ccp(pos.x+winSize.getWidth()/3.0f, pos.y)));
	}
	
	protected void SetTitleCircle(int num) {
		for (int i = 0; i < num; i++) {
			CCSprite title_circle = CCSprite.sprite("title/title_circle.png");
			title_circle.setPosition(winSize.getWidth() * ccMacros.CCRANDOM_0_1(), winSize.getHeight() * ccMacros.CCRANDOM_0_1());
			float v = title_circle.getPosition().y / 20.0f;
			
			SetNextCircleAction(title_circle, v);			
			addChild(title_circle);
		}
	}
	
	public void CallBackCircleMoveEnd(Object sender) {
		if (sender instanceof CCSprite) {
			CCSprite sprite = (CCSprite) sender;
			sprite.setPosition(winSize.getWidth() * ccMacros.CCRANDOM_0_1(), winSize.getHeight()+sprite.getContentSize().getHeight()*scaleY);
			float v = ccMacros.CCRANDOM_0_1()*20.0f + 30.0f;
			
			SetNextCircleAction(sprite, v);
		}
	}
	
	public void SetNextCircleAction(CCSprite sprite, float v) {
		sprite.setScale(scaleX * (ccMacros.CCRANDOM_0_1()*0.5f + 0.1f));
		switch ((int) ((ccMacros.CCRANDOM_0_1() * 3) % 3)) {
			case 0: sprite.setColor(SB_Constants.TITLE_CIRCLE_BLUE); break;
			case 1: sprite.setColor(SB_Constants.TITLE_CIRCLE_VIOLET); break;
			case 2: sprite.setColor(SB_Constants.TITLE_CIRCLE_YELLOW); break;
		}			
		sprite.runAction(CCSequence.actions(
				CCMoveTo.action(v, CGPoint.ccp(sprite.getPosition().x, -sprite.getContentSize().getHeight()*scaleY)),
				CCCallFuncN.action(this, "CallBackCircleMoveEnd")
				));
	}
}
