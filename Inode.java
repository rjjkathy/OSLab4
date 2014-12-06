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
						addFileToMap(blockNum);
					} else if (blockNum >= 12 && blockNum < 112) {
						int index = level1Block.getIndexOfDirectBlock(blockNum);
						level1Block.setDirectBlockInfo(index, new DirectBlock(blockNum));
						addFileToMap(blockNum);
					} else if (blockNum >= 112 && blockNum < 10112) {
						int index1 = level2Block.getIndexOfSingleIndirectBlock(blockNum);
						SingleIndirectBlock sdb = level2Block.getSingleIndirectBlocks(index1);
						int index2 = sdb.getIndexOfDirectBlock(blockNum);
						sdb.setDirectBlockInfo(index2,new DirectBlock(blockNum));
						addFileToMap(blockNum);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		}
	}
	
	private void addFileToMap(int blockNum){
		String blockNumStr = String.valueOf(blockNum);
		String dataFileName = "";
		for(int i = 0; i < blockNumStr.length();i++){
			dataFileName += numberToStringMap.get(i);
		}
		dataFileName += ".txt";
		File dataFile = new File(dataFileName);
		addressToDataMap.put(blockNum, dataFile);
	}

	private void writeToFile(String fileName, String content) {
		try {
			File output = new File(fileName);
			PrintWriter printer = new PrintWriter(output);
			printer.write(content);
		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		}

	}

	public static void main(String[] args) {
		String fileName = "inputfile_1.txt";
		File testFile = new File(fileName);
		Inode inode = new Inode(testFile);
	}

}
