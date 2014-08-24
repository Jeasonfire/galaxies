package com.jeasonfire.galaxies.components;

import com.artemis.Component;

public class CPosition extends Component {
	public float x, y;
	
	public CPosition() {
		this.x = 0;
		this.y = 0;
	}
	
	public CPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
