package models;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public enum Avatar {
	ALDRICH_KILLIAN("aldrich-killian", "Aldrich Killian", true),
	BEETLE("beetle", "Beetle", true),
	BLACK_WIDOW("black-widow", "Black Widow", false),
	CAPTAIN_AMERICA("captain-america", "Captain America", false),
	CYCLOPS("cyclops", "Cyclops", false),
	DEADPOOL("deadpool", "Deadpool", true),
	DOCTOR_DOOM("doctor-doom", "Doctor Doom", true),
	DOCTOR_OCTOPUS("doctor-octopus", "Doctor Octopus", true),
	DRAX("drax", "Drax", false),
	ELECTRO("electro", "Electro", true),
	EXTREMIS_SOLDIER("extremis-soldier", "Extremis Soldier", true),
	FALCON("falcon", "Falcon", false),
	GAMORA("gamora", "Gamora", true),
	GREEN_GOBLIN("green-goblin", "Green Goblin", true),
	GROOT("groot", "Groot", false),
	HAWKEYE("hawkeye", "Hawkeye", false),
	HULK("hulk", "Hulk", false),
	HYDRA_HENCHMAN("hydra-henchman", "Hydra Henchman", true),
	IRON_FIST("iron-fist", "Iron Fist", true),
	IRON_MAN("iron-man", "Iron Man", false),
	J_JONAH_JAMESON("j-jonah-jameson", "J. Jonah Jameson", false),
	LOKI("loki", "Loki", true),
	MAGNETO("magneto", "Magneto", true),
	MARY_JANE_WATSON("mary-jane-watson", "Mary Jane Watson", false),
	MODOK("modok", "MODOK", true),
	NEBULA("nebula", "Nebula", true),
	NICK_FURY("nick-fury", "Nick Fury", false),
	NOVA("nova", "Nova", false),
	PEPPER_POTTS("pepper-potts", "Pepper Potts", false),
	POWER_MAN("power-man", "Power Man", false),
	RED_SKULL("red-skull", "Red Skull", true),
	ROCKET_RACCOON("rocket", "Rocket Raccoon", false),
	RONAN("ronan", "Ronan", true),
	SPIDERMAN("spiderman", "Spiderman", false),
	STARLORD("starlord", "Starlord", false),
	Taskmaster("taskmaster", "Taskmaster", true),
	THE_MANDARIN("the-mandarin", "The Mandarin", true),
	THOR("thor", "Thor", false),
	TONY_STARK("tony-stark", "Tony Stark", false),
	VENOM("venom", "Venom", true),
	WAR_MACHINE("war-machine", "War Machine", false),
	WOLVERINE("wolverine", "Wolverine", false);
		
	private static final String IMAGE_TYPE = ".png";
	private static final String IMAGE_HEAD_SUFFIX = "_head" + IMAGE_TYPE;
	private static final String IMAGE_FULL_SUFFIX = IMAGE_TYPE;
	
	private static Map<String, Avatar> avatars = new TreeMap<>();
	static {
		for(Avatar avatar : Avatar.values())
			avatars.put(avatar.getId(), avatar);
	}
	
	private String id;
	private String name;
	private boolean isVillain;
	private String imageHead;
	private String imageFull;
			
	Avatar() { }
	
	Avatar(String id, String name, boolean isVillain) {
		this.id = id;
		this.name = name;
		this.isVillain = isVillain;
		this.imageHead = id + IMAGE_HEAD_SUFFIX;
		this.imageFull = id + IMAGE_FULL_SUFFIX;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageHead() {
		return imageHead;
	}
	
	public String getImageFull() {
		return imageFull;
	}
	
	public boolean isVillain() {
		return isVillain;
	}
	
	public static Avatar getAvatar(String id) {
		return avatars.get(id);
	}
	
	public static Avatar getRandomAvatar() {
		return values()[new Random().nextInt(values().length)];
	}
	
	public static Avatar getOpponent(Avatar avatar) {
		Avatar opponent = getRandomAvatar();
		while(opponent.isVillain() == avatar.isVillain())
			opponent = getRandomAvatar();
		return opponent;
	}
}