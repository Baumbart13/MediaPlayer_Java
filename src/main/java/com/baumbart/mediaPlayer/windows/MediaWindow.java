package com.baumbart.mediaPlayer.windows;

import com.baumbart.annotations.Author;
import com.baumbart.mediaPlayer.listeners.KeyListeners.KeyListener_Controls;
import com.baumbart.mediaPlayer.osDependent.OsDependencies;
import com.baumbart.mediaPlayer.osDependent.Windows;
import jdk.jshell.spi.ExecutionControl;
import uk.co.caprica.vlcj.filefilters.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.Meta;
import uk.co.caprica.vlcj.media.MetaData;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.temporal.ChronoUnit;

@Author
public final class MediaWindow extends JFrame {

	static final String DEBUG_DefaultPath = System.getProperty("user.dir");
	static final String filePath_short_WAV	= "sample\\PatlamayaDevam_short.wav";
	static final String filePath_short_MP3	= "sample\\PatlamayaDevam_short.mp3";
	static final String filePath_long_WAV	= "sample\\PatlamayaDevam_long.wav";
	static final String filePath_long_MP3	= "sample\\PatlamayaDevam_long.mp3";
	static final String filePath_short_MP4	= "sample\\OsuGameplayBaumbart13.mp4";

	MediaList playList;
	AudioPlayerComponent audioPlayerComponent;
	OsDependencies OsDependent;
	MetaData currMetaData;
	Media currMedia;

	int xPos = 200;
	int yPos = 200;
	int xWidth = 800;
	int yHeight = 600;

	//private final MediaWindow self;
	private boolean isPlaying = false;

	private DEBUG_OutputWindow debugWindow;

	public DEBUG_OutputWindow getDebugWindow(){
		return debugWindow;
	}

	public MediaWindow(boolean isDebugModeActive) {
		debugWindow = new DEBUG_OutputWindow(isDebugModeActive);
		setName("Baumbart Media Player");
		setTitle("Baumbart Media Player");
		detectOs();
		//self = this;

		// Hotkeys
		addHotkeys();

		// Menubar
		createMenu();

		// Playlist, control-buttons, MediaProgressBar, ...
		prepareLayout();

		// Prepare for video
		readyMedia();

		// set size of window
		setBounds(new Rectangle(xPos, yPos, xWidth, yHeight));

		registerWindowListener();

		setVisible(true);
		if (isDebugModeActive){
			debugWindow.setVisible(isDebugModeActive);
		}
		//DEBUG
		if(isDebugModeActive) {
			playMedia(filePath_short_MP4);
		}
	}

	public void detectOs(){
		var osName = System.getProperty("os.name").toLowerCase();

		if(osName.contains("windows")){
			OsDependent = new Windows();
		}
	}

	public boolean isPlaying(){
		return this.isPlaying;
	}

	public void openLink(){
		if(debugWindow.isDebugModeActive()){
			debugWindow.println("Load File");
		}

		// testing link
		String url = "https://www.youtube.com/watch?v=CSk9dUBH4K0";

		prepareUrl(url);

		playMedia(url);
	}

	public void openFile() {
		// Maybe cut this feature of pausing the media while selecting mediafiles
		//  pause media
		// Turns out, this feature was crap.. glad to have this cut
		if(debugWindow.isDebugModeActive()) {
			debugWindow.println("Load File");
		}
		//System.out.println("Load File");
		//pauseMedia();

		// OS-Dependent code
		String defaultMusicPath = OsDependent.getMusicPath();

		FileChooser fc = new FileChooser(defaultMusicPath);

		fc.setAcceptAllFileFilterUsed(true);
		fc.addChoosableFileFilter(SwingFileFilterFactory.newAudioFileFilter());
		fc.addChoosableFileFilter(SwingFileFilterFactory.newMediaFileFilter());
		fc.addChoosableFileFilter(SwingFileFilterFactory.newVideoFileFilter());
		fc.addChoosableFileFilter(SwingFileFilterFactory.newPlayListFileFilter());
		// TODO: Implement playlist and activate multiSelection
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(this);

		// resume afterwards
		if(returnVal == JFileChooser.APPROVE_OPTION){
			playMedia(fc.getSelectedFile().getAbsolutePath());
			if(debugWindow.isDebugModeActive()) {
				debugWindow.println("File loaded");
			}
			currMetaData = audioPlayerComponent.mediaPlayer().media().meta().asMetaData();
			//System.out.println("File loaded.");
		}else if(returnVal == JFileChooser.CANCEL_OPTION){
			// This feature was crap... glad to have this cut
			// resume media on cancellation
			//unpauseMedia();
			if(debugWindow.isDebugModeActive()){
				debugWindow.println("File not loaded. Cancelled");
			}
		}
	}

	public void nextMedia(){
		// TODO: implement playlist
		System.err.println(this.getClass() + ".nextMedia() is not implemented yet");

		if(debugWindow.isDebugModeActive()){
			debugWindow.println("Next Media");
		}
	}

	public void prevMedia(){
		// TODO: implement playlist
		System.err.println(this.getClass() + ".prevMedia() is not implemented yet");

		if(debugWindow.isDebugModeActive()){
			debugWindow.println("Previous Media");
		}
	}

	public void stopMedia(){
		isPlaying = false;
		audioPlayerComponent.mediaPlayer().controls().stop();

		if(debugWindow.isDebugModeActive()){
			debugWindow.println("Stop Media");
		}
	}

	public void pauseMedia(){
		isPlaying = false;
		audioPlayerComponent.mediaPlayer().controls().pause();

		if(debugWindow.isDebugModeActive()){
			debugWindow.println("Pause Media");
		}
	}

	public void resumeMedia(){
		isPlaying = true;
		audioPlayerComponent.mediaPlayer().controls().play();

		if(debugWindow.isActive()){
			debugWindow.println("Resume Media \"" + audioPlayerComponent.mediaPlayer().media().info().audioTracks().get(0));
		}
	}

	public void togglePlay(){
		if(isPlaying()){
			pauseMedia();
		}else{
			resumeMedia();
		}
	}

	public void playMedia(String filePath){
		isPlaying = true;
		boolean loadSuccessfully = audioPlayerComponent.mediaPlayer().media().start(filePath);

		if(debugWindow.isDebugModeActive()){

			debugWindow.println("File loaded: " + loadSuccessfully);
		}
	}

	public void prepareUrl(String url){
		audioPlayerComponent.mediaPlayer().media().prepare(url);
	}

	public void readyMedia(){
		setContentPane(new JRootPane());
		audioPlayerComponent = new AudioPlayerComponent();
	}

	public void jumpTo(ChronoUnit unit,  long amount){
		try{
			throw new ExecutionControl.NotImplementedException("Cannot jump on media yet");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void addHotkeys(){
		addKeyListener(new KeyListener_Controls());

		/*addKeyListener(new Baumbart_KeyListener_File(this));
		addKeyListener(new Baumbart_KeyListener_MediaControls(this));*/

	}

	public void createMenu(){
		var menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		var file = new JMenu("File");
		menuBar.add(file);
		addFileMenuItems(file);

		var player = new JMenu("Player");
		menuBar.add(player);
		addPlayerMenuItems(player);

		if(debugWindow.isDebugModeActive()){
			JMenu debugMenu = new JMenu("DEBUG");
			menuBar.add(debugMenu);
			{
				var openDebugWindow = new JMenuItem("Open Debug Window. Not working yet");
				debugMenu.add(openDebugWindow);
				openDebugWindow.addActionListener((e) -> {
					debugWindow = new com.baumbart.mediaPlayer.windows.DEBUG_OutputWindow(debugWindow);
					debugWindow.setVisible(true);
				});

				var PatlamayaDevam_short_WAV = new JMenuItem("Patlamaya Devam_short.wav");
				debugMenu.add(PatlamayaDevam_short_WAV);
				PatlamayaDevam_short_WAV.addActionListener((e) -> {
					playMedia("sample\\PatlamayaDevam_short.wav");
					debugWindow.printf("Playing \"%s\"", currMetaData.get(Meta.TITLE));
				});

				var PatlamayaDevam_short_MP3 = new JMenuItem("Patlamaya Devam_short.mp3");
				debugMenu.add(PatlamayaDevam_short_MP3);
				PatlamayaDevam_short_MP3.addActionListener((e) -> {
					playMedia("sample\\PatlamayaDevam_short.mp3");
					debugWindow.printf("Playing \"%s\"", currMetaData.get(Meta.TITLE));
				});

				var PatlamayaDevam_long_WAV = new JMenuItem("Patlamaya Devam_long.wav");
				debugMenu.add(PatlamayaDevam_long_WAV);
				PatlamayaDevam_long_WAV.addActionListener((e) -> {
					playMedia("sample\\PatlamayaDevam_long.wav");
					debugWindow.printf("Playing \"%s\"", currMetaData.get(Meta.TITLE));
				});

				var PatlamayaDevam_long_MP3 = new JMenuItem("Patlamaya Devam_long.mp3");
				debugMenu.add(PatlamayaDevam_long_MP3);
				PatlamayaDevam_long_MP3.addActionListener((e) -> {
					playMedia("sample\\PatlamayaDevam_long.mp3");
					debugWindow.printf("Playing \"%s\"", currMetaData.get(Meta.TITLE));
				});

				var OsuGameplayBaumbart13_MP4 = new JMenuItem("Osu-Gameplay Baumbart13.mp4");
				debugMenu.add(OsuGameplayBaumbart13_MP4);
				OsuGameplayBaumbart13_MP4.addActionListener((e) -> {
					playMedia("sample\\OsuGameplayBaumbart13.mp4");
					debugWindow.printf("Playing \"%s\"", currMetaData.get(Meta.TITLE));
				});
			}

			var printTestMetaData = new JMenuItem("Print Meta Data");
			debugMenu.add(printTestMetaData);
			printTestMetaData.addActionListener((e) ->{
				for(var k : currMetaData.values().keySet()){
					var v = currMetaData.values().get(k);

					debugWindow.println(String.format("\"%s\":\"%s\"", k, v));
				}
			});
		}
	}

	public void addPlayerMenuItems(JMenu menu){
		var pause = new JMenuItem("Pause");
		var play = new JMenuItem("Play");
		var stop = new JMenuItem("Stop");

		menu.add(play);
		menu.add(pause);
		menu.add(stop);

		play.addActionListener(e -> resumeMedia());
		pause.addActionListener(e -> pauseMedia());
		stop.addActionListener(e -> stopMedia());
	}

	public void addFileMenuItems(JMenu menu){
		JMenuItem menuItem = new JMenuItem("Select file");

		menu.add(menuItem);
		menuItem.addActionListener(e -> {
			openFile();
			//Desktop.getDesktop().browseFileDirectory(new File(filePath_short_MP4));
		});
	}

	public void registerWindowListener(){
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e){
				System.out.println("Closing Application");
				releaseEverything();
				System.exit(0);
			}
		});

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				debugWindow.setAlwaysOnTop(true);
				//debugWindow.maximize();
			}

			@Override
			public void windowClosing(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {
				//debugWindow.setAlwaysOnTop(true);
				//debugWindow.maximize();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				debugWindow.setAlwaysOnTop(false);
				debugWindow.minimize();
			}
		});


	}

	public void prepareLayout(){
		JPanel mainPanel = new JPanel(new GridLayout(3, 1));

		JRootPane mainPane = new JRootPane();

		// TODO: implement Icons
		JButton bttnPause = new JButton("Pause");
		JButton bttnPlay = new JButton("Play");
		JButton bttnStop = new JButton("Stop");

		bttnPause.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pauseMedia();
			}
		});
		bttnPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				resumeMedia();
			}
		});
		bttnStop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stopMedia();
			}
		});

		mainPanel.add(bttnPause);
		mainPanel.add(bttnPlay);
		mainPanel.add(bttnStop);

		this.add(mainPanel);
	}

	public void releaseEverything(){
		if(audioPlayerComponent != null){
			audioPlayerComponent.release();
		}
		if(debugWindow != null){
			debugWindow.removeAll();
			debugWindow.setVisible(false);
			debugWindow = null;
		}
	}
}
