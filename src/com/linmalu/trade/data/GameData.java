package com.linmalu.trade.data;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameData
{
	public final String inventoryName = ChatColor.BLACK + "거래플러그인 - 린마루";
	public final int time = 30;
	public final int distance = 20;
	public LinkedList<PlayerData> playerData = new LinkedList<>();
	public LinkedList<InventoryData> inventoryData = new LinkedList<>();

	public PlayerData getPlayerData(Player player)
	{
		for(PlayerData pd : playerData)
		{
			if(pd.checkPlayer(player))
			{
				return pd;
			}
		}
		return null;
	}
	public InventoryData getInventoryData(Player player)
	{
		for(InventoryData id : inventoryData)
		{
			if(id.checkPlayer(player))
			{
				return id;
			}
		}
		return null;
	}
}
