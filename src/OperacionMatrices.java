import java.util.*;
//Parte del codigo de esta clase fue tomado de fuentes externas ya que no se logró conseguir una librería
//que operara las matrices, y estos son algoritmos complejos que no entran en lo que se enceña en este curso (invertir y multiplicar matrices.
//los otros  métodos si fueron desarrollados por nosotros
public class OperacionMatrices {

 public OperacionMatrices()
 {
	 
 }
/**
 * devuelve una lista de strings para poder imprimir matriz recibida por parámetro
 * @param matriz a imprimir
 * @return
 */
 public static List<String> imprimirMatriz(double[][] matriz) {
	 List<String> respuesta=new ArrayList<String>();
	 for (double[] fila:matriz)
	 {
		String cadena="";
		for (int j=0; j<fila.length; j++)
		{
			 if (j>0) cadena+=" ";
			 cadena+=fila[j]<0?"-":"+";
			 cadena+=String.format("%.8f",Math.abs(fila[j])).replace(',','.');
		}
		respuesta.add(cadena);
	 }
     return respuesta;
   }

 /**
  * devuelve un strings para poder imprimir lo que tiene este arreglo de doubles recibido
  * @param fila
  * @return string con el arreglo
  */
  public static String imprimirArreglo(Double[] fila) {
	  String cadena="";
	  for (int j=0; j<fila.length; j++)
	  {
		 if (j>0) cadena+=" ";
		 cadena+=fila[j]<0?"-":"+";
		 cadena+=String.format("%.8f",Math.abs(fila[j])).replace(',','.');
	  }
	  return cadena;
  }
  
  /**
   * devuelve un strings para poder imprimir lo que tiene este arreglo de enteros recibido
   * @param fila
   * @return string con el arreglo
   */
  public static String imprimirArreglo(Integer[] fila) {
	  String cadena="";
	  for (int j=0; j<fila.length; j++)
	  {
		 if (j>0) cadena+=" ";
		 cadena+=fila[j];
	  }
	  return cadena;
  }


  // Autor: Alejandro Sotelo Arevalo
  // Tomado de la ancheta de algoritmos del equipo Math-O-matic
  /**
   * invierte la matriz recibida por parámetro y devuelve la inversa
   */
  public static double[][] gaussJordan(double[][] matriz)
  {
    if (matriz.length==0) throw new IllegalArgumentException("La matriz no puede ser vacía.");
    if (matriz.length!=matriz[0].length) throw new IllegalArgumentException("La matriz debe ser cuadrada.");
    int n=matriz.length,m=n*2;
    double epsilon=1e-12;
    double[][] extendida=new double[n][];
    for (int i=0; i<n; i++) 
    {
      extendida[i]=Arrays.copyOf(matriz[i],m);
      extendida[i][i+n]=1.0;
    }
    for (int i=0; i<n; i++)
    {
      int w=i;
      for (int u=i+1; u<n; u++) 
      {
        if (Math.abs(extendida[u][i])>Math.abs(extendida[w][i])) 
        {
          w=u;
        }
      }
      if (Math.abs(extendida[w][i])<epsilon)
      {
        return null; // La matriz no es invertible
      }
      if (w!=i) 
      {
        double[] tmp=extendida[w];
        extendida[w]=extendida[i];
        extendida[i]=tmp;
      }
      double z=extendida[i][i];
      for (int v=i; v<m; v++) 
      {
        extendida[i][v]/=z;
      }
      for (int u=0; u<n; u++)
      {
        z=extendida[u][i];
        if (u!=i&&Math.abs(z)>=epsilon) 
        {
          for (int v=i; v<m; v++)
          {
            extendida[u][v]-=z*extendida[i][v];
          }
        }
      }
    }
    for (int i=0; i<n; i++)
    {
      extendida[i]=Arrays.copyOfRange(extendida[i],n,m);
    }
    return extendida;
  }

  // Autor: Alejandro Sotelo Arevalo
  /**
   * multiplica las matrices recibidas por parámetro y devuelve el resultado
   */
  public static double[][] multiplicar(double[][] matriz1, double[][] matriz2)
  {
    if (matriz1.length==0) throw new IllegalArgumentException("La primera matriz no puede ser vacía.");
    if (matriz2.length==0) throw new IllegalArgumentException("La segunda matriz no puede ser vacía.");
    if (matriz1[0].length!=matriz2.length) throw new IllegalArgumentException("El número de filas de la primera matriz debe ser igual al número de columnas de la segunda.");
    int a=matriz1.length,b=matriz2.length,c=matriz2[0].length;
    double[][] respuesta=new double[a][c];
    for (int i=0; i<a; i++)
    {
      for (int k=0; k<c; k++) 
      {
        for (int j=0; j<b; j++) 
        {
          respuesta[i][k]+=matriz1[i][j]*matriz2[j][k];
        }
      }
    }
    return respuesta;
  }

  /**
   * resta las matrices recibidas por parámetro, que solo cuentan con una fila
   * @param matriz1
   * @param matriz2
   * @return una matriz con el resultado de la resta
   */
  public static double[][] restarMatrices1Fila(double[][] matriz1, double[][] matriz2)
  {
	  if (matriz1.length==0) throw new IllegalArgumentException("La primera matriz no puede ser vacía.");
	  if (matriz2.length==0) throw new IllegalArgumentException("La segunda matriz no puede ser vacía.");
	  if (matriz1.length!=matriz2.length) throw new IllegalArgumentException("El número de filas de la primera matriz debe ser igual al número de filas de la segunda.");
	  if (matriz1[0].length!=matriz2[0].length) throw new IllegalArgumentException("El número de columnas de la primera matriz debe ser igual al número de columnas de la segunda.");
	  double[][] respuesta=new double[1][matriz1[0].length];
	  for(int i=0;i<matriz1[0].length;i++)
	  {
		  respuesta[0][i]=matriz1[0][i]-matriz2[0][i];
	  }
	  return respuesta;
  }
  
  /**
   * Multiplica la segunda matriz recibida por parametro por el double recivido por parámetro y luego suma ambas matrices, estas solo tienen 1 columna
   * @param matriz1
   * @param matriz2
   * @param longitudM
   * @return una matriz con la respuesta de estas operaciones
   */
  public static double[][] sumarMatrices1ColumnaConLongitudDeMovimiento(double[][] matriz1, double[][] matriz2,double longitudM)
  {
	  if (matriz1.length==0) throw new IllegalArgumentException("La primera matriz no puede ser vacía.");
	  if (matriz2.length==0) throw new IllegalArgumentException("La segunda matriz no puede ser vacía.");
	  if (matriz1.length!=matriz2.length) throw new IllegalArgumentException("El número de filas de la primera matriz debe ser igual al número de filas de la segunda.");
	  if (matriz1[0].length!=matriz2[0].length) throw new IllegalArgumentException("El número de columnas de la primera matriz debe ser igual al número de columnas de la segunda.");
	  double[][] respuesta=new double[matriz1.length][1];
	  for(int i=0;i<matriz1.length;i++)
	  {
		  respuesta[i][0]=matriz1[i][0]+(longitudM*(matriz2[i][0]));
	  }
	  return respuesta;
  }
}

