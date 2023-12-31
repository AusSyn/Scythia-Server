package com.ruseps.world.content.referral;

import com.ruseps.model.Item;

public enum RefferalOptions {
	YOUTUBERS(new String[] {"Wr3ckedYou", "Jipy"}, new Item[] {new Item(18338, 1),new Item(19994, 10)}, RefferalType.YOUTUBERS),
	WEBSITES(new String[] {"Google search", "Reddit", "Rune-server", "Runelocus", "Facebook", "Twitter", "Discord", "",""}, new Item[] {new Item(18338, 1),new Item(19994, 10)}, RefferalType.WEBSITES),
	OTHER(new String[] {"Word of mouth", "","","","","","","","",""}, new Item[] {new Item(18338, 1),new Item(19994, 10)}, RefferalType.OTHER);
	
	private String[] options;
	
	private RefferalType type;
	
	private Item[] rewards;
	
	RefferalOptions(String[] options, Item[] rewards, RefferalType type) {
		this.options = options;
		this.rewards = rewards;
		this.type = type;
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public RefferalType getType() {
		return type;
	}
	
	public Item[] getRewards() {
		return rewards;
	}

}
