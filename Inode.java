import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Inode class
 * 
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
	private HashMap<String, String> numberToStringMap = new HashMap<String, String>();

	public Inode(File inputFile){
		readFromFile(inputFile);
		createNumberMap();
	}

	private void createNumberMap() {
		numberToStringMap.put("0","Zero");
		numberToStringMap.put("1","One");
		numberToStringMap.put("2","Two");
		numberToStringMap.put("3","Three");
		numberToStringMap.put("4","Four");
		numberToStringMap.put("5","Five");
		numberToStringMap.put("6","Six");
		numberToStringMap.put("7","Seven");
		numberToStringMap.put("8","Eight");
		numberToStringMap.put("9","Nine");
	}

	public void read(int blockNum) {

	}

	public void write(int blockNum, String content) {

	}

	private void readFromFile(File inputFile){
		try {
			Scanner sc = new Scanner(inputFile);
			int blockNum = 0;
			String data = "";
			// skip first line
			while (sc.hasNextLine()) {
				String str = sc.nextLine();
				// skip comments
				if (str.charAt(0) != '#') {
					String[] stringArray = str.split(",");
					blockNum = Integer.parseInt(stringArray[0]);
					data = stringArray[1];
					if (blockNum >= 0 && blockNum < 12) {
						level0Block.set(blockNum, new DirectBlock(blockNum));
						addFileToMap(blockNum, data);
					} else if (blockNum >= 12 && blockNum < 112) {
						int index = level1Block.getIndexOfDirectBlock(blockNum);
						level1Block.setDirectBlockInfo(index, new DirectBlock(blockNum));
						addFileToMap(blockNum, data);
					} else if (blockNum >= 112 && blockNum < 10112) {
						int index1 = level2Block.getIndexOfSingleIndirectBlock(blockNum);
						SingleIndirectBlock sdb = level2Block.getSingleIndirectBlocksContent(index1);
						int index2 = sdb.getIndexOfDirectBlock(blockNum);
						sdb.setDirectBlockInfo(index2,new DirectBlock(blockNum));
						addFileToMap(blockNum, data);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		}
	}
	
	private void addFileToMap(int blockNum, String data){
		String blockNumStr = String.valueOf(blockNum);
		String dataFileName = "";
		for(int i = 0; i < blockNumStr.length();i++){
			dataFileName += numberToStringMap.get(i);
		}
		dataFileName += ".txt";
		File dataFile = new File(dataFileName);
		writeToFile(dataFile, data);
		addressToDataMap.put(blockNum, dataFile);
	}

	private void writeToFile(File file, String data) {
		try {
			PrintWriter printer = new PrintWriter(file);
			printer.write(data);
		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		}
	}

	public static void main(String[] args) {
		String fileName = "inputfile_1.txt";
		File testFile = new File(fileName);
		Inode inode = new Inode(testFile);
	}
	
	private void reafFromAccessFile(String fileName)
	{
		Scanner sc = new Scanner(fileName);
		String operation ="";
		int blockNum = 0;
		String content = "";
		while(sc.hasNextLine()) {
			
			 String str = sc.nextLine();
		     String[] stringArray = str.split(", ");
		     int length = stringArray.length;
		     switch(length){
		    	 case 1:
		    		 System.out.println("input length 1, sth wrong!");
		    		 break;
		    	 case 2:
		    		 System.out.println("read an R instruction, verifying");
		    		 operation = stringArray[0];
		    		 if(!operation.equals("R"))
		    		 {
		    			 System.out.println("should be R but not R, sth wrong");
		    			 break;
		    		 }
		    		 else
		    		 {
		    			 blockNum = Integer.parseInt(stringArray[1]);
		    		 }
		    		 
		    		 // DO STH ABOUT READ
		    		 
		    		 break;		    		 
		    	 case 3:
		    		 System.out.println("read an W instruction, verifying");
		    		 operation = stringArray[0];
		    		 if(!operation.equals("W"))
		    		 {
		    			 System.out.println("should be W but not W, sth wrong");
		    			 break;
		    		 }
		    		 else
		    		 {
		    			 blockNum = Integer.parseInt(stringArray[1]);
		    			 content = stringArray[2];	        			 
		    		 }
		    		 
		    		 // DO STH ABOUT WRITE
		    		 
		    		 break;
		     }           
		}

}
