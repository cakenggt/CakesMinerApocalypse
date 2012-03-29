package me.cakenggt.CakesMinerApocalypse;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class CakesMinerApocalypse extends JavaPlugin {
	private Listener playerListener;
	private Listener moveListener;
	private Listener blockListener;
	private Listener chunkListener;
	private Listener nukeListener;
	private CakesMinerApocalypseBroadcast broadcast;
	private int size = 10000;
	private Map<World, Boolean> worldsTable = new HashMap<World, Boolean>();
	private List<Location> craters;
	private List<Location> GECKs;
	private List<Long> craterTimes;
	private List<Location> radios;
	private boolean apocalypseDamage = true;
	private boolean randomSpawn = true;
	private double shelterChance = 0.001;
	private double craterChance = 0.001;
	private int pipboyID = 345;
	private int chatDistance = 50;
	private final String configname = "config.yml";
    
    public void onDisable() {
        // TODO: Place any custom disable code here.
        System.out.println(this + " is now disabled!");
    }
    
    public void onEnable() {
    	playerListener = new CakesMinerApocalypsePlayerLogin(this);
    	moveListener = new CakesMinerApocalypsePlayerMovement(this);
    	blockListener = new CakesMinerApocalypseBlockListener(this);
    	chunkListener = new CakesMinerApocalypseVaultCreator(this);
    	nukeListener = new CakesMinerApocalypseNuke(this);
    	if(!loadConfig()) {
    		System.out.println(this + " has encountered an error while reading the configuration file," 
    				+ " continuing with defaults");
    	}
    	try {
			loadCraters();
			loadGECKs();
			loadCraterTimes();
			loadRadios();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	getServer().getPluginManager().registerEvents(playerListener, this);
    	getServer().getPluginManager().registerEvents(moveListener, this);
    	getServer().getPluginManager().registerEvents(blockListener, this);
    	getServer().getPluginManager().registerEvents(chunkListener, this);
    	getServer().getPluginManager().registerEvents(nukeListener, this);
    	this.broadcast = new CakesMinerApocalypseBroadcast(this);
    	try {
			checkGECKs();
			checkRadios();
		} catch (IOException e) {
			//e.printStackTrace();
		}
    	final ShapedRecipe gRecipe = new ShapedRecipe(new ItemStack(Material.SPONGE, 1));
        gRecipe.shape("CBC", "BAB", "CBC");
        gRecipe.setIngredient('A', Material.DIAMOND_BLOCK);
        gRecipe.setIngredient('B', Material.IRON_BLOCK);
        gRecipe.setIngredient('C', Material.REDSTONE);
        final FurnaceRecipe fRecipe = new FurnaceRecipe(new ItemStack(Material.GRAVEL, 1), Material.SNOW_BLOCK);
        final FurnaceRecipe aRecipe = new FurnaceRecipe(new ItemStack(Material.CHAINMAIL_HELMET, 1), Material.GOLD_HELMET);
        final FurnaceRecipe bRecipe = new FurnaceRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), Material.GOLD_CHESTPLATE);
        final FurnaceRecipe cRecipe = new FurnaceRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), Material.GOLD_LEGGINGS);
        final FurnaceRecipe dRecipe = new FurnaceRecipe(new ItemStack(Material.CHAINMAIL_BOOTS, 1), Material.GOLD_BOOTS);
        getServer().addRecipe(gRecipe);
        getServer().addRecipe(fRecipe);
        getServer().addRecipe(aRecipe);
        getServer().addRecipe(bRecipe);
        getServer().addRecipe(cRecipe);
        getServer().addRecipe(dRecipe);
        setupRefresh();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.broadcast, 20L, 100L);
        alternateWorlds();
        System.out.println(this + " is now enabled!");
    }

    private void setupRefresh() {
    	Timer refreshTimer = new Timer();
	     refreshTimer.scheduleAtFixedRate(new TimerTask() {
	       public void run() {
	    	   Player[] players = getServer().getOnlinePlayers();
	       		for (Player p : players) {
	       			p.setSneaking(false);
	       			p.setSneaking(true);
	       		}
	       	
	       }
	     }
	     , 500L, 5000L);
	   }
    
	private boolean loadConfig() {
	    YamlConfiguration config = new YamlConfiguration();
	    getDataFolder().mkdirs();
	    File configfile = new File(getDataFolder(),configname);
	    try {
	      config.load(configfile);
	    } catch (FileNotFoundException e) {
	      config.set("size", size);
	      for (World world : getServer().getWorlds()){
	    	  config.set("worlds." + world.getName(), true);
	      }
	      config.set("randomSpawn", true);
	      config.set("apocalypseDamage", apocalypseDamage);
	      config.set("shelterChance", shelterChance);
	      config.set("craterChance", craterChance);
	      config.set("pipboyID", pipboyID);
	      config.set("chatDistance", chatDistance);
	      try {
	        config.save(configfile);
	      } catch (IOException e1) {
	        System.out.println(this + " was unable to create the default config file!");
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	      return false;
	    } catch (InvalidConfigurationException e) {
	      e.printStackTrace();
	      return false;
	    }
	    this.setSize(config.getInt("size", this.getSize()));
	    for (World world : getServer().getWorlds()){
	    	this.setOn(world, config.getBoolean("worlds." + world.getName(), false));
	    }
	    this.setRandomSpawn(config.getBoolean("randomSpawn", this.getRandomSpawn()));
	    this.setApocalypseDamage(config.getBoolean("apocalypseDamage", this.getApocalypseDamage()));
	    this.setShelterChance(config.getDouble("shelterChance", this.getShelterChance()));
	    this.setCraterChance(config.getDouble("craterChance", this.getCraterChance()));
	    this.setPipboyID(config.getInt("pipboyID", this.getPipboyID()));
	    this.setChatDistance(config.getInt("chatDistance", this.getChatDistance()));
	    return true;
	  }

	public void checkGECKs() throws IOException {
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
		inputFile.close();
		PrintWriter outputFile = new PrintWriter(myFile);
		if (GECKs == null){
			outputFile.close();
			return;
		}
		for (Location GECK : GECKs){
			Block block = GECK.getBlock();
			if (block.getRelative(BlockFace.NORTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.SOUTH).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.EAST).getType() == Material.PISTON_BASE && block.getRelative(BlockFace.WEST).getType() == Material.PISTON_BASE){
				outputFile.println(block.getWorld().getName() + " " + block.getX() + " " + block.getY() + " " + block.getZ());
			}
		}
		outputFile.close();
	}
	
	public void checkRadios() throws IOException {
		if (new File("plugins/CakesMinerApocalypse/").mkdirs())
			System.out.println("radio file created");
		File myFile = new File("plugins/CakesMinerApocalypse/radios.txt");
		if (!myFile.exists()){
			PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/radios.txt");
			System.out.println("radio file created");
			outputFile.close();
		}
		Scanner inputFile = new Scanner(myFile);
		List<Location> radios = new ArrayList<Location>();
		while (inputFile.hasNextLine()){
			Location a = new Location(Bukkit.getServer().getWorld(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()));
			radios.add(a);
			inputFile.nextLine();
		}
		inputFile.close();
		PrintWriter outputFile = new PrintWriter(myFile);
		if (radios == null){
			outputFile.close();
			return;
		}
		for (Location radio : radios){
			Block block = radio.getBlock();
			if (block.isBlockIndirectlyPowered()){
				outputFile.println(block.getWorld().getName() + " " + block.getX() + " " + block.getY() + " " + block.getZ());
			}
		}
		outputFile.close();
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public void setApocalypseDamage(boolean apocalypseDamage) {
		this.apocalypseDamage = apocalypseDamage;
	}
	
	public boolean getApocalypseDamage() {
		return apocalypseDamage;
	}

	public void setOn(World world, boolean isOn){
		this.worldsTable.put(world, isOn);
	}

	public Map<World, Boolean> getOn(){
		return worldsTable;
	}

	public void setRandomSpawn(boolean randomSpawn){
		this.randomSpawn = randomSpawn;
	}
	
	public boolean getRandomSpawn() {
		return randomSpawn;
	}
	
	public void setShelterChance(double shelterChance) {
		this.shelterChance = shelterChance;
	}
	
	public double getShelterChance() {
		return shelterChance;
	}
	
	public void setCraterChance(double craterChance) {
		this.craterChance = craterChance;
	}
	
	public double getCraterChance() {
		return craterChance;
	}
	
	public void setPipboyID(int id) {
		this.pipboyID = id;
	}
	
	public int getPipboyID() {
		return pipboyID;
	}
	
	public void setChatDistance(int distance) {
		this.chatDistance = distance;
	}
	
	public int getChatDistance() {
		return chatDistance;
	}
	
	public void loadCraters() throws IOException {
		File myFile = new File("plugins/CakesMinerApocalypse/craters.txt");
		Scanner inputFile = new Scanner(myFile);
		List<Location> craters = new ArrayList<Location>();
		while (inputFile.hasNextLine()){
			Location a = new Location(Bukkit.getServer().getWorld(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()));
			craters.add(a);
			inputFile.nextLine();
		}
		inputFile.close();
		this.craters = craters;
	}
	public List<Location> getCraters() {
		return craters;
	}
	public void loadGECKs() throws IOException{
		File myFile = new File("plugins/CakesMinerApocalypse/GECKs.txt");
		Scanner inputFile = new Scanner(myFile);
		List<Location> GECKs = new ArrayList<Location>();
		while (inputFile.hasNextLine()){
			Location a = new Location(Bukkit.getServer().getWorld(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()));
			GECKs.add(a);
			inputFile.nextLine();
		}
		inputFile.close();
		this.GECKs = GECKs;
	}
	public List<Location> getGECKs() {
		return GECKs;
	}
	public void loadCraterTimes() throws IOException {
		File myFile = new File("plugins/CakesMinerApocalypse/craterTimes.txt");
		Scanner inputFile = new Scanner(myFile);
		List<Long> craterTimes = new ArrayList<Long>();
		while (inputFile.hasNextLine()){
			Long a = Long.valueOf(inputFile.next());
			craterTimes.add(a);
			inputFile.nextLine();
		}
		inputFile.close();
		this.craterTimes = craterTimes;
	}
	public List<Long> getCraterTimes() {
		return craterTimes;
	}
	public void loadRadios() throws IOException {
		File myFile = new File("plugins/CakesMinerApocalypse/radios.txt");
		Scanner inputFile = new Scanner(myFile);
		List<Location> radios = new ArrayList<Location>();
		while (inputFile.hasNextLine()){
			Location a = new Location(Bukkit.getServer().getWorld(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()), Double.valueOf(inputFile.next()));
			radios.add(a);
			inputFile.nextLine();
		}
		inputFile.close();
		this.radios = radios;
	}
	public List<Location> getRadios() {
		return radios;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	   	if(cmd.getName().equalsIgnoreCase("radio")){ // If the player typed /radio then do the following...
	   		Player player = null;
    		if (sender instanceof Player) {
    			player = (Player) sender;
	    	}
	   		if (player == null) {
	   			sender.sendMessage("this command can only be run by a player");
	   			return false;
	   		}
	   		if (player.getItemInHand().getTypeId() != getPipboyID()){
	   			player.sendMessage("You must be holding a compass to work the radio");
	   			return true;
	   		}
    		if (args.length != 1){
	    		sender.sendMessage(cmd.getUsage());
	   			return true;
	   		}
    		if (args[0].equalsIgnoreCase("scan")){
    			try {
					setFrequency(sender, args[0].toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
    		}
    		if (args[0].equalsIgnoreCase("version")){
    			String name = this.toString();
    			player.sendMessage(name);
    			return true;
    		}
    		try {
				Double.parseDouble(args[0].toString());
		    }
			catch (NumberFormatException e) {
				sender.sendMessage(cmd.getUsage());
				return true;
		    }
	   		String inputString = args[0].toString();
	   		try {
	   			setFrequency(sender, inputString);
    		} catch (IOException e) {
	    		e.printStackTrace();
    		}
    		return true;
	    }
	   	if(cmd.getName().equalsIgnoreCase("support")){
	   		String message = "";
	   		for (String part : args){
	   			message = message + " " + part;
	   		}
	   		System.out.println("[Support Chat] " + sender.getName() + ":" + message);
	   		Player[] recipientsArray = sender.getServer().getOnlinePlayers();
			for (int i = 0; i < recipientsArray.length; i ++){
				if (recipientsArray[i].isOp()){
					recipientsArray[i].sendMessage(ChatColor.BLUE + "[Support Chat] " + sender.getName() + ":" + message);
				}
			}
	   		return true;
	   	}
	    	return false;
	}
	    
	   public void setFrequency (CommandSender sender, String inputString) throws IOException{
		   if (new File("plugins/CakesMinerApocalypse/").mkdirs())
				System.out.println("Frequencies file created");
		   File myFile = new File("plugins/CakesMinerApocalypse/frequencies.txt");
		   if (myFile.exists()){
			   Scanner inputFileCheck = new Scanner(myFile);
	           int j = 0;
	           while (inputFileCheck.hasNext()) {
            	inputFileCheck.nextLine();
            	j++;
	           }
	           int size = (j + 1) / 2;
	           String[] nameArray = new String[size];
	           String[] circleArray = new String[size];
	           inputFileCheck.close();
	           Scanner inputFile = new Scanner(myFile);
	           for (int i = 0; i < size; i++) {
	               nameArray[i] = inputFile.nextLine();
	               circleArray[i] = inputFile.nextLine();
	           }
	           boolean isInFile = false;
	           for (int i = 0; i < size; i++) {
	               if (nameArray[i].equalsIgnoreCase(sender.getName())){
	               		circleArray[i] = inputString;
	               		sender.sendMessage("Frequency " + inputString + " set successfully!");
	               		isInFile = true;
	               }
	           }
	           inputFile.close();
	           PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/frequencies.txt");
	           for (int i = 0; i < size; i++) {
	               outputFile.println(nameArray[i]);
	               outputFile.println(circleArray[i]);
	           }
	           if (!isInFile){
	        	   outputFile.println(sender.getName());
	               outputFile.println(inputString);
	           }
	           outputFile.close();
		}
		else{
			PrintWriter outputFile = new PrintWriter("plugins/CakesMinerApocalypse/frequencies.txt");
	        outputFile.println(sender.getName());
	        outputFile.println(inputString);
	        sender.sendMessage("frequency " + inputString + " set successfully!");
	        outputFile.close();
		}
    }
	   
	   public void alternateWorlds (){
		   List<World> worlds = getServer().getWorlds();
		   for (World world : worlds){
			   if (this.worldsTable.get(world)){
				   if (getServer().getWorld(world.getName() + "Alternate") == null){
					   WorldCreator creator = new WorldCreator(world.getName() + "Alternate");
					   creator.copy(world);
					   creator.createWorld();
					   this.setOn(getServer().getWorld(world.getName() + "Alternate"), true);
				   }
				   else{
					   this.setOn(getServer().getWorld(world.getName() + "Alternate"), true);
				   }
			   }
		   }
	   }
}