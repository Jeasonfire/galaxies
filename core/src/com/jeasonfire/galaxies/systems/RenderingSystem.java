package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.jeasonfire.galaxies.Galaxies;
import com.jeasonfire.galaxies.components.CCameraFollower;
import com.jeasonfire.galaxies.components.CDirection;
import com.jeasonfire.galaxies.components.COrbit;
import com.jeasonfire.galaxies.components.CPickup;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CPosition;
import com.jeasonfire.galaxies.components.CSize;
import com.jeasonfire.galaxies.components.CSprite;
import com.jeasonfire.galaxies.components.CVelocity;
import com.jeasonfire.galaxies.util.GameState;
import com.jeasonfire.galaxies.util.Name;
import com.jeasonfire.galaxies.util.SpriteLoader;
import com.jeasonfire.galaxies.util.Timer;

public class RenderingSystem extends EntitySystem {
	public static final float GWIDTH = 640, GHEIGHT = 360;
	public static final String TITLE = "Galaxies";

	@Mapper
	private ComponentMapper<CPosition> positionMapper;
	@Mapper
	private ComponentMapper<CSprite> spriteMapper;
	@Mapper
	private ComponentMapper<CSize> sizeMapper;
	@Mapper
	private ComponentMapper<CDirection> directionMapper;
	@Mapper
	private ComponentMapper<COrbit> orbitMapper;
	@Mapper
	private ComponentMapper<CPickup> pickupMapper;

	public OrthographicCamera camera;
	public SpriteBatch batch;
	public BitmapFont normalFont, largeFont;
	private float parallax = 3f;

	private String infoText;
	private String tutorialText;
	private float tutorialTime = 0, tutorialTimeTarget = 0;

	@SuppressWarnings("unchecked")
	public RenderingSystem() {
		super(Aspect.getAspectForAll(CPosition.class, CSprite.class,
				CSize.class));
		camera = new OrthographicCamera();
		camera.setToOrtho(true, GWIDTH, GHEIGHT);
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Timer.startTimer(Name.INTRO);
		loadFiles();
	}

	private void loadFiles() {
		normalFont = new BitmapFont(Gdx.files.internal("default.fnt"), true);
		normalFont.setUseIntegerPositions(false);
		largeFont = new BitmapFont(Gdx.files.internal("large.fnt"), true);
		largeFont.setUseIntegerPositions(false);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCameraPosition(entities);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		switch (GameState.state) {
		default:
		case GameState.INTRO:
			largeFont.draw(batch, TITLE,
					GWIDTH / 2 - largeFont.getBounds(TITLE).width / 2, GHEIGHT
							/ 2 - largeFont.getBounds(TITLE).height / 2);
			if (Timer.getTime(Name.INTRO) > 1000) {
				GameState.state = GameState.MENU;
			}
			break;
		case GameState.MENU:
			drawMenu();
			break;
		case GameState.GAME:
			drawBackground();
			drawEntities(entities);
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i).getComponent(CPlayer.class) != null) {
					drawHUD(entities.get(i), entities);
				}
			}
			break;
		case GameState.GAME_OVER:
			drawText(TITLE, GWIDTH / 2 - largeFont.getBounds(TITLE).width / 2,
					GHEIGHT / 2 - largeFont.getBounds(TITLE).height, largeFont);
			String message = "You're out of fuel :(";
			drawText(message, GWIDTH / 2 - normalFont.getBounds(message).width
					/ 2, GHEIGHT / 2 + normalFont.getBounds(message).height,
					normalFont);
			if (!Timer.timerExists(Name.GAME_OVER)) {
				Timer.startTimer(Name.GAME_OVER);
			}
			if (Timer.getTime(Name.GAME_OVER) > 3000) {
				GameState.state = GameState.MENU;
				Timer.removeTimer(Name.GAME_OVER);
			}
			break;
		}
		drawInfo();
		batch.end();
	}

	private void drawMenu() {
		drawText(TITLE, GWIDTH / 2 - largeFont.getBounds(TITLE).width / 2,
				GHEIGHT / 5 - largeFont.getBounds(TITLE).height / 2, largeFont);

		String playText = "Play";
		if (getBounds(playText,
				GWIDTH / 2 - largeFont.getBounds(playText).width / 2,
				GHEIGHT / 2 - largeFont.getBounds(playText).height / 2,
				largeFont).contains(Gdx.input.getX(), Gdx.input.getY())) {
			playText = "> Play <";
			if (Gdx.input.justTouched()) {
				Galaxies.instance.startGame();
			}
		}
		drawText(playText,
				GWIDTH / 2 - largeFont.getBounds(playText).width / 2, GHEIGHT
						/ 2 - largeFont.getBounds(playText).height / 2,
				largeFont);

		String quitText = "Quit";
		if (getBounds(quitText,
				GWIDTH / 2 - largeFont.getBounds(quitText).width / 2,
				GHEIGHT / 2 - largeFont.getBounds(quitText).height / 2 + 30,
				largeFont).contains(Gdx.input.getX(), Gdx.input.getY())) {
			quitText = "> Quit <";
			if (Gdx.input.justTouched()) {
				Galaxies.instance.startGame();
			}
		}
		drawText(quitText,
				GWIDTH / 2 - largeFont.getBounds(quitText).width / 2, GHEIGHT
						/ 2 - largeFont.getBounds(quitText).height / 2 + 30,
				largeFont);
	}

	private void drawInfo() {
		if (Timer.getTime(Name.INFO) < 1000) {
			drawText(infoText, GWIDTH / 2
					- normalFont.getBounds(infoText).width / 2, GHEIGHT / 2
					- normalFont.getBounds(infoText).height / 2, normalFont);
		} else if (Timer.timerExists(Name.INFO)) {
			Timer.removeTimer(Name.INFO);
		}
	}

	private void drawBackground() {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				batch.draw(SpriteLoader.STAR_BACKGROUND, camera.position.x
						- GWIDTH / 2 + x * GWIDTH
						- (camera.position.x / parallax) % GWIDTH,
						camera.position.y - GHEIGHT / 2 + y * GHEIGHT
								- (camera.position.y / parallax) % GHEIGHT);
			}
		}
	}

	private void drawEntities(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			drawEntity(entities.get(i));
		}
	}

	private void drawEntity(Entity e) {
		if (directionMapper.getSafe(e) != null) {
			spriteMapper.get(e).sprite
					.setRotation(directionMapper.get(e).direction);
		}
		spriteMapper.get(e).sprite.setPosition(positionMapper.get(e).x
				- sizeMapper.get(e).w / 2,
				positionMapper.get(e).y - sizeMapper.get(e).h / 2);
		spriteMapper.get(e).sprite.setScale(1f);
		spriteMapper.get(e).sprite.draw(batch);
	}

	/**
	 * @param player
	 *            Should have component CPlayer
	 */
	private void drawHUD(Entity player, ImmutableBag<Entity> entities) {
		CPlayer cPlayer;
		if ((cPlayer = player.getComponent(CPlayer.class)) == null) {
			return;
		}
		if (cPlayer.shopOpen) {
			drawShop(player);
		}
		if (cPlayer.statsOpen) {
			drawStats(player);
			drawPointers(player, entities);
		}
		drawPickupPointers(player, entities);
		drawTutorials();
	}

	private void drawTutorials() {
		tutorialTime += Gdx.graphics.getDeltaTime();

		float textSpeed = 8;
		if (tutorialTime < textSpeed) {
			tutorialTimeTarget = textSpeed;
			tutorialText = "Use the W/A/S/D keys to move.";
		} else if (tutorialTime < textSpeed * 2) {
			tutorialTimeTarget = textSpeed * 2;
			tutorialText = "Use the Space key to turn off your thrusters temporarily.";
		} else if (tutorialTime < textSpeed * 3) {
			tutorialTimeTarget = textSpeed * 3;
			tutorialText = "Hold Left Shift to open the Stats menu.";
		} else if (tutorialTime < textSpeed * 4) {
			tutorialTimeTarget = textSpeed * 4;
			tutorialText = "Holding Left Shift also brings up green pointers that point at planets.";
		} else if (tutorialTime < textSpeed * 5) {
			tutorialTimeTarget = textSpeed * 5;
			tutorialText = "Being near a planet brings up the Shop menu.";
		} else if (tutorialTime < textSpeed * 6) {
			tutorialTimeTarget = textSpeed * 6;
			tutorialText = "In the Shop menu, you can buy upgrades, fuel and sell junk.";
		} else if (tutorialTime < textSpeed * 7) {
			tutorialTimeTarget = textSpeed * 7;
			tutorialText = "You get Junk by flying at asteroids. Blue pointers point at asteroids.";
		} else if (tutorialTime < textSpeed * 8) {
			tutorialTimeTarget = textSpeed * 8;
			tutorialText = "Remember to buy fuel so you don't run out! No more tutorials~";
		} else {
			tutorialText = "";
		}

		float yOffset = (int) (tutorialTimeTarget % textSpeed)
				- (tutorialTime - (tutorialTimeTarget - textSpeed));
		drawText(tutorialText, 220, GHEIGHT + 25 + yOffset * (GHEIGHT + 150)
				/ textSpeed, 200, normalFont);
	}

	private void drawPointers(Entity player, ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (orbitMapper.getSafe(e) == null) {
				continue;
			}

			float x = positionMapper.get(e).x - positionMapper.get(player).x;
			float y = positionMapper.get(e).y - positionMapper.get(player).y;
			float len = (float) Math.sqrt(x * x + y * y);
			if (len > 4096) {
				continue;
			}
			x /= len;
			y /= len;
			SpriteLoader.POINTER.setRotation((float) Math.toDegrees(Math.atan2(
					y, x)));
			SpriteLoader.POINTER.setPosition(camera.position.x + x * 64,
					camera.position.y + y * 64);
			SpriteLoader.POINTER.draw(batch);
			continue;
		}
	}

	private void drawPickupPointers(Entity player, ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (pickupMapper.getSafe(e) == null) {
				continue;
			}

			float x = positionMapper.get(e).x - positionMapper.get(player).x;
			float y = positionMapper.get(e).y - positionMapper.get(player).y;
			float len = (float) Math.sqrt(x * x + y * y);
			if (len > 2048) {
				continue;
			}
			if (len > 1024 && (pickupMapper.get(e).junkAmt < 4)) {
				continue;
			}
			if (len > 512 && (pickupMapper.get(e).junkAmt < 2)) {
				continue;
			}
			x /= len;
			y /= len;
			SpriteLoader.POINTER_SMALL.setRotation((float) Math.toDegrees(Math
					.atan2(y, x)));
			SpriteLoader.POINTER_SMALL.setPosition(camera.position.x + x * 96,
					camera.position.y + y * 96);
			SpriteLoader.POINTER_SMALL.draw(batch);
		}
	}

	private void drawStats(Entity player) {
		CPlayer cPlayer = player.getComponent(CPlayer.class);
		int uiXOffset = (int) Math.min(30, Timer.getTime(Name.UI)
				- SpriteLoader.UI_BACKGROUND.getWidth()), uiYOffset = 55, rowHeight = 40;
		drawSprite(SpriteLoader.UI_BACKGROUND, uiXOffset - 10, 20);
		drawText("Stats", uiXOffset, 30, largeFont);
		int shipSize = (int) SpriteLoader.PLAYER_STATIC.getWidth();

		String accelText = "Acceleration:\n"
				+ (int) (cPlayer.accelerationSpeed / shipSize) + " ship len/s";
		drawText(accelText, uiXOffset, uiYOffset, 170, normalFont);

		String brakeText = "Brakes:\n" + (int) (cPlayer.brakeSpeed / shipSize)
				+ " ship len/s";
		drawText(brakeText, uiXOffset, uiYOffset + rowHeight, 170, normalFont);

		String turningText = "Turning:\n" + cPlayer.turningSpeed + " deg/s";
		drawText(turningText, uiXOffset, uiYOffset + rowHeight * 2, 170,
				normalFont);

		String aeroDynText = "Max velocity:\n"
				+ (int) (cPlayer.aeroDyn / shipSize) + " ship len/s";
		drawText(aeroDynText, uiXOffset, uiYOffset + rowHeight * 3, 170,
				normalFont);

		String currentVelocityText = "Velocity:\n"
				+ (int) (player.getComponent(CVelocity.class).velocity / shipSize)
				+ " ship len/s";
		drawText(currentVelocityText, uiXOffset, uiYOffset + rowHeight * 4,
				170, normalFont);

		String fuelText = "Fuel tank:\n" + (int) cPlayer.fuel + "/"
				+ (int) cPlayer.maxFuel + " liters";
		drawText(fuelText, uiXOffset, uiYOffset + rowHeight * 5, 170,
				normalFont);

		String cashText = "Cash:\n$" + cPlayer.cash;
		drawText(cashText, uiXOffset, uiYOffset + rowHeight * 6, 170,
				normalFont);
	}

	private void drawShop(Entity player) {
		CPlayer cPlayer = player.getComponent(CPlayer.class);
		int uiXOffset = (int) Math.max(450, 470 + (SpriteLoader.UI_BACKGROUND
				.getWidth() - Timer.getTime(Name.SHOP))), uiYOffset = 55, rowHeight = 40;
		drawSprite(SpriteLoader.UI_BACKGROUND, uiXOffset - 10, 20);
		drawText("Shop", uiXOffset, 30, largeFont);

		String accelText = "Upgrade\nAccel. [$" + cPlayer.upgradeCostAccel
				+ "]";
		if (getBounds(accelText, uiXOffset, uiYOffset, normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			accelText = "> " + accelText;
			if (Gdx.input.justTouched()) {
				if (!cPlayer.upgradeAccel()) {
					infoText = "Not enough cash!";
					Timer.startTimer(Name.INFO);
				}
			}
		}
		drawText(accelText, uiXOffset, uiYOffset, 170, normalFont);

		String brakeText = "Upgrade\nBrakes [$" + cPlayer.upgradeCostBrake
				+ "]";
		if (getBounds(brakeText, uiXOffset, uiYOffset + rowHeight, normalFont)
				.contains(
						Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
						Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			brakeText = "> " + brakeText;
			if (Gdx.input.justTouched()) {
				if (!cPlayer.upgradeBrakes()) {
					infoText = "Not enough cash!";
					Timer.startTimer(Name.INFO);
				}
			}
		}
		drawText(brakeText, uiXOffset, uiYOffset + rowHeight, 170, normalFont);

		String turnText = "Upgrade\nTurns [$" + cPlayer.upgradeCostTurning
				+ "]";
		if (getBounds(turnText, uiXOffset, uiYOffset + rowHeight * 2,
				normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			turnText = "> " + turnText;
			if (Gdx.input.justTouched()) {
				if (!cPlayer.upgradeTurning()) {
					infoText = "Not enough cash!";
					Timer.startTimer(Name.INFO);
				}
			}
		}
		drawText(turnText, uiXOffset, uiYOffset + rowHeight * 2, 170,
				normalFont);

		String aeroDynText = "Upgrade\nAerodyn. [$"
				+ cPlayer.upgradeCostAeroDyn + "]";
		if (getBounds(aeroDynText, uiXOffset, uiYOffset + rowHeight * 3,
				normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			aeroDynText = "> " + aeroDynText;
			if (Gdx.input.justTouched()) {
				if (!cPlayer.upgradeAeroDyn()) {
					infoText = "Not enough cash!";
					Timer.startTimer(Name.INFO);
				}
			}
		}
		drawText(aeroDynText, uiXOffset, uiYOffset + rowHeight * 3, 170,
				normalFont);

		float missingFuel = cPlayer.maxFuel - cPlayer.fuel;
		String fuelText = "Buy Fuel\n[$"
				+ (int) Math.min(cPlayer.cash, missingFuel * CPlayer.FUEL_COST)
				+ "]";
		if (getBounds(fuelText, uiXOffset, uiYOffset + rowHeight * 4,
				normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			fuelText = "> " + fuelText;
			if (Gdx.input.justTouched()
					&& (int) Math.min(cPlayer.cash, missingFuel
							* CPlayer.FUEL_COST) > 0) {
				cPlayer.fuel += Math.min(cPlayer.cash / CPlayer.FUEL_COST,
						missingFuel);
				cPlayer.cash -= Math.min(cPlayer.cash, missingFuel
						* CPlayer.FUEL_COST);
			}
		}
		drawText(fuelText, uiXOffset, uiYOffset + rowHeight * 4, 160,
				normalFont);

		String fuelUpgradeText = "Upgrade Fuel\n[$200]";
		if (getBounds(fuelUpgradeText, uiXOffset, uiYOffset + rowHeight * 5,
				normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			fuelUpgradeText = "> " + fuelUpgradeText;
			if (Gdx.input.justTouched()) {
				if (!cPlayer.upgradeFuel()) {
					infoText = "Not enough cash!";
					Timer.startTimer(Name.INFO);
				}
			}
		}
		drawText(fuelUpgradeText, uiXOffset, uiYOffset + rowHeight * 5, 160,
				normalFont);

		String junkText = "Sell Junk\n[$"
				+ (int) (cPlayer.junk * CPlayer.JUNK_COST) + "]";
		if (getBounds(junkText, uiXOffset, uiYOffset + rowHeight * 6,
				normalFont).contains(
				Gdx.input.getX() / (Gdx.graphics.getWidth() / GWIDTH),
				Gdx.input.getY() / (Gdx.graphics.getHeight() / GHEIGHT))) {
			junkText = "> " + junkText;
			if (Gdx.input.justTouched()) {
				cPlayer.cash += cPlayer.junk * CPlayer.JUNK_COST;
				cPlayer.junk = 0;
			}
		}
		drawText(junkText, uiXOffset, uiYOffset + rowHeight * 6, 160,
				normalFont);
	}

	private void updateCameraPosition(ImmutableBag<Entity> entities) {
		for (int i = 0; i < entities.size(); i++) {
			CCameraFollower f;
			if ((f = entities.get(i).getComponent(CCameraFollower.class)) != null
					&& f.follow) {
				camera.position.set(positionMapper.get(entities.get(i)).x,
						positionMapper.get(entities.get(i)).y, 0);
			}
		}
	}

	private void drawText(String text, float x, float y, float wrapWidth,
			BitmapFont font) {
		font.drawWrapped(batch, text, x - GWIDTH / 2 + camera.position.x, y
				- GHEIGHT / 2 + camera.position.y, wrapWidth);
	}

	private void drawText(String text, float x, float y, BitmapFont font) {
		font.draw(batch, text, x - GWIDTH / 2 + camera.position.x, y - GHEIGHT
				/ 2 + camera.position.y);
	}

	private Rectangle getBounds(String text, float x, float y, BitmapFont font) {
		return new Rectangle(x, y, font.getBounds(text).width,
				font.getBounds(text).height);
	}

	private void drawSprite(Sprite sprite, float x, float y) {
		batch.draw(sprite, x - GWIDTH / 2 + camera.position.x, y - GHEIGHT / 2
				+ camera.position.y);
	}

	@Override
	protected boolean checkProcessing() {
		return true;
	}

	public void dispose() {
		normalFont.dispose();
		largeFont.dispose();
		batch.dispose();
	}
}
