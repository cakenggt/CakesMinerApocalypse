package me.cakenggt.CakesMinerApocalypse;

import java.util.Date;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class CakesMinerApocalypsePlayerMovement implements Listener {
	CakesMinerApocalypse p;
	public CakesMinerApocalypsePlayerMovement(CakesMinerApocalypse plugin) {
		p = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!this.p.getOn().get(event.getPlayer().getWorld())){
			return;
		}
		Location to = event.getTo();
		//try {
		//	size = mapSize();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		int size = this.p.getSize();
		double halfSize = size /2;
		double eigthSize = halfSize/8;
		if (to.getWorld().getEnvironment() == Environment.NETHER){
			if (to.getX() < -1 * eigthSize) {
				to = to.add(size, 0, 0);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getX() > eigthSize) {
				to = to.add(-1 * size, 0, 0);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getZ() < -1 * eigthSize) {
				to = to.add(0, 0, size);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getZ() > eigthSize) {
				to = to.add(0, 0, -1 * size);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
		}
		else {
			if (to.getX() < -1 * halfSize) {
				to = to.add(size, 0, 0);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getX() > halfSize) {
				to = to.add(-1 * size, 0, 0);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getZ() < -1 * halfSize) {
				to = to.add(0, 0, size);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
			if (to.getZ() > halfSize) {
				to = to.add(0, 0, -1 * size);
				to.getWorld().loadChunk((int) to.getX(), (int) to.getZ(), true);
				to.setY(to.getWorld().getHighestBlockAt(to).getY());
				event.getPlayer().teleport(to);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void apocalypseDamage(PlayerMoveEvent event) {
		if (!this.p.getApocalypseDamage())
			return;
		Player player = event.getPlayer();
		ItemStack[] inventoryArray = player.getInventory().getContents();
		Location loc = player.getLocation();
		List<Location> craters = this.p.getCraters();
		List<Location> GECKs = this.p.getGECKs();
		List<Long> craterTimes = this.p.getCraterTimes();
		double damageChance = 0;
		double geck = 0;
		if ((loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.STATIONARY_WATER) && !player.isInsideVehicle()){
		    //System.out.println("is in water");
            if (this.p.getConfig().getBoolean("apocalypseDamageWater", true)) {
                damageChance += .05;
            }
		}
		if (loc.getWorld().getHighestBlockYAt(loc) <= loc.getY() + 1 && (loc.getWorld().hasStorm() || loc.getWorld().isThundering())){
            if (this.p.getConfig().getBoolean("apocalypseDamageAcidRain", true)) {
                damageChance += .05;
            }
		}
		java.util.Date now = new Date();
		if (craters != null) {
			for (Location crater : craters) {
				if (crater.getWorld() == loc.getWorld()) {
					//damageChance += 1/(crater.distanceSquared(loc)/50);
					//double dam = 1/(crater.distanceSquared(loc)/50);
					//System.out.println("distance " + crater.distance(loc));
					//System.out.println("normal damage " + dam);
					int index = craters.lastIndexOf(crater);
					Long history = now.getTime()- craterTimes.get(index);
					//System.out.println("age " + history.doubleValue()/(1000*60*60));
					damageChance += (1000/ (crater.distanceSquared(loc) / 50))* (1/ (Math.pow(10, (Math.log10(history.doubleValue()/(1000*60*60)) / Math.log10(7)))));
					//System.out.println("damageChance " + damageChance);
				}
			}
		}
		if (GECKs != null){
			for (Location GECK : GECKs){
				Block block = GECK.getBlock();
				if (block.isBlockIndirectlyPowered() && block.getRelative(BlockFace.NORTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.SOUTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.EAST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.WEST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.NORTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.SOUTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.EAST).isBlockIndirectlyPowered() && block.getRelative(BlockFace.WEST).isBlockIndirectlyPowered()){
					if (GECK.getWorld() == loc.getWorld())
						geck += 1/(GECK.distanceSquared(loc)/50);
				}
			}
		}
		if (!(loc.getWorld().getHighestBlockYAt(loc) <= loc.getY() + 1) && (loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.STATIONARY_WATER)){
			//System.out.println("under a block and not in water");
			damageChance = 0;
		}
		for (ItemStack a : inventoryArray){
			if (a != null){
				if (a.getType() == Material.SNOW){
					damageChance += .05 * a.getAmount();
				}
			}
		}
		//player.sendMessage("damage Chance = " + (damageChance - geck));
		if (damageChance >= 50){
            ItemStack[] armor = player.getInventory().getArmorContents();
            int bar = 1;
            for (ItemStack armors : armor){
                if (armors.getType() == Material.CHAINMAIL_BOOTS || armors.getType() == Material.CHAINMAIL_CHESTPLATE || armors.getType() == Material.CHAINMAIL_HELMET || armors.getType() == Material.CHAINMAIL_LEGGINGS)
                    bar = bar * 1;
                else
                    bar = bar * 0;
            }
            if (this.p.getConfig().getBoolean("alternateWorldsTemporalMesh", true)) {
                if (player.getWorld().getName().endsWith("Alternate")){
                    World altWorld = this.p.getServer().getWorld(player.getWorld().getName().substring(0, player.getLocation().getWorld().getName().length() - 9));
                    Location playerLoc = player.getLocation();
                    playerLoc.setWorld(altWorld);
                    player.teleport(playerLoc);
                }
                else{
                    World altWorld = this.p.getServer().getWorld(player.getWorld().getName() + "Alternate");
                    Location playerLoc = player.getLocation();
                    playerLoc.setWorld(altWorld);
                    player.teleport(playerLoc);
                }
            }
            if (this.p.getConfig().getBoolean("alternateWorldsPotionEffects", true)) {
                if (bar == 0){
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 24000, 4));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 24000, 4));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 24000, 4));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 24000, 4));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 24000, 4));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 24000, 4));
                }
            }
		}
		if (this.p.getConfig().getBoolean("alternateWorldsFixedTime", true) && player.getWorld().getName().endsWith("Alternate")){
			//player.getWorld().setTime(18000);
			player.getWorld().setTime(6000);
			byte light = player.getLocation().getBlock().getLightLevel();
			double dLight = (double) light;
			damageChance += .09 - (dLight/100);
			//player.sendMessage("" + light + " " + damageChance);
		}
		if (Math.random() <= damageChance - geck){
			PlayerInventory inventory = player.getInventory();
			ItemStack[] armor = inventory.getArmorContents();
			if (player.getItemInHand().getType() == Material.COMPASS){
				player.playEffect(loc, Effect.CLICK1, 0);
				//System.out.println("tick");
			}
			ItemStack[] inventoryA = inventory.getContents();
			for (ItemStack poss : inventoryA){
				if (poss != null) {
					if (poss.getType() == Material.CHAINMAIL_BOOTS
							|| poss.getType() == Material.CHAINMAIL_CHESTPLATE
							|| poss.getType() == Material.CHAINMAIL_HELMET
							|| poss.getType() == Material.CHAINMAIL_LEGGINGS) {
						poss.setDurability((short) ((short) poss
								.getDurability() - 2));
					}
				}
			}
			inventory.setContents(inventoryA);
			//player.sendMessage("armor length = " + armor.length);
			int whichArmor = (int)(Math.random() * armor.length);
			if (armor[whichArmor].getAmount() == 0){
				player.damage(1);
			}
			else
				armor[whichArmor].setDurability((short) ((short) armor[whichArmor].getDurability() + 2));
			if (armor[whichArmor].getType() == Material.CHAINMAIL_HELMET)
				armor[whichArmor].setDurability((short) ((short) armor[whichArmor].getDurability() + 2));
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_CHESTPLATE)
				armor[whichArmor].setDurability((short) ((short) armor[whichArmor].getDurability() + 2));
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_BOOTS)
				armor[whichArmor].setDurability((short) ((short) armor[whichArmor].getDurability() + 2));
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_LEGGINGS)
				armor[whichArmor].setDurability((short) ((short) armor[whichArmor].getDurability() + 2));
			if (armor[whichArmor].getType() == Material.LEATHER_HELMET && armor[whichArmor].getDurability() > 56)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.LEATHER_CHESTPLATE && armor[whichArmor].getDurability() > 81)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.LEATHER_LEGGINGS && armor[whichArmor].getDurability() > 76)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.LEATHER_BOOTS && armor[whichArmor].getDurability() > 66)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.GOLD_HELMET && armor[whichArmor].getDurability() > 78)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.GOLD_CHESTPLATE && armor[whichArmor].getDurability() > 113)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.GOLD_LEGGINGS && armor[whichArmor].getDurability() > 106)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.GOLD_BOOTS && armor[whichArmor].getDurability() > 92)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.IRON_HELMET && armor[whichArmor].getDurability() > 166)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.IRON_CHESTPLATE && armor[whichArmor].getDurability() > 241)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.IRON_LEGGINGS && armor[whichArmor].getDurability() > 226)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.IRON_BOOTS && armor[whichArmor].getDurability() > 196)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_HELMET && armor[whichArmor].getDurability() > 166)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_CHESTPLATE && armor[whichArmor].getDurability() > 241)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_LEGGINGS && armor[whichArmor].getDurability() > 226)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.CHAINMAIL_BOOTS && armor[whichArmor].getDurability() > 196)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.DIAMOND_HELMET && armor[whichArmor].getDurability() > 364)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.DIAMOND_CHESTPLATE && armor[whichArmor].getDurability() > 529)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.DIAMOND_LEGGINGS && armor[whichArmor].getDurability() > 496)
				armor[whichArmor].setAmount(0);
			else if (armor[whichArmor].getType() == Material.DIAMOND_BOOTS && armor[whichArmor].getDurability() > 430)
				armor[whichArmor].setAmount(0);
			inventory.setArmorContents(armor);
		}
		if (damageChance < 0 && (Math.random() * -1) >= damageChance && player.getHealth() < 20){
			player.setHealth(player.getHealth() + 1);
		}
		if (damageChance < 0 && (Math.random() * -1) >= damageChance && player.getFoodLevel() < 20){
			player.setFoodLevel(player.getFoodLevel() + 1);
			player.setSaturation(player.getFoodLevel());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void geigerCounter(PlayerInteractEvent event){
		if (!this.p.getApocalypseDamage())
			return;
		if (!((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getTypeId() == this.p.getPipboyID()))
			return;
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		ItemStack[] inventoryArray = player.getInventory().getContents();
		List<Location> craters = this.p.getCraters();
		List<Location> GECKs = this.p.getGECKs();
		List<Long> craterTimes = this.p.getCraterTimes();
		double damageChance = 0;
		double geck = 0;
		if ((loc.getBlock().getType() == Material.WATER || loc.getBlock().getType() == Material.STATIONARY_WATER) && !player.isInsideVehicle()){
		    //System.out.println("is in water");
            if (this.p.getConfig().getBoolean("apocalypseDamageWater", true)) {
                damageChance += .05;
            }
		}
		if (loc.getWorld().getHighestBlockYAt(loc) <= loc.getY() && (loc.getWorld().hasStorm() || loc.getWorld().isThundering())){
            if (this.p.getConfig().getBoolean("apocalypseDamageAcidRain", true)) {
                damageChance += .05;
            }
		}
		//System.out.println(damageChance);
		java.util.Date now = new Date();
		if (craters != null) {
			for (Location crater : craters) {
				if (crater.getWorld() == loc.getWorld()) {
					//damageChance += 1/(crater.distanceSquared(loc)/50);
					//double dam = 1/(crater.distanceSquared(loc)/50);
					//System.out.println("distance " + crater.distance(loc));
					//System.out.println("normal damage " + dam);
					int index = craters.lastIndexOf(crater);
					Long history = now.getTime()- craterTimes.get(index);
					//System.out.println("age " + history.doubleValue()/(1000*60*60));
					damageChance += (1000/ (crater.distanceSquared(loc) / 50))* (1/ (Math.pow(10, (Math.log10(history.doubleValue()/(1000*60*60)) / Math.log10(7)))));
					//System.out.println("damageChance " + damageChance);
				}
			}
		}
		//System.out.println(damageChance);
		if (GECKs != null){
			for (Location GECK : GECKs){
				Block block = GECK.getBlock();
				if (block.isBlockIndirectlyPowered() && block.getRelative(BlockFace.NORTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.SOUTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.EAST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.WEST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.NORTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.SOUTH).isBlockIndirectlyPowered() && block.getRelative(BlockFace.EAST).isBlockIndirectlyPowered() && block.getRelative(BlockFace.WEST).isBlockIndirectlyPowered()){
					if (GECK.getWorld() == loc.getWorld())
						geck += 1/(GECK.distanceSquared(loc)/50);
				}
			}
		}
		//System.out.println(damageChance);
		if (!(loc.getWorld().getHighestBlockYAt(loc) <= loc.getY()) && (loc.getBlock().getType() != Material.WATER && loc.getBlock().getType() != Material.STATIONARY_WATER)){
			//System.out.println("under a block and not in water");
			damageChance = 0;
		}
		//System.out.println(damageChance);
		for (ItemStack a : inventoryArray){
			if (a != null){
				if (a.getType() == Material.SNOW){
					damageChance += .05 * a.getAmount();
				}
			}
		}
		//System.out.println(damageChance);
		int rad = (int)((damageChance - geck)*1000);
		if (rad < 0)
			rad = 0;
		player.sendMessage(rad + " rads/sec");
	}

	/*
	@EventHandler(priority = EventPriority.HIGHEST)
	public void walternatePlace(BlockCanBuildEvent event){
		//System.out.println("blockcanbuildevent");
		if (!this.p.getOn().get(event.getBlock().getWorld())){
			//System.out.println("returned");
			return;
		}
		Block block = event.getBlock();
		if (event.isBuildable()){
			//System.out.println("buildable");
			if (event.getBlock().getWorld().getName().endsWith("Alternate")){
				World altWorld = this.p.getServer().getWorld(event.getBlock().getWorld().getName().substring(0, block.getLocation().getWorld().getName().length() - 9));
				Location loc = block.getLocation();
				//System.out.println(loc.getWorld().getName() + " " + typeId + " " + data);
				loc.setWorld(altWorld);
				loc.getBlock().setType(event.getMaterial());
				//System.out.println(loc.getWorld().getName() + " " + typeId + " " + data);
			}
			else{
				World altWorld = this.p.getServer().getWorld(event.getBlock().getWorld().getName() + "Alternate");
				Location loc = block.getLocation();
				//System.out.println(loc.getWorld().getName() + " " + typeId + " " + data);
				loc.setWorld(altWorld);
				loc.getBlock().setType(event.getMaterial());
				//System.out.println(loc.getWorld().getName() + " " + typeId + " " + data);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void walternateBreak(BlockBreakEvent event){
		//System.out.println("blockbreakevent");
		if (!this.p.getOn().get(event.getBlock().getWorld())){
			//System.out.println("returned");
			return;
		}
		Block block = event.getBlock();
		if (event.getBlock().getWorld().getName().endsWith("Alternate")){
			World altWorld = this.p.getServer().getWorld(event.getBlock().getWorld().getName().substring(0, block.getLocation().getWorld().getName().length() - 9));
			Location loc = block.getLocation();
			loc.setWorld(altWorld);
			loc.getBlock().setType(Material.AIR);
			//System.out.println(loc.getWorld().getName());
		}
		else{
			World altWorld = this.p.getServer().getWorld(event.getBlock().getWorld().getName() + "Alternate");
			Location loc = block.getLocation();
			loc.setWorld(altWorld);
			loc.getBlock().setType(Material.AIR);
			//System.out.println(loc.getWorld().getName());
		}
	}
	*/
	@EventHandler(priority = EventPriority.HIGHEST)
	public void portalDevice(PlayerInteractEvent event){
		Player player = event.getPlayer();
		ItemStack[] armor = player.getInventory().getArmorContents();
		for (ItemStack armors : armor){
			if (!(armors.getType() == Material.CHAINMAIL_BOOTS || armors.getType() == Material.CHAINMAIL_CHESTPLATE || armors.getType() == Material.CHAINMAIL_HELMET || armors.getType() == Material.CHAINMAIL_LEGGINGS))
				return;
		}
		boolean enoughHel = player.getInventory().getHelmet().getDurability() < 86;
		boolean enoughChe = player.getInventory().getChestplate().getDurability() < 126;
		boolean enoughLeg = player.getInventory().getLeggings().getDurability() < 116;
		boolean enoughBoo = player.getInventory().getBoots().getDurability() < 96;
		if (player.getItemInHand().getType() == Material.WATCH && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& (enoughHel && enoughChe && enoughLeg && enoughBoo)){
			if (player.getWorld().getName().endsWith("Alternate")){
				World altWorld = this.p.getServer().getWorld(player.getWorld().getName().substring(0, player.getLocation().getWorld().getName().length() - 9));
				Location playerLoc = player.getLocation();
				playerLoc.setWorld(altWorld);
				player.teleport(playerLoc);
			}
			else{
				World altWorld = this.p.getServer().getWorld(player.getWorld().getName() + "Alternate");
				Location playerLoc = player.getLocation();
				playerLoc.setWorld(altWorld);
				player.teleport(playerLoc);
			}
			player.getInventory().getHelmet().setDurability((short) ((short) player.getInventory().getHelmet().getDurability() + 80));
			player.getInventory().getChestplate().setDurability((short) ((short) player.getInventory().getChestplate().getDurability() + 115));
			player.getInventory().getLeggings().setDurability((short) ((short) player.getInventory().getLeggings().getDurability() + 110));
			player.getInventory().getBoots().setDurability((short) ((short) player.getInventory().getBoots().getDurability() + 100));
		}
	}
}
