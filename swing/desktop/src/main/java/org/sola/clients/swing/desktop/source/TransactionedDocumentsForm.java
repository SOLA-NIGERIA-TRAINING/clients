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
package org.sola.clients.swing.desktop.source;

import org.sola.clients.swing.ui.source.DocumentSearchPanel;
import org.sola.clients.swing.ui.source.DocumentsPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import javax.swing.ImageIcon;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.beans.source.SourceSearchResultBean;
import org.sola.clients.beans.source.SourceSummaryBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * This form is used to manage transaction-driven documents. 
 * Document can be attached to transaction or detached from it.
 */
public class TransactionedDocumentsForm extends ContentPanel {

    private ApplicationBean appBean;
    private ApplicationServiceBean appService;

    /** Creates new form TransactionedDocumentsForm */
    public TransactionedDocumentsForm(ApplicationBean appBean, ApplicationServiceBean appService) {
        this.appBean = appBean;
        this.appService = appService;
        initComponents();
    
        postInit();
    }

    private void postInit() {
        
        
        customizeComponents();
        
        String serviceName = "";
        String applicationNr = "";
        
        if (appService != null) {
            selectedDocumentsPanel.getSourceListBean().loadSourceByService(appService.getId());
            serviceName = appService.getRequestType().getDisplayValue();
        }
        if (appBean != null) {
            applicationDocumentsPanel.setSourceList(appBean.getSourceList());
            applicationNr = appBean.getNr();
        }
        
        headerPanel.setTitleText(MessageFormat.format(headerPanel.getTitleText(), applicationNr, serviceName));
        groupSelectedDocuments.setTitleText(MessageFormat.format(groupSelectedDocuments.getTitleText(), serviceName));
        
        customizeRemoveButton(null);
        customizeAddFromSearchButton(null);
        customizeAddFromApplicationButton(null);
        
        selectedDocumentsPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(DocumentsPanel.SELECTED_SOURCE)){
                    customizeRemoveButton((SourceBean)evt.getNewValue());
                }
            }
        });
        
        applicationDocumentsPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(DocumentsPanel.SELECTED_SOURCE)){
                    customizeAddFromApplicationButton((SourceBean)evt.getNewValue());
                }
            }
        });
        
        documentSeachPanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(DocumentSearchPanel.SELECTED_SOURCE)){
                    customizeAddFromSearchButton((SourceSearchResultBean)evt.getNewValue());
                }
            }
        });
    }
    
    
     /** Applies customization of component L&F. */
    private void customizeComponents() {
     
//    BUTTONS   
    LafManager.getInstance().setBtnProperties(btnAddDocumentFromApplication);
    LafManager.getInstance().setBtnProperties(btnAddDocumentFromSearch);
    LafManager.getInstance().setBtnProperties(btnRemove);
    
//    TABBED PANELS
     LafManager.getInstance().setTabProperties(jTabbedPane1);
   
    }
    
    
    private void customizeRemoveButton(SourceBean source){
        if(source == null){
            btnRemove.setEnabled(false);
        } else {
            btnRemove.setEnabled(SecurityBean.isInRole(RolesConstants.SOURCE_TRANSACTIONAL));
        }
    }
    
    private void customizeAddFromApplicationButton(SourceBean source){
        if(source == null){
            btnAddDocumentFromApplication.setEnabled(false);
        } else {
            btnAddDocumentFromApplication.setEnabled(SecurityBean.isInRole(RolesConstants.SOURCE_TRANSACTIONAL));
        }
    }
    
    private void customizeAddFromSearchButton(SourceSearchResultBean source){
        if(source == null){
            btnAddDocumentFromSearch.setEnabled(false);
        } else {
            btnAddDocumentFromSearch.setEnabled(SecurityBean.isInRole(RolesConstants.SOURCE_TRANSACTIONAL));
        }
    }
    
    private void attachToTransaction(SourceSummaryBean sourceToAttach){
        if(sourceToAttach != null && appService !=null){
            SourceBean attachedSource = SourceBean.attachToTransaction(sourceToAttach.getId(), appService.getId());
            if(attachedSource!=null){
                selectedDocumentsPanel.getSourceListBean().getSourceBeanList().add(attachedSource);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        selectedDocumentsPanel = new org.sola.clients.swing.ui.source.DocumentsPanel();
        btnRemove = new javax.swing.JButton();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        applicationDocumentsPanel = new org.sola.clients.swing.ui.source.DocumentsPanel();
        btnAddDocumentFromApplication = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        documentSeachPanel = new org.sola.clients.swing.ui.source.DocumentSearchPanel();
        btnAddDocumentFromSearch = new javax.swing.JButton();
        groupSelectedDocuments = new org.sola.clients.swing.ui.GroupPanel();

        setCloseOnHide(true);
        setHeaderPanel(headerPanel);
        setName("Form"); // NOI18N

        headerPanel.setName("headerPanel"); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(TransactionedDocumentsForm.class);
        headerPanel.setTitleText(resourceMap.getString("headerPanel.titleText")); // NOI18N

        jScrollPane1.setBorder(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        selectedDocumentsPanel.setName("selectedDocumentsPanel"); // NOI18N

        btnRemove.setText(resourceMap.getString("btnRemove.text")); // NOI18N
        btnRemove.setName("btnRemove"); // NOI18N
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        groupPanel1.setName("groupPanel1"); // NOI18N
        groupPanel1.setTitleText(resourceMap.getString("groupPanel1.titleText")); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        applicationDocumentsPanel.setName("applicationDocumentsPanel"); // NOI18N

        btnAddDocumentFromApplication.setText(resourceMap.getString("btnAddDocumentFromApplication.text")); // NOI18N
        btnAddDocumentFromApplication.setName("btnAddDocumentFromApplication"); // NOI18N
        btnAddDocumentFromApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDocumentFromApplicationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(applicationDocumentsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                    .addComponent(btnAddDocumentFromApplication, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applicationDocumentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddDocumentFromApplication)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        documentSeachPanel.setName("documentSeachPanel"); // NOI18N
        documentSeachPanel.setShowPrintButton(false);

        btnAddDocumentFromSearch.setText(resourceMap.getString("btnAddDocumentFromSearch.text")); // NOI18N
        btnAddDocumentFromSearch.setName("btnAddDocumentFromSearch"); // NOI18N
        btnAddDocumentFromSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDocumentFromSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddDocumentFromSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(documentSeachPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(documentSeachPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddDocumentFromSearch)
                .addContainerGap())
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        groupSelectedDocuments.setName("groupSelectedDocuments"); // NOI18N
        groupSelectedDocuments.setTitleText(resourceMap.getString("groupSelectedDocuments.titleText")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(selectedDocumentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
            .addComponent(groupSelectedDocuments, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(groupSelectedDocuments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(selectedDocumentsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        SourceBean selectedSource = selectedDocumentsPanel.getSourceListBean().getSelectedSource();
        if(selectedSource != null && MessageUtility.displayMessage(ClientMessage
                .SOURCE_DETACH_TRANSACTION_WARNING) == MessageUtility.BUTTON_ONE){
            if(SourceBean.detachFromTransaction(selectedSource.getId())){
                selectedDocumentsPanel.getSourceListBean().removeSelectedSource();
            }
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddDocumentFromApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDocumentFromApplicationActionPerformed
        if(MessageUtility.displayMessage(ClientMessage
                .SOURCE_ATTACH_TRANSACTION_WARNING) == MessageUtility.BUTTON_ONE){
            attachToTransaction(applicationDocumentsPanel.getSourceListBean().getSelectedSource());
        }
    }//GEN-LAST:event_btnAddDocumentFromApplicationActionPerformed

    private void btnAddDocumentFromSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDocumentFromSearchActionPerformed
        if(MessageUtility.displayMessage(ClientMessage
                .SOURCE_ATTACH_TRANSACTION_WARNING) == MessageUtility.BUTTON_ONE){
            attachToTransaction(documentSeachPanel.getSelectedSource());
        }
    }//GEN-LAST:event_btnAddDocumentFromSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.source.DocumentsPanel applicationDocumentsPanel;
    private javax.swing.JButton btnAddDocumentFromApplication;
    private javax.swing.JButton btnAddDocumentFromSearch;
    private javax.swing.JButton btnRemove;
    private org.sola.clients.swing.ui.source.DocumentSearchPanel documentSeachPanel;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupSelectedDocuments;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private org.sola.clients.swing.ui.source.DocumentsPanel selectedDocumentsPanel;
    // End of variables declaration//GEN-END:variables
}
