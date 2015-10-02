import java.util.List;


public class Iteracion
{
	/**
	 * costos no básicas
	 */
	public double [][]costosN;
	
	/**
	 * costos básicas
	 */
	public double [][]costosB;
	/**
	 * matriz no básicas
	 */
	public double [][]noBasica;
	
	/**
	 * matriz basicas
	 */
	public double [][]Basica;
	
	/**
	 * matriz basica invertida
	 */
	public double [][]BInvertida;
	
	/**
	 * vector x actual
	 */
	public double [][]Xactual;
	
	/**
	 * vector xb actual
	 */
	public double [][]Xb;
	
	/**
	 * matris de coef. tecnologicos
	 */
	public double [][]A;
	
	/**
	 * vector b
	 */
	public double [][]vectorB;
	
	/**
	 * lista de subindices basicos
	 */
	public List<Integer>IB;
	
	/**
	 * lista sub indices no basicos
	 */
	public List<Integer>IN;
	
	/**
	 * vector de x actualizada
	 */
	public double [][]Xactualizada;
	
	/**
	 * direccion de movimiento
	 */
	public double [][]direccionM;
	
	/**
	 * longitud de movimiento
	 */
	public double longitudM;
	
	/**
	 * vector costos reducidos
	 */
	public double [][]r;
	
	/**
	 * indica si ya se está en el óptimo
	 */
	public boolean optimo;
	
	/**
	 * constructor de la clase
	 * @param costosNN
	 * @param costosBN
	 * @param noBasicaN
	 * @param BasicaN
	 * @param BInvertidaN
	 * @param XactualN
	 * @param XbN
	 * @param AN
	 * @param vectorBN
	 * @param IBN
	 * @param INN
	 * @param direccionMN
	 * @param longitudMN
	 * @param rN
	 * @param op
	 * @param XactualizadaN
	 */
	public Iteracion(double [][]costosNN,double [][]costosBN,double [][]noBasicaN,double [][]BasicaN,double [][]BInvertidaN,double[][]XactualN,double [][]XbN,double [][]AN,double [][]vectorBN,List<Integer>IBN,List<Integer>INN,double [][]direccionMN,double longitudMN,double [][]rN, boolean op,double [][]XactualizadaN)
	{
		costosN=costosNN;
		costosB=costosBN;
		noBasica=noBasicaN;
		Basica=BasicaN;
		BInvertida=BInvertidaN;
		Xactual=XactualN;
		Xb=XbN;
		A=AN;
		vectorB=vectorBN;
		IB=IBN;
		IN=INN;
		
		direccionM=direccionMN;
		longitudM=longitudMN;
		r=rN;
		optimo=op;
		Xactualizada=XactualizadaN;
	}
	
}
