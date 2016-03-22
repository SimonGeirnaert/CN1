package DHCP.Message;

import DHCP.Utilities;

/**
 * Class representing the options field of a DHCP message. 
 * Contains all the different options.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class OptionsList {
	
	/**
	 * Variable representing a list of options
	 */
	private Option[] options = null;
	
	/**
	 * Initialize the options list with given options.
	 * 
	 * @param options
	 * 		  The options to add to the list.
	 * @post  The options are equal to the given options.
	 */
	public OptionsList(Option ... options){
		setOptions(options);
	}

	/**
	 * Returns the byte representation of the options as used in a DHCP message.
	 * 
	 * @return The byte representation of the options as used in a DHCP message.
	 */
	public byte[] returnBytes(){
		byte[] result = new byte[Message.MESSAGE_SIZE-240];
		int i = 0;
		for(Option option: getOptions()){
			Utilities.insertSubArrayInArrayAt(option.returnBytes(), result, i);
			i = i + 2 + option.getLengthContent();
		}
		return result;
	}
	
	/**
	 * Creates the OptionsList object out of a byte array as used in a DHCP message.
	 * 
	 * @param bytes 
	 * 		  The byte array to convert
	 * @return An OptionsList object that represents the given byte array bytes.
	 */
	public static OptionsList returnOptionsList(byte[] bytes){
		OptionsList result = new OptionsList();
		int index = 0;
		while(index < bytes.length){
			if(bytes[index]==0) 
				index++;
			else {
				Option option = Option.returnOptionAtIndex(bytes, index);
				result.addOption(option);
				index = index + option.getLengthContent() + 2;
			}
		}
		return result;
	}
	
	/**
	 * Adds a given option to the options list.
	 * 
	 * @param option The option to add.
	 */
	private void addOption(Option option){
		Option[] newList = new Option[getOptions().length+1];
		for(int i = 0; i<getOptions().length; i++){
			newList[i] = getOptions()[i];
		}
		newList[getOptions().length] = option;
		setOptions(newList);
	}
	
	/**
	 * Get an option with given option code from the options list.
	 * 
	 * @param optionCode
	 * 		  The option code of the searched option.
	 * @return The searched option.
	 * @throws IllegalArgumentException
	 * 		   The option is not available in the list
	 */
	public Option getOption(int optionCode) throws IllegalArgumentException {
		for(Option option : getOptions()) {
			if(option.getOptionCode() == optionCode)
				return option;
		}
		throw new IllegalArgumentException("This option is not present in the list.");
	}
	
	/**
	 * Return the options of the options list.
	 * 
	 * @return The options of the options list.
	 */
	public Option[] getOptions() {
		return options;
	}

	/**
	 * Sets the options of the options list.
	 * 
	 * @param options 
	 * 		  The options to set.
	 */
	private void setOptions(Option[] options) {
		this.options = options;
	}
	
	/**
	 * Return the number of options in the options list.
	 * 
	 * @return The number of options.
	 */
	public int getNumberOfOptions() {
		return getOptions().length;
	}
	
	/**
	 * Return the option at a given index of the option list.
	 * 
	 * @param index
	 * 		  The index of the searched option.
	 * @return The option associated with the given index.
	 * @throws IndexOutOfBoundsException
	 * 		   The given index is not positive or it exceeds the number of options in the list.
	 */
	public Option getOptionAt(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index > getNumberOfOptions() - 1)
			throw new IndexOutOfBoundsException();
		return getOptions()[index];
	}

}
