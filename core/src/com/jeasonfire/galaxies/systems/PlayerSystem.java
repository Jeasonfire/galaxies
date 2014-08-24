package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.jeasonfire.galaxies.Galaxies;
import com.jeasonfire.galaxies.components.CCollider;
import com.jeasonfire.galaxies.components.COrbit;
import com.jeasonfire.galaxies.components.CPickup;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.util.EntityManager;
import com.jeasonfire.galaxies.util.GameState;
import com.jeasonfire.galaxies.util.Name;
import com.jeasonfire.galaxies.util.Timer;

public class PlayerSystem extends EntityProcessingSystem {
	@Mapper
	private ComponentMapper<COrbit> orbitMapper;
	@Mapper 
	private ComponentMapper<CPickup> pickupMapper;
	
	@SuppressWarnings("unchecked")
	public PlayerSystem() {
		super(Aspect.getAspectForAll(CPlayer.class));
	}

	@Override
	protected void process(Entity e) {
		if (GameState.state != GameState.GAME) {
			return;
		}
		
		CPlayer player = e.getComponent(CPlayer.class);
		Entity planet;
		if (e.getComponent(CCollider.class).colliding.size > 0
				&& (planet = e.getComponent(CCollider.class).colliding.get(0)) != null) {
			if (orbitMapper.get(planet) != null) {
				// Collided planet
				if (!player.shopOpen) {
					Timer.startTimer(Name.SHOP);
				}
				player.shopOpen = true;
			} else if (pickupMapper.get(planet) != null) {
				// Collided junk
				player.junk += pickupMapper.get(planet).junkAmt;
				switch (pickupMapper.get(planet).junkAmt) {
				case 1:
					Galaxies.playSound(Name.HIGH);
					break;
				case 2:
					Galaxies.playSound(Name.MED);
					break;
				case 4:
					Galaxies.playSound(Name.LOW);
					break;
				}
				EntityManager.removeEntity(planet);
			}
		} else {
			player.shopOpen = false;
		}
		
		if (player.fuel <= 0) {
			GameState.state = GameState.GAME_OVER;
			EntityManager.removeEntity(e);
		}
	}
}
