/* @(#)ButtonFactory.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui.action;
/**
 * ButtonFactory.
 *
 * <p>
 *
 * <p>Design pattern:<br>
 * Name: Abstract Factory.<br>
 * Role: Abstract Factory.<br>
 * Partners: org.jhotdraw.samples.draw.DrawApplicationModel as Client,
 * org.jhotdraw.samples.draw.DrawView as Client, org.jhotdraw.samples.draw.DrawingPanel as Client.
 *
 * <p>
 *
 * <p>FIXME - All buttons created using the ButtonFactory must automatically become
 * disabled/enabled, when the DrawingEditor is disabled/enabled.
 */
public class ButtonFactory {
    /**
     * Mac OS X 'Apple Color Palette'. This palette has 8 columns.
     */
    public static final java.util.List<org.jhotdraw.draw.action.ColorIcon> DEFAULT_COLORS;

    static {
        DEFAULT_COLORS = java.util.List.of(new org.jhotdraw.draw.action.ColorIcon(0x800000, "Cayenne"), new org.jhotdraw.draw.action.ColorIcon(0x808000, "Asparagus"), new org.jhotdraw.draw.action.ColorIcon(0x8000, "Clover"), new org.jhotdraw.draw.action.ColorIcon(0x8080, "Teal"), new org.jhotdraw.draw.action.ColorIcon(0x80, "Midnight"), new org.jhotdraw.draw.action.ColorIcon(0x800080, "Plum"), new org.jhotdraw.draw.action.ColorIcon(0x7f7f7f, "Tin"), new org.jhotdraw.draw.action.ColorIcon(0x808080, "Nickel"), new org.jhotdraw.draw.action.ColorIcon(0xff0000, "Maraschino"), new org.jhotdraw.draw.action.ColorIcon(0xffff00, "Lemon"), new org.jhotdraw.draw.action.ColorIcon(0xff00, "Spring"), new org.jhotdraw.draw.action.ColorIcon(0xffff, "Turquoise"), new org.jhotdraw.draw.action.ColorIcon(0xff, "Blueberry"), new org.jhotdraw.draw.action.ColorIcon(0xff00ff, "Magenta"), new org.jhotdraw.draw.action.ColorIcon(0x666666, "Steel"), new org.jhotdraw.draw.action.ColorIcon(0x999999, "Aluminium"), new org.jhotdraw.draw.action.ColorIcon(0xff6666, "Salmon"), new org.jhotdraw.draw.action.ColorIcon(0xffff66, "Banana"), new org.jhotdraw.draw.action.ColorIcon(0x66ff66, "Flora"), new org.jhotdraw.draw.action.ColorIcon(0x66ffff, "Ice"), new org.jhotdraw.draw.action.ColorIcon(0x6666ff, "Orchid"), new org.jhotdraw.draw.action.ColorIcon(0xff66ff, "Bubblegum"), new org.jhotdraw.draw.action.ColorIcon(0x4c4c4c, "Iron"), new org.jhotdraw.draw.action.ColorIcon(0xb3b3b3, "Magnesium"), new org.jhotdraw.draw.action.ColorIcon(0x804000, "Mocha"), new org.jhotdraw.draw.action.ColorIcon(0x408000, "Fern"), new org.jhotdraw.draw.action.ColorIcon(0x8040, "Moss"), new org.jhotdraw.draw.action.ColorIcon(0x4080, "Ocean"), new org.jhotdraw.draw.action.ColorIcon(0x400080, "Eggplant"), new org.jhotdraw.draw.action.ColorIcon(0x800040, "Maroon"), new org.jhotdraw.draw.action.ColorIcon(0x333333, "Tungsten"), new org.jhotdraw.draw.action.ColorIcon(0xcccccc, "Silver"), new org.jhotdraw.draw.action.ColorIcon(0xff8000, "Tangerine"), new org.jhotdraw.draw.action.ColorIcon(0x80ff00, "Lime"), new org.jhotdraw.draw.action.ColorIcon(0xff80, "Sea Foam"), new org.jhotdraw.draw.action.ColorIcon(0x80ff, "Aqua"), new org.jhotdraw.draw.action.ColorIcon(0x8000ff, "Grape"), new org.jhotdraw.draw.action.ColorIcon(0xff0080, "Strawberry"), new org.jhotdraw.draw.action.ColorIcon(0x191919, "Lead"), new org.jhotdraw.draw.action.ColorIcon(0xe6e6e6, "Mercury"), new org.jhotdraw.draw.action.ColorIcon(0xffcc66, "Cantaloupe"), new org.jhotdraw.draw.action.ColorIcon(0xccff66, "Honeydew"), new org.jhotdraw.draw.action.ColorIcon(0x66ffcc, "Spindrift"), new org.jhotdraw.draw.action.ColorIcon(0x66ccff, "Sky"), new org.jhotdraw.draw.action.ColorIcon(0xcc66ff, "Lavender"), new org.jhotdraw.draw.action.ColorIcon(0xff6fcf, "Carnation"), new org.jhotdraw.draw.action.ColorIcon(0x0, "Licorice"), new org.jhotdraw.draw.action.ColorIcon(0xffffff, "Snow"));
    }

    public static final int DEFAULT_COLORS_COLUMN_COUNT = 8;

    /**
     * Websave color palette as used by Macromedia Fireworks. This palette has 19 columns. The
     * leftmost column contains a redundant set of color icons to make selection of gray scales and of
     * the primary colors easier.
     */
    public static final java.util.List<org.jhotdraw.draw.action.ColorIcon> WEBSAVE_COLORS;

    static {
        java.util.List<org.jhotdraw.draw.action.ColorIcon> m = new java.util.ArrayList<>();
        for (int b = 0; b <= 0xff; b += 0x33) {
            int rgb = ((b << 16) | (b << 8)) | b;
            m.add(new org.jhotdraw.draw.action.ColorIcon(rgb));
            for (int r = 0; r <= 0x66; r += 0x33) {
                for (int g = 0; g <= 0xff; g += 0x33) {
                    rgb = ((r << 16) | (g << 8)) | b;
                    m.add(new org.jhotdraw.draw.action.ColorIcon(rgb));
                }
            }
        }
        int[] firstColumn = new int[]{ 0xff0000, 0xff00, 0xff, 0xff00ff, 0xffff, 0xffff00 };
        for (int b = 0x0, i = 0; b <= 0xff; b += 0x33 , i++) {
            int rgb = ((b << 16) | (b << 8)) | b;
            m.add(new org.jhotdraw.draw.action.ColorIcon(firstColumn[i]));
            for (int r = 0x99; r <= 0xff; r += 0x33) {
                for (int g = 0; g <= 0xff; g += 0x33) {
                    rgb = ((0xff000000 | (r << 16)) | (g << 8)) | b;
                    m.add(new org.jhotdraw.draw.action.ColorIcon(rgb, "#" + java.lang.Integer.toHexString(rgb).substring(2)));
                }
            }
        }
        WEBSAVE_COLORS = java.util.Collections.unmodifiableList(m);
    }

    public static final int WEBSAVE_COLORS_COLUMN_COUNT = 19;

    /**
     * HSB color palette with a set of colors chosen based on a physical criteria.
     *
     * <p>
     *
     * <p>This is a 'human friendly' color palette which arranges the color in a way that makes it
     * easy for humans to select the desired color. The colors are ordered in a way which minimizes
     * the color contrast effect in the human visual system.
     *
     * <p>
     *
     * <p>This palette has 12 columns and 10 rows.
     *
     * <p>
     *
     * <p>The topmost row contains a null-color and a gray scale from white to black in 10 percent
     * steps.
     *
     * <p>
     *
     * <p>The remaining rows contain colors taken from the outer hull of the HSB color model:
     *
     * <p>
     *
     * <p>The columns are ordered by hue starting with red - the lowest wavelength - and ending with
     * purple - the highest wavelength. There are 12 different hues, so that all primary colors with
     * their additive complements can be selected.
     *
     * <p>
     *
     * <p>The rows are orderd by brightness with the brightest color at the top (sky) and the darkest
     * color at the bottom (earth). The first 5 rows contain colors with maximal brightness and a
     * saturation ranging form 20% up to 100%. The remaining 4 rows contain colors with maximal
     * saturation and a brightness ranging from 90% to 20% (this also makes for a range from 100% to
     * 20% if the 5th row is taken into account).
     */
    public static final java.util.List<org.jhotdraw.draw.action.ColorIcon> HSB_COLORS;

    public static final int HSB_COLORS_COLUMN_COUNT = 12;

    /**
     * This is the same palette as HSB_COLORS, but all color values are specified in the sRGB color
     * space.
     */
    public static final java.util.List<org.jhotdraw.draw.action.ColorIcon> HSB_COLORS_AS_RGB;

    public static final int HSB_COLORS_AS_RGB_COLUMN_COUNT = 12;

    static {
        java.awt.color.ColorSpace grayCS = java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY);
        org.jhotdraw.color.HSBColorSpace hsbCS = org.jhotdraw.color.HSBColorSpace.getInstance();
        java.util.List<org.jhotdraw.draw.action.ColorIcon> m = new java.util.ArrayList<>();
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        m.add(new org.jhotdraw.draw.action.ColorIcon(new java.awt.Color(0, true), labels.getToolTipTextProperty("attribute.color.noColor")));
        for (int b = 10; b >= 0; b--) {
            java.awt.Color c = new java.awt.Color(grayCS, new float[]{ b / 10.0F }, 1.0F);
            m.add(new org.jhotdraw.draw.action.ColorIcon(c, labels.getFormatted("attribute.color.grayComponents.toolTipText", b * 10)));
        }
        for (int s = 2; s <= 8; s += 2) {
            for (int h = 0; h < 12; h++) {
                java.awt.Color c = new java.awt.Color(hsbCS, new float[]{ h / 12.0F, s * 0.1F, 1.0F }, 1.0F);
                m.add(new org.jhotdraw.draw.action.ColorIcon(c, labels.getFormatted("attribute.color.hsbComponents.toolTipText", (h * 360) / 12, s * 10, 100)));
            }
        }
        for (int b = 10; b >= 2; b -= 2) {
            for (int h = 0; h < 12; h++) {
                java.awt.Color c = new java.awt.Color(hsbCS, new float[]{ h / 12.0F, 1.0F, b * 0.1F }, 1.0F);
                m.add(new org.jhotdraw.draw.action.ColorIcon(new java.awt.Color(hsbCS, new float[]{ h / 12.0F, 1.0F, b * 0.1F }, 1.0F), labels.getFormatted("attribute.color.hsbComponents.toolTipText", (h * 360) / 12, 100, b * 10)));
            }
        }
        HSB_COLORS = java.util.Collections.unmodifiableList(m);
        m = new java.util.ArrayList<>();
        for (org.jhotdraw.draw.action.ColorIcon ci : HSB_COLORS) {
            if (ci.getColor() == null) {
                m.add(new org.jhotdraw.draw.action.ColorIcon(new java.awt.Color(0, true), labels.getToolTipTextProperty("attribute.color.noColor")));
            } else {
                java.awt.Color c = ci.getColor();
                c = (c.getColorSpace() == grayCS) ? new java.awt.Color(c.getGreen(), c.getGreen(), c.getGreen(), c.getAlpha())// workaround for rounding error
                 : new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
                m.add(new org.jhotdraw.draw.action.ColorIcon(c, labels.getFormatted("attribute.color.rgbComponents.toolTipText", c.getRed(), c.getGreen(), c.getBlue())));
            }
        }
        HSB_COLORS_AS_RGB = java.util.Collections.unmodifiableList(m);
    }

    private static class ToolButtonListener implements java.awt.event.ItemListener {
        private org.jhotdraw.draw.tool.Tool tool;

        private org.jhotdraw.draw.DrawingEditor editor;

        public ToolButtonListener(org.jhotdraw.draw.tool.Tool t, org.jhotdraw.draw.DrawingEditor editor) {
            this.tool = t;
            this.editor = editor;
        }

        @java.lang.Override
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                editor.setTool(tool);
            }
        }
    }

    /**
     * Prevent instance creation.
     */
    private ButtonFactory() {
    }

    public static java.util.Collection<javax.swing.Action> createDrawingActions(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createDrawingActions(editor, new java.util.ArrayList<>());
    }

    public static java.util.Collection<javax.swing.Action> createDrawingActions(org.jhotdraw.draw.DrawingEditor editor, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        java.util.List<javax.swing.Action> list = new java.util.ArrayList<>();
        org.jhotdraw.draw.action.AbstractSelectedAction a;
        list.add(new org.jhotdraw.action.edit.CutAction());
        list.add(new org.jhotdraw.action.edit.CopyAction());
        list.add(new org.jhotdraw.action.edit.PasteAction());
        list.add(a = new org.jhotdraw.draw.action.SelectSameAction(editor));
        dsp.add(a);
        return list;
    }

    public static java.util.Collection<javax.swing.Action> createSelectionActions(org.jhotdraw.draw.DrawingEditor editor) {
        java.util.List<javax.swing.Action> a = new java.util.ArrayList<>();
        a.add(new org.jhotdraw.action.edit.DuplicateAction());
        a.add(null);// separator

        a.add(new org.jhotdraw.draw.action.GroupAction(editor));
        a.add(new org.jhotdraw.draw.action.UngroupAction(editor));
        a.add(null);// separator

        a.add(new org.jhotdraw.draw.action.BringToFrontAction(editor));
        a.add(new org.jhotdraw.draw.action.SendToBackAction(editor));
        return a;
    }

    public static javax.swing.JToggleButton addSelectionToolTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor, org.jhotdraw.gui.action.ButtonFactory.createDrawingActions(editor), org.jhotdraw.gui.action.ButtonFactory.createSelectionActions(editor));
    }

    public static javax.swing.JToggleButton addSelectionToolTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor, java.util.Collection<javax.swing.Action> drawingActions, java.util.Collection<javax.swing.Action> selectionActions) {
        org.jhotdraw.draw.tool.Tool selectionTool = new org.jhotdraw.draw.tool.DelegationSelectionTool(drawingActions, selectionActions);
        return org.jhotdraw.gui.action.ButtonFactory.addSelectionToolTo(tb, editor, selectionTool);
    }

    public static javax.swing.JToggleButton addSelectionToolTo(javax.swing.JToolBar tb, final org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.tool.Tool selectionTool) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        javax.swing.JToggleButton t;
        org.jhotdraw.draw.tool.Tool tool;
        java.util.HashMap<java.lang.String, java.lang.Object> attributes;
        javax.swing.ButtonGroup group;
        if (tb.getClientProperty("toolButtonGroup") instanceof javax.swing.ButtonGroup) {
            group = ((javax.swing.ButtonGroup) (tb.getClientProperty("toolButtonGroup")));
        } else {
            group = new javax.swing.ButtonGroup();
            tb.putClientProperty("toolButtonGroup", group);
        }
        // Selection tool
        editor.setTool(selectionTool);
        t = new javax.swing.JToggleButton();
        final javax.swing.JToggleButton defaultToolButton = t;
        if (!(tb.getClientProperty("toolHandler") instanceof org.jhotdraw.draw.event.ToolListener)) {
            org.jhotdraw.draw.event.ToolListener toolHandler;
            toolHandler = new org.jhotdraw.draw.event.ToolAdapter() {
                @java.lang.Override
                public void toolDone(org.jhotdraw.draw.event.ToolEvent event) {
                    defaultToolButton.setSelected(true);
                }
            };
            tb.putClientProperty("toolHandler", toolHandler);
        }
        labels.configureToolBarButton(t, "selectionTool");
        t.setSelected(true);
        t.addItemListener(new org.jhotdraw.gui.action.ButtonFactory.ToolButtonListener(selectionTool, editor));
        t.setFocusable(false);
        group.add(t);
        tb.add(t);
        return t;
    }

    /**
     * Method addSelectionToolTo must have been invoked prior to this on the JToolBar.
     */
    public static javax.swing.JToggleButton addToolTo(javax.swing.JToolBar tb, org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.tool.Tool tool, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels) {
        javax.swing.ButtonGroup group = ((javax.swing.ButtonGroup) (tb.getClientProperty("toolButtonGroup")));
        org.jhotdraw.draw.event.ToolListener toolHandler = ((org.jhotdraw.draw.event.ToolListener) (tb.getClientProperty("toolHandler")));
        javax.swing.JToggleButton t = new javax.swing.JToggleButton();
        labels.configureToolBarButton(t, labelKey);
        t.addItemListener(new org.jhotdraw.gui.action.ButtonFactory.ToolButtonListener(tool, editor));
        t.setFocusable(false);
        tool.addToolListener(toolHandler);
        group.add(t);
        tb.add(t);
        return t;
    }

    public static void addZoomButtonsTo(javax.swing.JToolBar bar, final org.jhotdraw.draw.DrawingEditor editor) {
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createZoomButton(editor));
    }

    public static javax.swing.AbstractButton createZoomButton(final org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        final org.jhotdraw.gui.JPopupButton zoomPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(zoomPopupButton, "view.zoomFactor");
        zoomPopupButton.setFocusable(false);
        if (editor.getDrawingViews().size() == 0) {
            zoomPopupButton.setText("100 %");
        } else {
            zoomPopupButton.setText(((int) (editor.getDrawingViews().iterator().next().getScaleFactor() * 100)) + " %");
        }
        editor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // String constants are interned
                if (((evt.getPropertyName() == null) && (org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.draw.DrawingEditor.ACTIVE_VIEW_PROPERTY))) {
                    if (evt.getNewValue() == null) {
                        zoomPopupButton.setText("100 %");
                    } else {
                        zoomPopupButton.setText(((int) (editor.getActiveView().getScaleFactor() * 100)) + " %");
                    }
                }
            }
        });
        double[] factors = new double[]{ 16, 8, 5, 4, 3, 2, 1.5, 1.25, 1, 0.75, 0.5, 0.25, 0.1 };
        for (int i = 0; i < factors.length; i++) {
            zoomPopupButton.add(new org.jhotdraw.draw.action.ZoomEditorAction(editor, factors[i], zoomPopupButton) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    super.actionPerformed(e);
                    zoomPopupButton.setText(((int) (editor.getActiveView().getScaleFactor() * 100)) + " %");
                }
            });
        }
        // zoomPopupButton.setPreferredSize(new Dimension(16,16));
        zoomPopupButton.setFocusable(false);
        return zoomPopupButton;
    }

    public static javax.swing.AbstractButton createZoomButton(org.jhotdraw.draw.DrawingView view) {
        return org.jhotdraw.gui.action.ButtonFactory.createZoomButton(view, new double[]{ 5, 4, 3, 2, 1.5, 1.25, 1, 0.75, 0.5, 0.25, 0.1 });
    }

    public static javax.swing.AbstractButton createZoomButton(final org.jhotdraw.draw.DrawingView view, double[] factors) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        final org.jhotdraw.gui.JPopupButton zoomPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(zoomPopupButton, "view.zoomFactor");
        zoomPopupButton.setFocusable(false);
        zoomPopupButton.setText(((int) (view.getScaleFactor() * 100)) + " %");
        view.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // String constants are interned
                if ("scaleFactor".equals(evt.getPropertyName())) {
                    zoomPopupButton.setText(((int) (view.getScaleFactor() * 100)) + " %");
                }
            }
        });
        for (int i = 0; i < factors.length; i++) {
            zoomPopupButton.add(new org.jhotdraw.draw.action.ZoomAction(view, factors[i], zoomPopupButton) {
                private static final long serialVersionUID = 1L;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    super.actionPerformed(e);
                    zoomPopupButton.setText(((int) (view.getScaleFactor() * 100)) + " %");
                }
            });
        }
        // zoomPopupButton.setPreferredSize(new Dimension(16,16));
        zoomPopupButton.setFocusable(false);
        return zoomPopupButton;
    }

    /**
     * Creates toolbar buttons and adds them to the specified JToolBar
     */
    public static void addAttributesButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        javax.swing.JButton b;
        b = bar.add(new org.jhotdraw.draw.action.PickAttributesAction(editor));
        b.setFocusable(false);
        b = bar.add(new org.jhotdraw.draw.action.ApplyAttributesAction(editor));
        b.setFocusable(false);
        bar.addSeparator();
        org.jhotdraw.gui.action.ButtonFactory.addColorButtonsTo(bar, editor);
        bar.addSeparator();
        org.jhotdraw.gui.action.ButtonFactory.addStrokeButtonsTo(bar, editor);
        bar.addSeparator();
        org.jhotdraw.gui.action.ButtonFactory.addFontButtonsTo(bar, editor);
    }

    public static void addColorButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.gui.action.ButtonFactory.addColorButtonsTo(bar, editor, org.jhotdraw.gui.action.ButtonFactory.DEFAULT_COLORS, org.jhotdraw.gui.action.ButtonFactory.DEFAULT_COLORS_COLUMN_COUNT);
    }

    public static void addColorButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor, java.util.List<org.jhotdraw.draw.action.ColorIcon> colors, int columnCount) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, org.jhotdraw.draw.AttributeKeys.STROKE_COLOR, colors, columnCount, "attribute.strokeColor", labels, new java.util.HashMap<>()));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, org.jhotdraw.draw.AttributeKeys.FILL_COLOR, colors, columnCount, "attribute.fillColor", labels, new java.util.HashMap<>()));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, org.jhotdraw.draw.AttributeKeys.TEXT_COLOR, colors, columnCount, "attribute.textColor", labels, new java.util.HashMap<>()));
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Microsoft Office:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks on the action region, the default color of the DrawingEditor is
     *       applied to the selected figures.
     *   <li>When the user opens the popup menu, a color palette is displayed. Choosing a color from
     *       the palette changes the default color of the editor and also changes the color of the
     *       selected figures.
     *   <li>A rectangle on the color button displays the current default color of the DrawingEditor.
     *       The rectangle has the dimensions 1, 17, 20, 4 (x, y, width, height).
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a LinkedMap,
     * 		so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     */
    public static org.jhotdraw.gui.JPopupButton createEditorColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, null);
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Microsoft Office:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks on the action region, the default color of the DrawingEditor is
     *       applied to the selected figures.
     *   <li>When the user opens the popup menu, a color palette is displayed. Choosing a color from
     *       the palette changes the default color of the editor and also changes the color of the
     *       selected figures.
     *   <li>A rectangle on the color button displays the current default color of the DrawingEditor.
     *       The rectangle has the dimensions 1, 17, 20, 4 (x, y, width, height).
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a LinkedMap,
     * 		so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     */
    public static org.jhotdraw.gui.JPopupButton createEditorColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes) {
        return org.jhotdraw.gui.action.ButtonFactory.createEditorColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, defaultAttributes, new java.awt.Rectangle(1, 17, 20, 4));
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Microsoft Office:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks on the action region, the default color of the DrawingEditor is
     *       applied to the selected figures.
     *   <li>When the user opens the popup menu, a color palette is displayed. Choosing a color from
     *       the palette changes the default color of the editor and also changes the color of the
     *       selected figures.
     *   <li>A shape on the color button displays the current default color of the DrawingEditor.
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     * @param colorShape
     * 		This shape is superimposed on the icon of the button. The shape is drawn with
     * 		the default color of the DrawingEditor.
     */
    public static org.jhotdraw.gui.JPopupButton createEditorColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape) {
        final org.jhotdraw.gui.JPopupButton popupButton = new org.jhotdraw.gui.JPopupButton();
        popupButton.setPopupAlpha(1.0F);
        if (defaultAttributes == null) {
            defaultAttributes = new java.util.HashMap<>();
        }
        popupButton.setAction(new org.jhotdraw.draw.action.DefaultAttributeAction(editor, attributeKey, defaultAttributes), new java.awt.Rectangle(0, 0, 22, 22));
        popupButton.setColumnCount(columnCount, false);
        boolean hasNullColor = false;
        for (org.jhotdraw.draw.action.ColorIcon swatch : swatches) {
            org.jhotdraw.draw.action.AttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            java.awt.Color swatchColor = swatch.getColor();
            attributes.put(attributeKey, swatchColor);
            if ((swatchColor == null) || (swatchColor.getAlpha() == 0)) {
                hasNullColor = true;
            }
            popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attributes, labels.getToolTipTextProperty(labelKey), swatch));
            a.putValue(javax.swing.Action.SHORT_DESCRIPTION, swatch.getName());
            a.setUpdateEnabledState(false);
        }
        // No color
        if (!hasNullColor) {
            org.jhotdraw.draw.action.AttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            attributes.put(attributeKey, null);
            popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attributes, labels.getToolTipTextProperty("attribute.color.noColor"), new org.jhotdraw.draw.action.ColorIcon(null, labels.getToolTipTextProperty("attribute.color.noColor"), swatches.get(0).getIconWidth(), swatches.get(0).getIconHeight())));
            a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.noColor"));
            a.setUpdateEnabledState(false);
        }
        // Color chooser
        javax.swing.ImageIcon chooserIcon = new javax.swing.ImageIcon(org.jhotdraw.util.Images.createImage(org.jhotdraw.gui.action.ButtonFactory.class, "/org/jhotdraw/draw/action/images/attribute.color.colorChooser.png"));
        javax.swing.Action a;
        popupButton.add(a = new org.jhotdraw.draw.action.EditorColorChooserAction(editor, attributeKey, "color", chooserIcon, defaultAttributes));
        labels.configureToolBarButton(popupButton, labelKey);
        a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.colorChooser"));
        javax.swing.Icon icon = new org.jhotdraw.draw.action.EditorColorIcon(editor, attributeKey, labels.getLargeIconProperty(labelKey, org.jhotdraw.gui.action.ButtonFactory.class).getImage(), colorShape);
        popupButton.setIcon(icon);
        popupButton.setDisabledIcon(icon);
        popupButton.setFocusable(false);
        editor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                popupButton.repaint();
            }
        });
        return popupButton;
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Adobe Fireworks:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks at the button a popup menu with a color palette is displayed.
     *       Choosing a color from the palette changes the default color of the editor and also
     *       changes the color of the selected figures.
     *   <li>A shape on the color button displays the color of the selected figures. If no figures are
     *       selected, the default color of the DrawingEditor is displayed.
     *   <li>A rectangle on the color button displays the current default color of the DrawingEditor.
     *       The rectangle has the dimensions 1, 17, 20, 4 (x, y, width, height).
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     */
    public static org.jhotdraw.gui.JPopupButton createSelectionColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createSelectionColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, null);
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Adobe Fireworks:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks at the button a popup menu with a color palette is displayed.
     *       Choosing a color from the palette changes the default color of the editor and also
     *       changes the color of the selected figures.
     *   <li>A rectangle on the color button displays the current default color of the DrawingEditor.
     *       The rectangle has the dimensions 1, 17, 20, 4 (x, y, width, height).
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     */
    public static org.jhotdraw.gui.JPopupButton createSelectionColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes) {
        return org.jhotdraw.gui.action.ButtonFactory.createSelectionColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, defaultAttributes, new java.awt.Rectangle(1, 17, 20, 4));
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Adobe Fireworks:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks at the button a popup menu with a color palette is displayed.
     *       Choosing a color from the palette changes the default color of the editor and also
     *       changes the color of the selected figures.
     *   <li>A shape on the color button displays the color of the selected figures. If no figures are
     *       selected, the default color of the DrawingEditor is displayed.
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     * @param colorShape
     * 		This shape is superimposed on the icon of the button. The shape is drawn with
     * 		the default color of the DrawingEditor.
     */
    public static org.jhotdraw.gui.JPopupButton createSelectionColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape) {
        return org.jhotdraw.gui.action.ButtonFactory.createSelectionColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, defaultAttributes, colorShape, new java.util.ArrayList<>());
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button works like the color
     * button in Adobe Fireworks:
     *
     * <p>
     *
     * <ul>
     *   <li>When the user clicks at the button a popup menu with a color palette is displayed.
     *       Choosing a color from the palette changes the default color of the editor and also
     *       changes the color of the selected figures.
     *   <li>A shape on the color button displays the color of the selected figures. If no figures are
     *       selected, the default color of the DrawingEditor is displayed.
     * </ul>
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     * @param colorShape
     * 		This shape is superimposed on the icon of the button. The shape is drawn with
     * 		the default color of the DrawingEditor.
     */
    public static org.jhotdraw.gui.JPopupButton createSelectionColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        final org.jhotdraw.gui.JPopupButton popupButton = new org.jhotdraw.gui.JPopupButton();
        popupButton.setPopupAlpha(1.0F);
        if (defaultAttributes == null) {
            defaultAttributes = new java.util.HashMap<>();
        }
        popupButton.setColumnCount(columnCount, false);
        boolean hasNullColor = false;
        for (org.jhotdraw.draw.action.ColorIcon swatch : swatches) {
            org.jhotdraw.draw.action.AttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            if (swatch != null) {
                java.awt.Color swatchColor = swatch.getColor();
                attributes.put(attributeKey, swatchColor);
                if ((swatchColor == null) || (swatchColor.getAlpha() == 0)) {
                    hasNullColor = true;
                }
                popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attributes, labels.getToolTipTextProperty(labelKey), swatch));
                a.putValue(javax.swing.Action.SHORT_DESCRIPTION, swatch.getName());
                a.setUpdateEnabledState(false);
                dsp.add(a);
            } else {
                popupButton.add(new javax.swing.JPanel());
            }
        }
        // No color
        if (!hasNullColor) {
            org.jhotdraw.draw.action.AttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            attributes.put(attributeKey, null);
            popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attributes, labels.getToolTipTextProperty("attribute.color.noColor"), new org.jhotdraw.draw.action.ColorIcon(null, labels.getToolTipTextProperty("attribute.color.noColor"))));
            a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.noColor"));
            a.setUpdateEnabledState(false);
            dsp.add(a);
        }
        // Color chooser
        javax.swing.ImageIcon chooserIcon = new javax.swing.ImageIcon(org.jhotdraw.util.Images.createImage(org.jhotdraw.gui.action.ButtonFactory.class, "/org/jhotdraw/draw/action/images/attribute.color.colorChooser.png"));
        org.jhotdraw.draw.action.AttributeAction a;
        popupButton.add(a = new org.jhotdraw.draw.action.SelectionColorChooserAction(editor, attributeKey, labels.getToolTipTextProperty("attribute.color.colorChooser"), chooserIcon, defaultAttributes));
        a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.colorChooser"));
        dsp.add(a);
        labels.configureToolBarButton(popupButton, labelKey);
        javax.swing.Icon icon = new org.jhotdraw.draw.action.SelectionColorIcon(editor, attributeKey, labels.getLargeIconProperty(labelKey, org.jhotdraw.gui.action.ButtonFactory.class).getImage(), colorShape);
        popupButton.setIcon(icon);
        popupButton.setDisabledIcon(icon);
        popupButton.setFocusable(false);
        dsp.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, popupButton));
        return popupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createSelectionColorChooserButton(final org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, final java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        return org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, attributeKey, labelKey, labels, defaultAttributes, colorShape, null, dsp);
    }

    public static org.jhotdraw.gui.JPopupButton createSelectionColorChooserButton(final org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, final java.lang.Class<?> uiclass, final java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton popupButton;
        popupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(popupButton, labelKey);
        popupButton.setFocusable(true);
        popupButton.setRequestFocusEnabled(false);
        // We lazily initialize the popup menu because creating a JColorChooser
        // takes a lot of time.
        org.jhotdraw.gui.JComponentPopup popupMenu = new org.jhotdraw.gui.JComponentPopup() {
            private static final long serialVersionUID = 1L;

            private javax.swing.JColorChooser colorChooser;

            @java.lang.Override
            public void show(java.awt.Component invoker, int x, int y) {
                if (colorChooser == null) {
                    initialize();
                }
                java.awt.Color c;
                if ((editor.getActiveView() != null) && (editor.getActiveView().getSelectionCount() > 0)) {
                    c = editor.getActiveView().getSelectedFigures().iterator().next().attr().get(attributeKey);
                } else {
                    c = editor.getDefaultAttribute(attributeKey);
                }
                colorChooser.setColor(c == null ? new java.awt.Color(0, true) : c);
                super.show(invoker, x, y);
            }

            private void initialize() {
                colorChooser = new javax.swing.JColorChooser();
                colorChooser.setOpaque(true);
                colorChooser.setBackground(java.awt.Color.WHITE);
                if (uiclass != null) {
                    try {
                        colorChooser.setUI(((javax.swing.plaf.ColorChooserUI) (org.jhotdraw.util.Methods.invokeStatic(uiclass, "createUI", new java.lang.Class<?>[]{ javax.swing.JComponent.class }, new java.lang.Object[]{ colorChooser }))));
                    } catch (java.lang.NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                }
                dsp.add(new org.jhotdraw.draw.action.SelectionColorChooserHandler(editor, attributeKey, colorChooser, this));
                add(colorChooser);
            }
        };
        popupButton.setPopupMenu(popupMenu);
        popupButton.setPopupAlpha(1.0F);// must be set after we set the popup menu

        javax.swing.Icon icon = new org.jhotdraw.draw.action.SelectionColorIcon(editor, attributeKey, labels.getLargeIconProperty(labelKey, org.jhotdraw.gui.action.ButtonFactory.class).getImage(), colorShape);
        popupButton.setIcon(icon);
        popupButton.setDisabledIcon(icon);
        popupButton.setFocusable(false);
        if (dsp != null) {
            dsp.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, popupButton));
        }
        return popupButton;
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button acts on attributes
     * of the Drawing object in the current DrawingView of the DrawingEditor.
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     */
    public static org.jhotdraw.gui.JPopupButton createDrawingColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createDrawingColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, null);
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button acts on attributes
     * of the Drawing object in the current DrawingView of the DrawingEditor.
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     */
    public static org.jhotdraw.gui.JPopupButton createDrawingColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes) {
        return org.jhotdraw.gui.action.ButtonFactory.createDrawingColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, defaultAttributes, new java.awt.Rectangle(1, 17, 20, 4));
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button acts on attributes
     * of the Drawing object in the current DrawingView of the DrawingEditor.
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     * @param colorShape
     * 		This shape is superimposed on the icon of the button. The shape is drawn with
     * 		the default color of the DrawingEditor.
     */
    public static org.jhotdraw.gui.JPopupButton createDrawingColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape) {
        return org.jhotdraw.gui.action.ButtonFactory.createDrawingColorButton(editor, attributeKey, swatches, columnCount, labelKey, labels, defaultAttributes, colorShape, new java.util.ArrayList<>());
    }

    /**
     * Creates a color button, with an action region and a popup menu. The button acts on attributes
     * of the Drawing object in the current DrawingView of the DrawingEditor.
     *
     * @param editor
     * 		The DrawingEditor.
     * @param attributeKey
     * 		The AttributeKey of the color.
     * @param swatches
     * 		A list with labeled colors containing the color palette of the popup menu. The
     * 		actual labels are retrieved from the supplied resource bundle. This is usually a
     * 		LinkedHashMap, so that the colors have a predictable order.
     * @param columnCount
     * 		The number of columns of the color palette.
     * @param labelKey
     * 		The resource bundle key used for retrieving the icon and the tooltip of the
     * 		button.
     * @param labels
     * 		The resource bundle.
     * @param defaultAttributes
     * 		A set of attributes which are also applied to the selected figures,
     * 		when a color is selected. This can be used, to set attributes that otherwise prevent the
     * 		color from being shown. For example, when the color attribute is set, we wan't the gradient
     * 		attribute of the Figure to be cleared.
     * @param colorShape
     * 		This shape is superimposed on the icon of the button. The shape is drawn with
     * 		the default color of the DrawingEditor.
     */
    public static org.jhotdraw.gui.JPopupButton createDrawingColorButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.util.List<org.jhotdraw.draw.action.ColorIcon> swatches, int columnCount, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        final org.jhotdraw.gui.JPopupButton popupButton = new org.jhotdraw.gui.JPopupButton();
        popupButton.setPopupAlpha(1.0F);
        if (defaultAttributes == null) {
            defaultAttributes = new java.util.HashMap<>();
        }
        popupButton.setColumnCount(columnCount, false);
        boolean hasNullColor = false;
        for (org.jhotdraw.draw.action.ColorIcon swatch : swatches) {
            org.jhotdraw.draw.action.DrawingAttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            if (swatch != null) {
                java.awt.Color swatchColor = swatch.getColor();
                attributes.put(attributeKey, swatchColor);
                if ((swatchColor == null) || (swatchColor.getAlpha() == 0)) {
                    hasNullColor = true;
                }
                popupButton.add(a = new org.jhotdraw.draw.action.DrawingAttributeAction(editor, attributes, labels.getToolTipTextProperty(labelKey), swatch));
                dsp.add(a);
                a.putValue(javax.swing.Action.SHORT_DESCRIPTION, swatch.getName());
                a.setUpdateEnabledState(false);
            } else {
                popupButton.add(new javax.swing.JPanel());
            }
        }
        // No color
        if (!hasNullColor) {
            org.jhotdraw.draw.action.DrawingAttributeAction a;
            java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attributes = new java.util.HashMap<>(defaultAttributes);
            attributes.put(attributeKey, null);
            popupButton.add(a = new org.jhotdraw.draw.action.DrawingAttributeAction(editor, attributes, labels.getToolTipTextProperty("attribute.color.noColor"), new org.jhotdraw.draw.action.ColorIcon(null, labels.getToolTipTextProperty("attribute.color.noColor"))));
            dsp.add(a);
            a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.noColor"));
            a.setUpdateEnabledState(false);
        }
        // Color chooser
        javax.swing.ImageIcon chooserIcon = new javax.swing.ImageIcon(org.jhotdraw.util.Images.createImage(org.jhotdraw.gui.action.ButtonFactory.class, "/org/jhotdraw/draw/action/images/attribute.color.colorChooser.png"));
        org.jhotdraw.draw.action.DrawingColorChooserAction a;
        popupButton.add(a = new org.jhotdraw.draw.action.DrawingColorChooserAction(editor, attributeKey, "color", chooserIcon, defaultAttributes));
        dsp.add(a);
        labels.configureToolBarButton(popupButton, labelKey);
        a.putValue(javax.swing.Action.SHORT_DESCRIPTION, labels.getToolTipTextProperty("attribute.color.colorChooser"));
        javax.swing.Icon icon = new org.jhotdraw.draw.action.DrawingColorIcon(editor, attributeKey, labels.getLargeIconProperty(labelKey, org.jhotdraw.gui.action.ButtonFactory.class).getImage(), colorShape);
        popupButton.setIcon(icon);
        popupButton.setDisabledIcon(icon);
        popupButton.setFocusable(false);
        if (editor != null) {
            dsp.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, popupButton));
        }
        return popupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createDrawingColorChooserButton(final org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, final java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        return org.jhotdraw.gui.action.ButtonFactory.createSelectionColorChooserButton(editor, attributeKey, labelKey, labels, defaultAttributes, colorShape, null, dsp);
    }

    public static org.jhotdraw.gui.JPopupButton createDrawingColorChooserButton(final org.jhotdraw.draw.DrawingEditor editor, final org.jhotdraw.draw.AttributeKey<java.awt.Color> attributeKey, java.lang.String labelKey, org.jhotdraw.util.ResourceBundleUtil labels, java.util.Map<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> defaultAttributes, java.awt.Shape colorShape, final java.lang.Class<?> uiclass, final java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton popupButton;
        popupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(popupButton, labelKey);
        popupButton.setFocusable(true);
        popupButton.setRequestFocusEnabled(false);
        // We lazily initialize the popup menu because creating a JColorChooser
        // takes a lot of time.
        org.jhotdraw.gui.JComponentPopup popupMenu = new org.jhotdraw.gui.JComponentPopup() {
            private static final long serialVersionUID = 1L;

            private javax.swing.JColorChooser colorChooser;

            @java.lang.Override
            public void show(java.awt.Component invoker, int x, int y) {
                if (colorChooser == null) {
                    initialize();
                }
                java.awt.Color c;
                if (editor.getActiveView() != null) {
                    c = editor.getActiveView().getDrawing().attr().get(attributeKey);
                } else {
                    c = editor.getDefaultAttribute(attributeKey);
                }
                colorChooser.setColor(c == null ? new java.awt.Color(0, true) : c);
                super.show(invoker, x, y);
            }

            private void initialize() {
                colorChooser = new javax.swing.JColorChooser();
                colorChooser.setOpaque(true);
                colorChooser.setBackground(java.awt.Color.WHITE);
                if (uiclass != null) {
                    try {
                        colorChooser.setUI(((javax.swing.plaf.ColorChooserUI) (org.jhotdraw.util.Methods.invokeStatic(uiclass, "createUI", new java.lang.Class<?>[]{ javax.swing.JComponent.class }, new java.lang.Object[]{ colorChooser }))));
                    } catch (java.lang.NoSuchMethodException ex) {
                        ex.printStackTrace();
                    }
                }
                dsp.add(new org.jhotdraw.draw.action.DrawingColorChooserHandler(editor, attributeKey, colorChooser, this));
                add(colorChooser);
            }
        };
        popupButton.setPopupMenu(popupMenu);
        popupButton.setPopupAlpha(1.0F);// must be set after we set the popup menu

        javax.swing.Icon icon = new org.jhotdraw.draw.action.DrawingColorIcon(editor, attributeKey, labels.getLargeIconProperty(labelKey, org.jhotdraw.gui.action.ButtonFactory.class).getImage(), colorShape);
        popupButton.setIcon(icon);
        popupButton.setDisabledIcon(icon);
        popupButton.setFocusable(false);
        if (dsp != null) {
            dsp.add(new org.jhotdraw.draw.event.SelectionComponentRepainter(editor, popupButton));
        }
        return popupButton;
    }

    public static void addStrokeButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeDecorationButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeWidthButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeTypeButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokePlacementButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeWidthButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeWidthButton(editor, new double[]{ 0.0, 0.5, 1.0, 2.0, 3.0, 5.0, 9.0, 13.0 }, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeWidthButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeWidthButton(editor, new double[]{ 0.5, 1.0, 2.0, 3.0, 5.0, 9.0, 13.0 }, labels);
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeWidthButton(org.jhotdraw.draw.DrawingEditor editor, double[] widths) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeWidthButton(editor, new double[]{ 0.5, 1.0, 2.0, 3.0, 5.0, 9.0, 13.0 }, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeWidthButton(org.jhotdraw.draw.DrawingEditor editor, double[] widths, org.jhotdraw.util.ResourceBundleUtil labels) {
        org.jhotdraw.gui.JPopupButton strokeWidthPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(strokeWidthPopupButton, "attribute.strokeWidth");
        strokeWidthPopupButton.setFocusable(false);
        java.text.NumberFormat formatter = java.text.NumberFormat.getInstance();
        if (formatter instanceof java.text.DecimalFormat) {
            ((java.text.DecimalFormat) (formatter)).setMaximumFractionDigits(1);
            ((java.text.DecimalFormat) (formatter)).setMinimumFractionDigits(0);
        }
        for (int i = 0; i < widths.length; i++) {
            java.lang.String label = java.lang.Double.toString(widths[i]);
            javax.swing.Icon icon = new org.jhotdraw.draw.action.StrokeIcon(new java.awt.BasicStroke(((float) (widths[i])), java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL));
            org.jhotdraw.draw.action.AttributeAction a = new org.jhotdraw.draw.action.AttributeAction(editor, org.jhotdraw.draw.AttributeKeys.STROKE_WIDTH, widths[i], label, icon);
            a.putValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY, labels.getString("attribute.strokeWidth.text"));
            javax.swing.AbstractButton btn = strokeWidthPopupButton.add(a);
            btn.setDisabledIcon(icon);
        }
        return strokeWidthPopupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDecorationButton(org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.JPopupButton strokeDecorationPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(strokeDecorationPopupButton, "attribute.strokeDecoration");
        strokeDecorationPopupButton.setFocusable(false);
        strokeDecorationPopupButton.setColumnCount(2, false);
        org.jhotdraw.draw.decoration.LineDecoration[] decorations = new org.jhotdraw.draw.decoration.LineDecoration[]{ // Arrow
        new org.jhotdraw.draw.decoration.ArrowTip(0.35, 12, 11.3), // Arrow
        new org.jhotdraw.draw.decoration.ArrowTip(0.35, 13, 7), // Generalization triangle
        new org.jhotdraw.draw.decoration.ArrowTip(java.lang.Math.PI / 5, 12, 9.8, true, true, false), // Dependency arrow
        new org.jhotdraw.draw.decoration.ArrowTip(java.lang.Math.PI / 6, 12, 0, false, true, false), // Link arrow
        new org.jhotdraw.draw.decoration.ArrowTip(java.lang.Math.PI / 11, 13, 0, false, true, true), // Aggregation diamond
        new org.jhotdraw.draw.decoration.ArrowTip(java.lang.Math.PI / 6, 10, 18, false, true, false), // Composition diamond
        new org.jhotdraw.draw.decoration.ArrowTip(java.lang.Math.PI / 6, 10, 18, true, true, true), null };
        for (org.jhotdraw.draw.decoration.LineDecoration decoration : decorations) {
            strokeDecorationPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, org.jhotdraw.draw.AttributeKeys.START_DECORATION, decoration, null, new org.jhotdraw.draw.action.LineDecorationIcon(decoration, true)));
            strokeDecorationPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, org.jhotdraw.draw.AttributeKeys.END_DECORATION, decoration, null, new org.jhotdraw.draw.action.LineDecorationIcon(decoration, false)));
        }
        return strokeDecorationPopupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDashesButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDashesButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, new double[][]{ null, new double[]{ 4.0, 4.0 }, new double[]{ 2.0, 2.0 }, new double[]{ 4.0, 2.0 }, new double[]{ 2.0, 4.0 }, new double[]{ 8.0, 2.0 }, new double[]{ 6.0, 2.0, 2.0, 2.0 } }, labels);
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDashesButton(org.jhotdraw.draw.DrawingEditor editor, double[][] dashes) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, dashes, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDashesButton(org.jhotdraw.draw.DrawingEditor editor, double[][] dashes, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeDashesButton(editor, dashes, labels, new java.util.ArrayList<>());
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeDashesButton(org.jhotdraw.draw.DrawingEditor editor, double[][] dashes, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton strokeDashesPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(strokeDashesPopupButton, "attribute.strokeDashes");
        strokeDashesPopupButton.setFocusable(false);
        // strokeDashesPopupButton.setColumnCount(2, false);
        for (double[] dashe : dashes) {
            float[] fdashes;
            if (dashe == null) {
                fdashes = null;
            } else {
                fdashes = new float[dashe.length];
                for (int j = 0; j < dashe.length; j++) {
                    fdashes[j] = ((float) (dashe[j]));
                }
            }
            javax.swing.Icon icon = new org.jhotdraw.draw.action.StrokeIcon(new java.awt.BasicStroke(2.0F, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL, 10.0F, fdashes, 0));
            org.jhotdraw.draw.action.AttributeAction a;
            javax.swing.AbstractButton btn = strokeDashesPopupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, org.jhotdraw.draw.AttributeKeys.STROKE_DASHES, dashe, null, icon));
            dsp.add(a);
            btn.setDisabledIcon(icon);
        }
        return strokeDashesPopupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeTypeButton(org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.JPopupButton strokeTypePopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(strokeTypePopupButton, "attribute.strokeType");
        strokeTypePopupButton.setFocusable(false);
        strokeTypePopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, org.jhotdraw.draw.AttributeKeys.StrokeType.BASIC, labels.getString("attribute.strokeType.basic"), new org.jhotdraw.draw.action.StrokeIcon(new java.awt.BasicStroke(1, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_BEVEL))));
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, org.jhotdraw.draw.AttributeKeys.StrokeType.DOUBLE);
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 2.0);
        strokeTypePopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeType.double"), new org.jhotdraw.draw.action.StrokeIcon(new org.jhotdraw.geom.DoubleStroke(2, 1))));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, org.jhotdraw.draw.AttributeKeys.StrokeType.DOUBLE);
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 3.0);
        strokeTypePopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeType.double"), new org.jhotdraw.draw.action.StrokeIcon(new org.jhotdraw.geom.DoubleStroke(3, 1))));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_TYPE, org.jhotdraw.draw.AttributeKeys.StrokeType.DOUBLE);
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_INNER_WIDTH_FACTOR, 4.0);
        strokeTypePopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeType.double"), new org.jhotdraw.draw.action.StrokeIcon(new org.jhotdraw.geom.DoubleStroke(4, 1))));
        return strokeTypePopupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokePlacementButton(org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        org.jhotdraw.gui.JPopupButton strokePlacementPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(strokePlacementPopupButton, "attribute.strokePlacement");
        strokePlacementPopupButton.setFocusable(false);
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attr;
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.CENTER);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.CENTER);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.center"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.INSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.CENTER);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.inside"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.OUTSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.CENTER);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.outside"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.CENTER);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.FULL);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.centerFilled"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.INSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.FULL);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.insideFilled"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.OUTSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.FULL);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.outsideFilled"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.CENTER);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.NONE);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.centerUnfilled"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.INSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.NONE);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.insideUnfilled"), null));
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_PLACEMENT, org.jhotdraw.draw.AttributeKeys.StrokePlacement.OUTSIDE);
        attr.put(org.jhotdraw.draw.AttributeKeys.FILL_UNDER_STROKE, org.jhotdraw.draw.AttributeKeys.Underfill.NONE);
        strokePlacementPopupButton.add(new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokePlacement.outsideUnfilled"), null));
        return strokePlacementPopupButton;
    }

    public static void addFontButtonsTo(javax.swing.JToolBar bar, org.jhotdraw.draw.DrawingEditor editor) {
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createFontStyleBoldButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createFontStyleItalicButton(editor));
        bar.add(org.jhotdraw.gui.action.ButtonFactory.createFontStyleUnderlineButton(editor));
    }

    public static org.jhotdraw.gui.JPopupButton createFontButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createFontButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor, org.jhotdraw.draw.AttributeKeys.FONT_FACE, labels);
    }

    public static org.jhotdraw.gui.JPopupButton createFontButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Font> key, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontButton(editor, key, labels, new java.util.ArrayList<>());
    }

    public static org.jhotdraw.gui.JPopupButton createFontButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.draw.AttributeKey<java.awt.Font> key, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton fontPopupButton;
        fontPopupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(fontPopupButton, "attribute.font");
        fontPopupButton.setFocusable(false);
        org.jhotdraw.gui.JComponentPopup popupMenu = new org.jhotdraw.gui.JComponentPopup();
        org.jhotdraw.gui.JFontChooser fontChooser = new org.jhotdraw.gui.JFontChooser();
        dsp.add(new org.jhotdraw.gui.action.FontChooserHandler(editor, key, fontChooser, popupMenu));
        popupMenu.add(fontChooser);
        fontPopupButton.setPopupMenu(popupMenu);
        fontPopupButton.setFocusable(false);
        return fontPopupButton;
    }

    public static javax.swing.JButton createFontStyleBoldButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleBoldButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static javax.swing.JButton createFontStyleBoldButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleBoldButton(editor, labels, new java.util.ArrayList<>());
    }

    public static javax.swing.JButton createFontStyleBoldButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        javax.swing.JButton btn;
        btn = new javax.swing.JButton();
        labels.configureToolBarButton(btn, "attribute.fontStyle.bold");
        btn.setFocusable(false);
        javax.swing.AbstractAction a = new org.jhotdraw.draw.action.AttributeToggler<>(editor, org.jhotdraw.draw.AttributeKeys.FONT_BOLD, java.lang.Boolean.TRUE, java.lang.Boolean.FALSE, new javax.swing.text.StyledEditorKit.BoldAction());
        a.putValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY, labels.getString("attribute.fontStyle.bold.text"));
        btn.addActionListener(a);
        return btn;
    }

    public static javax.swing.JButton createFontStyleItalicButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleItalicButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static javax.swing.JButton createFontStyleItalicButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleItalicButton(editor, labels, new java.util.ArrayList<>());
    }

    public static javax.swing.JButton createFontStyleItalicButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        javax.swing.JButton btn;
        btn = new javax.swing.JButton();
        labels.configureToolBarButton(btn, "attribute.fontStyle.italic");
        btn.setFocusable(false);
        javax.swing.AbstractAction a = new org.jhotdraw.draw.action.AttributeToggler<>(editor, org.jhotdraw.draw.AttributeKeys.FONT_ITALIC, java.lang.Boolean.TRUE, java.lang.Boolean.FALSE, new javax.swing.text.StyledEditorKit.BoldAction());
        a.putValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY, labels.getString("attribute.fontStyle.italic.text"));
        btn.addActionListener(a);
        return btn;
    }

    public static javax.swing.JButton createFontStyleUnderlineButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleUnderlineButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static javax.swing.JButton createFontStyleUnderlineButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createFontStyleUnderlineButton(editor, labels, new java.util.ArrayList<>());
    }

    public static javax.swing.JButton createFontStyleUnderlineButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        javax.swing.JButton btn;
        btn = new javax.swing.JButton();
        labels.configureToolBarButton(btn, "attribute.fontStyle.underline");
        btn.setFocusable(false);
        javax.swing.AbstractAction a = new org.jhotdraw.draw.action.AttributeToggler<>(editor, org.jhotdraw.draw.AttributeKeys.FONT_UNDERLINE, java.lang.Boolean.TRUE, java.lang.Boolean.FALSE, new javax.swing.text.StyledEditorKit.BoldAction());
        a.putValue(org.jhotdraw.util.ActionUtil.UNDO_PRESENTATION_NAME_KEY, labels.getString("attribute.fontStyle.underline.text"));
        btn.addActionListener(a);
        return btn;
    }

    /**
     * Creates toolbar buttons and adds them to the specified JToolBar
     */
    public static void addAlignmentButtonsTo(javax.swing.JToolBar bar, final org.jhotdraw.draw.DrawingEditor editor) {
        org.jhotdraw.gui.action.ButtonFactory.addAlignmentButtonsTo(bar, editor, new java.util.ArrayList<>());
    }

    /**
     * Creates toolbar buttons and adds them to the specified JToolBar.
     */
    public static void addAlignmentButtonsTo(javax.swing.JToolBar bar, final org.jhotdraw.draw.DrawingEditor editor, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.draw.action.AbstractSelectedAction d;
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.West(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.East(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.Horizontal(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.North(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.South(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.AlignAction.Vertical(editor)).setFocusable(false);
        dsp.add(d);
        bar.addSeparator();
        bar.add(d = new org.jhotdraw.draw.action.MoveAction.West(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.MoveAction.East(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.MoveAction.North(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(d = new org.jhotdraw.draw.action.MoveAction.South(editor)).setFocusable(false);
        dsp.add(d);
        bar.addSeparator();
        bar.add(new org.jhotdraw.draw.action.BringToFrontAction(editor)).setFocusable(false);
        dsp.add(d);
        bar.add(new org.jhotdraw.draw.action.SendToBackAction(editor)).setFocusable(false);
        dsp.add(d);
    }

    /**
     * Creates a button which toggles between two GridConstrainer for a DrawingView.
     */
    public static javax.swing.AbstractButton createToggleGridButton(final org.jhotdraw.draw.DrawingView view) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels");
        final javax.swing.JToggleButton toggleButton;
        toggleButton = new javax.swing.JToggleButton();
        labels.configureToolBarButton(toggleButton, "view.toggleGrid");
        toggleButton.setFocusable(false);
        toggleButton.addItemListener(new java.awt.event.ItemListener() {
            @java.lang.Override
            public void itemStateChanged(java.awt.event.ItemEvent event) {
                view.setConstrainerVisible(toggleButton.isSelected());
                // view.getComponent().repaint();
            }
        });
        view.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // String constants are interned
                if (((evt.getPropertyName() == null) && (org.jhotdraw.draw.DrawingView.CONSTRAINER_VISIBLE_PROPERTY == null)) || ((evt.getPropertyName() != null) && evt.getPropertyName().equals(org.jhotdraw.draw.DrawingView.CONSTRAINER_VISIBLE_PROPERTY))) {
                    toggleButton.setSelected(view.isConstrainerVisible());
                }
            }
        });
        return toggleButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeCapButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeCapButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeCapButton(editor, labels, new java.util.ArrayList<>());
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeCapButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton popupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(popupButton, "attribute.strokeCap");
        popupButton.setFocusable(false);
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attr;
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_CAP, java.awt.BasicStroke.CAP_BUTT);
        org.jhotdraw.draw.action.AttributeAction a;
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeCap.butt"), null));
        dsp.add(a);
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_CAP, java.awt.BasicStroke.CAP_ROUND);
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeCap.round"), null));
        dsp.add(a);
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_CAP, java.awt.BasicStroke.CAP_SQUARE);
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeCap.square"), null));
        dsp.add(a);
        return popupButton;
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeJoinButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor, org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.draw.Labels"));
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeJoinButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels) {
        return org.jhotdraw.gui.action.ButtonFactory.createStrokeJoinButton(editor, labels, new java.util.ArrayList<>());
    }

    public static org.jhotdraw.gui.JPopupButton createStrokeJoinButton(org.jhotdraw.draw.DrawingEditor editor, org.jhotdraw.util.ResourceBundleUtil labels, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        org.jhotdraw.gui.JPopupButton popupButton = new org.jhotdraw.gui.JPopupButton();
        labels.configureToolBarButton(popupButton, "attribute.strokeJoin");
        popupButton.setFocusable(false);
        java.util.HashMap<org.jhotdraw.draw.AttributeKey<?>, java.lang.Object> attr;
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN, java.awt.BasicStroke.JOIN_BEVEL);
        org.jhotdraw.draw.action.AttributeAction a;
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeJoin.bevel"), null));
        dsp.add(a);
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN, java.awt.BasicStroke.JOIN_ROUND);
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeJoin.round"), null));
        dsp.add(a);
        attr = new java.util.HashMap<>();
        attr.put(org.jhotdraw.draw.AttributeKeys.STROKE_JOIN, java.awt.BasicStroke.JOIN_MITER);
        popupButton.add(a = new org.jhotdraw.draw.action.AttributeAction(editor, attr, labels.getString("attribute.strokeJoin.miter"), null));
        dsp.add(a);
        return popupButton;
    }

    public static javax.swing.JButton createPickAttributesButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createPickAttributesButton(editor, new java.util.ArrayList<>());
    }

    public static javax.swing.JButton createPickAttributesButton(org.jhotdraw.draw.DrawingEditor editor, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        javax.swing.JButton btn;
        org.jhotdraw.draw.action.AbstractSelectedAction d;
        btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.PickAttributesAction(editor));
        dsp.add(d);
        if (btn.getIcon() != null) {
            btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
        }
        btn.setHorizontalTextPosition(javax.swing.JButton.CENTER);
        btn.setVerticalTextPosition(javax.swing.JButton.BOTTOM);
        btn.setText(null);
        btn.setFocusable(false);
        return btn;
    }

    /**
     * Creates a button that applies the default attributes of the editor to the current selection.
     */
    public static javax.swing.JButton createApplyAttributesButton(org.jhotdraw.draw.DrawingEditor editor) {
        return org.jhotdraw.gui.action.ButtonFactory.createApplyAttributesButton(editor, new java.util.ArrayList<>());
    }

    public static javax.swing.JButton createApplyAttributesButton(org.jhotdraw.draw.DrawingEditor editor, java.util.List<org.jhotdraw.api.app.Disposable> dsp) {
        javax.swing.JButton btn;
        org.jhotdraw.draw.action.AbstractSelectedAction d;
        btn = new javax.swing.JButton(d = new org.jhotdraw.draw.action.ApplyAttributesAction(editor));
        dsp.add(d);
        if (btn.getIcon() != null) {
            btn.putClientProperty("hideActionText", java.lang.Boolean.TRUE);
        }
        btn.setHorizontalTextPosition(javax.swing.JButton.CENTER);
        btn.setVerticalTextPosition(javax.swing.JButton.BOTTOM);
        btn.setText(null);
        btn.setFocusable(false);
        return btn;
    }
}