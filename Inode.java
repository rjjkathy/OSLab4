import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Inode class
 * @author ningxie
 *
 */
public class Inode {
	// file name of inode
	private String fileName;
	private ArrayList<DirectBlock> level0Block = new ArrayList<DirectBlock>();
	private int maxSize = 100;
	private SingleIndirectBlock level1Block = new SingleIndirectBlock();
	private DoubleIndirectBlock level2Block = new DoubleIndirectBlock();
	private HashMap<Integer, File> addressToDataMap = new HashMap<Integer, File>();
	
	public Inode(File inputFile){
		init();
		readFromFile(inputFile);
	}
	
	private void init(){
		//level0Block.set(12, level1Block);
		
	}
	
	public void read(int blockNum){
		
	}
	
	public void write(int blockNum, String content){
		
	}
	
	private void readFromFile(File inputFile){
		try {
	        Scanner sc = new Scanner(inputFile);
	        int blockNum = 0;
	        String data = "";
	        while(sc.hasNextLine()) {
	            String str = sc.nextLine();
	            String[] stringArray = str.split(",");
	            blockNum = Integer.parseInt(stringArray[0]);
	            data = stringArray[1];
	            if(!level1Block.isFull()){
	            	
	            }
	        }
	    }
	    catch(FileNotFoundException e) {
	        System.err.println("File not found. Please scan in new file.");
	    }
	}
	
	private void writeToFile(String fileName, String content){
		try {
	        File output = new File(fileName);
	        PrintWriter printer = new PrintWriter(output);
	        printer.write(content);
	    }
	    catch(FileNotFoundException e) {
	        System.err.println("File not found. Please scan in new file.");
	    }
		
	}
	
	public static void main(String[] args){
		String fileName = "inputfile_1.txt";
		File testFile = new File(fileName);
		Inode inode = new Inode(testFile);
	}
	
}
