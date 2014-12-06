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
	private int maxLevel0Size = 12;
	private int maxLevel1Size, maxLevel2Size = 100;
	// 0 ~ 11
	private ArrayList<DirectBlock> level0Block = new ArrayList<DirectBlock>();
	// 12 ~ 111
	private SingleIndirectBlock level1Block = new SingleIndirectBlock();
	// 112 ~ 10111
	private DoubleIndirectBlock level2Block = new DoubleIndirectBlock();
	// map to get the data file from a block number
	private HashMap<Integer, File> addressToDataMap = new HashMap<Integer, File>();
	
	public Inode(File inputFile){
		init();
		readFromFile(inputFile);
	}
	
	private void init(){
		
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
	            if(blockNum >=0 && blockNum < 12){
	            	level0Block.set(blockNum, new DirectBlock(blockNum));
	            	// add the file
	            	//addressToDataMap.put(blockNum, file);
	            }else if(blockNum >= 12 && blockNum < 112){
	            	int index = level1Block.getIndexOfDirectBlock(blockNum);
	            	level1Block.set(index, new DirectBlock(blockNum));
	            }else if(blockNum >= 112 && blockNum < 10112){
	            	int index = level2Block.getIndexOfSingleIndirectBlock(blockNum);
	            	SingleDirectBlock sdb = level2Block.
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
