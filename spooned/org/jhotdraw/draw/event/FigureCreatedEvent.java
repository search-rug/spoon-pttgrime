/* Gingko.Systeme
(c) 2013
 */
package org.jhotdraw.draw.event;
/**
 * Event wird gefeuert, wenn ein Element erstellt über JHotDraw wurde.
 *
 * @author fg
 */
public class FigureCreatedEvent extends org.jhotdraw.draw.event.ToolEvent {
    final org.jhotdraw.draw.figure.Figure figure;

    public FigureCreatedEvent(org.jhotdraw.draw.tool.Tool src, org.jhotdraw.draw.DrawingView view, org.jhotdraw.draw.figure.Figure figure) {
        super(src, view, null);
        this.figure = figure;
    }

    public org.jhotdraw.draw.figure.Figure getFigure() {
        return figure;
    }
}