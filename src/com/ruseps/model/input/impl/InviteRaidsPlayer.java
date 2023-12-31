package com.ruseps.model.input.impl;

import com.ruseps.model.input.Input;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

public class InviteRaidsPlayer extends Input {

	@Override
	public void handleSyntax(Player player, String plrToInvite) {

		//player.getPacketSender().sendInterfaceRemoval();
		Player invite = World.getPlayerByName(plrToInvite);
		if(invite == null) {
			player.getPacketSender().sendMessage("That player is currently not online.");
			return;
		}
		if(player.getMinigameAttributes().getRaidsAttributes().getParty().getPlayers().contains(invite)) {
			player.getPacketSender().sendMessage("That player is already in your party.");
			return;
		}
		player.sendMessage("Sent attemptInvite to " + plrToInvite);

		player.getMinigameAttributes().getRaidsAttributes().getParty().invite(invite);

	}
}
