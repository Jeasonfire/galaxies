package com.jeasonfire.galaxies.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

public class CParticleEmitter extends Component {
	public boolean emitting;
	public Color color;
	public float emitInterval, currentTime = 0;
	public double lifeTime;
	public int randomFactor;

	public CParticleEmitter(boolean emitting, Color color, float emitInterval,
			double lifeTime, int randomFactor) {
		this.emitting = emitting;
		this.color = color;
		this.emitInterval = emitInterval;
		this.lifeTime = lifeTime;
		this.randomFactor = randomFactor;
	}
}
