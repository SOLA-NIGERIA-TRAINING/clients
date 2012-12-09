/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.beans;

import java.util.List;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.transaction.TransactionBulkOperationSpatialTO;

/**
 *
 * @author Elton Manoku
 */
public class TransactionBulkOperationSpatial extends AbstractIdBean {
    
    private boolean generateFirstPart = false;
    private List<SpatialUnitTemporaryBean> spatialUnitTemporaryList;

    public TransactionBulkOperationSpatial(){
        super();
    }

    public boolean isGenerateFirstPart() {
        return generateFirstPart;
    }

    public void setGenerateFirstPart(boolean generateFirstPart) {
        this.generateFirstPart = generateFirstPart;
    }
    
    public List<SpatialUnitTemporaryBean> getSpatialUnitTemporaryList() {
        return spatialUnitTemporaryList;
    }

    public void setSpatialUnitTemporaryList(List<SpatialUnitTemporaryBean> spatialUnitTemporaryList) {
        this.spatialUnitTemporaryList = spatialUnitTemporaryList;
    }
    public TransactionBulkOperationSpatialTO getTO() {
        return TypeConverters.BeanToTrasferObject(this, TransactionBulkOperationSpatialTO.class);
    }
    
    public List<ValidationResultBean> save() {
        return TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getCadastreService().saveTransactionBulkOperationSpatial(
                this.getTO()), ValidationResultBean.class, null);
    }
    
    public boolean reject(){
        return WSManager.getInstance().getCadastreService().rejectTransactionBulkOperationSpatial(
                getTO());
    }
}