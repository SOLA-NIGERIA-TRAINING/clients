/*
 * Copyright 2012 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.beans.systematicregistration;

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.validation.ValidationResultBean;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.SysRegSigningListTO;

/**
 *
 * @author RizzoM
 */
public class SigningListListBean extends AbstractBindingBean {

    public static final String SELECTED_PARCEL_PROPERTY = "selectedParcels";
    private SolaObservableList<SigningListBean> parcels;
    private SigningListBean selectedParcels;

    public SigningListListBean() {
        super();
        parcels = new SolaObservableList<SigningListBean>();
    }

    public SolaObservableList<SigningListBean> getSigningList() {
        return parcels;
    }

    public SigningListBean getSelectedParcels() {
        return selectedParcels;
    }

    public void setSelectedParcels(SigningListBean selectedParcels) {
        SigningListBean oldValue = this.selectedParcels;
        this.selectedParcels = selectedParcels;
        propertySupport.firePropertyChange(SELECTED_PARCEL_PROPERTY, oldValue, this.selectedParcels);
    }

    /**
     * Returns collection of {@link ParcelNumberListingListBean} objects. This
     * method is used by Jasper report designer to extract properties of
     * application bean to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        SigningListListBean bean = new SigningListListBean();
        collection.add(bean);
        return collection;
    }

    //      /** Passes from date and to date search criteria. */
    public void passParameter(String params) {

        List<SysRegSigningListTO> sysRegListingTO =
                WSManager.getInstance().getAdministrative().getSysRegSigningList(params);

        TypeConverters.TransferObjectListToBeanList(sysRegListingTO,
                SigningListBean.class, (List) parcels);
    }
    
     /**
     * Withdraws application
     */
    public List<ValidationResultBean> publicDisplay(String params) {
        List<ValidationResultBean> result = TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getAdministrative().publicDisplay(params),
                ValidationResultBean.class, null);
       
        return result;
    }

}
