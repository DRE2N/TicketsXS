/*
 * Copyright (C) 2017 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dre2n.ticketsxs.command;

import io.github.dre2n.ticketsxs.TicketsXS;
import io.github.dre2n.ticketsxs.ticket.Ticket;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class CheckTicketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This is not a console command.");
            return false;
        }
        if (args.length == 0 || !StringUtils.isNumeric(args[0])) {
            sender.sendMessage(ChatColor.DARK_RED + "/checkticket [ID]");
            return false;
        }
        int id = Integer.parseInt(args[0]);
        Ticket ticket = TicketsXS.getById(id);
        if (!sender.hasPermission(TicketsXS.PERMISSION) && !ticket.getPlayerId().equals(((Player) sender).getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to check a ticket status.");
            return false;
        }
        if (ticket == null) {
            sender.sendMessage(ChatColor.DARK_RED + "This ticket does not exist.");
            return false;
        }
        sender.sendMessage("======= " + ticket.getStatus().getColor() + Ticket.TICKET + ticket.getId() + ChatColor.WHITE + " =======");
        sender.sendMessage(ChatColor.YELLOW + ticket.getDate().toString() + " | "
                + ticket.getLocation().getBlockX() + "," + ticket.getLocation().getBlockY() + "," + ticket.getLocation().getBlockZ());
        ticket.getConversation().forEach(c -> sender.sendMessage(c));
        sender.sendMessage("Use " + ChatColor.DARK_RED + "/replyticket [ID] [Text...]" + ChatColor.WHITE + " to reply.");
        return true;
    }

}
