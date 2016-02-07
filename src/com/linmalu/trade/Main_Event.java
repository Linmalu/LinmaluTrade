package com.linmalu.trade;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.linmalu.library.api.LinmaluVersion;
import com.linmalu.trade.data.GameData;
import com.linmalu.trade.data.InventoryData;

public class Main_Event implements Listener
{
	private GameData data = Main.getMain().getGameData();

	@EventHandler
	public void Event(PlayerInteractEntityEvent event)
	{
	}
	@EventHandler
	public void Event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluVersion.check(Main.getMain(), player, Main.getMain().getTitle() + ChatColor.GREEN + "최신버전이 존재합니다.");
		}
	}
	@EventHandler
	public void Event(InventoryCloseEvent event)
	{
		Player player;
		if(event.getPlayer().getType() == EntityType.PLAYER && event.getInventory().getName().equals(data.inventoryName))
		{
			player = (Player)event.getPlayer();
			InventoryData id = data.getInventoryData(player);
			if(id != null)
			{
				id.cancel();
			}
		}
	}
	@EventHandler
	public void Event(InventoryClickEvent event)
	{
		Player player;
		if(event.getWhoClicked().getType() == EntityType.PLAYER && event.getInventory().getName().equals(data.inventoryName))
		{
			player = (Player)event.getWhoClicked();
			InventoryData id = data.getInventoryData(player);
			if(id != null)
			{
				int slot = event.getRawSlot();
				if(slot < 9)
				{
					id.changeItem();
				}
				else if(9 <= slot && slot < 45)
				{
					event.setCancelled(true);
				}
				else if(45 <= slot && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
				{
					ItemStack[] items = event.getInventory().getContents();
					boolean item = false;
					for(int i = 0; i < 9; i++)
					{
						if(items[i] == null)
						{
							item = true;
						}
					}
					if(!item)
					{
						event.setCancelled(true);
					}
				}
				if(38 <= slot && slot <= 42)
				{
					id.toggleReady(player);
				}
			}
		}
	}
	@EventHandler
	public void Event(InventoryDragEvent event)
	{
		Player player;
		if(event.getWhoClicked().getType() == EntityType.PLAYER && event.getInventory().getName().equals(data.inventoryName))
		{
			player = (Player)event.getWhoClicked();
			InventoryData id = data.getInventoryData(player);
			if(id != null)
			{
				for(int slot : event.getRawSlots())
				{
					if(9 <= slot && slot < 45)
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
