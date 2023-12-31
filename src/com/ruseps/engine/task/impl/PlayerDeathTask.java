package com.ruseps.engine.task.impl;

import java.util.ArrayList;

import java.util.concurrent.CopyOnWriteArrayList;

import com.ruseps.GameSettings;
import com.ruseps.engine.task.Task;
import com.ruseps.model.Animation;
import com.ruseps.model.Flag;
import com.ruseps.model.GameMode;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;
import com.ruseps.model.Locations.Location;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Position;
import com.ruseps.model.Skill;
import com.ruseps.util.Misc;
import com.ruseps.util.RandomUtility;
import com.ruseps.world.World;
import com.ruseps.world.content.ItemsKeptOnDeath;
import com.ruseps.world.content.battle_royale.BattleRoyale;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.npc.NPC;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.GameLoader;

/**
 * Represents a player's death task, through which the process of dying is handled,
 * the animation, dropping items, etc.
 * 
 * @author relex lawl, redone by Gabbe.
 */

public class PlayerDeathTask extends Task {

	public void handleBattleRoyalePlayerDeath(Player player, Player killer) {
		if(player.getLocation() == Location.MELEE_BATTLE_ROYALE) {
			killer.setSpecialPercentage(100);
	        CombatSpecial.updateBar(killer);
			} else {
				player.getPointsHandler().incrementBattleRoyalPoints(1, dropItems);	
			}
		}
	/**
	 * The PlayerDeathTask constructor.
	 * @param player	The player setting off the task.
	 */
	public PlayerDeathTask(Player player) {
		super(1, player, false);
		this.player = player;
	}

	private Player player;
	private int ticks = 5;
	private boolean dropItems = true;
	Position oldPosition;
	Location loc;
	ArrayList<Item> itemsToKeep = null; 
	NPC death;

	@Override
	public void execute() {
		if(player == null) {
			stop();
			return;
		}
		try {
			switch (ticks) {
			case 5:
				player.getPacketSender().sendInterfaceRemoval();
				player.getMovementQueue().setLockMovement(true).reset();
				break;
			case 3:
				Player killerr = player.getCombatBuilder().getKiller(true);
				handleBattleRoyalePlayerDeath(player, killerr);
				player.performAnimation(new Animation(0x900));
				player.getPacketSender().sendMessage("Oh dear, you are dead!");
				break;
			case 1:
					
				this.oldPosition = player.getPosition().copy();
				this.loc = player.getLocation();
				if(loc != Location.DUNGEONEERING && loc != Location.PEST_CONTROL_GAME && loc != Location.DUEL_ARENA && loc != Location.FREE_FOR_ALL_ARENA && loc != Location.FREE_FOR_ALL_WAIT && loc != Location.SOULWARS && loc != Location.FIGHT_PITS && loc != Location.FIGHT_PITS_WAIT_ROOM && loc != Location.FIGHT_CAVES && loc != Location.RECIPE_FOR_DISASTER && loc != Location.GRAVEYARD) {
					Player killer = player.getCombatBuilder().getKiller(true);
					if(player.getUsername().equalsIgnoreCase("umran") || player.getRights().equals(PlayerRights.OWNER) || player.getRights().equals(PlayerRights.DEVELOPER))
						dropItems = false;
					if(loc == Location.WILDERNESS) {
						if(killer != null && (killer.getRights().equals(PlayerRights.OWNER) || killer.getRights().equals(PlayerRights.DEVELOPER)))
							dropItems = false;
					}
					
					if(loc != Location.WILDERNESS) {
						dropItems = false;
					}
					
					if(killer != null) {
						if(killer.getRights().equals(PlayerRights.OWNER) || killer.getRights().equals(PlayerRights.DEVELOPER)) {
							dropItems = false;
						}
					}
					
					if(player.getInstanceManager().inInstance) {
	                	player.getInstanceManager().endInstance(player, true);
	                }
					
					boolean spawnItems = loc != Location.NOMAD && !(loc == Location.GODWARS_DUNGEON && player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom());
					if(killer != null && loc == Location.WILDERNESS && (killer.getGameMode() == GameMode.IRONMAN || killer.getGameMode() == GameMode.HARDCORE_IRONMAN)) {
						spawnItems = false;
					}
					if(dropItems) {
						itemsToKeep = ItemsKeptOnDeath.getItemsToKeep(player);
						final CopyOnWriteArrayList<Item> playerItems = new CopyOnWriteArrayList<Item>();
						playerItems.addAll(player.getInventory().getValidItems());
						playerItems.addAll(player.getEquipment().getValidItems());
						final Position position = player.getPosition();
						for (Item item : playerItems) {
							if(!item.tradeable() || itemsToKeep.contains(item)) {
								if(!itemsToKeep.contains(item)) {
									itemsToKeep.add(item);
								}
								continue;
							}
							if(spawnItems) {
								if(item != null && item.getId() > 0 && item.getAmount() > 0) {
									GroundItemManager.spawnGroundItem((killer != null && killer.getGameMode() == GameMode.NORMAL ? killer : player), new GroundItem(item, position, killer != null ? killer.getUsername() : player.getUsername(), player.getHostAddress(), false, 150, true, 150));
								}
							}
						}
						if (killer != null) {

							killer.getPA().sendMessage("gothereee");
							killer.getPlayerKillingAttributes().add(player);
							killer.getPlayerKillingAttributes()
									.setPlayerKills(killer.getPlayerKillingAttributes().getPlayerKills()+1);
							killer.getPointsHandler().refreshPanel();
							
							
							player.getPlayerKillingAttributes()
									.setPlayerDeaths(player.getPlayerKillingAttributes().getPlayerDeaths() + 1);
							player.getPlayerKillingAttributes().setPlayerKillStreak(0);
							player.getPointsHandler().refreshPanel();
						}
						player.getInventory().resetItems().refreshItems();
						player.getEquipment().resetItems().refreshItems();
					}
				} else
					dropItems = false;
				player.getPacketSender().sendInterfaceRemoval();
				player.setEntityInteraction(null);
				player.getMovementQueue().setFollowCharacter(null);
				player.getCombatBuilder().cooldown(false);
				player.setTeleporting(false);
				player.setWalkToTask(null);
				player.getSkillManager().stopSkilling();
				break;
			case 0:
				if(dropItems) {
					if(player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
						//GameMode.set(player, player.getGameMode(), true);
					} else { 
						if(itemsToKeep != null) {
							for(Item it : itemsToKeep) {
								player.getInventory().add(it.getId(), 1);
							}
							itemsToKeep.clear();
						}
					}
				}
				if(death != null) {
					World.deregister(death);
				}
				player.restart();
				player.getUpdateFlag().flag(Flag.APPEARANCE);
				loc.onDeath(player);
				if(loc != Location.DUNGEONEERING) {
					if(player.getPosition().equals(oldPosition))
						player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				}
				player = null;
				oldPosition = null;
				stop();
				break;
			}
			ticks--;
		} catch(Exception e) {
			setEventRunning(false);
			e.printStackTrace();
			if(player != null) {
				player.moveTo(GameSettings.DEFAULT_POSITION.copy());
				player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION));
			}	
		}
	}

	public static NPC getDeathNpc(Player player) {
		NPC death = new NPC(2862, new Position(player.getPosition().getX() + 1, player.getPosition().getY() + 1));
		World.register(death);
		death.setEntityInteraction(player);
		death.performAnimation(new Animation(401));
		death.forceChat(randomDeath(player.getUsername()));
		return death;
	}
	

	public static String randomDeath(String name) {
		switch (RandomUtility.getRandom(8)) {
		case 0:
			return "There is no escape, " + Misc.formatText(name)
					+ "...";
		case 1:
			return "Muahahahaha!";
		case 2:
			return "You belong to me!";
		case 3:
			return "Beware mortals, " + Misc.formatText(name)
					+ " travels with me!";
		case 4:
			return "Your time here is over, " + Misc.formatText(name)
					+ "!";
		case 5:
			return "Now is the time you die, " + Misc.formatText(name)
					+ "!";
		case 6:
			return "I claim " + Misc.formatText(name) + " as my own!";
		case 7:
			return "" + Misc.formatText(name) + " is mine!";
		case 8:
			return "Let me escort you back to Edgeville, "
					+ Misc.formatText(name) + "!";
		case 9:
			return "I have come for you, " + Misc.formatText(name)
					+ "!";
		}
		return "";
	}

}
