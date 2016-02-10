package com.linmalu.trade;

import org.bukkit.ChatColor;

import com.linmalu.library.api.LinmaluMain;
import com.linmalu.trade.data.GameData;

public class Main extends LinmaluMain
{
	public static final String INVENTORY_NAME = ChatColor.BLACK + "LinmaluTrade - 린마루(Linmalu)";

	private GameData gamedata;

	public static Main getMain()
	{
		return (Main) LinmaluMain.getMain();
	}

	@Override
	public void onEnable()
	{
		gamedata = new GameData();
		registerCommand(new Main_Command());
		registerEvents(new Main_Event());
		getLogger().info("제작 : 린마루(Linmalu)");
	}
	@Override
	public void onDisable()
	{
		getLogger().info("제작 : 린마루(Linmalu)");
	}
	public GameData getGameData()
	{
		return gamedata;
	}
}
