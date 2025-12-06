package dev.ItemRotation.valley.api;

import dev.ItemRotation.valley.managers.multitool.BlockToolMapping;
import dev.ItemRotation.valley.managers.multitool.MultiToolManager;
import dev.ItemRotation.valley.managers.multitool.ToolType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class MultiToolAPI {

    private final MultiToolManager manager;

    public MultiToolAPI(MultiToolManager manager) {
        this.manager = manager;
    }

    public ToolType getRecommendedTool(Material block) {
        return manager.getLearningData().getPreferredTool(block);
    }

    public void addBlockToolMapping(Material block, ToolType tool) {
        manager.recordBlockBreak(block, tool);
    }

    public Map<ToolType, Integer> getBlockStatistics(Material block) {
        return manager.getLearningData().getBlockStatistics(block);
    }

    public int getUsageCount(Material block, ToolType tool) {
        return manager.getLearningData().getUsageCount(block, tool);
    }

    public ToolType getCurrentPlayerTool(Player player) {
        return manager.getCurrentTool(player.getUniqueId());
    }

    public ToolType getCurrentPlayerTool(UUID playerId) {
        return manager.getCurrentTool(playerId);
    }

    public void forceToolChange(Player player, ToolType tool) {
        manager.cycleToolManually(player, player.getInventory().getItemInMainHand());
    }

    public void resetPlayerData(UUID playerId) {
        manager.resetPlayerTool(playerId);
    }

    public void saveLearningData() {
        manager.saveData();
    }

    public void clearAllLearningData() {
        manager.getLearningData().clearData();
    }

    public boolean isBlockMapped(Material block) {
        return BlockToolMapping.isToolApplicable(block);
    }

    public ToolType getDefaultToolForBlock(Material block) {
        return BlockToolMapping.getToolType(block);
    }
}