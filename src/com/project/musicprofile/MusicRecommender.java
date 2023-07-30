package com.project.musicprofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive menu where the user can use the functionalities of the
 * MusicRecommender
 *
 * @version 2
 * @author
 */

public class MusicRecommender {
    String fileName;
    List<String> songs = new ArrayList<>();

    public MusicRecommender(String fileName) {
	this.fileName = fileName;
	addSongs();
    }

    public void addSongs() {

	BufferedReader reader = null;
	try {
	    reader = new BufferedReader(
		    new FileReader("C:\\Users\\shrik\\Downloads\\Music Profile\\Music Profile\\Music_list.txt"));
	    String line;
	    while ((line = reader.readLine()) != null) {
		songs.add(line);
	    }
	} catch (IOException e) {
	    System.out.println("Error: no songs found!");
	}

	finally {
	    try {
		reader.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    void printList() {
	for (String s : songs) {
	    System.out.println(s);
	}
    }

    private ArrayList<Music> searchArtists(MusicProfile musicProfile) throws NoRecommendationException {
	// TODO Auto-generated method stub
	ArrayList<Music> searchResult = new ArrayList<>();
	String prefferedArtist = musicProfile.getPreferredArtist().toLowerCase();

	for (int i = 0; i < songs.size(); i++) {
	    String[] arr = songs.get(i).split(" ");
	    String track = arr[0].toLowerCase();
	    String artist = arr[1].toLowerCase();
	    String genre = arr[2].toLowerCase();
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (artist.indexOf(prefferedArtist) != -1) {
		Music m1 = new Music(track, artist, genre, BPM, popularity + 1);
		searchResult.add(m1);
		songs.set(i, track + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }

	}
	if (searchResult.isEmpty()) {
	    throw new NoRecommendationException("No recommendation found with given artist!");
	}
	return searchResult;
    }

    private Music genreBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {
	// TODO Auto-generated method stub
	Music m1 = null;
	String prefferedGenre = musicProfile.getPreferredGenre();

	for (int i = 0; i < songs.size(); i++) {
	    String[] arr = songs.get(i).split(" ");
	    String track = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (prefferedGenre.equalsIgnoreCase(genre)) {
		m1 = new Music(track, artist, genre, BPM, popularity + 1);
		songs.set(i, track + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }

	}
	if (m1 == null) {
	    throw new NoRecommendationException("No recommendation found with given genre!");

	}
	return m1;
    }

    private Music BPMBasedRecommendation(MusicProfile musicProfile) throws NoRecommendationException {
	// TODO Auto-generated method stub
	Music m1 = null;
	int prefferedBPM = musicProfile.getPreferredBPM();

	for (int i = 0; i < songs.size(); i++) {
	    String[] arr = songs.get(i).split(" ");
	    String track = arr[0];
	    String artist = arr[1];
	    String genre = arr[2];
	    int BPM = Integer.parseInt(arr[3]);
	    int popularity = Integer.parseInt(arr[4]);

	    if (prefferedBPM == BPM) {
		m1 = new Music(track, artist, genre, BPM, popularity + 1);
		songs.set(i, track + " " + artist + " " + genre + " " + BPM + " " + (popularity + 1));
	    }

	}
	if (m1 == null) {
	    throw new NoRecommendationException("No recommendation found with given BPM!");
	}
	return m1;

    }

    private Music getMostPopularMusic() {
	// TODO Auto-generated method stub
	Music m1 = null;
	int max = 0;
	int index = 0;
	for (int i = 0; i < songs.size(); i++) {
	    String[] arr = songs.get(i).split(" ");
	    int popularity = Integer.parseInt(arr[4]);

	    if (popularity > max) {
		max = popularity;
		index = i;
	    }

	}
	String[] arr1 = songs.get(index).split(" ");
	String track1 = arr1[0];
	String artist1 = arr1[1];
	String genre1 = arr1[2];
	int BPM1 = Integer.parseInt(arr1[3]);
	int popularity1 = Integer.parseInt(arr1[4]);
	m1 = new Music(track1, artist1, genre1, BPM1, popularity1 + 1);
	songs.set(index, track1 + " " + artist1 + " " + genre1 + " " + BPM1 + " " + (popularity1 + 1));

	return m1;
    }

    private void saveMusicList() {
	// TODO Auto-generated method stub
	try (BufferedWriter writer = new BufferedWriter(new FileWriter(
		"C:\\\\Users\\\\shrik\\\\Downloads\\\\Music Profile\\\\Music Profile\\\\Music_list.txt"))) {
	    for (String line : songs) {
		writer.write(line);
		writer.newLine();
	    }
	    System.out.println("File written successfully!");
	} catch (IOException e) {
	    System.err.println("Error writing to file: " + e.getMessage());
	}
    }

    public class MusicFileFormatException extends Exception {

	/**
	 * Calls the constructor of the exception superclass with the message passed in
	 * as a parameter
	 * 
	 * @param message The message for the exception
	 */
	public MusicFileFormatException(String message) {
	    super(message);
	}
    }

    public static MusicRecommender createMusicRecommender(String musicListFileName)
	    throws FileNotFoundException, MusicFileFormatException {
	MusicRecommender musicRecommender = new MusicRecommender(musicListFileName);
	return musicRecommender;

    }

    /**
     * Interactive menu where the user can use the functionalities of the
     * MusicRecommender
     * 
     * @param args No argument is needed for this main method
     */

    public static void main(String[] args) {
	Scanner scan = new Scanner(System.in);
	MusicRecommender musicRecommender = null;
	while (true) {
	    try {
		System.out.println("Welcome! What's the name of the file containing the music list?");
		String musicListFileName = scan.nextLine();

		musicRecommender = MusicRecommender.createMusicRecommender(musicListFileName);
//		musicRecommender.printList();
		break;
	    } catch (FileNotFoundException e) {
		System.out.println("The file does not exist! Please enter a valid file name!");
	    } catch (MusicFileFormatException e) {
		System.out.println(e.getMessage());
		return;
	    }
	}
	System.out.println("What's the name of the user?");
	String name = scan.nextLine();
	System.out.println("Who's your favorite artist?");
	String artist = scan.nextLine();
	System.out.println("What's your favorite genre?");
	String genre = scan.nextLine();
	System.out.println("What's your preferred BPM?");
	int BPM = scan.nextInt();
	System.out.println("Do you like popular music? (Y/N)");
	scan.nextLine();
	boolean likePopular = (scan.nextLine().equalsIgnoreCase("Y"));
	MusicProfile musicProfile = new MusicProfile(name, artist, genre, BPM, likePopular);

	System.out.printf("Hi %s!\n", musicProfile.getName());
	int option = -1;
	while (option != 5) {
	    System.out.println(
		    "1. Find songs of my favorite artists\n" + "2. Get a recommendation based on my preferred genre\n"
			    + "3. Get a recommendation based on my preferred BPM\n"
			    + "4. Show me the most popular song\n" + "5. Exit");
	    boolean wrongCondition;
	    do {
		option = scan.nextInt();
		wrongCondition = ((option != 1) && (option != 2) && (option != 3) && (option != 4) && (option != 5));
		if (wrongCondition)
		    System.out.println("Please enter a valid option!");
	    } while (wrongCondition);
	    switch (option) {
	    case 1 -> {
		try {
		    ArrayList<Music> searchResult = musicRecommender.searchArtists(musicProfile);
		    System.out.printf("Listing songs by %s:\n", musicProfile.getPreferredArtist());
		    for (Music music : searchResult) {
			System.out.println(music.toString());
		    }
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 2 -> {
		try {
		    System.out.println(musicRecommender.genreBasedRecommendation(musicProfile).toString());
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 3 -> {
		try {
		    System.out.println(musicRecommender.BPMBasedRecommendation(musicProfile).toString());
		} catch (NoRecommendationException e) {
		    System.out.println(e.getMessage());
		}
	    }
	    case 4 -> System.out.println(musicRecommender.getMostPopularMusic().toString());
	    case 5 -> {
		break;
	    }
	    }
	}
	musicRecommender.saveMusicList();
	System.out.println("Thanks for using the music recommender!");
    }

}