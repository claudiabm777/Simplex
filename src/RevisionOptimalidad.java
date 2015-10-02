import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RevisionOptimalidad
{
	/**
	 * Atributo que define si el problema es de minimizaci�n o maximizaci�n
	 */
	public boolean minmax;
	
	/**
	 * Vector de costos no b�sicos
	 */
	public double[][]cn;
	
	/**
	 * Matriz de variables no-b�sicas
	 */
	public double[][]n;
	
	/**
	 * vector w transpuesto
	 */
	public double[][]wt;
	
	/**
	 * vector costos reducidos
	 */
	public double[][]r;
	
	
	/**
	 * matris basica inversa
	 */
	private double[][]BInversa;
	
	/**
	 * vector lado derecho
	 */
	private double[][]vectorB;
	
	/**
	 * posicion de la que entra a las basicas
	 */
	public int posN=-1;
	
	/**
	 * posicion de la que sale de las b�sicas
	 */
	public int posB=-1;
	
	/**
	 * opcion de seleccion del costo reducido escogida por el usuario
	 */
	public TipoCostosReducidos opcion;
	
	/**
	 * vector x actual
	 */
	private double[][]Xactual;
	
	/**
	 * vector X actualizado con los resultados de la iteraci�n
	 */
	public double[][]Xactualizada;
	
	
	/**
	 * Constructor de la clase
	 * @param minmaxV-true si se maximiza, false si se minimiza
	 * @param cnN-vector costos de las no b�sicas
	 * @param cb-vector costos de las b�sicas
	 * @param B-matriz de las variables b�sicas
	 * @param nN-matriz de las no-b�sicas
	 */
	public RevisionOptimalidad(boolean minmaxV, double[][]cnN,double[][]cb,double[][]BInversaN, double[][]nN,double[][]bN,double [][]xAc, TipoCostosReducidos opcionN)
	{
		vectorB=bN;
		minmax=minmaxV;
		cn=cnN;
		n=nN;
		BInversa=BInversaN;

		wt=OperacionMatrices.multiplicar(cb, BInversa);

		double[][]matriz=OperacionMatrices.multiplicar(wt, n);
		r=calcularCostoReducido(matriz);
		posN=-1;
		opcion=opcionN;
		Xactual=xAc;
	}
	
	/**
	 * calcula el vector costos reducidos
	 * @param matriz-matriz a restar al vector cn
	 * @return el vesctor de costos reducidos
	 */
	private double[][]calcularCostoReducido(double[][]matriz)
	{
		return OperacionMatrices.restarMatrices1Fila(cn, matriz);
	}
	
	/**
	 * da el vector xb (variables b�sicas)
	 * @return
	 */
	public double[][]darXb()
	{
		return OperacionMatrices.multiplicar(BInversa, vectorB);
	}
	
	/**
	 * indica si los costos reducidos (dependiendo de si se esta maximizando o minimizando) dejan de ser favorables o no
	 * @return true si no son favorables los costos reducidos, false dlc
	 */
	public boolean estamosEnElOptimo()
	{
		if(minmax)
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{
					return false;
				}
			}
			
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * verifica si hay algun cero en los costos reducidos
	 * @return true si si existe alguno
	 */
	private boolean hayAlgunCero()
	{
		for(int i=0;i<r[0].length;i++)
		{
			if(Simplex.sonIguales(r[0][i],0))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * verifica si hay �ptimos alternos
	 * @return true si hay optimos alternos, false dlc
	 */
	public boolean hayOptimosAlternos()
	{
		if(hayAlgunCero()==false)
		{
			return false;
		}
		
		if(minmax)
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{
					return false;
				}
			}
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					return false;
				}
			}
			
		}
		return true;
		
	}
	
	/**
	 * da la fila de la matriz no b�sica de acuerdo al costo reducido favorable mayor
	 * @return-la fila de la no b�sica
	 */
	private double[][]darFilaNoBasicaDireccionCostoReducidoMayor()
	{
		double[][]respuesta=new double[n.length][1];
		double mayor=Double.NEGATIVE_INFINITY;
		posN=-1;
		if(minmax==true)
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{
					if(r[0][i]>mayor)
					{
						mayor=r[0][i];
						posN=i;
					}
				}
			}
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					if(r[0][i]>mayor)
					{
						mayor=r[0][i];
						posN=i;
					}
				}
			}
		}
		for(int i=0;i<n.length;i++)
		{
			respuesta[i][0]=n[i][posN];
		}
		return respuesta;
	}
	
	/**
	 * da la fila de la matriz no b�sica de acuerdo al costo reducido favorable menor
	 * @return-la fila de la no b�sica
	 */
	public double[][]darFilaNoBasicaDireccionCostoReducidoMenor()
	{
		double[][]respuesta=new double[n.length][1];	
		double menor=Double.POSITIVE_INFINITY;
		posN=-1;
		if(minmax==true)
		{			
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{
					if(r[0][i]<menor)
					{
						menor=r[0][i];
						posN=i;
					}
				}
			}
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					if(r[0][i]<menor)
					{
						menor=r[0][i];
						posN=i;
					}
				}
			}
		}
		for(int i=0;i<n.length;i++)
		{
			respuesta[i][0]=n[i][posN];
		}
		return respuesta;
	}
	
	/**
	 * da la fila de la matriz no b�sica de acuerdo al costo reducido favorable primero encontrado
	 * @return-la fila de la no b�sica
	 */
	public double[][]darFilaNoBasicaDireccionCostoReducidoPrimero()
	{
		double[][]respuesta=new double[n.length][1];
		posN=-1;
		if(minmax==true)
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{	
					posN=i;
					for(int ii=0;ii<n.length;ii++)
					{
						respuesta[ii][0]=n[ii][posN];
					}	
					return respuesta;
				}
			}
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					posN=i;
					for(int ii=0;ii<n.length;ii++)
					{
						respuesta[ii][0]=n[ii][posN];
					}
					return respuesta;
				}
			}
		}
		return respuesta;
	}
	
	/**
	 * da la fila de la matriz no b�sica de acuerdo al costo reducido favorable ultimo encontrado
	 * @return-la fila de la no b�sica
	 */
	public double[][]darFilaNoBasicaDireccionCostoReducidoUltimo()
	{
		double[][]respuesta=new double[n.length][1];
		posN=-1;
		if(minmax==true)
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]>0)
				{
					posN=i;		
				}
			}
		}
		else
		{
			for(int i=0;i<r[0].length;i++)
			{
				if(r[0][i]<0)
				{
					posN=i;
				}
			}
		}
		for(int i=0;i<n.length;i++)
		{
			respuesta[i][0]=n[i][posN];
		}

		return respuesta;
	}
	
	/**
	 * calcula el vector de direccion de movimiento
	 * @return- vector direccion de movimiento
	 */
	public double[][] calculoDireccionMovimiento()
	{
		double[][]filaDeN=null;
		
		switch (opcion)
		{
			case MAYOR:
				filaDeN=darFilaNoBasicaDireccionCostoReducidoMayor();
				break;
			case MENOR:
				filaDeN=darFilaNoBasicaDireccionCostoReducidoMenor();
				break;
			case PRIMERO:
				filaDeN=darFilaNoBasicaDireccionCostoReducidoPrimero();
				break;
			case ULTIMO:
				filaDeN=darFilaNoBasicaDireccionCostoReducidoUltimo();
				break;
		}
		
		double[][] respuesta=new double[BInversa[0].length + n[0].length][1];

		double[][]matriz=OperacionMatrices.multiplicar(BInversa, filaDeN);
		
		for(int i =0;i<matriz.length;i++)
		{
			respuesta[i][0]=-1*matriz[i][0];
		}
		int c=0;
		for(int i =matriz.length;i<(BInversa[0].length + n[0].length);i++)
		{
			if(c==posN)
			{
				respuesta[i][0]=1;
			}
			else
			{
				respuesta[i][0]=0;
			}
			c++;
		}

		return respuesta;
	}
	
	/**
	 * calcula la longitud de movimiento
	 * @return. longitud de movimiento
	 */
	public double longitudDeMovimiento()
	{
		List<Double>numeros=new ArrayList<Double>();
		double[][]x=OperacionMatrices.multiplicar(BInversa, vectorB);
		double[][]direccion= calculoDireccionMovimiento();
		List<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<x.length;i++)
		{
			
			if(direccion[i][0]<0)
			{
				l.add(i);
				numeros.add((x[i][0])/(-1*direccion[i][0]));
			}
		}
		
		double min=Collections.min(numeros);
		for(int i =0;i<numeros.size();i++)
		{
			if(min==numeros.get(i))
			{
				posB=l.get(i);
			}
		}
		return min;
		
	}
	
	/**
	 * calcula la actualizacion del vector de las variables x
	 * @return-vector de x actualizado para la siguiente iteracion
	 */
	public double[][] actualizacion()
	{
		
		Xactualizada=OperacionMatrices.sumarMatrices1ColumnaConLongitudDeMovimiento(Xactual, calculoDireccionMovimiento(), longitudDeMovimiento());
		return Xactualizada;
	}
}
