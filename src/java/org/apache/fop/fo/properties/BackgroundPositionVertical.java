/*
 * $Id$
 *
 *
 * Copyright 1999-2003 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  
 */
package org.apache.fop.fo.properties;

import org.apache.fop.datatypes.Percentage;
import org.apache.fop.datatypes.PropertyValue;
import org.apache.fop.fo.PropNames;
import org.apache.fop.fo.expr.PropertyException;

public class BackgroundPositionVertical extends Property  {
    public static final int dataTypes =
                                    PERCENTAGE | LENGTH | ENUM | INHERIT;

    public int getDataTypes() {
        return dataTypes;
    }

    public static final int traitMapping = VALUE_CHANGE;

    public int getTraitMapping() {
        return traitMapping;
    }

    public static final int initialValueType = PERCENTAGE_IT;

    public int getInitialValueType() {
        return initialValueType;
    }

    public static final int TOP = 1;
    public static final int CENTER = 2;
    public static final int BOTTOM = 3;
    public PropertyValue getInitialValue(int property)
        throws PropertyException
    {
        return Percentage.makePercentage
                        (PropNames.BACKGROUND_POSITION_VERTICAL, 0.0d);
    }
    public static final int inherited = NO;

    public int getInherited() {
        return inherited;
    }


    private static final String[] rwEnums = {
        null
        ,"top"
        ,"center"
        ,"bottom"
    };
    public int getEnumIndex(String enum) throws PropertyException {
        return enumValueToIndex(enum, rwEnums);
    }
    public String getEnumText(int index) {
        return rwEnums[index];
    }
}

