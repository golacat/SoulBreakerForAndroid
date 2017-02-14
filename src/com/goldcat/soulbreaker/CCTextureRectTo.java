package com.goldcat.soulbreaker;

import org.cocos2d.actions.interval.CCIntervalAction;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;

public class CCTextureRectTo extends CCIntervalAction {
	protected float x;
	protected float y;
	protected float w;
	protected float h;
	
	protected float startX;
	protected float startY;
	protected float startW;
	protected float startH;
	
	protected float endX;
	protected float endY;
	protected float endW;
	protected float endH;
	
	protected float deltaX;
	protected float deltaY;
	protected float deltaW;
	protected float deltaH;
	
	protected CCSprite sprite;
	
	public static CCTextureRectTo action(float t, float x, float y, float w, float h, CCSprite s) {
		return new CCTextureRectTo(t, x, y, w, h, s);
	}
	
	protected CCTextureRectTo(float t, float x, float y, float w, float h, CCSprite s) {
		super(t);
		endX = x;
		endY = y;
		endW = w;
		endH = h;
		sprite = s;
	}
	
	@Override
	public CCTextureRectTo copy() {
		return new CCTextureRectTo(duration, endX, endY, endW, endH, sprite);
	}
	
	@Override
	public void start(CCNode aTarget) {
		super.start(aTarget);
		startX = sprite.getTextureRect().origin.x;
		startY = sprite.getTextureRect().origin.y;
		startW = sprite.getTextureRect().size.width;
		startH = sprite.getTextureRect().size.height;
		
		deltaX = endX - startX;
		deltaY = endY - startY;
		deltaW = endW - startW;
		deltaH = endH - startH;		
	}
	
	@Override
	public void update(float t) {
		sprite.setTextureRect(
				startX + deltaX * t,
				startY + deltaY * t,
				startW + deltaW * t,
				startH + deltaH * t,
				false);
	}
}
