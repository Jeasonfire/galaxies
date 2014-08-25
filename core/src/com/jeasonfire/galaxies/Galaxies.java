package com.jeasonfire.galaxies;

import java.util.Random;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.systems.CollisionSystem;
import com.jeasonfire.galaxies.systems.MovementSystem;
import com.jeasonfire.galaxies.systems.OrbitSystem;
import com.jeasonfire.galaxies.systems.ParticleEmitterSystem;
import com.jeasonfire.galaxies.systems.ParticleRenderingSystem;
import com.jeasonfire.galaxies.systems.PlayerInputSystem;
import com.jeasonfire.galaxies.systems.PlayerSystem;
import com.jeasonfire.galaxies.systems.RenderingSystem;
import com.jeasonfire.galaxies.util.EntityManager;
import com.jeasonfire.galaxies.util.GameState;
import com.jeasonfire.galaxies.util.Name;
import com.jeasonfire.galaxies.util.SpriteLoader;

public class Galaxies extends ApplicationAdapter {
	public static Galaxies instance;
	private static final Random random = new Random();

	private World world;
	private Entity startingPlanet;

	private RenderingSystem renderingSystem;
	private ParticleRenderingSystem particleSystem;
	
	private static Music music;
	private static Sound dingLow, dingMedium, dingHigh, dingLong;

	@Override
	public void create() {
		instance = this;
		world = new World();
		world.setSystem(new PlayerInputSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new OrbitSystem());
		world.setSystem(new ParticleEmitterSystem());
		world.setSystem(new CollisionSystem());
		world.setSystem(new PlayerSystem());
		renderingSystem = new RenderingSystem();
		world.setSystem(renderingSystem);
		particleSystem = new ParticleRenderingSystem(renderingSystem.camera);
		world.setSystem(particleSystem);
		world.initialize();

		EntityManager.setWorld(world);
		
		for (int i = 0; i < 256; i++) {
			EntityManager.createJunkLarge(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}
		for (int i = 0; i < 512; i++) {
			EntityManager.createJunkMedium(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}
		for (int i = 0; i < 1024; i++) {
			EntityManager.createJunkSmall(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}

		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				createStarSystem(16384 * x + random.nextInt(4096) - 2048, 16384
						* y + random.nextInt(4096) - 2048);
			}
		}
		startingPlanet = EntityManager.createPlanet(0, 0);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music.ogg"));
		music.setLooping(true);
		music.play();
		
		dingLow = Gdx.audio.newSound(Gdx.files.internal("dingLow.ogg"));
		dingMedium = Gdx.audio.newSound(Gdx.files.internal("dingMedium.ogg"));
		dingHigh = Gdx.audio.newSound(Gdx.files.internal("dingHigh.ogg"));
		dingLong = Gdx.audio.newSound(Gdx.files.internal("dingLong.ogg"));
	}

	private void createStarSystem(float x, float y) {
		EntityManager.createStar(x, y);
		int amtOfPlanets = new Random().nextInt(4) + 2;
		for (int i = 0; i < amtOfPlanets; i++) {
			if (startingPlanet == null) {
				startingPlanet = EntityManager.createPlanet(x, y);
			} else {
				EntityManager.createPlanet(x, y);
			}
		}
	}

	public void startGame() {
		GameState.state = GameState.GAME;
		
		for (int i = 0; i < world.getEntityManager().getTotalDeleted() / 4; i++) {
			EntityManager.createJunkLarge(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}
		for (int i = 0; i < world.getEntityManager().getTotalDeleted() / 4; i++) {
			EntityManager.createJunkMedium(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}
		for (int i = 0; i < world.getEntityManager().getTotalDeleted() / 2; i++) {
			EntityManager.createJunkSmall(random.nextInt(32768) - 16384,
					random.nextInt(32768) - 16384);
		}
		
		Entity player = EntityManager.createPlayer(
				startingPlanet.getComponent(CPosition.class).x,
				startingPlanet.getComponent(CPosition.class).y);
		player.getComponent(CPlayer.class).resetStats();
	}
	
	public static void playSound(String name) {
		if (name.equals(Name.HIGH)) {
			dingHigh.play(0.3f);
		}
		if (name.equals(Name.MED)) {
			dingMedium.play(0.3f);
		}
		if (name.equals(Name.LOW)) {
			dingLow.play(0.3f);
		}
		if (name.equals(Name.LONG)) {
			dingLong.play(0.3f);
		}
	}

	@Override
	public void render() {
		world.setDelta(Gdx.graphics.getDeltaTime());
		world.process();
	}

	@Override
	public void dispose() {
		SpriteLoader.dispose();
		renderingSystem.dispose();
		particleSystem.dispose();
		music.dispose();
	}
}
