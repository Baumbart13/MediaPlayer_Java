package com.baumbart.mediaPlayer.osDependent;

import com.baumbart.annotations.Author;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeMapped;
import com.sun.jna.PointerType;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.sun.jna.win32.W32APITypeMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.baumbart.mediaPlayer.MediaPlayer.isDebugMode;

@Author
public class Windows extends OsDependencies{
	public Windows() {
		super();
	}

	@Override
	public String[] getAllSpecialFoldersPlusId() {
		if(folders.isLocked() || loadFolders.isAlive()){
			return new String[]{"Special folders are not ready yet"};
		}
		if(folders.getEntries().size() < 1){
			return new String[]{"No special folders are loaded"};
		}
		return folders.getEntries().toArray(new String[0]);
	}

	private String getSpecialFolderPath_Windows(int folderId){
		var path = "";

		//HWND hwndOwner = null;
		//var nFolder = folderId;
		//HANDLE hToken = null;
		var dwFlags = Shell32.SHGFP_TYPE_CURRENT;
		var pszPath = new char[Shell32.MAX_PATH];
		var  hResult = Shell32.INSTANCE.SHGetFolderPath(null, folderId,
				null, dwFlags, pszPath);
		if (Shell32.S_OK == hResult) {
			path = new String(pszPath);
			var len = path.indexOf('\0');
			path = path.substring(0, len);

			path = String.format("%d - \"%s\"", folderId, path);
			//System.out.println(folderId + " - \"" + path + "\"");
		} else {
			path = String.format("%d - \"%s\"", folderId, "Error");
			//System.err.println("Error: " + hResult);
		}

		return path;
	}

	@Override
	public String name() {
		return "Windows";
	}

	@Override
	protected void loadSpecialFolders() {
		var file = new File(String.format("resources%sspecialDirsInWindows.txt",
				File.separatorChar));
		//System.out.println(String.format("Absolute Path: \"%s\"", file.getAbsolutePath()));

		if(!file.exists()){

			BufferedWriter writer = null;
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				writer = new BufferedWriter(new FileWriter(file));
				for (var i = 1; i < 0xffff; ++i) {
					var temp = getSpecialFolderPath_Windows(i);

					if (temp.contains("Error")) continue;

					if(folders.contains(temp)) continue;

					writer.write(temp);
					writer.newLine();
					folders.add(temp);
				}
			}catch (IOException e){
				if(isDebugMode()) {
					e.printStackTrace();
				}
			}finally {
				try{
					writer.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}

		}else {

			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));

				var line = reader.readLine();
				while (line != null) {
					if (folders.contains(line)) {
						folders.add(line);
					}
					line = reader.readLine();
				}
			} catch (IOException e) {
				if(isDebugMode()) {
					e.printStackTrace();
				}
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					if(isDebugMode()) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public String getMusicPath() {
		var path = "";

		HWND hwndOwner = null;
		var nFolder = Shell32.FOLDERID_Music;
		HANDLE hToken = null;
		var dwFlags = Shell32.SHGFP_TYPE_CURRENT;
		var pszPath = new char[Shell32.MAX_PATH];
		var hResult = Shell32.INSTANCE.SHGetFolderPath(hwndOwner, nFolder,
				hToken, dwFlags, pszPath);

		if(hResult == Shell32.S_OK){
			path = new String(pszPath);
			var len = path.indexOf('\0');
			path = path.substring(0, len);
		}else{
			path = String.format("Error: %s", hResult);
		}

		return path;
	}



	private static Map<String, Object> OPTIONS = new HashMap<String, Object>();
	static {
		OPTIONS.put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.UNICODE);
		OPTIONS.put(Library.OPTION_FUNCTION_MAPPER, W32APIFunctionMapper.UNICODE);
	}

	static class HANDLE extends PointerType implements NativeMapped{}
	static class HWND extends HANDLE{}
	static interface Shell32 extends Library{
		public static final int MAX_PATH = 260;
		public static final int FOLDERID_LocalAppData = 0x001c;
		public static final int FOLDERID_Desktop = 0x0000;
		public static final int FOLDERID_Documents = 0x0005;
		public static final int FOLDERID_Favorites = 0x0006;
		public static final int FOLDERID_Startup = 0x0007;
		public static final int FOLDERID_Music = 0x000d;
		public static final int FOLDERID_Videos = 0x000e;
		public static final int SHGFP_TYPE_CURRENT = 0;
		public static final int SHGFP_TYPE_DEFAULT = 1;
		public static final int S_OK = 0;

		static Shell32 INSTANCE = (Shell32) Native.loadLibrary("shell32",
				Shell32.class, OPTIONS);

		/**
		 * see http://msdn.microsoft.com/en-us/library/bb762181(VS.85).aspx
		 *
		 * HRESULT SHGetFolderPath( HWND hwndOwner, int nFolder, HANDLE hToken,
		 * DWORD dwFlags, LPTSTR pszPath);
		 */
		public int SHGetFolderPath(HWND hwndOwner, int nFolder, HANDLE hToken,
								   int dwFlags, char[] pszPath);

	}
}
