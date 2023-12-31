package com.ruseps.world.content.combat.pvp;

import java.util.ArrayList;
import java.util.List;

import com.ruseps.GameLoader;
import com.ruseps.model.PlayerRights;
import com.ruseps.model.Skill;
import com.ruseps.model.Locations.Location;
import com.ruseps.util.Misc;
import com.ruseps.world.World;
import com.ruseps.world.content.Artifacts;
import com.ruseps.world.content.LoyaltyProgramme;
import com.ruseps.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.ruseps.world.content.combat.weapon.CombatSpecial;
import com.ruseps.world.content.Scoreboards;
import com.ruseps.world.entity.impl.GroundItemManager;
import com.ruseps.world.entity.impl.player.Player;
import com.ruseps.model.GroundItem;
import com.ruseps.model.Item;

public class PlayerKillingAttributes {

	private final Player player;
	private Player target;
	private int playerKills;
	private int playerKillStreak;
	private int playerDeaths;
	private int targetPercentage;
	private long lastPercentageIncrease;
	private int safeTimer;
	
	Location loc;

	private final int WAIT_LIMIT = 2;
	private List<String> killedPlayers = new ArrayList<String>();

	public PlayerKillingAttributes(Player player) {
		this.player = player;
	}

	public void add(Player other) {

		
		if(other.getAppearance().getBountyHunterSkull() >= 0)
			other.getAppearance().setBountyHunterSkull(-1);

		boolean target = player.getPlayerKillingAttributes().getTarget() != null && player.getPlayerKillingAttributes().getTarget().getIndex() == other.getIndex() || other.getPlayerKillingAttributes().getTarget() != null && other.getPlayerKillingAttributes().getTarget().getIndex() == player.getIndex();
		if(target)
			killedPlayers.clear();

		if (killedPlayers.size() >= WAIT_LIMIT) {
			killedPlayers.clear();
			handleReward(other, target);
		} else {
			if (!killedPlayers.contains(other.getUsername()))
				handleReward(other, target);
			else
				player.getPacketSender().sendMessage("You were not given points because you have recently defeated " + other.getUsername() + ".");
		}
		Item item1 = new Item(995, 3000000 + Misc.getRandom(12000000));

		if(target)
			
			GroundItemManager.spawnGroundItem(player, new GroundItem(item1, player.getPosition(),
					player.getUsername(), false, 150, false, 200));
		
			BountyHunter.resetTargets(player, other, true, "You have defeated your target!");
	}

	/**
	 * Gives the player a reward for defeating his opponent
	 * @param other
	 * 
	 */
	private void handleReward(Player o, boolean targetKilled) {
	if (!o.getSerialNumber().equals(player.getSerialNumber()) && !player.getHostAddress().equalsIgnoreCase(o.getHostAddress()) && player.getLocation() == Location.WILDERNESS) {
			if(!killedPlayers.contains(o.getUsername()))
		killedPlayers.add(o.getUsername());
			player.getPacketSender().sendMessage(getRandomKillMessage(o.getUsername()));
		
		
		
			this.playerKillStreak +=1;
		//	this.playerKills += 1;
			
			
		

				/*if (GameLoader.getDay() == null) {
					player.getPointsHandler().setPkPoints(2, true);
					player.getPacketSender().sendMessage("You've received 2 Pk points.");
				} else {
					player.getPointsHandler().setPkPoints(1, true);
					player.getPacketSender().sendMessage("You've received a Pk point.");
				}		*/
			
			
			
			
			Artifacts.handleDrops(player, o, targetKilled);
			if(player.getAppearance().getBountyHunterSkull() < 4)
				player.getAppearance().setBountyHunterSkull(player.getAppearance().getBountyHunterSkull()+1);
			player.getPointsHandler().refreshPanel();
			
			/** ACHIEVEMENTS AND LOYALTY TITLES **/
			LoyaltyProgramme.unlock(player, LoyaltyTitles.KILLER);
			
			if(this.playerKills >= 20) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.SLAUGHTERER);
			} 
			if(this.playerKills >= 50) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.GENOCIDAL);
			}
			
			if(this.playerKillStreak == 3) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 3! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 5) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 5! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 7) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 7! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 10) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 10! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 15) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 15! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 20) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 20! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 25) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 30) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 40) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 50) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 60) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 70) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 75) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 80) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 90) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 100) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 125) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 150) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 175) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 200) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			if(this.playerKillStreak == 250) {
				World.sendMessage("@blu@[Killstreak]@bla@ "+player.getUsername()+ " is on a kill streak of 25! Kill them to end their streak!");
			}
			
			if(this.playerKillStreak >= 15) {
				LoyaltyProgramme.unlock(player, LoyaltyTitles.IMMORTAL);
			}
		}
	}

	public List<String> getKilledPlayers() {
		return killedPlayers;
	}

	public void setKilledPlayers(List<String> list) {
		killedPlayers = list;
	}

	/**
	 * Gets a random message after killing a player
	 * @param killedPlayer 		The player that was killed
	 */
	public static String getRandomKillMessage(String killedPlayer){
		int deathMsgs = Misc.getRandom(8);
		switch(deathMsgs) {
		case 0: return "With a crushing blow, you defeat "+killedPlayer+".";
		case 1: return "It's humiliating defeat for "+killedPlayer+".";
		case 2: return ""+killedPlayer+" didn't stand a chance against you.";
		case 3: return "You've defeated "+killedPlayer+".";
		case 4: return ""+killedPlayer+" regrets the day they met you in combat.";
		case 5: return "It's all over for "+killedPlayer+".";
		case 6: return ""+killedPlayer+" falls before you might.";
		case 7: return "Can anyone defeat you? Certainly not "+killedPlayer+".";
		case 8: return "You were clearly a better fighter than "+killedPlayer+".";
		}
		return null;
	}

	public int getPlayerKills() {
		return playerKills;
	}

	public void setPlayerKills(int playerKills) {
		this.playerKills = playerKills;
	}

	public int getPlayerKillStreak() {
		return playerKillStreak;
	}

	public void setPlayerKillStreak(int playerKillStreak) {
		this.playerKillStreak = playerKillStreak;
	}

	public int getPlayerDeaths() {
		return playerDeaths;
	}

	
	public void setPlayerDeaths(int playerDeaths) {
		this.playerDeaths = playerDeaths;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	public int getTargetPercentage() {
		return targetPercentage;
	}

	public void setTargetPercentage(int targetPercentage) {
		this.targetPercentage = targetPercentage;
	}

	public long getLastTargetPercentageIncrease() {
		return lastPercentageIncrease;
	}

	public void setLastTargetPercentageIncrease(long lastPercentageIncrease) {
		this.lastPercentageIncrease = lastPercentageIncrease;
	}

	public int getSafeTimer() {
		return safeTimer;
	}

	public void setSafeTimer(int safeTimer) {
		this.safeTimer = safeTimer;
	}
}
