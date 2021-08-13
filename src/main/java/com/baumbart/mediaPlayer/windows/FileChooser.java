package com.baumbart.mediaPlayer.windows;

import com.baumbart.annotations.Author;
import com.baumbart.mediaPlayer.MediaPlayer;
import com.sun.jna.Platform;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

@Author
public class FileChooser extends JFileChooser {

	JFileChooser fc = new JFileChooser();

	/**
	 * Constructs a JFileChooser pointing to the user's default directory. This default depends on the operating system.
	 * It is typically the "My Documents" folder on Windows, and the user's home directory on Unix.
	 */
	public FileChooser() {
		super();
		setLookAndFeel();
	}

	/**
	 * Constructs a JFileChooser using the given path. Passing in a null string causes the file chooser to point to the
	 * user's default directory. This default depends on the operating system. It is typically the "My Documents" folder
	 * on Windows, and the user's home directory on Unix.
	 * @param currentDirectoryPath a String giving the path to a file or directory
	 */
	public FileChooser(String currentDirectoryPath) {
		this(new File(currentDirectoryPath));
	}

	/**
	 * Constructs a JFileChooser using the given File as the path. Passing in a null file causes the file chooser to
	 * point to the user's default directory. This default depends on the operating system. It is typically the
	 * "My Documents" folder on Windows, and the user's home directory on Unix.
	 * @param currentDirectory a File object specifying the path to a file or directory
	 */
	public FileChooser(File currentDirectory) {
		super(currentDirectory);
		setLookAndFeel();
	}

	/**
	 * Constructs a JFileChooser using the given FileSystemView.
	 * @param fsv a FileSystemView
	 */
	public FileChooser(FileSystemView fsv) {
		super(fsv);
		setLookAndFeel();
	}

	/**
	 * Constructs a JFileChooser using the given current directory and FileSystemView.
	 * @param currentDirectory a File object specifying the path to a file or directory
	 * @param fsv a FileSystemView
	 */
	public FileChooser(File currentDirectory, FileSystemView fsv) {
		super(currentDirectory, fsv);
		setLookAndFeel();
	}

	/**
	 * Constructs a JFileChooser using the given current directory path and FileSystemView.
	 * @param currentDirectoryPath a String specifying the path to a file or directory
	 * @param fsv a FileSystemView
	 */
	public FileChooser(String currentDirectoryPath, FileSystemView fsv) {
		this(new File(currentDirectoryPath), fsv);
	}

	private void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			/*if(Platform.isWindows()) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}else if(Platform.isLinux()){
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.linux.LinuxLookAndFeel");
			}*/
			SwingUtilities.updateComponentTreeUI(this);

		}catch(ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}catch(IllegalAccessException e){
			MediaPlayer.getPlayerWindow().getDebugWindow().println("Access denied");
		} catch (UnsupportedLookAndFeelException e) {
			if(MediaPlayer.isDebugMode()) {
				e.printStackTrace();
			}
		}
	}
}
