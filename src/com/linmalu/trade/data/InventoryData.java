package com.linmalu.trade.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.linmalu.library.api.LinmaluActionbar;
import com.linmalu.library.api.LinmaluTitle;
import com.linmalu.trade.Main;

public class InventoryData implements Runnable
{
	private final int taskId;
	private final Player[] players;
	private final GameData data = Main.getMain().getGameData();;
	private final Inventory inventorys[] = new Inventory[] {Bukkit.createInventory(null, 9 * 5, Main.INVENTORY_NAME), Bukkit.createInventory(null, 9 * 5, Main.INVENTORY_NAME)};;
	private int time = data.getTime() * 20;
	private boolean readys[] = {false, false};

	public InventoryData(Player ... players)
	{
		this.players = players;
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getMain(), this, 0L, 1L);
	}
	public void run()
	{
		switch(time)
		{
		case 0:
			if(getPlayer1().isOnline())
			{
				getPlayer1().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName2() + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
				LinmaluActionbar.sendMessage(getPlayer1(), Main.getMain().getTitle() + ChatColor.GOLD + getName2() + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
				LinmaluTitle.sendMessage(getPlayer1(), ChatColor.GREEN + "시간초과 " + ChatColor.GOLD + getName2(), Main.getMain().getTitle(), 20, 20, 20);
			}
			if(getPlayer2().isOnline())
			{
				getPlayer2().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName1() + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
				LinmaluActionbar.sendMessage(getPlayer2(), Main.getMain().getTitle() + ChatColor.GOLD + getName1() + ChatColor.GREEN + "님과의 거래가 시간초과로 취소되었습니다.");
				LinmaluTitle.sendMessage(getPlayer2(), ChatColor.GREEN + "시간초과 " + ChatColor.GOLD + getName1(), Main.getMain().getTitle(), 20, 20, 20);
			}
			cancel();
			break;
		case -1:
			checkInventory();
			if(readys[0] && readys[1])
			{
				cancel();
			}
			break;
		default :
			if(time % 20 == 0)
			{
				LinmaluActionbar.sendMessage(getPlayer1(), Main.getMain().getTitle() + ChatColor.GOLD + getName2() + ChatColor.GREEN + "님과의 거래 대기시간 : " + ChatColor.GOLD + (time / 20) + ChatColor.YELLOW + "초");
				LinmaluActionbar.sendMessage(getPlayer2(), Main.getMain().getTitle() + ChatColor.GOLD + getName1() + ChatColor.GREEN + "님과의 거래 대기시간 : " + ChatColor.GOLD + (time / 20) + ChatColor.YELLOW + "초");
			}
			if(getPlayer1().isOnline() && getPlayer2().isOnline())
			{
				time--;
			}
			else
			{
				time = 0;
			}
			break;
		}
	}
	private void checkInventory()
	{
		for(Player player : players)
		{
			int n1 = isFirst(player) ? 0 : 1;
			int n2 = isFirst(player) ? 1 : 0;
			for(int i = 9; i < inventorys[n1].getSize(); i++)
			{
				ItemStack item;
				if(i / 9 == 2)
				{
					item = inventorys[n2].getItem(i - 18);
				}
				else if(i / 9 == 4 && 2 <= i % 9 && i % 9 <= 6)
				{
					if(readys[n1])
					{
						item = getItemStack(Material.GLOWSTONE, ChatColor.GREEN + "거래 완료 버튼 " + ChatColor.YELLOW + "= O =");
					}
					else
					{
						item = getItemStack(Material.SOUL_SAND, ChatColor.GREEN + "거래 완료 버튼 " + ChatColor.YELLOW + "= X =");
					}
				}
				else if(i / 9 == 4 && (i % 9 == 0 || i % 9 == 8))
				{
					if(readys[n2])
					{
						item = getItemStack(Material.GLOWSTONE, ChatColor.GOLD + players[n2].getName() + ChatColor.GREEN + " 거래 완료 버튼 " + ChatColor.YELLOW + "= O =");
					}
					else
					{
						item = getItemStack(Material.SOUL_SAND, ChatColor.GOLD + players[n2].getName() + ChatColor.GREEN + " 거래 완료 버튼 " + ChatColor.YELLOW + "= X =");
					}
				}
				else
				{
					item = getItemStack(Material.STAINED_GLASS_PANE, Main.getMain().getTitle());
					item.setDurability((short)15);
				}
				inventorys[n1].setItem(i, item);
			}
		}
	}
	private ItemStack getItemStack(Material type, String name)
	{
		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	public void changeReady(HumanEntity entity)
	{
		if(isFirst(entity))
		{
			readys[0] = !readys[0];
		}
		else
		{
			readys[1] = !readys[1];
		}
	}
	public void changeItem()
	{
		readys[0] = readys[1] = false;
	}
	public void cancel()
	{
		data.removeInventoryData(this);
		if(time == -1)
		{
			if(readys[0] == true && readys[1] == true)
			{
				for(int i = 0; i < 9; i++)
				{
					if((inventorys[1].getItem(i)) != null)
					{
						for(ItemStack item : getPlayer1().getInventory().addItem(inventorys[1].getItem(i)).values())
						{
							getPlayer1().getWorld().dropItem(getPlayer1().getLocation(), item);
						}
					}
					if((inventorys[0].getItem(i)) != null)
					{
						for(ItemStack item : getPlayer2().getInventory().addItem(inventorys[0].getItem(i)).values())
						{
							getPlayer2().getWorld().dropItem(getPlayer2().getLocation(), item);
						}
					}
				}
				getPlayer1().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName2() + ChatColor.GREEN + "님과 거래가 완료되었습니다.");
				getPlayer2().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName1() + ChatColor.GREEN + "님과 거래가 완료되었습니다.");
				LinmaluTitle.sendMessage(getPlayer1(), ChatColor.GREEN + "거래완료 " + ChatColor.GOLD + getName2(), Main.getMain().getTitle(), 20, 20, 20);
				LinmaluTitle.sendMessage(getPlayer2(), ChatColor.GREEN + "거래완료 " + ChatColor.GOLD + getName1(), Main.getMain().getTitle(), 20, 20, 20);
			}
			else
			{
				for(int i = 0; i < 9; i++)
				{
					if((inventorys[0].getItem(i)) != null)
					{
						players[0].getInventory().addItem(inventorys[0].getItem(i));
					}
					if((inventorys[1].getItem(i)) != null)
					{
						players[1].getInventory().addItem(inventorys[1].getItem(i));
					}
				}
				getPlayer1().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName2() + ChatColor.YELLOW + "님과 거래가 취소되었습니다.");
				getPlayer2().sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + getName1() + ChatColor.YELLOW + "님과 거래가 취소되었습니다.");
				LinmaluTitle.sendMessage(getPlayer1(), ChatColor.GREEN + "거래취소 " + ChatColor.GOLD + getName2(), Main.getMain().getTitle(), 20, 20, 20);
				LinmaluTitle.sendMessage(getPlayer2(), ChatColor.GREEN + "거래취소 " + ChatColor.GOLD + getName1(), Main.getMain().getTitle(), 20, 20, 20);
			}
			for(int i = 0; i < players.length; i++)
			{
				inventorys[i].clear();
			}
			for(Player player : players)
			{
				player.closeInventory();
			}
		}
		Bukkit.getScheduler().cancelTask(taskId);
	}
	public void openInventory()
	{
		for(int i = 0; i < players.length; i++)
		{
			players[i].openInventory(inventorys[i]);
		}
		time = -1;
	}
	public Player getPlayer1()
	{
		return players[0];
	}
	public Player getPlayer2()
	{
		return players[1];
	}
	public String getName1()
	{
		return players[0].getName();
	}
	public String getName2()
	{
		return players[1].getName();
	}
	public boolean isFirst(HumanEntity entity)
	{
		return players[0] == entity;
	}
	public boolean isPlayer(HumanEntity entity)
	{
		for(Player p : players)
		{
			if(p == entity)
			{
				return true;
			}
		}
		return false;
	}
}
