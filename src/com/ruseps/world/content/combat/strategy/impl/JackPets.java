package com.ruseps.world.content.combat.strategy.impl;

import com.ruseps.engine.task.Task;
import com.ruseps.engine.task.TaskManager;
import com.ruseps.model.Animation;
import com.ruseps.model.Graphic;
import com.ruseps.model.GraphicHeight;
import com.ruseps.model.Locations;
import com.ruseps.model.Projectile;
import com.ruseps.util.Misc;
import com.ruseps.world.content.combat.CombatContainer;
import com.ruseps.world.content.combat.CombatType;
import com.ruseps.world.content.combat.strategy.CombatStrategy;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.npc.NPC;

public class JackPets implements CombatStrategy {

	private static final Animation anim = new Animation(422);
	private static final Animation anim2 = new Animation(423);
	private static final Animation anim3 = new Animation(438);
	private static final Graphic gfx1 = new Graphic(1886, 3, GraphicHeight.MIDDLE);
	private static final Graphic gfx2 = new Graphic(1885);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC td = (NPC)entity;
		if(victim.getConstitution() <= 0) {
			return true;
		}
		if(td.isChargingAttack()) {
			return true;
		}
		if(Locations.goodDistance(td.getPosition().copy(), victim.getPosition().copy(), 1) && Misc.getRandom(6) <= 4) {
			td.performAnimation(anim);
			td.performGraphic(gfx1);
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MELEE, true));
		} else if(Misc.getRandom(10) <= 7) {
			td.performAnimation(anim2);
			td.setChargingAttack(true);
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.RANGED, true));
			TaskManager.submit(new Task(1, td, false) {
				@Override
				protected void execute() {
					new Projectile(td, victim, 1884, 44, 3, 43, 31, 0).sendProjectile();
					td.setChargingAttack(false).getCombatBuilder().setAttackTimer(td.getDefinition().getAttackSpeed() - 1);
					stop();
				}
			});
		} else {
			td.performAnimation(anim3);
			victim.performGraphic(gfx2);
			td.getCombatBuilder().setContainer(new CombatContainer(td, victim, 1, 2, CombatType.MAGIC, true));
		}
		return true;
	}


	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 5;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
