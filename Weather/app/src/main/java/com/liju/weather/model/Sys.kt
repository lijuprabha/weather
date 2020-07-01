import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class Sys (

	val type : Int,
	val id : Int,
	val country : String,
	val sunrise : Long,
	val sunset : Long
){
	fun getDate(milliSeconds: Long): String? {

		val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
		val milli = TimeUnit.SECONDS.toMillis(milliSeconds)
		val calendar: Calendar = Calendar.getInstance()
		calendar.timeInMillis = milli
		return formatter.format(calendar.getTime())
	}
}