/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.desktop.application;

import java.awt.ComponentOrientation;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.sola.clients.beans.application.ApplicationSearchParamsBean;
import org.sola.clients.beans.application.ApplicationSearchResultBean;
import org.sola.clients.beans.application.ApplicationSearchResultsListBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.cadastre.SearchParcelDialog;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.BooleanCellRenderer;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.RolesConstants;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.swing.desktop.MainForm;

/**
 * This form provides parameterized application search capabilities. <p>The
 * following list of beans is used to bind the data on the form:<br />
 * {@link ApplicationSearchResultsListBean},<br />{@link ApplicationSearchParamsBean}</p>
 */
public class ApplicationSearchPanel extends ContentPanel {
    private class AssignmentPanelListener implements PropertyChangeListener {
 
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(ApplicationAssignmentDialog.ASSIGNMENT_CHANGED)) {
            }
        }
    }
    private AssignmentPanelListener assignmentPanelListener;

    /**
     * Default constructor to create form and initialize parameters.
     */
    public ApplicationSearchPanel() {
        initComponents();
        setHeaderPanel(headerPanel1);
        this.appList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationSearchResultsListBean.SELECTED_APPLICATION_PROPERTY)) {
                    customizeOpenButton((ApplicationSearchResultBean) evt.getNewValue());
                }

            }
        });
        customizeOpenButton(null);

        btnFind.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_VIEW_APPS));

    }

    private void customizeOpenButton(ApplicationSearchResultBean searchResult) {
        btnOpenApplication.setEnabled(searchResult != null);
        menuOpenApplication.setEnabled(btnOpenApplication.isEnabled());
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        appList = new org.sola.clients.beans.application.ApplicationSearchResultsListBean();
        searchParams = new org.sola.clients.beans.application.ApplicationSearchParamsBean();
        popupSearchResults = new javax.swing.JPopupMenu();
        menuOpenApplication = new javax.swing.JMenuItem();
        appListPanel = new javax.swing.JScrollPane();
        tbAppList = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpenApplication = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        lblSearchResults = new javax.swing.JLabel();
        labResults = new javax.swing.JLabel();
        btnAssignApplication = new javax.swing.JButton();
        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        labFrom = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnShowCalendarFrom = new javax.swing.JButton();
        txtFromDate = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnShowCalendarTo = new javax.swing.JButton();
        txtToDate = new javax.swing.JFormattedTextField();
        labTo = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        labAppNumber = new javax.swing.JLabel();
        txtAppNumber = new javax.swing.JTextField();
        txtParcel = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labContactPerson = new javax.swing.JLabel();
        txtContactPerson = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        labAgentName = new javax.swing.JLabel();
        txtAgentName = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        btnSearchUpiWardParcel = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        txtDocumentReference = new javax.swing.JTextField();
        txtDocumentNumber = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        btnFind = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cadastreObjectSearch = new org.sola.clients.swing.ui.cadastre.LocationSearch();
        lblSection = new javax.swing.JLabel();

        popupSearchResults.setName("popupSearchResults"); // NOI18N

        menuOpenApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        menuOpenApplication.setText(bundle.getString("ApplicationSearchPanel.menuOpenApplication.text")); // NOI18N
        menuOpenApplication.setName("menuOpenApplication"); // NOI18N
        menuOpenApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenApplicationActionPerformed(evt);
            }
        });
        popupSearchResults.add(menuOpenApplication);

        setHelpTopic(bundle.getString("ApplicationSearchPanel.helpTopic")); // NOI18N
        setMinimumSize(new java.awt.Dimension(512, 351));
        setName("Form"); // NOI18N

        appListPanel.setBorder(null);
        appListPanel.setAlignmentX(1.0F);
        appListPanel.setAlignmentY(1.0F);
        appListPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        appListPanel.setName("appListPanel"); // NOI18N
        appListPanel.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        tbAppList.setAlignmentX(1.0F);
        tbAppList.setAlignmentY(1.0F);
        tbAppList.setComponentPopupMenu(popupSearchResults);
        tbAppList.setName("tbAppList"); // NOI18N
        tbAppList.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${applicationSearchResultsList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appList, eLProperty, tbAppList);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${partialNr}"));
        columnBinding.setColumnName("Partial Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${section}"));
        columnBinding.setColumnName("Section");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${lodgingDatetime}"));
        columnBinding.setColumnName("Lodging Datetime");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${serviceStatusList}"));
        columnBinding.setColumnName("Service Status List");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${parcel}"));
        columnBinding.setColumnName("Parcel");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${contactPerson}"));
        columnBinding.setColumnName("Contact Person");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status}"));
        columnBinding.setColumnName("Status");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${feePaid}"));
        columnBinding.setColumnName("Fee Paid");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appList, org.jdesktop.beansbinding.ELProperty.create("${selectedApplication}"), tbAppList, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tbAppList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbAppListMouseClicked(evt);
            }
        });
        appListPanel.setViewportView(tbAppList);
        tbAppList.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title0_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title2_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title1_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title3_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(3).setCellRenderer(new org.sola.clients.swing.ui.renderers.CellDelimitedListRenderer());
        tbAppList.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title7")); // NOI18N
        tbAppList.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title4_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title5_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(7).setHeaderValue(bundle.getString("ApplicationSearchPanel.tbAppList.columnModel.title6_1")); // NOI18N
        tbAppList.getColumnModel().getColumn(7).setCellRenderer(new BooleanCellRenderer());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnOpenApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenApplication.setText(bundle.getString("ApplicationSearchPanel.btnOpenApplication.text")); // NOI18N
        btnOpenApplication.setFocusable(false);
        btnOpenApplication.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenApplication.setName("btnOpenApplication"); // NOI18N
        btnOpenApplication.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenApplicationActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenApplication);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        lblSearchResults.setFont(LafManager.getInstance().getLabFontBold());
        lblSearchResults.setText(bundle.getString("ApplicationSearchPanel.lblSearchResults.text")); // NOI18N
        lblSearchResults.setName("lblSearchResults"); // NOI18N
        jToolBar1.add(lblSearchResults);

        labResults.setText(bundle.getString("ApplicationSearchPanel.labResults.text")); // NOI18N
        labResults.setName("labResults"); // NOI18N
        labResults.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        labResults.setHorizontalAlignment(JLabel.LEADING);
        jToolBar1.add(labResults);

        btnAssignApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/assign.png"))); // NOI18N
        btnAssignApplication.setText(bundle.getString("ApplicationSearchPanel.btnAssignApplication.text_1")); // NOI18N
        btnAssignApplication.setToolTipText(bundle.getString("ApplicationSearchPanel.btnAssignApplication.toolTipText_1")); // NOI18N
        btnAssignApplication.setFocusable(false);
        btnAssignApplication.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAssignApplication.setName(bundle.getString("ApplicationSearchPanel.btnAssignApplication.name_1")); // NOI18N
        btnAssignApplication.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAssignApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignApplicationActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAssignApplication);

        headerPanel1.setName("headerPanel1"); // NOI18N
        headerPanel1.setTitleText(bundle.getString("ApplicationSearchPanel.headerPanel1.titleText")); // NOI18N

        jPanel10.setName("jPanel10"); // NOI18N

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setLayout(new java.awt.GridLayout(2, 1));

        jPanel6.setName("jPanel6"); // NOI18N

        labFrom.setText(bundle.getString("ApplicationSearchPanel.labFrom.text")); // NOI18N
        labFrom.setName("labFrom"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);
        btnShowCalendarFrom.setName("btnShowCalendarFrom"); // NOI18N
        btnShowCalendarFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFromActionPerformed(evt);
            }
        });

        txtFromDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtFromDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtFromDate.setName("txtFromDate"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${fromDate}"), txtFromDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txtFromDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFromDate.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowCalendarFrom)
                .addGap(2, 2, 2))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnShowCalendarFrom))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labFrom)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(labFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel6);

        jPanel3.setName("jPanel3"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        btnShowCalendarTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarTo.setText(bundle.getString("ApplicationSearchPanel.btnShowCalendarTo.text")); // NOI18N
        btnShowCalendarTo.setBorder(null);
        btnShowCalendarTo.setName("btnShowCalendarTo"); // NOI18N
        btnShowCalendarTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarToActionPerformed(evt);
            }
        });

        txtToDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtToDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtToDate.setName("txtToDate"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${toDate}"), txtToDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txtToDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtToDate.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnShowCalendarTo))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(btnShowCalendarTo)
                .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        labTo.setText(bundle.getString("ApplicationSearchPanel.labTo.text")); // NOI18N
        labTo.setName("labTo"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTo)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel3);

        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setLayout(new java.awt.GridLayout(2, 1));

        jPanel12.setName("jPanel12"); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        labAppNumber.setText(bundle.getString("ApplicationSearchPanel.labAppNumber.text")); // NOI18N
        labAppNumber.setName("labAppNumber"); // NOI18N

        txtAppNumber.setName("txtAppNumber"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${nr}"), txtAppNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtAppNumber.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtAppNumber.setHorizontalAlignment(JTextField.LEADING);

        txtParcel.setName(bundle.getString("ApplicationSearchPanel.txtParcel.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${parcel}"), txtParcel, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel5.setText(bundle.getString("ApplicationSearchPanel.jLabel5.text")); // NOI18N
        jLabel5.setName(bundle.getString("ApplicationSearchPanel.jLabel5.name")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAppNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labAppNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParcel, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labAppNumber)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAppNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtParcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel12);

        jPanel1.setName("jPanel1"); // NOI18N

        labContactPerson.setText(bundle.getString("ApplicationSearchPanel.labContactPerson.text")); // NOI18N
        labContactPerson.setName("labContactPerson"); // NOI18N

        txtContactPerson.setName("txtContactPerson"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${contactPerson}"), txtContactPerson, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtContactPerson.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtContactPerson.setHorizontalAlignment(JTextField.LEADING);

        jPanel2.setName("jPanel2"); // NOI18N

        labAgentName.setText(bundle.getString("ApplicationSearchPanel.labAgentName.text")); // NOI18N
        labAgentName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        labAgentName.setName("labAgentName"); // NOI18N

        txtAgentName.setName("txtAgentName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${agent}"), txtAgentName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtAgentName.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtAgentName.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(labAgentName)
                        .addGap(0, 203, Short.MAX_VALUE))
                    .addComponent(txtAgentName))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(labAgentName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAgentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labContactPerson, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labContactPerson)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel9.add(jPanel1);

        jPanel11.setName("jPanel11"); // NOI18N
        jPanel11.setLayout(new java.awt.GridLayout(2, 1));

        jPanel15.setName(bundle.getString("ApplicationSearchPanel.jPanel15.name")); // NOI18N

        btnSearchUpiWardParcel.setText(bundle.getString("ApplicationSearchPanel.btnSearchUpiWardParcel.text")); // NOI18N
        btnSearchUpiWardParcel.setName(bundle.getString("ApplicationSearchPanel.btnSearchUpiWardParcel.name")); // NOI18N
        btnSearchUpiWardParcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchUpiWardParcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(btnSearchUpiWardParcel)
                .addGap(0, 72, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(btnSearchUpiWardParcel)
                .addContainerGap())
        );

        jPanel11.add(jPanel15);

        jPanel13.setName("jPanel13"); // NOI18N

        btnClear.setText(bundle.getString("ApplicationSearchPanel.btnClear.text")); // NOI18N
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setName("btnClear"); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 84, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(btnClear)
                .addContainerGap())
        );

        jPanel11.add(jPanel13);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)))
        );

        txtDocumentReference.setToolTipText(bundle.getString("ApplicationSearchPanel.txtDocumentReference.toolTipText")); // NOI18N
        txtDocumentReference.setName(bundle.getString("ApplicationSearchPanel.txtDocumentReference.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${documentReference}"), txtDocumentReference, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtDocumentNumber.setToolTipText(bundle.getString("ApplicationSearchPanel.txtDocumentNumber.toolTipText")); // NOI18N
        txtDocumentNumber.setName(bundle.getString("ApplicationSearchPanel.txtDocumentNumber.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${documentNumber}"), txtDocumentNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel3.setText(bundle.getString("ApplicationSearchPanel.jLabel3.text")); // NOI18N
        jLabel3.setName(bundle.getString("ApplicationSearchPanel.jLabel3.name")); // NOI18N

        jLabel4.setText(bundle.getString("ApplicationSearchPanel.jLabel4.text")); // NOI18N
        jLabel4.setName(bundle.getString("ApplicationSearchPanel.jLabel4.name")); // NOI18N

        jPanel14.setName("jPanel14"); // NOI18N

        btnFind.setText(bundle.getString("ApplicationSearchPanel.btnFind.text")); // NOI18N
        btnFind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFind.setName("btnFind"); // NOI18N
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        jLabel2.setText(bundle.getString("ApplicationSearchPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap())
            .addComponent(btnFind, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFind)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        cadastreObjectSearch.setName(bundle.getString("ApplicationSearchPanel.cadastreObjectSearch.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, searchParams, org.jdesktop.beansbinding.ELProperty.create("${section}"), cadastreObjectSearch, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lblSection.setText(bundle.getString("ApplicationSearchPanel.lblSection.text")); // NOI18N
        lblSection.setName(bundle.getString("ApplicationSearchPanel.lblSection.name")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocumentReference, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDocumentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSection, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(appListPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSection))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDocumentReference, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocumentNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(appListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void tbAppListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbAppListMouseClicked
        if (evt.getClickCount() == 2) {
            openApplication();
        }
    }//GEN-LAST:event_tbAppListMouseClicked

    private void btnShowCalendarFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFromActionPerformed
        showCalendar(txtFromDate);
    }//GEN-LAST:event_btnShowCalendarFromActionPerformed

    private void btnShowCalendarToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarToActionPerformed
        showCalendar(txtToDate);
    }//GEN-LAST:event_btnShowCalendarToActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_SEARCHING));
                appList.searchApplications(searchParams);
                return null;
            }

            @Override
            public void taskDone() {
                if (appList.getApplicationSearchResultsList().size() <= 0) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                }

                if (appList.getApplicationSearchResultsList().size() > 99) {
                    Object[] parms = {100};
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, parms);
                }
                labResults.setText(String.format("(%s)", appList.getApplicationSearchResultsList().size()));
                tbAppList.setVisible(true);
                txtAppNumber.requestFocus();
            }
        };

        TaskManager.getInstance().runTask(t);
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtAppNumber.setText(null);
        txtAgentName.setText(null);
        txtFromDate.setValue(null);
        txtToDate.setValue(null);
        txtContactPerson.setText(null);
        txtDocumentReference.setText(null);
        txtDocumentNumber.setText(null);
        txtParcel.setText(null);
        labResults.setText(null);
        tbAppList.setVisible(false);
        txtAppNumber.requestFocus();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnOpenApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenApplicationActionPerformed
        openApplication();
    }//GEN-LAST:event_btnOpenApplicationActionPerformed

    private void menuOpenApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenApplicationActionPerformed
        openApplication();
    }//GEN-LAST:event_menuOpenApplicationActionPerformed

    private void btnSearchUpiWardParcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchUpiWardParcelActionPerformed
        SearchUpiWardParcel();
    }//GEN-LAST:event_btnSearchUpiWardParcelActionPerformed

    private void btnAssignApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignApplicationActionPerformed
        assignApplication();
    }//GEN-LAST:event_btnAssignApplicationActionPerformed
    
    private void setUpiWardParcel(String parcelNumber, String upiWardCode) {
        this.txtParcel.setText(upiWardCode+'/'+parcelNumber);
    }
    
    /**
     * Opens form to assign application.
     */
    private void assignApplication() {
        assignUnassign(appList.getSelectedApplication(), true);
    }
    
      /**
     * Opens application assignment form with selected applications.
     *
     * @param appList Selected applications to assign or unassign.
     * @param assign Indicates whether to assign or unassign applications
     */
    private void assignUnassign(final ApplicationSearchResultBean appList, final boolean assign) {
        
         if (appList == null) {
            return;
        }

     if (assign) {
            if (appList.getAssigneeId()== null) {
                ApplicationAssignmentDialog form = new ApplicationAssignmentDialog(appList, MainForm.getInstance(), true);
                WindowUtility.centerForm(form);
                form.addPropertyChangeListener(assignmentPanelListener);
                form.setVisible(true);
            } else {
                MessageUtility.displayMessage(ClientMessage.CHECK_ALREADY_ASSIGNED, new Object[]{appList.getAssigneeName()});
                return;
            } 
        } else {
            if (MessageUtility.displayMessage(ClientMessage.APPLICATION_CONFIRM_UNASSIGN) == MessageUtility.BUTTON_ONE) {
                    String assigneeId = appList.getAssigneeId();
                    if (assigneeId != null && assigneeId.equals(SecurityBean.getCurrentUser().getId())
                            && !SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_YOURSELF,
                            RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS)) {
                        // Can't unassign from yourself
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_UNASSIGN_FROM_SELF_FORBIDDEN,
                                new Object[]{appList.getNr()});
                    }
                    if (assigneeId != null && !assigneeId.equals(SecurityBean.getCurrentUser().getId())
                            && !SecurityBean.isInRole(RolesConstants.APPLICATION_UNASSIGN_FROM_OTHERS)) {
                        MessageUtility.displayMessage(ClientMessage.APPLICATION_UNASSIGN_FROM_OTHERS_FORBIDDEN,
                                new Object[]{appList.getNr()});
                    }
                    ApplicationBean.assignUser(appList, null);
                }
        }
    }    
    private void SearchUpiWardParcel() {
        SearchParcelDialog form = new SearchParcelDialog(null, true);
        WindowUtility.centerForm(form);
        form.addPropertyChangeListener(new PropertyChangeListener() {
        
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(SearchParcelDialog.SELECTED_PARCEL)) {

                    CadastreObjectBean cadastreObject = (CadastreObjectBean) evt.getNewValue();
                    
                    setUpiWardParcel(cadastreObject.getNameFirstpart(),cadastreObject.getNameLastpart());
                         
             }
            }
        });
        form.setVisible(true);
    }
    public void clickFind() {
        this.btnFindActionPerformed(null);
    }

    /**
     * Opens {@link ApplicationForm} for selected application in search results.
     */
    private void openApplication() {
        if (!SecurityBean.isInRole(RolesConstants.APPLICATION_EDIT_APPS)
                || appList.getSelectedApplication() == null) {
            return;
        }

        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APP));
                if (getMainContentPanel() != null) {
                    ApplicationPanel applicationPanel = new ApplicationPanel(
                            appList.getSelectedApplication().getId());
                    getMainContentPanel().addPanel(applicationPanel, MainContentPanel.CARD_APPLICATION, true);
                }
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationSearchResultsListBean appList;
    private javax.swing.JScrollPane appListPanel;
    private javax.swing.JButton btnAssignApplication;
    private javax.swing.JButton btnClear;
    public javax.swing.JButton btnFind;
    private javax.swing.JButton btnOpenApplication;
    private javax.swing.JButton btnSearchUpiWardParcel;
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarTo;
    private org.sola.clients.swing.ui.cadastre.LocationSearch cadastreObjectSearch;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labAgentName;
    private javax.swing.JLabel labAppNumber;
    private javax.swing.JLabel labContactPerson;
    private javax.swing.JLabel labFrom;
    private javax.swing.JLabel labResults;
    private javax.swing.JLabel labTo;
    private javax.swing.JLabel lblSearchResults;
    private javax.swing.JLabel lblSection;
    private javax.swing.JMenuItem menuOpenApplication;
    private javax.swing.JPopupMenu popupSearchResults;
    private org.sola.clients.beans.application.ApplicationSearchParamsBean searchParams;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tbAppList;
    private javax.swing.JTextField txtAgentName;
    private javax.swing.JTextField txtAppNumber;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtDocumentNumber;
    private javax.swing.JTextField txtDocumentReference;
    private javax.swing.JFormattedTextField txtFromDate;
    private javax.swing.JTextField txtParcel;
    private javax.swing.JFormattedTextField txtToDate;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
