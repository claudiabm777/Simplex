//interface de notificadores de resultados
public interface NotificadorResultados
{
	
	public void notificarValorVariable(String nombre, double[][] matriz);

	public void notificarValorVariable(String nombre, Double[] arreglo);

	public void notificarValorVariable(String nombre, Integer[] arreglo);

	public void notificarValorVariable(String nombre, Object objeto);

	public void notificarTitulo(String mensaje);

	public void notificarMensaje(String mensaje);

}
