package com.linmalu.trade.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.linmalu.trade.Main;

public class PlayerData implements Runnable
{
	private GameData data;
	private int taskId;
	private UUID player1;
	private UUID player2;
	private String name1;
	private String name2;
	private int time;

	public PlayerData(Player player1, Player player2)
	{
		data = Main.getMain().getGameData();
		this.player1 = player1.getUniqueId();
		this.player2 = player2.getUniqueId();
		name1 = player1.getName();
		name2 = player2.getName();
		time = data.time;
		data.playerData.add(this);
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), this, 20L, 20L);
	}
	public void run()
	{
		if(time == 0)
		{
			Player player;
			if((player = Bukkit.getPlayer(player1)) != null)
			{
				player.sendMessage(ChatColor.GOLD + name2 + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
			}
			if((player = Bukkit.getPlayer(player2)) != null)
			{
				player.sendMessage(ChatColor.GOLD + name1 + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
			}
			cancel();
		}
		time--;
	}
	public void cancel()
	{
		Bukkit.getScheduler().cancelTask(taskId);
		data.playerData.remove(this);
	}
	public UUID getPlayer1()
	{
		return player1;
	}
	public UUID getPlayer2()
	{
		return player2;
	}
	public String getName1()
	{
		return name1;
	}
	public String getName2()
	{
		return name2;
	}
	public int getTime()
	{
		return time;
	}
	public boolean isFirst(Player player)
	{
		if(player.getUniqueId() == player1)
		{
			return true;
		}
		return false;
	}
	public boolean checkPlayer(Player player)
	{
		if(player1 == player.getUniqueId() || player2 == player.getUniqueId())
		{
			return true;
		}
		return false;
	}
}
