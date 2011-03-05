/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package org.apache.fop.fonts;

import java.nio.CharBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// CSOFF: InnerAssignmentCheck
// CSOFF: LineLengthCheck

/**
 * The <code>GlyphDefinitionTable</code> class is a glyph table that implements
 * glyph definition functionality according to the OpenType GDEF table.
 * @author Glenn Adams
 */
public class GlyphDefinitionTable extends GlyphTable {

    /** logging instance */
    private static final Log log = LogFactory.getLog(GlyphDefinitionTable.class);                                       // CSOK: ConstantNameCheck

    /** glyph class subtable type */
    public static final int GDEF_LOOKUP_TYPE_GLYPH_CLASS = 1;
    /** attachment point subtable type */
    public static final int GDEF_LOOKUP_TYPE_ATTACHMENT_POINT = 2;
    /** ligature caret subtable type */
    public static final int GDEF_LOOKUP_TYPE_LIGATURE_CARET = 3;
    /** mark attachment subtable type */
    public static final int GDEF_LOOKUP_TYPE_MARK_ATTACHMENT = 4;

    /** pre-defined glyph class - base glyph */
    public static final int GLYPH_CLASS_BASE = 1;
    /** pre-defined glyph class - ligature glyph */
    public static final int GLYPH_CLASS_LIGATURE = 2;
    /** pre-defined glyph class - mark glyph */
    public static final int GLYPH_CLASS_MARK = 3;
    /** pre-defined glyph class - component glyph */
    public static final int GLYPH_CLASS_COMPONENT = 4;

    /** singleton glyph class table */
    private GlyphClassSubtable gct;
    /** singleton attachment point table */
    // private AttachmentPointSubtable apt;
    /** singleton ligature caret table */
    // private LigatureCaretSubtable lct;
    /** singleton mark attachment table */
    // private MarkAttachmentSubtable mat;

    /**
     * Instantiate a <code>GlyphDefinitionTable</code> object using the specified subtables.
     * @param subtables a list of identified subtables
     */
    public GlyphDefinitionTable ( List subtables ) {
        super ( null, new HashMap(0) );
        if ( ( subtables == null ) || ( subtables.size() == 0 ) ) {
            throw new AdvancedTypographicTableFormatException ( "subtables must be non-empty" );
        } else {
            for ( Iterator it = subtables.iterator(); it.hasNext();) {
                Object o = it.next();
                if ( o instanceof GlyphDefinitionSubtable ) {
                    addSubtable ( (GlyphSubtable) o );
                } else {
                    throw new AdvancedTypographicTableFormatException ( "subtable must be a glyph definition subtable" );
                }
            }
            freezeSubtables();
        }
    }

    /** {@inheritDoc} */
    protected void addSubtable ( GlyphSubtable subtable ) {
        if ( subtable instanceof GlyphClassSubtable ) {
            this.gct = (GlyphClassSubtable) subtable;
        } else if ( subtable instanceof AttachmentPointSubtable ) {
            // TODO - not yet used
            // this.apt = (AttachmentPointSubtable) subtable;
        } else if ( subtable instanceof LigatureCaretSubtable ) {
            // TODO - not yet used
            // this.lct = (LigatureCaretSubtable) subtable;
        } else if ( subtable instanceof MarkAttachmentSubtable ) {
            // TODO - not yet used
            // this.mat = (MarkAttachmentSubtable) subtable;
        } else {
            throw new UnsupportedOperationException ( "unsupported glyph definition subtable type: " + subtable );
        }
    }

    /**
     * Determine if glyph belongs to pre-defined glyph class.
     * @param gid a glyph identifier (index)
     * @param gc a pre-defined glyph class (GLYPH_CLASS_BASE|GLYPH_CLASS_LIGATURE|GLYPH_CLASS_MARK|GLYPH_CLASS_COMPONENT).
     * @return true if glyph belongs to specified glyph class
     */
    public boolean isGlyphClass ( int gid, int gc ) {
        if ( gct != null ) {
            return gct.isGlyphClass ( gid, gc );
        } else {
            return false;
        }
    }

    /**
     * Map a lookup type name to its constant (integer) value.
     * @param name lookup type name
     * @return lookup type
     */
    public static int getLookupTypeFromName ( String name ) {
        int t;
        String s = name.toLowerCase();
        if ( "glyphclass".equals ( s ) ) {
            t = GDEF_LOOKUP_TYPE_GLYPH_CLASS;
        } else if ( "attachmentpoint".equals ( s ) ) {
            t = GDEF_LOOKUP_TYPE_ATTACHMENT_POINT;
        } else if ( "ligaturecaret".equals ( s ) ) {
            t = GDEF_LOOKUP_TYPE_LIGATURE_CARET;
        } else if ( "markattachment".equals ( s ) ) {
            t = GDEF_LOOKUP_TYPE_MARK_ATTACHMENT;
        } else {
            t = -1;
        }
        return t;
    }

    /**
     * Map a lookup type constant (integer) value to its name.
     * @param type lookup type
     * @return lookup type name
     */
    public static String getLookupTypeName ( int type ) {
        String tn = null;
        switch ( type ) {
        case GDEF_LOOKUP_TYPE_GLYPH_CLASS:
            tn = "glyphclass";
            break;
        case GDEF_LOOKUP_TYPE_ATTACHMENT_POINT:
            tn = "attachmentpoint";
            break;
        case GDEF_LOOKUP_TYPE_LIGATURE_CARET:
            tn = "ligaturecaret";
            break;
        case GDEF_LOOKUP_TYPE_MARK_ATTACHMENT:
            tn = "markattachment";
            break;
        default:
            tn = "unknown";
            break;
        }
        return tn;
    }

    /**
     * Create a definition subtable according to the specified arguments.
     * @param type subtable type
     * @param id subtable identifier
     * @param sequence subtable sequence
     * @param flags subtable flags (must be zero)
     * @param format subtable format
     * @param mapping subtable mapping table
     * @param entries subtable entries
     * @return a glyph subtable instance
     */
    public static GlyphSubtable createSubtable ( int type, String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
        GlyphSubtable st = null;
        switch ( type ) {
        case GDEF_LOOKUP_TYPE_GLYPH_CLASS:
            st = GlyphClassSubtable.create ( id, sequence, flags, format, mapping, entries );
            break;
        case GDEF_LOOKUP_TYPE_ATTACHMENT_POINT:
            st = AttachmentPointSubtable.create ( id, sequence, flags, format, mapping, entries );
            break;
        case GDEF_LOOKUP_TYPE_LIGATURE_CARET:
            st = LigatureCaretSubtable.create ( id, sequence, flags, format, mapping, entries );
            break;
        case GDEF_LOOKUP_TYPE_MARK_ATTACHMENT:
            st = MarkAttachmentSubtable.create ( id, sequence, flags, format, mapping, entries );
            break;
        default:
            break;
        }
        return st;
    }

    private abstract static class GlyphClassSubtable extends GlyphDefinitionSubtable {
        GlyphClassSubtable ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping );
        }
        /** {@inheritDoc} */
        public int getType() {
            return GDEF_LOOKUP_TYPE_GLYPH_CLASS;
        }
        /**
         * Determine if glyph belongs to pre-defined glyph class.
         * @param gid a glyph identifier (index)
         * @param gc a pre-defined glyph class (GLYPH_CLASS_BASE|GLYPH_CLASS_LIGATURE|GLYPH_CLASS_MARK|GLYPH_CLASS_COMPONENT).
         * @return true if glyph belongs to specified glyph class
         */
        public abstract boolean isGlyphClass ( int gid, int gc );
        static GlyphDefinitionSubtable create ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            if ( format == 1 ) {
                return new GlyphClassSubtableFormat1 ( id, sequence, flags, format, mapping, entries );
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class GlyphClassSubtableFormat1 extends GlyphClassSubtable {
        GlyphClassSubtableFormat1 ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping, entries );
        }
        /** {@inheritDoc} */
        public List getEntries() {
            return null;
        }
        /** {@inheritDoc} */
        public boolean isCompatible ( GlyphSubtable subtable ) {
            return subtable instanceof GlyphClassSubtable;
        }
        /** {@inheritDoc} */
        public boolean isGlyphClass ( int gid, int gc ) {
            GlyphClassMapping cm = getClasses();
            if ( cm != null ) {
                return cm.getClassIndex ( gid, 0 ) == gc;
            } else {
                return false;
            }
        }
    }

    private abstract static class AttachmentPointSubtable extends GlyphDefinitionSubtable {
        AttachmentPointSubtable ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping );
        }
        /** {@inheritDoc} */
        public int getType() {
            return GDEF_LOOKUP_TYPE_ATTACHMENT_POINT;
        }
        static GlyphDefinitionSubtable create ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            if ( format == 1 ) {
                return new AttachmentPointSubtableFormat1 ( id, sequence, flags, format, mapping, entries );
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class AttachmentPointSubtableFormat1 extends AttachmentPointSubtable {
        AttachmentPointSubtableFormat1 ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping, entries );
        }
        /** {@inheritDoc} */
        public List getEntries() {
            return null;
        }
        /** {@inheritDoc} */
        public boolean isCompatible ( GlyphSubtable subtable ) {
            return subtable instanceof AttachmentPointSubtable;
        }
    }

    private abstract static class LigatureCaretSubtable extends GlyphDefinitionSubtable {
        LigatureCaretSubtable ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping );
        }
        /** {@inheritDoc} */
        public int getType() {
            return GDEF_LOOKUP_TYPE_LIGATURE_CARET;
        }
        static GlyphDefinitionSubtable create ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            if ( format == 1 ) {
                return new LigatureCaretSubtableFormat1 ( id, sequence, flags, format, mapping, entries );
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class LigatureCaretSubtableFormat1 extends LigatureCaretSubtable {
        LigatureCaretSubtableFormat1 ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping, entries );
        }
        /** {@inheritDoc} */
        public List getEntries() {
            return null;
        }
        /** {@inheritDoc} */
        public boolean isCompatible ( GlyphSubtable subtable ) {
            return subtable instanceof LigatureCaretSubtable;
        }
    }

    private abstract static class MarkAttachmentSubtable extends GlyphDefinitionSubtable {
        MarkAttachmentSubtable ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping );
        }
        /** {@inheritDoc} */
        public int getType() {
            return GDEF_LOOKUP_TYPE_MARK_ATTACHMENT;
        }
        static GlyphDefinitionSubtable create ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            if ( format == 1 ) {
                return new MarkAttachmentSubtableFormat1 ( id, sequence, flags, format, mapping, entries );
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class MarkAttachmentSubtableFormat1 extends MarkAttachmentSubtable {
        MarkAttachmentSubtableFormat1 ( String id, int sequence, int flags, int format, GlyphMappingTable mapping, List entries ) {
            super ( id, sequence, flags, format, mapping, entries );
        }
        /** {@inheritDoc} */
        public List getEntries() {
            return null;
        }
        /** {@inheritDoc} */
        public boolean isCompatible ( GlyphSubtable subtable ) {
            return subtable instanceof MarkAttachmentSubtable;
        }
    }

}
