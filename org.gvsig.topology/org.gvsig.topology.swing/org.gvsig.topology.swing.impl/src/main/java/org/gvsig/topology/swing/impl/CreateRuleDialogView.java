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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CreateRuleDialogView extends JPanel
{
   JLabel lblDataSet2 = new JLabel();
   JLabel lblDataSet1 = new JLabel();
   JLabel lblRule = new JLabel();
   JComboBox cboDataSet1 = new JComboBox();
   JComboBox cboRule = new JComboBox();
   JComboBox cboDataSet2 = new JComboBox();
   JLabel lblImage = new JLabel();
   JLabel lblDescription = new JLabel();
   JLabel lblLabelDescription = new JLabel();
   JLabel lblTolerance = new JLabel();
   JTextField txtTolerance = new JTextField();

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
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.7),FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.1),FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.2),FILL:4DLU:NONE","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lblDataSet2.setName("lblDataSet2");
      lblDataSet2.setText("_Secondary_dataset");
      jpanel1.add(lblDataSet2,cc.xy(2,6));

      lblDataSet1.setName("lblDataSet1");
      lblDataSet1.setText("_Primary_dataset");
      jpanel1.add(lblDataSet1,cc.xy(2,2));

      lblRule.setName("lblRule");
      lblRule.setText("_Rule");
      jpanel1.add(lblRule,cc.xy(2,4));

      cboDataSet1.setName("cboDataSet1");
      jpanel1.add(cboDataSet1,cc.xy(4,2));

      cboRule.setEnabled(false);
      cboRule.setName("cboRule");
      jpanel1.add(cboRule,cc.xy(4,4));

      cboDataSet2.setEnabled(false);
      cboDataSet2.setName("cboDataSet2");
      jpanel1.add(cboDataSet2,cc.xy(4,6));

      lblImage.setName("lblImage");
      jpanel1.add(lblImage,cc.xywh(6,4,1,6));

      lblDescription.setName("lblDescription");
      jpanel1.add(lblDescription,cc.xywh(8,4,1,6));

      lblLabelDescription.setName("lblLabelDescription");
      lblLabelDescription.setText("_Rule_description");
      jpanel1.add(lblLabelDescription,cc.xywh(6,2,3,1));

      lblTolerance.setName("lblTolerance");
      lblTolerance.setText("_Tolerance");
      jpanel1.add(lblTolerance,cc.xy(2,8));

      txtTolerance.setName("txtTolerance");
      txtTolerance.setHorizontalAlignment(JTextField.RIGHT);
      jpanel1.add(txtTolerance,cc.xy(4,8));

      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5,6,7,8,9 },new int[]{ 1,2,3,4,5,6,7,8,9 });
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
