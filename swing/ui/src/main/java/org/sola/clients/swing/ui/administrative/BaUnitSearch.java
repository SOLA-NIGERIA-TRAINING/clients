/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.ui.administrative;

import org.sola.clients.swing.common.controls.FreeTextSearch;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultListModel;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.AdministrativeClient;

/**
 *
 * @author RizzoM
 */
public class BaUnitSearch extends FreeTextSearch {

    private SolaTask searchTask = null;

    public BaUnitSearch() {
        super();
        this.setMinimalSearchStringLength(3);
        setRefreshTextInSelection(true);
    }

    @Override
    public void onNewSearchString(final String searchString, final DefaultListModel listModel) {
        firePropertyChange(ELEMENT_SELECTED, false, true);
        
        // Check if a search is currently running and if so, cancel it
        if (searchTask != null && TaskManager.getInstance().isTaskRunning(searchTask.getId())) {
            TaskManager.getInstance().removeTask(searchTask);
        }

        // Use a SolaTask to make the search much smoother. 
        final List<BaUnitBean> searchResult = new LinkedList<BaUnitBean>();
        
        listModel.clear();
        searchTask = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                // Perform the search on a background thread
                setMessage(MessageUtility.getLocalizedMessage(
                        ClientMessage.PROGRESS_MSG_BA_UNIT_GETTING,
                        new String[]{""}).getMessage());
                try {
                    // Allow a small delay on the background thread so that the thread can be cancelled
                    // before executing the search if the user is still typing. 
                    Thread.sleep(500);
                    TypeConverters.TransferObjectListToBeanList(
                            WSManager.getInstance().getAdministrative().getBaUnitsByString(searchString),
                            BaUnitBean.class, (List) searchResult);
                } catch (InterruptedException ex) {
                }
                return null;
            }

            @Override
            public void taskDone() {
//                // Update the GUI using the primary EDT thread
                String oldValue = "begin";
                String newValue;
                if (searchResult.size() > 0) {
                    for (BaUnitBean baunitObject : searchResult) {
                        newValue = baunitObject.getNameFirstpart()+" "+baunitObject.getNameLastpart();
                        if (!newValue.contains(oldValue)) {
                           listModel.addElement(newValue);
                        }
                        oldValue = baunitObject.getNameFirstpart()+" "+baunitObject.getNameLastpart();
                    }
                 }
                   
            }
        };
        TaskManager.getInstance().runTask(searchTask);
    }
}
