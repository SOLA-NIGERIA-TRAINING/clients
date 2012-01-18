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
package org.sola.clients.swing.ui.source;

import java.awt.ComponentOrientation;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import javax.swing.JTextField;
import org.sola.clients.swing.common.controls.BrowseControlListener;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.swing.ui.renderers.SimpleComboBoxRenderer;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.swing.common.LafManager;

/**
 * Document panel, used to create or update document. {@link SourceBean} is used
 * to bind data on the panel controls.
 */
public class DocumentPanel extends javax.swing.JPanel {

    public static final String UPDATED_SOURCE = "updatedSource";
    private boolean isNew = true;
    private boolean allowEditing = true;
    private String okButtonText="Add";

    private SourceBean createSource() {
        if (sourceBean == null) {
            sourceBean = new SourceBean();
        }
        return sourceBean;
    }

    public DocumentPanel(SourceBean sourceBean) {
        this.sourceBean = sourceBean;
        isNew = false;
        initComponents();
        postInit();
    }

    public DocumentPanel() {
        initComponents();
        postInit();
    }

    public boolean isAllowEditing() {
        return allowEditing;
    }

    public void setAllowEditing(boolean allowEditing) {
        this.allowEditing = allowEditing;
        customizeForm();
    }
    /** Applies customization of component L&F. */
    private void customizeComponents() {
       
//    BUTTONS   
    LafManager.getInstance().setBtnProperties(btnOk);
    
//    COMBOBOXES
    LafManager.getInstance().setCmbProperties(cbxDocType);
    
    
//    LABELS    
    LafManager.getInstance().setLabProperties(jLabel1);
    LafManager.getInstance().setLabProperties(jLabel2);
    LafManager.getInstance().setLabProperties(jLabel3);
    LafManager.getInstance().setLabProperties(jLabel4);
    
//    TXT FIELDS
    LafManager.getInstance().setTxtProperties(txtDocRefNumber);
  
//    FORMATTED TXT
    LafManager.getInstance().setFormattedTxtProperties(txtDocRecordDate);
    }

    /** Customizes form elements, based on the provided setting. */
    private void customizeForm(){
        cbxDocType.setEnabled(allowEditing);
        txtDocRecordDate.setEnabled(allowEditing);
        txtDocRefNumber.setEnabled(allowEditing);
        browseAttachment.setDisplayBrowseButton(allowEditing);
        browseAttachment.setDisplayDeleteButton(allowEditing);
        btnOk.setEnabled(allowEditing);
    }
    
    /** 
     * Makes post initialization tasks. Binds listener to the browse control, 
     * sets text of OK button.
     */
    private void postInit() {
        customizeComponents() ;
        btnOk.setText(okButtonText);
        cbxDocType.setSelectedIndex(-1);
        // Init browse attachment
        browseAttachment.addBrowseControlEventListener(new BrowseControlListener() {

            @Override
            public void deleteButtonClicked(MouseEvent e) {
                sourceBean.removeAttachment();
            }

            @Override
            public void browseButtonClicked(MouseEvent e) {
                openAttachmentForm();
            }

            @Override
            public void controlClicked(MouseEvent e) {
            }

            @Override
            public void textClicked(MouseEvent e) {
                DocumentBean.openDocument(sourceBean.getArchiveDocument().getId());
            }
        });
        customizeForm();
    }

    private void openAttachmentForm() {
        FileBrowserForm fileBrowser = new FileBrowserForm(null, true, FileBrowserForm.AttachAction.CLOSE_WINDOW);
        fileBrowser.setLocationRelativeTo(this);
        fileBrowser.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(FileBrowserForm.ATTACHED_DOCUMENT)) {
                    if (e.getNewValue() != null) {
                        DocumentBean document = (DocumentBean) e.getNewValue();
                        sourceBean.setArchiveDocument(document);
                    }
                }
            }
        });
        fileBrowser.setVisible(true);
    }

    private void clearFields() {
        sourceBean.clean();
        browseAttachment.setText(null);
        cbxDocType.setSelectedIndex(-1);
    }

    public String getOkButtonText() {
        return okButtonText;
    }

    public void setOkButtonText(String okButtonText) {
        this.okButtonText = okButtonText;
        btnOk.setText(okButtonText);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        sourceTypeListBean = new org.sola.clients.beans.source.SourceTypeListBean();
        sourceBean = createSource();
        txtDocRecordDate = new javax.swing.JFormattedTextField();
        txtDocRefNumber = new javax.swing.JTextField();
        cbxDocType = new javax.swing.JComboBox();
        btnOk = new javax.swing.JButton();
        browseAttachment = new org.sola.clients.swing.common.controls.BrowseControl();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        txtDocRecordDate.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtDocRecordDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtDocRecordDate.setName("txtDocRecordDate"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sourceBean, org.jdesktop.beansbinding.ELProperty.create("${recordation}"), txtDocRecordDate, org.jdesktop.beansbinding.BeanProperty.create("value"), "DocDate");
        bindingGroup.addBinding(binding);

        txtDocRecordDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtDocRecordDate.setHorizontalAlignment(JTextField.LEADING);

        txtDocRefNumber.setFont(new java.awt.Font("Thaoma", 0, 12));
        txtDocRefNumber.setName("txtDocRefNumber"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sourceBean, org.jdesktop.beansbinding.ELProperty.create("${referenceNr}"), txtDocRefNumber, org.jdesktop.beansbinding.BeanProperty.create("text"), "DocRef");
        bindingGroup.addBinding(binding);

        txtDocRefNumber.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtDocRefNumber.setHorizontalAlignment(JTextField.LEADING);

        cbxDocType.setFont(new java.awt.Font("Thaoma", 0, 12));
        cbxDocType.setLightWeightPopupEnabled(false);
        cbxDocType.setName("cbxDocType"); // NOI18N
        cbxDocType.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${sourceTypeList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sourceTypeListBean, eLProperty, cbxDocType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sourceBean, org.jdesktop.beansbinding.ELProperty.create("${sourceType}"), cbxDocType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"), "DocType");
        bindingGroup.addBinding(binding);

        cbxDocType.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/source/Bundle"); // NOI18N
        btnOk.setText(bundle.getString("DocumentPanel.btnOk.text")); // NOI18N
        btnOk.setName("btnOk"); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        browseAttachment.setName("browseAttachment"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, sourceBean, org.jdesktop.beansbinding.ELProperty.create("${archiveDocument.name}"), browseAttachment, org.jdesktop.beansbinding.BeanProperty.create("text"), "DocAttachment");
        bindingGroup.addBinding(binding);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setText(bundle.getString("DocumentPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText(bundle.getString("DocumentPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setText(bundle.getString("DocumentPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel4.setText(bundle.getString("DocumentPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(browseAttachment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxDocType, 0, 241, Short.MAX_VALUE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtDocRefNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDocRecordDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                            .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDocRefNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDocRecordDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxDocType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseAttachment, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOk)))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        if (sourceBean.validate(true).size() < 1) {
            SourceBean updatedSource;
            if (isNew) {
                updatedSource = sourceBean.copy();
                clearFields();
            } else {
                updatedSource = sourceBean;
            }
            firePropertyChange(UPDATED_SOURCE, null, updatedSource);
        }
    }//GEN-LAST:event_btnOkActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.controls.BrowseControl browseAttachment;
    private javax.swing.JButton btnOk;
    private javax.swing.JComboBox cbxDocType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private org.sola.clients.beans.source.SourceBean sourceBean;
    private org.sola.clients.beans.source.SourceTypeListBean sourceTypeListBean;
    private javax.swing.JFormattedTextField txtDocRecordDate;
    private javax.swing.JTextField txtDocRefNumber;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}