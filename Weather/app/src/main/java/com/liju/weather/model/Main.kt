

data class Main (
	val temp : Double,
	val feels_like : Double,
	val temp_min : Double,
	val temp_max : Double,
	val pressure : Int,
	val humidity : Int
){
	fun getKelvinToCelsius():String{
		return (temp-273.15).toString()+"°C"
	}
}