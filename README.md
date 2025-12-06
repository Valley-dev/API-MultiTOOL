Main Features

Get the recommended tool for a specific block based on collected player data.

Record block–tool usage, allowing the system to learn which tool is most effective.

View statistics for how often each tool is used on a block.

Check or change a player's current tool through the API.

Reset or clear learning data for players or the entire system.

Check if a block has a default tool and get the default recommended tool.

What It Can Do

getRecommendedTool(block) – Returns the best tool for breaking the given block.

addBlockToolMapping(block, tool) – Records that a tool was used on a block.

getBlockStatistics(block) – Shows usage counts per tool for that block.

getCurrentPlayerTool(player) – Gets the tool type the player is currently using.

forceToolChange(player, tool) – Forces the player to switch tools.

saveLearningData() – Saves all tool-usage learning data.

clearAllLearningData() – Removes all stored data.

isBlockMapped(block) – Checks if the block has a known default tool.

getDefaultToolForBlock(block) – Returns the predefined default tool for a block.
