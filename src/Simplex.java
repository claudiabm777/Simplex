import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Simplex
{
	/**
	 * Atributo que representa el infinito en nuestro ejercicio ya que se inicializa con gran M
	 */
	public static double INFINITO=999;
	
	/**
	 * atributo que indica si el problema es de maximizacion (true) o minimizacion(false)
	 */
	public boolean minmax;
	
	/**
	 * Vector de costos de las no básicas
	 */
	public double [][]costosN;
	
	/**
	 * Vector de costos de las básicas
	 */
	public double [][]costosB;
	
	/**
	 * matriz de las no basicas
	 */
	public double [][]noBasica;
	
	/**
	 * matriz de las básicas
	 */
	public double [][]Basica;
	
	/**
	 * matriz de B-1
	 */
	public double [][]BInvertida;
	
	/**
	 * vector x actual
	 */
	public double [][]Xactual;
	
	/**
	 * matriz de coeficientes tecnológicos
	 */
	public double [][]A;
	
	/**
	 * vector lado derecho
	 */
	public double [][]vectorB;
	
	/**
	 * subindices de las básicas
	 */
	public List<Integer>IB;
	
	/**
	 * subindices de las no basicas
	 */
	public List<Integer>IN;
	
	/**
	 * opcion escogida por el usuario para calcular costos reducidos
	 */
	public TipoCostosReducidos opcion;
	
	/**
	 * Notificador de resultados
	 */
	public NotificadorResultados notificador;
	
	/**
	 * validador de ciertas características del problema
	 */
	public Validacion validador;
	
	/**
	 * constructor de la clase, se encarga de inicializar el problema de acuerdo a lo que recibe por parámetro
	 * @param AN-matriz A
	 * @param INN-sub indices básicos
	 * @param IBN-subindices no basicos
	 * @param vectorBN- vector lado derecho
	 * @param costos-vector costos
	 * @param mminmax-tipo de problema
	 * @param opcionN-opcion de costos reducidos
	 * @param notificadorN-notificador  de la interfaz
	 */
	public Simplex(double [][]AN, List<Integer>INN, List<Integer>IBN, double [][]vectorBN, List<Double>costos, boolean mminmax, TipoCostosReducidos opcionN, NotificadorResultados notificadorN)
	{
		validador=new Validacion();
		opcion=opcionN;
		minmax=mminmax;
		A=AN;
		IB=IBN;
		IN=INN;
		vectorB=vectorBN;
		Basica=new double[5][5];
		notificador=notificadorN;
		
		notificador.notificarTitulo("Datos de entrada (Si algun valor del vector del lado derecho (b) era negativo se multiplico esta fila por -1:");
		notificador.notificarValorVariable("A",AN);
		notificador.notificarValorVariable("b",vectorBN);
		notificador.notificarValorVariable("C",costos.toArray(new Double[0]));
		notificador.notificarValorVariable("IN",INN==null?new Integer[0]:INN.toArray(new Integer[0]));
		notificador.notificarValorVariable("IB",IBN==null?new Integer[0]:IBN.toArray(new Integer[0]));
		notificador.notificarValorVariable("maximizando",mminmax?"SI":"NO");
		notificador.notificarValorVariable("opcion",opcionN);

		if (costos.size()!=9)
		{
			throw new IllegalArgumentException("Debe entregar nueve costos");
		}

		if (vectorBN.length!=5 || vectorBN[0].length!=1)
		{
			throw new IllegalArgumentException("El vector b debe tener 5 filas y 1 columna");
		}

		if (IB!=null && IB.size()!=5)
		{
			throw new IllegalArgumentException("Debe proveer exactamente cinco variables básicas en IB");
		}

		if (IN!=null && IN.size()!=4)
		{
			throw new IllegalArgumentException("Debe proveer exactamente cuatro variables no básicas en IN");
		}

		if ((IN==null)!=(IB==null))
		{
			throw new IllegalArgumentException("Ingrese completamente los valores de IB e IN, o déjelos todos vacíos!");	
		}

		if (AN.length!=5 || AN[0].length!=9)
		{
			throw new IllegalArgumentException();	
		}
		
		if(IN==null && IB==null)
		{
			IB=new ArrayList<Integer>();
			IN=new ArrayList<Integer>();
			int columnas=9;
			int col=-1;
			for (int i=0;i<A[0].length;i++)
			{
				int c=0;
				int suma=0;
				for(int j=0;j<A.length;j++)
				{
					if(sonIguales(A[j][i],0))
					{
						c++;
					}
					suma+=A[j][i];
				}
				if(c==4 && sonIguales(suma,1))
				{
					col++;
					IB.add(i+1);
					
					for(int k=0;k<A.length;k++)
					{
						Basica[k][col]=A[k][i];
						    
					}					
				}
			}
			
			if(4-col!=0)
			{
				for(int i=0;i<5;i++)
				{
					double sum=0;
					for(int j=0;j<col+1;j++)
					{
						sum+=Basica[i][j];
					}
					if(sonIguales(sum,0))
					{
						Basica[i][col+1]=1;
						columnas++;
						IB.add(columnas);
						col++;
					}
					else if (!sonIguales(sum,1))
					{
						throw new IllegalArgumentException("Debe ingresar datos correctos");
					}
					
				}
			}
			
			noBasica=new double[5][columnas-5];
			int b=0;
			for(int j=0;j<A[0].length;j++)
			{
				if (!IB.contains(j+1))
				{
					for(int i=0;i<5;i++)
					{
						noBasica[i][j-b]=A[i][j];
					}
					IN.add(j+1);	
				}
				else
				{
					b+=1;
				}
			}
			
		}
		else if (IN!=null && IB!=null)
		{
			List<Integer> listaConjunta=new ArrayList<Integer>();
			listaConjunta.addAll(IN);
			listaConjunta.addAll(IB);
			Collections.sort(listaConjunta);
			
			if (listaConjunta.get(0)!=1 || listaConjunta.get(8)!=9)
			{
				throw new IllegalArgumentException("Debe proveer subíndices correctos");
			}

			
			noBasica=new double[5][4];

			for(int i=0;i<4;i++)
			{
				for(int j=0;j<5;j++)
				{
					noBasica[j][i]=A[j][IN.get(i)-1];
				}
			}

			for(int i=0;i<5;i++)
			{
				for(int j=0;j<5;j++)
				{
					Basica[j][i]=A[j][IB.get(i)-1];
				}
			}
			
		}
		
		costos = new ArrayList<Double>(costos);

		for(int i=0;i<5;i++)
		{
			int c=IN.size()+5-9;
			
			for(int l=0;l<c;l++)
			{
				if(minmax)
				{
					costos.add(-INFINITO);
				}
				else
				{
					costos.add(INFINITO);
				}
			}
		}
		costosB = new double[1][5];
		costosN = new double[1][IN.size()];
		
		for(int i=0;i<5;i++)
		{
			costosB[0][i]=costos.get(IB.get(i)-1);
		}
		
		for(int i=0;i<IN.size();i++)
		{
			costosN[0][i]=costos.get(IN.get(i)-1);
		}
		
		BInvertida=OperacionMatrices.gaussJordan(Basica);
		
		if(BInvertida==null)
		{
			throw new IllegalArgumentException("Dé una solución inicial válida o deja IB e IN en blanco para que se inicialice solo.");
		}
		
		Xactual=new double[(IB.size()+IN.size())][1];
		
		double[][]m = OperacionMatrices.multiplicar(BInvertida, vectorB);
		for(int i=0;i<m.length;i++)
		{
			Xactual[i][0]=m[i][0];
		}
		
	}
	/**
	 * se encarga de realizar las iteraciones y notificar a la interfas de cada una de estas y si ha encontrado optimos o no
	 * @return-una lista con las iteraciones
	 */
	public List<Iteracion> iteracionesSimplex()
	{
		List<Iteracion>	respuesta=new ArrayList<Iteracion>();
		
		while (true)
		{
			notificador.notificarTitulo("Iteración #"+(respuesta.size()+1));
			notificador.notificarValorVariable("B",Basica);
			notificador.notificarValorVariable("N",noBasica);
			notificador.notificarValorVariable("b",vectorB);
			notificador.notificarValorVariable("IB",IB.toArray(new Integer[0]));
			notificador.notificarValorVariable("IN",IN.toArray(new Integer[0]));
			notificador.notificarValorVariable("CB^t",costosB);
			notificador.notificarValorVariable("CN^t",costosN);

			RevisionOptimalidad rev=new RevisionOptimalidad(minmax, costosN, costosB, BInvertida, noBasica, vectorB, Xactual, opcion);
			double xb[][]=rev.darXb();
			notificador.notificarValorVariable("XB",xb);
			notificador.notificarValorVariable("r^t",rev.r);
			notificador.notificarValorVariable("z=CB^t*XB",OperacionMatrices.multiplicar(costosB,xb));
			
			
			if(!validador.esBaseFactible(Basica, vectorB))
			{
				System.out.println("esta aca");
				notificador.notificarMensaje("Problema Infactible");
				Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  null, -1, rev.r, true,null);
				respuesta.add(it);
				return respuesta;
			}
			else if(rev.estamosEnElOptimo())
			{
				if(validador.quedanArtificialesenIB(IB))
				{
					notificador.notificarMensaje("Problema Infactible, aún hay variables artificiales en las básicas");
					Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  null, -1, rev.r, true,null);
					respuesta.add(it);
					return respuesta;
				}
				else
				{
				notificador.notificarMensaje("Óptimo encontrado");
				Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  null, -1, rev.r, true,null);
				respuesta.add(it);
				return respuesta;
				}
			}
			else if(rev.hayOptimosAlternos())
			{
				notificador.notificarMensaje("Hay óptimos alternos");
				Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  null, -1, rev.r, true,null);
				respuesta.add(it);
				return respuesta;
			}
			else
			{	
				double[][] direccion=rev.calculoDireccionMovimiento();
				notificador.notificarValorVariable("d^t",direccion);
				boolean noAcotado=true;
				
				for(int i=0;i<direccion.length;i++)
				{
					if(direccion[i][0]<0)
					{
						noAcotado=false;
					}
				}
				if(noAcotado)
				{	
					notificador.notificarMensaje("El problema es no acotado");
					Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  direccion, -1, rev.r, true,null);
					respuesta.add(it);
					return respuesta;
				}
				else
				{
					Iteracion it=new Iteracion(costosN, costosB, noBasica, Basica, BInvertida, Xactual, xb, A, vectorB, IB, IN,  direccion, rev.longitudDeMovimiento(), rev.r, false,rev.actualizacion());
					respuesta.add(it);
				}
				
				int salePosB=rev.posB;
				int entraPosN=rev.posN;
				
				for(int i=0;i<5;i++)
				{
					double temporal=Basica[i][salePosB];
					Basica[i][salePosB]=noBasica[i][entraPosN];
					noBasica[i][entraPosN]=temporal;
				}
				
				double temporal=costosB[0][salePosB];
				costosB[0][salePosB]=costosN[0][entraPosN];
				costosN[0][entraPosN]=temporal;
					
				int temporalInt=IN.get(entraPosN);
				IN.set(entraPosN, IB.get(salePosB));
				IB.set(salePosB, temporalInt);

				BInvertida=OperacionMatrices.gaussJordan(Basica);

				if(BInvertida==null)
				{
					throw new IllegalArgumentException("Error interno: una base intermedia dio no invertible");
				}

			}
		}
	}
	/**
	 * metodo que indica si dos doubles son iguales (se consideran iguales cuando el valor absoluto de la resta es un número menor a 10^-10 con el fin de evitar problemas con el redondeo
	 * @param a-double a
	 * @param b-double b
	 * @return-true si son iguales, false de lo contrario
	 */
	public static boolean sonIguales(double a, double b) {
		return Math.abs(a-b)<1E-10;
	}
}
