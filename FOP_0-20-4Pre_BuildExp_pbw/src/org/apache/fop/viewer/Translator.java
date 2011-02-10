/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package org.apache.fop.viewer;


/**
 * Definition f�r die �bersetzer-Klassen.
 *
 * @version 03.12.99
 * @author Stanislav.Gorkhover@jCatalog.com
 *
 */
public interface Translator {

    /**
     * �bersetzt ein Wort.
     */
    public String getString(String key);

    /**
     * Ein Translator soll die fehlenden keys hervorheben k�nnen.
     */
    public void setMissingEmphasized(boolean b);

    /**
     * Gibt an ob die �bersetzungsquelle gefunden ist.
     */
    public boolean isSourceFound();

    /**
     * Gibt an ob ein Key in der �bersetzungsquelle vorhanden ist.
     */
    public boolean contains(String key);
}