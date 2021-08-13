package com.baumbart.mediaPlayer.listeners.KeyListeners;


import com.baumbart.annotations.Author;
import com.baumbart.mediaPlayer.MediaPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Author
public class KeyListener_Controls implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		/////////////////////////
		//                     //
		//        [CTRL]       //
		//                     //
		/////////////////////////
		if(e.isControlDown()){
			switch (e.getExtendedKeyCode()){
				// ... + [O] -> OpenFile
				case KeyEvent.VK_O:
					if(MediaPlayer.isDebugMode()){
						MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [OpenFile] pressed");
					}
					MediaPlayer.getPlayerWindow().openFile();
					break;
				// ... + [N] -> OpenLink
				case KeyEvent.VK_N:
					if(MediaPlayer.isDebugMode()){
						MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [OpenLink] pressed");
					}
					MediaPlayer.getPlayerWindow().openLink();
					break;
			}
		}

		/////////////////////////
		//                     //
		//       [SHIFT]       //
		//                     //
		/////////////////////////
		if(e.isShiftDown()){
			switch (e.getExtendedKeyCode()){
				// ... + [N] -> NextMedia
				case KeyEvent.VK_N:
					if(MediaPlayer.isDebugMode()){
						MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [NextMedia] pressed");
					}
					MediaPlayer.getPlayerWindow().nextMedia();
					break;
				// ... + [P] -> PrevMedia
				case KeyEvent.VK_P:
					if(MediaPlayer.isDebugMode()){
						MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [PreviousMedia] pressed");
					}
					MediaPlayer.getPlayerWindow().prevMedia();
					break;
			}
		}

		/////////////////////////
		//                     //
		// mediaplayer control //
		//                     //
		/////////////////////////
		switch(e.getExtendedKeyCode()){
			// [space] -> pause/unpause
			case KeyEvent.VK_SPACE:
				if(MediaPlayer.isDebugMode()){
					MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [TogglePlay] pressed");
				}
				MediaPlayer.getPlayerWindow().togglePlay();
				break;
			// [LArrow] -> 10sec back
			case KeyEvent.VK_LEFT:
				// TODO: implement ability to jump through media and go 10 seconds back
				if(MediaPlayer.isDebugMode()){
					MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [10SecBack] pressed");
				}
				//MediaPlayer.getPlayerWindow().jumpTo(MediaPlayer.getPlayerWindow().currPos() - Duration.ofSeconds(10));
				break;
			// [RArrow] -> 10sec fwd
			case KeyEvent.VK_RIGHT:
				// TODO: implement ability to jump through media and go 10 seconds forward
				if(MediaPlayer.isDebugMode()){
					MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [10SecForward] pressed");
				}
				//MediaPlayer.getPlayerWindow().jumpTo(MediaPlayer.getPlayerWindow().currPos() + Duration.ofSeconds(10));
				break;
			// [DArrow] -> 5% more silent
			case KeyEvent.VK_DOWN:
				// TODO: implement ability to adjust the volume and set it 5% (measured by the absolute maximum) lower
				if(MediaPlayer.isDebugMode()){
					MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [5%Silent] pressed");
				}
				//MediaPlayer.getPlayerWindow().setVolume(MediaPlayer.getPlayerWindow().getVolume() - 5%);
				break;
			// [UArrow] -> 5% louder
			case KeyEvent.VK_UP:
				// TODO: implement ability to adjust the volume and set it 5% (measured by the absolute maximum) higher
				if(MediaPlayer.isDebugMode()){
					MediaPlayer.getPlayerWindow().getDebugWindow().println("Hotkey [5%Louder] pressed");
				}
				//MediaPlayer.getPlayerWindow().setVolume(MediaPlayer.getPlayerWindow().getVolume() + 5%)
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
