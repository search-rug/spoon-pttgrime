/* @(#)JSheet.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.gui;
/**
 * JSheet is a document modal dialog which is displayed below the title bar of a JFrame.
 *
 * <p>A JSheet blocks input on its owner window, while it is visible.
 *
 * <p>Unlike application modal dialogs, the show method of a JSheet does return immediately, when
 * the JSheet has become visible. Applications need to use a SheetListener to get the return value
 * of a JSheet.
 *
 * <p>Requires Java 1.4.
 *
 * <p>Caveats: We are using an unsupported API call to make the JSheet translucent. This API may go
 * away in future versions of the Macintosh Runtime for Java. In such a case, we (hopefully) just
 * end up with a non-opaque sheet.
 */
public class JSheet extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;

    /**
     * Event listener list.
     */
    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    /**
     * This handler is used to handle movements of the owner. If the owner moves, we have to change
     * the location of the sheet as well.
     */
    private java.awt.event.ComponentListener ownerMovementHandler;

    /**
     * If this is non-null, we put the owner to the specified location, when the sheet is hidden.
     */
    private java.awt.Point shiftBackLocation;

    /**
     * We need to keep track of the old owner position, in order to avoid processing duplicate owner
     * moved events.
     */
    private java.awt.Point oldLocation;

    /**
     * Focus owner on the owner window, before the sheet is shown.
     */
    private java.awt.Component oldFocusOwner;

    /**
     * This is set to true, when the listeners for the JSheet are installed on the parent component.
     */
    private boolean isInstalled;

    /**
     * If this is set to true, the JSheet uses a transition effect when shown and when hidden.
     */
    private boolean isAnimated = true;

    /**
     * If this is set to true, the JSheet uses native Mac OS X sheets.
     */
    private static final boolean IS_NATIVE_SHEET_SUPPORTED;

    /**
     * If this is set to true, the JSheet uses native document modal dialogs.
     */
    private static final boolean IS_DOCUMENT_MODALITY_SUPPORTED;

    /**
     * This variable is only used in Java 1.5 and previous versions. In order to make the sheet
     * document modal, we have to block events on the owner window. We do this by setting a JPanel as
     * the glass pane on the owner window. Before we do this, we store the glass pane of the owner
     * window here, and restore it after we have finished.
     */
    private java.awt.Component ownersGlassPane;

    static {
        // SoyLatte doesn't properly support document modal dialogs yet.
        IS_DOCUMENT_MODALITY_SUPPORTED = (!java.lang.System.getProperty("os.name").equals("Darwin")) && (java.lang.System.getProperty("java.version").compareTo("1.6") >= 0);
        IS_NATIVE_SHEET_SUPPORTED = java.lang.System.getProperty("os.name").toLowerCase().startsWith("mac os x") && (java.lang.System.getProperty("java.version").compareTo("1.6") >= 0);
    }

    /**
     * Creates a new JSheet.
     */
    public JSheet(java.awt.Frame owner) {
        super(owner);
        init();
    }

    /**
     * Creates a new JSheet.
     */
    public JSheet(java.awt.Dialog owner) {
        super(owner);
        init();
    }

    private void init() {
        if ((getOwner() != null) && isShowAsSheet()) {
            if (org.jhotdraw.gui.JSheet.isNativeSheetSupported()) {
                setUndecorated(true);// Must be set to undecorated for J2SE7

                getRootPane().putClientProperty("apple.awt.documentModalSheet", java.lang.Boolean.TRUE);
            } else {
                setUndecorated(true);
                getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.NONE);
                getRootPane().setBorder(javax.swing.UIManager.getBorder("Sheet.border"));
            }
            if (org.jhotdraw.gui.JSheet.isDocumentModalitySupported()) {
                org.jhotdraw.util.Methods.invokeIfExistsWithEnum(this, "setModalityType", "java.awt.Dialog$ModalityType", "DOCUMENT_MODAL");
            }
        }
        // We move the sheet when the user moves the owner, so that it
        // will always stay centered below the title bar of the owner.
        // If the user has moved the owner, we 'forget' the shift back location,
        // and don't shift the owner back to the place it was, when we opened
        // the sheet.
        ownerMovementHandler = new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                java.awt.Window owner = getOwner();
                java.awt.Point newLocation = owner.getLocation();
                if (!newLocation.equals(oldLocation)) {
                    setLocation(newLocation.x + ((owner.getWidth() - getWidth()) / 2), newLocation.y + owner.getInsets().top);
                    shiftBackLocation = null;
                    oldLocation = newLocation;
                }
            }
        };
    }

    protected boolean isShowAsSheet() {
        return javax.swing.UIManager.getLookAndFeel().getID().equals("Aqua") || javax.swing.UIManager.getBoolean("Sheet.showAsSheet");
    }

    /**
     * Installs the sheet on the owner. This method is invoked just before the JSheet is shown.
     */
    protected void installSheet() {
        if (!isInstalled) {
            java.awt.Window owner = getOwner();
            if (owner != null) {
                // Determine the location for the sheet and its owner while
                // the sheet will be visible.
                // In case we have to shift the owner to fully display the
                // dialog, we remember the shift back position.
                java.awt.Point ownerLoc = owner.getLocation();
                java.awt.Point sheetLoc;
                if (isShowAsSheet()) {
                    if (owner instanceof javax.swing.JFrame) {
                        sheetLoc = new java.awt.Point(ownerLoc.x + ((owner.getWidth() - getWidth()) / 2), (ownerLoc.y + owner.getInsets().top) + ((javax.swing.JFrame) (owner)).getRootPane().getContentPane().getY());
                    } else if (owner instanceof javax.swing.JDialog) {
                        sheetLoc = new java.awt.Point(ownerLoc.x + ((owner.getWidth() - getWidth()) / 2), (ownerLoc.y + owner.getInsets().top) + ((javax.swing.JDialog) (owner)).getRootPane().getContentPane().getY());
                    } else {
                        sheetLoc = new java.awt.Point(ownerLoc.x + ((owner.getWidth() - getWidth()) / 2), ownerLoc.y + owner.getInsets().top);
                    }
                    if (sheetLoc.x < 0) {
                        owner.setLocation(ownerLoc.x - sheetLoc.x, ownerLoc.y);
                        sheetLoc.x = 0;
                        shiftBackLocation = ownerLoc;
                        oldLocation = owner.getLocation();
                    } else {
                        shiftBackLocation = null;
                        oldLocation = ownerLoc;
                    }
                } else {
                    sheetLoc = new java.awt.Point(ownerLoc.x + ((owner.getWidth() - getWidth()) / 2), ownerLoc.y + ((owner.getHeight() - getHeight()) / 3));
                }
                setLocation(sheetLoc);
                oldFocusOwner = owner.getFocusOwner();
                // Note: We mustn't change the windows focusable state because
                // this also affects the focusable state of the JSheet.
                // owner.setFocusableWindowState(false);
                owner.setEnabled(false);
                // ((JFrame) owner).setResizable(false);
                if (isShowAsSheet()) {
                    owner.addComponentListener(ownerMovementHandler);
                } else if (owner instanceof java.awt.Frame) {
                    setTitle(((java.awt.Frame) (owner)).getTitle());
                }
            }
            isInstalled = true;
        }
    }

    /**
     * Uninstalls the sheet on the owner. This method is invoked immediately after the JSheet is
     * hidden.
     */
    protected void uninstallSheet() {
        if (isInstalled) {
            java.awt.Window owner = getOwner();
            if (owner != null) {
                // Note: We mustn't change the windows focusable state because
                // this also affects the focusable state of the JSheet.
                // owner.setFocusableWindowState(true);
                owner.setEnabled(true);
                // ((JFrame) owner).setResizable(true);
                owner.removeComponentListener(ownerMovementHandler);
                if (shiftBackLocation != null) {
                    owner.setLocation(shiftBackLocation);
                }
                if (oldFocusOwner != null) {
                    owner.toFront();
                    oldFocusOwner.requestFocus();
                }
            }
            isInstalled = false;
        }
    }

    @java.lang.Override
    public void addNotify() {
        super.addNotify();
        if (javax.swing.UIManager.getBoolean("Sheet.showAsSheet")) {
            // QuaquaUtilities.setWindowAlpha(this, 240);
        }
    }

    /**
     * If this is set to true, the JSheet uses a transition effect when shown and when hidden.
     */
    public void setAnimated(boolean newValue) {
        boolean oldValue = isAnimated;
        isAnimated = newValue;
        firePropertyChange("animated", oldValue, newValue);
    }

    /**
     * If this returns true, the JSheet uses a transition effect when shown and when hidden.
     */
    public boolean isAnimated() {
        return isAnimated;
    }

    /**
     * If this returns true, the JSheet uses native support for sheet display.
     */
    private static boolean isNativeSheetSupported() {
        return org.jhotdraw.gui.JSheet.IS_NATIVE_SHEET_SUPPORTED;
    }

    /**
     * If this returns true, the JSheet uses native support for sheet display.
     */
    private static boolean isDocumentModalitySupported() {
        return org.jhotdraw.gui.JSheet.IS_DOCUMENT_MODALITY_SUPPORTED;
    }

    @java.lang.Override
    public void dispose() {
        super.dispose();
        uninstallSheet();
    }

    @java.lang.SuppressWarnings("deprecation")
    protected void hide0() {
        javax.swing.JRootPane rp = null;
        if (getOwner() instanceof javax.swing.JFrame) {
            rp = ((javax.swing.JFrame) (getOwner())).getRootPane();
        } else if (getOwner() instanceof javax.swing.JDialog) {
            rp = ((javax.swing.JDialog) (getOwner())).getRootPane();
        }
        if ((rp != null) && (!org.jhotdraw.gui.JSheet.isDocumentModalitySupported())) {
            java.awt.Component blockingComponent = rp.getGlassPane();
            blockingComponent.setVisible(false);
            if (ownersGlassPane != null) {
                rp.setGlassPane(ownersGlassPane);
                ownersGlassPane = null;
            }
        }
        super.hide();
    }

    @java.lang.SuppressWarnings("deprecation")
    protected void show0() {
        javax.swing.JRootPane rp = null;
        if (getOwner() instanceof javax.swing.JFrame) {
            rp = ((javax.swing.JFrame) (getOwner())).getRootPane();
        } else if (getOwner() instanceof javax.swing.JDialog) {
            rp = ((javax.swing.JDialog) (getOwner())).getRootPane();
        }
        if ((rp != null) && (!org.jhotdraw.gui.JSheet.isDocumentModalitySupported())) {
            ownersGlassPane = rp.getGlassPane();
            javax.swing.JPanel blockingPanel = new javax.swing.JPanel();
            blockingPanel.setOpaque(false);
            rp.setGlassPane(blockingPanel);
            blockingPanel.setVisible(true);
        }
        super.show();
    }

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public void hide() {
        if ((isAnimated() && isShowAsSheet()) && (!org.jhotdraw.gui.JSheet.isNativeSheetSupported())) {
            getContentPane().setVisible(false);
            final java.awt.Rectangle startBounds = getBounds();
            int parentWidth = getParent().getWidth();
            final java.awt.Rectangle endBounds = new java.awt.Rectangle(parentWidth < startBounds.width ? startBounds.x + ((startBounds.width - parentWidth) / 2) : startBounds.x, startBounds.y, java.lang.Math.min(startBounds.width, parentWidth), 0);
            final javax.swing.Timer timer = new javax.swing.Timer(20, null);
            timer.addActionListener(new java.awt.event.ActionListener() {
                long startTime;

                long endTime;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    long now = java.lang.System.currentTimeMillis();
                    if (startTime == 0) {
                        startTime = now;
                        endTime = startTime + 200;
                    }
                    if (now > endTime) {
                        timer.stop();
                        hide0();
                        setBounds(startBounds);
                        getContentPane().setVisible(true);
                        uninstallSheet();
                    } else {
                        float ratio = (now - startTime) / ((float) (endTime - startTime));
                        setBounds(((int) ((startBounds.x * (1.0F - ratio)) + (endBounds.x * ratio))), ((int) ((startBounds.y * (1.0F - ratio)) + (endBounds.y * ratio))), ((int) ((startBounds.width * (1.0F - ratio)) + (endBounds.width * ratio))), ((int) ((startBounds.height * (1.0F - ratio)) + (endBounds.height * ratio))));
                    }
                }
            });
            timer.setRepeats(true);
            timer.setInitialDelay(5);
            timer.start();
        } else {
            hide0();
            uninstallSheet();
        }
    }

    @java.lang.SuppressWarnings("deprecation")
    @java.lang.Override
    public void show() {
        if ((isAnimated() && isShowAsSheet()) && (!org.jhotdraw.gui.JSheet.isNativeSheetSupported())) {
            installSheet();
            getContentPane().setVisible(false);
            final java.awt.Rectangle endBounds = getBounds();
            int parentWidth = getParent().getWidth();
            final java.awt.Rectangle startBounds = new java.awt.Rectangle(parentWidth < endBounds.width ? endBounds.x + ((endBounds.width - parentWidth) / 2) : endBounds.x, endBounds.y, java.lang.Math.min(endBounds.width, parentWidth), 0);
            setBounds(startBounds);
            if (!org.jhotdraw.gui.JSheet.isDocumentModalitySupported()) {
                ((java.awt.Window) (getParent())).toFront();
            }
            show0();
            final javax.swing.Timer timer = new javax.swing.Timer(20, null);
            timer.addActionListener(new java.awt.event.ActionListener() {
                long startTime;

                long endTime;

                @java.lang.Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    long now = java.lang.System.currentTimeMillis();
                    if (startTime == 0) {
                        startTime = now;
                        endTime = startTime + 200;
                    }
                    if (now > endTime) {
                        timer.stop();
                        setBounds(endBounds);
                        getContentPane().setVisible(true);
                        java.awt.Component c = getFocusTraversalPolicy().getInitialComponent(JSheet.this);
                        if (c != null) {
                            c.requestFocus();
                        } else {
                            getContentPane().requestFocus();
                        }
                    } else {
                        float ratio = (now - startTime) / ((float) (endTime - startTime));
                        setBounds(((int) ((startBounds.x * (1.0F - ratio)) + (endBounds.x * ratio))), ((int) ((startBounds.y * (1.0F - ratio)) + (endBounds.y * ratio))), ((int) ((startBounds.width * (1.0F - ratio)) + (endBounds.width * ratio))), ((int) ((startBounds.height * (1.0F - ratio)) + (endBounds.height * ratio))));
                    }
                }
            });
            timer.setRepeats(true);
            timer.setInitialDelay(5);
            timer.start();
        } else {
            installSheet();
            show0();
        }
        org.jhotdraw.gui.JSheet.requestUserAttention(true);
    }

    /**
     * Requests attention from user. This is invoked when the sheet is opened.
     */
    public static void requestUserAttention(boolean requestCritical) {
        /* NSApplication app = NSApplication.sharedApplication();
        int id = app.requestUserAttention(
        NSApplication.UserAttentionRequestInformational);
         */
        /* try {
        Object app = Methods.invokeStatic("com.apple.cocoa.application.NSApplication", "sharedApplication");
        Methods.invoke(app, "requestUserAttention", app.getClass().getDeclaredField("UserAttentionRequestInformational").getInt(app));
        } catch (Throwable ex) {
        System.err.println("Quaqua Warning: Couldn't invoke NSApplication.requestUserAttention");
        }
         */
    }

    /**
     * Adds a sheet listener.
     */
    public void addSheetListener(org.jhotdraw.gui.event.SheetListener l) {
        listenerList.add(org.jhotdraw.gui.event.SheetListener.class, l);
    }

    /**
     * Removes a sheet listener.
     */
    public void removeSheetListener(org.jhotdraw.gui.event.SheetListener l) {
        listenerList.remove(org.jhotdraw.gui.event.SheetListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     */
    protected void fireOptionSelected(javax.swing.JOptionPane pane) {
        java.lang.Object value = pane.getValue();
        int option;
        if (value == null) {
            option = javax.swing.JOptionPane.CLOSED_OPTION;
        } else if (pane.getOptions() == null) {
            if (value instanceof java.lang.Integer) {
                option = ((java.lang.Integer) (value));
            } else {
                option = javax.swing.JOptionPane.CLOSED_OPTION;
            }
        } else {
            option = javax.swing.JOptionPane.CLOSED_OPTION;
            java.lang.Object[] options = pane.getOptions();
            for (int i = 0, n = options.length; i < n; i++) {
                if (options[i].equals(value)) {
                    option = i;
                    break;
                }
            }
            if (option == javax.swing.JOptionPane.CLOSED_OPTION) {
                value = null;
            }
        }
        fireOptionSelected(pane, option, value, pane.getInputValue());
    }

    /**
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     */
    protected void fireOptionSelected(javax.swing.JOptionPane pane, int option, java.lang.Object value, java.lang.Object inputValue) {
        org.jhotdraw.gui.event.SheetEvent sheetEvent = null;
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.gui.event.SheetListener.class) {
                // Lazily create the event:
                if (sheetEvent == null) {
                    sheetEvent = new org.jhotdraw.gui.event.SheetEvent(this, pane, option, value, inputValue);
                }
                ((org.jhotdraw.gui.event.SheetListener) (listeners[i + 1])).optionSelected(sheetEvent);
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     */
    protected void fireOptionSelected(javax.swing.JFileChooser pane, int option) {
        org.jhotdraw.gui.event.SheetEvent sheetEvent = null;
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.gui.event.SheetListener.class) {
                // Lazily create the event:
                if (sheetEvent == null) {
                    sheetEvent = new org.jhotdraw.gui.event.SheetEvent(this, pane, option, null);
                }
                ((org.jhotdraw.gui.event.SheetListener) (listeners[i + 1])).optionSelected(sheetEvent);
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on this event type. The
     * event instance is lazily created using the parameters passed into the fire method.
     */
    protected void fireOptionSelected(org.jhotdraw.api.gui.URIChooser pane, int option) {
        org.jhotdraw.gui.event.SheetEvent sheetEvent = null;
        // Guaranteed to return a non-null array
        java.lang.Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == org.jhotdraw.gui.event.SheetListener.class) {
                // Lazily create the event:
                if (sheetEvent == null) {
                    sheetEvent = new org.jhotdraw.gui.event.SheetEvent(this, pane, option, null);
                }
                ((org.jhotdraw.gui.event.SheetListener) (listeners[i + 1])).optionSelected(sheetEvent);
            }
        }
    }

    /**
     * Displays an option pane as a sheet on its parent window.
     *
     * @param pane
     * 		The option pane.
     * @param parentComponent
     * 		The parent of the option pane.
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showSheet(javax.swing.JOptionPane pane, java.awt.Component parentComponent, org.jhotdraw.gui.event.SheetListener listener) {
        final org.jhotdraw.gui.JSheet sheet = org.jhotdraw.gui.JSheet.createSheet(pane, parentComponent, org.jhotdraw.gui.JSheet.styleFromMessageType(pane.getMessageType()));
        sheet.addSheetListener(listener);
        sheet.show();
    }

    /**
     * Brings up a sheet with the options <i>Yes</i>, <i>No</i> and <i>Cancel</i>.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the sheet is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showConfirmSheet(java.awt.Component parentComponent, java.lang.Object message, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showConfirmSheet(parentComponent, message, javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, listener);
    }

    /**
     * Brings up a sheet where the number of choices is determined by the <code>optionType</code>
     * parameter.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the sheet is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param optionType
     * 		an int designating the options available on the dialog: <code>YES_NO_OPTION
     * 		</code>, or <code>YES_NO_CANCEL_OPTION</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showConfirmSheet(java.awt.Component parentComponent, java.lang.Object message, int optionType, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showConfirmSheet(parentComponent, message, optionType, javax.swing.JOptionPane.QUESTION_MESSAGE, listener);
    }

    /**
     * Brings up a sheet where the number of choices is determined by the <code>optionType</code>
     * parameter, where the <code>messageType</code> parameter determines the icon to display. The
     * <code>messageType</code> parameter is primarily used to supply a default icon from the Look and
     * Feel.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param optionType
     * 		an integer designating the options available on the dialog: <code>
     * 		YES_NO_OPTION</code>, or <code>YES_NO_CANCEL_OPTION</code>
     * @param messageType
     * 		an integer designating the kind of message this is; primarily used to
     * 		determine the icon from the pluggable Look and Feel: <code>JOptionPane.ERROR_MESSAGE</code>
     * 		, <code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showConfirmSheet(java.awt.Component parentComponent, java.lang.Object message, int optionType, int messageType, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showConfirmSheet(parentComponent, message, optionType, messageType, null, listener);
    }

    /**
     * Brings up a sheet with a specified icon, where the number of choices is determined by the
     * <code>optionType</code> parameter. The <code>messageType</code> parameter is primarily used to
     * supply a default icon from the look and feel.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the Object to display
     * @param optionType
     * 		an int designating the options available on the dialog: <code>YES_NO_OPTION
     * 		</code>, or <code>YES_NO_CANCEL_OPTION</code>
     * @param messageType
     * 		an int designating the kind of message this is, primarily used to determine
     * 		the icon from the pluggable Look and Feel: <code>JOptionPane.ERROR_MESSAGE</code>, <code>
     * 		JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>, <code>
     * 		JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param icon
     * 		the icon to display in the dialog
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showConfirmSheet(java.awt.Component parentComponent, java.lang.Object message, int optionType, int messageType, javax.swing.Icon icon, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showOptionSheet(parentComponent, message, optionType, messageType, icon, null, null, listener);
    }

    /**
     * Shows a question-message sheet requesting input from the user parented to <code>parentComponent
     * </code>.
     *
     * @param parentComponent
     * 		the parent <code>Component</code> for the dialog
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showInputSheet(java.awt.Component parentComponent, java.lang.Object message, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showInputSheet(parentComponent, message, javax.swing.JOptionPane.QUESTION_MESSAGE, listener);
    }

    /**
     * Shows a question-message sheet requesting input from the user and parented to <code>
     * parentComponent</code>. The input value will be initialized to <code>initialSelectionValue
     * </code>.
     *
     * @param parentComponent
     * 		the parent <code>Component</code> for the dialog
     * @param message
     * 		the <code>Object</code> to display
     * @param initialSelectionValue
     * 		the value used to initialize the input field
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showInputSheet(java.awt.Component parentComponent, java.lang.Object message, java.lang.Object initialSelectionValue, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showInputSheet(parentComponent, message, javax.swing.JOptionPane.QUESTION_MESSAGE, null, null, initialSelectionValue, listener);
    }

    /**
     * Shows a sheet requesting input from the user parented to <code>parentComponent</code> and
     * message type <code>messageType</code>.
     *
     * @param parentComponent
     * 		the parent <code>Component</code> for the dialog
     * @param message
     * 		the <code>Object</code> to display
     * @param messageType
     * 		the type of message that is to be displayed: <code>JOptionPane.ERROR_MESSAGE
     * 		</code>, <code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE
     * 		</code>, <code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE
     * 		</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showInputSheet(java.awt.Component parentComponent, java.lang.Object message, int messageType, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showInputSheet(parentComponent, message, messageType, null, null, null, listener);
    }

    /**
     * Prompts the user for input in a sheet where the initial selection, possible selections, and all
     * other options can be specified. The user will able to choose from <code>selectionValues</code>,
     * where <code>null</code> implies the user can input whatever they wish, usually by means of a
     * <code>JTextField</code>. <code>initialSelectionValue</code> is the initial value to prompt the
     * user with. It is up to the UI to decide how best to represent the <code>selectionValues</code>,
     * but usually a <code>JComboBox</code>, <code>JList</code>, or <code>JTextField</code> will be
     * used.
     *
     * @param parentComponent
     * 		the parent <code>Component</code> for the dialog
     * @param message
     * 		the <code>Object</code> to display
     * @param messageType
     * 		the type of message to be displayed: <code>JOptionPane.ERROR_MESSAGE</code>,
     * 		<code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param icon
     * 		the <code>Icon</code> image to display
     * @param selectionValues
     * 		an array of <code>Object</code>s that gives the possible selections
     * @param initialSelectionValue
     * 		the value used to initialize the input field
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showInputSheet(java.awt.Component parentComponent, java.lang.Object message, int messageType, javax.swing.Icon icon, java.lang.Object[] selectionValues, java.lang.Object initialSelectionValue, org.jhotdraw.gui.event.SheetListener listener) {
        javax.swing.JOptionPane pane = new javax.swing.JOptionPane(message, messageType, javax.swing.JOptionPane.OK_CANCEL_OPTION, icon, null, null);
        pane.setWantsInput(true);
        pane.setSelectionValues(selectionValues);
        pane.setInitialSelectionValue(initialSelectionValue);
        pane.setComponentOrientation((parentComponent == null ? javax.swing.JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());
        int style = org.jhotdraw.gui.JSheet.styleFromMessageType(messageType);
        org.jhotdraw.gui.JSheet sheet = org.jhotdraw.gui.JSheet.createSheet(pane, parentComponent, style);
        pane.selectInitialValue();
        /* sheet.addWindowListener(new WindowAdapter() {
        public void windowClosed(WindowEvent evt) {
        sheet.dispose();
        }
        });
         */
        sheet.addSheetListener(listener);
        sheet.show();
        sheet.toFront();
    }

    /**
     * Brings up an information-message sheet.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     */
    public static void showMessageSheet(java.awt.Component parentComponent, java.lang.Object message) {
        org.jhotdraw.gui.JSheet.showMessageSheet(parentComponent, message, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Brings up an information-message sheet.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param listener
     * 		This listener is notified when the sheet is dismissed.
     */
    public static void showMessageSheet(java.awt.Component parentComponent, java.lang.Object message, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showMessageSheet(parentComponent, message, javax.swing.JOptionPane.INFORMATION_MESSAGE, listener);
    }

    /**
     * Brings up a sheet that displays a message using a default icon determined by the <code>
     * messageType</code> parameter.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param messageType
     * 		the type of message to be displayed: <code>JOptionPane.ERROR_MESSAGE</code>,
     * 		<code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     */
    public static void showMessageSheet(java.awt.Component parentComponent, java.lang.Object message, int messageType) {
        org.jhotdraw.gui.JSheet.showMessageSheet(parentComponent, message, messageType, null, new org.jhotdraw.gui.event.SheetListener() {
            @java.lang.Override
            public void optionSelected(org.jhotdraw.gui.event.SheetEvent evt) {
                // empty
            }
        });
    }

    /**
     * Brings up a sheet that displays a message using a default icon determined by the <code>
     * messageType</code> parameter.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param messageType
     * 		the type of message to be displayed: <code>JOptionPane.ERROR_MESSAGE</code>,
     * 		<code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param listener
     * 		This listener is notified when the sheet is dismissed.
     */
    public static void showMessageSheet(java.awt.Component parentComponent, java.lang.Object message, int messageType, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showMessageSheet(parentComponent, message, messageType, null, listener);
    }

    /**
     * Brings up a sheet displaying a message, specifying all parameters.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the sheet is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param messageType
     * 		the type of message to be displayed: <code>JOptionPane.ERROR_MESSAGE</code>,
     * 		<code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param icon
     * 		an icon to display in the sheet that helps the user identify the kind of message
     * 		that is being displayed
     * @param listener
     * 		This listener is notified when the sheet is dismissed.
     */
    public static void showMessageSheet(java.awt.Component parentComponent, java.lang.Object message, int messageType, javax.swing.Icon icon, org.jhotdraw.gui.event.SheetListener listener) {
        org.jhotdraw.gui.JSheet.showOptionSheet(parentComponent, message, javax.swing.JOptionPane.DEFAULT_OPTION, messageType, icon, null, null, listener);
    }

    /**
     * Brings up a sheet with a specified icon, where the initial choice is determined by the <code>
     * initialValue</code> parameter and the number of choices is determined by the <code>optionType
     * </code> parameter.
     *
     * <p>If <code>optionType</code> is <code>YES_NO_OPTION</code>, or <code>YES_NO_CANCEL_OPTION
     * </code> and the <code>options</code> parameter is <code>null</code>, then the options are
     * supplied by the look and feel.
     *
     * <p>The <code>messageType</code> parameter is primarily used to supply a default icon from the
     * look and feel.
     *
     * @param parentComponent
     * 		determines the <code>Frame</code> in which the dialog is displayed; if
     * 		<code>null</code>, or if the <code>parentComponent</code> has no <code>Frame</code>, the
     * 		sheet is displayed as a dialog.
     * @param message
     * 		the <code>Object</code> to display
     * @param optionType
     * 		an integer designating the options available on the dialog: <code>
     * 		YES_NO_OPTION</code>, or <code>YES_NO_CANCEL_OPTION</code>
     * @param messageType
     * 		an integer designating the kind of message this is, primarily used to
     * 		determine the icon from the pluggable Look and Feel: <code>JOptionPane.ERROR_MESSAGE</code>
     * 		, <code>JOptionPane.INFORMATION_MESSAGE</code>, <code>JOptionPane.WARNING_MESSAGE</code>,
     * 		<code>JOptionPane.QUESTION_MESSAGE</code>, * or <code>JOptionPane.PLAIN_MESSAGE</code>
     * @param icon
     * 		the icon to display in the dialog
     * @param options
     * 		an array of objects indicating the possible choices the user can make; if the
     * 		objects are components, they are rendered properly; non-<code>String</code> objects are
     * 		rendered using their <code>toString</code> methods; if this parameter is <code>null</code>,
     * 		the options are determined by the Look and Feel
     * @param initialValue
     * 		the object that represents the default selection for the dialog; only
     * 		meaningful if <code>options</code> is used; can be <code>null</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showOptionSheet(java.awt.Component parentComponent, java.lang.Object message, int optionType, int messageType, javax.swing.Icon icon, java.lang.Object[] options, java.lang.Object initialValue, org.jhotdraw.gui.event.SheetListener listener) {
        javax.swing.JOptionPane pane = new javax.swing.JOptionPane(message, messageType, optionType, icon, options, initialValue);
        pane.setInitialValue(initialValue);
        pane.setComponentOrientation((parentComponent == null ? javax.swing.JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());
        int style = org.jhotdraw.gui.JSheet.styleFromMessageType(messageType);
        org.jhotdraw.gui.JSheet sheet = org.jhotdraw.gui.JSheet.createSheet(pane, parentComponent, style);
        pane.selectInitialValue();
        sheet.addSheetListener(listener);
        sheet.show();
        sheet.toFront();
    }

    private static int styleFromMessageType(int messageType) {
        switch (messageType) {
            case javax.swing.JOptionPane.ERROR_MESSAGE :
                return javax.swing.JRootPane.ERROR_DIALOG;
            case javax.swing.JOptionPane.QUESTION_MESSAGE :
                return javax.swing.JRootPane.QUESTION_DIALOG;
            case javax.swing.JOptionPane.WARNING_MESSAGE :
                return javax.swing.JRootPane.WARNING_DIALOG;
            case javax.swing.JOptionPane.INFORMATION_MESSAGE :
                return javax.swing.JRootPane.INFORMATION_DIALOG;
            case javax.swing.JOptionPane.PLAIN_MESSAGE :
            default :
                return javax.swing.JRootPane.PLAIN_DIALOG;
        }
    }

    private static org.jhotdraw.gui.JSheet createSheet(final javax.swing.JOptionPane pane, java.awt.Component parent, int style) {
        // If the parent is on a popup menu retrieve its invoker
        javax.swing.JPopupMenu popup = (parent instanceof javax.swing.JPopupMenu) ? ((javax.swing.JPopupMenu) (parent)) : ((javax.swing.JPopupMenu) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, parent)));
        if (popup != null) {
            parent = popup.getInvoker();
        }
        java.awt.Window window = org.jhotdraw.gui.JSheet.getWindowForComponent(parent);
        final org.jhotdraw.gui.JSheet sheet;
        boolean isUndecorated;
        if (window instanceof java.awt.Frame) {
            isUndecorated = ((java.awt.Frame) (window)).isUndecorated();
            sheet = new org.jhotdraw.gui.JSheet(((java.awt.Frame) (window)));
        } else {
            isUndecorated = ((java.awt.Dialog) (window)).isUndecorated();
            sheet = new org.jhotdraw.gui.JSheet(((java.awt.Dialog) (window)));
        }
        javax.swing.JComponent contentPane = ((javax.swing.JComponent) (sheet.getContentPane()));
        contentPane.setLayout(new java.awt.BorderLayout());
        if (org.jhotdraw.gui.JSheet.isNativeSheetSupported() && (!isUndecorated)) {
            contentPane.setBorder(new javax.swing.border.EmptyBorder(12, 0, 0, 0));
        }
        contentPane.add(pane, java.awt.BorderLayout.CENTER);
        sheet.setResizable(false);
        sheet.addWindowListener(new java.awt.event.WindowAdapter() {
            private boolean gotFocus = false;

            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent we) {
                pane.setValue(null);
            }

            @java.lang.Override
            public void windowClosed(java.awt.event.WindowEvent we) {
                if (pane.getValue() == javax.swing.JOptionPane.UNINITIALIZED_VALUE) {
                    sheet.fireOptionSelected(pane);
                }
            }

            @java.lang.Override
            public void windowGainedFocus(java.awt.event.WindowEvent we) {
                // Once window gets focus, set initial focus
                if (!gotFocus) {
                    // Ugly dirty hack: JOptionPane.selectInitialValue() is protected.
                    // So we call directly into the UI. This may cause mayhem,
                    // because we override the encapsulation.
                    // pane.selectInitialValue();
                    javax.swing.plaf.OptionPaneUI ui = pane.getUI();
                    if (ui != null) {
                        ui.selectInitialValue(pane);
                    }
                    gotFocus = true;
                }
            }
        });
        sheet.addComponentListener(new java.awt.event.ComponentAdapter() {
            @java.lang.Override
            public void componentShown(java.awt.event.ComponentEvent ce) {
                // reset value to ensure closing works properly
                pane.setValue(javax.swing.JOptionPane.UNINITIALIZED_VALUE);
            }
        });
        pane.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            @java.lang.Override
            public void propertyChange(java.beans.PropertyChangeEvent event) {
                // Let the defaultCloseOperation handle the closing
                // if the user closed the window without selecting a button
                // (newValue = null in that case).  Otherwise, close the sheet.
                if ((((sheet.isVisible() && (event.getSource() == pane)) && event.getPropertyName().equals(javax.swing.JOptionPane.VALUE_PROPERTY)) && (event.getNewValue() != null)) && (event.getNewValue() != javax.swing.JOptionPane.UNINITIALIZED_VALUE)) {
                    sheet.setVisible(false);
                    sheet.fireOptionSelected(pane);
                }
            }
        });
        sheet.pack();
        return sheet;
    }

    /**
     * Returns the specified component's toplevel <code>Frame</code> or <code>Dialog</code>.
     *
     * @param parentComponent
     * 		the <code>Component</code> to check for a <code>Frame</code> or <code>
     * 		Dialog</code>
     * @return the <code>Frame</code> or <code>Dialog</code> that contains the component, or the
    default frame if the component is <code>null</code>, or does not have a valid <code>Frame
    </code> or <code>Dialog</code> parent
     */
    static java.awt.Window getWindowForComponent(java.awt.Component parentComponent) {
        if (parentComponent == null) {
            return javax.swing.JOptionPane.getRootFrame();
        }
        if ((parentComponent instanceof java.awt.Frame) || (parentComponent instanceof java.awt.Dialog)) {
            return ((java.awt.Window) (parentComponent));
        }
        return org.jhotdraw.gui.JSheet.getWindowForComponent(parentComponent.getParent());
    }

    /**
     * Displays a "Save File" file chooser sheet. Note that the text that appears in the approve
     * button is determined by the L&amp;F.
     *
     * @param parent
     * 		the parent component of the dialog, can be <code>null</code>.
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showSaveSheet(javax.swing.JFileChooser chooser, java.awt.Component parent, org.jhotdraw.gui.event.SheetListener listener) {
        chooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        org.jhotdraw.gui.JSheet.showSheet(chooser, parent, null, listener);
    }

    /**
     * Displays an "Open File" file chooser sheet. Note that the text that appears in the approve
     * button is determined by the L&amp;F.
     *
     * @param parent
     * 		the parent component of the dialog, can be <code>null</code>.
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showOpenSheet(javax.swing.JFileChooser chooser, java.awt.Component parent, org.jhotdraw.gui.event.SheetListener listener) {
        chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        org.jhotdraw.gui.JSheet.showSheet(chooser, parent, null, listener);
    }

    /**
     * Displays a "Save File" file chooser sheet. Note that the text that appears in the approve
     * button is determined by the L&amp;F.
     *
     * @param parent
     * 		the parent component of the dialog, can be <code>null</code>.
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showSaveSheet(org.jhotdraw.api.gui.URIChooser chooser, java.awt.Component parent, org.jhotdraw.gui.event.SheetListener listener) {
        chooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        org.jhotdraw.gui.JSheet.showSheet(chooser, parent, null, listener);
    }

    /**
     * Displays an "Open File" file chooser sheet. Note that the text that appears in the approve
     * button is determined by the L&amp;F.
     *
     * @param parent
     * 		the parent component of the dialog, can be <code>null</code>.
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showOpenSheet(org.jhotdraw.api.gui.URIChooser chooser, java.awt.Component parent, org.jhotdraw.gui.event.SheetListener listener) {
        chooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
        org.jhotdraw.gui.JSheet.showSheet(chooser, parent, null, listener);
    }

    /**
     * Displays a custom file chooser sheet with a custom approve button.
     *
     * @param parent
     * 		the parent component of the dialog; can be <code>null</code>
     * @param approveButtonText
     * 		the text of the <code>ApproveButton</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showSheet(final javax.swing.JFileChooser chooser, java.awt.Component parent, java.lang.String approveButtonText, org.jhotdraw.gui.event.SheetListener listener) {
        if (approveButtonText != null) {
            chooser.setApproveButtonText(approveButtonText);
            chooser.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        }
        // If the parent is on a popup menu retrieve its invoker
        javax.swing.JPopupMenu popup = (parent instanceof javax.swing.JPopupMenu) ? ((javax.swing.JPopupMenu) (parent)) : ((javax.swing.JPopupMenu) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, parent)));
        if (popup != null) {
            parent = popup.getInvoker();
        }
        // Begin Create Dialog
        java.awt.Frame frame = (parent instanceof java.awt.Frame) ? ((java.awt.Frame) (parent)) : ((java.awt.Frame) (javax.swing.SwingUtilities.getAncestorOfClass(java.awt.Frame.class, parent)));
        java.lang.String title = chooser.getUI().getDialogTitle(chooser);
        chooser.getAccessibleContext().setAccessibleDescription(title);
        final org.jhotdraw.gui.JSheet sheet = new org.jhotdraw.gui.JSheet(frame);
        sheet.addSheetListener(listener);
        java.awt.Container contentPane = sheet.getContentPane();
        contentPane.setLayout(new java.awt.BorderLayout());
        contentPane.add(chooser, java.awt.BorderLayout.CENTER);
        // End Create Dialog
        final java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int option;
                if ("ApproveSelection".equals(evt.getActionCommand())) {
                    option = javax.swing.JFileChooser.APPROVE_OPTION;
                } else {
                    option = javax.swing.JFileChooser.CANCEL_OPTION;
                }
                sheet.hide();
                sheet.fireOptionSelected(chooser, option);
                chooser.removeActionListener(this);
            }
        };
        chooser.addActionListener(actionListener);
        sheet.addWindowListener(new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                sheet.fireOptionSelected(chooser, javax.swing.JFileChooser.CANCEL_OPTION);
                chooser.removeActionListener(actionListener);
            }
        });
        chooser.rescanCurrentDirectory();
        sheet.pack();
        sheet.show();
        sheet.toFront();
    }

    /**
     * Displays a custom file chooser sheet with a custom approve button.
     *
     * @param parent
     * 		the parent component of the dialog; can be <code>null</code>
     * @param approveButtonText
     * 		the text of the <code>ApproveButton</code>
     * @param listener
     * 		The listener for SheetEvents.
     */
    public static void showSheet(final org.jhotdraw.api.gui.URIChooser chooser, java.awt.Component parent, java.lang.String approveButtonText, org.jhotdraw.gui.event.SheetListener listener) {
        if (approveButtonText != null) {
            chooser.setApproveButtonText(approveButtonText);
            chooser.setDialogType(org.jhotdraw.api.gui.URIChooser.CUSTOM_DIALOG);
        }
        // If the parent is on a popup menu retrieve its invoker
        javax.swing.JPopupMenu popup = (parent instanceof javax.swing.JPopupMenu) ? ((javax.swing.JPopupMenu) (parent)) : ((javax.swing.JPopupMenu) (javax.swing.SwingUtilities.getAncestorOfClass(javax.swing.JPopupMenu.class, parent)));
        if (popup != null) {
            parent = popup.getInvoker();
        }
        // Begin Create Dialog
        java.awt.Frame frame = (parent instanceof java.awt.Frame) ? ((java.awt.Frame) (parent)) : ((java.awt.Frame) (javax.swing.SwingUtilities.getAncestorOfClass(java.awt.Frame.class, parent)));
        if (chooser instanceof javax.swing.JFileChooser) {
            java.lang.String title = ((javax.swing.JFileChooser) (chooser)).getUI().getDialogTitle(((javax.swing.JFileChooser) (chooser)));
            ((javax.swing.JFileChooser) (chooser)).getAccessibleContext().setAccessibleDescription(title);
        }
        final org.jhotdraw.gui.JSheet sheet = new org.jhotdraw.gui.JSheet(frame);
        sheet.addSheetListener(listener);
        java.awt.Container contentPane = sheet.getContentPane();
        contentPane.setLayout(new java.awt.BorderLayout());
        contentPane.add(chooser.getComponent(), java.awt.BorderLayout.CENTER);
        // End Create Dialog
        final java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {
            @java.lang.Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int option;
                if ("ApproveSelection".equals(evt.getActionCommand())) {
                    option = javax.swing.JFileChooser.APPROVE_OPTION;
                } else {
                    option = javax.swing.JFileChooser.CANCEL_OPTION;
                }
                sheet.hide();
                sheet.fireOptionSelected(chooser, option);
                chooser.removeActionListener(this);
            }
        };
        chooser.addActionListener(actionListener);
        sheet.addWindowListener(new java.awt.event.WindowAdapter() {
            @java.lang.Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                sheet.fireOptionSelected(chooser, javax.swing.JFileChooser.CANCEL_OPTION);
                chooser.removeActionListener(actionListener);
            }
        });
        chooser.rescanCurrentDirectory();
        sheet.pack();
        sheet.show();
        sheet.toFront();
    }
}