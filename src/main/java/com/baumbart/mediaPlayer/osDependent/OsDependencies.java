package com.baumbart.mediaPlayer.osDependent;

import com.baumbart.annotations.Author;

import java.util.LinkedList;
import java.util.Map;

@Author
public abstract class OsDependencies {
	private static int timesThreadWasCreated = 0;
	public static int getTimesThreadWasCreated(){
		return timesThreadWasCreated;
	}

	public abstract String name();

	protected Thread loadFolders = new Thread(() ->{
		++timesThreadWasCreated;
		folders.lock();
		loadSpecialFolders();
		folders.unlock();
		System.out.println("Special folders loaded");
	});

	protected static SpecialFolders folders = new SpecialFolders();

	@Author(name="Baumbart13")
	protected static class SpecialFolders {
		private boolean isLocked = false;
		protected LinkedList<String> entries = new LinkedList<String>();

		public void lock(){
			isLocked = true;
		}
		public void unlock(){
			isLocked = false;
		}

		public boolean contains(Object o){
			return entries.contains(o);
		}

		public boolean add(String e){
			return entries.add(e);
		}

		public LinkedList<String> getEntries(){
			return entries;
		}
		public boolean isLocked(){
			return isLocked;
		}
	}

	public String[] getAllAvailableProperties(){
		var out = new String[System.getProperties().entrySet().size()];

		var i = 0;
		for(Map.Entry<?,?> e : System.getProperties().entrySet()){
			out[i++] = String.format("%s - \"%s\"", e.getKey(), e.getValue());
		}

		return out;
	}

	public OsDependencies(){
		loadFolders.start();
	}

	public abstract String[] getAllSpecialFoldersPlusId();
	protected abstract void loadSpecialFolders();
	public abstract String getMusicPath();
}
