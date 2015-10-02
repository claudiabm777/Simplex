import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class PanelModificarMatriz extends JPanel
{
	
	private static DecimalFormat FORMATO=new DecimalFormat("0.########");
	
	/**
	 * tabla de la interfas para ingresar datos
	 */
	public JTable tabla=new JTable();
	
	/**
	 * matriz de la tabla
	 */
	public double[][] matriz;
	
	/**
	 * indica si en esa tabla se reciben solo enteros o no
	 */
	public boolean soloEnteros;
	
	/**
	 * editor de los datos de la tabla
	 */
	public DefaultCellEditor editor;
	
	/**
	 * constructor de la clase
	 * @param titulo.titulo de la tabla
	 * @param nmatriz- matriz que identifica la tabla (sea A, b, IN etc)
	 * @param nSoloEnteros-si solo recibe enteros
	 */
	public PanelModificarMatriz(String titulo, double[][] nmatriz, boolean nSoloEnteros)
	{
		super(new BorderLayout());
		matriz=nmatriz;
		soloEnteros=nSoloEnteros;
		tabla.setModel(new ModeloTabla());
		tabla.setRowSorter(null);
		tabla.setRowSelectionAllowed(false);
		tabla.setColumnSelectionAllowed(false);
		if (soloEnteros)
		{
			editor=new DefaultCellEditor(new JComboBox(new String[]{"borrar","1","2","3","4","5","6","7","8","9"}));
		}
		else
		{
			editor=new DefaultCellEditor(new JTextField());
			editor.setClickCountToStart(1);
		}
		tabla.setDefaultEditor(String.class,editor);
		add(tabla,BorderLayout.CENTER);
		setBorder(BorderFactory.createTitledBorder(titulo));
	}
	
	//clase privada quedefine el modelo que va a tener la tabla a crear dependiendo de la matriz que se desee representar
	private class ModeloTabla extends DefaultTableModel
	{
		
		public ModeloTabla()
		{
		}
		
	    public int getRowCount()
	    {
	    	return matriz.length;
	    }


	    public int getColumnCount()
	    {
	    	return matriz[0].length;
	    }
	    
	    public String getColumnName(int columnIndex)
	    {
	    	return "";
	    }

	    public Class<?> getColumnClass(int columnIndex)
	    {
	    	return String.class;
	    }
	    
	    public boolean isCellEditable(int rowIndex, int columnIndex)
	    {
	    	return true;
	    }

	    public Object getValueAt(int rowIndex, int columnIndex)
	    {
	    	if (soloEnteros)
	    	{
	    		int valor=(int)Math.round(matriz[rowIndex][columnIndex]);
	    		if (valor==0)
	    		{
	    			return "";
	    		}
	    		else
	    		{
	    			return ""+valor;
	    		}
	    	}
	    	else
	    	{
	    		return FORMATO.format(matriz[rowIndex][columnIndex]).replace(',','.');
	    	}
	    }

	    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	    {
	    	try
	    	{
	    		if (soloEnteros)
	    		{
		    		matriz[rowIndex][columnIndex]=Integer.parseInt(aValue.toString());
	    		}
	    		else 
	    		{
		    		matriz[rowIndex][columnIndex]=Double.parseDouble(aValue.toString().replace(',','.'));
	    		}
	    		fireTableCellUpdated(rowIndex, columnIndex);
	    	}
	    	catch (Throwable th)
	    	{
	    		if (soloEnteros)
	    		{
		    		matriz[rowIndex][columnIndex]=0;
		    		fireTableCellUpdated(rowIndex, columnIndex);
	    		}
	    	}
	    }

		
	}
	
}
