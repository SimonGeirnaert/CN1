import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to provide utilities and conversion methods.
 * 
 * @version 1.0 - 2016
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public abstract class Utilities {
	
	/**
	 * Convert integer to array of bytes.
	 * 
	 * @param nbOfBytes
	 * 		  Number of bytes of the byte array.
	 * @param content
	 * 		  Integer to convert to bytes.
	 * @return Returns the integer converted to a byte array.
	 */
	public static byte[] convertToByteArray(int nbOfBytes, int content){
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(content);
		byte[] tempBytes = bb.array();
		byte[] bytes = new byte[nbOfBytes];
		for(int i=0; i<nbOfBytes; i++){
				bytes[i] = tempBytes[i+(4-nbOfBytes)];
		}
		return bytes;
	}
	
	/**
	 * Convert byte array to integer.
	 * 
	 * @param bytes
	 * 		  The byte array to convert to an integer.
	 * @return The converted byte array to integer.
	 * @throws IllegalArgumentException
	 * 		   The number of bytes can not exceed four.
	 * 		   | bytes.length <= 4
	 */
	public static int convertToInt(byte[] bytes){
		if(bytes.length > 4)
			throw new IllegalArgumentException("The number of bytes can not exceed four.");
		int nbOfBytes = bytes.length;
		byte[] convertedBytes = new byte[4];
		for(int i=0; i<nbOfBytes; i++){
			convertedBytes[i+(4-nbOfBytes)] = bytes[i];
		}
		ByteBuffer bb = ByteBuffer.wrap(convertedBytes); // big-endian by default
		int result = bb.getInt();
		return result;
	}
	
	/**
	 * Insert a given byte array in another given byte array at a given index.
	 *
	 * @param addArray
	 * 		  The byte array to insert into the given array.
	 * @param array
	 * 		  The array to insert the given array into.
	 * @param index
	 * 		  The index to insert the given subarray at.
	 */
	public static byte[] insertSubArrayInArrayAt(byte[] addArray, byte[] array, int index){
		byte[] result = array;
		int nbOfBytes = addArray.length;
		for(int i=0; i<nbOfBytes; i++){
			result[index+i] = addArray[i];
		}
		return result;
	}
	
	/**
	 * Remove all tailing zeros from a byte array.
	 * 
	 * @param array
	 * 		  The byte array to be tailed.
	 * @return The tailed byte array.
	 */
	public static byte[] trimZeros(byte[] array){
		int size = 0;
		for(int i=0; i<array.length; i++)
			if(array[i]!=0 && i<array.length){
				size++;
			}
		byte[] result = new byte[size];
		for(int i=0; i<size; i++) 
			result[i] = array[i];
		return result;
	}

	/**
	 * Extract subarray from given array within given indices.
	 * 
	 * @param beginIndex
	 * 		  The begin index to extract the subarray from.
	 * @param endIndex
	 * 		  The end index to extract the subarray from.
	 * @param array
	 * 		  The array to extract a subarray from.
	 * @return The subtracted subarray.
	 * @pre The begin index has to be smaller than the end index
	 * 		| beginIndex <= endIndex
	 */
	public static byte[] getPartArray(int beginIndex, int endIndex, byte[] array) {
		int size = endIndex-beginIndex+1;
		byte[] result = new byte[size];
		for(int k=0; k<size; k++)
			result[k] = array[beginIndex+k];
		return result;
	}

	/**
	 * Generates a random int to use as transaction ID.
	 * 
	 * @return A random int.
	 */
	public static int generateXid() {
		return ThreadLocalRandom.current().nextInt();
	}
}
