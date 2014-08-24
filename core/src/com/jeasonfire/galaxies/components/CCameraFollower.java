package com.jeasonfire.galaxies.components;

import com.artemis.Component;

public class CCameraFollower extends Component {
	public boolean follow = false;
	
	public CCameraFollower() {
	}
	
	public CCameraFollower(boolean follow) {
		this.follow = follow;
	}
}
