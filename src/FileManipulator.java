import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class contains methods to read and write text and serializable objects to files
 * 
 * @author Tim Frew
 *
 */
public class FileManipulator {
	/**
	 * This method writes text to a file
	 * @param text : the text to be written
	 * @param file : the file to write to
	 */
	public static void writeToFile(String text, String file){
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write("");
			bw.write(text);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method returns text that is read from a file
	 * @param file : the file to read from
	 */
	public static String readFromFile(String file){
		String toReturn = "";
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line; 
		try {
			while ((line = in.readLine()) != null) {
				toReturn = line;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return toReturn;
	}

	/**
	 * This method writes a GameHistory object to a file
	 * @param user : the GameHistory object to be written to a file
	 * @param file : the file to write to
	 */
	public static void writeHistoryTofile(GameHistory user, String file){
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(fout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos.writeObject(user);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	/**
	 * This method reads a GameHistory object from a specified file
	 * @param file : The specified file
	 * @return : The GameHistory object read from the file
	 */
	public static GameHistory readHistoryFromFile(String file){
		GameHistory history = null;
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream(file);
			objectinputstream = new ObjectInputStream(streamIn);
			history = (GameHistory) objectinputstream.readObject();
			objectinputstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return history;
	}

	/**
	 * This method appends text to a file
	 * @param text : the text to be written
	 * @param file : the file to append to
	 */
	public static void appendToFile(String text, String file) {
		BufferedReader in = null;
		ArrayList<String> data = new ArrayList<>();
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line; 
		try {
			while ((line = in.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.add(text);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			for(String wordList : data){
				bw.append(wordList + "\n");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
