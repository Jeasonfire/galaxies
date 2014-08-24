package com.jeasonfire.galaxies.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class SpriteLoader {
	private static Array<Texture> textureList = new Array<Texture>();
	public static Sprite PLAYER_STATIC = loadSprite("player.png"),
			PLAYER_MOVING = loadSprite("playerMoving.png"),
			PLANET_HOT = loadSprite("planetHot.png"),
			PLANET_TERRA = loadSprite("planetTerra.png"),
			PLANET_COLD = loadSprite("planetCold.png"),
			STAR = loadSprite("star.png"),
			STAR_BACKGROUND = loadSprite("starBackground.png"),
			UI_BACKGROUND = loadSprite("uiBackground.png"),
			POINTER = loadSprite("pointer.png"),
			POINTER_SMALL = loadSprite("smallPointer.png"),
			JUNK1 = loadSprite("junkSmall.png"),
			JUNK2 = loadSprite("junkMedium.png"),
			JUNK3 = loadSprite("junkLarge.png");

	private SpriteLoader() {
	}

	public static Sprite loadSprite(String filename) {
		Texture tex = new Texture(Gdx.files.internal(filename));
		Sprite sprite = new Sprite(tex);
		sprite.setFlip(false, true);
		textureList.add(tex);
		return sprite;
	}

	public static void dispose() {
		for (int i = 0; i < textureList.size; i++) {
			textureList.get(i).dispose();
		}
	}
}
