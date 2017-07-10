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
package io.github.dre2n.ticketsxs.ticket;

import io.github.dre2n.ticketsxs.TicketsXS;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Daniel Saukel
 */
public class Ticket {

    public static final String YAML = ".yml";
    public static final String TICKET = " - Ticket #";
    public static final String ASSIGNED = ChatColor.YELLOW + "Assigned: ";

    private static int newId = 0;

    private int id;
    private TicketStatus status = TicketStatus.OPEN;
    private Date date;
    private Location location;
    private List<String> conversation = new ArrayList<>();
    private UUID user;
    private String assigned;

    public Ticket(Player player, String text) {
        id = newId;
        newId++;
        date = Calendar.getInstance().getTime();
        location = player.getLocation();
        conversation.add(ChatColor.DARK_AQUA + text);
        user = player.getUniqueId();
        save();
    }

    public Ticket(File file) {
        id = Integer.parseInt(file.getName().replace(YAML, new String()));
        if (id >= newId) {
            newId = id + 1;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        status = TicketStatus.valueOf(config.getString("status"));
        date = new Date(config.getLong("date"));
        location = (Location) config.get("location");
        conversation = config.getStringList("conversation");
        user = UUID.fromString(config.getString("user"));
    }

    /* Getters and setters */
    public int getId() {
        return id;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getConversation() {
        return conversation;
    }

    public UUID getPlayerId() {
        return user;
    }

    public String getAssignedName() {
        return assigned;
    }

    public void setAssignedName(String name) {
        assigned = name;
    }

    public void reply(CommandSender sender, String text) {
        ChatColor c = ChatColor.RED;
        if (sender instanceof Player && ((Player) sender).getUniqueId().equals(user)) {
            c = ChatColor.DARK_AQUA;
        }
        conversation.add(c + text);
        save();
    }

    public BaseComponent[] toChat() {
        BaseComponent[] msg = TextComponent.fromLegacyText(status.getColor() + TICKET + id + " (" + Bukkit.getOfflinePlayer(user).getName() + ")");
        String date = ChatColor.GRAY + this.date.toString();
        String assigned = "\n" + ASSIGNED + (this.assigned != null ? this.assigned : "---") + "\n";
        HoverEvent onHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(date + assigned + conversation.get(0)));
        ClickEvent onClick = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/checkticket " + id);
        for (BaseComponent comp : msg) {
            comp.setHoverEvent(onHover);
            comp.setClickEvent(onClick);
        }
        return msg;
    }

    public void save() {
        File file = new File(TicketsXS.getInstance().getDataFolder(), id + YAML);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("status", status.toString());
        config.set("date", date.getTime());
        config.set("location", location);
        config.set("conversation", conversation);
        config.set("user", user.toString());
        config.set("assigned", assigned);
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
