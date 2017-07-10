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
import io.github.dre2n.ticketsxs.ticket.TicketStatus;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class SetTicketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This is not a console command.");
            return false;
        }
        if (args.length < 2 || !StringUtils.isNumeric(args[0])) {
            sender.sendMessage(ChatColor.DARK_RED + "/setticket [ID] [CLOSED|OPEN|ON_HOLD]");
            return false;
        }
        int id = Integer.parseInt(args[0]);
        Ticket ticket = TicketsXS.getById(id);
        if (ticket == null) {
            sender.sendMessage(ChatColor.DARK_RED + "This ticket does not exist.");
            return false;
        }
        if (args.length == 1) {
            sender.sendMessage("Ticket #" + ticket.getId() + " is " + ticket.getStatus().getClass() + ticket.getStatus().toString());
            return true;
        }
        if (!sender.hasPermission(TicketsXS.PERMISSION) && !ticket.getPlayerId().equals(((Player) sender).getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to modify this ticket.");
            return false;
        }
        try {
            TicketStatus status = TicketStatus.valueOf(args[1].toUpperCase());
            if (!sender.hasPermission(TicketsXS.PERMISSION) && status == TicketStatus.ON_HOLD) {
                sender.sendMessage(ChatColor.DARK_RED + "This status may only be used by moderators.");
                return false;
            }
            ticket.setStatus(status);
            String msg = "The status of #" + status.getColor().toString() + ticket.getId() + ChatColor.WHITE.toString() + " is now "
                    + status.getColor() + status.toString() + ChatColor.WHITE + ".";
            Bukkit.broadcast(msg, TicketsXS.PERMISSION);
            if (!sender.hasPermission(TicketsXS.PERMISSION)) {
                sender.sendMessage(msg);
            }
        } catch (RuntimeException exception) {
            sender.sendMessage(ChatColor.DARK_RED + args[1] + "is not a valid status!");
        }
        return true;
    }

}
