package com.linmalu.trade.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.linmalu.trade.Main;

public class UpdateInventory implements Runnable
{
	private Player player[];

	public UpdateInventory(Player player[])
	{
		this.player = player;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getMain(), this);
	}
	public void run()
	{
		for(int i = 0; i < player.length; i++)
		{
			player[i].updateInventory();
		}
	}
}
