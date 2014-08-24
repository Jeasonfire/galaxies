package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.jeasonfire.galaxies.components.CParticleEmitter;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.util.EntityManager;
import com.jeasonfire.galaxies.util.GameState;

public class ParticleEmitterSystem extends EntityProcessingSystem {
	@Mapper
	private ComponentMapper<CPosition> positionMapper;
	@Mapper
	private ComponentMapper<CParticleEmitter> emitterMapper;

	@SuppressWarnings("unchecked")
	public ParticleEmitterSystem() {
		super(Aspect.getAspectForAll(CPosition.class, CParticleEmitter.class));
	}

	@Override
	protected void process(Entity e) {
		if (GameState.state != GameState.GAME) {
			return;
		}
		
		emitterMapper.get(e).currentTime -= Gdx.graphics.getDeltaTime();
		if (emitterMapper.get(e).currentTime <= 0) {
			EntityManager.createParticle(positionMapper.get(e).x,
					positionMapper.get(e).y, emitterMapper.get(e).color,
					emitterMapper.get(e).lifeTime, emitterMapper.get(e).randomFactor);
			emitterMapper.get(e).currentTime = emitterMapper.get(e).emitInterval;
		}
	}
}
