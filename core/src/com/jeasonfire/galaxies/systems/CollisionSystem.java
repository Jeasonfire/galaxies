package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.jeasonfire.galaxies.components.CCollider;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.components.CSize;
import com.jeasonfire.galaxies.util.GameState;

public class CollisionSystem extends IntervalEntitySystem {
	@Mapper
	private ComponentMapper<CPosition> positionMapper;
	@Mapper
	private ComponentMapper<CSize> sizeMapper;
	@Mapper
	private ComponentMapper<CCollider> colliderMapper;
	@Mapper
	private ComponentMapper<CPlayer> playerMapper;
	
	@SuppressWarnings("unchecked")
	public CollisionSystem() {
		super(Aspect.getAspectForAll(CPosition.class, CSize.class, CCollider.class), 0.064f);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		if (GameState.state != GameState.GAME) {
			return;
		}
		
		for (int i = 0; i < entities.size(); i++) {
			if (playerMapper.get(entities.get(i)) == null) {
				continue;
			}
			CCollider collider = colliderMapper.get(entities.get(i));
			collider.colliding.clear();
			CPosition pos1 = positionMapper.get(entities.get(i));
			CSize size1 = sizeMapper.get(entities.get(i));
			for (int j = 0; j < entities.size(); j++) {
				if (i == j) {
					continue;
				}
				CPosition pos2 = positionMapper.get(entities.get(j));
				CSize size2 = sizeMapper.get(entities.get(j));
				if (pos1.x + size1.w / 2 < pos2.x - size2.w / 2) {
					continue;
				}
				if (pos1.x - size1.w / 2 > pos2.x + size2.w / 2) {
					continue;
				}
				if (pos1.y + size1.h / 2 < pos2.y - size2.h / 2) {
					continue;
				}
				if (pos1.y - size1.h / 2 > pos2.y + size2.h / 2) {
					continue;
				}
				collider.colliding.add(entities.get(j));
			}
		}
	}
}
