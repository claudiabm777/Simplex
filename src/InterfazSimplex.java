import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.*;


public class InterfazSimplex extends JFrame implements ActionListener
{
	//AUTORES: Claudia Bedoya-Daniel Ramirez
	/**
	 * boton de ejecucion
	 */
	private JButton botonEjecutar=new JButton("Ejecutar");
	
	/**
	 * validador de características del problema
	 */
	private Validacion validador;
	
	/**
	 * atributo que indica que estilo de documento se va a manejar en el textPane
	 */
	private DefaultStyledDocument documentoResultados=new DefaultStyledDocument();
	
	/**
	 * campo donde se muestran los resultados de las iteraciones
	 */
	private JTextPane textoResultados=new JTextPane(documentoResultados);
	
	/**
	 * representa donde se escogerá la forma en que se calcularan los cotos reducidos
	 */
	private JComboBox comboOpciones=new JComboBox(TipoCostosReducidos.values());
	
	/**
	 * representa la opcion de maximizar
	 */
	private JRadioButton radioMaximizar=new JRadioButton("Maximizar",true);
	
	/**
	 * representa la opcion de minimizar
	 */
	private JRadioButton radioMinimizar=new JRadioButton("Minimizar",false);
	/**
	 * matriz A
	 */
	private double AN[][]={
			{2,2,2,1,1,0,0,0,0},
			{0.4,0.7,0.8,0.3,0,1,0,0,0},
			{0,-1,1,0,0,0,1,0,0},
			{1,0,0,-1,0,0,0,1,0},
			{1,1,1,1,0,0,0,0,-1}};
	
	/**
	 * vector lado derecho
	 */
	private double vectorBN[][]={{60},{50},{10},{4},{35}};
	
	/**
	 * vector costos
	 */
	private double costos[]={6.0,9.0,8.0,5.0,0.0,0.0,0.0,0.0,0.0};
	
	/**
	 * vector subindices basicos
	 */
	private double IB[]={0,0,0,0,0};
	
	/**
	 * vector de subindices no basicos
	 */
	private double IN[]={0,0,0,0};
	
	/**
	 * panel de costos (representado por medio de una tabla)
	 */
	private PanelModificarMatriz panelC;
	
	/**
	 * panel de subindices basics (representado por medio de una tabla)
	 */
	private PanelModificarMatriz panelIB;
	
	/**
	 * panel de subindices no basicos (representado por medio de una tabla)
	 */
	private PanelModificarMatriz panelIN;
	
	/**
	 * panel de A (representado por medio de una tabla)
	 */
	private PanelModificarMatriz panelA;
	
	/**
	 * panel de vector lado derecho (representado por medio de una tabla)
	 */
	private PanelModificarMatriz panelBN;
	
	/**
	 * constructor del frame
	 */
	public InterfazSimplex()
	{
		super("Simplex");
		textoResultados.setEditable(false);
		textoResultados.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));
		validador=new Validacion();
		JPanel panelTextoResultados=new JPanel(new BorderLayout());
		panelTextoResultados.add(textoResultados,BorderLayout.CENTER);
		
		JScrollPane scrollTextoResultados=new JScrollPane(panelTextoResultados);
		scrollTextoResultados.setPreferredSize(new Dimension(800,300));

		JPanel panelOpciones=new JPanel(new BorderLayout());
		panelOpciones.add(new JLabel("Opción: "),BorderLayout.WEST);
		panelOpciones.add(comboOpciones,BorderLayout.CENTER);

		ButtonGroup grupoRadiosTipoOptimizacion=new ButtonGroup();
		grupoRadiosTipoOptimizacion.add(radioMaximizar);
		grupoRadiosTipoOptimizacion.add(radioMinimizar);
		
		JPanel panelTipoOptimizacion=new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTipoOptimizacion.add(new JLabel("Tipo de optimización: "));
		panelTipoOptimizacion.add(radioMaximizar);
		panelTipoOptimizacion.add(radioMinimizar);

		JPanel panelFormulario=new JPanel();
		panelFormulario.setLayout(new BoxLayout(panelFormulario,BoxLayout.Y_AXIS));
		panelFormulario.add(panelOpciones);
		panelFormulario.add(panelTipoOptimizacion);

		panelC=new PanelModificarMatriz("Vector C:",new double[][]{costos},false);
		panelIB=new PanelModificarMatriz("Vector IB:",new double[][]{IB},true);
		panelIN=new PanelModificarMatriz("Vector IN:",new double[][]{IN},true);
		panelA=new PanelModificarMatriz("Matriz A:",AN,false);
		panelBN=new PanelModificarMatriz("Vector b:",vectorBN,false);
		
		JPanel panelDatosInterno=new JPanel(new BorderLayout());
		panelDatosInterno.add(panelC,BorderLayout.NORTH);
		panelDatosInterno.add(panelIB,BorderLayout.CENTER);
		panelDatosInterno.add(panelIN,BorderLayout.EAST);

		JPanel panelDatos=new JPanel(new BorderLayout());
		panelDatos.add(panelFormulario,BorderLayout.NORTH);
		panelDatos.add(panelA,BorderLayout.CENTER);
		panelDatos.add(panelBN,BorderLayout.EAST);
		panelDatos.add(panelDatosInterno,BorderLayout.SOUTH);

		JPanel panelResultados=new JPanel(new BorderLayout());
		panelResultados.add(new JLabel("<html><b>RESULTADOS:</b></html>"),BorderLayout.NORTH);
		panelResultados.add(scrollTextoResultados,BorderLayout.CENTER);
		panelResultados.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(botonEjecutar,BorderLayout.NORTH);
		panel.add(panelDatos,BorderLayout.CENTER);
		panel.add(panelResultados,BorderLayout.SOUTH);
		
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		botonEjecutar.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evento)
	{
		botonEjecutar.setEnabled(false);
		try
		{
			panelC.editor.stopCellEditing();
			panelIB.editor.stopCellEditing();
			panelIN.editor.stopCellEditing();
			panelA.editor.stopCellEditing();
			panelBN.editor.stopCellEditing();

			ejecutar();
		}
		catch (Throwable th)
		{
			if (!(th instanceof IllegalArgumentException)&&!(th instanceof IllegalStateException))
			{
				th.printStackTrace();
			}
			JOptionPane.showMessageDialog(this,th.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			botonEjecutar.setEnabled(true);
		}
	}

	/**
	 * ejecuta cuando se oprime el boton
	 */
	public void ejecutar()
	{
		textoResultados.setText("");
		List<Double> ncostos=new ArrayList<Double>();
		for (int i=0; i<costos.length; i++)
		{
			ncostos.add(costos[i]);
		}
		List<Integer> INN=new ArrayList<Integer>();
		for (int i=0; i<IN.length; i++)
		{
    		int valor=(int)Math.round(IN[i]);
			if (valor!=0) INN.add(valor);
		}
		List<Integer> IBN=new ArrayList<Integer>();
		for (int i=0; i<IB.length; i++)
		{
    		int valor=(int)Math.round(IB[i]);
			if (valor!=0) IBN.add(valor);
		}
		if (INN.isEmpty()) INN=null;
		if (IBN.isEmpty()) IBN=null;
		
		TipoCostosReducidos opcion=(TipoCostosReducidos)(comboOpciones.getSelectedItem());
		if (opcion==null) throw new IllegalStateException("Debe seleccionar una opción");
		boolean minmax=radioMaximizar.isSelected();
		NotificadorResultados notificadorResultados=new ImplementacionNotificadorResultados();
		
		while(validador.esNegativoTerminoVectorLadoDerecho(vectorBN)>=0)
		{
			
			cambiarLadoDerecho(validador.esNegativoTerminoVectorLadoDerecho(vectorBN));
			
			
		}
		Simplex simplex=new Simplex(AN, INN, IBN, vectorBN, ncostos, minmax, opcion, notificadorResultados);
		simplex.iteracionesSimplex();
	}
	
	/**
	 * multiplica por -1 la fila p cuando el vector del lado derecho es negativo
	 * @param p
	 */
	public void cambiarLadoDerecho(int p)
	{
		for(int i=0;i<AN[0].length;i++)
		{
			AN[p][i]=AN[p][i]*-1;
			System.out.println(" nuevo valor A: "+AN[p][i]);
		}
		vectorBN[p][0]=-1*vectorBN[p][0];
		System.out.println(" nuevo valor b: "+vectorBN[p][0]);
	}
	
	//clase para realizar las notificaciones a las tablas y actualizar correctamente los datos
	private class ImplementacionNotificadorResultados implements NotificadorResultados
	{

		private void insertarTexto(String texto, AttributeSet atributos)
		{
			try
			{
				documentoResultados.insertString(documentoResultados.getLength(),texto,atributos);
			}
			catch (Throwable th) {
			}
		}

		private void insertarVariable(String nombre, List<String> valor)
		{
			String textoNombre=nombre+" = ";
			char indentacion[]=new char[textoNombre.length()];
			Arrays.fill(indentacion,' ');
			for (int i=0; i<valor.size(); i++)
			{
				if (i==0)
				{
					insertarTexto(textoNombre,null);
				}
				else
				{
					insertarTexto(new String(indentacion),null);
				}
				insertarTexto(valor.get(i),null);
				insertarTexto("\r\n",null);
			}
			insertarTexto("\r\n",null);
		}

		@Override
		public void notificarMensaje(String mensaje)
		{
			SimpleAttributeSet atributos=new SimpleAttributeSet();
			atributos.addAttribute(StyleConstants.CharacterConstants.Background,Color.GREEN);
			atributos.addAttribute(StyleConstants.CharacterConstants.Foreground,Color.BLACK);
			atributos.addAttribute(StyleConstants.CharacterConstants.Bold,Boolean.TRUE);
			insertarTexto(mensaje+"\r\n",atributos);
		}

		@Override
		public void notificarTitulo(String mensaje)
		{
			SimpleAttributeSet atributos=new SimpleAttributeSet();
			atributos.addAttribute(StyleConstants.CharacterConstants.Background,Color.YELLOW);
			atributos.addAttribute(StyleConstants.CharacterConstants.Foreground,Color.RED);
			atributos.addAttribute(StyleConstants.CharacterConstants.Bold,Boolean.TRUE);
			insertarTexto(mensaje+"\r\n",atributos);			
		}

		@Override
		public void notificarValorVariable(String nombre, double[][] matriz)
		{
			insertarVariable(nombre, OperacionMatrices.imprimirMatriz(matriz));
		}

		@Override
		public void notificarValorVariable(String nombre, Double[] arreglo)
		{
			insertarVariable(nombre, Arrays.asList(OperacionMatrices.imprimirArreglo(arreglo)));
		}

		@Override
		public void notificarValorVariable(String nombre, Integer[] arreglo)
		{
			insertarVariable(nombre, Arrays.asList(OperacionMatrices.imprimirArreglo(arreglo)));
		}

		@Override
		public void notificarValorVariable(String nombre, Object objeto)
		{
			insertarVariable(nombre, Arrays.asList(objeto.toString()));
		}
		
	}
	
	public static void main(String[] args)
	{
		InterfazSimplex interfaz=new InterfazSimplex();
		interfaz.setVisible(true);
	}
	
}
