import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Inode class, writes input .txt files into actual files in system, and reads
 * and writes according to the files
 * 
 * @author Ning&Jingjing
 * 
 */
public class Inode {

	// File name of inode
	private String superFileName = "super_block.txt";
	private String level1FileName = "level1_indirection.txt";
	private String level2FileName = "level2_indirection.txt";

	// Sizes of each level
	private int maxLevel0Size = 12;
	private int maxLevel1Size, maxLevel2Size = 100;
	private int maxBlockNumSofar = 0;

	// This is used for generating string names from int block number
	private HashMap<String, String> numberToStringMap = new HashMap<String, String>();

	// Printers each in charge of printing to one kind of file
	private PrintWriter printer;
	private PrintWriter level1Printer;
	private PrintWriter level2Printer;
	private PrintWriter dataFilePrinter;

	// Created directory according to input file name
	private File directory;

	/**
	 * Constructor
	 * 
	 * @param inputFile
	 */
	public Inode(File inputFile) {

		// Create a directory to store files according to input file name
		String inputFileName = inputFile.getName();
		String partialDirName = inputFileName.substring(0,
				inputFileName.length() - 4);
		String dirName = partialDirName + "_dir";
		directory = new File(dirName);
		directory.mkdir();

		// Printer is in charge of printing to super level, first print in the
		// input file name
		try {
			String path = dirName + "\\" + superFileName;
			printer = new PrintWriter(path);
			printer.println(inputFileName);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found!");
		}

		// Initialize the int to String map
		createNumberMap();

		// Read from input file and create real files in system
		readFromFile(inputFile);
	}

	/**
	 * Helper map for naming
	 */
	private void createNumberMap() {
		numberToStringMap.put("0", "Zero");
		numberToStringMap.put("1", "One");
		numberToStringMap.put("2", "Two");
		numberToStringMap.put("3", "Three");
		numberToStringMap.put("4", "Four");
		numberToStringMap.put("5", "Five");
		numberToStringMap.put("6", "Six");
		numberToStringMap.put("7", "Seven");
		numberToStringMap.put("8", "Eight");
		numberToStringMap.put("9", "Nine");
	}

	/**
	 * Helper to process input file
	 * 
	 * @param inputFile
	 */
	private void readFromFile(File inputFile) {
		try {
			Scanner sc = new Scanner(inputFile);
			int blockNum = 0;
			String data = "";
			// skip first line
			boolean level1used = false;
			boolean level2used = false;

			int prevLevel2SubIndex = 0;
			PrintWriter subfilePrinter = null;

			while (sc.hasNextLine()) {
				String str = sc.nextLine();

				// skip comments
				if (str.charAt(0) != '#') {

					// get the input and parse it
					String[] stringArray = str.split(",");
					blockNum = Integer.parseInt(stringArray[0]);
					data = stringArray[1];
					String dataFileName = getDataFileName(blockNum);
					File newFile = new File(dataFileName);
					if (blockNum >= 0 && blockNum < 12) {

						// set data structure content
						// level0Block.set(blockNum, new DirectBlock(blockNum));

						// output to super_block.txt
						printer.println(dataFileName);

						// add to file map
						// addFileToMap(blockNum, newFile);

						// write data file content
						dataFilePrinter = new PrintWriter(directory.getName()
								+ "\\" + dataFileName);
						dataFilePrinter.println(data);
						dataFilePrinter.close();

					} else if (blockNum >= 12 && blockNum < 112) {

						// if somewhere in the 13th block
						int index = getIndexOfDirectBlock(blockNum);
						// level1Block.setDirectBlockInfo(index, new
						// DirectBlock(blockNum));
						level1used = true;

						// set the printer to write the lvl1 file
						if (level1Printer == null) {
							String path = directory.getName() + "\\"
									+ level1FileName;
							level1Printer = new PrintWriter(path);
						}
						writeToOtherLevelFile(level1Printer,
								getDataFileName(blockNum));

						// write use data printer to fill in the data content
						dataFilePrinter = new PrintWriter(directory.getName()
								+ "\\" + dataFileName);
						dataFilePrinter.println(data);
						dataFilePrinter.close();

					} else if (blockNum >= 112 && blockNum < 10112) {

						// Should put into level2 menu
						level2used = true;

						// Under this level2sub menu
						int indexSIB = getIndexOfSingleIndirectBlock(blockNum);

						if (level2Printer == null) {
							String path = directory.getName() + "\\"
									+ level2FileName;
							level2Printer = new PrintWriter(path);
						}

						if (indexSIB > prevLevel2SubIndex) {
							if (subfilePrinter != null) {
								// Moved on to a new index, close previous
								// printer
								subfilePrinter.close();
							}
							// Generate the a new subfile under
							// level2_indireaction
							String level2SubfileName = "level2_indirection"
									+ "_" + getDataFileName(indexSIB);
							// Write the SingleIndirectBlock file name into the
							// level2 file
							level2Printer.println(level2SubfileName);
							// Then write the DB into the subfile
							subfilePrinter = new PrintWriter(
									directory.getName() + "\\"
											+ level2SubfileName);
							subfilePrinter.println(getDataFileName(blockNum));

							// write fill content
							dataFilePrinter = new PrintWriter(
									directory.getName() + "\\" + dataFileName);
							dataFilePrinter.println(data);
							dataFilePrinter.close();
						} else {
							// Still in the same SIB
							if (subfilePrinter == null) {
								// Generate the a new subfile under
								// level2_indireaction
								String level2SubfileName = "level2_indirection"
										+ "_" + getDataFileName(indexSIB);
								// Write the SingleIndirectBlock file name into
								// the level2 file
								level2Printer.println(directory.getName()
										+ "\\" + level2SubfileName);
								// Then write the DB into the subfile
								subfilePrinter = new PrintWriter(
										directory.getName() + "\\"
												+ level2SubfileName);
							}
							// Then write the DB into the subfile
							subfilePrinter.println(getDataFileName(blockNum));

							// write fill content
							dataFilePrinter = new PrintWriter(
									directory.getName() + "\\" + dataFileName);
							dataFilePrinter.println(data);
							dataFilePrinter.close();
						}

						// If this is the last DB to put in the SIB, then this
						// SIB will become the previous one
						prevLevel2SubIndex = indexSIB;
					}
				}
				maxBlockNumSofar = blockNum;
			}
			if (level1used) {
				printer.println(level1FileName);
			}
			if (level2used) {
				printer.println(level2FileName);
			}

			printer.close();
			level1Printer.close();
			level2Printer.close();
			subfilePrinter.close();

		} catch (FileNotFoundException e) {
			System.err.println("File not found. Please scan in new file.");
		}
	}

	/**
	 * Helper method, ask a printer to print certain things
	 * 
	 * @param p
	 * @param content
	 */
	private void writeToOtherLevelFile(PrintWriter p, String content) {
		p.println(content);
	}

	/**
	 * Helper, gets the file name according to its block number
	 * 
	 * @param blockNum
	 * @return
	 */
	private String getDataFileName(int blockNum) {
		String blockNumStr = String.valueOf(blockNum);
		String dataFileName = "";
		for (int i = 0; i < blockNumStr.length(); i++) {
			String s = "" + blockNumStr.charAt(i);
			String result = numberToStringMap.get(s);
			dataFileName += result;
		}
		dataFileName += ".txt";
		return extracted(dataFileName);
	}

	private void readFromAccessFile(File fileName) {

		Scanner sc;
		try {
			sc = new Scanner(fileName);

			String operation = "";
			int blockNum = 0;
			String content = "";
			while (sc.hasNextLine()) {

				String str = sc.nextLine();
				String[] stringArray = str.split(", ");
				int length = stringArray.length;
				switch (length) {
				case 1:
					System.out.println("input length 1, sth wrong!");
					break;
				case 2:
					System.out.println("read an R instruction, verifying");
					operation = stringArray[0];
					if (!operation.equals("R")) {
						System.out.println("should be R but not R, sth wrong");
						break;
					} else {
						blockNum = Integer.parseInt(stringArray[1]);
						if(blockNum > maxBlockNumSofar)
						{
							System.out.println("This block number " + blockNum+ " is out of range, no such entry yet!");
							break;
						}
					}

					// DO STH ABOUT READ
					int targetBlockNum = Integer.parseInt(stringArray[1]);
					// only reading not writing
					String result = RetrieveTargetBlockData(targetBlockNum,
							false, "");
					System.out.println("Read Result is: " + result);
					System.out.println("");
					break;

				case 3:
					System.out.println("read an W instruction, verifying");
					operation = stringArray[0];
					if (!operation.equals("W")) {
						System.out.println("should be W but not W, sth wrong");
						break;
					} else {
						blockNum = Integer.parseInt(stringArray[1]);
						content = stringArray[2];
					}

					RetrieveTargetBlockData(blockNum, true, content);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper with read operation
	 * 
	 * @param targetBlockNum
	 * @return
	 */
	private String RetrieveTargetBlockData(int targetBlockNum, boolean writing,
			String newValue) {

		if (targetBlockNum >= 0 && targetBlockNum < 12) {

			return retrieveFromLevel0(targetBlockNum, writing, newValue);

		} else if (targetBlockNum >= 12 && targetBlockNum < 112) {

			// level 1 indirect blocks, access lvl1 file, then data file
			return retrieveFromLevel1(targetBlockNum, writing, newValue);

		} else if (targetBlockNum >= 112 && targetBlockNum < 10112) {

			// data under level 2 file, look up there
			return retrieveFromLevel2(targetBlockNum, writing, newValue);

		} else {
			System.out.println("Invalid Read Block Number! You entered "
					+ targetBlockNum);
			return "not yet implemented";
		}
	}

	private String retrieveFromLevel2(int targetBlockNum, boolean writing,
			String newValue) {
		// Double Direct Blocks
		try {
			Scanner scanner = new Scanner(new File(directory.getName() + "\\"
					+ superFileName));
			// read the level 1 menu file name
			System.out.println("accessing pointer " + 14
					+ "th pointer of inode. ");

			for (int i = 0; i <= 13; i++) {
				// skip the first 14 lines in front of target line, 1 line of
				// file name + 13 other pointers
				scanner.nextLine();
			}

			// get the lvl2 file name, and get the scanner to scan that
			// file
			String level2FileName = scanner.nextLine();
			scanner = new Scanner(new File(directory.getName() + "\\"
					+ level2FileName));

			// Now calculate the index in the level1 file we need to read.
			int indexSIB = getIndexOfSingleIndirectBlock(targetBlockNum);

			System.out.println("next accessing the #" + indexSIB
					+ "entry of file " + level2FileName);

			// Skip all the indices smaller than the target index
			for (int j = 0; j < indexSIB; j++) {
				scanner.nextLine();
			}

			String level2SubFileName = scanner.nextLine();
			System.out.println("Got target subfile name " + level2SubFileName);


			// Now get which index is the DB stored in the level2sub menu
			int indexDB = getIndexOfDirectBlock(targetBlockNum);
			System.out.println("Now accessing the #" + indexDB + "file of " + level2SubFileName);
			
			// Skip all the indices smaller than the target index
			for (int j = 0; j < indexDB; j++) {
				scanner.nextLine();
			}

			String DBFileName = scanner.nextLine();
			
			// now load the data file, and read the first line which is the
			// actual data
			scanner = new Scanner(new File(directory.getName() + "\\"
					+ DBFileName));
			String result = scanner.nextLine();
			
			if(writing)
			{
				PrintWriter p = new PrintWriter(directory.getName() + "\\"
						+ DBFileName);
				p.println(newValue);
				p.close();
			}
			return result;
			

		} catch (FileNotFoundException e) {
			return e.getMessage();
		}
	}

	private String retrieveFromLevel0(int targetBlockNum, boolean writing,
			String newValue) {
		// Direct Blocks
		try {
			Scanner scanner = new Scanner(new File(directory.getName() + "\\"
					+ superFileName));
			// data blocks
			System.out
					.println("accessing pointer "
							+ targetBlockNum
							+ "th pointer of inode. "
							+ "Next access the #"
							+ (targetBlockNum + 1)
							+ " entry of super_block.txt file, assuming start from #0, "
							+ "and first entry is file name");

			for (int i = 0; i < targetBlockNum + 1; i++) {
				// skip the lines in front of target line
				scanner.nextLine();
			}

			// get the target file name, and get the scanner to scan that
			// file
			String targetFileName = scanner.nextLine();
			scanner = new Scanner(new File(directory.getName() + "\\"
					+ targetFileName));

			// since it is in Direct Blocks range, just read the data of the
			// file
			String result = scanner.nextLine();

			if(writing)
			{
				PrintWriter p = new PrintWriter(directory.getName() + "\\"
						+ targetFileName);
				p.println(newValue);
				p.close();
			}
			
			return result;

		} catch (FileNotFoundException e) {
			return e.getMessage();
		}
	}

	private String retrieveFromLevel1(int targetBlockNum, boolean writing,
			String newValue) {

		// Single Direct Blocks
		try {
			Scanner scanner = new Scanner(new File(directory.getName() + "\\"
					+ superFileName));
			// read the level 1 menu file name
			System.out.println("accessing pointer " + 13
					+ "th pointer of inode. ");

			for (int i = 0; i <= 12; i++) {
				// skip the first 13 lines in front of target line, 1 line of
				// file name + 12 pointers
				scanner.nextLine();
			}

			// get the lvl1 file name, and get the scanner to scan that
			// file
			String level1FileName = scanner.nextLine();
			scanner = new Scanner(new File(directory.getName() + "\\"
					+ level1FileName));

			// Now calculate the index in the level1 file we need to read.
			int index = getIndexOfDirectBlock(targetBlockNum);

			System.out.println("next accessing the #" + index
					+ "entry of file " + level1FileName);
			// Skip all the indices smaller than the target index
			for (int j = 0; j < index; j++) {
				scanner.nextLine();
			}

			String dataFileName = scanner.next();

			// now load the data file, and read the first line which is the
			// actual data
			scanner = new Scanner(new File(directory.getName() + "\\"
					+ dataFileName));
			String result = scanner.nextLine();
			
			if(writing)
			{
				PrintWriter p = new PrintWriter(directory.getName() + "\\"
						+ dataFileName);
				p.println(newValue);
				p.close();
			}		
			return result;

		} catch (FileNotFoundException e) {
			return e.getMessage();
		}
	}

	private String extracted(String result) {
		return result;
	}

	/**
	 * gets the index of the SIB from the actual block index
	 * 
	 * @param blockIndex
	 *            the actual block index in the file system
	 * @return the index of the single indirect block in the array in this class
	 */
	private int getIndexOfSingleIndirectBlock(int blockIndex) {
		if (blockIndex < 112) {
			System.out.println("block index < 112, should not call DIB");
		}
		int minusOffset = blockIndex - 112;
		int index = (int) Math.floor((double) minusOffset / 100);
		return index;
	}

	/**
	 * get the block index in this array from the real block index The Single
	 * Indirect Block contains max of 100 direct block, starting from blockIndex
	 * 12 up to blockIndex 111
	 * 
	 * @param blockIndex
	 * @return int the target index in this class
	 */
	private int getIndexOfDirectBlock(int actualBlockIndex) {
		if (actualBlockIndex < 12) {
			System.out.println("block number < 12, should not access SIB");
		} else if (actualBlockIndex >= 112 && actualBlockIndex <= 211) {
			return actualBlockIndex - 112;
		} else if (actualBlockIndex > 211) {
			// calling from double indirect block
			int minusOffest = actualBlockIndex - 112;
			int mod100 = minusOffest % 100;
			return mod100;
		}
		// if from 11 ~ 111
		return actualBlockIndex - 12;
	}

	public static void main(String[] args) {
		String fileName = "input_file1.txt";
		String readAndWriteFileName = "access_trace1.txt";

		File testFile = new File(fileName);
		Inode inode = new Inode(testFile);
		File accessFile = new File(readAndWriteFileName);
		inode.readFromAccessFile(accessFile);

	}
}
