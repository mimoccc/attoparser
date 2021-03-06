/*
 * =============================================================================
 * 
 *   Copyright (c) 2012, The ATTOPARSER team (http://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.attoparser.markup.xml;

import java.util.Map;

import org.attoparser.AttoParseException;
import org.attoparser.markup.dom.INestableNode;
import org.attoparser.markup.dom.impl.CDATASection;
import org.attoparser.markup.dom.impl.Comment;
import org.attoparser.markup.dom.impl.DocType;
import org.attoparser.markup.dom.impl.Document;
import org.attoparser.markup.dom.impl.Element;
import org.attoparser.markup.dom.impl.ProcessingInstruction;
import org.attoparser.markup.dom.impl.Text;
import org.attoparser.markup.dom.impl.XmlDeclaration;




/**
 * <p>
 *   Implementation of {@link org.attoparser.IAttoHandler} that considers input
 *   as XML code and builds an attoDOM tree with objects from package 
 *   <tt>org.attoparser.markup.dom</tt>. 
 * </p>
 * <p>
 *   Use of this handler requires the document to be well-formed from the XML standpoint.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class DOMXmlAttoHandler extends AbstractStandardXmlAttoHandler {
    
    private final String documentName;
    
    private Document document = null;
    private boolean parsingFinished = false;
    private long parsingStartTimeNanos = -1L;
    private long parsingEndTimeNanos = -1L;
    private long parsingTotalTimeNanos = -1L;

    private INestableNode currentParent = null;
    
    

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p> 
     */
    public DOMXmlAttoHandler() {
        this(null);
    }

    /**
     * <p>
     *   Creates a new instance of this handler.
     * </p> 
     */
    public DOMXmlAttoHandler(final String documentName) {
        super();
        this.documentName = 
                (documentName == null? 
                        String.valueOf(System.identityHashCode(this)) : documentName);
    }

    
    
    /**
     * <p>
     *   Returns the attoDOM {@link Document} created during parsing.
     * </p>
     * 
     * @return the built DOM document object. 
     */
    public Document getDocument() {
        return this.document;
    }

    

    /**
     * <p>
     *   Returns the time (in nanoseconds) when parsing started.
     * </p>
     * 
     * @return the start time.
     */
    public long getParsingStartTimeNanos() {
        return this.parsingStartTimeNanos;
    }

    /**
     * <p>
     *   Returns the time (in nanoseconds) when parsing ended.
     * </p>
     * 
     * @return the end time.
     */
    public long getParsingEndTimeNanos() {
        return this.parsingEndTimeNanos;
    }

    /**
     * <p>
     *   Returns the difference (in nanoseconds) between parsing start and end.
     * </p>
     * 
     * @return the parsing time in nanos.
     */
    public long getParsingTotalTimeNanos() {
        return this.parsingTotalTimeNanos;
    }

    
    /**
     * <p>
     *   Returns whether parsing has already finished or not.
     * </p>
     * 
     * @return <tt>true</tt> if parsing has finished, <tt>false</tt> if not.
     */
    public boolean isParsingFinished() {
        return this.parsingFinished;
    }


    

    @Override
    public void handleXmlDocumentStart(
            final long startTimeNanos, 
            final int line, final int col) 
            throws AttoParseException {
        
        super.handleXmlDocumentStart(startTimeNanos, line, col);
        
        this.document = new Document(this.documentName);
        this.parsingStartTimeNanos = startTimeNanos;
        
    }

    
    
    @Override
    public void handleXmlDocumentEnd(
            final long endTimeNanos, final long totalTimeNanos, 
            final int line, final int col)
            throws AttoParseException {

        super.handleXmlDocumentEnd(endTimeNanos, totalTimeNanos, line, col);
        
        this.parsingEndTimeNanos = endTimeNanos;
        this.parsingTotalTimeNanos = totalTimeNanos;
        
        this.parsingFinished = true;
        
    }

    
    
    
    
    @Override
    public void handleXmlDeclaration(
            final String version, final String encoding, final String standalone, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleXmlDeclaration(version, encoding, standalone, line, col);
        
        final XmlDeclaration xmlDeclaration = new XmlDeclaration(version, encoding, standalone);
        xmlDeclaration.setLine(Integer.valueOf(line));
        xmlDeclaration.setLine(Integer.valueOf(col));
        
        
        if (this.currentParent == null) {
            this.document.addChild(xmlDeclaration);
        } else {
            this.currentParent.addChild(xmlDeclaration);
        }
        
    }

    
    
    @Override
    public void handleDocType(
            final String elementName, 
            final String publicId, final String systemId, final String internalSubset, 
            final int line, final int col)
            throws AttoParseException {

        super.handleDocType(elementName, publicId, systemId, internalSubset, line, col);

        final DocType docType = new DocType(elementName, publicId, systemId, internalSubset);
        docType.setLine(Integer.valueOf(line));
        docType.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(docType);
        } else {
            this.currentParent.addChild(docType);
        }
        
    }

    
    
    
    
    @Override
    public void handleXmlStandaloneElement(
            final String elementName, final Map<String, String> attributes, 
            final int line, final int col)
            throws AttoParseException {

        super.handleXmlStandaloneElement(elementName, attributes, line, col);

        final Element element = new Element(elementName);
        element.addAttributes(attributes);
        element.setLine(Integer.valueOf(line));
        element.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(element);
        } else {
            this.currentParent.addChild(element);
        }
        
    }

    
    
    @Override
    public void handleXmlOpenElement(
            final String elementName, final Map<String, String> attributes, 
            final int line, final int col)
            throws AttoParseException {

        super.handleXmlOpenElement(elementName, attributes, line, col);

        final Element element = new Element(elementName);
        element.addAttributes(attributes);
        element.setLine(Integer.valueOf(line));
        element.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(element);
        } else {
            this.currentParent.addChild(element);
        }
        this.currentParent = element;
        
    }


    
    @Override
    public void handleXmlCloseElement(
            final String elementName, 
            final int line, final int col)
            throws AttoParseException {

        super.handleXmlCloseElement(elementName, line, col);

        this.currentParent = this.currentParent.getParent();
        
    }

    
    

    
    
    @Override
    public void handleComment(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col) 
            throws AttoParseException {

        super.handleComment(buffer, offset, len, line, col);
        
        final Comment comment = new Comment(new String(buffer, offset, len));
        comment.setLine(Integer.valueOf(line));
        comment.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(comment);
        } else {
            this.currentParent.addChild(comment);
        }
        
    }
    
    

    @Override
    public void handleCDATASection(
            final char[] buffer, final int offset, final int len,
            final int line, final int col) 
            throws AttoParseException {

        super.handleCDATASection(buffer, offset, len, line, col);
        
        final CDATASection cdataSection = new CDATASection(new String(buffer, offset, len));
        cdataSection.setLine(Integer.valueOf(line));
        cdataSection.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(cdataSection);
        } else {
            this.currentParent.addChild(cdataSection);
        }
        
    }
    
    

    @Override
    public void handleText(
            final char[] buffer, final int offset, final int len, 
            final int line, final int col)
            throws AttoParseException {

        super.handleText(buffer, offset, len, line, col);
        
        final Text text = new Text(new String(buffer, offset, len));
        text.setLine(Integer.valueOf(line));
        text.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(text);
        } else {
            this.currentParent.addChild(text);
        }
        
    }



    
    
    @Override
    public void handleProcessingInstruction(
            final String target, final String content,
            final int line, final int col) 
            throws AttoParseException {

        super.handleProcessingInstruction(target, content, line, col);
        
        final ProcessingInstruction processingInstruction = 
                new ProcessingInstruction(target, content);
        processingInstruction.setLine(Integer.valueOf(line));
        processingInstruction.setLine(Integer.valueOf(col));
        
        if (this.currentParent == null) {
            this.document.addChild(processingInstruction);
        } else {
            this.currentParent.addChild(processingInstruction);
        }
        
    }
    
}
