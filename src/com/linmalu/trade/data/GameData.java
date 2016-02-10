package com.linmalu.trade.data;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.linmalu.library.api.LinmaluYamlConfiguration;
import com.linmalu.trade.Main;

public class GameData
{
	private final File file = new File(Main.getMain().getDataFolder(), "config.yml");
	private final CopyOnWriteArrayList<InventoryData> inventoryDatas = new CopyOnWriteArrayList<>();
	private final String DISTANCE = "거리";
	private final String TIME = "시간";
	private int distance = 20;
	private int time = 30;

	public GameData()
	{
		reloadConfig();
	}
	public void reloadConfig()
	{
		LinmaluYamlConfiguration config = LinmaluYamlConfiguration.loadConfiguration(file);
		if(!file.exists())
		{
			config.set(DISTANCE, 20);
			config.set(TIME, 30);
		}
		distance = config.getInt(DISTANCE, distance);
		time = config.getInt(TIME, time);
		try
		{
			config.save(file);
		}
		catch(IOException e){}
	}
	public InventoryData getInventoryData(HumanEntity entity)
	{
		for(InventoryData id : inventoryDatas)
		{
			if(id.isPlayer(entity))
			{
				return id;
			}
		}
		return null;
	}
	public void addInventoryData(Player player1, Player player2)
	{
		inventoryDatas.add(new InventoryData(player1, player2));
	}
	public void removeInventoryData(InventoryData id)
	{
		inventoryDatas.remove(id);
	}
	public int getDistance()
	{
		return distance;
	}
	public int getTime()
	{
		return time;
	}
}
