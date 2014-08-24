package com.jeasonfire.galaxies.util;

import java.util.Random;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.jeasonfire.galaxies.components.CCameraFollower;
import com.jeasonfire.galaxies.components.CCollider;
import com.jeasonfire.galaxies.components.CDirection;
import com.jeasonfire.galaxies.components.COrbit;
import com.jeasonfire.galaxies.components.CParticle;
import com.jeasonfire.galaxies.components.CPickup;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.components.CSize;
import com.jeasonfire.galaxies.components.CSprite;
import com.jeasonfire.galaxies.components.CVelocity;

public class EntityManager {
	private static World world;
	private static final Random random = new Random();

	public static void setWorld(World world) {
		EntityManager.world = world;
	}

	public static Entity createPlayer(float x, float y) {
		Entity player = world.createEntity();
		player.addComponent(new CPlayer());
		player.addComponent(new CPosition(x, y));
		player.addComponent(new CSprite(SpriteLoader.PLAYER_STATIC));
		player.addComponent(new CSize(32, 32));
		player.addComponent(new CVelocity(500f));
		player.addComponent(new CDirection());
		player.addComponent(new CCameraFollower(true));
		player.addComponent(new CCollider());
		world.addEntity(player);
		return player;
	}

	public static Entity createJunkSmall(float x, float y) {
		Entity junk = world.createEntity();
		junk.addComponent(new CPosition(x, y));
		junk.addComponent(new CSprite(SpriteLoader.JUNK1));
		junk.addComponent(new CSize(16, 16));
		junk.addComponent(new CCollider());
		junk.addComponent(new CPickup(1));
		world.addEntity(junk);
		return junk;
	}

	public static Entity createJunkMedium(float x, float y) {
		Entity junk = world.createEntity();
		junk.addComponent(new CPosition(x, y));
		junk.addComponent(new CSprite(SpriteLoader.JUNK2));
		junk.addComponent(new CSize(32, 32));
		junk.addComponent(new CCollider());
		junk.addComponent(new CPickup(2));
		world.addEntity(junk);
		return junk;
	}

	public static Entity createJunkLarge(float x, float y) {
		Entity junk = world.createEntity();
		junk.addComponent(new CPosition(x, y));
		junk.addComponent(new CSprite(SpriteLoader.JUNK3));
		junk.addComponent(new CSize(64, 64));
		junk.addComponent(new CCollider());
		junk.addComponent(new CPickup(4));
		world.addEntity(junk);
		return junk;
	}
	
	public static Entity createStar(float x, float y) {
		Entity star = world.createEntity();
		star.addComponent(new CPosition(x, y));
		star.addComponent(new CSprite(SpriteLoader.STAR));
		star.addComponent(new CSize(1024, 1024));
		world.addEntity(star);
		return star;
	}

	public static Entity createPlanet(float centerX, float centerY) {
		Entity planet = world.createEntity();
		planet.addComponent(new CPosition());
		float orbitSpeed = random.nextInt(120) + 240;
		planet.addComponent(new COrbit(centerX, centerY,
				random.nextInt(4096) + 2048, orbitSpeed));
		switch (random.nextInt(3)) {
		default:
		case 0:
			planet.addComponent(new CSprite(SpriteLoader.PLANET_HOT));
			break;
		case 1:
			planet.addComponent(new CSprite(SpriteLoader.PLANET_TERRA));
			break;
		case 2:
			planet.addComponent(new CSprite(SpriteLoader.PLANET_COLD));
			break;
		}
		planet.addComponent(new CSize(128, 128));
		planet.addComponent(new CCollider());
		world.addEntity(planet);
		return planet;
	}

	public static Entity createParticle(float x, float y, Color color,
			double lifeTime, int randomFactor) {
		Entity fire = world.createEntity();
		if (randomFactor > 0) {
			fire.addComponent(new CPosition(x + random.nextInt(randomFactor)
					- randomFactor / 2, y + random.nextInt(randomFactor)
					- randomFactor / 2));
		} else {
			fire.addComponent(new CPosition(x, y));
		}
		fire.addComponent(new CParticle(lifeTime, color));
		world.addEntity(fire);
		return fire;
	}

	public static void removeEntity(Entity e) {
		world.deleteEntity(e);
	}
}
