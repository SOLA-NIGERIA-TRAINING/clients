/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.ui.party;

import java.awt.ComponentOrientation;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.JTextField;
import org.jdesktop.application.Action;
import org.sola.clients.swing.ui.renderers.SimpleComboBoxRenderer;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartyRoleBean;
import org.sola.clients.beans.referencedata.CommunicationTypeListBean;
import org.sola.clients.beans.referencedata.GenderTypeListBean;
import org.sola.clients.beans.referencedata.IdTypeListBean;
import org.sola.clients.beans.referencedata.PartyRoleTypeBean;
import org.sola.clients.beans.referencedata.PartyRoleTypeListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.utils.BindingTools;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Used to create or edit party object. {@link PartyBean} is used to bind data on the form.
 */
public class PartyPanel extends javax.swing.JPanel {

    public static final String UPDATED_PARTY = "updatedParty";
    public static final String CREATED_PARTY = "createdParty";
    public static final String CANCEL_ACTION = "Cancel";
    private String saveEventToFire = UPDATED_PARTY;
    private PartyBean partyBean;
    private boolean closeOnSave;
    private boolean closeOnCreate;
    static String individualString = "naturalPerson";
    static String entityString = "nonNaturalPerson";
    private static final String individualLabel = MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_INDIVIDUAL).getMessage();
//"First Name(s):";
    private static final String entityLabel = MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_ENTITY).getMessage();
//"Entity name:";
    private boolean savePartyOnAction;
    private boolean readOnly = false;

    /** Default form constructor. */
    public PartyPanel() {
        initComponents();
        customizeComponents();
    }

    /** 
     * Form constructor. 
     * @param savePartyOnAction Boolean flag to indicate whether to save party 
     * when Save button is clicked.
     */
    public PartyPanel(boolean savePartyOnAction) {
        this(savePartyOnAction, null, false);
        customizeComponents();
    }

    /** 
     * Form constructor. 
     * @param savePartyOnAction Boolean flag to indicate whether to save party 
     * when Save button is clicked.
     * @param partyBean {@link PartyBean} instance to display.
     * @param readOnly Indicates whether to allow any changes on the form.
     */
    public PartyPanel(boolean savePartyOnAction, PartyBean partyBean, boolean readOnly) {
        this.partyBean = partyBean;
        this.savePartyOnAction = savePartyOnAction;

        if (!readOnly) {
            this.readOnly = !SecurityBean.isInRole(RolesConstants.PARTY_SAVE);
        }

        initComponents();
        
        setupPartyBean(null);

        this.partyRoleTypes.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PartyRoleTypeListBean.SELECTED_PARTYROLETYPE_PROPERTY)) {
                    customizeAddRoleButton((PartyRoleTypeBean) evt.getNewValue());
                }
            }
        });

        customizeAddRoleButton(null);
        customizeRoleButtons(null);
        customizeComponents();
    }

    public boolean isCloseOnCreate() {
        return closeOnCreate;
    }

    public void setCloseOnCreate(boolean closeOnCreate) {
        this.closeOnCreate = closeOnCreate;
        setButtonOkCaption();
    }

    public boolean isCloseOnSave() {
        return closeOnSave;
    }

    public void setCloseOnSave(boolean closeOnSave) {
        this.closeOnSave = closeOnSave;
        setButtonOkCaption();
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        customizePanel();
    }

    public boolean isSavePartyOnAction() {
        return savePartyOnAction;
    }

    public void setSavePartyOnAction(boolean savePartyOnAction) {
        this.savePartyOnAction = savePartyOnAction;
    }

    public PartyBean getPartyBean() {
        if (partyBean == null) {
            partyBean = new PartyBean();
            firePropertyChange("partyBean", null, this.partyBean);
        }
        return partyBean;
    }

    public void setPartyBean(PartyBean partyBean) {
        setupPartyBean(partyBean);
    }

    private IdTypeListBean createIdTypes() {
        if (idTypes == null) {
            idTypes = new IdTypeListBean(true);
        }
        return idTypes;
    }

    private GenderTypeListBean createGenderTypes() {
        if (genderTypes == null) {
            genderTypes = new GenderTypeListBean(true);
        }
        return genderTypes;
    }

    private CommunicationTypeListBean createCommunicationTypes() {
        if (communicationTypes == null) {
            communicationTypes = new CommunicationTypeListBean(true);
        }
        return communicationTypes;
    }

    /** Setup reference data bean object, used to bind data on the form. */
    private void setupPartyBean(PartyBean partyBean) {
        detailsPanel.setSelectedIndex(0);
        cbxPartyRoleTypes.setSelectedIndex(0);
        
        if (partyBean != null) {
            this.partyBean = partyBean;
            saveEventToFire = UPDATED_PARTY;
        } else {
            this.partyBean = new PartyBean();
            saveEventToFire = CREATED_PARTY;
        }

        this.partyBean.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PartyBean.SELECTED_ROLE_PROPERTY)) {
                    customizeRoleButtons((PartyRoleBean) evt.getNewValue());
                }
            }
        });

        setButtonOkCaption();
        customizePanel();
        firePropertyChange("partyBean", null, this.partyBean);
        BindingTools.refreshBinding(bindingGroup, "rolesGroup");
    }

    /** Assigns OK button text label. */
    private void setButtonOkCaption() {
        try {
            if (saveEventToFire.equals(UPDATED_PARTY)) {
                if (closeOnSave) {
                    btnSave.setText(MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_SAVE_AND_CLOSE).getMessage());
                } else {
                    btnSave.setText(MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_SAVE).getMessage());
                }
            } else {
                if (closeOnCreate) {
                    btnSave.setText(MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_CREATE_AND_CLOSE).getMessage());
                } else {
                    btnSave.setText(MessageUtility.getLocalizedMessage(
                            ClientMessage.GENERAL_LABELS_CREATE).getMessage());
                }
            }
        } catch (Exception e) {
        }
    }

    /** Applies customization of component L&F. */
    private void customizeComponents() {

//    BUTTONS   
        LafManager.getInstance().setBtnProperties(btnAddRole);
        LafManager.getInstance().setBtnProperties(btnRemoveRole);
        LafManager.getInstance().setBtnProperties(btnSave);
        LafManager.getInstance().setBtnProperties(btnCancel);


        //  RADIO  BUTTONS   
        LafManager.getInstance().setRadioProperties(entityButton);
        LafManager.getInstance().setRadioProperties(individualButton);


//    COMBOBOXES
        LafManager.getInstance().setCmbProperties(cbxCommunicationWay);
        LafManager.getInstance().setCmbProperties(cbxGender);
        LafManager.getInstance().setCmbProperties(cbxIdType);
        LafManager.getInstance().setCmbProperties(cbxPartyRoleTypes);


//    LABELS    
        LafManager.getInstance().setLabProperties(labAddress);
        LafManager.getInstance().setLabProperties(labAlias);
        LafManager.getInstance().setLabProperties(labEmail);
        LafManager.getInstance().setLabProperties(labFatherFirstName);
        LafManager.getInstance().setLabProperties(labFatherLastName);
        LafManager.getInstance().setLabProperties(labFax);
        LafManager.getInstance().setLabProperties(labIdType);
        LafManager.getInstance().setLabProperties(labIdref);
        LafManager.getInstance().setLabProperties(labLastName);
        LafManager.getInstance().setLabProperties(labMobile);
        LafManager.getInstance().setLabProperties(labName);
        LafManager.getInstance().setLabProperties(labPhone);
        LafManager.getInstance().setLabProperties(labPreferredWay);
        LafManager.getInstance().setLabProperties(lblGender);


//    TXT FIELDS
        LafManager.getInstance().setTxtProperties(txtAddress);
        LafManager.getInstance().setTxtProperties(txtAlias);
        LafManager.getInstance().setTxtProperties(txtEmail);
        LafManager.getInstance().setTxtProperties(txtFatherFirstName);
        LafManager.getInstance().setTxtProperties(txtFatherLastName);
        LafManager.getInstance().setTxtProperties(txtFax);
        LafManager.getInstance().setTxtProperties(txtFirstName);
        LafManager.getInstance().setTxtProperties(txtIdref);
        LafManager.getInstance().setTxtProperties(txtLastName);
        LafManager.getInstance().setTxtProperties(txtMobile);
        LafManager.getInstance().setTxtProperties(txtPhone);


//    TABBED PANELS
        LafManager.getInstance().setTabProperties(detailsPanel);

    }

    /** 
     * Enables or disables "add", depending on selection in the list of role 
     * types and user rights. 
     */
    private void customizeAddRoleButton(PartyRoleTypeBean partyRoleType) {
        btnAddRole.getAction().setEnabled(partyRoleType != null && !readOnly);
    }

    /** 
     * Enables or disables "remove" and "view" buttons for roles, depending on 
     * selection in the roles list and user rights. 
     */
    private void customizeRoleButtons(PartyRoleBean partyRole) {
        btnRemoveRole.getAction().setEnabled(partyRole != null && !readOnly);
    }

    /** Applies post initialization settings. */
    private void customizePanel() {
        if (partyBean.isNew()) {
            switchPartyType(true);
            individualButton.setSelected(true);
        } else {
            if (partyBean.getTypeCode() != null
                    && partyBean.getTypeCode().equals(entityString)) {
                entityButton.setSelected(true);
                labName.setText(entityLabel);
                enableIndividualFields(false);
            } else {
                individualButton.setSelected(true);
                labName.setText(individualLabel);
                enableIndividualFields(true);
            }
        }

        if (readOnly) {
            individualButton.setEnabled(false);
            entityButton.setEnabled(false);
            txtFirstName.setEnabled(false);
            txtLastName.setEnabled(false);
            cbxCommunicationWay.setEnabled(false);
            cbxIdType.setEnabled(false);
            cbxGender.setEnabled(false);
            cbxPartyRoleTypes.setEnabled(false);
            txtIdref.setEnabled(false);
            txtAddress.setEnabled(false);
            btnAddRole.getAction().setEnabled(false);
            btnRemoveRole.getAction().setEnabled(false);
            txtFatherFirstName.setEnabled(false);
            txtFatherLastName.setEnabled(false);
            txtAlias.setEnabled(false);
            txtPhone.setEnabled(false);
            txtFax.setEnabled(false);
            txtEmail.setEnabled(false);
            txtMobile.setEnabled(false);
            btnSave.setEnabled(false);
        }
    }

    /** Switch individual and entity type */
    private void switchPartyType(boolean isIndividual) {
        if (isIndividual) {
            partyBean.setTypeCode(individualString);
            labName.setText(individualLabel);
        } else {
            partyBean.setTypeCode(entityString);
            labName.setText(entityLabel);
        }
        cbxGender.setSelectedIndex(0);
        cbxIdType.setSelectedIndex(0);
        cbxCommunicationWay.setSelectedIndex(0);
        partyBean.setPreferredCommunication(null);
        partyBean.setName(null);
        partyBean.setLastName(null);
        partyBean.setFathersName(null);
        partyBean.setFathersLastName(null);
        partyBean.setAlias(null);
        partyBean.setIdNumber(null);
        partyBean.setGenderCode(null);
        partyBean.setIdType(null);
        enableIndividualFields(isIndividual);
    }

    private void enableIndividualFields(boolean enable) {
        txtLastName.setEnabled(enable);
        txtFatherFirstName.setEnabled(enable);
        txtFatherLastName.setEnabled(enable);
        txtAlias.setEnabled(enable);
        cbxGender.setEnabled(enable);
        cbxIdType.setEnabled(enable);
        txtIdref.setEnabled(enable);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        communicationTypes = createCommunicationTypes();
        idTypes = createIdTypes();
        partyRoleTypes = new org.sola.clients.beans.referencedata.PartyRoleTypeListBean();
        buttonGroup1 = new javax.swing.ButtonGroup();
        popupRoles = new javax.swing.JPopupMenu();
        menuRemoveRole = new javax.swing.JMenuItem();
        genderTypes = createGenderTypes();
        detailsPanel = new javax.swing.JTabbedPane();
        basicPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        roleTableScrollPanel = new javax.swing.JScrollPane();
        tablePartyRole = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar1 = new javax.swing.JToolBar();
        cbxPartyRoleTypes = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        btnAddRole = new javax.swing.JButton();
        btnRemoveRole = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        labName = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        labLastName = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        labIdType = new javax.swing.JLabel();
        cbxIdType = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        labIdref = new javax.swing.JLabel();
        txtIdref = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        lblGender = new javax.swing.JLabel();
        cbxGender = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        labAddress = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        fullPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        labFatherFirstName = new javax.swing.JLabel();
        txtFatherFirstName = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        labFatherLastName = new javax.swing.JLabel();
        txtFatherLastName = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        labAlias = new javax.swing.JLabel();
        txtAlias = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        labPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        txtMobile = new javax.swing.JTextField();
        labMobile = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        txtFax = new javax.swing.JTextField();
        labFax = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        labPreferredWay = new javax.swing.JLabel();
        cbxCommunicationWay = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        txtEmail = new javax.swing.JTextField();
        labEmail = new javax.swing.JLabel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        entityButton = new javax.swing.JRadioButton();
        individualButton = new javax.swing.JRadioButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        buttonGroup1.add(individualButton);
        buttonGroup1.add(entityButton);

        popupRoles.setName("popupRoles"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(PartyPanel.class, this);
        menuRemoveRole.setAction(actionMap.get("removeRole")); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/party/Bundle"); // NOI18N
        menuRemoveRole.setText(bundle.getString("PartyPanel.menuRemoveRole.text")); // NOI18N
        menuRemoveRole.setName("menuRemoveRole"); // NOI18N
        popupRoles.add(menuRemoveRole);

        setMinimumSize(new java.awt.Dimension(300, 425));
        setName("Form"); // NOI18N

        detailsPanel.setFont(new java.awt.Font("Thaoma", 0, 12));
        detailsPanel.setName("detailsPanel"); // NOI18N

        basicPanel.setName("basicPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        roleTableScrollPanel.setName("roleTableScrollPanel"); // NOI18N

        tablePartyRole.setComponentPopupMenu(popupRoles);
        tablePartyRole.setFont(new java.awt.Font("Thaoma", 0, 12));
        tablePartyRole.setName("tablePartyRole"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partyBean.filteredRoleList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tablePartyRole, "rolesGroup");
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${role.displayValue}"));
        columnBinding.setColumnName("Role.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.selectedRole}"), tablePartyRole, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        roleTableScrollPanel.setViewportView(tablePartyRole);
        tablePartyRole.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PartyPanel.tablePartyRole.columnModel.title0_1")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        cbxPartyRoleTypes.setFont(new java.awt.Font("Thaoma", 0, 12));
        cbxPartyRoleTypes.setName("cbxPartyRoleTypes"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partyRoleTypeList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partyRoleTypes, eLProperty, cbxPartyRoleTypes);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partyRoleTypes, org.jdesktop.beansbinding.ELProperty.create("${selectedPartyRoleType}"), cbxPartyRoleTypes, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jToolBar1.add(cbxPartyRoleTypes);

        filler1.setName("filler1"); // NOI18N
        jToolBar1.add(filler1);

        btnAddRole.setAction(actionMap.get("addRole")); // NOI18N
        btnAddRole.setFont(new java.awt.Font("Tahoma", 0, 12));
        btnAddRole.setText(bundle.getString("PartyPanel.btnAddRole.text")); // NOI18N
        btnAddRole.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddRole.setName("btnAddRole"); // NOI18N
        jToolBar1.add(btnAddRole);

        btnRemoveRole.setAction(actionMap.get("removeRole")); // NOI18N
        btnRemoveRole.setFont(new java.awt.Font("Tahoma", 0, 12));
        btnRemoveRole.setText(bundle.getString("PartyPanel.btnRemoveRole.text")); // NOI18N
        btnRemoveRole.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveRole.setName("btnRemoveRole"); // NOI18N
        jToolBar1.add(btnRemoveRole);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
            .addComponent(roleTableScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roleTableScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
        );

        jPanel10.setName("jPanel10"); // NOI18N
        jPanel10.setLayout(new java.awt.GridLayout(3, 2, 15, 0));

        jPanel4.setName("jPanel4"); // NOI18N

        labName.setFont(new java.awt.Font("Tahoma", 0, 12));
        labName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        labName.setText(bundle.getString("ApplicationForm.labName.text")); // NOI18N
        labName.setIconTextGap(1);
        labName.setName("labName"); // NOI18N

        txtFirstName.setFont(new java.awt.Font("Nepali", 0, 12));
        txtFirstName.setDisabledTextColor(new java.awt.Color(240, 240, 240));
        txtFirstName.setName("txtFirstName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.name}"), txtFirstName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtFirstName.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFirstName.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(labName, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel4);

        jPanel5.setName("jPanel5"); // NOI18N

        labLastName.setFont(new java.awt.Font("Tahoma", 0, 12));
        labLastName.setText(bundle.getString("ApplicationForm.labLastName.text")); // NOI18N
        labLastName.setIconTextGap(1);
        labLastName.setName("labLastName"); // NOI18N

        txtLastName.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtLastName.setName("txtLastName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.lastName}"), txtLastName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtLastName.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtLastName.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(labLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel5);

        jPanel6.setName("jPanel6"); // NOI18N

        labIdType.setFont(new java.awt.Font("Tahoma", 0, 12));
        labIdType.setText(bundle.getString("PartyPanel.labIdType.text")); // NOI18N
        labIdType.setName("labIdType"); // NOI18N

        cbxIdType.setBackground(new java.awt.Color(226, 244, 224));
        cbxIdType.setFont(new java.awt.Font("Thaoma", 0, 12));
        cbxIdType.setName("cbxIdType"); // NOI18N
        cbxIdType.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${idTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, idTypes, eLProperty, cbxIdType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.idType}"), cbxIdType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labIdType, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(cbxIdType, 0, 232, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(labIdType, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxIdType, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel6);

        jPanel7.setName("jPanel7"); // NOI18N

        labIdref.setFont(new java.awt.Font("Tahoma", 0, 12));
        labIdref.setText(bundle.getString("PartyPanel.labIdref.text")); // NOI18N
        labIdref.setName("labIdref"); // NOI18N

        txtIdref.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtIdref.setName("txtIdref"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.idNumber}"), txtIdref, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labIdref, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(txtIdref, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(labIdref, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdref, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel7);

        jPanel8.setName("jPanel8"); // NOI18N

        lblGender.setFont(new java.awt.Font("Tahoma", 0, 12));
        lblGender.setText(bundle.getString("PartyPanel.labGender.text")); // NOI18N
        lblGender.setToolTipText(bundle.getString("PartyPanel.labGender.toolTipText")); // NOI18N
        lblGender.setName("labGender"); // NOI18N

        cbxGender.setBackground(new java.awt.Color(226, 244, 224));
        cbxGender.setFont(new java.awt.Font("Thaoma", 0, 12));
        cbxGender.setName("cbxGender"); // NOI18N
        cbxGender.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${genderTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, genderTypes, eLProperty, cbxGender);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.genderType}"), cbxGender, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblGender, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(cbxGender, 0, 232, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(lblGender, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxGender, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel8);

        jPanel9.setName("jPanel9"); // NOI18N

        labAddress.setFont(new java.awt.Font("Tahoma", 0, 12));
        labAddress.setText(bundle.getString("PartyPanel.labAddress.text")); // NOI18N
        labAddress.setName("labAddress"); // NOI18N

        txtAddress.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtAddress.setName("txtAddress"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.address.description}"), txtAddress, org.jdesktop.beansbinding.BeanProperty.create("text"), "");
        bindingGroup.addBinding(binding);

        txtAddress.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtAddress.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
            .addComponent(txtAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(labAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.add(jPanel9);

        groupPanel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        groupPanel1.setName("groupPanel1"); // NOI18N
        groupPanel1.setTitleText(bundle.getString("PartyPanel.groupPanel1.titleText")); // NOI18N

        javax.swing.GroupLayout basicPanelLayout = new javax.swing.GroupLayout(basicPanel);
        basicPanel.setLayout(basicPanelLayout);
        basicPanelLayout.setHorizontalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addComponent(groupPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
                .addContainerGap())
        );
        basicPanelLayout.setVerticalGroup(
            basicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        detailsPanel.addTab(bundle.getString("PartyPanel.basicPanel.TabConstraints.tabTitle"), basicPanel); // NOI18N

        fullPanel.setName("fullPanel"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.GridLayout(2, 2, 15, 0));

        jPanel2.setName("jPanel2"); // NOI18N

        labFatherFirstName.setFont(new java.awt.Font("Tahoma", 0, 12));
        labFatherFirstName.setText(bundle.getString("PartyPanel.labFatherFirstName.text")); // NOI18N
        labFatherFirstName.setName("labFatherFirstName"); // NOI18N

        txtFatherFirstName.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtFatherFirstName.setName("txtFatherFirstName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.fathersName}"), txtFatherFirstName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(labFatherFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
            .addComponent(txtFatherFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(labFatherFirstName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFatherFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2);

        jPanel17.setName("jPanel17"); // NOI18N

        labFatherLastName.setFont(new java.awt.Font("Tahoma", 0, 12));
        labFatherLastName.setText(bundle.getString("PartyPanel.labFatherLastName.text")); // NOI18N
        labFatherLastName.setName("labFatherLastName"); // NOI18N

        txtFatherLastName.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtFatherLastName.setName("txtFatherLastName"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.fathersLastName}"), txtFatherLastName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(labFatherLastName)
                .addContainerGap(132, Short.MAX_VALUE))
            .addComponent(txtFatherLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(labFatherLastName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFatherLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel17);

        jPanel16.setName("jPanel16"); // NOI18N

        labAlias.setFont(new java.awt.Font("Tahoma", 0, 12));
        labAlias.setText(bundle.getString("PartyPanel.labAlias.text")); // NOI18N
        labAlias.setName("labAlias"); // NOI18N

        txtAlias.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtAlias.setName("txtAlias"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.alias}"), txtAlias, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(labAlias)
                .addContainerGap(205, Short.MAX_VALUE))
            .addComponent(txtAlias, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(labAlias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAlias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel16);

        jPanel18.setName("jPanel18"); // NOI18N
        jPanel18.setLayout(new java.awt.GridLayout(3, 2, 15, 3));

        jPanel11.setName("jPanel11"); // NOI18N

        labPhone.setFont(new java.awt.Font("Tahoma", 0, 12));
        labPhone.setText(bundle.getString("PartyPanel.labPhone.text")); // NOI18N
        labPhone.setName("labPhone"); // NOI18N

        txtPhone.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtPhone.setMaximumSize(new java.awt.Dimension(15, 2147483647));
        txtPhone.setName("txtPhone"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.phone}"), txtPhone, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtPhone.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtPhone.setHorizontalAlignment(JTextField.LEADING);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(labPhone)
                .addContainerGap(193, Short.MAX_VALUE))
            .addComponent(txtPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(labPhone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel11);

        jPanel12.setName("jPanel12"); // NOI18N

        txtMobile.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtMobile.setName("txtMobile"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.mobile}"), txtMobile, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        labMobile.setFont(new java.awt.Font("Tahoma", 0, 12));
        labMobile.setText(bundle.getString("PartyPanel.labMobile.text")); // NOI18N
        labMobile.setName("labMobile"); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(labMobile)
                .addContainerGap(194, Short.MAX_VALUE))
            .addComponent(txtMobile, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(labMobile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel12);

        jPanel13.setName("jPanel13"); // NOI18N

        txtFax.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtFax.setName("txtFax"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.fax}"), txtFax, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtFax.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFax.setHorizontalAlignment(JTextField.LEADING);

        labFax.setFont(new java.awt.Font("Tahoma", 0, 12));
        labFax.setText(bundle.getString("PartyPanel.labFax.text")); // NOI18N
        labFax.setName("labFax"); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(labFax)
                .addContainerGap(210, Short.MAX_VALUE))
            .addComponent(txtFax, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(labFax)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel13);

        jPanel15.setName("jPanel15"); // NOI18N

        labPreferredWay.setFont(new java.awt.Font("Tahoma", 0, 12));
        labPreferredWay.setText(bundle.getString("PartyPanel.labPreferredWay.text")); // NOI18N
        labPreferredWay.setName("labPreferredWay"); // NOI18N

        cbxCommunicationWay.setBackground(new java.awt.Color(226, 244, 224));
        cbxCommunicationWay.setFont(new java.awt.Font("Thaoma", 0, 12));
        cbxCommunicationWay.setName("cbxCommunicationWay"); // NOI18N
        cbxCommunicationWay.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${communicationTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, communicationTypes, eLProperty, cbxCommunicationWay);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.preferredCommunication}"), cbxCommunicationWay, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxCommunicationWay.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(labPreferredWay, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
            .addComponent(cbxCommunicationWay, 0, 232, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(labPreferredWay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxCommunicationWay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel15);

        jPanel14.setName("jPanel14"); // NOI18N

        txtEmail.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtEmail.setName("txtEmail"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${partyBean.email}"), txtEmail, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtEmail.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtEmail.setHorizontalAlignment(JTextField.LEADING);

        labEmail.setFont(new java.awt.Font("Tahoma", 0, 12));
        labEmail.setText(bundle.getString("PartyPanel.labEmail.text")); // NOI18N
        labEmail.setName("labEmail"); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(labEmail)
                .addContainerGap(197, Short.MAX_VALUE))
            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(labEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel14);

        groupPanel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        groupPanel2.setName("groupPanel2"); // NOI18N
        groupPanel2.setTitleText(bundle.getString("PartyPanel.groupPanel2.titleText")); // NOI18N

        javax.swing.GroupLayout fullPanelLayout = new javax.swing.GroupLayout(fullPanel);
        fullPanel.setLayout(fullPanelLayout);
        fullPanelLayout.setHorizontalGroup(
            fullPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fullPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fullPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
                .addContainerGap())
        );
        fullPanelLayout.setVerticalGroup(
            fullPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fullPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        detailsPanel.addTab(bundle.getString("PartyPanel.fullPanel.TabConstraints.tabTitle"), fullPanel); // NOI18N

        buttonGroup1.add(entityButton);
        entityButton.setFont(new java.awt.Font("Tahoma", 0, 12));
        entityButton.setText(bundle.getString("PartyPanel.entityButton.text")); // NOI18N
        entityButton.setActionCommand(bundle.getString("PartyPanel.entityButton.actionCommand")); // NOI18N
        entityButton.setName("entityButton"); // NOI18N
        entityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entityButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(individualButton);
        individualButton.setFont(new java.awt.Font("Tahoma", 0, 12));
        individualButton.setText(bundle.getString("PartyPanel.individualButton.text")); // NOI18N
        individualButton.setActionCommand(bundle.getString("PartyPanel.individualButton.actionCommand")); // NOI18N
        individualButton.setName("individualButton"); // NOI18N
        individualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                individualButtonActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 12));
        btnSave.setText(bundle.getString("PartyPanel.btnSave.text")); // NOI18N
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 12));
        btnCancel.setText(bundle.getString("PartyPanel.btnCancel.text")); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(individualButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entityButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(293, Short.MAX_VALUE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(individualButton)
                    .addComponent(entityButton))
                .addGap(7, 7, 7)
                .addComponent(detailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (partyBean.validate(true).size() < 1) {
            if (this.savePartyOnAction) {
                partyBean.saveParty();
            }
            if (saveEventToFire.equals(CREATED_PARTY) && !closeOnCreate) {
                setupPartyBean(null);
            }
            firePropertyChange(saveEventToFire, null, partyBean);
        }
}//GEN-LAST:event_btnSaveActionPerformed

    private void individualButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_individualButtonActionPerformed
        if (individualButton.isSelected()) {
            switchPartyType(true);
        }
    }//GEN-LAST:event_individualButtonActionPerformed

    private void entityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entityButtonActionPerformed
        if (entityButton.isSelected()) {
            switchPartyType(false);
        }
    }//GEN-LAST:event_entityButtonActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        firePropertyChange(CANCEL_ACTION, false, true);
    }//GEN-LAST:event_btnCancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel basicPanel;
    private javax.swing.JButton btnAddRole;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemoveRole;
    private javax.swing.JButton btnSave;
    private javax.swing.ButtonGroup buttonGroup1;
    public javax.swing.JComboBox cbxCommunicationWay;
    public javax.swing.JComboBox cbxGender;
    public javax.swing.JComboBox cbxIdType;
    private javax.swing.JComboBox cbxPartyRoleTypes;
    private org.sola.clients.beans.referencedata.CommunicationTypeListBean communicationTypes;
    private javax.swing.JTabbedPane detailsPanel;
    private javax.swing.JRadioButton entityButton;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel fullPanel;
    private org.sola.clients.beans.referencedata.GenderTypeListBean genderTypes;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.beans.referencedata.IdTypeListBean idTypes;
    private javax.swing.JRadioButton individualButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labAddress;
    private javax.swing.JLabel labAlias;
    private javax.swing.JLabel labEmail;
    private javax.swing.JLabel labFatherFirstName;
    private javax.swing.JLabel labFatherLastName;
    private javax.swing.JLabel labFax;
    private javax.swing.JLabel labIdType;
    private javax.swing.JLabel labIdref;
    private javax.swing.JLabel labLastName;
    private javax.swing.JLabel labMobile;
    private javax.swing.JLabel labName;
    private javax.swing.JLabel labPhone;
    private javax.swing.JLabel labPreferredWay;
    private javax.swing.JLabel lblGender;
    private javax.swing.JMenuItem menuRemoveRole;
    private org.sola.clients.beans.referencedata.PartyRoleTypeListBean partyRoleTypes;
    private javax.swing.JPopupMenu popupRoles;
    private javax.swing.JScrollPane roleTableScrollPanel;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tablePartyRole;
    public javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAlias;
    public javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFatherFirstName;
    private javax.swing.JTextField txtFatherLastName;
    public javax.swing.JTextField txtFax;
    public javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtIdref;
    public javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMobile;
    public javax.swing.JTextField txtPhone;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    @Action
    public void addRole() {
        if (partyRoleTypes.getSelectedPartyRoleType() == null) {
            MessageUtility.displayMessage(ClientMessage.PARTY_SELECT_ROLE);
            return;
        } else {
            if (partyBean.checkRoleExists(partyRoleTypes.getSelectedPartyRoleType())) {
                MessageUtility.displayMessage(ClientMessage.PARTY_ALREADYSELECTED_ROLE);
                return;
            }
        }
        partyBean.addRole(partyRoleTypes.getSelectedPartyRoleType());
    }

    @Action
    public void removeRole() {
        partyBean.removeSelectedRole();
    }
}