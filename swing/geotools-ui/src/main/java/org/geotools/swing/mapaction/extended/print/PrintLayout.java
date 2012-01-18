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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.swing.mapaction.extended.print;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Elton manoku
 */
public class PrintLayout extends ElementLayout {

    private String name;
    private int pageWidth;
    private int pageHeight;
    private ImageLayout map;
    private ImageLayout scalebar;
    private List<ImageLayout> imageLayouts = new ArrayList<ImageLayout>();
    private List<TextLayout> textLayouts = new ArrayList<TextLayout>();

    public PrintLayout() {
    }

    public PrintLayout(String xmlFormatedLayout) {
        try {
            InputSource is = new InputSource(new StringReader(xmlFormatedLayout.trim()));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            this.initialize(doc);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public PrintLayout(InputStream xmlFormatedLayoutStream) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    xmlFormatedLayoutStream);
            this.initialize(doc);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private void initialize(Document doc) throws Exception {
        this.parseNode(doc, "name");
        this.parseNode(doc, "page");
        this.parseNode(doc, "map");
        this.parseNode(doc, "scalebar");
        this.parseNode(doc, "text");
        this.parseNode(doc, "image");
    }

    private void parseNode(Document doc, String nodeName) throws Exception {
        NodeList nodeList = doc.getElementsByTagName(nodeName);
        if (nodeName.equals("name")) {
            if (nodeList.getLength() == 0) {
                throw new Exception("Layout is missing name.");
            }
            Node node = nodeList.item(0);
            this.name = node.getTextContent();
        }else if (nodeName.equals("page")) {
            if (nodeList.getLength() == 0) {
                throw new Exception("Layout is missing page definition.");
            }
            Node node = nodeList.item(0);
            this.pageWidth = Integer.parseInt(this.getAttributeValue(node, "width"));
            this.pageHeight = Integer.parseInt(this.getAttributeValue(node, "height"));
        }else if (nodeName.equals("map")) {
            if (nodeList.getLength() == 0) {
                throw new Exception("Layout is missing map definition.");
            }
            Node node = nodeList.item(0);
            this.map = new ImageLayout(node);
        } else if (nodeName.equals("scalebar")) {
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);
                this.scalebar = new ImageLayout(node);
            }
        } else if (nodeName.equals("text")) {
            for(int nodeInd=0; nodeInd< nodeList.getLength(); nodeInd++){                
                this.textLayouts.add(new TextLayout(nodeList.item(nodeInd)));
            }
        } else if (nodeName.equals("image")) {
            for(int nodeInd=0; nodeInd< nodeList.getLength(); nodeInd++){                
                this.imageLayouts.add(new ImageLayout(nodeList.item(nodeInd)));
            }
        } 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public ImageLayout getMap() {
        return map;
    }

    public void setMap(ImageLayout map) {
        this.map = map;
    }

    public ImageLayout getScalebar() {
        return scalebar;
    }

    public void setScalebar(ImageLayout scalebar) {
        this.scalebar = scalebar;
    }

    public List<ImageLayout> getImageLayouts() {
        return imageLayouts;
    }

    public List<TextLayout> getTextLayouts() {
        return textLayouts;
    }

    @Override
    public String toString() {
        return this.name;
    }
}