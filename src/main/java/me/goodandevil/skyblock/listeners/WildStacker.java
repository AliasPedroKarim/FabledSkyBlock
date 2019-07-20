package me.goodandevil.skyblock.listeners;

import com.bgsoftware.wildstacker.api.events.BarrelPlaceEvent;
import com.bgsoftware.wildstacker.api.events.BarrelStackEvent;
import com.bgsoftware.wildstacker.api.events.BarrelUnstackEvent;
import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.events.SpawnerStackEvent;
import com.bgsoftware.wildstacker.api.events.SpawnerUnstackEvent;
import me.goodandevil.skyblock.SkyBlock;
import me.goodandevil.skyblock.config.FileManager;
import me.goodandevil.skyblock.island.Island;
import me.goodandevil.skyblock.island.IslandLevel;
import me.goodandevil.skyblock.island.IslandManager;
import me.goodandevil.skyblock.utils.version.Materials;
import me.goodandevil.skyblock.utils.version.NMSUtil;
import me.goodandevil.skyblock.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;

public class WildStacker implements Listener {

    private final SkyBlock skyblock;

    public WildStacker(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBarrelPlace(BarrelPlaceEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getBarrel().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        Material material = event.getBarrel().getType();
        byte data = (byte) event.getBarrel().getData();

        if (NMSUtil.getVersionNumber() > 12 && material.name().startsWith("LEGACY_")) {
            material = Material.matchMaterial(material.name().replace("LEGACY_", ""));
            data = 0;
        }

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getMaterials(material, data);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                long materialAmount = 0;
                if (level.hasMaterial(materials.name())) {
                    materialAmount = level.getMaterialAmount(materials.name());
                }

                level.setMaterialAmount(materials.name(), materialAmount + event.getBarrel().getStackAmount() - 1); // -1 because the Interact handler will always add +1
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBarrelStack(BarrelStackEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getBarrel().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        Material material = event.getBarrel().getType();
        byte data = (byte) event.getBarrel().getData();

        if (NMSUtil.getVersionNumber() > 12 && material.name().startsWith("LEGACY_")) {
            material = Material.matchMaterial(material.name().replace("LEGACY_", ""));
            data = 0;
        }

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getMaterials(material, data);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                long materialAmount = 0;
                if (level.hasMaterial(materials.name())) {
                    materialAmount = level.getMaterialAmount(materials.name());
                }

                level.setMaterialAmount(materials.name(), materialAmount + event.getTarget().getStackAmount() - 1); // -1 because the Interact handler will always add +1
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBarrelUnstack(BarrelUnstackEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getBarrel().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        Material material = event.getBarrel().getType();
        byte data = (byte) event.getBarrel().getData();

        if (NMSUtil.getVersionNumber() > 12 && material.name().startsWith("LEGACY_")) {
            material = Material.matchMaterial(material.name().replace("LEGACY_", ""));
            data = 0;
        }

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getMaterials(material, data);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                if (level.hasMaterial(materials.name())) {
                    long materialAmount = level.getMaterialAmount(materials.name());

                    if (materialAmount - event.getAmount() <= 0) {
                        level.removeMaterial(materials.name());
                    } else {
                        level.setMaterialAmount(materials.name(), materialAmount - event.getAmount());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawnerPlace(SpawnerPlaceEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getSpawner().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        EntityType spawnerType = event.getSpawner().getSpawnedType();

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getSpawner(spawnerType);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                long materialAmount = 0;
                if (level.hasMaterial(materials.name())) {
                    materialAmount = level.getMaterialAmount(materials.name());
                }

                level.setMaterialAmount(materials.name(), materialAmount + event.getSpawner().getStackAmount());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawnerStack(SpawnerStackEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getSpawner().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        EntityType spawnerType = event.getSpawner().getSpawnedType();

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getSpawner(spawnerType);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                long materialAmount = 0;
                if (level.hasMaterial(materials.name())) {
                    materialAmount = level.getMaterialAmount(materials.name());
                }

                level.setMaterialAmount(materials.name(), materialAmount + event.getTarget().getStackAmount());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawnerUnstack(SpawnerUnstackEvent event) {
        IslandManager islandManager = skyblock.getIslandManager();
        WorldManager worldManager = skyblock.getWorldManager();

        Location location = event.getSpawner().getLocation();
        if (!worldManager.isIslandWorld(location.getWorld())) return;

        Island island = islandManager.getIslandAtLocation(location);

        EntityType spawnerType = event.getSpawner().getSpawnedType();

        FileManager.Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        if (configLoad.getBoolean("Island.Block.Level.Enable")) {
            Materials materials = Materials.getSpawner(spawnerType);
            if (materials != null) {
                IslandLevel level = island.getLevel();

                if (level.hasMaterial(materials.name())) {
                    long materialAmount = level.getMaterialAmount(materials.name());

                    if (materialAmount - event.getAmount() <= 0) {
                        level.removeMaterial(materials.name());
                    } else {
                        level.setMaterialAmount(materials.name(), materialAmount - event.getAmount());
                    }
                }
            }
        }
    }

}
