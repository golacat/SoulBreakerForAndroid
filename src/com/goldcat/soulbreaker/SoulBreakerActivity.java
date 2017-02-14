package com.goldcat.soulbreaker;

import java.io.ByteArrayOutputStream;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class SoulBreakerActivity extends Activity {
	private String mFacebookAccessToken;
	private Facebook mFacebook = new Facebook(SB_Constants.FACEBOOK_APP_ID);
	private CCGLSurfaceView mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mGLSurfaceView = new CCGLSurfaceView(this);
		setContentView(mGLSurfaceView);
		
		mFacebookAccessToken = SB_Util.getAppPreferences(this, "ACCESS_TOKEN");
		mFacebook.setAccessToken(mFacebookAccessToken);
	}

	@Override
	public void onStart() {
		super.onStart();

//		Log.d("SB", "onStart");

		if (CCDirector.sharedDirector().getRunningScene()!=null) {
			
		} else {
		// attach the OpenGL view to a window
		CCDirector.sharedDirector().attachInView(mGLSurfaceView);

		// set landscape mode
		CCDirector.sharedDirector().setLandscape(false);

		// show FPS
		CCDirector.sharedDirector().setDisplayFPS(true);

		// frames per second
		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60);

//		CCScene scene = SplashLayer.scene();
		CCScene scene = CharSelLayer.scene();

		// Make the scene active
		CCDirector.sharedDirector().runWithScene(scene);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
//		Log.d("SB", CCFormatter.format("onPause: %s", CCDirector.sharedDirector().getRunningScene().getUserData().toString()));
		
		CCDirector.sharedDirector().pause();		
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (CCDirector.sharedDirector().getRunningScene()!=null)
//			Log.d("SB", CCFormatter.format("onPause: %s", CCDirector.sharedDirector().getRunningScene().getUserData().toString()));
//		else
//			Log.d("SB", "onResume");

		CCDirector.sharedDirector().resume();		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

//		Log.d("SB", CCFormatter.format("onDestroy: %s", CCDirector.sharedDirector().getRunningScene().getUserData().toString()));

		CCSpriteFrameCache.purgeSharedSpriteFrameCache();
		CCTextureCache.purgeSharedTextureCache();
		
		CCDirector.sharedDirector().purgeCachedData();
		CCDirector.sharedDirector().getSendCleanupToScene();
		CCDirector.sharedDirector().end();
	}
    @Override
	public void onBackPressed() {    	
    }
    
    public Handler GetHandler() {
    	return m_handler;
    }
	
	Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SB_Constants.MSG_EXIT: {
					// 종료 메시지가 왔다.
			    	AlertDialog.Builder adb = new AlertDialog.Builder(CCDirector.sharedDirector().getActivity());
					adb.setTitle(R.string.exit_title);
					adb.setMessage(R.string.exit_msg);
					adb.setPositiveButton(R.string.exit_positive, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CCDirector.sharedDirector().getRunningScene().removeAllChildren(true);
							moveTaskToBack(true);
							finish();
							android.os.Process.killProcess(android.os.Process.myPid() ); 
						}
					});
					adb.setNegativeButton(R.string.exit_negative, null);
					adb.show();
					break;
				}
				case SB_Constants.MSG_FACEBOOKLOGIN: {
					login();
					break;	
				}
				case SB_Constants.MSG_FACEBOOKFEED: {
					feed(msg);
					break;
				}
				case SB_Constants.MSG_FACEBOOKLOGOUT: {
					logout();
					break;
				}
			}
		}
	};
	
	// facebook 연동.
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			if (requestCode == 32665) {
				mFacebook.authorizeCallback(requestCode, resultCode, data);
			}
		}
		else {
			if (requestCode == 32665) {
				mFacebook.authorizeCallback(requestCode, resultCode, data);
			}
		}
	}
	
	private void login() {
		try {			
			if (!"".equals(mFacebookAccessToken) && mFacebookAccessToken != null) {
				mFacebook.setAccessToken(mFacebookAccessToken);
				String  resultStr = mFacebook.request("me");
				
				JSONObject jo = new JSONObject(resultStr);
				String t = jo.getString("verified");
				
				if (!"true".equals(t))
					mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());
			}			
			else
				mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());	
		} catch (JSONException je) {
			je.printStackTrace();
			mFacebook.authorize2(this, new String[] {"publish_stream, user_photos, email"}, new AuthorizeListener());			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	private void feed(Message msg) {
		try {
//			Log.d("SB", "access token : " + mFacebook.getAccessToken());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (msg.obj instanceof Bitmap) {
				Bitmap bitmap = (Bitmap)msg.obj;
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}
			
			byte[] data = baos.toByteArray();
			
			Bundle params = new Bundle();
			params.putString("message", "페이스 북 연동 테스트 중 입니다.");
			params.putString("name", "");
			params.putString("link", "");
			params.putString("description", "description SoulBreaker 를 통해 포스팅 됨.");
			params.putString("caption", "caption SoulBreaker 를 통해 포스팅 됨.");
//			params.putString("picture", "");
			params.putByteArray("picture", data);
			
//			mFacebook.request("me/feed", params, "POST");
			mFacebook.request("me/photos", params, "POST");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void logout() {
		try {
			mFacebook.logout(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 페이스 북 인증 후 처리를 위한 callback
	public class AuthorizeListener implements DialogListener {
		@Override
		public void onCancel() {
//			Log.d("SB", "onCancel");
		}
		
		@Override
		public void onComplete(Bundle values) {
//			Log.d("SB", "onComplete");
			
			mFacebookAccessToken = mFacebook.getAccessToken();
			SB_Util.setAppPreferences(SoulBreakerActivity.this, "ACCESS_TOKEN", mFacebookAccessToken);
		}
		
		@Override
		public void onError(DialogError e) {
//			Log.d("SB", "onError");
		}
		
		@Override
		public void onFacebookError(FacebookError e) {
//			Log.d("SB", "onFacebookError");
		}
	}
}