/* @(#)Images.java

Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
You may not use, copy or modify this file, except in compliance with the
accompanying license terms.
 */
package org.jhotdraw.util;
/**
 * Image processing methods.
 */
public class Images {
    /**
     * Prevent instance creation.
     */
    private Images() {
    }

    public static java.awt.Image createImage(java.lang.Class<?> baseClass, java.lang.String resourceName) {
        java.net.URL resource = baseClass.getResource(resourceName);
        if (resource == null) {
            throw new java.lang.InternalError((("Ressource \"" + resourceName) + "\" not found for class ") + baseClass);
        }
        java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(resource);
        return image;
    }

    public static java.awt.Image createImage(java.net.URL resource) {
        if (resource == null) {
            throw new java.lang.IllegalArgumentException("resource must not be null");
        }
        java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(resource);
        return image;
    }

    /**
     * Converts an Image to BufferedImage. If the Image is already a BufferedImage, the same image is
     * returned.
     *
     * @param rImg
     * 		An Image.
     * @return A BufferedImage.
     */
    public static java.awt.image.BufferedImage toBufferedImage(java.awt.image.RenderedImage rImg) {
        java.awt.image.BufferedImage image;
        if (rImg instanceof java.awt.image.BufferedImage) {
            image = ((java.awt.image.BufferedImage) (rImg));
        } else {
            java.awt.image.Raster r = rImg.getData();
            java.awt.image.WritableRaster wr = java.awt.image.WritableRaster.createWritableRaster(r.getSampleModel(), null);
            rImg.copyData(wr);
            image = new java.awt.image.BufferedImage(rImg.getColorModel(), wr, rImg.getColorModel().isAlphaPremultiplied(), null);
        }
        return image;
    }

    public static java.awt.image.BufferedImage toBufferedImage(java.awt.Image image) {
        if (image instanceof java.awt.image.BufferedImage) {
            return ((java.awt.image.BufferedImage) (image));
        }
        // This code ensures that all the pixels in the image are loaded
        image = new javax.swing.ImageIcon(image).getImage();
        // Create a buffered image with a format that's compatible with the screen
        java.awt.image.BufferedImage bimage = null;
        if (java.lang.System.getProperty("java.version").startsWith("1.4.1_")) {
            // Workaround for Java 1.4.1 on Mac OS X.
            // For this JVM, we always create an ARGB image to prevent a class
            // cast exception in
            // sun.awt.image.BufImgSurfaceData.createData(BufImgSurfaceData.java:434)
            // when we attempt to draw the buffered image.
            bimage = new java.awt.image.BufferedImage(image.getWidth(null), image.getHeight(null), java.awt.image.BufferedImage.TYPE_INT_ARGB);
        } else {
            // Determine if the image has transparent pixels; for this method's
            // implementation, see e661 Determining If an Image Has Transparent Pixels
            boolean hasAlpha;
            try {
                hasAlpha = org.jhotdraw.util.Images.hasAlpha(image);
            } catch (java.lang.IllegalAccessError e) {
                // If we can't determine this, we assume that we have an alpha,
                // in order not to loose data.
                hasAlpha = true;
            }
            java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                // Determine the type of transparency of the new buffered image
                int transparency = java.awt.Transparency.OPAQUE;
                if (hasAlpha) {
                    transparency = java.awt.Transparency.TRANSLUCENT;
                }
                // Create the buffered image
                java.awt.GraphicsDevice gs = ge.getDefaultScreenDevice();
                java.awt.GraphicsConfiguration gc = gs.getDefaultConfiguration();
                bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
            } catch (java.lang.Exception e) {
                // } catch (HeadlessException e) {
                // The system does not have a screen
            }
            if (bimage == null) {
                // Create a buffered image using the default color model
                int type = java.awt.image.BufferedImage.TYPE_INT_RGB;
                if (hasAlpha) {
                    type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
                }
                bimage = new java.awt.image.BufferedImage(image.getWidth(null), image.getHeight(null), type);
            }
        }
        // Copy image to buffered image
        java.awt.Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    /**
     * This method returns true if the specified image has transparent pixels
     *
     * <p>Code taken from the Java Developers Almanac 1.4
     * http://javaalmanac.com/egs/java.awt.image/HasAlpha.html
     */
    public static boolean hasAlpha(java.awt.Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof java.awt.image.BufferedImage) {
            java.awt.image.BufferedImage bimage = ((java.awt.image.BufferedImage) (image));
            return bimage.getColorModel().hasAlpha();
        }
        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (java.lang.InterruptedException e) {
            // empty allowed
        }
        // Get the image's color model
        java.awt.image.ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * Splits an image into count subimages.
     */
    public static java.awt.image.BufferedImage[] split(java.awt.Image image, int count, boolean isHorizontal) {
        java.awt.image.BufferedImage src = org.jhotdraw.util.Images.toBufferedImage(image);
        if (count == 1) {
            return new java.awt.image.BufferedImage[]{ src };
        }
        java.awt.image.BufferedImage[] parts = new java.awt.image.BufferedImage[count];
        for (int i = 0; i < count; i++) {
            if (isHorizontal) {
                parts[i] = src.getSubimage((src.getWidth() / count) * i, 0, src.getWidth() / count, src.getHeight());
            } else {
                parts[i] = src.getSubimage(0, (src.getHeight() / count) * i, src.getWidth(), src.getHeight() / count);
            }
        }
        return parts;
    }

    /**
     * Creates a scaled instanceof the image.
     *
     * <p>If either width or height is a negative number then a value is s ubstituted to maintain the
     * aspect ratio of the original image dimensions. If both width and height are negative, then the
     * original image dimensions are used.
     *
     * <p>On Mac OS X 10.6, this method has a much better performance than
     * BufferedImage.getScaledInstance.
     *
     * @param image
     * 		the image.
     * @param width
     * 		the width to which to scale the image.
     * @param height
     * 		the height to which to scale the image.
     */
    public static java.awt.image.BufferedImage getScaledInstance(java.awt.Image image, int width, int height) {
        int w;
        int h;
        if ((width < 0) && (height < 0)) {
            w = image.getWidth(null);
            h = image.getHeight(null);
        } else if (width < 0) {
            w = (image.getWidth(null) * height) / image.getHeight(null);
            h = height;
        } else if (height < 0) {
            w = width;
            h = (image.getHeight(null) * width) / image.getWidth(null);
        } else {
            w = width;
            h = height;
        }
        java.awt.image.BufferedImage scaled = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(image, 0, 0, w, h, null);
        g.dispose();
        return scaled;
    }
}