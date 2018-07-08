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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class DefaultJTopologyPlanPropertiesView extends JPanel
{
   JLabel lblName = new JLabel();
   JTextField txtName = new JTextField();
   JTabbedPane tabPanel = new JTabbedPane();
   JList lstDataSets = new JList();
   JButton btnAddDataSet = new JButton();
   JButton btnRemoveDataSet = new JButton();
   JList lstRules = new JList();
   JButton btnAddRule = new JButton();
   JButton btnRemoveRule = new JButton();
   JLabel lblTolerance = new JLabel();
   JTextField txtTolerance = new JTextField();

   /**
    * Default constructor
    */
   public DefaultJTopologyPlanPropertiesView()
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
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lblName.setName("lblName");
      lblName.setText("_Name");
      jpanel1.add(lblName,cc.xy(2,2));

      txtName.setName("txtName");
      jpanel1.add(txtName,cc.xy(4,2));

      tabPanel.setName("tabPanel");
      tabPanel.addTab("_DataSets",null,createPanel1());
      tabPanel.addTab("_Rules",null,createPanel3());
      jpanel1.add(tabPanel,cc.xywh(2,6,3,1));

      lblTolerance.setName("lblTolerance");
      lblTolerance.setText("_Tolerance");
      jpanel1.add(lblTolerance,cc.xy(2,4));

      txtTolerance.setName("txtTolerance");
      txtTolerance.setHorizontalAlignment(JTextField.RIGHT);
      jpanel1.add(txtTolerance,cc.xy(4,4));

      addFillComponents(jpanel1,new int[]{ 1,2,3,4,5 },new int[]{ 1,2,3,4,5,6,7 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lstDataSets.setName("lstDataSets");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(lstDataSets);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(2,2));

      jpanel1.add(createPanel2(),cc.xy(2,4));
      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5 });
      return jpanel1;
   }

   public JPanel createPanel2()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:8DLU:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      btnAddDataSet.setIcon(loadImage("org/gvsig/topology/swing/impl/images/dataset_add.png"));
      btnAddDataSet.setName("btnAddDataSet");
      btnAddDataSet.setToolTipText("_Add_dataSet");
      EmptyBorder emptyborder1 = new EmptyBorder(2,2,2,2);
      btnAddDataSet.setBorder(emptyborder1);
      jpanel1.add(btnAddDataSet,cc.xy(6,1));

      btnRemoveDataSet.setIcon(loadImage("org/gvsig/topology/swing/impl/images/dataset_remove.png"));
      btnRemoveDataSet.setName("btnRemoveDataSet");
      btnRemoveDataSet.setToolTipText("_Remove_dataSet");
      EmptyBorder emptyborder2 = new EmptyBorder(2,2,2,2);
      btnRemoveDataSet.setBorder(emptyborder2);
      jpanel1.add(btnRemoveDataSet,cc.xy(4,1));

      addFillComponents(jpanel1,new int[]{ 1,2,3,5 },new int[]{ 1 });
      return jpanel1;
   }

   public JPanel createPanel3()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lstRules.setName("lstRules");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(lstRules);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(2,2));

      jpanel1.add(createPanel4(),cc.xy(2,4));
      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5 });
      return jpanel1;
   }

   public JPanel createPanel4()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      btnAddRule.setIcon(loadImage("org/gvsig/topology/swing/impl/images/rule_add.png"));
      btnAddRule.setName("btnAddRule");
      btnAddRule.setToolTipText("_Add_rule");
      EmptyBorder emptyborder1 = new EmptyBorder(2,2,2,2);
      btnAddRule.setBorder(emptyborder1);
      jpanel1.add(btnAddRule,cc.xy(5,1));

      btnRemoveRule.setIcon(loadImage("org/gvsig/topology/swing/impl/images/rule_remove.png"));
      btnRemoveRule.setName("btnRemoveRule");
      btnRemoveRule.setToolTipText("_Remove_rule");
      EmptyBorder emptyborder2 = new EmptyBorder(2,2,2,2);
      btnRemoveRule.setBorder(emptyborder2);
      jpanel1.add(btnRemoveRule,cc.xy(3,1));

      addFillComponents(jpanel1,new int[]{ 1,2,4 },new int[]{ 1 });
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
