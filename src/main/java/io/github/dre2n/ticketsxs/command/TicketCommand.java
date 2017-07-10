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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class TicketCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This is not a console command.");
            return false;
        }
        if (TicketsXS.getTickets(TicketStatus.OPEN, (Player) sender).size() > 3) {
            sender.sendMessage(ChatColor.DARK_RED + "You may not create more tickets than three.");
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "/ticket [Text]");
            return false;
        }
        String text = new String();
        for (String arg : args) {
            if (!text.isEmpty()) {
                text += " ";
            }
            text += arg;
        }
        TicketsXS.getTickets().add(new Ticket((Player) sender, text));
        sender.sendMessage(ChatColor.GREEN + "Ticket created.");
        Bukkit.broadcast(ChatColor.DARK_RED + sender.getName() + ChatColor.AQUA + " opened a new help ticket.", TicketsXS.PERMISSION);
        return true;
    }

}
