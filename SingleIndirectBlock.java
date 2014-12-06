import java.util.ArrayList;

/**
 * Single Indirect Block Each Single Indirect Block contains max 100 direct
 * blocks
 * 
 * @author Jingjing&Ning
 * 
 */
public class SingleIndirectBlock {

	// File name
	private String fileName = "level1_indirection.txt";

	// Stores 100 SingleIndirectBlocks
	private ArrayList<DirectBlock> directBlocks = new ArrayList<DirectBlock>();

	public SingleIndirectBlock() {
		init();
	}

	private void init() {
		for (int i = 0; i < 100; i++) {
			directBlocks.add(new DirectBlock(-1));
		}
	}

	/**
	 * get the block index in this array from the real block index The Single
	 * Indirect Block contains max of 100 direct block, starting from blockIndex
	 * 12 up to blockIndex 111
	 * 
	 * @param blockIndex
	 * @return int the target index in this class
	 */
	public int getIndexOfDirectBlock(int actualBlockIndex) {
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

	/**
	 * gets the corresponding DB from the list according to the actual block
	 * index
	 * 
	 * @param actualBlockIndex
	 *            the actual block index
	 * @return
	 * @throws Exception
	 */
	public DirectBlock getDirectBlockFromBlockIndex(int actualBlockIndex) {
		int targetIndex = this.getIndexOfDirectBlock(actualBlockIndex);
		return directBlocks.get(targetIndex);
	}

	// setter for the directBlocks arraylist
	public void setDirectBlockInfo(int index, DirectBlock db) {
		this.directBlocks.set(index, db);
	}
	
	public String getFileName(){
		return fileName;
	}
}
