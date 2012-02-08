package me.cakenggt.CakesMinerApocalypse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class CakesMinerApocalypseNuke implements Listener {
	CakesMinerApocalypse p;
	public CakesMinerApocalypseNuke(CakesMinerApocalypse plugin) {
		p = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void smeltEvent(FurnaceSmeltEvent event) {
		//System.out.println("furnace smelt event");
		if (event.getSource().getType() != Material.SNOW_BLOCK){
			//System.out.println("not snow");
			//System.out.println(event.getResult().getTypeId());
			return;
		}
		double random = Math.random() * 10000.0;
		//System.out.println(random);
		if (random < 1){
			//System.out.println("smelting succeed");
			Furnace furnace = (Furnace) event.getFurnace().getState();
			Inventory furnaceInventory = furnace.getInventory();
			furnaceInventory.setItem(2, new ItemStack(Material.SNOW, 1));
			furnace.update(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void nukeAugment(EntityExplodeEvent event) throws IOException{
		//System.out.println("explode event");
		List<Block> exploded = event.blockList();
		int amount = 0;
		for (Block b : exploded){
			if(b.getState() instanceof ContainerBlock){
				//System.out.println("was a containerblock");
			    ContainerBlock container = (ContainerBlock)b.getState();
			    Inventory bInventory = container.getInventory();
			    ItemStack[] contents = bInventory.getContents();
			    for (ItemStack a : contents){
			    	if (a != null){
			    		if (a.getType() == Material.SNOW){
			    			amount += a.getAmount();
			    			//System.out.println(a.getAmount() + " snow");
			    		}
			    	}
			    }
			    if (bInventory.contains(Material.SNOW))
			    	b.setType(Material.AIR);
			}
		}
		if (amount == 0)
			return;
		event.getLocation().getWorld().createExplosion(event.getLocation(), 32F * amount);
		for (int i = 0; i < amount; i++){
			craterWrite(event.getLocation());
		}
		System.out.println("Nuclear detonation of yield " + amount + "Mt! at " + event.getLocation().getX() + " " + event.getLocation().getZ());
		this.p.loadCraters();
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
}