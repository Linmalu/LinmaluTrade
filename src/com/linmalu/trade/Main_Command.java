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
import com.linmalu.library.api.LinmaluTitle;
import com.linmalu.library.api.LinmaluVersion;
import com.linmalu.trade.data.GameData;
import com.linmalu.trade.data.InventoryData;

public class Main_Command implements CommandExecutor
{
	public Main_Command()
	{
		Main.getMain().getCommand(Main.getMain().getDescription().getName()).setTabCompleter(new TabCompleter()
		{
			@Override
			public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
			{
				ArrayList<String> list = new ArrayList<>();
				if(args.length == 1)
				{
					list.add("신청");
					list.add("수락");
					list.add("취소");
					if(sender.isOp())
					{
						list.add("리로드");
						list.add("reload");
					}
				}
				else if(args.length == 2 && args[0].equals("신청"))
				{
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(sender != player)
						{
							list.add(player.getName());
						}
					}
				}
				return list;
			}
		});
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String args[])
	{
		if(sender instanceof Player)
		{
			GameData data = Main.getMain().getGameData();
			Player player = (Player)sender;
			InventoryData id = data.getInventoryData(player);
			if(args.length == 2 && args[0].equals("신청"))
			{
				Player target = Bukkit.getPlayerExact(args[1]);
				if(target != null)
				{
					if(player == target)
					{
						player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "자신에게는 거래를 할 수 없습니다.");
					}
					else if(id != null)
					{
						if(id == data.getInventoryData(target) && id.isFirst(target))
						{
							id.openInventory();
						}
						else if(id.isFirst(player))
						{
							player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "이미 " + ChatColor.GOLD + id.getName2() + ChatColor.YELLOW + "님에게 거래를 신청했습니다.");
						}
						else
						{
							player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "이미 " + ChatColor.GOLD + id.getName1() + ChatColor.YELLOW + "님에게 거래신청을 받았습니다.");
						}
					}
					else if(data.getInventoryData(target) != null)
					{
						player.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님은 이미 거래중입니다.");
					}
					else if(player.getWorld() != target.getWorld() || player.getLocation().distance(target.getLocation()) > data.getDistance())
					{
						player.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님과의 거리가 멉니다.");
					}
					else
					{
						player.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + args[1] + ChatColor.GREEN + "님에게 거래를 신청했습니다.");
						player.sendMessage(Main.getMain().getTitle() + ChatColor.GRAY + "※ 인벤토리가 꽉찼을 경우에는 아이템이 떨어집니다. ※");
						target.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "님에게 거래신청이 들어왔습니다.");
						target.sendMessage(Main.getMain().getTitle() + ChatColor.GRAY + "※ 인벤토리가 꽉찼을 경우에는 아이템이 떨어집니다. ※");
						new LinmaluTellraw("$C:" + ChatColor.GOLD + "/거래 수락|/거래 수락|$" + ChatColor.WHITE + " 또는 " + "$C:" + ChatColor.GOLD + "/거래 취소|/거래 취소$").changeCmd().sendMessage(target);
						LinmaluTitle.sendMessage(target, ChatColor.GREEN + "거래신청 " + ChatColor.GOLD + player.getName(), Main.getMain().getTitle(), 20, 20, 20);
						data.addInventoryData(player, target);
					}
				}
				else
				{
					player.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + args[1] + ChatColor.YELLOW + "님은 없습니다.");
				}
			}
			else if(args.length == 1 && args[0].equals("수락"))
			{
				if(id == null)
				{
					player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					if(id.isFirst(player))
					{
						player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "거래를 받은적이 없습니다.");
					}
					else
					{
						id.openInventory();
					}
				}
			}
			else if(args.length == 1 && args[0].equals("취소"))
			{
				if(id == null)
				{
					player.sendMessage(Main.getMain().getTitle() + ChatColor.YELLOW + "거래가 없습니다.");
				}
				else
				{
					player.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + id.getName2() + ChatColor.GREEN + "님과의 거래를 취소했습니다.");
					Player target = id.isFirst(player) ? id.getPlayer2() : id.getPlayer1();
					if(target.isOnline())
					{
						target.sendMessage(Main.getMain().getTitle() + ChatColor.GOLD + player.getName() + ChatColor.GREEN + "님이 거래를 취소했습니다.");
					}
					id.cancel();
				}
			}
			else if(sender.isOp() && args.length == 1 && (args[0].equals("리로드") || args[0].equals("reload")))
			{
				data.reloadConfig();
				sender.sendMessage(Main.getMain().getTitle() + ChatColor.GREEN + "설정파일을 다시 불러왔습니다.");
			}
			else
			{
				sender.sendMessage(ChatColor.GREEN + " = = = = = [ Linmalu Trade ] = = = = =");
				LinmaluTellraw.sendChat(sender, "/" + label + " 신청 ", ChatColor.GOLD + "/" + label + " 신청 <플레이어>" + ChatColor.GRAY + " : 플레이어에게 거래신청을 합니다.");
				LinmaluTellraw.sendChat(sender, "/" + label + " 수락 ", ChatColor.GOLD + "/" + label + " 수락" + ChatColor.GRAY + " : 거래신청을 수락합니다.");
				LinmaluTellraw.sendChat(sender, "/" + label + " 취소 ", ChatColor.GOLD + "/" + label + " 취소" + ChatColor.GRAY + " : 거래신청을 취소합니다.");
				if(sender.isOp())
				{
					LinmaluTellraw.sendChat(sender, "/" + label + " 리로드 ", ChatColor.GOLD + "/" + label + " 리로드 // reload" + ChatColor.GRAY + " : 설정파일을 다시 불러옵니다.");
					LinmaluVersion.check(Main.getMain(), sender);
				}
				sender.sendMessage(ChatColor.YELLOW + "제작자 : " + ChatColor.AQUA + "린마루(Linmalu)" + ChatColor.WHITE + " - http://blog.linmalu.com");
				sender.sendMessage(ChatColor.YELLOW + "카페 : " + ChatColor.WHITE + "http://cafe.naver.com/craftproducer");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.RED + "콘솔에서는 사용할 수 없습니다.");
		}
		return true;
	}
}
