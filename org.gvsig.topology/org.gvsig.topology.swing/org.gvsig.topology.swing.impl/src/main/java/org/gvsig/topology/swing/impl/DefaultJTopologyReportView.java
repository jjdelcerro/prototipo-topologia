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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;


public class DefaultJTopologyReportView extends JPanel
{
   JLabel lblShow = new JLabel();
   JComboBox jcombobox1 = new JComboBox();
   JToggleButton btnShowErrors = new JToggleButton();
   JToggleButton jtogglebutton1 = new JToggleButton();
   JToggleButton jtogglebutton2 = new JToggleButton();
   JButton btnRemoveDataSet = new JButton();
   JButton btnRemoveDataSet1 = new JButton();
   JButton btnRemoveDataSet2 = new JButton();
   JButton btnRemoveDataSet3 = new JButton();
   JTable jtable1 = new JTable();

   /**
    * Default constructor
    */
   public DefaultJTopologyReportView()
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
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      jpanel1.add(createPanel1(),cc.xy(2,2));
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(jtable1);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(2,4));

      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lblShow.setName("lblShow");
      lblShow.setText("_Show");
      jpanel1.add(lblShow,cc.xy(1,1));

      jpanel1.add(jcombobox1,cc.xy(3,1));

      btnShowErrors.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_errors.png"));
      btnShowErrors.setName("btnShowErrors");
      btnShowErrors.setSelected(true);
      btnShowErrors.setToolTipText("_Show_errors");
      EmptyBorder emptyborder1 = new EmptyBorder(2,2,2,2);
      btnShowErrors.setBorder(emptyborder1);
      jpanel1.add(btnShowErrors,cc.xy(5,1));

      jtogglebutton1.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_exceptions.png"));
      jtogglebutton1.setToolTipText("_Show_exceptions");
      EmptyBorder emptyborder2 = new EmptyBorder(2,2,2,2);
      jtogglebutton1.setBorder(emptyborder2);
      jpanel1.add(jtogglebutton1,cc.xy(7,1));

      jtogglebutton2.setIcon(loadImage("org/gvsig/topology/swing/impl/images/visible_extent.png"));
      jtogglebutton2.setToolTipText("_Visible_extent_only");
      EmptyBorder emptyborder3 = new EmptyBorder(2,2,2,2);
      jtogglebutton2.setBorder(emptyborder3);
      jpanel1.add(jtogglebutton2,cc.xy(9,1));

      btnRemoveDataSet.setIcon(loadImage("org/gvsig/topology/swing/impl/images/zoom.png"));
      btnRemoveDataSet.setName("btnRemoveDataSet");
      btnRemoveDataSet.setToolTipText("_Zoom");
      EmptyBorder emptyborder4 = new EmptyBorder(2,2,2,2);
      btnRemoveDataSet.setBorder(emptyborder4);
      jpanel1.add(btnRemoveDataSet,cc.xy(11,1));

      btnRemoveDataSet1.setIcon(loadImage("org/gvsig/topology/swing/impl/images/center_view.png"));
      btnRemoveDataSet1.setName("btnRemoveDataSet");
      btnRemoveDataSet1.setToolTipText("_Center");
      EmptyBorder emptyborder5 = new EmptyBorder(2,2,2,2);
      btnRemoveDataSet1.setBorder(emptyborder5);
      jpanel1.add(btnRemoveDataSet1,cc.xy(13,1));

      btnRemoveDataSet2.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_actions.png"));
      btnRemoveDataSet2.setName("btnRemoveDataSet");
      btnRemoveDataSet2.setToolTipText("_Actions");
      EmptyBorder emptyborder6 = new EmptyBorder(2,2,2,2);
      btnRemoveDataSet2.setBorder(emptyborder6);
      jpanel1.add(btnRemoveDataSet2,cc.xy(15,1));

      btnRemoveDataSet3.setIcon(loadImage("org/gvsig/topology/swing/impl/images/refresh.png"));
      btnRemoveDataSet3.setName("btnRemoveDataSet");
      btnRemoveDataSet3.setToolTipText("_refresh");
      EmptyBorder emptyborder7 = new EmptyBorder(2,2,2,2);
      btnRemoveDataSet3.setBorder(emptyborder7);
      jpanel1.add(btnRemoveDataSet3,cc.xy(17,1));

      addFillComponents(jpanel1,new int[]{ 2,4,6,8,10,12,14,16 },new int[0]);
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
