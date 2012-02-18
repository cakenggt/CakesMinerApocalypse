package me.cakenggt.CakesMinerApocalypse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

class CakesMinerApocalypseBroadcast
  implements Runnable
{
  CakesMinerApocalypse p;
  public CakesMinerApocalypseBroadcast(CakesMinerApocalypse plugin) {
	  p = plugin;
  }

  public void run() {
	  //System.out.println("broadcast run");
	  List<Location> radios = p.getRadios();
	  if (radios == null)
		  return;
	  for (Location radioA : radios){
		  Block radioB = radioA.getBlock();
		  if (radioB.isBlockIndirectlyPowered()){
			  Block actBlock = radioB;
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
				while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
					ironBarCount++;
					curBlock = curBlock.getRelative(0, 1, 0);
				}
				if (ironBarCount > 16)
					ironBarCount = 16;
				double broadcastDistance = (ironBarCount * 30) + (broadcastGarble * 550);
				if (radioB.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radioB.getRelative(BlockFace.NORTH).getState();
					//String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					//radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					//radioSign.update(true);
					try {
						double frequency = Double.parseDouble(radioSign.getLine(0));
						String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
						broadcast(frequency, message, broadcastDistance, broadcastGarble, radioB);
				    }
					catch (NumberFormatException e) {
				    }
				}
				if (radioB.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radioB.getRelative(BlockFace.SOUTH).getState();
					//String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					//radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					//radioSign.update(true);
					try {
						double frequency = Double.parseDouble(radioSign.getLine(0));
						String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
						broadcast(frequency, message, broadcastDistance, broadcastGarble, radioB);
				    }
					catch (NumberFormatException e) {
				    }
				}
				if (radioB.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radioB.getRelative(BlockFace.EAST).getState();
					//String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					//radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					//radioSign.update(true);
					try {
						double frequency = Double.parseDouble(radioSign.getLine(0));
						String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
						broadcast(frequency, message, broadcastDistance, broadcastGarble, radioB);
				    }
					catch (NumberFormatException e) {
				    }
				}
				if (radioB.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN){
					Sign radioSign = (Sign) radioB.getRelative(BlockFace.WEST).getState();
					//String radioLoc = radio.getX()+ "" + radio.getY() + "" + radio.getZ();
					//radioSign.setLine(0, Integer.toString(radioLoc.hashCode()));
					//radioSign.update(true);
					try {
						double frequency = Double.parseDouble(radioSign.getLine(0));
						String message = radioSign.getLine(1) + " " + radioSign.getLine(2) + " " + radioSign.getLine(3);
						broadcast(frequency, message, broadcastDistance, broadcastGarble, radioB);
				    }
					catch (NumberFormatException e) {
				    }
				}
		  }
	  }
  }
  public void broadcast (double frequency, String message, double broadcastDistance, double broadcastGarble, Block radio){
		List<Player> recipients = radio.getWorld().getPlayers();
		Player[] recipientsArray = recipients.toArray(new Player[recipients.size()]);
		for (int i = 0; i < recipientsArray.length; i ++){
			if (recipientsArray[i].getLocation().getWorld() == radio.getLocation().getWorld()) {
				double distance = recipientsArray[i].getLocation().distance(
						radio.getLocation());
				if (recipientsArray[i].getWorld().hasStorm())
					distance = 2 * distance;
				if (recipientsArray[i].getWorld().isThundering())
					distance = 0;
				int ironBarCount = 0;
				if (recipientsArray[i].getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.IRON_FENCE){
					ironBarCount ++;
					Block curBlock = recipientsArray[i].getLocation().getBlock().getRelative(1, 0, 0);
					while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
						ironBarCount++;
						curBlock = curBlock.getRelative(0, 1, 0);
					}
				}
				if (recipientsArray[i].getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.IRON_FENCE){
					ironBarCount ++;
					Block curBlock = recipientsArray[i].getLocation().getBlock().getRelative(0, 0, 1);
					while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
						ironBarCount++;
						curBlock = curBlock.getRelative(0, 1, 0);
					}
				}
				if (recipientsArray[i].getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.IRON_FENCE){
					ironBarCount ++;
					Block curBlock = recipientsArray[i].getLocation().getBlock().getRelative(-1, 0, 0);
					while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
						ironBarCount++;
						curBlock = curBlock.getRelative(0, 1, 0);
					}
				}
				if (recipientsArray[i].getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.IRON_FENCE){
					ironBarCount ++;
					Block curBlock = recipientsArray[i].getLocation().getBlock().getRelative(0, 0, -1);
					while (curBlock.getRelative(0, 1, 0).getType() == Material.IRON_FENCE) {
						ironBarCount++;
						curBlock = curBlock.getRelative(0, 1, 0);
					}
				}
				distance -= ironBarCount * 30;
				if (distance <= broadcastGarble * broadcastDistance) {
					boolean frequencyMatch = false;
					try {
						frequencyMatch = checkFrequency(recipientsArray[i],
								frequency);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (frequencyMatch) {
						recipientsArray[i].sendMessage(ChatColor.RED
								+ "[Radio " + frequency + "] " + message);
					}
				} else if (distance > broadcastGarble * broadcastDistance
						&& distance <= broadcastDistance) {
					boolean frequencyMatch = false;
					try {
						frequencyMatch = checkFrequency(recipientsArray[i],
								frequency);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (frequencyMatch) {
						int messageLength = message.length();
						double percent = (distance - (broadcastGarble * broadcastDistance))
								/ ((1 - broadcastGarble) * broadcastDistance);
						int amountRemoved = (int) (percent * ((double) messageLength));
						char[] charString = message.toCharArray();
						for (int k = 0; k < amountRemoved; k++) {
							int removalPoint = (int) (Math.random() * (charString.length - 1));
							charString[removalPoint] = ' ';
						}
						String charMessage = new String(charString);
						recipientsArray[i].sendMessage(ChatColor.RED
								+ "[Radio " + frequency + "] " + charMessage);
					}
				}
			}
		}
	}
	
	public boolean checkFrequency(Player player, double frequency) throws IOException{
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
			try {
		        Double.parseDouble(setFrequency);
		    } catch (NumberFormatException e) {
		        return false;
		    }
			return (Double.parseDouble(setFrequency) == frequency) && player.getInventory().contains(this.p.getPipboyID());
		}
		else
			return false;
	}
}