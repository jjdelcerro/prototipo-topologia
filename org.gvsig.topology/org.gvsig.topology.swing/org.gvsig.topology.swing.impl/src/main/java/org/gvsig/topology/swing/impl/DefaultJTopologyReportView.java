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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;


public class DefaultJTopologyReportView extends JPanel
{
   JTabbedPane tabData = new JTabbedPane();
   JTable tblErrors = new JTable();
   JLabel lblShow = new JLabel();
   JComboBox cboRules = new JComboBox();
   JToggleButton btnVisibleExtentOnly = new JToggleButton();
   JButton btnZoom = new JButton();
   JButton btnCenter = new JButton();
   JButton btnActions = new JButton();
   JButton btnRefresh = new JButton();
   JToggleButton btnShowErrors = new JToggleButton();
   JToggleButton btnShowExceptions = new JToggleButton();
   JPanel pnlParameters = new JPanel();
   JButton btnParametersCancel = new JButton();
   JButton btnParametersAccept = new JButton();
   JLabel lblActionDescription = new JLabel();
   JLabel lblActionTitle = new JLabel();
   JLabel lblTaskStatusMessage = new JLabel();
   JProgressBar pbTaskStatusProgress = new JProgressBar();
   JButton btnTaskStatusCancel = new JButton();
   JLabel lblTaskStatusTitle = new JLabel();

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
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      tabData.setName("tabData");
      tabData.addTab("_Errors",null,createPanel1());
      tabData.addTab("_Parameters",null,createPanel3());
      jpanel1.add(tabData,cc.xy(2,2));

      jpanel1.add(createPanel5(),cc.xy(2,4));
      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      tblErrors.setName("tblErrors");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(tblErrors);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(2,5));

      jpanel1.add(createPanel2(),cc.xy(2,2));
      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5,6 });
      return jpanel1;
   }

   public JPanel createPanel2()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lblShow.setName("lblShow");
      lblShow.setText("_Show");
      jpanel1.add(lblShow,cc.xy(1,1));

      cboRules.setName("cboRules");
      jpanel1.add(cboRules,cc.xy(3,1));

      btnVisibleExtentOnly.setIcon(loadImage("org/gvsig/topology/swing/impl/images/visible_extent.png"));
      btnVisibleExtentOnly.setName("btnVisibleExtentOnly");
      btnVisibleExtentOnly.setToolTipText("_Show_only_in_visible_extent");
      EmptyBorder emptyborder1 = new EmptyBorder(2,2,2,2);
      btnVisibleExtentOnly.setBorder(emptyborder1);
      jpanel1.add(btnVisibleExtentOnly,cc.xy(9,1));

      btnZoom.setIcon(loadImage("org/gvsig/topology/swing/impl/images/zoom.png"));
      btnZoom.setName("btnZoom");
      btnZoom.setToolTipText("_Zoom");
      EmptyBorder emptyborder2 = new EmptyBorder(2,2,2,2);
      btnZoom.setBorder(emptyborder2);
      jpanel1.add(btnZoom,cc.xy(11,1));

      btnCenter.setIcon(loadImage("org/gvsig/topology/swing/impl/images/center_view.png"));
      btnCenter.setName("btnCenter");
      btnCenter.setToolTipText("_Center");
      EmptyBorder emptyborder3 = new EmptyBorder(2,2,2,2);
      btnCenter.setBorder(emptyborder3);
      jpanel1.add(btnCenter,cc.xy(13,1));

      btnActions.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_actions.png"));
      btnActions.setName("btnActions");
      btnActions.setToolTipText("_Actions");
      EmptyBorder emptyborder4 = new EmptyBorder(2,2,2,2);
      btnActions.setBorder(emptyborder4);
      jpanel1.add(btnActions,cc.xy(15,1));

      btnRefresh.setIcon(loadImage("org/gvsig/topology/swing/impl/images/refresh.png"));
      btnRefresh.setName("btnRefresh");
      btnRefresh.setToolTipText("_Update");
      EmptyBorder emptyborder5 = new EmptyBorder(2,2,2,2);
      btnRefresh.setBorder(emptyborder5);
      jpanel1.add(btnRefresh,cc.xy(17,1));

      btnShowErrors.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_errors.png"));
      btnShowErrors.setName("btnShowErrors");
      btnShowErrors.setToolTipText("_Show_only_in_visible_extent");
      EmptyBorder emptyborder6 = new EmptyBorder(2,2,2,2);
      btnShowErrors.setBorder(emptyborder6);
      jpanel1.add(btnShowErrors,cc.xy(5,1));

      btnShowExceptions.setIcon(loadImage("org/gvsig/topology/swing/impl/images/show_exceptions.png"));
      btnShowExceptions.setName("btnShowExceptions");
      btnShowExceptions.setToolTipText("_Show_only_in_visible_extent");
      EmptyBorder emptyborder7 = new EmptyBorder(2,2,2,2);
      btnShowExceptions.setBorder(emptyborder7);
      jpanel1.add(btnShowExceptions,cc.xy(7,1));

      addFillComponents(jpanel1,new int[]{ 2,4,6,8,10,12,14,16 },new int[0]);
      return jpanel1;
   }

   public JPanel createPanel3()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0),FILL:4DLU:NONE","CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,FILL:DEFAULT:GROW(1.0),CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      pnlParameters.setName("pnlParameters");
      jpanel1.add(pnlParameters,cc.xy(2,6));

      jpanel1.add(createPanel4(),cc.xy(2,8));
      lblActionDescription.setName("lblActionDescription");
      jpanel1.add(lblActionDescription,cc.xy(2,4));

      lblActionTitle.setName("lblActionTitle");
      jpanel1.add(lblActionTitle,cc.xy(2,2));

      addFillComponents(jpanel1,new int[]{ 1,2,3 },new int[]{ 1,2,3,4,5,6,7,8,9 });
      return jpanel1;
   }

   public JPanel createPanel4()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      btnParametersCancel.setActionCommand("_Cancel");
      btnParametersCancel.setName("btnParametersCancel");
      btnParametersCancel.setText("_Cancel");
      jpanel1.add(btnParametersCancel,cc.xy(5,1));

      btnParametersAccept.setActionCommand("_Execute");
      btnParametersAccept.setName("btnParametersAccept");
      btnParametersAccept.setText("_Execute");
      jpanel1.add(btnParametersAccept,cc.xy(3,1));

      addFillComponents(jpanel1,new int[]{ 1,2,4 },new int[]{ 1 });
      return jpanel1;
   }

   public JPanel createPanel5()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(0.3),FILL:4DLU:NONE,FILL:DEFAULT:GROW(0.7),FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      lblTaskStatusMessage.setName("lblTaskStatusMessage");
      lblTaskStatusMessage.setText("...");
      jpanel1.add(lblTaskStatusMessage,cc.xy(3,1));

      pbTaskStatusProgress.setName("pbTaskStatusProgress");
      pbTaskStatusProgress.setValue(25);
      jpanel1.add(pbTaskStatusProgress,cc.xy(5,1));

      btnTaskStatusCancel.setName("btnTaskStatusCancel");
      btnTaskStatusCancel.setToolTipText("_Zoom");
      EmptyBorder emptyborder1 = new EmptyBorder(2,2,2,2);
      btnTaskStatusCancel.setBorder(emptyborder1);
      jpanel1.add(btnTaskStatusCancel,cc.xy(7,1));

      lblTaskStatusTitle.setName("lblTaskStatusTitle");
      lblTaskStatusTitle.setText("...");
      jpanel1.add(lblTaskStatusTitle,cc.xy(1,1));

      addFillComponents(jpanel1,new int[]{ 2,4,6 },new int[0]);
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
