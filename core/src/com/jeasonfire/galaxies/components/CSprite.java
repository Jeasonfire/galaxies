package com.jeasonfire.galaxies.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jeasonfire.galaxies.util.SpriteLoader;

public class CSprite extends Component {
	public Sprite sprite;
	
	public CSprite(String filename) {
		this.sprite = SpriteLoader.loadSprite(filename);
	}
	
	public CSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
