package com.goldcat.soulbreaker;

import org.cocos2d.types.ccColor3B;

public interface SB_Constants {
	public static final int MAIN_WIDTH = 480;
	public static final int MAIN_HEIGHT = 800;
	
	public static final boolean D = true;
	public static final String FACEBOOK_APP_ID = "304573936228451";
	public static final int FACEBOOK_AUTH_ID = 32665;	
	
	public static final ccColor3B TITLE_CIRCLE_YELLOW = ccColor3B.ccc3(255, 251, 118);
	public static final ccColor3B TITLE_CIRCLE_BLUE = ccColor3B.ccc3(166, 255, 253);
	public static final ccColor3B TITLE_CIRCLE_VIOLET = ccColor3B.ccc3(244, 140, 203);
	public static final ccColor3B TITLE_TEXT_COLOR = ccColor3B.ccc3(255, 135, 150);
	
	public static final ccColor3B CHR_COLOR_MADOKA = ccColor3B.ccc3(255, 135, 150);
	public static final ccColor3B CHR_COLOR_HOMURA = ccColor3B.ccc3(140, 92, 168);
	public static final ccColor3B CHR_COLOR_SAYAKA = ccColor3B.ccc3(22, 56, 150);
	public static final ccColor3B CHR_COLOR_MAMI = ccColor3B.ccc3(249, 191, 42);
	public static final ccColor3B CHR_COLOR_KYOKO = ccColor3B.ccc3(158, 15, 44);
	
	public static final int MSG_EXIT = 99;
	public static final int MSG_FACEBOOKLOGIN = 200;
	public static final int MSG_FACEBOOKFEED = 201;
	public static final int MSG_FACEBOOKLOGOUT = 202;
	
	public static final float REPLACE_TRANSITION_DURATION = 0.5f;
	public static final ccColor3B REPLACE_TRANSITION_COLOR = ccColor3B.ccBLACK;
	
	public static final int TITLE_MENU_NOEXIT = 0;
	public static final int TITLE_MENU_EXIT = 1;
	
	public static final String PLAY_SOULGEM_MADOKA = "1";
	public static final String PLAY_SOULGEM_HOMURA = "2";
	public static final String PLAY_SOULGEM_SAYAKA = "3";
	public static final String PLAY_SOULGEM_MAMI = "4";
	public static final String PLAY_SOULGEM_KYOKO = "5";	
	public static final String PLAY_SOULGEM_SERVANT = "6";
	public static final String PLAY_SOULGEM_DEAD = "7";
	public static final int CHRSEL_MADOKA = 1;
	public static final int CHRSEL_HOMURA = 2;
	public static final int CHRSEL_SAYAKA = 3;
	public static final int CHRSEL_MAMI = 4;
	public static final int CHRSEL_KYOKO = 5;
	public static final int CHRSEL_BACK = 6;
	
	public static final int PLAY_SOULGEM_NUM = 3;
	public static final float PLAY_SCALE_VEL = 0.1f;
	public static final float PLAY_MOVE_VEL = 0.1f;
	public static final float PLAY_DOWN_VEL = 0.1f;
	
	public static final int PLAY_SCORE_PER_SOULGEM = 10;
	
	public static final String PLAY_WITCH_NAME_1 = "GERTRUD";
	public static final String PLAY_WITCH_NAME_2 = "CHARLOTTE";
}
