package com.linmalu.trade.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.linmalu.trade.Main;

public class InventoryData implements Runnable
{
	private GameData data;
	private int taskId;
	private Player player[];
	private String name[];
	private Inventory inventory[];
	private boolean ready[] = {false, false};
	
	public InventoryData(PlayerData pd)
	{
		data = Main.getMain().getGameData();
		player = new Player[] {Bukkit.getPlayer(pd.getPlayer1()), Bukkit.getPlayer(pd.getPlayer2())};
		name = new String[] {pd.getName1(), pd.getName2()};
		if(player[0] == null || player[1] == null)
		{
			sendMessage();
			return;
		}
		inventory = new Inventory[] {Bukkit.createInventory(null, 9 * 5, ChatColor.BLACK + "거래플러그인 - 린마루"), Bukkit.createInventory(null, 9 * 5, ChatColor.BLACK + "거래플러그인 - 린마루")};
		data.inventoryData.add(this);
		pd.cancel();
		for(int i = 0; i < player.length; i++)
		{
			player[i].openInventory(inventory[i]);
		}
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), this, 0L, 1L);
	}
	public void run()
	{
		checkInventory(0, 1);
		checkInventory(1, 0);
		if(ready[0] == true && ready[1] == true)
		{
			cancel();
		}
	}
	private void checkInventory(int p1, int p2)
	{
		ItemStack item;
		ItemMeta meta;
		for(int i = 9; i < inventory[p1].getSize(); i++)
		{
			item = new ItemStack(Material.STAINED_GLASS_PANE);
			item.setDurability((short)15);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "거래 : " + ChatColor.YELLOW + name[p1] + " - " + name[p2]);
			item.setItemMeta(meta);
			if(i / 9 == 1 || i / 9 == 3)
			{
			}
			else if(i / 9 == 4)
			{
				if(i % 9 == 0 || i % 9 == 8)
				{
					if(!ready[p2])
					{
						item = new ItemStack(Material.SOUL_SAND);
						meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + "상대방 거래 완료 버튼 상태 " + ChatColor.YELLOW + "= X =");
						item.setItemMeta(meta);
					}
					else
					{
						item = new ItemStack(Material.GLOWSTONE);
						meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + "상대방 거래 완료 버튼 상태 " + ChatColor.YELLOW + "= O =");
						item.setItemMeta(meta);
					}
				}
				else if(2 <= i % 9 && i % 9 <= 6)
				{
					if(!ready[p1])
					{
						item = new ItemStack(Material.SOUL_SAND);
						meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + "거래 완료 버튼 " + ChatColor.YELLOW + "= X =");
						item.setItemMeta(meta);
					}
					else
					{
						item = new ItemStack(Material.GLOWSTONE);
						meta = item.getItemMeta();
						meta.setDisplayName(ChatColor.GREEN + "거래 완료 버튼 " + ChatColor.YELLOW + "= O =");
						item.setItemMeta(meta);
					}
				}
				else
				{
				}
			}
			else
			{
				item = inventory[p2].getItem(i - 18);
				if(item == null)
				{
					item = new ItemStack(Material.AIR);
				}
			}
			inventory[p1].setItem(i, item);
		}
	}
	public void toggleReady(Player player)
	{
		if(this.player[0] == player)
		{
			toggleReady(0);
		}
		else
		{
			toggleReady(1);
		}
	}
	private void toggleReady(int player)
	{
		if(!ready[player])
		{
			ready[player] = true;
		}
		else
		{
			ready[player] = false;
		}
	}
	public void changeItem()
	{
		for(int i = 0; i < player.length; i++)
		{
			ready[i] = false;
		}
	}
	public void cancel()
	{
		tradeItem();
		sendMessage();
		data.inventoryData.remove(this);
		Bukkit.getScheduler().cancelTask(taskId);
		for(int i = 0; i < player.length; i++)
		{
			player[i].closeInventory();
		}
	}
	private void tradeItem()
	{
		if(ready[0] == true && ready[1] == true)
		{
			for(int i = 0; i < 9; i++)
			{
				ItemStack item;
				if((item = inventory[1].getItem(i)) != null)
				{
					player[0].getInventory().addItem(item);
				}
				if((item = inventory[0].getItem(i)) != null)
				{
					player[1].getInventory().addItem(item);
				}
			}
		}
		else
		{
			for(int i = 0; i < 9; i++)
			{
				ItemStack item;
				if((item = inventory[0].getItem(i)) != null)
				{
					player[0].getInventory().addItem(item);
				}
				if((item = inventory[1].getItem(i)) != null)
				{
					player[1].getInventory().addItem(item);
				}
			}
		}
		for(int i = 0; i < player.length; i++)
		{
			inventory[i].clear();
		}
		new UpdateInventory(player);
	}
	private void sendMessage()
	{
		if(ready[0] == true && ready[1] == true)
		{
			player[0].sendMessage(ChatColor.GOLD + name[1] + ChatColor.GREEN + "님과 거래가 완료되었습니다.");
			player[1].sendMessage(ChatColor.GOLD + name[0] + ChatColor.GREEN + "님과 거래가 완료되었습니다.");
		}
		else
		{
			player[0].sendMessage(ChatColor.GOLD + name[1] + ChatColor.YELLOW + "님과 거래가 완료되었습니다.");
			player[1].sendMessage(ChatColor.GOLD + name[0] + ChatColor.YELLOW + "님과 거래가 완료되었습니다.");
		}
	}
	public boolean checkPlayer(Player player)
	{
		for(int i = 0; i < this.player.length; i++)
		{
			if(this.player[i] == player)
			{
				return true;
			}
		}
		return false;
	}
}
