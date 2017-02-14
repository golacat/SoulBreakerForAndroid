package com.goldcat.soulbreaker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGSize;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class SB_Util {
	
	// 화면 전환
	public static void ReplaceScene(CCScene scene) {
		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCDirector.sharedDirector().getRunningScene().removeAllChildren(true);
		CCDirector.sharedDirector().replaceScene(
				CCFadeTransition.transition(
						SB_Constants.REPLACE_TRANSITION_DURATION,
						scene,
						SB_Constants.REPLACE_TRANSITION_COLOR));
		
	}
	
	// X 축 비율
	public static float GetScaleX() {
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float scaleX = winSize.getWidth() / SB_Constants.MAIN_WIDTH;
		return scaleX;
	}

	// Y 출 비율
	public static float GetScaleY() {
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		float scaleY = winSize.getHeight() / SB_Constants.MAIN_HEIGHT;
		return scaleY;
	}

	// 값 저장 (http://jeehun.egloos.com/4008815)
	public static void setAppPreferences(Activity context, String key, String value) {
		SharedPreferences pref = null;
		pref = context.getSharedPreferences("SoulBreaker", 0);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(key, value);
		
		prefEditor.commit();
	}
	
	// 값 불러오기 (http://jeehun.egloos.com/4008815)
	public static String getAppPreferences(Activity context, String key) {		
		SharedPreferences pref = null;
		pref = context.getSharedPreferences("SoulBreaker", 0);		
		return pref.getString(key, "");
	}
	
	// OpenGL glsurfaceview 화면 캡쳐 (http://blog.naver.com/PostView.nhn?blogId=ateon1&logNo=120145377162)
	public static Bitmap getScreenCapture() {
		GL10 gl = CCDirector.gl;
		int width = (int)CCDirector.sharedDirector().displaySize().getWidth();
		int height = (int)CCDirector.sharedDirector().displaySize().getHeight();
		int size = width * height;
		
		ByteBuffer buf = ByteBuffer.allocateDirect(size*4);
		buf.order(ByteOrder.nativeOrder());
		
		gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buf);
		
		int data[] = new int[size];
		buf.asIntBuffer().get(data);
		buf = null;
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(data, size-width, -width, 0, 0, width, height);
//		bitmap = Bitmap.createScaledBitmap(bitmap, 200, 120, true);
		data = null;
		
		short sdata[] = new short[size];
		ShortBuffer sbuf = ShortBuffer.wrap(sdata);
		bitmap.copyPixelsToBuffer(sbuf);
		
		for (int i=0; i<size; i++) {
			// BGR-565 to RGB-565
			short v = sdata[i];
			sdata[i] = (short) (((v & 0x1f) << 11) | (v &0x7e0) | ((v & 0xf800) >> 11));
		}
		sbuf.rewind();
		bitmap.copyPixelsFromBuffer(sbuf);
		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);		
//		byte[] value = baos.toByteArray();
		
		return bitmap;
	}
	
	// 파일 내용을 바이트 어레이로 가져옴. (http://jeehun.egloos.com/4008815)
//	public static byte[] getByteFromFile(File file) throws IOException {
//		InputStream is = new FileInputStream(file);
//		
//		// Get the size of the file
//		long length = file.length();
//		
//		if (length > Integer.MAX_VALUE) {
//			// too large
//		}
//		
//		byte[] bytes = new byte[(int)length];
//		
//		int offset = 0;
//		int numRead = 0;
//		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length-offset)) >=0) {
//			offset += numRead;
//		}
//		
//		if (offset < bytes.length) {
//			throw new IOException("Could not completely read file. " + file.getName());
//		}
//		
//		is.close();
//		return bytes;
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
