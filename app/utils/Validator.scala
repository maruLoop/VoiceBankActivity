package utils

object Validator {
  // TODO なぜかdomain.DllFunctionType.GET_PCM_DATA.getCodeが出来ない
  def isValidFunctionGetPcmData(param: String): Boolean = param == "getpcmdata"
}