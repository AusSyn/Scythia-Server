package com.ruseps.world.entity.impl.npc.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.definitions.DropUtils;
import com.ruseps.model.definitions.NPCDrops;
import com.ruseps.model.definitions.NPCDrops.NpcDropItem;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.combat.CombatBuilder.CombatDamageCache;
import com.ruseps.world.content.combat.CombatFactory;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A class that handles the Wyrm boss.
 * 
 * @author Blake
 *
 */
public class WreckedLoot extends NPC {
	
	public static int spawnTime = 10;
	
	public static int[] COMMONLOOT = {14793, 19994, 20106, 20108, 20110, 20112, 20114, 20116,  };
	public static int[] MEDIUMLOOT = { 20100, 20102, 20104};
	public static int[] RARELOOT = { 18448, 18950, 18918, 18936, 18920, };
	public static int[] SUPERRARELOOT = { 2701, 2702, 2704, 2705, 2706, 2707, 2708, 10942, 18057,};
	
	/**
	 * The npc id.
	 */
	public static final int NPC_ID = 1416;

	/**
	 * Constructs a new {@link Wyrm}.
	 * 
	 * @param position
	 *            The position.
	 */
	public WreckedLoot(Position position) {
		super(NPC_ID, position); 
	}
	
	@Override
	public void dropItems(Player killer) {
		handleDrop(this);
		spawnTime = 30;
	}

	/**
	 * Handles the drops for the Wyrm.
	 * 
	 * @param npc
	 *            The npc.
	 */
	
	public static void handleDrop(NPC npc) {
		if (npc.getCombatBuilder().getDamageMap().size() == 0) {
			return;
		}

		Map<Player, Long> killers = new HashMap<>();

		for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {

			if (entry == null) {
				continue;
			}

			long timeout = entry.getValue().getStopwatch().elapsed();

			if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
				continue;
			}

			Player player = entry.getKey();

			if (player.getConstitution() <= 0 || !player.isRegistered()) {
				continue;
			}

			killers.put(player, entry.getValue().getDamage());
			
		}

		npc.getCombatBuilder().getDamageMap().clear();

		List<Entry<Player, Long>> result = sortEntries(killers);
		
		int count = 0;
		
		for (Entry<Player, Long> entry : result) {

			Player killer = entry.getKey();
			Long damage = entry.getValue();

			handleDrop(npc, killer, damage);

			

			if (++count >= 10) {
				break;
			}

		}

}
	
	/**
	 * Gives the loot to the player.
	 * 
	 * @param player
	 *            The player.
	 * @param npc
	 *            The npc.
	 * @param pos
	 *            The position.
	 */
	public static void giveLoot(Player player, NPC npc, Position pos) {
		int chance = Misc.getRandom(2000);
		int superrare = SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)];
		int rare = RARELOOT[Misc.getRandom(RARELOOT.length - 1)];
		int common = COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)];
		int medium = MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)];

		World.sendMessage("<img=17> @red@<shad=1>"+player.getUsername()+ " @mag@received a loot from the @gre@Wrecked's Girl!");

		GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(19994, Misc.inclusiveRandom(5, 10)), pos, player.getUsername(), false, 150, true, 200));

		boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);
	
		if (chance > 1999) {
			//super rare
			if (isWearingCollector)
			{
				player.addItemToAny(SUPERRARELOOT[Misc.getRandom(SUPERRARELOOT.length - 1)], 1);
				player.addItemToAny(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1);
				player.addItemToAny(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1);
				return;
			}
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(superrare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance > 1995) {
			//rare
			if (isWearingCollector)
			{
				player.addItemToAny(RARELOOT[Misc.getRandom(RARELOOT.length - 1)], 1);
				return;
			}
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(rare), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance > 1750) {
			if (isWearingCollector)
			{
				player.addItemToAny(MEDIUMLOOT[Misc.getRandom(MEDIUMLOOT.length - 1)], 1);
				return;
			}
			//medium
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(medium), pos, player.getUsername(), false, 150, true, 200));
			return;
		}
		
		if (chance >= 0) {
			//common
			if (isWearingCollector)
			{
				player.addItemToAny(COMMONLOOT[Misc.getRandom(COMMONLOOT.length - 1)], 1);
				return;
			}
			GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(common), pos, player.getUsername(), false, 150, true, 200));
			return;
		} 
	}
	
	/**
	 * Handles the drop.
	 * 
	 * @param npc
	 *            The npc.
	 * @param player
	 *            The player.
	 * @param damage
	 *            The damage.
	 */
	private static void handleDrop(NPC npc, Player player, Long damage) {
		Position pos = npc.getPosition();
		giveLoot(player, npc, pos);
	}
	
	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
			
		});

		return sortedEntries;
		
	}

	public static final void loadDrops() {
		Map<Integer, NpcDropItem> items = new HashMap<>();
		
		items.put(19994, new NpcDropItem(19994, new int[] { 5 }, 0));
		
		Arrays.stream(COMMONLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 3)));
		Arrays.stream(MEDIUMLOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 5)));
		Arrays.stream(RARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 8)));
		Arrays.stream(SUPERRARELOOT).filter(i -> !items.containsKey(i)).forEach(i -> items.put(i, new NpcDropItem(i, new int[] { 1 }, 9)));
		
		NPCDrops drops = new NPCDrops();
		drops.setDrops(items.values().toArray(new NpcDropItem[items.size()]));
		
		NPCDrops.getDrops().put(NPC_ID, drops);
	}
	
}

