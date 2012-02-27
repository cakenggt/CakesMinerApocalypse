package me.cakenggt.CakesMinerApocalypse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;




public class CakesMinerApocalypseVaultCreator implements Listener {
	CakesMinerApocalypse p;
	public CakesMinerApocalypseVaultCreator(CakesMinerApocalypse plugin) {
		p = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void vaultCreator(ChunkLoadEvent event) throws IOException {
		//System.out.println("chunk load event");
		if(!event.isNewChunk())
			return;
		//System.out.println("new chunk");
		
		double randomA = Math.random() * (1 / this.p.getShelterChance());

		Chunk chunk = event.getChunk();
		int x = chunk.getX() * 16;
		int z = chunk.getZ() * 16;
		int y = chunk.getWorld().getHighestBlockYAt(x + 8, z + 2);
		
		double randomB = Math.random() * (1 / this.p.getCraterChance());
		
		if (randomB <= 1) {
			Location start = new Location(event.getWorld(), x + 8, y, z + 8);
			System.out.println("Crater generated at " + start.getX() + " " + start.getZ());
			start.getWorld().createExplosion(start, 32F, false);
			craterWrite(start);
			craterTimeWrite();
			this.p.loadCraters();
			this.p.loadCraterTimes();
		}
		
		if (randomA <= 1) {
			Location start = new Location(event.getWorld(), x + 8, y, z + 2);
			generateVault(start);
		}
	}

	public void generateVault(Location start) {
		System.out.println("Fallout shelter generated at " + start.getX() + " " + start.getZ());
		//start.getBlock().setType(Material.GLOWSTONE);
		start = start.subtract(0, 1, 0);
		start.getBlock().setType(Material.TRAP_DOOR);
		double y = start.getY() - 2;
		while (start.getBlock().getY() > y / 2) {
			start.subtract(0, 1, 0);
			//start = start.subtract(0, 1, 0);
			start.getBlock().setType(Material.LADDER);
			start.getBlock().setData((byte) 3);
			//2, 4, 3
		}
		start.getBlock().getRelative(0, 0, 1).setType(Material.AIR);
		start.getBlock().getRelative(0, 1, 1).setType(Material.AIR);
		start.getBlock().getRelative(0, 0, 2).setType(Material.AIR);
		start.getBlock().getRelative(0, 1, 2).setType(Material.AIR);
		start.subtract(2, 0, -3);
		start.subtract(2, 2, 2);
		Location end = new Location(start.getWorld(), start.getX() + 8,
				start.getY() + 7, start.getZ() + 11);
		int xIteration = 0;
		int yIteration = 0;
		Block startBlock = start.getBlock();
		while (startBlock.getY() <= end.getY()) {
			while (startBlock.getX() <= end.getX()) {
				while (startBlock.getZ() <= end.getZ()) {
					startBlock.setType(Material.BEDROCK);
					startBlock = startBlock.getRelative(0, 0, 1);
				}
				xIteration++;
				startBlock = start.getBlock().getRelative(xIteration,
						yIteration, 0);
			}
			yIteration++;
			xIteration = 0;
			startBlock = start.getBlock().getRelative(0, yIteration, 0);
		}
		start.add(2, 2, 2);
		start.getBlock().getRelative(2, 0, -1).setType(Material.AIR);
		start.getBlock().getRelative(2, 1, -1).setType(Material.AIR);
		start.getBlock().getRelative(2, 0, -2).setType(Material.AIR);
		start.getBlock().getRelative(2, 1, -2).setType(Material.AIR);
		startBlock = start.getBlock();
		end = new Location(start.getWorld(), start.getX() + 4,
				start.getY() + 3, start.getZ() + 7);
		//System.out.println("start " + start.getX() + " " + start.getY() + " " + start.getZ());
		//System.out.println("end " + end.getX() + " " + end.getY() + " " + end.getZ());
		xIteration = 0;
		yIteration = 0;
		while (startBlock.getY() <= end.getY()) {
			while (startBlock.getX() <= end.getX()) {
				while (startBlock.getZ() <= end.getZ()) {
					startBlock.setType(Material.AIR);
					startBlock = startBlock.getRelative(0, 0, 1);
				}
				xIteration++;
				startBlock = start.getBlock().getRelative(xIteration,
						yIteration, 0);
			}
			yIteration++;
			xIteration = 0;
			startBlock = start.getBlock().getRelative(0, yIteration, 0);
		}
		start.getBlock().getRelative(2, 4, 2).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(2, 4, 5).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 1, 1).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 1, 2).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 3, 1).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 3, 2).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 1, 5).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 1, 6).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 3, 5).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 3, 6).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 1, 1).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 1, 2).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 3, 1).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 3, 2).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 1, 5).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 1, 6).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 3, 5).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(5, 3, 6).setType(Material.GLOWSTONE);
		start.getBlock().getRelative(-1, 0, 1).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 0, 2).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 2, 1).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 2, 2).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 0, 5).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 0, 6).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 2, 5).setType(Material.CHEST);
		start.getBlock().getRelative(-1, 2, 6).setType(Material.CHEST);
		start.getBlock().getRelative(5, 0, 1).setType(Material.CHEST);
		start.getBlock().getRelative(5, 0, 2).setType(Material.CHEST);
		start.getBlock().getRelative(5, 2, 1).setType(Material.CHEST);
		start.getBlock().getRelative(5, 2, 2).setType(Material.CHEST);
		start.getBlock().getRelative(5, 0, 5).setType(Material.CHEST);
		start.getBlock().getRelative(5, 0, 6).setType(Material.CHEST);
		start.getBlock().getRelative(5, 2, 5).setType(Material.CHEST);
		start.getBlock().getRelative(5, 2, 6).setType(Material.CHEST);
		Block[] chestArray = { start.getBlock().getRelative(-1, 0, 1),
				start.getBlock().getRelative(-1, 0, 2),
				start.getBlock().getRelative(-1, 2, 1),
				start.getBlock().getRelative(-1, 2, 2),
				start.getBlock().getRelative(-1, 0, 5),
				start.getBlock().getRelative(-1, 0, 6),
				start.getBlock().getRelative(-1, 2, 5),
				start.getBlock().getRelative(-1, 2, 6),
				start.getBlock().getRelative(5, 0, 1),
				start.getBlock().getRelative(5, 0, 2),
				start.getBlock().getRelative(5, 2, 1),
				start.getBlock().getRelative(5, 2, 2),
				start.getBlock().getRelative(5, 0, 5),
				start.getBlock().getRelative(5, 0, 6),
				start.getBlock().getRelative(5, 2, 5),
				start.getBlock().getRelative(5, 2, 6) };
		for (Block block : chestArray) {
			Chest chest = (Chest) block.getState();
			Inventory chestInventory = chest.getInventory();
			//ItemStack[] chestStack = new ItemStack[27];
			for (int t = 0; t < 27; t++) {
				int a = (int) (Math.random() * 64);
				int b = (int) (Math.random() * 64);
				if (a >= 54 && a <= 63)
					a = a - 19;
				if (a == 0) {
					chestInventory.setItem(t, new ItemStack(
							Material.PUMPKIN_SEEDS, b));
				} else if (a == 1) {
					chestInventory.setItem(t, new ItemStack(Material.BREAD,
							b));
				} else if (a == 2) {
					chestInventory.setItem(t, new ItemStack(Material.CAKE,
							b));
				} else if (a == 3) {
					chestInventory.setItem(t, new ItemStack(
							Material.COOKIE, b));
				} else if (a == 4) {
					chestInventory.setItem(t, new ItemStack(Material.MELON,
							b));
				} else if (a == 5) {
					chestInventory.setItem(t, new ItemStack(
							Material.MUSHROOM_SOUP, b));
				} else if (a == 6) {
					chestInventory.setItem(t, new ItemStack(
							Material.COOKED_CHICKEN, b));
				} else if (a == 7) {
					chestInventory.setItem(t, new ItemStack(
							Material.COOKED_BEEF, b));
				} else if (a == 8) {
					chestInventory.setItem(t, new ItemStack(
							Material.GRILLED_PORK, b));
				} else if (a == 9) {
					chestInventory.setItem(t, new ItemStack(
							Material.COOKED_FISH, b));
				} else if (a == 10) {
					chestInventory.setItem(t, new ItemStack(
							Material.REDSTONE, b));
				} else if (a == 11) {
					chestInventory.setItem(t, new ItemStack(Material.DIODE,
							b));
				} else if (a == 12) {
					chestInventory.setItem(t, new ItemStack(
							Material.REDSTONE_TORCH_ON, b));
				} else if (a == 13) {
					chestInventory.setItem(t, new ItemStack(Material.TORCH,
							b));
				} else if (a == 14) {
					chestInventory.setItem(t, new ItemStack(
							Material.IRON_FENCE, b));
				} else if (a == 15) {
					chestInventory.setItem(t, new ItemStack(
							Material.COMPASS, b));
				} else if (a == 16) {
					chestInventory.setItem(t, new ItemStack(
							Material.IRON_BOOTS, b));
				} else if (a == 17) {
					chestInventory.setItem(t, new ItemStack(
							Material.IRON_CHESTPLATE, b));
				} else if (a == 18) {
					chestInventory.setItem(t, new ItemStack(
							Material.IRON_HELMET, b));
				} else if (a == 19) {
					chestInventory.setItem(t, new ItemStack(
							Material.IRON_LEGGINGS, b));
				} else if (a == 20) {
					chestInventory.setItem(t, new ItemStack(
							Material.MELON_SEEDS, b));
				} else if (a == 21) {
					chestInventory.setItem(t, new ItemStack(Material.SEEDS,
							b));
				}
			}
			chest.update(true);
		}
		Block bottom = start.getBlock().getRelative(0, 0, 4);
		bottom.setTypeIdAndData(26, (byte) 8, false);
		bottom.getRelative(0, 0, -1).setTypeIdAndData(26, (byte) 0, true);
		bottom = start.getBlock().getRelative(2, 0, -2);
		bottom.setTypeIdAndData(71, (byte) 0, false);
		bottom.getRelative(0, 1, 0).setTypeIdAndData(71, (byte) 8, true);
		bottom = start.getBlock().getRelative(2, 1, -1);
		bottom.setTypeIdAndData(69, (byte) 1, false);
		start.getBlock().getRelative(4, 0, 3).setType(Material.CHEST);
		Chest chest = (Chest) start.getBlock().getRelative(4, 0, 3)
				.getState();
		Inventory chestInventory = chest.getInventory();
		chestInventory.setItem(0, new ItemStack(Material.SPONGE, 1));
		for (int t = 1; t < 27; t++) {
			int a = (int) (Math.random() * 48);
			int b = (int) (Math.random() * 64);
			if (a == 0) {
				chestInventory.setItem(t, new ItemStack(
						Material.GLASS_BOTTLE, b));
			} else if (a == 1) {
				chestInventory.setItem(t, new ItemStack(
						372, b)); //Nether wart
			} else if (a == 2) {
				chestInventory.setItem(t, new ItemStack(
						Material.GLOWSTONE_DUST, b));
			} else if (a == 3) {
				chestInventory.setItem(t, new ItemStack(Material.REDSTONE,
						b));
			} else if (a == 4) {
				chestInventory.setItem(t, new ItemStack(
						Material.FERMENTED_SPIDER_EYE, b));
			} else if (a == 5) {
				chestInventory.setItem(t, new ItemStack(
						Material.MAGMA_CREAM, b));
			} else if (a == 6) {
				chestInventory.setItem(t, new ItemStack(Material.SUGAR, b));
			} else if (a == 7) {
				chestInventory.setItem(t, new ItemStack(382, b));
			} else if (a == 8) {
				chestInventory.setItem(t, new ItemStack(
						Material.SPIDER_EYE, b));
			} else if (a == 9) {
				chestInventory.setItem(t, new ItemStack(
						Material.GHAST_TEAR, b));
			} else if (a == 10) {
				chestInventory.setItem(t, new ItemStack(
						Material.BLAZE_POWDER, b));
			} else if (a == 11) {
				chestInventory.setItem(t,
						new ItemStack(Material.SULPHUR, b));
			}
		}
		chest.update(true);
		start.getBlock().getRelative(4, -1, 4).setType(Material.WATER);
		start.getBlock().getRelative(0, 0, 7).setType(
				Material.ENCHANTMENT_TABLE);
		start.getBlock().getRelative(1, 0, 7).setType(
				Material.BREWING_STAND);
		start.getBlock().getRelative(2, 0, 7).setType(Material.WORKBENCH);
		start.getBlock().getRelative(3, 0, 7).setType(Material.FURNACE);
		Furnace furnace = (Furnace) start.getBlock().getRelative(3, 0, 7)
				.getState();
		Inventory furnaceInventory = furnace.getInventory();
		furnaceInventory.setItem(1, new ItemStack(Material.COAL, 64));
		furnace.update(true);
		start.getBlock().getRelative(4, 0, 7).setType(Material.CAULDRON);
		start.getBlock().getRelative(1, 0, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(2, 0, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(0, 1, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(0, 2, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(0, 3, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(1, 4, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(2, 4, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(3, 1, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(3, 2, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(3, 3, 8).setType(Material.OBSIDIAN);
		start.getBlock().getRelative(1, 1, 8).setType(Material.AIR);
		start.getBlock().getRelative(1, 2, 8).setType(Material.AIR);
		start.getBlock().getRelative(1, 3, 8).setType(Material.AIR);
		start.getBlock().getRelative(2, 1, 8).setType(Material.AIR);
		start.getBlock().getRelative(2, 2, 8).setType(Material.AIR);
		start.getBlock().getRelative(2, 3, 8).setType(Material.AIR);
	}

	public static void craterWrite (Location location) throws IOException{
		if (new File("plugins/CakesMinerApocalypse/").mkdirs())
			System.out.println("crater file created");
		File myFile = new File("plugins/CakesMinerApocalypse/craters.txt");
		if (!myFile.exists()){
			PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/craters.txt");
			System.out.println("crater file created");
			outputFile.close();
		}
		FileWriter fWriter = new FileWriter("plugins/CakesMinerApocalypse/craters.txt", true);
		PrintWriter outputFile = new PrintWriter(fWriter);
		outputFile.println(location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ());
		outputFile.close();
	}
	public static void craterTimeWrite () throws IOException{
		if (new File("plugins/CakesMinerApocalypse/").mkdirs())
			System.out.println("crater time file created");
		File myFile = new File("plugins/CakesMinerApocalypse/craterTimes.txt");
		if (!myFile.exists()){
			PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/craterTimes.txt");
			System.out.println("crater time file created");
			outputFile.close();
		}
		FileWriter fWriter = new FileWriter("plugins/CakesMinerApocalypse/craterTimes.txt", true);
		PrintWriter outputFile = new PrintWriter(fWriter);
		java.util.Date now = new Date();
		outputFile.println(now.getTime() - 1234800000);
		outputFile.close();
	}
}
