package com.jeasonfire.galaxies.components;

import com.artemis.Component;
import com.jeasonfire.galaxies.util.SpriteLoader;

public class CPlayer extends Component {
	public static final float JUNK_COST = 5f, FUEL_COST = 2f,
			FUEL_PER_VELOCITY = 1f / SpriteLoader.PLAYER_STATIC.getWidth();

	public float turningSpeed = 180f, brakeSpeed = 100f,
			accelerationSpeed = 100f, aeroDyn = 300f;
	public boolean thrust = true, shopOpen = false, statsOpen = false;
	public int cash = 100, junk = 0;
	public float fuel = 100, maxFuel = 100;
	public int upgradeCostTurning = 150, upgradeCostBrake = 50,
			upgradeCostAccel = 100, upgradeCostAeroDyn = 150,
			upgradeCostFuel = 200;
	public float upgradeMultiplierTurning = 1.5f,
			upgradeMultiplierBrake = 1.5f, upgradeMultiplierAccel = 1.5f,
			upgradeMultiplierAeroDyn = 1.5f, upgradeMultiplierFuel = 2.0f;

	public void resetStats() {
		turningSpeed = 180f;
		brakeSpeed = 100f;
		accelerationSpeed = 100f;
		aeroDyn = 300f;

		thrust = true;
		shopOpen = false;
		statsOpen = false;

		cash = 100;
		junk = 0;

		fuel = 100;
		maxFuel = 100;

		upgradeCostTurning = 150;
		upgradeCostBrake = 50;
		upgradeCostAccel = 100;
		upgradeCostAeroDyn = 150;
		upgradeCostFuel = 200;

		upgradeMultiplierTurning = 1.5f;
		upgradeMultiplierBrake = 1.5f;
		upgradeMultiplierAccel = 1.5f;
		upgradeMultiplierAeroDyn = 1.5f;
		upgradeMultiplierFuel = 2.0f;
	}

	public boolean upgradeTurning() {
		if (cash >= upgradeCostTurning) {
			cash -= upgradeCostTurning;
			upgradeCostTurning = (int) (upgradeCostTurning * 1.5f);
			turningSpeed *= upgradeMultiplierTurning;
			upgradeMultiplierTurning *= 0.8f;
			if (upgradeMultiplierTurning < 1.1f) {
				upgradeMultiplierTurning = 1.1f;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean upgradeBrakes() {
		if (cash >= upgradeCostBrake) {
			cash -= upgradeCostBrake;
			upgradeCostBrake = (int) (upgradeCostBrake * 1.5f);
			brakeSpeed *= upgradeMultiplierBrake;
			upgradeMultiplierBrake *= 0.8f;
			if (upgradeMultiplierBrake < 1.1f) {
				upgradeMultiplierBrake = 1.1f;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean upgradeAccel() {
		if (cash >= upgradeCostAccel) {
			cash -= upgradeCostAccel;
			upgradeCostAccel = (int) (upgradeCostAccel * 1.5f);
			accelerationSpeed *= upgradeMultiplierAccel;
			upgradeMultiplierAccel *= 0.8f;
			if (upgradeMultiplierAccel < 1.1f) {
				upgradeMultiplierAccel = 1.1f;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean upgradeAeroDyn() {
		if (cash >= upgradeCostAeroDyn) {
			cash -= upgradeCostAeroDyn;
			upgradeCostAeroDyn = (int) (upgradeCostAeroDyn * 1.5f);
			aeroDyn *= upgradeMultiplierAeroDyn;
			upgradeMultiplierAeroDyn *= 0.8f;
			if (upgradeMultiplierAeroDyn < 1.1f) {
				upgradeMultiplierAeroDyn = 1.1f;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean upgradeFuel() {
		if (cash >= upgradeCostFuel) {
			cash -= upgradeCostFuel;
			upgradeCostFuel = (int) (upgradeCostFuel * 1.5f);
			maxFuel *= upgradeMultiplierFuel;
			upgradeMultiplierFuel *= 0.8f;
			if (upgradeMultiplierFuel < 1.1f) {
				upgradeMultiplierFuel = 1.1f;
			}
			return true;
		} else {
			return false;
		}
	}
}
