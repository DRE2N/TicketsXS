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
import java.util.Arrays;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class TicketsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "This is not a console command.");
            return false;
        }
        Player player = (Player) sender;
        boolean others = sender.hasPermission(TicketsXS.PERMISSION);
        boolean closed = args.length >= 1 && Arrays.asList(args).contains("-closed");
        Set<Ticket> tickets = null;
        if (others && closed) {
            tickets = TicketsXS.getTickets();
        } else if (others) {
            tickets = TicketsXS.getTickets(TicketStatus.OPEN);
            tickets.addAll(TicketsXS.getTickets(TicketStatus.ON_HOLD));
        } else if (closed && sender instanceof Player) {
            tickets.addAll(TicketsXS.getTickets(player));
        } else if (sender instanceof Player) {
            tickets = TicketsXS.getTickets(TicketStatus.OPEN, player);
            tickets.addAll(TicketsXS.getTickets(TicketStatus.ON_HOLD, player));
        }
        sender.sendMessage("======= " + ChatColor.DARK_RED + "Tickets" + ChatColor.WHITE + " =======");
        tickets.forEach(t -> player.spigot().sendMessage(t.toChat()));
        sender.sendMessage("Use " + ChatColor.DARK_RED + "/checkticket [ID]" + ChatColor.WHITE + " or click at an entry for further information.");
        return true;
    }

}
