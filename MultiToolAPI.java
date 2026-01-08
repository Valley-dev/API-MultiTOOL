package dev.ItemRotation.valley.api;

import dev.ItemRotation.valley.managers.multitool.BlockToolMapping;
import dev.ItemRotation.valley.managers.multitool.MultiToolManager;
import dev.ItemRotation.valley.managers.multitool.ToolType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MultiToolAPI {

    private final MultiToolManager manager;

    public MultiToolAPI(MultiToolManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("MultiToolManager cannot be null");
        }
        this.manager = manager;
    }

    public ToolType getRecommendedTool(Material block) {
        validateMaterial(block);
        return manager.getLearningData().getPreferredTool(block);
    }

    public ToolType getRecommendedTool(Player player) {
        validatePlayer(player);
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            return null;
        }
        return getRecommendedTool(item.getType());
    }

    public void recordBlockBreak(Material block, ToolType tool) {
        validateMaterial(block);
        validateToolType(tool);
        manager.recordBlockBreak(block, tool);
    }

    public void recordBlockBreak(Player player, Material block, ToolType tool) {
        validatePlayer(player);
        validateMaterial(block);
        validateToolType(tool);
        manager.recordBlockBreak(block, tool);
    }

    public Map<ToolType, Integer> getBlockStatistics(Material block) {
        validateMaterial(block);
        return Map.copyOf(manager.getLearningData().getBlockStatistics(block));
    }

    public int getUsageCount(Material block, ToolType tool) {
        validateMaterial(block);
        validateToolType(tool);
        return manager.getLearningData().getUsageCount(block, tool);
    }

    public double getToolEfficiency(Material block, ToolType tool) {
        validateMaterial(block);
        validateToolType(tool);
        
        Map<ToolType, Integer> stats = manager.getLearningData().getBlockStatistics(block);
        if (stats.isEmpty()) return 0.0;
        
        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        int usage = stats.getOrDefault(tool, 0);
        
        return total > 0 ? (double) usage / total * 100.0 : 0.0;
    }

    public ToolType getCurrentTool(Player player) {
        validatePlayer(player);
        return manager.getCurrentTool(player.getUniqueId());
    }

    public ToolType getCurrentTool(UUID playerId) {
        validateUUID(playerId);
        return manager.getCurrentTool(playerId);
    }

    public void setCurrentTool(Player player, ToolType tool) {
        validatePlayer(player);
        validateToolType(tool);
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            throw new IllegalStateException("Player must hold an item to change tool");
        }
        
        manager.cycleToolManually(player, item);
    }

    public void setCurrentTool(UUID playerId, ToolType tool) {
        validateUUID(playerId);
        validateToolType(tool);
        manager.resetPlayerTool(playerId);
    }

    public void cycleTool(Player player) {
        validatePlayer(player);
        
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            return;
        }
        
        manager.cycleToolManually(player, item);
    }

    public void resetPlayerData(Player player) {
        validatePlayer(player);
        manager.resetPlayerTool(player.getUniqueId());
    }

    public void resetPlayerData(UUID playerId) {
        validateUUID(playerId);
        manager.resetPlayerTool(playerId);
    }

    public void saveData() {
        manager.saveData();
    }

    public CompletableFuture<Void> saveDataAsync() {
        return CompletableFuture.runAsync(() -> manager.saveData());
    }

    public void clearAllData() {
        manager.getLearningData().clearData();
    }

    public void clearBlockData(Material block) {
        validateMaterial(block);
        manager.getLearningData().getBlockStatistics(block).clear();
    }

    public boolean isToolApplicable(Material block) {
        if (block == null) return false;
        return BlockToolMapping.isToolApplicable(block);
    }

    public ToolType getDefaultTool(Material block) {
        validateMaterial(block);
        return BlockToolMapping.getToolType(block);
    }

    public boolean hasLearningData(Material block) {
        if (block == null) return false;
        return !manager.getLearningData().getBlockStatistics(block).isEmpty();
    }

    public int getTotalBreaks(Material block) {
        validateMaterial(block);
        return manager.getLearningData()
            .getBlockStatistics(block)
            .values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    public ToolType getMostUsedTool(Material block) {
        validateMaterial(block);
        
        Map<ToolType, Integer> stats = manager.getLearningData().getBlockStatistics(block);
        if (stats.isEmpty()) {
            return getDefaultTool(block);
        }
        
        return stats.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(getDefaultTool(block));
    }

    public Map<Material, ToolType> getAllRecommendations() {
        return Map.copyOf(manager.getLearningData().getAllPreferredTools());
    }

    public boolean isPlayerToolActive(Player player) {
        validatePlayer(player);
        return manager.getCurrentTool(player.getUniqueId()) != null;
    }

    public boolean isPlayerToolActive(UUID playerId) {
        validateUUID(playerId);
        return manager.getCurrentTool(playerId) != null;
    }

    public MultiToolManager getManager() {
        return manager;
    }

    private void validateMaterial(Material material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (material.isAir()) {
            throw new IllegalArgumentException("Material cannot be AIR");
        }
    }

    private void validateToolType(ToolType tool) {
        if (tool == null) {
            throw new IllegalArgumentException("ToolType cannot be null");
        }
    }

    private void validatePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (!player.isOnline()) {
            throw new IllegalStateException("Player must be online");
        }
    }

    private void validateUUID(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
    }
}
