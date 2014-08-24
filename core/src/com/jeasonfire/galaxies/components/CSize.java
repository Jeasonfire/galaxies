package com.jeasonfire.galaxies.components;

import com.artemis.Component;

public class CSize extends Component {
	public float w, h;
	
	public CSize() {
		this.w = 0;
		this.h = 0;
	}
	
	public CSize(float w, float h) {
		this.w = w;
		this.h = h;
	}
}
