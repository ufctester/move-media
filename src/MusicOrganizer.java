import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * 
 */

/**
 * @author apps
 *
 */
public class MusicOrganizer {
	
	private String musicDirString = "X:\\Music";
	private String targetMusicDirString = "X:\\Music-new";
	//private String musicDirString="C:\\gitbash\\opt\\music-beets\\music";
	//private String targetMusicDirString="C:\\gitbash\\opt\\music-beets\\music-clean";
	
	private Map<String,Artist> artistToObjectMap = new LinkedHashMap<String,Artist>();
	
	public class Artist
	{
		private String artist=null;
		private File artistFolder=null;
		private Map<File,File> originalAlbumToNewAlbumMap = new LinkedHashMap<File,File>();
		
		public Artist(String artist)
		{
			super();
			this.artist=artist;
			artistFolder=new File(targetMusicDirString + File.separator + artist);
			
			System.out.println(artist);
		}
		
		public String getArtist() {
			return artist;
		}
		
		public File getArtistFolder() {
			return artistFolder;
		}
		
		public void setArtistFolder(File artistFolder) {
			this.artistFolder = artistFolder;
		}
		
		public Map<File, File> getOriginalAlbumToNewAlbumMap() {
			return originalAlbumToNewAlbumMap;
		}
		
		public void addAlbum(File originalArtistAlbumFolder,String albumName)
		{
			File newAlbumFolder=new File(artistFolder.toString() + File.separator + albumName);
			originalAlbumToNewAlbumMap.put(originalArtistAlbumFolder, newAlbumFolder);
		}
		
		public String createCommand()
		{
			StringBuffer sBuffer = new StringBuffer();
			
			sBuffer.append("echo ArtistFolder: " + getArtistFolder() + "\n");
			
			for (Iterator iterator = originalAlbumToNewAlbumMap.keySet().iterator(); iterator.hasNext();) {
				File originalDirectory = (File) iterator.next();
				File targetDirectory = originalAlbumToNewAlbumMap.get(originalDirectory);
				sBuffer.append("echo \"" + originalDirectory + "\" \"" + targetDirectory + "\"\n" );	
			}
			
			
			sBuffer.append("echo.\n");
			sBuffer.append("echo Would you like to continue?\n");
			sBuffer.append("pause \n\n");
			
			sBuffer.append("mkdir \"" + getArtistFolder() + "\"\n");
			
			for (Iterator iterator = originalAlbumToNewAlbumMap.keySet().iterator(); iterator.hasNext();) {
				File originalDirectory = (File) iterator.next();
				File targetDirectory = originalAlbumToNewAlbumMap.get(originalDirectory);
				sBuffer.append("move \"" + originalDirectory + "\" \"" + targetDirectory + "\"\n" );	
			}			
			//sBuffer.append("pause \n");
			sBuffer.append("\n");
			sBuffer.append("echo.\n");
			sBuffer.append("echo ############################################################### \n");
			
			return sBuffer.toString();
						
		}
		
		@Override
		public String toString() {
			StringBuffer sBuffer = new StringBuffer();
			sBuffer.append("artist: " + artist +" \n");
			for (Iterator iterator = originalAlbumToNewAlbumMap.keySet().iterator(); iterator.hasNext();) {
				File source = (File) iterator.next();
				File target = originalAlbumToNewAlbumMap.get(source);
				sBuffer.append("   source: " + source + "\n   target: " + target + "\n");
				
			}
			return sBuffer.toString();
		}
		
	}
	
	public MusicOrganizer()
	{
		super();
	}
	
	public void execute()
	{
		File musicDir = new File(musicDirString);
		//File musicDir = new File("Y:\\Entertainment\\Music");
		
//		Map<File,File> originalAlbumToNewAlbumMap = new LinkedHashMap<File,File>();
//		Map<String,File> artistsToArtistFileMap = new LinkedHashMap<String,File>();

		//Collect the root directories 
		//2 Chainz - Pretty Girls Like Trap Music - [2017]
		final File[] rootDirectories = musicDir.listFiles();
		
		if (rootDirectories != null) {
			for (final File file : rootDirectories) {
				if (file.isDirectory()) {
					//artistsAlbumDirectories.add(file);
					
					String artistName = getArtistNameFromOriginalFolder(file);
					String albumName = getAlbumNameFromOriginalFolder(file);
					//System.out.println("artistName: " + artistName + " albumName: " + albumName);
					
					//New Artist Folder <artist>/<album> - [year]
					Artist artist=artistToObjectMap.get(artistName);
					if(artist == null)
					{
						artist = new Artist(artistName);
						artistToObjectMap.put(artistName, artist);
					}

					//Check if the old artist album file is in the map
					if(!artist.getOriginalAlbumToNewAlbumMap().containsKey(file))
					{
						artist.addAlbum(file,albumName);
					}
				}
			}
		}
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("echo OFF \n\n");
		for (Iterator iterator = artistToObjectMap.keySet().iterator(); iterator.hasNext();) {
			String artistName = (String) iterator.next();
			Artist artist = artistToObjectMap.get(artistName);
			sBuffer.append(artist.createCommand());
		}
		
		try {
			FileUtils.writeStringToFile(new File("C:\\\\tmp\\\\convert.bat"), sBuffer.toString(), Charset.defaultCharset());
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MusicOrganizer organizer = new MusicOrganizer();
		organizer.execute();
	}

	
	//2 Chainz - Pretty Girls Like Trap Music - [2017]
	private static String getArtistNameFromOriginalFolder(File artistAlbumDirectory)
	{	
		//2 Chainz - Pretty Girls Like Trap Music - [2017]
		String artistAlbumDirectoryName = artistAlbumDirectory.getName();
		
		//System.out.println("artistName: " + artistAlbumDirectoryName);
		int index = artistAlbumDirectoryName.indexOf(" - ");
		
		if (index > -1) {
			String artistName = artistAlbumDirectoryName.substring(0, index);
			// Remove leading and trailing spaces
			artistName = artistName.trim();
			return artistName;
		}	
		
		return null;
	}
	
	//2 Chainz - Pretty Girls Like Trap Music - [2017]
	private static String getAlbumNameFromOriginalFolder(File artistAlbumDirectory)
	{	
		//2 Chainz - Pretty Girls Like Trap Music - [2017]
		String artistAlbumDirectoryName = artistAlbumDirectory.getName();
		
		//System.out.println("artistName: " + artistAlbumDirectoryName);
		int index = artistAlbumDirectoryName.indexOf(" - ");
		
		if (index > -1) {
			String artistName = artistAlbumDirectoryName.substring(0, index);
			String albumName = artistAlbumDirectoryName.substring(index + " - ".length());
			// Remove leading and trailing spaces
			albumName = albumName.trim();
			return albumName;
		}	
		
		return null;
	}	

}
