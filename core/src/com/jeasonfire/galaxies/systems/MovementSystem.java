package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.jeasonfire.galaxies.components.CDirection;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.components.CVelocity;
import com.jeasonfire.galaxies.util.GameState;

public class MovementSystem extends EntityProcessingSystem {
	public static final float FRICTION = 1f;

	@Mapper
	private ComponentMapper<CDirection> directionMapper;
	@Mapper
	private ComponentMapper<CVelocity> velocityMapper;
	@Mapper
	private ComponentMapper<CPosition> positionMapper;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(CDirection.class, CVelocity.class,
				CPosition.class));
	}

	@Override
	protected void process(Entity e) {
		if (GameState.state != GameState.GAME) {
			return;
		}

		positionMapper.get(e).x += directionMapper.get(e).getX()
				* velocityMapper.get(e).velocity * Gdx.graphics.getDeltaTime();
		positionMapper.get(e).y += directionMapper.get(e).getY()
				* velocityMapper.get(e).velocity * Gdx.graphics.getDeltaTime();

		if (e.getComponent(CPlayer.class) != null) {
			e.getComponent(CPlayer.class).fuel -= velocityMapper.get(e).velocity
					* CPlayer.FUEL_PER_VELOCITY * Gdx.graphics.getDeltaTime() / 60f;
		}
	}
}
