package mifareReader.misc;

public enum CodeCommands {

	/*
	 * GNetPlus CodeCommands
	 */
	ACK(0x06, "Acknowledge"),
	NAK(0x15, "Negatice acknowledge"),
	EVN(0x12, "Event message"),
		
	CARD_DETECTED(0x49, "Card detected"),
	CARD_REMOVED(0x52, "Card removed"),
	BUTTON_PRESSED(0x4b, "Button pressed"),
		
	POLLING(0x00, "Polling"),
	GET_VERSION(0x01, "Get Version"),
	RESET(0x1E, "Reset"),

	//Response NAK Code Table
	ACCESS_DENIED(0xE0, "Access Denied"),
	Illegal_Query_Code(0xE4, "Illegel Query Code"),
	Overrun(0xE6, "Overrun, Out of record count"),
	CRC_Error(0xE7, "CRC Error"),
	Query_Number_No_Support(0xEC,"Query Number no support"),
	Out_Of_Memory_Range(0xED, "Out of Memory Range"),
	Address_Number_Out_Of_Range(0xEE, "Address Number out of range"),
	Unknown_NAK(0xEF, "Unknown NAK"),
	
	/*
	 * Mifare Query Function Code Table
	 */
	REQUEST(0x20, "Request"),
	ANTI_COLLISION(0x21, "Anti-collision"),
	Select_Card(0x22, "Select Card"),
	Authenticate(0x23, "Authenticate"),
	READ_BLOCK(0x24, "Read a block"),
	WRITE_BLOCK(0x25, "Write a block"),
	Set_Value(0x26, "Set Value"),
	Read_Value(0x27, "Read Value"),
	
	AUTHENTICATE_WITH_KEY(0x2E, "Authenticate + key"),
	
	AUTO_MODE(0x3F, "AutoMode"),
	
	//Error Code
	
	EMPTY(0x03, "Empty"),
	AUTHENTICATE_ERROR(0x04, "Authenticate error"),
	KEY_ERROR(0x09, "Key error"),
	NOT_AUTHENTICATE_ERROR(0x0A, "Not authenticate error"),
	TRANSFER_ERROR(0x0E, "Transfern error"),
	WRITE_ERROR(0x0F, "Write error"),
	
	NO_TAG_ERROR(0x1F, "No tag error");
	
	int code;
	String description;
	
	private CodeCommands(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getCodeByInt(int code) {
		
		for(CodeCommands c : CodeCommands.values()) {
			if(c.getCode() == code) {
				return c.getDescription();
			}
		}
		return "<no enum entry>";
	}
	
	public String getDescription() {
		return description;
	}
}
