public enum TipoCostosReducidos
{
	//Enumeración: para facilitar el manejo de la informacion ingresada por el usuario con respecto a la forma en que se escoje el coto reducido favorable
	PRIMERO, ULTIMO, MAYOR, MENOR;
	/**
	 * Se re-define el método toString
	 */
	public String toString() {
		switch (this) {
			case PRIMERO:
				return "cálculos utilizando el primer costo reducido encontrado";
			case ULTIMO:
				return "cálculos utilizando el último costo reducido encontrado";
			case MAYOR:
				return "cálculos utilizando el mayor costo reducido encontrado";
			case MENOR:
				return "cálculos utilizando el menor costo reducido encontrado";
			default:
				return "";
		}
	}
}
