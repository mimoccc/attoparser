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
package org.attoparser;

import java.io.CharArrayReader;
import java.io.StringReader;

import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractAttoParser implements IAttoParser {


    
    
    protected AbstractAttoParser() {
        super();
    }
    

    
    
    public final void parse(final String document, final IAttoHandler handler) 
            throws AttoParseException  {
        parse(new StringReader(document), handler);
    }
    
    public final void parse(final char[] document, final IAttoHandler handler) 
            throws AttoParseException {
        parse(new CharArrayReader(document), handler);
    }
    
    public final void parse(
            final char[] document, final int offset, final int len, final IAttoHandler handler) 
            throws AttoParseException {
        if (offset < 0 || len < 0) {
            throw new IllegalArgumentException(
                    "Neither document offset (" + offset + ") nor document length (" + 
                    len + ") can be less than zero");
        }
        parse(new CharArrayReader(document, offset, len), handler);
    }
    
    
}
