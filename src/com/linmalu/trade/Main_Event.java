package com.linmalu.trade;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.linmalu.library.api.LinmaluVersion;
import com.linmalu.trade.data.GameData;
import com.linmalu.trade.data.InventoryData;

public class Main_Event implements Listener
{
	private GameData data = Main.getMain().getGameData();

	@EventHandler
	public void Event(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		if(player.isOp())
		{
			LinmaluVersion.check(Main.getMain(), player);
		}
	}
	@EventHandler
	public void Event(PlayerInteractAtEntityEvent event)
	{
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if(player.isSneaking() && entity.getType() == EntityType.PLAYER)
		{
			player.chat("/LinmaluTrade 신청 " + ((Player)entity).getName());
		}
	}
	@EventHandler
	public void Event(InventoryClickEvent event)
	{
		HumanEntity entity = event.getWhoClicked();
		if(entity.getType() == EntityType.PLAYER && checkInventory(event.getInventory()))
		{
			InventoryData id = data.getInventoryData(entity);
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
					id.changeReady(entity);
				}
			}
		}
	}
	@EventHandler
	public void Event(InventoryDragEvent event)
	{
		HumanEntity entity = event.getWhoClicked();
		if(entity.getType() == EntityType.PLAYER && checkInventory(event.getInventory()))
		{
			InventoryData id = data.getInventoryData(entity);
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
	@EventHandler
	public void Event(InventoryCloseEvent event)
	{
		HumanEntity entity = event.getPlayer();
		if(entity.getType() == EntityType.PLAYER && checkInventory(event.getInventory()))
		{
			InventoryData id = data.getInventoryData(entity);
			if(id != null)
			{
				id.cancel();
			}
		}
	}
	private boolean checkInventory(Inventory inventory)
	{
		return inventory.getName().equals(Main.INVENTORY_NAME);
	}
}
