/**
 * Class representing the options field of a DHCP message. Contains all the different options.
 * 
 * @author Laurent De Laere
 * 		   Simon Geirnaert
 *
 */
public class OptionsList {
	
	/**
	 * Variable representing a list of options
	 */
	Option[] options = null;
	
	/**
	 * 
	 * @param options
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
		byte[] result = new byte[256];
		int i = 0;
		for(Option option: getOptions()){
			Utilities.insertSubArrayInArrayAt(option.returnBytes(), result, i);
			i = i + 2 + option.getLength();
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
				index = index + option.getLength() + 2;
			}
		}
		return result;
	}
	
	/**
	 * Adds a given option to the options list.
	 * 
	 * @param option The option to add.
	 */
	public void addOption(Option option){
		Option[] newList = new Option[getOptions().length+1];
		for(int i = 0; i<getOptions().length; i++){
			newList[i] = getOptions()[i];
		}
		newList[getOptions().length] = option;
		setOptions(newList);
	}
	
	/**
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

}
