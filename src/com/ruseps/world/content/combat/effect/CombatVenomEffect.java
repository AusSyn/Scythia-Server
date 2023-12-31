package com.ruseps.world.content.combat.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ruseps.engine.task.Task;
import com.ruseps.model.CombatIcon;
import com.ruseps.model.Hit;
import com.ruseps.model.Hitmask;
import com.ruseps.model.Item;
import com.ruseps.world.content.PlayerPunishments.Jail;
import com.ruseps.world.entity.impl.Character;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A {@link Task} implementation that handles the venom process.
 * 
 * @author lare96
 */
public class CombatVenomEffect extends Task {

	/** The entity being inflicted with venom. */
	private Character entity;

	/**
	 * Create a new {@link CombatVenomEffect}.
	 * 
	 * @param entity
	 *            the entity being inflicted with venom.
	 */
	public CombatVenomEffect(Character entity) {
		super(33, entity, false);
		this.entity = entity;
	}

	/**
	 * Holds all of the different strengths of venom.
	 * 
	 * @author lare96
	 */
	public enum VenomType {
		MILD(130), EXTRA(150), SUPER(200);

		/** The starting damage for this venom type. */
		private int damage;

		/**
		 * Create a new {@link VenomType}.
		 * 
		 * @param damage
		 *            the starting damage for this venom type.
		 */
		private VenomType(int damage) {
			this.damage = damage;
		}

		/**
		 * Gets the starting damage for this venom type.
		 * 
		 * @return the starting damage for this venom type.
		 */
		public int getDamage() {
			return damage;
		}
	}

	@Override
	public void execute() {
		// Stop the task if the entity is unregistered.
		if (!entity.isRegistered() || !entity.isVenomed()) {
			this.stop();
			return;
		}
		if(entity.isPlayer()) {
			Player player = (Player) entity;
			if(Jail.isJailed(player)) {
				return;
			}
		}
		// Deal the damage, then try and decrement the damage count.
		entity.dealDamage(new Hit(entity.getAndDecrementVenomDamage(), Hitmask.DARK_GREEN, CombatIcon.NONE));
		/*
		 * if(entity.isPlayer()) {
		 * ((Player)entity).getPacketSender().sendInterfaceRemoval(); }
		 */
	}

	/**
	 * The small utility class that manages all of the combat venom data.
	 * 
	 * @author lare96
	 * @author Advocatus
	 */
	public static final class CombatVenomData {

		/** The map of all of the different weapons that venom. */
		// Increase the capacity of the map as more elements are added.
		private static final Map<Integer, VenomType> types = new HashMap<>(2);

		/** Load all of the venom data. */
		public static void init() {
			types.put(12926, VenomType.EXTRA);
			types.put(12284, VenomType.EXTRA);
			types.put(12282, VenomType.SUPER);
		}

		/**
		 * Gets the venom type of the specified item.
		 * 
		 * @param item
		 *            the item to get the venom type of.
		 * 
		 * @return the venom type of the specified item, or <code>null</code> if
		 *         the item is not able to venom the victim.
		 */
		public static Optional<VenomType> getVenomType(Item item) {
			if (item == null || item.getId() < 1 || item.getAmount() < 1)
				return Optional.empty();
			return Optional.ofNullable(types.get(item.getId()));
		}

		/** Default private constructor. */
		private CombatVenomData() {
		}
	}
}
