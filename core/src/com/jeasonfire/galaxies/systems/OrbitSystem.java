package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.jeasonfire.galaxies.components.COrbit;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.util.GameState;

public class OrbitSystem extends EntitySystem {
	@Mapper
	private ComponentMapper<CPosition> positionMapper;
	@Mapper
	private ComponentMapper<COrbit> orbitMapper;

	private double time = 0;
	private boolean firstTicked = false;

	@SuppressWarnings("unchecked")
	public OrbitSystem() {
		super(Aspect.getAspectForAll(CPosition.class, COrbit.class));
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		if (GameState.state != GameState.GAME && firstTicked) {
			return;
		} else {
			firstTicked = true;
		}

		time += Gdx.graphics.getDeltaTime();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			positionMapper.get(e).x = (float) Math.cos(Math.toRadians(time
					* 360f / orbitMapper.get(e).orbitSpeed + orbitMapper.get(e).orbitOffset))
					* orbitMapper.get(e).orbitRadius
					+ orbitMapper.get(e).orbitCenterX;
			positionMapper.get(e).y = (float) Math.sin(Math.toRadians(time
					* 360f / orbitMapper.get(e).orbitSpeed + orbitMapper.get(e).orbitOffset))
					* orbitMapper.get(e).orbitRadius
					+ orbitMapper.get(e).orbitCenterY;
		}
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}
}
