import java.util.ArrayList;

/**
 * Single Indirect Block
 * Each Single Indirect Block contains max 100 direct blocks
 * @author Jingjing&Ning
 *
 */
public class SingleIndirectBlock {

	// File name
	private String fileName = "level1_indirection.txt";
	
	// Starting block index
	private int startingIndex;
	
	// Stores 100 SingleIndirectBlocks
	private ArrayList<DirectBlock> directBlocks = new ArrayList<DirectBlock>();
	
	public SingleIndirectBlock(int startingIndex)
	{
		// Records the corresponding starting index of the real block index
		this.startingIndex = startingIndex;
	}
	/**
	 * get the block index in this array from the real block index
	 * The Single Indirect Block contains max of 100 direct block, starting from blockIndex 12 up to blockIndex 111
	 * @param blockIndex
	 * @return int the target index in this class
	 */
	public int getIndexOfDirectBlock(int actualBlockIndex)
	{
		if(actualBlockIndex < 12)
		return actualBlockIndex - 12;
	}
	
	/**
	 * gets the corresponding DB from the list according to the actual block index
	 * @param actualBlockIndex the actual block index
	 * @return
	 */
	public DirectBlock getDirectBlockFromBlockIndex(int actualBlockIndex)
	{
		int targetIndex = this.getIndexOfDirectBlock(actualBlockIndex);
		return directBlocks.get(targetIndex);
	}

}
