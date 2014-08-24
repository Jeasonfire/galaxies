package com.jeasonfire.galaxies.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class CParticle extends Component {
	public double lifeTime, fullLifeTime;
	public Color color;

	public CParticle(double lifeTime, Color color) {
		this.lifeTime = lifeTime;
		this.fullLifeTime = lifeTime;
		this.color = color;
	}

	public CParticle(double lifeTime, int color) {
		this.lifeTime = lifeTime;
		this.color = new Color((color & 0xFF0000) >> 16, (color & 0xFF00) >> 8,
				(color & 0xFF), (color & 0xFF000000) >> 24);
	}
}
