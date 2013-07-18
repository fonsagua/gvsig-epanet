/*
 * Copyright 2008 Deputación Provincial de A Coruña
 * Copyright 2009 Deputación Provincial de Pontevedra
 * Copyright 2010 CartoLab, Universidad de A Coruña
 *
 * This file is part of openCADTools, developed by the Cartography
 * Engineering Laboratory of the University of A Coruña (CartoLab).
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
 */

package es.udc.cartolab.gvsig.epanet.cad;

import java.awt.event.InputEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.CADStatus;

/**
 * @author Vicente Caballero Navarro
 * @author Laboratorio de Bases de Datos. Universidad de A Coruña
 * @author Cartolab. Universidad de A Coruña
 */
public final class PipeCADToolContext extends statemap.FSMContext {
    // ---------------------------------------------------------------
    // Member methods.
    //

    public PipeCADToolContext(PipeCADTool owner) {
	super();

	_owner = owner;
	setState(Polyline.FirstPoint);
	Polyline.FirstPoint.Entry(this);
    }

    public void addOption(String s) {
	_transition = "addOption";
	getState().addOption(this, s);
	_transition = "";
	return;
    }

    public void addPoint(double pointX, double pointY, InputEvent event) {
	_transition = "addPoint";
	getState().addPoint(this, pointX, pointY, event);
	_transition = "";
	return;
    }

    public void addValue(double d) {
	_transition = "addValue";
	getState().addValue(this, d);
	_transition = "";
	return;
    }

    public PipeCADToolState getState() throws statemap.StateUndefinedException {
	if (_state == null) {
	    throw (new statemap.StateUndefinedException());
	}

	return ((PipeCADToolState) _state);
    }

    protected PipeCADTool getOwner() {
	return (_owner);
    }

    public void removePoint(InputEvent event, int numPoints) {
	_transition = "removePoint";
	getState().removePoint(this, event, numPoints);
	_transition = "";
	return;
    }

    // ---------------------------------------------------------------
    // Member data.
    //

    transient private PipeCADTool _owner;

    // ---------------------------------------------------------------
    // Inner classes.
    //

    public static abstract class PipeCADToolState extends statemap.State {
	// -----------------------------------------------------------
	// Member methods.
	//

	protected PipeCADToolState(String name, int id) {
	    super(name, id);
	}

	protected void Entry(PipeCADToolContext context) {
	}

	protected void Exit(PipeCADToolContext context) {
	}

	protected abstract String[] getDescription();

	protected void addOption(PipeCADToolContext context, String s) {
	    Default(context);
	}

	protected void addPoint(PipeCADToolContext context, double pointX,
		double pointY, InputEvent event) {
	    Default(context);
	}

	protected void addValue(PipeCADToolContext context, double d) {
	    Default(context);
	}

	protected void Default(PipeCADToolContext context) {
	    throw (new statemap.TransitionUndefinedException("State: "
		    + context.getState().getName() + ", Transition: "
		    + context.getTransition()));
	}

	protected void removePoint(PipeCADToolContext context,
		InputEvent event, int numPoints) {
	    Default(context);
	}
	// -----------------------------------------------------------
	// Member data.
	//
    }

    /* package */static abstract class Polyline {
	// -----------------------------------------------------------
	// Member methods.
	//

	// -----------------------------------------------------------
	// Member data.
	//

	// -------------------------------------------------------
	// Statics.
	//
	/* package */static Polyline_Default.Polyline_FirstPoint FirstPoint;
	/* package */static Polyline_Default.Polyline_NextPointOrArcOrClose NextPointOrArcOrClose;
	/* package */static Polyline_Default.Polyline_NextPointOrLineOrClose NextPointOrLineOrClose;
	private static Polyline_Default Default;

	static {
	    FirstPoint = new Polyline_Default.Polyline_FirstPoint(
		    "Polyline.FirstPoint", 0);
	    NextPointOrArcOrClose = new Polyline_Default.Polyline_NextPointOrArcOrClose(
		    "Polyline.NextPointOrArcOrClose", 1);
	    NextPointOrLineOrClose = new Polyline_Default.Polyline_NextPointOrLineOrClose(
		    "Polyline.NextPointOrLineOrClose", 2);
	    Default = new Polyline_Default("Polyline.Default", -1);
	}

    }

    protected static class Polyline_Default extends PipeCADToolState {
	// -----------------------------------------------------------
	// Member methods.
	//

	protected Polyline_Default(String name, int id) {
	    super(name, id);
	}

	@Override
	protected String[] getDescription() {
	    return new String[] { "cancel" };
	}

	@Override
	protected void addOption(PipeCADToolContext context, String s) {
	    PipeCADTool ctxt = context.getOwner();

	    if (s.equals(PluginServices.getText(this, "cancel"))) {
		boolean loopbackFlag = context.getState().getName()
			.equals(Polyline.FirstPoint.getName());

		if (loopbackFlag == false) {
		    (context.getState()).Exit(context);
		}

		context.clearState();
		try {
		    ctxt.cancel();
		} finally {
		    context.setState(Polyline.FirstPoint);

		    if (loopbackFlag == false) {
			(context.getState()).Entry(context);
		    }

		}
	    } else if (s.equals("")) {
		boolean loopbackFlag = context.getState().getName()
			.equals(Polyline.FirstPoint.getName());

		if (loopbackFlag == false) {
		    (context.getState()).Exit(context);
		}

		context.clearState();
		try {
		    ctxt.endGeometry();
		} finally {
		    context.setState(Polyline.FirstPoint);

		    if (loopbackFlag == false) {
			(context.getState()).Entry(context);
		    }

		}
	    } else {
		ctxt.throwOptionException(
			PluginServices.getText(this, "incorrect_option"), s);
	    }

	    return;
	}

	@Override
	protected void addValue(PipeCADToolContext context, double d) {
	    PipeCADTool ctxt = context.getOwner();

	    boolean loopbackFlag = context.getState().getName()
		    .equals(Polyline.FirstPoint.getName());

	    if (loopbackFlag == false) {
		(context.getState()).Exit(context);
	    }

	    context.clearState();
	    try {
		ctxt.throwValueException(
			PluginServices.getText(this, "incorrect_value"), d);
	    } finally {
		context.setState(Polyline.FirstPoint);

		if (loopbackFlag == false) {
		    (context.getState()).Entry(context);
		}

	    }
	    return;
	}

	@Override
	protected void addPoint(PipeCADToolContext context, double pointX,
		double pointY, InputEvent event) {
	    PipeCADTool ctxt = context.getOwner();

	    boolean loopbackFlag = context.getState().getName()
		    .equals(Polyline.FirstPoint.getName());

	    if (loopbackFlag == false) {
		(context.getState()).Exit(context);
	    }

	    context.clearState();
	    try {
		ctxt.throwPointException(
			PluginServices.getText(this, "incorrect_point"),
			pointX, pointY);
	    } finally {
		context.setState(Polyline.FirstPoint);

		if (loopbackFlag == false) {
		    (context.getState()).Entry(context);
		}

	    }
	    return;
	}

	@Override
	protected void removePoint(PipeCADToolContext context,
		InputEvent event, int numPoints) {
	    PipeCADTool ctxt = context.getOwner();

	    boolean loopbackFlag = context.getState().getName()
		    .equals(Polyline.FirstPoint.getName());

	    if (loopbackFlag == false) {
		(context.getState()).Exit(context);
	    }

	    context.clearState();
	    try {
		ctxt.throwNoPointsException(PluginServices.getText(this,
			"no_points"));
	    } finally {
		context.setState(Polyline.FirstPoint);

		if (loopbackFlag == false) {
		    (context.getState()).Entry(context);
		}

	    }
	    return;
	}

	// -----------------------------------------------------------
	// Inner classse.
	//

	private static final class Polyline_FirstPoint extends Polyline_Default {
	    // -------------------------------------------------------
	    // Member methods.
	    //

	    private Polyline_FirstPoint(String name, int id) {
		super(name, id);
	    }

	    @Override
	    protected String[] getDescription() {
		return new String[] { "cancel" };
	    }

	    @Override
	    protected void Entry(PipeCADToolContext context) {
		PipeCADTool ctxt = context.getOwner();

		ctxt.setQuestion(PluginServices.getText(this,
			"insert_first_point"));
		ctxt.setDescription(getDescription());
		return;
	    }

	    @Override
	    protected void addPoint(PipeCADToolContext context, double pointX,
		    double pointY, InputEvent event) {
		PipeCADTool ctxt = context.getOwner();

		(context.getState()).Exit(context);
		context.clearState();
		try {
		    ctxt.addPoint(pointX, pointY, event);
		} finally {
		    context.setState(Polyline.NextPointOrArcOrClose);
		    (context.getState()).Entry(context);
		}
		return;
	    }

	    // -------------------------------------------------------
	    // Member data.
	    //
	}

	private static final class Polyline_NextPointOrArcOrClose extends
		Polyline_Default {
	    // -------------------------------------------------------
	    // Member methods.
	    //

	    private Polyline_NextPointOrArcOrClose(String name, int id) {
		super(name, id);
	    }

	    @Override
	    protected String[] getDescription() {
		return new String[] { "inter_arc", "close_polyline",
			"terminate", "cancel", "removePoint" };
	    }

	    @Override
	    protected void Entry(PipeCADToolContext context) {
		PipeCADTool ctxt = context.getOwner();
		boolean deleteButton3 = CADStatus.getCADStatus()
			.isDeleteButtonActivated();
		if (deleteButton3) {
		    ctxt.setQuestion(PluginServices.getText(this,
			    "insert_next_point_arc_or_close_del"));
		} else {
		    ctxt.setQuestion(PluginServices.getText(this,
			    "insert_next_point_arc_or_close"));
		}
		ctxt.setDescription(getDescription());
		return;
	    }

	    @Override
	    protected void addOption(PipeCADToolContext context, String s) {
		PipeCADTool ctxt = context.getOwner();

		if (s.equals("A") || s.equals("a")
			|| s.equals(PluginServices.getText(this, "inter_arc"))) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
		    } finally {
			context.setState(Polyline.NextPointOrLineOrClose);
			(context.getState()).Entry(context);
		    }
		} else if (s.equals("C")
			|| s.equals("c")
			|| s.equals(PluginServices.getText(this,
				"close_polyline"))) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
			ctxt.closeGeometry();
			ctxt.endGeometry();
			ctxt.end();
		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		}
		// [NachoV] else if (s.equals("T") || s.equals("t") ||
		// s.equals(PluginServices.getText(this,"terminate")))
		else if (s.equals("espacio")
			|| s.equals(PluginServices.getText(this, "terminate"))) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
			ctxt.endGeometry();
			ctxt.end();
			ctxt.fireEndGeometry();
		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		} else {
		    super.addOption(context, s);
		}

		return;
	    }

	    @Override
	    protected void addPoint(PipeCADToolContext context, double pointX,
		    double pointY, InputEvent event) {
		PipeCADTool ctxt = context.getOwner();

		PipeCADToolState endState = context.getState();

		context.clearState();
		try {
		    ctxt.addPoint(pointX, pointY, event);
		} finally {
		    context.setState(endState);
		    (context.getState()).Entry(context);
		}
		return;
	    }

	    @Override
	    protected void removePoint(PipeCADToolContext context,
		    InputEvent event, int numPoints) {
		PipeCADTool ctxt = context.getOwner();

		if (numPoints > 1) {
		    PipeCADToolState endState = context.getState();
		    context.clearState();
		    try {
			ctxt.removePoint(event);
		    } finally {
			context.setState(endState);
			(context.getState()).Entry(context);
		    }
		} else if (numPoints == 1) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.removePoint(event);

		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		} else {
		    super.removePoint(context, event, numPoints);
		}

		return;
	    }

	    // -------------------------------------------------------
	    // Member data.
	    //
	}

	private static final class Polyline_NextPointOrLineOrClose extends
		Polyline_Default {
	    // -------------------------------------------------------
	    // Member methods.
	    //

	    private Polyline_NextPointOrLineOrClose(String name, int id) {
		super(name, id);
	    }

	    @Override
	    protected String[] getDescription() {
		return new String[] { "inter_arc", "close_polyline",
			"terminate", "cancel", "removePoint" };
	    }

	    @Override
	    protected void Entry(PipeCADToolContext context) {
		PipeCADTool ctxt = context.getOwner();
		ctxt.setQuestion(PluginServices.getText(this,
			"insert_next_point_line_or_close"));
		ctxt.setDescription(getDescription());
		return;
	    }

	    @Override
	    protected void addOption(PipeCADToolContext context, String s) {
		PipeCADTool ctxt = context.getOwner();

		if (s.equals("N") || s.equals("n")
			|| s.equals(PluginServices.getText(this, "inter_line"))) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
		    } finally {
			context.setState(Polyline.NextPointOrArcOrClose);
			(context.getState()).Entry(context);
		    }
		} else if (s.equals("C")
			|| s.equals("c")
			|| s.equals(PluginServices.getText(this,
				"close_polyline"))) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
			ctxt.closeGeometry();
			ctxt.endGeometry();
			ctxt.end();
		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		} else if (s.equals("espacio")
			|| s.equals(PluginServices.getText(this, "terminate")))
		// else if (s.equals("T") || s.equals("t") ||
		// s.equals(PluginServices.getText(this,"terminate")))
		{

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.addOption(s);
			ctxt.endGeometry();
			ctxt.end();
			ctxt.fireEndGeometry();
		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		} else {
		    super.addOption(context, s);
		}

		return;
	    }

	    @Override
	    protected void addPoint(PipeCADToolContext context, double pointX,
		    double pointY, InputEvent event) {
		PipeCADTool ctxt = context.getOwner();

		PipeCADToolState endState = context.getState();

		context.clearState();
		try {
		    ctxt.addPoint(pointX, pointY, event);
		} finally {
		    context.setState(endState);
		    (context.getState()).Entry(context);
		}
		return;
	    }

	    @Override
	    protected void removePoint(PipeCADToolContext context,
		    InputEvent event, int numPoints) {
		PipeCADTool ctxt = context.getOwner();

		if (numPoints > 1) {
		    PipeCADToolState endState = context.getState();

		    context.clearState();
		    try {
			ctxt.removePoint(event);
		    } finally {
			context.setState(endState);
			(context.getState()).Entry(context);
		    }
		} else if (numPoints == 1) {

		    (context.getState()).Exit(context);
		    context.clearState();
		    try {
			ctxt.removePoint(event);
		    } finally {
			context.setState(Polyline.FirstPoint);
			(context.getState()).Entry(context);
		    }
		} else {
		    super.removePoint(context, event, numPoints);
		}

		return;
	    }

	    // -------------------------------------------------------
	    // Member data.
	    //
	}

	// -----------------------------------------------------------
	// Member data.
	//
    }
}
