package com.jeasonfire.galaxies.components;

import java.util.Random;

import com.artemis.Component;

public class COrbit extends Component {
	private static final Random random = new Random();
	public float orbitCenterX, orbitCenterY, orbitRadius, orbitSpeed,
			orbitOffset;

	/**
	 * @param orbitSpeed
	 *            Length of a day on this orbiting object in seconds (eg.
	 *            orbitSpeed = 1 would make the planet move so fast it goes
	 *            through it's orbit in 1 second)
	 */
	public COrbit(float orbitCenterX, float orbitCenterY, float orbitRadius,
			float orbitSpeed) {
		this.orbitCenterX = orbitCenterX;
		this.orbitCenterY = orbitCenterY;
		this.orbitRadius = orbitRadius;
		this.orbitSpeed = orbitSpeed;
		this.orbitOffset = random.nextInt();
	}
}
