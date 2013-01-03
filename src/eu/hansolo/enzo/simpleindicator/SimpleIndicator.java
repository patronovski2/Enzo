/*
 * Copyright (c) 2013, Gerrit Grunwald
 * All right reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * The names of its contributors may not be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package eu.hansolo.enzo.simpleindicator;

import javafx.scene.control.Control;


/**
 * Created by
 * User: hansolo
 * Date: 06.03.12
 * Time: 13:48
 */
public class SimpleIndicator extends Control {
    public static final String STYLE_CLASS_OFF     = "indicator-off";
    public static final String STYLE_CLASS_RED     = "indicator-red";
    public static final String STYLE_CLASS_GREEN   = "indicator-green";
    public static final String STYLE_CLASS_BLUE    = "indicator-blue";
    public static final String STYLE_CLASS_YELLOW  = "indicator-yellow";
    public static final String STYLE_CLASS_ORANGE  = "indicator-orange";
    public static final String STYLE_CLASS_CYAN    = "indicator-cyan";
    public static final String STYLE_CLASS_MAGENTA = "indicator-magenta";
    public static final String STYLE_CLASS_PURPLE  = "indicator-purple";
    public static final String STYLE_CLASS_GRAY    = "indicator-gray";


    // ******************** Constructors **************************************
    public SimpleIndicator() {
        getStyleClass().add("indicator");
    }


    // ******************** Style related *************************************
    @Override protected String getUserAgentStylesheet() {
        return getClass().getResource("simpleindicator.css").toExternalForm();
    }
}