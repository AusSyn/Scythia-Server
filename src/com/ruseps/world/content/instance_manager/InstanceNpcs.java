package com.ruseps.world.content.instance_manager;

public enum InstanceNpcs {

	SNOWMAN(5049),
	RYUK(4990),
	JESUS(4991),
	SIMBA(4992),
	KID_SORA(4999),
	SULLY(4994),
	MEGA_CHARIZARD(4981),
	SAURON(4997),
	SQUIDWARD(4993),
	ICE_DEMON(4980),
	EVE(4271),
	GIMLEE(4265),
	BLOOD_ELEMENTAL(4267),
	TIKI_DEMON(4268),
	ARAGORN(4270),
	RAYQUAZA(4275),
	LEGOLAS(3008),
	/** BOSSES **/
	DARTH_MAUL(5048),
	DIAMOND_HEAD(4998),
	DARIUS(4263),
	DEADLY_ROBOT(4264),
	ZELDORADO(4606),
	HEATBLAST(4266),
	KEVIN_FOUR_ARMS(4269),
	RA(4272),
	DARK_KNIGHT(5048),
	BAD_BITCH(4274),
	CANNONBOLT(3010),
	RED_ASSASSIN(5049),
	IT(3014),
	YVALTAL(190),
	UPGRADE(185),
	MOUNTAIN_DWELLER(4387),
	DARK_MAGICIAN_GIRL(1506),
	OBELISK(1510),
	BLOOD_QUEEN(1508),
	FOUR_ARMS(1509);
	;
	
	private int npcId;
	
	InstanceNpcs(int npcId) {
		this.npcId = npcId;
	}
	
	public int getNpcId() {
		return npcId;
	}
}