package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.jeasonfire.galaxies.components.CParticle;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.util.EntityManager;
import com.jeasonfire.galaxies.util.GameState;

public class ParticleRenderingSystem extends EntitySystem {
	@Mapper
	private ComponentMapper<CPosition> positionMapper;
	@Mapper
	private ComponentMapper<CParticle> particleMapper;

	public ShapeRenderer renderer;
	private Camera camera;

	@SuppressWarnings("unchecked")
	public ParticleRenderingSystem(Camera camera) {
		super(Aspect.getAspectForAll(CPosition.class, CParticle.class));
		this.camera = camera;
		renderer = new ShapeRenderer();
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		if (GameState.state != GameState.GAME) {
			return;
		}
		
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Point);
		float delta = Gdx.graphics.getDeltaTime();
		for (int i = 0; i < entities.size(); i++) {
			particleMapper.get(entities.get(i)).lifeTime -= delta;
			renderer.setColor(
					particleMapper.get(entities.get(i)).color.r,
					particleMapper.get(entities.get(i)).color.g,
					particleMapper.get(entities.get(i)).color.b,
					(float) (particleMapper.get(entities.get(i)).lifeTime / particleMapper
							.get(entities.get(i)).fullLifeTime));
			renderer.point(positionMapper.get(entities.get(i)).x,
					positionMapper.get(entities.get(i)).y, 0);
			if (particleMapper.get(entities.get(i)).lifeTime <= 0) {
				EntityManager.removeEntity(entities.get(i));
			}
		}
		renderer.end();
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	public void dispose() {
		renderer.dispose();
	}
}
