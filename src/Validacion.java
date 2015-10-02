import java.util.List;


public class Validacion
{
	/**
	 * Atributo que sirve para realizar operaciones entre matrices
	 */
	private OperacionMatrices operador;
	
	/**
	 * Constructor de la clase 
	 */
	public Validacion()
	{
		operador=new OperacionMatrices();
	}
	
	/**
	 * Metodo que dice si la matris basica que recibe por parámetro es una base factible para el problema
	 * @param matrizBasica-Matriz a validar
	 * @param vectorB-vector del lado derecho
	 * @return true si es una base factible false de lo contrario
	 */
	public boolean esBaseFactible(double[][] matrizBasica,double[][] vectorB)
	{
		double[][]mInversa=operador.gaussJordan(matrizBasica);
		double[][]x=operador.multiplicar(mInversa, vectorB);
		for(int i=0;i<x.length;i++)
		{
			if(x[i][0]<0)
			{
				return false;
			}
		}
		return true;
		
	}
	/**
	 * Valida que el vector del lado derecho sea positivo, si no retorna el subindice de la fila donde esta el primer número negativo encontrado
	 * @param vectorB-vector lado derecho
	 * @return la posición de la fila donde se encuentra el primer número negativo encontrado, -1 si es positivo
	 */
	public int esNegativoTerminoVectorLadoDerecho(double[][] vectorB)
	{
		for(int i=0;i<vectorB.length;i++)
		{
			if(vectorB[i][0]<0)
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Verifica si quedan artificiales en la lista recibida por parámetro
	 * @param IB- lista de indices básicos
	 * @return- true si quedan artificiales, false dlc.
	 */
	public boolean quedanArtificialesenIB(List<Integer>IB)
	{
		for(int i=0;i<IB.size();i++)
		{
			if(IB.get(i)>9)
			{
				return true;
			}
		}
		return false;
	}
	
}
