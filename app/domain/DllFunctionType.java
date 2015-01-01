package domain;

public enum DllFunctionType {
	/**
	 * getPcmData (code: getPcmData)
	 */
	GET_PCM_DATA("getPcmData"),
	/**
	 * getFrqData(code: getFrqData)
	 */
	GET_FRQ_DATA("getFrqData");
	
	private final String code;
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param code コード
	 */
	DllFunctionType(String code){
		if(code == null){
			throw new IllegalArgumentException("code is null");
		}
		this.code = code;
	}
	
	/**
	 * コードを取得する。
	 * 
	 * @return コード
	 */
	public String getCode(){
		return code;
	}
	
	/**
	 * コードから{@Link DllFunctionType}を解決する
	 * 
	 * @param code コード
	 * @return {@Link DllFunctionType}
	 */
	public static DllFunctionType resolve(String code){
		if(code == null){
			throw new IllegalArgumentException("code is null");
		}
		
		for(DllFunctionType value: values()){
			if(value.code.equals(code)){
				return value;
			}
		}
		throw new IllegalArgumentException(String.format("unknown code is %s", code));
	}
}
