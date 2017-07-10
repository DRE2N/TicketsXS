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
package io.github.dre2n.ticketsxs;

import io.github.dre2n.ticketsxs.command.*;
import io.github.dre2n.ticketsxs.ticket.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Daniel Saukel
 */
public class TicketsXS extends JavaPlugin {

    private static TicketsXS instance;

    public static String PERMISSION = "txs.mod";

    private Set<Ticket> tickets = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdir();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        ArrayList<File> delete = new ArrayList<>();
        for (File file : getDataFolder().listFiles()) {
            Ticket ticket = new Ticket(file);
            if (ticket.getDate().after(calendar.getTime()) && ticket.getStatus() == TicketStatus.CLOSED) {
                delete.add(file);
            }
        }
        delete.forEach(f -> f.delete());
        Arrays.stream(getDataFolder().listFiles()).forEach(f -> tickets.add(new Ticket(f)));
        getCommand("checkticket").setExecutor(new CheckTicketCommand());
        getCommand("replyticket").setExecutor(new ReplyTicketCommand());
        getCommand("setticket").setExecutor(new SetTicketCommand());
        getCommand("taketicket").setExecutor(new TakeTicketCommand());
        getCommand("ticket").setExecutor(new TicketCommand());
        getCommand("tickets").setExecutor(new TicketsCommand());
        getCommand("ticketsxs").setExecutor(new TicketsXSCommand());
        getServer().getPluginManager().registerEvents(new TicketListener(), this);
    }

    public static TicketsXS getInstance() {
        return instance;
    }

    public static Set<Ticket> getTickets() {
        return instance.tickets;
    }

    public static Set<Ticket> getTickets(TicketStatus status) {
        Set<Ticket> toReturn = new HashSet<>();
        for (Ticket ticket : instance.tickets) {
            if (ticket.getStatus() == status) {
                toReturn.add(ticket);
            }
        }
        return toReturn;
    }

    public static Set<Ticket> getTickets(Player player) {
        Set<Ticket> toReturn = new HashSet<>();
        for (Ticket ticket : instance.tickets) {
            if (ticket.getPlayerId().equals(player.getUniqueId())) {
                toReturn.add(ticket);
            }
        }
        return toReturn;
    }

    public static Set<Ticket> getTickets(TicketStatus status, Player player) {
        Set<Ticket> toReturn = new HashSet<>();
        for (Ticket ticket : instance.tickets) {
            if (ticket.getPlayerId().equals(player.getUniqueId()) && ticket.getStatus() == status) {
                toReturn.add(ticket);
            }
        }
        return toReturn;
    }

    public static Ticket getById(int id) {
        for (Ticket ticket : instance.tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

}
