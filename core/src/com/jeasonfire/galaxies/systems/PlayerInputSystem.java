package com.jeasonfire.galaxies.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.jeasonfire.galaxies.components.CDirection;
import com.jeasonfire.galaxies.components.CPlayer;
import com.jeasonfire.galaxies.components.CSprite;
import com.jeasonfire.galaxies.components.CVelocity;
import com.jeasonfire.galaxies.util.GameState;
import com.jeasonfire.galaxies.util.Name;
import com.jeasonfire.galaxies.util.SpriteLoader;
import com.jeasonfire.galaxies.util.Timer;

public class PlayerInputSystem extends EntityProcessingSystem {
	public static int KEY_UP = Keys.W, KEY_LEFT = Keys.A, KEY_DOWN = Keys.S,
			KEY_RIGHT = Keys.D, KEY_THRUST = Keys.SPACE, KEY_STATS = Keys.SHIFT_LEFT;

	@Mapper
	private ComponentMapper<CDirection> directionMapper;
	@Mapper
	private ComponentMapper<CVelocity> velocityMapper;
	@Mapper
	private ComponentMapper<CPlayer> playerMapper;

	@SuppressWarnings("unchecked")
	public PlayerInputSystem() {
		super(Aspect.getAspectForAll(CPlayer.class, CDirection.class,
				CVelocity.class));
	}

	@Override
	protected void process(Entity e) {
		if (GameState.state != GameState.GAME) {
			return;
		}

		boolean thrust = true;
		float direction = directionMapper.get(e).direction, turningSpeed = playerMapper
				.get(e).turningSpeed * Gdx.graphics.getDeltaTime();
		if (direction <= 0) {
			direction += 360;
		}
		if (direction > 360) {
			direction -= 360;
		}
		if (Gdx.input.isKeyPressed(KEY_UP) && !Gdx.input.isKeyPressed(KEY_LEFT)
				&& !Gdx.input.isKeyPressed(KEY_DOWN)
				&& !Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Up
			if (direction < 270 && direction >= 90) {
				direction += turningSpeed;
				if (direction > 270) {
					direction = 270;
				}
			} else if (direction > 270) {
				direction -= turningSpeed;
				if (direction < 270) {
					direction = 270;
				}
			} else if (direction < 90) {
				direction -= turningSpeed;
			}
		} else if (!Gdx.input.isKeyPressed(KEY_UP)
				&& Gdx.input.isKeyPressed(KEY_LEFT)
				&& !Gdx.input.isKeyPressed(KEY_DOWN)
				&& !Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Left
			if (direction < 180) {
				direction += turningSpeed;
				if (direction > 180) {
					direction = 180;
				}
			} else if (direction > 180) {
				direction -= turningSpeed;
				if (direction < 180) {
					direction = 180;
				}
			}
		} else if (!Gdx.input.isKeyPressed(KEY_UP)
				&& !Gdx.input.isKeyPressed(KEY_LEFT)
				&& Gdx.input.isKeyPressed(KEY_DOWN)
				&& !Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Down
			if (direction < 90) {
				direction += turningSpeed;
				if (direction > 90) {
					direction = 90;
				}
			} else if (direction > 90 && direction < 270) {
				direction -= turningSpeed;
				if (direction < 90) {
					direction = 90;
				}
			} else if (direction >= 270) {
				direction += turningSpeed;
			}
		} else if (!Gdx.input.isKeyPressed(KEY_UP)
				&& !Gdx.input.isKeyPressed(KEY_LEFT)
				&& !Gdx.input.isKeyPressed(KEY_DOWN)
				&& Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Right
			if (direction > 180) {
				direction += turningSpeed;
				if (direction > 360) {
					direction = 0;
				}
			} else if (direction > 0) {
				direction -= turningSpeed;
				if (direction < 0) {
					direction = 0;
				}
			}
		} else if (Gdx.input.isKeyPressed(KEY_UP)
				&& Gdx.input.isKeyPressed(KEY_LEFT)
				&& !Gdx.input.isKeyPressed(KEY_DOWN)
				&& !Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Up/left
			if (direction < 225 && direction >= 45) {
				direction += turningSpeed;
				if (direction > 225) {
					direction = 225;
				}
			} else if (direction > 225) {
				direction -= turningSpeed;
				if (direction < 225) {
					direction = 225;
				}
			} else if (direction < 45) {
				direction -= turningSpeed;
			}
		} else if (Gdx.input.isKeyPressed(KEY_UP)
				&& !Gdx.input.isKeyPressed(KEY_LEFT)
				&& !Gdx.input.isKeyPressed(KEY_DOWN)
				&& Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Up/Right
			if (direction < 315 && direction > 135) {
				direction += turningSpeed;
				if (direction > 315) {
					direction = 315;
				}
			} else if (direction > 315) {
				direction -= turningSpeed;
				if (direction < 315) {
					direction = 315;
				}
			} else if (direction < 135) {
				direction -= turningSpeed;
			}
		} else if (!Gdx.input.isKeyPressed(KEY_UP)
				&& Gdx.input.isKeyPressed(KEY_LEFT)
				&& Gdx.input.isKeyPressed(KEY_DOWN)
				&& !Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Down/Left
			if (direction < 135) {
				direction += turningSpeed;
				if (direction > 135) {
					direction = 135;
				}
			} else if (direction > 135 && direction <= 315) {
				direction -= turningSpeed;
				if (direction < 135) {
					direction = 135;
				}
			} else if (direction > 315) {
				direction += turningSpeed;
			}
		} else if (!Gdx.input.isKeyPressed(KEY_UP)
				&& !Gdx.input.isKeyPressed(KEY_LEFT)
				&& Gdx.input.isKeyPressed(KEY_DOWN)
				&& Gdx.input.isKeyPressed(KEY_RIGHT)) {
			// Down/Right
			if (direction < 45) {
				direction += turningSpeed;
				if (direction > 45) {
					direction = 45;
				}
			} else if (direction > 45 && direction < 225) {
				direction -= turningSpeed;
				if (direction < 45) {
					direction = 45;
				}
			} else if (direction > 225) {
				direction += turningSpeed;
			}
		} else {
			thrust = false;
		}
		directionMapper.get(e).direction = direction;
		boolean beforeThrust = playerMapper.get(e).thrust;
		playerMapper.get(e).thrust = !Gdx.input.isKeyPressed(KEY_THRUST);
		boolean lastStatsOpen = playerMapper.get(e).statsOpen;
		playerMapper.get(e).statsOpen = Gdx.input.isKeyPressed(KEY_STATS);
		velocityMapper.get(e).maxSpeed = playerMapper.get(e).aeroDyn;

		if (thrust && playerMapper.get(e).thrust) {
			velocityMapper.get(e).velocity += playerMapper.get(e).accelerationSpeed
					* Gdx.graphics.getDeltaTime();
			if (velocityMapper.get(e).velocity > velocityMapper.get(e).maxSpeed) {
				velocityMapper.get(e).velocity = velocityMapper.get(e).maxSpeed;
			}
		} else if (velocityMapper.get(e).velocity > 0) {
			velocityMapper.get(e).velocity -= playerMapper.get(e).brakeSpeed
					* Gdx.graphics.getDeltaTime();
			if (velocityMapper.get(e).velocity < 0) {
				velocityMapper.get(e).velocity = 0;
			}
			playerMapper.get(e).thrust = false;
		} else {
			playerMapper.get(e).thrust = false;
		}

		if (playerMapper.get(e).thrust != beforeThrust) {
			e.getComponent(CSprite.class).sprite = !beforeThrust ? SpriteLoader.PLAYER_MOVING
					: SpriteLoader.PLAYER_STATIC;
			// Used beforeThrust because it takes less space
		}
		
		if (playerMapper.get(e).statsOpen != lastStatsOpen) {
			Timer.startTimer(Name.UI);
		}
	}
}
