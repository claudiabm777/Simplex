public enum TipoCostosReducidos
{
	//Enumeraci�n: para facilitar el manejo de la informacion ingresada por el usuario con respecto a la forma en que se escoje el coto reducido favorable
	PRIMERO, ULTIMO, MAYOR, MENOR;
	/**
	 * Se re-define el m�todo toString
	 */
	public String toString() {
		switch (this) {
			case PRIMERO:
				return "c�lculos utilizando el primer costo reducido encontrado";
			case ULTIMO:
				return "c�lculos utilizando el �ltimo costo reducido encontrado";
			case MAYOR:
				return "c�lculos utilizando el mayor costo reducido encontrado";
			case MENOR:
				return "c�lculos utilizando el menor costo reducido encontrado";
			default:
				return "";
		}
	}
}
