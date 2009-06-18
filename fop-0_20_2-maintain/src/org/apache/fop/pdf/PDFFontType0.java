/*
 * $Id$
 * ============================================================================
 *                    The Apache Software License, Version 1.1
 * ============================================================================
 * 
 * Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by the Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The names "FOP" and "Apache Software Foundation" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 * 
 * 5. Products derived from this software may not be called "Apache", nor may
 *    "Apache" appear in their name, without prior written permission of the
 *    Apache Software Foundation.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * APACHE SOFTWARE FOUNDATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ============================================================================
 * 
 * This software consists of voluntary contributions made by many individuals
 * on behalf of the Apache Software Foundation and was originally created by
 * James Tauber <jtauber@jtauber.com>. For more information on the Apache
 * Software Foundation, please see <http://www.apache.org/>.
 */ 
package org.apache.fop.pdf;

/**
 * class representing a Type0 font.
 *
 * Type0 fonts are specified on page 208 and onwards of the PDF 1.3 spec.
 */
public class PDFFontType0 extends PDFFontNonBase14 {

    /**
     * this should be an array of CIDFont but only the first one is used
     */
    protected PDFCIDFont descendantFonts;

    protected PDFCMap cmap;

    /**
     * create the /Font object
     *
     * @param number the object's number
     * @param fontname the internal name for the font
     * @param subtype the font's subtype (PDFFont.TYPE0)
     * @param basefont the base font name
     * @param encoding the character encoding schema used by the font
     * @param mapping the Unicode mapping mechanism
     */
    public PDFFontType0(int number, String fontname, byte subtype,
                        String basefont,
                        Object encoding /* , PDFToUnicode mapping */) {

        /* generic creation of PDF object */
        super(number, fontname, subtype, basefont, encoding /* , mapping */);

        /* set fields using paramaters */
        this.descendantFonts = null;
        cmap = null;
    }

    /**
     * create the /Font object
     *
     * @param number the object's number
     * @param fontname the internal name for the font
     * @param subtype the font's subtype (PDFFont.TYPE0)
     * @param basefont the base font name
     * @param encoding the character encoding schema used by the font
     * @param mapping the Unicode mapping mechanism
     * @param descendantFonts the CIDFont upon which this font is based
     */
    public PDFFontType0(int number, String fontname, byte subtype,
                        String basefont,
                        Object encoding /* , PDFToUnicode mapping */,
                        PDFCIDFont descendantFonts) {

        /* generic creation of PDF object */
        super(number, fontname, subtype, basefont, encoding /* , mapping */);

        /* set fields using paramaters */
        this.descendantFonts = descendantFonts;
    }

    /**
     * set the descendant font
     *
     * @param descendantFonts the CIDFont upon which this font is based
     */
    public void setDescendantFonts(PDFCIDFont descendantFonts) {
        this.descendantFonts = descendantFonts;
    }

    public void setCMAP(PDFCMap cmap) {
        this.cmap = cmap;
    }

    /**
     * fill in the specifics for the font's subtype
     */
    protected void fillInPDF(StringBuffer p) {
        if (descendantFonts != null) {
            p.append("\n/DescendantFonts [ "
                     + this.descendantFonts.referencePDF() + " ] ");
        }
        if (cmap != null) {
            p.append("\n/ToUnicode " + cmap.referencePDF());
        }
    }

}
