import java.util.ArrayList;

/**
 * Double Indirect Block 
 * @author Jingjing&Ning
 *
 */
public class DoubleIndirectBlock {
	
	// File name
	private String fileName = "level2_indirection.txt";
	
	// Stores 100 SingleIndirectBlocks
	private ArrayList<SingleIndirectBlock> singleIndirectBlocks = new ArrayList<SingleIndirectBlock>();
	
	/**
	 * gets the index of the SIB from the actual block index
	 * @param blockIndex the actual block index in the file system
	 * @return the index of the single indirect block in the array in this class
	 * @throws Exception 
	 */
	public int getIndexOfSingleIndirectBlock(int blockIndex) throws Exception
	{
		if(blockIndex < 112)
		{
			System.out.println("block index < 112, should not call DIB");
			throw new Exception("block index < 112, should not call DIB");
		}
		int minusOffset = blockIndex -112;
		int index = (int)Math.floor((double)minusOffset / 100);
		return index;
	}
	

	/**
	 * gets the corresponding DB from the list according to the actual block index
	 * @param actualBlockIndex the actual block index
	 * @return
	 * @throws Exception 
	 */
	public SingleIndirectBlock getDirectBlockFromBlockIndex(int actualBlockIndex) throws Exception
	{
		int targetIndex = this.getIndexOfSingleIndirectBlock(actualBlockIndex);
		return singleIndirectBlocks.get(targetIndex);
	}
}
