package com.jeasonfire.galaxies.components;

import com.artemis.Component;

public class CDirection extends Component {
	public float direction;

	public CDirection() {
		this.direction = 0;
	}

	public CDirection(float direction) {
		this.direction = direction;
	}

	public float setDirection(float x, float y) {
		return direction = (float) Math.toDegrees(Math.atan2(y, x));
	}

	public float getX() {
		return (float) Math.cos(Math.toRadians(direction));
	}

	public float getY() {
		return (float) Math.sin(Math.toRadians(direction));
	}
}
