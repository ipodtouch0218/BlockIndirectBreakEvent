package me.ipodtouch0218.indirectbreak;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

public class IndirectBreakMc18 implements Listener {

	//-- INITIALIZATION --//
	private static boolean INITIALIZED;
	private static String MC_VERSION;
	public static void initialize(Plugin source) {
		MC_VERSION = Bukkit.getServer().getClass().getPackage().getName().replaceAll(".+\\.", "");
		if (!MC_VERSION.equals("v1_8_R3")) {
			source.getLogger().log(Level.SEVERE, "Incorrect version for IndirectBreak 1.8! (Expected v1_8_R3, got " + MC_VERSION + ")");
			return;
		}
		if (!INITIALIZED) {
			Bukkit.getPluginManager().registerEvents(new IndirectBreakMc18(), source);
			INITIALIZED = true;
		}
	}
	
	//-- LISTENER --//
	@EventHandler(ignoreCancelled=true,priority=EventPriority.LOWEST)
	public void onPhysics(BlockPhysicsEvent e) {
		Block b = e.getBlock();
		if (b.getType() == Material.AIR)
			return;
		if (WEIRD_DOUBLE_BLOCKS.contains(b.getType()) && WEIRD_DOUBLE_BLOCKS.contains(e.getChangedType()))
			return;
		
		if (!isBlockValid(b)) {
			BlockIndirectBreakEvent indirect = new BlockIndirectBreakEvent(b);
			Bukkit.getPluginManager().callEvent(indirect);
			e.setCancelled(indirect.isCancelled());
		}
	}
	
	//-- UTIL --//
	private static final List<Material> WEIRD_DOUBLE_BLOCKS = Arrays.asList(new Material[]{
			Material.LONG_GRASS, 
			Material.SAPLING, 
			Material.RED_ROSE, 
			Material.YELLOW_FLOWER, 
			Material.DEAD_BUSH, 
			Material.BROWN_MUSHROOM, 
			Material.RED_MUSHROOM});
	
	@SuppressWarnings("deprecation")
	public boolean isBlockValid(Block b) {
		Block up = b.getRelative(BlockFace.UP),
				north = b.getRelative(BlockFace.NORTH),
				east = b.getRelative(BlockFace.EAST),
				south = b.getRelative(BlockFace.SOUTH),
				west = b.getRelative(BlockFace.WEST),
				down = b.getRelative(BlockFace.DOWN);

		Material dMat = down.getType();
		
		switch (b.getType()) {
		case LONG_GRASS:
		case SAPLING:
		case RED_ROSE:
		case YELLOW_FLOWER: {
			return dMat == Material.DIRT ||
					dMat == Material.GRASS;
		}
		case DEAD_BUSH: {
			return dMat == Material.SAND;
		}
		case BROWN_MUSHROOM:
		case RED_MUSHROOM: {
			return dMat == Material.MYCEL ||
					((dMat == Material.DIRT || dMat == Material.GRASS) && b.getLightLevel() < 13);
		}
		case CACTUS: {
			return !north.getType().isSolid() &&
					!east.getType().isSolid() &&
					!south.getType().isSolid() &&
					!west.getType().isSolid() &&
					dMat == Material.SAND;
		}
		case SUGAR_CANE_BLOCK: {
			return dMat == Material.SUGAR_CANE_BLOCK ||
					dMat == Material.SAND ||
					dMat == Material.GRASS ||
					dMat == Material.DIRT;
		}
		case IRON_TRAPDOOR:
		case TRAP_DOOR: {
			switch (b.getData() % 4) {
			case 0: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 1: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 2: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 3: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			}
		}
		case REDSTONE_TORCH_ON:
		case REDSTONE_TORCH_OFF:
		case TORCH: {
			switch (b.getData()) {
			case 1: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			case 2: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 3: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 4: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 5: {
				//Floor
				return down.getType().isSolid() && !down.getType().isTransparent();
			}
			}
		}
		case WOOD_BUTTON: 
		case STONE_BUTTON: {
			switch (b.getData() % 8) {
			case 0: {
				//Up
				return up.getType().isSolid() && !up.getType().isTransparent();
			}
			case 1: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			case 2: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 3: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 4: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 5: {
				//Floor
				return down.getType().isSolid() && !down.getType().isTransparent();
			}
			}
		}
		case TRIPWIRE_HOOK: {
			switch (b.getData() % 4) {
			case 0: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 1: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 2: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 3: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			}
		}
		case VINE: {
			BigInteger data = BigInteger.valueOf(b.getData());
			boolean connectedSouth = data.testBit(0);
			boolean connectedWest = data.testBit(1);
			boolean connectedNorth = data.testBit(2);
			boolean connectedEast = data.testBit(3);
			
			return b.getRelative(BlockFace.UP).getType() == Material.VINE ||
					(connectedSouth && (south.getType().isSolid() && !south.getType().isTransparent())) ||
					(connectedWest && (west.getType().isSolid() && !west.getType().isTransparent())) ||
					(connectedNorth && (north.getType().isSolid() && !north.getType().isTransparent())) ||
					(connectedEast && (east.getType().isSolid() && !east.getType().isTransparent()));
			
		}
		case WALL_BANNER:
		case WALL_SIGN: {
			switch (b.getData()) {
			case 0:
			case 2: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 3: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 4: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 5: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			}
		}
		case LEVER: {
			switch (b.getData() % 8) {
			case 0:
			case 7: {
				//Up
				return up.getType().isSolid() && !up.getType().isTransparent();
			}
			case 1: {
				//West
				return west.getType().isSolid() && !west.getType().isTransparent();
			}
			case 2: {
				//East
				return east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 3: {
				//North
				return north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 4: {
				//South
				return south.getType().isSolid() && !south.getType().isTransparent();
			}
			case 5:
			case 6: {
				//Down
				return down.getType().isSolid() && !down.getType().isTransparent();
			}
			}
		}
		case POWERED_RAIL:
		case DETECTOR_RAIL:
		case ACTIVATOR_RAIL:
		case RAILS: {
			switch (b.getData()) {
			case 0:
			case 1: 
			case 6:
			case 7: {
				//Down
				return down.getType().isSolid() && !down.getType().isTransparent();
			}
			case 2: {
				//Down + East
				return down.getType().isSolid() && !down.getType().isTransparent() &&
						east.getType().isSolid() && !east.getType().isTransparent();
			}
			case 3: {
				//Down + West
				return down.getType().isSolid() && !down.getType().isTransparent() &&
						west.getType().isSolid() && !west.getType().isTransparent();
			}
			case 4: {
				//Down + North
				return down.getType().isSolid() && !down.getType().isTransparent() &&
						north.getType().isSolid() && !north.getType().isTransparent();
			}
			case 5: {
				//Down + South
				return down.getType().isSolid() && !down.getType().isTransparent() &&
						south.getType().isSolid() && !south.getType().isTransparent();
			}
			}
		}
		case DOUBLE_PLANT: {
			return dMat == Material.DOUBLE_PLANT ||
					dMat == Material.GRASS ||
					dMat == Material.DIRT;
		}
		case NETHER_WARTS: {
			return dMat == Material.SOUL_SAND;
		}
		case CROPS:
		case CARROT:
		case POTATO: {
			return dMat == Material.SOIL;
		}
		case WATER_LILY: {
			return dMat == Material.STATIONARY_WATER;
		}
		case COCOA: {
			switch (b.getData() % 4) {
			case 0: {
				//South
				return south.getType() == Material.LOG && (south.getData() % 4 == 3);
			}
			case 1: {
				//West
				return west.getType() == Material.LOG && (west.getData() % 4 == 3);
			}
			case 2: { 
				//North
				return north.getType() == Material.LOG && (north.getData() % 4 == 3);
			}
			case 3: {
				//East
				return east.getType() == Material.LOG && (east.getData() % 4 == 3);
			}
			}
		}
		case FLOWER_POT:
		case SNOW:
		case CARPET:
		case GOLD_PLATE:
		case IRON_PLATE:
		case STONE_PLATE:
		case WOOD_PLATE:
		case ACACIA_DOOR:
		case SPRUCE_DOOR:
		case DARK_OAK_DOOR:
		case BIRCH_DOOR:
		case JUNGLE_DOOR:
		case IRON_DOOR_BLOCK:
		case WOODEN_DOOR:
		case SIGN_POST:
		case STANDING_BANNER:
		case REDSTONE_WIRE:
		case REDSTONE_COMPARATOR_ON:
		case REDSTONE_COMPARATOR_OFF:
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON: {
			return down.getType().isSolid() && !down.getType().isTransparent();
		}
		default: {
			return true;
		}
		}
	}
	
	//-- EVENT CLASS --//
	public static class BlockIndirectBreakEvent extends BlockEvent implements Cancellable {
		private static final HandlerList handlers = new HandlerList();
		private boolean cancelled;
		
		public BlockIndirectBreakEvent(Block theBlock) {
			super(theBlock);
		}
	
		@Override
		public boolean isCancelled() {
			return cancelled;
		}
	
		@Override
		public void setCancelled(boolean value) {
			cancelled = value;
		}
	
		@Override
		public HandlerList getHandlers() {
			return handlers;
		}
		
		public static HandlerList getHandlerList() {
			return handlers;
		}
	}
}
