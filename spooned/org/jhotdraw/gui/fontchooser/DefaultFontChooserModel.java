/**
 *
 * @(#)DefaultFontModel.java <p>Copyright (c) 2008 The authors and contributors of JHotDraw. You may not use, copy or modify
this file, except in compliance with the accompanying license terms.
 */
package org.jhotdraw.gui.fontchooser;
/**
 * DefaultFontChooserModel with a predefined set of font collections.
 *
 * <p>Loading the fonts may take a lot of time. Therefore it is recommended to create a Future
 * during the startup of an application, and set the fonts in the font chooser model when they are
 * needed.
 *
 * <p>Example:
 *
 * <pre>
 *   private static FutureTask&lt;Font[]&gt; future = new FutureTask&lt;Font[]&gt;(new Callable&lt;Font[]&gt;() {
 *
 *      public Font[] call() throws Exception {
 *          return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
 *
 *       }
 *   });
 * </pre>
 */
public class DefaultFontChooserModel extends org.jhotdraw.gui.fontchooser.AbstractFontChooserModel {
    /**
     * Root node.
     */
    protected javax.swing.tree.DefaultMutableTreeNode root;

    public DefaultFontChooserModel() {
        root = new javax.swing.tree.DefaultMutableTreeNode();
    }

    public DefaultFontChooserModel(java.awt.Font[] fonts) {
        root = new javax.swing.tree.DefaultMutableTreeNode();
        setFonts(fonts);
    }

    /**
     * Sets the fonts of the DefaultFontChooserModel.
     *
     * <p>Fires treeStructureChanged event on the root node.
     *
     * @param fonts
     */
    @java.lang.SuppressWarnings("unchecked")
    public void setFonts(java.awt.Font[] fonts) {
        org.jhotdraw.util.ResourceBundleUtil labels = org.jhotdraw.util.ResourceBundleUtil.getBundle("org.jhotdraw.gui.Labels");
        // collect families and sort them alphabetically
        java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> families = new java.util.ArrayList<>();
        java.util.HashMap<java.lang.String, org.jhotdraw.gui.fontchooser.FontFamilyNode> familyMap = new java.util.HashMap<>();
        for (java.awt.Font f : fonts) {
            java.lang.String familyName = f.getFamily();
            org.jhotdraw.gui.fontchooser.FontFamilyNode family;
            if (familyMap.containsKey(familyName)) {
                family = familyMap.get(familyName);
            } else {
                family = new org.jhotdraw.gui.fontchooser.FontFamilyNode(familyName);
                familyMap.put(familyName, family);
            }
            family.add(new org.jhotdraw.gui.fontchooser.FontFaceNode(f));
        }
        families.addAll(familyMap.values());
        java.util.Collections.sort(families);
        // group families into collections
        root.removeAllChildren();
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.allFonts"), ((java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode>) (families.clone()))));
        // Web-save fonts
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.web"), collectFamiliesNamed(families, "Arial", "Arial Black", "Comic Sans MS", "Georgia", "Impact", "Times New Roman", "Trebuchet MS", "Verdana", "Webdings")));
        /* // PDF Fonts
        root.add(
        new FontCollectionNode(labels.getString("FontCollection.pdf"), collectFamiliesNamed(families,
        "Andale Mono",
        "Courier",
        "Helvetica",
        "Symbol",
        "Times",
        "Zapf Dingbats")));
         */
        // Java System fonts
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.system"), collectFamiliesNamed(families, "Dialog", "DialogInput", "Monospaced", "SansSerif", "Serif")));
        // Serif fonts
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.serif"), // Fonts on Mac OS X 10.5:
        // Fonts on Mac OS X 10.6:
        // Fonts on Windows XP:
        // Fonts on Windows Vista
        collectFamiliesNamed(families, "Adobe Caslon Pro", "Adobe Garamond Pro", "American Typewriter", "Arno Pro", "Baskerville", "Baskerville Old Face", "Bell MT", "Big Caslon", "Bodoni SvtyTwo ITC TT", "Bodoni SvtyTwo OS ITC TT", "Bodoni SvtyTwo SC ITC TT", "Book Antiqua", "Bookman Old Style", "Calisto MT", "Chaparral Pro", "Century", "Century Schoolbook", "Cochin", "Footlight MT Light", "Garamond", "Garamond Premier Pro", "Georgia", "Goudy Old Style", "Hoefler Text", "Lucida Bright", "Lucida Fax", "Minion Pro", "Palatino", "Times", "Times New Roman", "Didot", "Palatino Linotype", "Bitstream Vera Serif Bold", "Bodoni MT", "Bodoni MT Black", "Bodoni MT Condensed", "Californian FB", "Cambria", "Cambria Math", "Centaur", "Constantia", "High Tower Text", "Perpetua", "Poor Richard", "Rockwell Condensed", "Slimbach-Black", "Slimbach-BlackItalic", "Slimbach-Bold", "Slimbach-BoldItalic", "Slimbach-Book", "Slimbach-BookItalic", "Slimbach-Medium", "Slimbach-MediumItalic", "Sylfaen", "Andalus", "Angsana New", "AngsanaUPC", "Arabic Typesetting", "Cambria", "Cambria Math", "Constantia", "DaunPenh", "David", "DilleniaUPC", "EucrosiaUPC", "Frank Ruehl", "IrisUPC", "Iskoola Pota", "JasmineUPC", "KodchiangUPC", "Narkisim")));
        // Sans Serif
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.sansSerif"), // Fonts on Mac OS X 10.5:
        // Fonts on Mac OS X 10.6:
        // Fonts on Windows XP:
        // Fonts on Windows Vista:
        collectFamiliesNamed(families, "Abadi MT Condensed Extra Bold", "Abadi MT Condensed Light", "AppleGothic", "Arial", "Arial Black", "Arial Narrow", "Arial Rounded MT Bold", "Arial Unicode MS", "Bell Gothic Std", "Blair MdITC TT", "Century Gothic", "Frutiger", "Futura", "Geneva", "Gill Sans", "Gulim", "Helvetica", "Helvetica Neue", "Lucida Grande", "Lucida Sans", "Microsoft Sans Serif", "Myriad Pro", "News Gothic", "Tahoma", "Trebuchet MS", "Verdana", "Charcoal", "Euphemia UCAS", "Franklin Gothic Medium", "Lucida Sans Unicode", "Agency FB", "Berlin Sans FB", "Berlin Sans FB Demi Bold", "Bitstream Vera Sans Bold", "Calibri", "Candara", "Corbel", "Estrangelo Edessa", "Eras Bold ITC", "Eras Demi ITC", "Eras Light ITC", "Eras Medium ITC", "Franklin Gothic Book", "Franklin Gothic Demi", "Franklin Gothic Demi Cond", "Franklin Gothic Heavy", "Franklin Gothic Medium Cond", "Gill Sans MT", "Gill Sans MT Condensed", "Gill Sans MT Ext Condensed Bold", "Maiandra GD", "MS Reference Sans...", "Tw Cen MT", "Tw Cen MT Condensed", "Tw Cen MT Condensed Extra Bold", "Aharoni", "Browallia New", "BrowalliaUPC", "Calibri", "Candara", "Corbel", "Cordia New", "CordiaUPC", "DokChampa", "Dotum", "Estrangelo Edessa", "Euphemia", "Freesia UPC", "Gautami", "Gisha", "Kalinga", "Kartika", "Levenim MT", "LilyUPC", "Malgun Gothic", "Meiryo", "Miriam", "Segoe UI")));
        // Scripts
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.script"), // Fonts on Mac OS X 10.5:
        // Fonts on Mac OS X 10.6:
        // Fonts on Windows XP:
        // Fonts on Windows Vista
        collectFamiliesNamed(families, "Apple Chancery", "Bickham Script Pro", "Blackmoor LET", "Bradley Hand ITC TT", "Brush Script MT", "Brush Script Std", "Chalkboard", "Charlemagne Std", "Comic Sans MS", "Curlz MT", "Edwardian Script ITC", "Footlight MT Light", "Giddyup Std", "Handwriting - Dakota", "Harrington", "Herculanum", "Kokonor", "Lithos Pro", "Lucida Blackletter", "Lucida Calligraphy", "Lucida Handwriting", "Marker Felt", "Matura MT Script Capitals", "Mistral", "Monotype Corsiva", "Party LET", "Papyrus", "Santa Fe LET", "Savoye LET", "SchoolHouse Cursive B", "SchoolHouse Printed A", "Skia", "Snell Roundhand", "Tekton Pro", "Trajan Pro", "Zapfino", "Casual", "Chalkduster", "Blackadder ITC", "Bradley Hand ITC", "Chiller", "Freestyle Script", "French Script MT", "Gigi", "Harlow Solid Italic", "Informal Roman", "Juice ITC", "Kristen ITC", "Kunstler Script", "Magneto Bold", "Maiandra GD", "Old English Text", "Palace Script MT", "Parchment", "Pristina", "Rage Italic", "Ravie", "Script MT Bold", "Tempus Sans ITC", "Viner Hand ITC", "Vivaldi Italic", "Vladimir Script", "Segoe Print", "Segoe Script")));
        // Monospaced
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.monospaced"), // Fonts on Mac OS X 10.5:
        // Fonts on Mac OS X 10.6:
        // Fonts on Windows XP:
        // Fonts on Windows Vista
        collectFamiliesNamed(families, "Andale Mono", "Courier", "Courier New", "Letter Gothic Std", "Lucida Sans Typewriter", "Monaco", "OCR A Std", "Orator Std", "Prestige Elite Std", "Menlo", "Lucida Console", "Bitstream Vera S...", "Consolas", "OCR A Extended", "OCR B", "Consolas", "DotumChe", "Miriam Fixed", "Rod")));
        // Decorative
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.decorative"), // Fonts on Mac OS X 10.5:
        // Fonts on Mac OS X 10.5:
        // Fonts on Windows XP:
        // Fonts on Windows Vista:
        collectFamiliesNamed(families, "Academy Engraved LET", "Arial Black", "Bank Gothic", "Bauhaus 93", "Bernard MT Condensed", "Birch Std", "Blackoak Std", "BlairMdITC TT", "Bordeaux Roman Bold LET", "Braggadocio", "Britannic Bold", "Capitals", "Colonna MT", "Cooper Black", "Cooper Std", "Copperplate", "Copperplate Gothic Bold", "Copperplate Gothic Light", "Cracked", "Desdemona", "Didot", "Eccentric Std", "Engravers MT", "Eurostile", "Gill Sans Ultra Bold", "Gloucester MT Extra Condensed", "Haettenschweiler", "Hobo Std", "Impact", "Imprint MT Shadow", "Jazz LET", "Kino MT", "Matura MT Script Capitals", "Mesquite Std", "Modern No. 20", "Mona Lisa Solid ITC TT", "MS Gothic", "Nueva Std", "Onyx", "Optima", "Perpetua Titling MT", "Playbill", "Poplar Std", "PortagoITC TT", "Princetown LET", "Rockwell", "Rockwell Extra Bold", "Rosewood Std", "Santa Fe LET", "Stencil", "Stencil Std", "Stone Sans ITC TT", "Stone Sans OS ITC TT", "Stone Sans Sem ITC TT", "Stone Sans Sem OS ITCTT", "Stone Sans Sem OS ITC TT", "Synchro LET", "Wide Latin", "HeadLineA", "Algerian", "Bodoni MT Black", "Bodoni MT Poster Compressed", "Broadway", "Castellar", "Elephant", "Felix Titling", "Franklin Gothic Heavy", "Gill Sans MT Ext Condensed Bold", "Gill Sans Ultra Bold Condensed", "Goudy Stout", "Jokerman", "Juice ITC", "Magneto", "Magneto Bold", "Niagara Engraved", "Niagara Solid", "Poor Richard", "Ravie", "Rockwell Condensed", "Showcard Gothic", "Slimbach-Black", "Slimbach-BlackItalic", "Snap ITC")));
        root.add(new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.symbols"), // Fonts on Mac OS X 10.5:
        // Fonts on Windows XP:
        // Fonts on Windows Vista:
        collectFamiliesNamed(families, "Apple Symbols", "Blackoack Std", "Bodoni Ornaments ITC TT", "EuropeanPi", "Monotype Sorts", "MT Extra", "Symbol", "Type Embellishments One LET", "Webdings", "Wingdings", "Wingdings 2", "Wingdings 3", "Zapf Dingbats", "Bookshelf Symbol")));
        // Collect font families, which are not in one of the other collections
        // (except the collection AllFonts).
        org.jhotdraw.gui.fontchooser.FontCollectionNode others = new org.jhotdraw.gui.fontchooser.FontCollectionNode(labels.getString("FontCollection.other"));
        java.util.HashSet<org.jhotdraw.gui.fontchooser.FontFamilyNode> otherFamilySet = new java.util.HashSet<>();
        otherFamilySet.addAll(families);
        for (int i = 1, n = root.getChildCount(); i < n; i++) {
            org.jhotdraw.gui.fontchooser.FontCollectionNode fcn = ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (root.getChildAt(i)));
            for (org.jhotdraw.gui.fontchooser.FontFamilyNode ffn : fcn.families()) {
                otherFamilySet.remove(ffn);
            }
        }
        java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> otherFamilies = new java.util.ArrayList<>();
        for (org.jhotdraw.gui.fontchooser.FontFamilyNode ffn : otherFamilySet) {
            otherFamilies.add(ffn.clone());
        }
        java.util.Collections.sort(otherFamilies);
        others.addAll(otherFamilies);
        root.add(others);
        fireTreeStructureChanged(this, new javax.swing.tree.TreePath(root));
    }

    protected java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> collectFamiliesNamed(java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> families, java.lang.String... names) {
        java.util.ArrayList<org.jhotdraw.gui.fontchooser.FontFamilyNode> coll = new java.util.ArrayList<>();
        java.util.HashSet<java.lang.String> nameMap = new java.util.HashSet<>();
        nameMap.addAll(java.util.Arrays.asList(names));
        for (org.jhotdraw.gui.fontchooser.FontFamilyNode family : families) {
            java.lang.String fName = family.getName();
            if (nameMap.contains(family.getName())) {
                coll.add(family.clone());
            }
        }
        return coll;
    }

    @java.lang.Override
    public boolean isEditable(javax.swing.tree.MutableTreeNode node) {
        boolean result = true;
        if (node instanceof org.jhotdraw.gui.fontchooser.FontFaceNode) {
            result &= ((org.jhotdraw.gui.fontchooser.FontFaceNode) (node)).isEditable();
            node = ((javax.swing.tree.MutableTreeNode) (node.getParent()));
        }
        if (result && (node instanceof org.jhotdraw.gui.fontchooser.FontFamilyNode)) {
            result &= ((org.jhotdraw.gui.fontchooser.FontFamilyNode) (node)).isEditable();
            node = ((javax.swing.tree.MutableTreeNode) (node.getParent()));
        }
        if (result && (node instanceof org.jhotdraw.gui.fontchooser.FontCollectionNode)) {
            result &= ((org.jhotdraw.gui.fontchooser.FontCollectionNode) (node)).isEditable();
        }
        return result;
    }

    @java.lang.Override
    public java.lang.Object getRoot() {
        return root;
    }

    @java.lang.Override
    public java.lang.Object getChild(java.lang.Object parent, int index) {
        return ((javax.swing.tree.TreeNode) (parent)).getChildAt(index);
    }

    @java.lang.Override
    public int getChildCount(java.lang.Object parent) {
        return ((javax.swing.tree.TreeNode) (parent)).getChildCount();
    }

    @java.lang.Override
    public boolean isLeaf(java.lang.Object node) {
        return ((javax.swing.tree.TreeNode) (node)).isLeaf();
    }

    @java.lang.Override
    public void valueForPathChanged(javax.swing.tree.TreePath path, java.lang.Object newValue) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    @java.lang.Override
    public int getIndexOfChild(java.lang.Object parent, java.lang.Object child) {
        return ((javax.swing.tree.TreeNode) (parent)).getIndex(((javax.swing.tree.TreeNode) (child)));
    }

    public static class UIResource extends org.jhotdraw.gui.fontchooser.DefaultFontChooserModel implements javax.swing.plaf.UIResource {}
}