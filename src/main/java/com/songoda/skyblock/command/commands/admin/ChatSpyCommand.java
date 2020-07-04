package com.songoda.skyblock.command.commands.admin;

import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager;
import com.songoda.skyblock.gui.bank.GuiBank;
import com.songoda.skyblock.island.Island;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.skyblock.playerdata.PlayerDataManager;
import com.songoda.skyblock.sound.SoundManager;
import com.songoda.skyblock.utils.player.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class ChatSpyCommand extends SubCommand {
    
    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        PlayerDataManager playerDataManager = skyblock.getPlayerDataManager();
        MessageManager messageManager = skyblock.getMessageManager();
        IslandManager islandManager = skyblock.getIslandManager();
        FileManager fileManager = skyblock.getFileManager();
        SoundManager soundManager = skyblock.getSoundManager();
    
        FileManager.Config language = fileManager.getConfig(new File(skyblock.getDataFolder(), "language.yml"));
        FileConfiguration languageLoad = language.getFileConfiguration();
        
        PlayerData playerData = playerDataManager.getPlayerData(player);
    
        if (args.length < 1) {
            toggleSpy(player, messageManager, languageLoad, playerData);
        } else {
            switch (args[0].toLowerCase()) {
                case "toggle":
                    toggleSpy(player, messageManager, languageLoad, playerData);
                    break;
                case "global":
                    if(!playerData.isGlobalChatSpy()){
                        playerData.enableGlobalChatSpy();
                        messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.Bank.ByConsole.Message"));
                    } else {
                        messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.Bank.ByConsole.Message"));
                        soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1f, 1f);
                    }
                    break;
                case "add":
                    if(args.length > 2){
                        Island island = islandManager.getIslandByPlayer(new OfflinePlayer(UUID.fromString(args[1])).getBukkitOfflinePlayer());
                        if(island != null) {
                            playerData.addChatSpyIsland(island);
                        }
                    } else {
                        messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.Bank.Unexpected.Message"));
                        soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1f, 1f);
                    }
                    break;
                case "remove":
                    if(args.length > 2){
                        Island island = islandManager.getIslandByPlayer(new OfflinePlayer(UUID.fromString(args[1])).getBukkitOfflinePlayer());
                        if(island != null) {
                            playerData.removeChatSpyIsland(island);
                        }
                    } else {
                        messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.Bank.Unexpected.Message"));
                        soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1f, 1f);
                    }
                    break;
                case "list":
                    
                    break;
                default:
                    messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.Bank.Unexpected.Message"));
                    soundManager.playSound(player, CompatibleSound.BLOCK_ANVIL_LAND.getSound(), 1f, 1f);
                    break;
            }
        }
    }
    
    private void toggleSpy(Player player, MessageManager messageManager, FileConfiguration languageLoad, PlayerData playerData) {
        if(playerData != null) {
            if(playerData.isChatSpy()){
                playerData.setChatSpy(false);
                messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.ChatSpy.Disabled.Message"));
            } else {
                playerData.setChatSpy(true);
                messageManager.sendMessage(player, languageLoad.getString("Command.Island.Admin.ChatSpy.Enabled.Message"));
            }
        }
    }
    
    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }
    
    @Override
    public String getName() {
        return "chatspy";
    }
    
    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Admin.ChatSpy.Info.Message";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"spychat", "spy"};
    }
    
    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
