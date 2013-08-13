/*
 * Copyright 2008 Deputaci�n Provincial de A Coru�a
 * Copyright 2009 Deputaci�n Provincial de Pontevedra
 * Copyright 2010-2013 CartoLab, Universidad de A Coru�a
 * 
 *
 * This file is part of gvSIG-Fonsagua, developed by the Cartography
 * Engineering Laboratory of the University of A Coru�a (CartoLab).
 * http://www.cartolab.es
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 *
 */

package es.udc.cartolab.gvsig.epanet.cad;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.core.FGeometryCollection;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.v02.FConverter;
import com.iver.cit.gvsig.fmap.edition.UtilFunctions;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.InsertionCADTool;

import es.udc.cartolab.gvsig.epanet.cad.PipeCADToolContext.PipeCADToolState;

/**
 * Based on EIELPolylineCADTool from opencadtools project
 * 
 * @author Vicente Caballero Navarro
 * @author Laboratorio de Bases de Datos. Universidad de A Coru�a
 * @author Cartolab. Universidad de A Coru�a
 * @author Francisco Puga <fpuga@cartolab.es>
 */
public class PipeCADTool extends InsertionCADTool {
    private PipeCADToolContext _fsm;
    protected Point2D firstPoint;
    protected Point2D antPoint;
    protected Point2D antantPoint;
    protected Point2D antCenter;
    protected Point2D antInter;
    private ArrayList list = new ArrayList();

    private ArrayList points = new ArrayList();

    public PipeCADTool() {

    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    @Override
    public void init() {
	// clear();
	if (_fsm == null) {
	    _fsm = new PipeCADToolContext(this);
	}
    }

    @Override
    public void clear() {
	super.init();
	this.setMultiTransition(true);
	points.clear();
	list.clear();
	firstPoint = null;
	antPoint = null;
	// con esto limpio el ultimo punto pulsado para reinicializar el
	// seguimiento de
	// los snappers
	getCadToolAdapter().setPreviousPoint((double[]) null);
	_fsm = new PipeCADToolContext(this);
    }

    public IGeometry getGeometry() {
	IGeometry[] geoms = (IGeometry[]) list.toArray(new IGeometry[0]);
	FGeometryCollection fgc = new FGeometryCollection(geoms);
	// No queremos guardar FGeometryCollections:
	GeneralPathX gp = new GeneralPathX();
	gp.append(fgc.getPathIterator(null, FConverter.FLATNESS), true);
	IGeometry newGeom = ShapeFactory.createPolyline2D(gp);
	return newGeom;
    }

    public void endGeometry() {
	IGeometry[] geoms = (IGeometry[]) list.toArray(new IGeometry[0]);
	FGeometryCollection fgc = new FGeometryCollection(geoms);

	// No queremos guardar FGeometryCollections:
	GeneralPathX gp = new GeneralPathX();
	gp.append(fgc.getPathIterator(null, FConverter.FLATNESS), true);
	IGeometry newGeom = null;
	int type = getCadToolAdapter().getActiveLayerType();
	if (type == FShape.POLYGON) {
	    newGeom = ShapeFactory.createPolygon2D(gp);
	} else {
	    newGeom = ShapeFactory.createPolyline2D(gp);
	}

	addGeometry(newGeom);
	_fsm = new PipeCADToolContext(this);
	list.clear();
	antantPoint = antCenter = antInter = antPoint = firstPoint = null;
    }

    @Override
    public void transition(double x, double y, InputEvent event) {
	_fsm.addPoint(x, y, event);
    }

    @Override
    public void transition(double d) {
	_fsm.addValue(d);
    }

    @Override
    public void transition(String s) throws CommandException {
	if (!super.changeCommand(s)) {
	    if (s.equals(PluginServices.getText(this, "removePoint"))) {
		_fsm.removePoint(null, points.size());
	    } else {
		_fsm.addOption(s);
	    }
	}
    }

    /**
     * Equivale al transition del prototipo pero sin pasarle como par�metro el
     * editableFeatureSource que ya estar� creado.
     * 
     */
    @Override
    public void addPoint(double x, double y, InputEvent event) {
	PipeCADToolState actualState = (PipeCADToolState) _fsm
		.getPreviousState();
	String status = actualState.getName();
	Point2D point = new Point2D.Double(x, y);

	// if (status.equals("Polyline.FirstPoint")) {
	// antPoint = new Point2D.Double(x, y);

	// if (firstPoint == null) {
	// firstPoint = (Point2D) antPoint.clone();
	// }
	// } else
	if (status.equals("Polyline.NextPointOrArc")
		|| status.equals("Polyline.FirstPoint")) {

	    if (firstPoint == null) {
		firstPoint = new Point2D.Double(x, y);
	    }
	    point = new Point2D.Double(x, y);

	    if (antPoint != null) {
		GeneralPathX elShape = new GeneralPathX(
			GeneralPathX.WIND_EVEN_ODD, 2);
		elShape.moveTo(antPoint.getX(), antPoint.getY());
		elShape.lineTo(point.getX(), point.getY());
		list.add(ShapeFactory.createPolyline2D(elShape));

	    }
	    if (antPoint == null) {
		antPoint = (Point2D) firstPoint.clone();
	    }

	    if (antPoint != null) {
		antantPoint = antPoint;
	    }
	    antPoint = point;

	} else if (status.equals("Polyline.NextPointOrLine")) {
	    point = new Point2D.Double(x, y);
	    Point2D lastp = antPoint; // (Point2D)points.get(i-1);

	    if (antantPoint == null) {
		antantPoint = new Point2D.Double(lastp.getX()
			+ (point.getX() / 2), lastp.getY() + (point.getY() / 2));
	    }

	    if (((point.getY() == lastp.getY()) && (point.getX() < lastp.getX()))
		    || ((point.getX() == lastp.getX()) && (point.getY() < lastp
			    .getY()))) {
	    } else {
		if (point.getX() == lastp.getX()) {
		    point = new Point2D.Double(point.getX() + 0.00000001,
			    point.getY());
		} else if (point.getY() == lastp.getY()) {
		    point = new Point2D.Double(point.getX(),
			    point.getY() + 0.00000001);
		}

		if (point.getX() == antantPoint.getX()) {
		    point = new Point2D.Double(point.getX() + 0.00000001,
			    point.getY());
		} else if (point.getY() == antantPoint.getY()) {
		    point = new Point2D.Double(point.getX(),
			    point.getY() + 0.00000001);
		}

		if (!(list.size() > 0)
			|| (((IGeometry) list.get(list.size() - 1))
				.getGeometryType() == FShape.LINE)) {
		    Point2D[] ps1 = UtilFunctions.getPerpendicular(antantPoint,
			    lastp, lastp);
		    Point2D mediop = new Point2D.Double(
			    (point.getX() + lastp.getX()) / 2,
			    (point.getY() + lastp.getY()) / 2);
		    Point2D[] ps2 = UtilFunctions.getPerpendicular(lastp,
			    point, mediop);
		    Point2D interp = UtilFunctions.getIntersection(ps1[0],
			    ps1[1], ps2[0], ps2[1]);
		    antInter = interp;

		    double radio = interp.distance(lastp);

		    if (UtilFunctions.isLowAngle(antantPoint, lastp, interp,
			    point)) {
			radio = -radio;
		    }

		    Point2D centerp = UtilFunctions.getPoint(interp, mediop,
			    radio);
		    antCenter = centerp;

		    IGeometry ig = ShapeFactory
			    .createArc(lastp, centerp, point);

		    if (ig != null) {
			list.add(ig);
		    }
		} else {
		    Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp,
			    antInter, lastp);
		    double a1 = UtilFunctions.getAngle(ps1[0], ps1[1]);
		    double a2 = UtilFunctions.getAngle(ps1[1], ps1[0]);
		    double angle = UtilFunctions.getAngle(antCenter, lastp);
		    Point2D ini1 = null;
		    Point2D ini2 = null;

		    if (UtilFunctions.absoluteAngleDistance(angle, a1) > UtilFunctions
			    .absoluteAngleDistance(angle, a2)) {
			ini1 = ps1[0];
			ini2 = ps1[1];
		    } else {
			ini1 = ps1[1];
			ini2 = ps1[0];
		    }

		    Point2D unit = UtilFunctions.getUnitVector(ini1, ini2);
		    Point2D correct = new Point2D.Double(lastp.getX()
			    + unit.getX(), lastp.getY() + unit.getY());
		    Point2D[] ps = UtilFunctions.getPerpendicular(lastp,
			    correct, lastp);
		    Point2D mediop = new Point2D.Double(
			    (point.getX() + lastp.getX()) / 2,
			    (point.getY() + lastp.getY()) / 2);
		    Point2D[] ps2 = UtilFunctions.getPerpendicular(lastp,
			    point, mediop);
		    Point2D interp = UtilFunctions.getIntersection(ps[0],
			    ps[1], ps2[0], ps2[1]);
		    antInter = interp;

		    double radio = interp.distance(lastp);

		    if (UtilFunctions.isLowAngle(correct, lastp, interp, point)) {
			radio = -radio;
		    }

		    Point2D centerp = UtilFunctions.getPoint(interp, mediop,
			    radio);
		    antCenter = centerp;
		    list.add(ShapeFactory.createArc(lastp, centerp, point));
		}

		antantPoint = antPoint;
		antPoint = point;
	    }

	}
	// [LBD]
	points.add(point);
    }

    public void removePoint(InputEvent event) {
	PipeCADToolState actualState = (PipeCADToolState) _fsm
		.getPreviousState();
	String status = actualState.getName();

	// System.out.println("Acci�n removePoint, estado = " + status);
	if (status.equals("Polyline.FirstPoint")) {
	    cancel();
	    // prueba para actualizar el ultimo punto pulsado
	    getCadToolAdapter().setPreviousPoint((double[]) null);
	} else if (status.equals("Polyline.NextPointOrLine")
		|| status.equals("Polyline.NextPointOrArc")) {
	    System.out.println("Numero de coordenadas antes de borrar: "
		    + points.size());
	    if (points.size() == 1) {
		cancel();
		// prueba para actualizar el ultimo punto pulsado
		getCadToolAdapter().setPreviousPoint((double[]) null);
	    } else if (points.size() == 2) {
		// prueba para actualizar el ultimo punto pulsado
		getCadToolAdapter().setPreviousPoint(
			(Point2D) points.get(points.size() - 2));

		list.remove(list.size() - 1);
		points.remove(points.size() - 1);
		antPoint = (Point2D) points.get(points.size() - 1);
	    } else {
		// prueba para actualizar el ultimo punto pulsado
		getCadToolAdapter().setPreviousPoint(
			(Point2D) points.get(points.size() - 2));

		list.remove(list.size() - 1);
		points.remove(points.size() - 1);
		antPoint = (Point2D) points.get(points.size() - 1);
	    }
	}

    }

    /**
     * M�todo para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     * 
     */
    @Override
    public void drawOperation(Graphics g, double x, double y) {
	PipeCADToolState actualState = _fsm.getState();
	String status = actualState.getName();

	if (status.equals("Polyline.NextPointOrArc")
		|| status.equals("Polyline.FirstPoint")) {
	    for (int i = 0; i < list.size(); i++) {
		((IGeometry) list.get(i)).cloneGeometry().draw((Graphics2D) g,
			getCadToolAdapter().getMapControl().getViewPort(),
			DefaultCADTool.geometrySelectSymbol);
	    }
	    if (antPoint != null) {
		drawLine((Graphics2D) g, antPoint, new Point2D.Double(x, y),
			DefaultCADTool.geometrySelectSymbol);
	    }

	} else if ((status.equals("Polyline.NextPointOrLine"))) {
	    for (int i = 0; i < list.size(); i++) {
		((IGeometry) list.get(i)).cloneGeometry().draw((Graphics2D) g,
			getCadToolAdapter().getMapControl().getViewPort(),
			DefaultCADTool.geometrySelectSymbol);
	    }

	    Point2D point = new Point2D.Double(x, y);
	    Point2D lastp = antPoint;

	    if (!(list.size() > 0)
		    || (((IGeometry) list.get(list.size() - 1))
			    .getGeometryType() == FShape.LINE)) {
		if (antantPoint == null) {
		    drawArc(point, lastp, new Point2D.Double(lastp.getX()
			    + (point.getX() / 2), lastp.getY()
			    + (point.getY() / 2)), g);
		} else {
		    drawArc(point, lastp, antantPoint, g);
		}
	    } else {
		if (antInter != null) {
		    Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp,
			    antInter, lastp);
		    double a1 = UtilFunctions.getAngle(ps1[0], ps1[1]);
		    double a2 = UtilFunctions.getAngle(ps1[1], ps1[0]);
		    double angle = UtilFunctions.getAngle(antCenter, lastp);
		    Point2D ini1 = null;
		    Point2D ini2 = null;

		    if (UtilFunctions.absoluteAngleDistance(angle, a1) > UtilFunctions
			    .absoluteAngleDistance(angle, a2)) {
			ini1 = ps1[0];
			ini2 = ps1[1];
		    } else {
			ini1 = ps1[1];
			ini2 = ps1[0];
		    }

		    Point2D unit = UtilFunctions.getUnitVector(ini1, ini2);
		    Point2D correct = new Point2D.Double(lastp.getX()
			    + unit.getX(), lastp.getY() + unit.getY());
		    drawArc(point, lastp, correct, g);
		}
	    }
	}
    }

    /**
     * M�todo para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     */
    @Override
    public void drawOperation(Graphics g, ArrayList listaPuntos) {
	// LineaCADToolState actualState =
	// ((LineaCADToolContext)_fsm).getState();
	// String status = actualState.getName();

	// if (status.equals("Linea.FirstPoint") ||
	// status.equals("Linea.SecondPoint") ||
	// status.equals("Linea.NextPoint")) {
	for (int i = 0; i < list.size(); i++) {
	    ((IGeometry) list.get(i)).cloneGeometry().draw((Graphics2D) g,
		    getCadToolAdapter().getMapControl().getViewPort(),
		    CADTool.drawingSymbol);
	}
	// ahora debemos pintar las lineas que vienen en la lista
	if (listaPuntos != null && listaPuntos.size() > 1) {
	    if (antPoint != null) {
		Point2D puntoInicial = antPoint;
		for (int i = 0; i < listaPuntos.size(); i++) {
		    Point2D puntoFinal = (Point2D) listaPuntos.get(i);
		    drawLine((Graphics2D) g, puntoInicial, puntoFinal);
		    puntoInicial = puntoFinal;
		    // pintamos los puntos para que se note el snapping
		    if (i < listaPuntos.size() - 1) {
			Point2D actual = null;
			actual = CADExtension.getEditionManager()
				.getMapControl().getViewPort()
				.fromMapPoint(puntoInicial);
			int sizePixels = 12;
			int half = sizePixels / 2;
			g.drawRect((int) (actual.getX() - half),
				(int) (actual.getY() - half), sizePixels,
				sizePixels);
		    }
		}
	    } else {
		Point2D puntoInicial = (Point2D) listaPuntos.get(0);
		for (int i = 1; i < listaPuntos.size(); i++) {
		    Point2D puntoFinal = (Point2D) listaPuntos.get(i);
		    drawLine((Graphics2D) g, puntoInicial, puntoFinal);
		    puntoInicial = puntoFinal;

		    // pintamos los puntos para que se note el snapping
		    if (i < listaPuntos.size() - 1) {
			Point2D actual = null;
			actual = CADExtension.getEditionManager()
				.getMapControl().getViewPort()
				.fromMapPoint(puntoInicial);
			int sizePixels = 12;
			int half = sizePixels / 2;
			g.drawRect((int) (actual.getX() - half),
				(int) (actual.getY() - half), sizePixels,
				sizePixels);
		    }

		}
	    }
	}
    }

    /**
     * Dibuja el arco sobre el graphics.
     * 
     * @param point
     *            Puntero del rat�n.
     * @param lastp
     *            �ltimo punto de la polilinea.
     * @param antp
     *            Punto antepenultimo.
     * @param g
     *            Graphics sobre el que se dibuja.
     */
    private void drawArc(Point2D point, Point2D lastp, Point2D antp, Graphics g) {
	if (((point.getY() == lastp.getY()) && (point.getX() < lastp.getX()))
		|| ((point.getX() == lastp.getX()) && (point.getY() < lastp
			.getY()))) {
	} else {
	    if (point.getX() == lastp.getX()) {
		point = new Point2D.Double(point.getX() + 0.00000001,
			point.getY());
	    } else if (point.getY() == lastp.getY()) {
		point = new Point2D.Double(point.getX(),
			point.getY() + 0.00000001);
	    }

	    if (point.getX() == antp.getX()) {
		point = new Point2D.Double(point.getX() + 0.00000001,
			point.getY());
	    } else if (point.getY() == antp.getY()) {
		point = new Point2D.Double(point.getX(),
			point.getY() + 0.00000001);
	    }

	    Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp, antp, lastp);
	    Point2D mediop = new Point2D.Double(
		    (point.getX() + lastp.getX()) / 2,
		    (point.getY() + lastp.getY()) / 2);
	    Point2D[] ps2 = UtilFunctions
		    .getPerpendicular(lastp, point, mediop);
	    Point2D interp = UtilFunctions.getIntersection(ps1[0], ps1[1],
		    ps2[0], ps2[1]);

	    double radio = interp.distance(lastp);

	    if (UtilFunctions.isLowAngle(antp, lastp, interp, point)) {
		radio = -radio;
	    }

	    Point2D centerp = UtilFunctions.getPoint(interp, mediop, radio);

	    drawLine((Graphics2D) g, lastp, point,
		    DefaultCADTool.geometrySelectSymbol);

	    IGeometry ig = ShapeFactory.createArc(lastp, centerp, point);

	    if (ig != null) {
		ig.draw((Graphics2D) g, getCadToolAdapter().getMapControl()
			.getViewPort(), DefaultCADTool.axisReferencesSymbol);
	    }
	}
    }

    /**
     * Add a diferent option.
     */
    @Override
    public void addOption(String s) {
	PipeCADToolState actualState = (PipeCADToolState) _fsm
		.getPreviousState();
	String status = actualState.getName();

	if (status.equals("Polyline.NextPointOrArc")) {
	    if (s.equals("A") || s.equals("a")
		    || s.equals(PluginServices.getText(this, "inter_arc"))) {
		// Arco
	    }
	} else if (status.equals("Polyline.NextPointOrLine")) {
	    if (s.equals("N") || s.equals("n")
		    || s.equals(PluginServices.getText(this, "inter_line"))) {
		// L�nea
	    }
	}

    }

    @Override
    public void addValue(double d) {
    }

    public void cancel() {
	// endGeometry();
	list.clear();
	// TODO NachoV added:
	points.clear();
	antantPoint = antCenter = antInter = antPoint = firstPoint = null;
    }

    @Override
    public void end() {
	/*
	 * CADExtension.setCADTool("polyline");
	 * PluginServices.getMainFrame().setSelectedTool("POLYLINE");
	 */
	clear();
    }

    @Override
    public String getName() {
	return "_epanet_cadtool_pipe";
    }

    @Override
    public String toString() {
	return "_polyline";
    }

    @Override
    public boolean isApplicable(int shapeType) {
	switch (shapeType) {
	case FShape.POINT:
	case FShape.MULTIPOINT:
	    return false;
	}
	return true;
    }

    @Override
    public boolean isMultiTransition() {
	return true;
    }

    @Override
    public void setMultiTransition(boolean condicion) {
    }

    public void setPreviousTool(DefaultCADTool tool) {
    }

    @Override
    public void transition(InputEvent event) {
	_fsm.removePoint(event, points.size());
    }

}
