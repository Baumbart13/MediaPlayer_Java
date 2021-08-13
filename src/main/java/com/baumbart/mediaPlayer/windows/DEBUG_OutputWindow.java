package com.baumbart.mediaPlayer.windows;

import com.baumbart.annotations.Author;
import com.baumbart.mediaPlayer.osDependent.OsDependencies;
import com.baumbart.mediaPlayer.osDependent.Windows;

import com.sun.jna.Platform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Author
public final class DEBUG_OutputWindow extends JFrame {

	private boolean isDebugModeActive = false;
	private javax.swing.JTextArea textArea;
	protected static String textContent = "";
	private final int width = 400;
	private final int height = 400;
	private File logFile;

	private OsDependencies OsDependent;

	public static String getLastText(){
		return textContent;
	}

	public boolean isDebugModeActive() {
		return isDebugModeActive;
	}

	public DEBUG_OutputWindow(DEBUG_OutputWindow window){
		this(window.isDebugModeActive(), window.textArea.getText());
	}

	public DEBUG_OutputWindow(boolean isDebugModeActive, String text){
		this(isDebugModeActive);
		textArea.setText(text);
	}

	public DEBUG_OutputWindow(boolean isDebugModeActive){
		this.isDebugModeActive = isDebugModeActive;
		if(!isDebugModeActive){
			return;
		}

		detectOs();
		createComponents();
		createMenu();
		addHotKeys();
		setBounds(0,0,width,height);

		setName("DEBUG OUTPUT");
		setTitle("DEBUG OUTPUT");

		registerWindowListener();

		logFile = new File(String.format("%s%slog.txt",
				OsDependent.getMusicPath().substring(0, OsDependent.getMusicPath().lastIndexOf(File.separatorChar)+1).concat("Documents"),
				File.separatorChar));
		log_initialLog();
	}

	private void detectOs(){

		if(Platform.isWindows()){
			OsDependent = new Windows();
		}else if(Platform.isLinux()){
			// TODO: implement OsDependencies for Linux
			//  OsDependent = new Linux();
		}else if(Platform.isMac()){
			// TODO: implement OsDependencies for Mac
			//  OsDependent = new Mac();
		}else{
			// TODO: implement OsDependencies for Linux as default-OS
			//  OsDependent = new Linux();
		}
	}

	public void minimize(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_ICONIFIED));
	}

	public void close(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public void maximize(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_DEICONIFIED));
	}

	public void print(String s){
		textArea.append(s);
	}

	public void print(String[] s){
		for(var t : s){
			print(t);

			if(!t.endsWith(System.lineSeparator())){
				log_logging(String.format("%s%s", t, System.lineSeparator()));
			}else{
				log_logging(t);
			}
		}
	}

	public void printf(String s, Object ... o){
		print(String.format(s, o));
	}

	public void println(String s){
		printf("%s%s", s, System.lineSeparator());
	}

	public void println(String[] s){
		for(var t : s){
			println(t);
		}
	}

	private void log_logging(String s){

		BufferedWriter writer = null;

		try{
			if(logFile.isDirectory()){
				logFile.delete();
			}
			if(!logFile.exists()){
				logFile.mkdirs();
				logFile.createNewFile();
			}
			logFile.setWritable(true);
			writer = new BufferedWriter(new FileWriter(logFile));

			writer.append(String.format("%s: %s",
					LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), s));
			println("Logfile: " + logFile);

			if(!s.endsWith(System.lineSeparator())){
				writer.newLine();
			}
		}catch(IOException e){
			System.err.println("An error occurred on the log file!");
			if(isDebugModeActive) {
				e.printStackTrace();
			}
		}finally{
			try {
				if(writer != null){
					writer.flush();
					writer.close();
				}
			}catch(IOException e){
				System.err.println("An error occurred on closing the BufferedWriter for the log file, while being in an Exception! WTF");
				if(isDebugModeActive) {
					e.printStackTrace();
				}
			}
		}

		//log_logging(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ": " + s + System.lineSeparator());
	}

	private void log_initialLog(){
		log_logging("Start of Program: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + System.lineSeparator() +
				"DebugMode-Active: " + this.isDebugModeActive + System.lineSeparator() +
				"Current OS: " + OsDependent.name());
	}

	private void log_initalLog(String[] s){
		log_initialLog();
		for(int i = 0; i < s.length; ++i){
			log_logging(s[i]);
		}
	}

	public void clear(){
		textArea.setText("");
	}

	public void createComponents(){
		setSize(width, height);
		setLayout(new BorderLayout());

		var panel = new JPanel(new BorderLayout());
		add(panel);
		panel.setVisible(true);

		textArea = new JTextArea();
		panel.add(textArea);
		textArea.setEditable(true);
		//textArea.setCaretPosition(textArea.getDocument().getLength());
		//textArea.setLayout(new GridLayout(1,1));
		//textArea.setSize((int)(this.getWidth()*0.5), (int)(getHeight()*0.5));
		var scrollPane = new JScrollPane(textArea);
		setVisible(true);
	}

	public void addHotKeys(){
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

				// Toggle DEBUG-mode with [Ctrl]+[Alt]+[Shift]+[D]
				if(e.isControlDown() && e.isShiftDown() && e.isAltDown() && e.getExtendedKeyCode() == KeyEvent.VK_D){
					toggleDebugMode();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {

			}
		});
	}

	public void toggleDebugMode(){
		isDebugModeActive = !isDebugModeActive;
		//setVisible(false);
	}

	public void createMenu(){
		var menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		var debugMenu = new JMenu("DEBUG");
		menuBar.add(debugMenu);

		var printSystemProps = new JMenuItem("Print System Props");
		debugMenu.add(printSystemProps);
		printSystemProps.addActionListener((e) -> {
			for(var s : OsDependent.getAllAvailableProperties()){
				for(var ss : s.split(";")){
					println(ss);
				}
			}
		});

		var printMusicPath = new JMenuItem("Print Music Path");
		debugMenu.add(printMusicPath);
		printMusicPath.addActionListener(e -> println(OsDependent.getMusicPath()));

		var printAllFolders = new JMenuItem("Print all folders (probably)");
		debugMenu.add(printAllFolders);
		printAllFolders.addActionListener(e -> println(OsDependent.getAllSpecialFoldersPlusId()));

		var countThreadCalled = new JMenuItem("How often was the Thread created to load special folders?");
		debugMenu.add(countThreadCalled);
		countThreadCalled.addActionListener(e -> {
			int timesCalled = OsDependencies.getTimesThreadWasCreated();
			if(timesCalled == 1){
				println(String.format("Thread was created %d time", timesCalled));
			}else{
				println(String.format("Thread was created %d times", timesCalled));
			}
		});
	}

	public void registerWindowListener(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				textContent = textArea.getText();
				super.windowClosing(e);
			}
		});
	}
}
