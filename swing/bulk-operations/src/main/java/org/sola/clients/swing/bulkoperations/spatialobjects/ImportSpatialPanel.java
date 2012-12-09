/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.spatialobjects;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Set;
import javax.swing.JPanel;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.jdesktop.beansbinding.Binding;
import org.reflections.Reflections;
import org.sola.clients.swing.bulkoperations.beans.*;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.gis.beans.TransactionCadastreChangeBean;
import org.sola.clients.swing.gis.data.PojoDataAccess;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Elton Manoku
 */
public class ImportSpatialPanel extends ContentPanel {

    private static String PANEL_NAME = "IMPORT_SPATIAL_PANEL";
    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(
            "org/sola/clients/swing/bulkoperations/spatialobjects/Bundle");
    private TransactionCadastreChangeBean transactionCadastreChange = null;
    private TransactionBulkOperationSpatial transaction = null;

    /**
     * Creates new form ImportSpatialPanel
     */
    public ImportSpatialPanel() {
        initComponents();
        this.setName(PANEL_NAME);
        initializeDestinationPanels();
        initializeEvents();
    }

    private void initializeDestinationPanels() {
        String namespaceToScan = ImportSpatialPanel.class.getPackage().getName();
        Reflections reflections = new Reflections(namespaceToScan);
        Set<Class<? extends ISpatialDestinationUI>> subTypes =
                reflections.getSubTypesOf(ISpatialDestinationUI.class);
        try {
            for (Class<? extends ISpatialDestinationUI> foundClass : subTypes) {
                JPanel destinationPanel = (JPanel) foundClass.newInstance();
                pnlMainPanel.addPanel(destinationPanel, destinationPanel.getName(), false);
            }
        } catch (InstantiationException ex) {
            LogUtility.log("Error initializing ISpatialDestinationUI", ex);
        } catch (IllegalAccessException ex) {
            LogUtility.log("Error initializing ISpatialDestinationUI", ex);
        }

    }

    private void initializeEvents() {
        spatialDestinationPotentialList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(
                        SpatialDestinationPotentialListBean.SELECTED_BEAN_PROPERTY)) {
                    destinationChanged((SpatialDestinationBean) evt.getNewValue());
                }
            }
        });

        SpatialDestinationBean destinationBeanToSelect = null;
        for (Object bean : spatialDestinationPotentialList.getBeanList()) {
            if (bean.getClass().equals(SpatialDestinationCadastreObjectBean.class)) {
                destinationBeanToSelect = (SpatialDestinationBean) bean;
                break;
            }
        }
        if (destinationBeanToSelect == null
                && spatialDestinationPotentialList.getBeanList().size() > 0) {
            destinationBeanToSelect = (SpatialDestinationBean) spatialDestinationPotentialList.getBeanList().get(0);
        }
        spatialDestinationPotentialList.setSelectedSpatialDestinationBean(
                destinationBeanToSelect);

        spatialBulkMove.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (SpatialBulkMoveBean.PROPERTY_SOURCE.equals(evt.getPropertyName())) {
                    sourceChanged((SpatialSourceBean) evt.getNewValue());
                }
            }
        });
    }

    private void destinationChanged(SpatialDestinationBean newDestination) {
        setPostLoadEnabled(false);
        String panelName = newDestination.getPanelName();
        pnlMainPanel.showPanel(panelName);
        spatialBulkMove.setDestination(
                ((ISpatialDestinationUI) pnlMainPanel.getPanel(panelName)).getBean());
        setSourceAttributesInDestinationPanel();
    }

    private void sourceChanged(SpatialSourceBean newSource) {
        setPostLoadEnabled(false);
        refreshBindingOfSourceAttributes();
    }

    private void findAndSetSource() {
        if (spatialBulkMove.getSource() == null) {
            return;
        }
        File sourceFile = JFileDataStoreChooser.showOpenFile(
                spatialBulkMove.getSource().getCode(), null);
        if (sourceFile == null) {
            return;
        }
        spatialBulkMove.getSource().setSourceFile(sourceFile);
        refreshBindingOfSourceAttributes();
    }

    private void refreshBindingOfSourceAttributes() {
        setPostLoadEnabled(false);
        for (Binding binding : bindingGroup.getBindings()) {
            if (binding.getTargetObject().equals(listAttributes)
                    || binding.getTargetObject().equals(txtSourceFeaturesNr)
                    || binding.getTargetObject().equals(txtSourceGeometryType)
                    || binding.getTargetObject().equals(txtSourcePath)) {
                binding.unbind();
                binding.bind();
            }
        }
        setSourceAttributesInDestinationPanel();
    }

    private void setSourceAttributesInDestinationPanel() {
        if (spatialBulkMove.getSource() == null) {
            return;
        }
        if (spatialBulkMove.getDestination() == null) {
            return;
        }
        ISpatialDestinationUI activeDestinationPanel =
                (ISpatialDestinationUI) pnlMainPanel.getPanel(
                spatialBulkMove.getDestination().getPanelName());
        activeDestinationPanel.getSourceAttributes().clear();
        activeDestinationPanel.getSourceAttributes().addAll(
                spatialBulkMove.getSource().getAttributes());
    }

    private void convertAndSendToServer() {

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(
                        ClientMessage.BULK_OPERATIONS_CONVERT_FEATURES_TO_BEANS_AND_SENDTOSERVER));
                transaction = spatialBulkMove.sendToServer();
                return null;
            }

            @Override
            protected void taskDone() {
                super.taskDone();
                afterConversion(transaction);
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    private void afterConversion(TransactionBulkOperationSpatial transaction) {
        if (spatialBulkMove.getDestination().getClass().equals(
                SpatialDestinationCadastreObjectBean.class)) {
            afterConversionOfCadastreObject(transaction);
        } else {
            afterConversionOfOtherObject(transaction);
        }
        setPostLoadEnabled(true);
    }

    private void afterConversionOfCadastreObject(TransactionBulkOperationSpatial transaction) {
        transactionCadastreChange =
                PojoDataAccess.getInstance().getTransactionCadastreChangeById(transaction.getId());

        String informationResourceName = "ImportSpatialPanel.lblInformationText.text.success";
        if (transactionCadastreChange.getSurveyPointList().size() > 0) {
            informationResourceName = "ImportSpatialPanel.lblInformationText.text.problem";
        }
        lblInformationText.setText(bundle.getString(informationResourceName));
    }

    private void afterConversionOfOtherObject(TransactionBulkOperationSpatial transaction) {
        String informationResourceName = "ImportSpatialPanel.lblInformationText.text.success";
        lblInformationText.setText(bundle.getString(informationResourceName));
    }

    private void openMap() {
        MapPanel mapPanel = null;
        if (spatialBulkMove.getDestination().getClass().equals(
                SpatialDestinationCadastreObjectBean.class)) {
            if (transactionCadastreChange == null) {
                return;
            }
            if (transactionCadastreChange.getSurveyPointList().size() > 0) {
                SpatialDestinationCadastreObjectBean destinationBean =
                        (SpatialDestinationCadastreObjectBean) spatialBulkMove.getDestination();
                mapPanel = new MapPanel(
                        transactionCadastreChange,
                        destinationBean.getCadastreObjectTypeCode(),
                        destinationBean.getNameLastPart());
            }
        }
        if (mapPanel == null) {
            mapPanel = new MapPanel(spatialBulkMove.getSource().getExtent());
        }
        this.getMainContentPanel().addPanel(mapPanel, mapPanel.getName(), true);

    }

    private void rollback() {
        if (transaction != null){
            transaction.reject();
        }
        setPostLoadEnabled(false);
    }

    private void setPostLoadEnabled(boolean enable) {
        btnOpenMap.setEnabled(enable);
        btnRollback.setEnabled(enable);
        if (!enable) {
            transactionCadastreChange = null;
            transaction = null;
            String informationResourceName = "ImportSpatialPanel.lblInformationText.text";
            lblInformationText.setText(bundle.getString(informationResourceName));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        spatialBulkMove = new org.sola.clients.swing.bulkoperations.beans.SpatialBulkMoveBean();
        spatialDestinationPotentialList = new org.sola.clients.swing.bulkoperations.beans.SpatialDestinationPotentialListBean();
        spatialSourcePotentialList = new org.sola.clients.swing.bulkoperations.beans.SpatialSourcePotentialListBean();
        cmbSourceType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSourcePath = new javax.swing.JTextField();
        cmdBrowse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAttributes = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        cmbDestination = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        btnMove = new javax.swing.JButton();
        txtSourceGeometryType = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSourceFeaturesNr = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        pnlMainPanel = new org.sola.clients.swing.ui.MainContentPanel();
        groupPanel3 = new org.sola.clients.swing.ui.GroupPanel();
        pnlPostProcess = new javax.swing.JPanel();
        btnOpenMap = new javax.swing.JButton();
        btnRollback = new javax.swing.JButton();
        lblInformationText = new javax.swing.JLabel();
        lblInformation = new javax.swing.JLabel();

        setHeaderPanel(headerPanel);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/spatialobjects/Bundle"); // NOI18N
        setHelpTopic(bundle.getString("ImportSpatialPanel.helpTopic")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${beanList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialSourcePotentialList, eLProperty, cmbSourceType);
        bindingGroup.addBinding(jComboBoxBinding);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialBulkMove, org.jdesktop.beansbinding.ELProperty.create("${source}"), cmbSourceType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel1.setText(bundle.getString("ImportSpatialPanel.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("ImportSpatialPanel.jLabel2.text")); // NOI18N

        txtSourcePath.setEditable(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialBulkMove, org.jdesktop.beansbinding.ELProperty.create("${source.sourceFile.absolutePath}"), txtSourcePath, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        cmdBrowse.setText(bundle.getString("ImportSpatialPanel.cmdBrowse.text")); // NOI18N
        cmdBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBrowseActionPerformed(evt);
            }
        });

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${source.attributes}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialBulkMove, eLProperty, listAttributes, "");
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${dataType}"));
        columnBinding.setColumnName("Data Type");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(listAttributes);
        listAttributes.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ImportSpatialPanel.listAttributes.columnModel.title0")); // NOI18N
        listAttributes.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ImportSpatialPanel.listAttributes.columnModel.title1")); // NOI18N

        jLabel3.setText(bundle.getString("ImportSpatialPanel.jLabel3.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${beanList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialDestinationPotentialList, eLProperty, cmbDestination);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialDestinationPotentialList, org.jdesktop.beansbinding.ELProperty.create("${selectedSpatialDestinationBean}"), cmbDestination, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel4.setText(bundle.getString("ImportSpatialPanel.jLabel4.text")); // NOI18N

        groupPanel1.setTitleText(bundle.getString("ImportSpatialPanel.groupPanel1.titleText")); // NOI18N

        groupPanel2.setTitleText(bundle.getString("ImportSpatialPanel.groupPanel2.titleText")); // NOI18N

        btnMove.setText(bundle.getString("ImportSpatialPanel.btnMove.text")); // NOI18N
        btnMove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveActionPerformed(evt);
            }
        });

        txtSourceGeometryType.setEditable(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialBulkMove, org.jdesktop.beansbinding.ELProperty.create("${source.geometryType}"), txtSourceGeometryType, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel9.setText(bundle.getString("ImportSpatialPanel.jLabel9.text")); // NOI18N

        txtSourceFeaturesNr.setEditable(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, spatialBulkMove, org.jdesktop.beansbinding.ELProperty.create("${source.featuresNumber}"), txtSourceFeaturesNr, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel10.setText(bundle.getString("ImportSpatialPanel.jLabel10.text")); // NOI18N

        headerPanel.setTitleText(bundle.getString("ImportSpatialPanel.headerPanel.titleText")); // NOI18N

        groupPanel3.setTitleText(bundle.getString("ImportSpatialPanel.groupPanel3.titleText")); // NOI18N

        btnOpenMap.setText(bundle.getString("ImportSpatialPanel.btnOpenMap.text")); // NOI18N
        btnOpenMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenMapActionPerformed(evt);
            }
        });

        btnRollback.setText(bundle.getString("ImportSpatialPanel.btnRollback.text")); // NOI18N
        btnRollback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollbackActionPerformed(evt);
            }
        });

        lblInformationText.setText(bundle.getString("ImportSpatialPanel.lblInformationText.text")); // NOI18N

        lblInformation.setText(bundle.getString("ImportSpatialPanel.lblInformation.text")); // NOI18N

        javax.swing.GroupLayout pnlPostProcessLayout = new javax.swing.GroupLayout(pnlPostProcess);
        pnlPostProcess.setLayout(pnlPostProcessLayout);
        pnlPostProcessLayout.setHorizontalGroup(
            pnlPostProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPostProcessLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblInformation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblInformationText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnRollback)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpenMap))
        );
        pnlPostProcessLayout.setVerticalGroup(
            pnlPostProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPostProcessLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPostProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPostProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblInformation)
                        .addComponent(lblInformationText))
                    .addGroup(pnlPostProcessLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnOpenMap)
                        .addComponent(btnRollback)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(groupPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlMainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(txtSourcePath)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmdBrowse))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSourceFeaturesNr, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtSourceGeometryType, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbDestination, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnMove)
                                .addGap(67, 67, 67)
                                .addComponent(pnlPostProcess, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbSourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSourcePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdBrowse))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSourceGeometryType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(txtSourceFeaturesNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addComponent(cmbDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnMove))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(pnlPostProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBrowseActionPerformed
        findAndSetSource();
    }//GEN-LAST:event_cmdBrowseActionPerformed

    private void btnMoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveActionPerformed
        setPostLoadEnabled(false);
        if (spatialBulkMove.validate(true).size() > 0) {
            return;
        }
        convertAndSendToServer();

    }//GEN-LAST:event_btnMoveActionPerformed

    private void btnRollbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollbackActionPerformed
        rollback();
    }//GEN-LAST:event_btnRollbackActionPerformed

    private void btnOpenMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenMapActionPerformed
        openMap();
    }//GEN-LAST:event_btnOpenMapActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMove;
    private javax.swing.JButton btnOpenMap;
    private javax.swing.JButton btnRollback;
    private javax.swing.JComboBox cmbDestination;
    private javax.swing.JComboBox cmbSourceType;
    private javax.swing.JButton cmdBrowse;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.GroupPanel groupPanel3;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblInformation;
    private javax.swing.JLabel lblInformationText;
    private javax.swing.JTable listAttributes;
    private org.sola.clients.swing.ui.MainContentPanel pnlMainPanel;
    private javax.swing.JPanel pnlPostProcess;
    private org.sola.clients.swing.bulkoperations.beans.SpatialBulkMoveBean spatialBulkMove;
    private org.sola.clients.swing.bulkoperations.beans.SpatialDestinationPotentialListBean spatialDestinationPotentialList;
    private org.sola.clients.swing.bulkoperations.beans.SpatialSourcePotentialListBean spatialSourcePotentialList;
    private javax.swing.JTextField txtSourceFeaturesNr;
    private javax.swing.JTextField txtSourceGeometryType;
    private javax.swing.JTextField txtSourcePath;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}