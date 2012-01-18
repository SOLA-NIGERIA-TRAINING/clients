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
package org.sola.clients.swing.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Displays different panels on the Main form.
 */
public class MainContentPanel extends javax.swing.JPanel {

    public final static String CARD_DASHBOARD = "dashboard";
    public final static String CARD_PERSONS = "persons";
    public final static String CARD_APPSEARCH = "appsearch";
    public final static String CARD_BAUNIT_SEARCH = "baunitsearch";
    public final static String CARD_DOCUMENT_SEARCH = "documentsearch";
    public final static String CARD_APPASSIGNMENT = "appassignment";
    public final static String CARD_MAP = "map";
    public final static String CARD_APPLICATION = "application";
    public final static String CARD_NEW_TITLE_WIZARD = "newTitleWizard";
    public final static String CARD_BAUNIT_SELECT_PANEL = "baUnitSelectPanel";
    
    private HashMap<String, Component> cards;
    private ArrayList<String> cardsIndex;
    private PropertyChangeListener panelListener;

    /** Default constructor. */
    public MainContentPanel() {
        cardsIndex = new ArrayList<String>();
        cards = new HashMap<String, Component>();
        panelListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                handlePanelPropertyChanges(evt);
            }
        };
        initComponents();
    }

    /** Listens to the panel property changes to trap close button click. */
    private void handlePanelPropertyChanges(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(HeaderPanel.CLOSE_BUTTON_CLICKED)) {
            closePanel((JPanel) evt.getSource());
        }
    }

    /** 
     * Checks if panel already opened. 
     * @param panelClass Class of the panel to search for.
     */
    public boolean isPanelOpened(String cardName) {
        return cards.containsKey(cardName);
    }

    /** 
     * Adds panel into cards panels collection.
     * @param panel Panel object to add into the cards collection.
     * @param cardName Name of the card to assign to the added panel.
     * @param showPanel Indicates whether to show added panel.
     */
    public void addPanel(Component panel, String cardName, boolean showPanel) {
        if (isPanelOpened(cardName)) {
            getPanel(cardName).removePropertyChangeListener(panelListener);
            closePanel(cardName);
        }
        
        addCard(panel, cardName);

        if (ContentPanel.class.isAssignableFrom(panel.getClass())) {
            ((ContentPanel) panel).setMainContentPanel(this);
        }
        panel.addPropertyChangeListener(panelListener);
        pnlContent.add(panel, cardName);
        if (showPanel) {
            showPanel(cardName);
        }
    }

    private void addCard(Component panel, String cardName){
        cards.put(cardName, panel);
        if (!cardsIndex.contains(cardName)) {
            cardsIndex.add(cardName);
        }
    }
    
    /** 
     * Adds panel into cards panels collection. 
     * @param panel Panel object to add into the cards collection.
     * @param cardName Name of the card to assign to the added panel.
     */
    public void addPanel(Component panel, String cardName) {
        addPanel(panel, cardName, false);
    }

    /** 
     * Returns card/panel by the given card name. 
     * @param cardName Name of the card to search by.
     */
    public Component getPanel(String cardName) {
        return cards.get(cardName);
    }

    /** 
     * Closes panel by component object. 
     * @param panel Panel object to remove from the cards collection.
     */
    public void closePanel(Component panel) {
        if (cards.containsValue(panel)) {
            Set<Entry<String, Component>> tab = cards.entrySet();
            for (Iterator<Entry<String, Component>> it = tab.iterator(); it.hasNext();) {
                Entry<String, Component> entry = it.next();
                if (entry.getValue().equals(panel)) {
                    closePanel(entry.getKey());
                    break;
                }
            }
        }
    }

    /** 
     * Closes panel by card name. 
     * @param cardName Name of the card to close.
     */
    public void closePanel(String cardName) {
        if (cards.containsKey(cardName)) {
            pnlContent.remove(cards.get(cardName));
            cards.remove(cardName);
            cardsIndex.remove(cardName);
            showLastCard();
        }
    }
    
    private void closeAutoClosablePanels(){
        Iterator<Entry<String, Component>> it = cards.entrySet().iterator();
        ArrayList<String> keys = new ArrayList<String>();
        
        while(it.hasNext()){
            Entry<String, Component> entry = it.next();
            if(ContentPanel.class.isAssignableFrom(entry.getValue().getClass())){
                if(((ContentPanel)entry.getValue()).isCloseOnHide() && !entry.getValue().isVisible()){
                    keys.add(entry.getKey());
                }
            }
        }
        
        for(String key : keys){
            closePanel(key);
        }
    }
    
    private void showLastCard() {
        if (cardsIndex.size() > 0) {
            ((CardLayout) pnlContent.getLayout()).show(pnlContent, cardsIndex.get(cardsIndex.size() - 1));
        }
    }

    /** 
     * Shows panel by the given card name. 
     * @param cardName Name of the card to search by.
     */
    public void showPanel(String cardName) {
        if (!cards.containsKey(cardName)) {
            return;
        }

        ((CardLayout) pnlContent.getLayout()).show(pnlContent, cardName);
        
        // move panel on top
        int cardIndx = cardsIndex.indexOf(cardName);
        if (cardIndx < cardsIndex.size() - 1) {
            String lastCardName = cardsIndex.get(cardsIndex.size() - 1);
            cardsIndex.set(cardsIndex.size() - 1, cardName);
            cardsIndex.set(cardIndx, lastCardName);
        }
        
        // close autoclosable panels
        closeAutoClosablePanels();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContent = new javax.swing.JPanel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(MainContentPanel.class);
        pnlContent.setBackground(resourceMap.getColor("pnlContent.background")); // NOI18N
        pnlContent.setName("pnlContent"); // NOI18N
        pnlContent.setLayout(new java.awt.CardLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlContent;
    // End of variables declaration//GEN-END:variables
}