package org.gvsig.topology.swing.impl;

import com.jeta.open.i18n.I18NUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class CreateRuleDialogView extends JPanel
{
   JTextField txtTolerance = new JTextField();
   JComboBox cboDataSet1 = new JComboBox();
   JLabel lblDataSet1 = new JLabel();
   JLabel lblRule = new JLabel();
   JComboBox cboRule = new JComboBox();
   JLabel lblDataSet2 = new JLabel();
   JComboBox cboDataSet2 = new JComboBox();
   JLabel lblTolerance = new JLabel();
   JEditorPane txtDescription = new JEditorPane();

   /**
    * Default constructor
    */
   public CreateRuleDialogView()
   {
      initializePanel();
   }

   /**
    * Adds fill components to empty cells in the first row and first column of the grid.
    * This ensures that the grid spacing will be the same as shown in the designer.
    * @param cols an array of column indices in the first row where fill components should be added.
    * @param rows an array of row indices in the first column where fill components should be added.
    */
   void addFillComponents( Container panel, int[] cols, int[] rows )
   {
      Dimension filler = new Dimension(10,10);

      boolean filled_cell_11 = false;
      CellConstraints cc = new CellConstraints();
      if ( cols.length > 0 && rows.length > 0 )
      {
         if ( cols[0] == 1 && rows[0] == 1 )
         {
            /** add a rigid area  */
            panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
            filled_cell_11 = true;
         }
      }

      for( int index = 0; index < cols.length; index++ )
      {
         if ( cols[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
      }

      for( int index = 0; index < rows.length; index++ )
      {
         if ( rows[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(1,rows[index]) );
      }

   }

   /**
    * Helper method to load an image file from the CLASSPATH
    * @param imageName the package and name of the file to load relative to the CLASSPATH
    * @return an ImageIcon instance with the specified image file
    * @throws IllegalArgumentException if the image resource cannot be loaded.
    */
   public ImageIcon loadImage( String imageName )
   {
      try
      {
         ClassLoader classloader = getClass().getClassLoader();
         java.net.URL url = classloader.getResource( imageName );
         if ( url != null )
         {
            ImageIcon icon = new ImageIcon( url );
            return icon;
         }
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      throw new IllegalArgumentException( "Unable to load image: " + imageName );
   }

   /**
    * Method for recalculating the component orientation for 
    * right-to-left Locales.
    * @param orientation the component orientation to be applied
    */
   public void applyComponentOrientation( ComponentOrientation orientation )
   {
      // Not yet implemented...
      // I18NUtils.applyComponentOrientation(this, orientation);
      super.applyComponentOrientation(orientation);
   }

   public JPanel createPanel()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.5),FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.5),FILL:4DLU:NONE","CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      jpanel1.add(createPanel1(),cc.xy(2,2));
      jpanel1.add(createPanel2(),cc.xy(4,2));
      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5 },new int[]{ 1,2,3 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      txtTolerance.setName("txtTolerance");
      txtTolerance.setHorizontalAlignment(JTextField.RIGHT);
      jpanel1.add(txtTolerance,cc.xy(1,15));

      cboDataSet1.setName("cboDataSet1");
      jpanel1.add(cboDataSet1,cc.xy(1,3));

      lblDataSet1.setName("lblDataSet1");
      lblDataSet1.setText("_Primary_dataset");
      jpanel1.add(lblDataSet1,cc.xy(1,1));

      lblRule.setName("lblRule");
      lblRule.setText("_Rule");
      jpanel1.add(lblRule,cc.xy(1,5));

      cboRule.setEnabled(false);
      cboRule.setName("cboRule");
      jpanel1.add(cboRule,cc.xy(1,7));

      lblDataSet2.setName("lblDataSet2");
      lblDataSet2.setText("_Secondary_dataset");
      jpanel1.add(lblDataSet2,cc.xy(1,9));

      cboDataSet2.setEnabled(false);
      cboDataSet2.setName("cboDataSet2");
      jpanel1.add(cboDataSet2,cc.xy(1,11));

      lblTolerance.setName("lblTolerance");
      lblTolerance.setText("_Tolerance");
      jpanel1.add(lblTolerance,cc.xy(1,13));

      addFillComponents(jpanel1,new int[0],new int[]{ 2,4,6,8,10,12,14 });
      return jpanel1;
   }

   public JPanel createPanel2()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(0.7)","FILL:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      txtDescription.setName("txtDescription");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(txtDescription);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(1,1));

      addFillComponents(jpanel1,new int[0],new int[0]);
      return jpanel1;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createPanel(), BorderLayout.CENTER);
   }


}
