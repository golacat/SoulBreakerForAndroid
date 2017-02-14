package com.goldcat.soulbreaker;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.particlesystem.CCQuadParticleSystem;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccBlendFunc;
import org.cocos2d.types.ccColor4F;

public class CCParticleSoulgemDead extends CCQuadParticleSystem {
	
	public static CCParticleSoulgemDead node(int chr, float scaleX, float scaleY) {
		return new CCParticleSoulgemDead(chr, scaleX, scaleY);
	}
	
	protected CCParticleSoulgemDead(int chr, float scaleX, float scaleY) {
		this(10, chr, scaleX, scaleY);
	}
	
	protected CCParticleSoulgemDead(int p, int chr, float scaleX, float scaleY) {
		super(p);
		
		// duration.
		duration = 0.1f;
		
//		emitterMode = kCCParticleModeGravity;
		this.setEmitterMode(kCCParticleModeGravity);
		
		// Gravity Mode: gravity
		this.setGravity(CGPoint.ccp(0,-9.8f));
		
		// Gravity Mode: speed of particles
		setSpeed(90);
		setSpeedVar(10);
		
		// Gravity Mode: radial
		setRadialAccel(0);
		setRadialAccelVar(0);
		
		// Gravity Mode: tangential
		setTangentialAccel(0);
		setTangentialAccelVar(0);
		
		// angle
		angle = 90;
		angleVar = 90;
		
		// emitter position
		CGSize winSize = CCDirector.sharedDirector().winSize();
		
		this.setPosition(CGPoint.ccp(winSize.width/2, winSize.height/2));
		posVar = CGPoint.zero();
		
		// life of particles
		life = 0.5f;
		lifeVar = 0.01f;
		
		// size, in pixels
//		startSize = 15.0f;
//		startSizeVar = 10.0f;
//		endSize = kCCParticleStartSizeEqualToEndSize;
		
		this.setStartSize(15.0f);
		this.setStartSizeVar(5.0f);
		this.setEndSize(kCCParticleStartSizeEqualToEndSize);
		
		// emits per second
		emissionRate = totalParticles/duration;
		
		// color of particles
		switch (chr) {
		case SB_Constants.CHRSEL_MADOKA:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MADOKA);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MADOKA);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_madoka.png"));
			break;
		case SB_Constants.CHRSEL_HOMURA:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_HOMURA);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_HOMURA);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_homura.png"));
			break;
		case SB_Constants.CHRSEL_SAYAKA:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_SAYAKA);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_SAYAKA);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_sayaka.png"));
			break;
		case SB_Constants.CHRSEL_MAMI:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MAMI);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MAMI);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_mami.png"));
			break;
		case SB_Constants.CHRSEL_KYOKO:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_KYOKO);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_KYOKO);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_kyoko.png"));
			break;
		default:
			startColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MADOKA);
			endColor = ccColor4F.ccc4FFromccc3B(SB_Constants.CHR_COLOR_MADOKA);
			setTexture(CCTextureCache.sharedTextureCache().addImage("play/soulgem/play_soulgemdead_madoka.png"));
			break;
		}
		startColorVar.r = 0.1f;
		startColorVar.g = 0.1f;
		startColorVar.b = 0.1f;
		startColorVar.a = 0.0f;
		endColorVar.r = 0.1f;
		endColorVar.g = 0.1f;
		endColorVar.b = 0.1f;
		endColorVar.a = 0.0f;	
		
		this.setScaleX(scaleX);
		this.setScaleY(scaleY);
				
		// additive
		setBlendAdditive(false);
		
		this.setAutoRemoveOnFinish(true);
	}
	
	@Override
	public ccBlendFunc getBlendFunc() {
		return null;
	}
	
	@Override
	public void setBlendFunc(ccBlendFunc blendFunc) {
		
	}
}
