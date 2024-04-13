package dev.warriorrr.entityttl;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EntityTTL extends JavaPlugin implements Listener {
    private final Map<NamespacedKey, Long> lifetimes = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    public void loadConfig() {
        final ConfigurationSection entities = getConfig().getConfigurationSection("entities");

        if (entities == null || entities.getKeys(false).isEmpty()) {
            getConfig().createSection("entities");
            getConfig().set("entities.snowball", -1);
            saveConfig();
            return;
        }

        for (final String entity : entities.getKeys(false)) {
            final NamespacedKey key = NamespacedKey.fromString(entity);

            if (key == null) {
                getLogger().warning("'" + entity + "' is not a valid namespaced key.");
                continue;
            }

            if (Registry.ENTITY_TYPE.get(key) == null) {
                getLogger().warning(key + " is not a valid entity type.");
                continue;
            }

            final long lifetime = entities.getLong(entity);
            if (lifetime < 0)
                continue;

            lifetimes.put(key, lifetime);
        }
    }

    @EventHandler
    public void onEntityAddToWorld(final EntityAddToWorldEvent event) {
        final Entity entity = event.getEntity();

        final Long removeTicks = lifetimes.get(entity.getType().getKey());
        if (removeTicks == null)
            return;

        // Remove immediately
        if (removeTicks == 0) {
            entity.getScheduler().run(this, task -> entity.remove(), () -> {});
            return;
        }

        entity.getScheduler().runDelayed(this, task -> entity.remove(), () -> {}, removeTicks);
    }
}
