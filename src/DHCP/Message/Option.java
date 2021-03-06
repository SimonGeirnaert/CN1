package DHCP.Message;

import DHCP.Utilities;

/**
 * Class representing an option of a DHCP message.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class Option {
	
	/**
	 * Variable representing the option code.
	 */
	private int optionCode = 0;
	
	/**
	 * Return the option code of the option.
	 * 
	 * @return The code identifying the option.
	 */
	public int getOptionCode() {
		return optionCode;
	}

	/**
	 * Sets the code identifying the option.
	 * 
	 * @param optionCode 
	 * 		  The option code to set.
	 */
	 void setOptionCode(int optionCode) {
		this.optionCode = optionCode;
	}

	/**
	 * Variable representing the content of the option.
	 */
	private byte[] contents = null;
	
	/**
	 * Return the contents of the option.
	 * 
	 * @return The contents of the option.
	 */
	public byte[] getContents() {
		return contents;
	}

	/**
	 * Sets the contents of the option to a given byte array.
	 * 
	 * @param contents 
	 * 		  The contents to set for the option.
	 */
	 void setContents(byte[] contents) {
		this.contents = contents;
	}

	/**
	 * Represents an option of a DHCP message.
	 * 
	 * @param optionCode 
	 * 		  The code identifying the option.
	 * @param contents 
	 * 		  The contents of the option.
	 * 
	 * @post  The option code is set to the given option code.
	 * @post  The content of the options is set to the given content.
	 */
	public Option(int optionCode, byte[] contents){
		setOptionCode(optionCode);
		setContents(contents);
	}
	
	/**
	 * Returns the byte representation of an option.
	 * 
	 * @return The byte representation of an option as used in a DHCP message.
	 */
	public byte[] returnBytes(){
		byte[] result = new byte[2+getLengthContent()];
		result[0] = Utilities.convertToByteArray(1, getOptionCode())[0];
		result[1] = Utilities.convertToByteArray(1, getLengthContent())[0];
		return Utilities.insertSubArrayInArrayAt(getContents(), result, 2);
	}
	
	/**
	 * Returns the option starting at index in a given array.
	 * 
	 * @param array 
	 * 		  The array in which the option is defined.
	 * @param index 
	 * 		  The index at which the option definition starts.
	 * @return The option that starts at index.
	 */
	public static Option returnOptionAtIndex(byte[] array, int index){
		Option result = new Option(array[index], Utilities.getPartArray(index + 2, index + 1 + array[index + 1], array));
		return result;
	}
	
	/**
	 * Return the length of the content of the option.
	 * 
	 * @return The length of the contents of the option.
	 */
	public int getLengthContent() {
		return getContents().length;
	}
	
	

}
