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
package org.sola.clients.swing.desktop.reports;

import java.awt.ComponentOrientation;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.clients.swing.ui.validation.ValidationResultForm;
import org.sola.common.FileUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author RizzoM
 */
public class SysRegSigningListForm extends javax.swing.JDialog {

    private String location;
    private String tmpLocation = "";
    private String report;
    private static String cachePath = System.getProperty("user.home") + "/sola/cache/documents/";
    private String reportdate;
    private String reportTogenerate;
    private SourceBean document;
    public final static String SELECTED_PARCEL = "selectedParcel";

    /**
     * Creates new form SysRegListingParamsForm
     */
    public SysRegSigningListForm(java.awt.Frame parent, boolean modal, String report) {
        super(parent, modal);
        initComponents();
        this.report = report;
        this.setTitle("Report " + report);
        this.document = new SourceBean();

    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setVisible(true);
        form.setAlwaysOnTop(true);
        try {
            postProcessReport(report);
        } catch (Exception ex) {
            Logger.getLogger(SysRegSigningListForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void postProcessReport(JasperPrint populatedReport) throws Exception {

        System.out.println("Inside postProcessReport");

        System.out.println("start download");

        String location = this.tmpLocation.replace(" ", "_");
        this.reportTogenerate = this.reportTogenerate.replace(" ", "_");
        this.reportTogenerate = this.reportTogenerate.replace("/", "_");

        JRPdfExporter exporterPdf = new JRPdfExporter();

        exporterPdf.setParameter(JRXlsExporterParameter.JASPER_PRINT, populatedReport);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporterPdf.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, cachePath + this.reportTogenerate);

        exporterPdf.exportReport();
        FileUtility.saveFileFromStream(null, this.reportTogenerate);

        System.out.println("End download");
        saveDocument(this.reportTogenerate, this.reportdate);
        FileUtility.deleteFileFromCache(this.reportTogenerate);

    }

    private void saveDocument(String fileName, String subDate) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date   recDate  = new Date ();
        String reportdate = formatter.format(recDate);
        
//        this.document.setTypeCode("publicNotification");
        this.document.setTypeCode("signingList");
        this.document.setReferenceNr(tmpLocation);
        this.document.setRecordation(recDate);
//        this.document.setExpirationDate(expDate);
        this.document.setDescription(this.reportTogenerate);


        DocumentBean document1 = new DocumentBean();
        File file = new File(cachePath + fileName);
        document1 = DocumentBean.createDocumentFromLocalFile(file);

        document.setArchiveDocument(document1);
        document.save();
    }

    private void showDocMessage(String fileName) {

        String params = fileName;
        MessageUtility.displayMessage(ClientMessage.SOURCE_SYS_REP_GENERATED, new Object[]{params});

    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourceBean = new org.sola.clients.beans.source.SourceBean();
        signingListListBean = new org.sola.clients.beans.systematicregistration.SigningListListBean();
        labHeader = new javax.swing.JLabel();
        viewReport = new javax.swing.JButton();
        labLocation = new javax.swing.JLabel();
        cadastreObjectSearch = new org.sola.clients.swing.ui.cadastre.LocationSearch();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labHeader.setBackground(new java.awt.Color(255, 153, 0));
        labHeader.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labHeader.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/reports/Bundle"); // NOI18N
        labHeader.setText(bundle.getString("SysRegSigningListForm.labHeader.text_1")); // NOI18N
        labHeader.setOpaque(true);

        viewReport.setText(bundle.getString("SysRegSigningListForm.viewReport.text_1")); // NOI18N
        viewReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportActionPerformed(evt);
            }
        });

        labLocation.setText(bundle.getString("SysRegSigningListForm.labLocation.text_1")); // NOI18N

        cadastreObjectSearch.setText(bundle.getString("SysRegSigningListForm.cadastreObjectSearch.text_1")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(227, 227, 227))
                            .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(viewReport)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labHeader)
                .addGap(79, 79, 79)
                .addComponent(labLocation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewReport)
                    .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(152, Short.MAX_VALUE))
        );

        labHeader.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labHeader.text")); // NOI18N
        viewReport.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.viewReport.text")); // NOI18N
        labLocation.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labLocation.text")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReportActionPerformed
        String reportRequested = "signingList";


        if (cadastreObjectSearch.getSelectedElement() != null) {
            this.firePropertyChange(SELECTED_PARCEL, null,
                    cadastreObjectSearch.getSelectedElement());
            this.location = cadastreObjectSearch.getSelectedElement().toString();
            tmpLocation = (this.location);

        } else {
            MessageUtility.displayMessage(ClientMessage.CHECK_SELECT_LOCATION);
            return;
        }
       


       
            Date currentdate = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
            this.reportdate = formatter.format(currentdate);
            this.reportTogenerate = this.report + "_" + tmpLocation + "_" + this.reportdate + ".pdf";
            this.reportTogenerate = this.reportTogenerate.replace(" ", "_");
            this.reportTogenerate = this.reportTogenerate.replace("/", "_");

            showDocMessage(this.reportTogenerate);

            if (this.report.contentEquals("signingList")) {
                signingListListBean.passParameter(tmpLocation);
                
                System.out.println("SIGNING LIST IN THE FORM  "+signingListListBean.getSigningList().get(0).getParcel());
                
                if (signingListListBean.getSigningList().isEmpty()) {
                    MessageUtility.displayMessage(ClientMessage.NO_REPORT_GENERATION);
                    return;
                } else {
                    List<ValidationResultBean> result = null;
                    result = signingListListBean.publicDisplay(tmpLocation);
                    String message = MessageUtility.getLocalizedMessage(
                            ClientMessage.REPORT_GENERATION_SUCCESS,
                            new String[]{signingListListBean.getSigningList().get(0).getNameLastpart()}).getMessage();
                    openValidationResultForm(result, true, message);

                    final String finalRequested = reportRequested;
                    SolaTask t = new SolaTask<Void, Void>() {

                        @Override
                        public Void doTask() {
                            setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_REPORT));

                            showReport(ReportManager.getSysRegSigningListReport(signingListListBean, tmpLocation, finalRequested));
                            return null;
                        }
                    };
                    TaskManager.getInstance().runTask(t);
                }
            }
            this.dispose();
       
    }//GEN-LAST:event_viewReportActionPerformed

    /**
     * Opens dialog form to display status change result for application or
     * service.
     */
    private void openValidationResultForm(List<ValidationResultBean> validationResultList,
            boolean isSuccess, String message) {
        ValidationResultForm resultForm = new ValidationResultForm(
                null, true, validationResultList, isSuccess, message);
        resultForm.setLocationRelativeTo(this);
        resultForm.setVisible(true);
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.cadastre.LocationSearch cadastreObjectSearch;
    private javax.swing.JLabel labHeader;
    private javax.swing.JLabel labLocation;
    private org.sola.clients.beans.systematicregistration.SigningListListBean signingListListBean;
    private org.sola.clients.beans.source.SourceBean sourceBean;
    private javax.swing.JButton viewReport;
    // End of variables declaration//GEN-END:variables
}
