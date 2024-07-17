/* Copyright (C) 2023 JHotDraw.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
MA 02110-1301  USA
 */
package org.jhotdraw.io;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.xmlunit.assertj.XmlAssert;
import static org.assertj.core.api.Assertions.assertThat;
/**
 *
 * @author tw
 */
public class DOMStorableInputOutputFormatTest {
    @org.junit.jupiter.api.Test
    public void testRectangle() throws java.io.IOException {
        org.jhotdraw.draw.io.InputFormat format = new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        format.read(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream("green_rectangle.xml"), drawing, true);
        org.assertj.core.api.Assertions.assertThat(drawing.getChildren()).hasSize(1);
        org.jhotdraw.draw.figure.Figure rect = drawing.getChild(0);
        org.assertj.core.api.Assertions.assertThat(rect).isInstanceOf(org.jhotdraw.draw.figure.RectangleFigure.class);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR)).isEqualTo(new java.awt.Color(255, 0, 0));
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH)).isEqualTo(3.0);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR)).isEqualTo(new java.awt.Color(0, 128, 0));
    }

    @org.junit.jupiter.api.Test
    public void testSomeFigures() throws java.io.IOException {
        org.jhotdraw.draw.io.InputFormat format = new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        format.read(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream("figures.xml"), drawing, true);
        org.assertj.core.api.Assertions.assertThat(drawing.getChildren()).hasSize(11);
        org.jhotdraw.draw.figure.Figure rect = drawing.getChild(0);
        org.assertj.core.api.Assertions.assertThat(rect).isInstanceOf(org.jhotdraw.draw.figure.RectangleFigure.class);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_COLOR)).isEqualTo(java.awt.Color.BLACK);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH)).isEqualTo(1.0);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR)).isEqualTo(new java.awt.Color(255, 255, 102));
    }

    @org.junit.jupiter.params.ParameterizedTest(name = "{index} {0}")
    @org.junit.jupiter.params.provider.CsvSource({ "figures", "arrowtip", "green_rectangle", "bezier", "group", "image" })
    public void testSomeFiguresInOut(java.lang.String filename) throws java.io.IOException, java.net.URISyntaxException {
        org.jhotdraw.io.DOMStorableInputOutputFormatTest.LOG.info(("testing " + filename) + ".xml");
        org.jhotdraw.draw.io.InputFormat format = new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        format.read(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream(filename + ".xml"), drawing, true);
        org.jhotdraw.draw.io.OutputFormat outFormat = new org.jhotdraw.io.DOMStorableOutputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        java.io.File outputFile = new java.io.File(("target/test-output/" + filename) + "_roundtrip.xml");
        outputFile.getParentFile().mkdirs();
        outFormat.write(new java.io.FileOutputStream(("target/test-output/" + filename) + "_roundtrip.xml"), drawing);
        org.xmlunit.assertj.XmlAssert.assertThat(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream(filename + ".xml")).and(outputFile).ignoreWhitespace().areIdentical();
    }

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getName());

    /**
     * This new implementation skipped color entries like <color id="14" rgba="#ff000000"/>.
     *
     * <p>Indeed this is correct since this is the standard value for this data item and skipping it
     * would result in the same output.
     */
    @org.junit.jupiter.api.Test
    public void testProblemAttributeRectangle() throws java.io.IOException {
        org.jhotdraw.draw.io.InputFormat format = new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        format.read(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream("problem_figure_attributes.xml"), drawing, true);
        org.assertj.core.api.Assertions.assertThat(drawing.getChildren()).hasSize(1);
        org.jhotdraw.draw.figure.Figure rect = drawing.getChild(0);
        org.assertj.core.api.Assertions.assertThat(rect).isInstanceOf(org.jhotdraw.draw.figure.RectangleFigure.class);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.FILL_COLOR)).isEqualTo(new java.awt.Color(102, 102, 255));
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR)).isEqualTo(new java.awt.Color(0, 0, 0));
    }

    @org.junit.jupiter.api.Test
    public void testBezierFigure() throws java.io.IOException {
        org.jhotdraw.draw.io.InputFormat format = new org.jhotdraw.io.DOMStorableInputFormat(new org.jhotdraw.io.DOMDefaultDrawFigureFactory());
        org.jhotdraw.draw.Drawing drawing = new org.jhotdraw.draw.DefaultDrawing();
        format.read(org.jhotdraw.io.DOMStorableInputOutputFormatTest.class.getResourceAsStream("bezier.xml"), drawing, true);
        org.assertj.core.api.Assertions.assertThat(drawing.getChildren()).hasSize(1);
        org.jhotdraw.draw.figure.Figure rect = drawing.getChild(0);
        org.assertj.core.api.Assertions.assertThat(rect).isInstanceOf(org.jhotdraw.draw.figure.BezierFigure.class);
        org.assertj.core.api.Assertions.assertThat(((org.jhotdraw.draw.figure.BezierFigure) (rect)).getNodeCount()).isEqualTo(10);
        org.assertj.core.api.Assertions.assertThat(rect.attr().get(org.jhotdraw.draw.AttributeKeys.TEXT_COLOR)).isEqualTo(new java.awt.Color(0, 0, 0));
    }
}