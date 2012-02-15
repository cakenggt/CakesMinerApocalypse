package me.cakenggt.CakesMinerApocalypse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;


public class CakesMinerApocalypseBlockListener implements Listener {
	CakesMinerApocalypse p;
	public CakesMinerApocalypseBlockListener(CakesMinerApocalypse plugin) {
		p = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		if (!this.p.getOn().get(event.getBlock().getWorld())){
			return;
		}
		Block radio = event.getBlock();
		if (event.getBlock().getRelative(1, 0, 0).getType() == Material.JUKEBOX)
			radio = event.getBlock().getRelative(1, 0, 0);
		else if (event.getBlock().getRelative(-1, 0, 0).getType() == Material.JUKEBOX)
			radio = event.getBlock().getRelative(-1, 0, 0);
		else if (event.getBlock().getRelative(0, 0, 1).getType() == Material.JUKEBOX)
			radio = event.getBlock().getRelative(0, 0, 1);
		else if (event.getBlock().getRelative(0, 0, -1).getType() == Material.JUKEBOX)
			radio = event.getBlock().getRelative(0, 0, -1);
		else if (event.getBlock().getType() == Material.WALL_SIGN)
			return;
		else{
			return;
		}
		if (radio.getType() == Material.JUKEBOX && radio.getY() >= 64 && (radio.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN || radio.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN || radio.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN || radio.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN)){
			if (event.getOldCurrent() < event.getNewCurrent()){
				Block actBlock = radio;
				int ironBarCount = 0;
				Block curBlock = actBlock;
				Material baseMaterial = Material.DIRT;
				if (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_BLOCK || curBlock.getRelative(0, 1, 0).getType() == Material.GOLD_BLOCK || curBlock.getRelative(0, 1, 0).getType() == Material.LAPIS_BLOCK || curBlock.getRelative(0, 1, 0).getType() == Material.DIAMOND_BLOCK){
					baseMaterial = curBlock.getRelative(0, 1, 0).getType();
					curBlock = curBlock.getRelative(0, 1, 0);
				}
				double broadcastGarble = 0;
				if (baseMaterial == Material.DIRT)
					broadcastGarble = 0;
				if (baseMaterial == Material.IRON_BLOCK)
					broadcastGarble = 0.15;
				if (baseMaterial == Material.GOLD_BLOCK)
					broadcastGarble = 0.50;
				if (baseMaterial == Material.LAPIS_BLOCK)
					broadcastGarble = 0.30;
				if (baseMaterial == Material.DIAMOND_BLOCK)
					broadcastGarble = 1;
				//okay, so the values are iron 50, lapis 100, gold 150, diamond 300, iron bar 30
				while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
					ironBarCount++;
					curBlock = curBlock.getRelative(0, 1, 0);
				}
				if (ironBarCount > 15)
					ironBarCount = 15;
				double broadcastDistance = (ironBarCount * 30) + (broadcastGarble * 550);
				if (radio.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radio.getRelative(BlockFace.NORTH).getState();
					String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					radioSign.update(true);
					String frequency = radioSign.getLine(0);
					String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
					broadcast(frequency, message, broadcastDistance, broadcastGarble, radio);
				}
				if (radio.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radio.getRelative(BlockFace.SOUTH).getState();
					String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					radioSign.update(true);
					String frequency = radioSign.getLine(0);
					String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
					broadcast(frequency, message, broadcastDistance, broadcastGarble, radio);
				}
				if (radio.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radio.getRelative(BlockFace.EAST).getState();
					String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					radioSign.update(true);
					String frequency = radioSign.getLine(0);
					String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
					broadcast(frequency, message, broadcastDistance, broadcastGarble, radio);
				}
				if (radio.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radio.getRelative(BlockFace.WEST).getState();
					String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					radioSign.update(true);
					String frequency = radioSign.getLine(0);
					String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
					broadcast(frequency, message, broadcastDistance, broadcastGarble, radio);
				}
			}
		}
	}
	
	public void broadcast (String frequency, String message, double broadcastDistance, double broadcastGarble, Block radio){
		List<Player> recipients = radio.getWorld().getPlayers();
		Player[] recipientsArray = recipients.toArray(new Player[recipients.size()]);
		for (int i = 0; i < recipientsArray.length; i ++){
			if (recipientsArray[i].getLocation().getWorld() != radio.getLocation().getWorld())
				return;
			double distance = recipientsArray[i].getLocation().distance(radio.getLocation());
			if (recipientsArray[i].getWorld().hasStorm())
				distance = 2 * distance;
			if (recipientsArray[i].getWorld().isThundering())
				distance = 0;
			if (distance <= broadcastGarble * broadcastDistance){
				boolean frequencyMatch = false;
				try {
					frequencyMatch = checkFrequency(recipientsArray[i], frequency);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (frequencyMatch){
					recipientsArray[i].sendMessage(ChatColor.RED + "[Radio " + frequency + "] " + message);
				}
			}
			else if (distance > broadcastGarble * broadcastDistance && distance <= broadcastDistance){
				boolean frequencyMatch = false;
				try {
					frequencyMatch = checkFrequency(recipientsArray[i], frequency);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (frequencyMatch){
					int messageLength = message.length();
					double percent = (distance - (broadcastGarble * broadcastDistance)) / ((1-broadcastGarble) * broadcastDistance);
					int amountRemoved = (int) (percent * ((double) messageLength));
					char [] charString = message.toCharArray();
					for (int k = 0; k < amountRemoved ; k++){
						int removalPoint = (int) (Math.random() * (charString.length - 1));
						charString[removalPoint] = ' ';
					}
					String charMessage = new String(charString);
					recipientsArray[i].sendMessage(ChatColor.RED + "[Radio " + frequency + "] " + charMessage);
				}
			}
			else
				return;
		}
	}
	
	public boolean checkFrequency(Player player, String frequency) throws IOException{
		File myFile = new File("plugins/CakesMinerApocalypse/frequencies.txt");
		if (myFile.exists()){
			Scanner inputFile = new Scanner(myFile);
			String setFrequency = "";
			while (inputFile.hasNextLine()){
				String name = inputFile.nextLine();
				if (name.equals(player.getName())){
					setFrequency = inputFile.nextLine();
				}
				else
					inputFile.nextLine();
			}
			inputFile.close();
			if (setFrequency.equals("scan") && player.getInventory().contains(this.p.getPipboyID())){
				double random = Math.random() * 100;
				return random <= 1;
			}
			else
				return setFrequency.equals(frequency) && player.getInventory().contains(this.p.getPipboyID());
		}
		else
			return false;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void geckRegister (PlayerInteractEvent event) throws IOException{
		if (!this.p.getOn().get(event.getPlayer().getWorld())){
			return;
		}
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getClickedBlock().getType() != Material.SPONGE)
			return;
		Block block = event.getClickedBlock();
		if (block.isBlockIndirectlyPowered() && block.getRelative(BlockFace.NORTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.SOUTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.EAST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.WEST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.NORTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.SOUTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.EAST).isBlockIndirectlyPowered() && block.getRelative(BlockFace.WEST).isBlockIndirectlyPowered()){
			geckWrite(block.getLocation());
			event.getPlayer().sendMessage("GECK registered successfully!");
			this.p.loadGECKs();
		}
	}
	public static void geckWrite (Location location) throws IOException{
		if (new File("plugins/CakesMinerApocalypse/").mkdirs())
			System.out.println("GECK file created");
		File myFile = new File("plugins/CakesMinerApocalypse/GECKs.txt");
		if (!myFile.exists()){
			PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/GECKs.txt");
			System.out.println("GECK file created");
			outputFile.close();
		}
		Scanner inputFile = new Scanner(myFile);
		List<Location> GECKs = new ArrayList<Location>();
		while (inputFile.hasNextLine()){
			Location a = new Location(Bukkit.getServer().getWorld(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()));
			GECKs.add(a);
			inputFile.nextLine();
		}
		if (GECKs.contains(location))
			return;
		inputFile.close();
		FileWriter fWriter = new FileWriter("plugins/CakesMinerApocalypse/GECKs.txt", true);
		PrintWriter outputFile = new PrintWriter(fWriter);
		outputFile.println(location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ());
		outputFile.close();
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void chunkUnload(ChunkUnloadEvent event){
		Chunk chunk = event.getChunk();
		BlockState[] array = chunk.getTileEntities();
		for (BlockState entity : array){
			if (entity.getType() == Material.WALL_SIGN){
				Block radio = entity.getBlock();
				if (radio.getRelative(BlockFace.NORTH).getType() == Material.JUKEBOX || radio.getRelative(BlockFace.EAST).getType() == Material.JUKEBOX || radio.getRelative(BlockFace.SOUTH).getType() == Material.JUKEBOX || radio.getRelative(BlockFace.WEST).getType() == Material.JUKEBOX){
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void chunkLoad(ChunkLoadEvent event){
		Chunk chunk = event.getChunk();
		int x = Math.abs(chunk.getX() * 16);
		int z = Math.abs(chunk.getZ() * 16);
		if (x > this.p.getSize()/2 + 16 || z > this.p.getSize()/2 + 16){
			chunk.unload();
			return;
		}
	}
}