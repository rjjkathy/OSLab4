/**
 * Single Indirect Block
 * @author Jingjing&Ning
 *
 */
public class SingleIndirectBlock {

	// File name
	private String fileName = "level1_indirection.txt";
	
	// Starting block index
	private int startingIndex;
	
	// Stores 100 SingleIndirectBlocks
	private DirectBlock[] directBlocks = new DirectBlock[100];
	
	public SingleIndirectBlock(int startingIndex)
	{
		// Records the corresponding starting index of the real block index
		this.startingIndex = startingIndex;
	}
	/**
	 * get the block index in this array from the real block index
	 * @param blockIndex
	 * @return int the target index in this class
	 */
	public int getIndexOfDirectBlock(int blockIndex)
	{
		// TODO implement this to return the index of target SIB of the target block
		return 0;
	}
	
	

}
