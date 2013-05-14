package mifareReader.misc;

public class CRC {

	private final int CRC_PRESET = 0xFFFF;
	private final int CRC_POLYNOM = 0xA001;

	public CRC() {
		
	}
	
	/**
	 * Berechnung der CRC16 Pruefsumme Laut "GNetPlus_TM970013.pdf" Seite 5
	 */
	public int[] calcCRC16(int[] data, int istart, int length) {

		int nCRC16=CRC_PRESET;
		
		for(int i=istart;i<(istart+length);i++) {
			nCRC16 ^= data[i];
			for(int k=0;k<8;k++) {
				if((nCRC16 & 1)==1) {
					nCRC16 = (nCRC16>>1) ^ CRC_POLYNOM;
				} else  {
					nCRC16 = (nCRC16>>1);
				}
			}
		}
		
		int[] crc16 = new int[2];
		crc16[0] = (nCRC16>>8)&0x000000FF;
		crc16[1] = nCRC16&0x000000FF;
		return crc16;
	}

}