package com.linmalu.trade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.linmalu.library.api.LinmaluTellraw;
import com.linmalu.library.api.LinmaluVersion;
import com.linmalu.trade.data.GameData;
import com.linmalu.trade.data.InventoryData;
import com.linmalu.trade.data.PlayerData;

public class Main_Command implements CommandExecutor
{
	public Main_Command()
	{
		Main.getMain().getCommand(Main.getMain().getDescription().getName()).setTabCompleter(new TabCompleter()
		{			
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
			{
				if(args.length == 1)
				{
					ArrayList<String> list = new ArrayList<>();
					list.add("신청");
					list.add("취소");
					list.add("수락");
					list.add("거절");
					list.add("상태");
					return list;
				}
				return null;
			}
		});
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String args[])
	{
		if(sender instanceof Player)
		{
			GameData data = Main.getMain().getGameData();
			Player player = (Player)sender;
			PlayerData pd = data.getPlayerData(player);
			if(args.length == 2 && args[0].equals("신청"))
			{
				Player tPlayer = Bukkit.getPlayerExact(args[1]);
				if(tPlayer != null)
				{
					if(player.getUniqueId() == tPlayer.getUniqueId())
					{
						player.sendMessage(ChatColor.YELLOW + "자신에게는 거래를 할 수 없습니다.");
					}
					else if(pd != null && pd.isFirst(player))
					{
						player.sendMessage(ChatColor.YELLOW + "이미 " + ChatColor.GOLD + pd.getName2() + ChatColor.YELLOW + "님에게 거래를 신청했습니다.");
					}
					else if(pd != null && !pd.isFirst(player))
					{
						player.sendMessage(ChatColor.YELLOW + "이미 " + ChatColor.GOLD + pd.getName1() + ChatColor.YELLOW + "님에게 거래신청을 받았습니다.");
					}
					else if(data.getInventoryData(tPlayer) != null)
					{
						player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님은 이미 거래중입니다.");
					}
					else if(player.getWorld() != tPlayer.getWorld() || player.getLocation().distance(tPlayer.getLocation()) > data.distance)
					{
						player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님과의 거리가 멉니다.");
					}
					else
					{
						player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.GREEN + "님에게 거래를 신청했습니다.");
						player.sendMessage(ChatColor.GRAY + "※ 인벤토리이 꽉찼을 경우에는 아이템이 사라집니다. ※");
						tPlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + "님에게 거래신청이 들어왔습니다.");
						tPlayer.sendMessage(ChatColor.GRAY + "※ 인벤토리이 꽉찼을 경우에는 아이템이 사라집니다. ※");
						tPlayer.sendMessage(ChatColor.GOLD + "/거래 수락 " + ChatColor.WHITE + "또는" + ChatColor.GOLD + " /거래 거절");
						new LinmaluTellraw("$C:" + ChatColor.GOLD + "/거래 수락|/거래 수락|$" + ChatColor.WHITE + " 또는 " + "$C:" + ChatColor.GOLD + "/거래 거절|/거래 거절$").changeCmd().sendMessage(tPlayer);
						new PlayerData(player, tPlayer);
					}
				}
				else
				{
					player.sendMessage(ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님은 없습니다.");
				}
			}
			else if(args.length == 1 && args[0].equals("취소"))
			{
				if(pd == null)
				{
					player.sendMessage(ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					if(pd.isFirst(player))
					{
						player.sendMessage(ChatColor.GOLD + pd.getName2() + ChatColor.GREEN + "님과의 거래를 취소했습니다.");
						Player tPlayer = Bukkit.getPlayer(pd.getPlayer2());
						if(tPlayer != null)
						{
							tPlayer.sendMessage(ChatColor.GOLD + pd.getName1() + ChatColor.GREEN + "님이 거래를 취소했습니다.");
						}
						pd.cancel();
					}
					else
					{
						player.sendMessage(ChatColor.YELLOW + "거래를 신청한적이 없습니다.");
					}
				}
			}
			else if(args.length == 1 && args[0].equals("수락"))
			{
				if(pd == null)
				{
					player.sendMessage(ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					if(pd.isFirst(player))
					{
						player.sendMessage(ChatColor.YELLOW + "거래를 받은적이 없습니다.");
					}
					else
					{
						new InventoryData(pd);
					}
				}
			}
			else if(args.length == 1 && args[0].equals("거절"))
			{
				if(pd == null)
				{
					player.sendMessage(ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					if(pd.isFirst(player))
					{
						player.sendMessage(ChatColor.YELLOW + "거래를 받은적이 없습니다.");
					}
					else
					{
						player.sendMessage(ChatColor.GOLD + pd.getName1() + ChatColor.GREEN + "님과의 거래를 거절했습니다.");
						Player tPlayer = Bukkit.getPlayer(pd.getPlayer1());
						if(tPlayer != null)
						{
							tPlayer.sendMessage(ChatColor.GOLD + pd.getName2() + ChatColor.GREEN + "님이 거래를 거절했습니다.");
						}
						pd.cancel();
					}
				}
			}
			else if(args.length == 1 && args[0].equals("상태"))
			{
				if(pd == null)
				{
					player.sendMessage(ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					if(pd.isFirst(player))
					{
						player.sendMessage(ChatColor.GOLD + pd.getName2() + ChatColor.GREEN + "님에게 거래를 신청했습니다.");
					}
					else
					{
						player.sendMessage(ChatColor.GOLD + pd.getName1() + ChatColor.GREEN + "님에게 거래신청이 들어왔습니다.");
					}
					player.sendMessage(ChatColor.GOLD + "남은시간 : " + ChatColor.YELLOW + pd.getTime() + "초");
				}
			}
			else
			{
				sender.sendMessage(ChatColor.GREEN + " = = = = = [ Linmalu GUI Trade ] = = = = =");
				LinmaluTellraw.sendCmdChat(sender, "/" + label + " 신청 ", ChatColor.GOLD + "/" + label + " 신청 <플레이어>" + ChatColor.GRAY + " : 플레이어에게 거래신청을 합니다.");
				LinmaluTellraw.sendCmdChat(sender, "/" + label + " 취소 ", ChatColor.GOLD + "/" + label + " 취소" + ChatColor.GRAY + " : 거래신청을 취소합니다.");
				LinmaluTellraw.sendCmdChat(sender, "/" + label + " 수락 ", ChatColor.GOLD + "/" + label + " 수락" + ChatColor.GRAY + " : 거래신청을 수락합니다.");
				LinmaluTellraw.sendCmdChat(sender, "/" + label + " 거절 ", ChatColor.GOLD + "/" + label + " 거절" + ChatColor.GRAY + " : 거래신청을 거절합니다.");
				LinmaluTellraw.sendCmdChat(sender, "/" + label + " 상태 ", ChatColor.GOLD + "/" + label + " 상태" + ChatColor.GRAY + " : 자신의 거래상태를 봅니다.");
				sender.sendMessage(ChatColor.YELLOW + "제작자 : " + ChatColor.AQUA + "린마루(Linmalu)" + ChatColor.WHITE + " - http://blog.linmalu.com");
				sender.sendMessage(ChatColor.YELLOW + "카페 : " + ChatColor.WHITE + "http://cafe.naver.com/craftproducer");
				LinmaluVersion.check(Main.getMain(), sender, Main.getMain().getTitle() + ChatColor.GREEN + "최신버전이 존재합니다.");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "콘솔에서는 사용할 수 없습니다.");
		}
		return true;
	}
}
