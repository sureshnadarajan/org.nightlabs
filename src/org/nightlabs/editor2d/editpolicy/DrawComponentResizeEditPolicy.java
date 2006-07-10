/* *****************************************************************************
 * NightLabs Editor2D - Graphical editor framework                             *
 * Copyright (C) 2004-2005 NightLabs - http://NightLabs.org                    *
 * Project author: Daniel Mazurek <Daniel.Mazurek [at] nightlabs [dot] org>    *
 *                                                                             *
 * This library is free software; you can redistribute it and/or               *
 * modify it under the terms of the GNU Lesser General Public                  *
 * License as published by the Free Software Foundation; either                *
 * version 2.1 of the License, or (at your option) any later version.          *
 *                                                                             *
 * This library is distributed in the hope that it will be useful,             *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU           *
 * Lesser General Public License for more details.                             *
 *                                                                             *
 * You should have received a copy of the GNU Lesser General Public            *
 * License along with this library; if not, write to the                       *
 *     Free Software Foundation, Inc.,                                         *
 *     51 Franklin St, Fifth Floor,                                            *
 *     Boston, MA  02110-1301  USA                                             *
 *                                                                             *
 * Or get it online :                                                          *
 *     http://www.gnu.org/copyleft/lesser.html                                 *
 *                                                                             *
 *                                                                             *
 ******************************************************************************/

package org.nightlabs.editor2d.editpolicy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.graphics.Color;
import org.nightlabs.editor2d.EditorStateManager;
import org.nightlabs.editor2d.edit.ShapeDrawComponentEditPart;
import org.nightlabs.editor2d.figures.ShapeFigure;
import org.nightlabs.editor2d.handle.EditorNonResizableHandleKit;
import org.nightlabs.editor2d.handle.EditorResizableHandleKit;
import org.nightlabs.editor2d.handle.RotateHandleKit;
import org.nightlabs.editor2d.handle.ShapeEditHandleKit;
import org.nightlabs.editor2d.request.EditorRequestConstants;
import org.nightlabs.editor2d.util.EditorUtil;
import org.nightlabs.editor2d.util.feedback.FeedbackUtil;


public class DrawComponentResizeEditPolicy 
extends ResizableEditPolicy 
implements EditorRequestConstants
{
  public static final Logger LOGGER = Logger.getLogger(DrawComponentResizeEditPolicy.class);
     
  /**
   * Creates the figure used for feedback.
   * @return the new feedback figure
   */
  protected IFigure createDragSourceFeedbackFigure() 
  {       	
    IFigure figure = getCustomFeedbackFigure(getHost().getModel());
  	figure.setBounds(getInitialFeedbackBounds());
  	addFeedback(figure);
  	return figure;
  }
             
  protected ShapeFigure getCustomFeedbackFigure(Object modelPart) 
  {
  	return FeedbackUtil.getCustomFeedbackFigure(modelPart);
  }  
  
  protected Rectangle getInitialFeedbackBounds() 
  {
  	return getHostFigure().getBounds();
  }
    
  protected List createSelectionHandles() 
  {
  	List list = new ArrayList();  	
    if (EditorStateManager.getCurrentState() == EditorStateManager.STATE_EDIT_SHAPE) 
    {
    	if (getHost() instanceof ShapeDrawComponentEditPart) {
    	  ShapeDrawComponentEditPart sdcEditPart = (ShapeDrawComponentEditPart) getHost();
    	  ShapeEditHandleKit.addHandles(sdcEditPart, list);
    	  return list;
    	}  	        
    }
    else if (EditorStateManager.getCurrentState() == EditorStateManager.STATE_ROTATE)
    {
      clearHandleLayer();
      RotateHandleKit.addHandles(getHost().getViewer().getSelectedEditParts(), list);
      return list;
    }
    else if (EditorStateManager.getCurrentState() == EditorStateManager.STATE_NORMAL_SELECTION)
    {    	
    	// WORKAROUND: use own ResizableHandleKit and NonResizeableHandleKit
    	// because MoveHandle are created with white line by default instead of black line
    	int directions = getResizeDirections();
    	if (directions == 0)
    		EditorNonResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);
    	else if (directions != -1) {
    		EditorResizableHandleKit.addMoveHandle((GraphicalEditPart)getHost(), list);
    		if ((directions & PositionConstants.EAST) != 0)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.EAST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.EAST);
    		if ((directions & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.SOUTH_EAST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list,
    					PositionConstants.SOUTH_EAST);
    		if ((directions & PositionConstants.SOUTH) != 0)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.SOUTH);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.SOUTH);
    		if ((directions & PositionConstants.SOUTH_WEST) == PositionConstants.SOUTH_WEST)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.SOUTH_WEST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    						PositionConstants.SOUTH_WEST);
    		if ((directions & PositionConstants.WEST) != 0)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.WEST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    						PositionConstants.WEST);
    		if ((directions & PositionConstants.NORTH_WEST) == PositionConstants.NORTH_WEST)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.NORTH_WEST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.NORTH_WEST);
    		if ((directions & PositionConstants.NORTH) != 0)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.NORTH);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.NORTH);
    		if ((directions & PositionConstants.NORTH_EAST) == PositionConstants.NORTH_EAST)
    			EditorResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    					PositionConstants.NORTH_EAST);
    		else
    			EditorNonResizableHandleKit.addHandle((GraphicalEditPart)getHost(), list, 
    						PositionConstants.NORTH_EAST);	
    	} else
    		EditorResizableHandleKit.addHandles((GraphicalEditPart)getHost(), list);    	    	
    }
   	return list;    
//  	return super.createSelectionHandles();  	  	   	
  }
    
  protected void clearHandleLayer() 
  {
  	IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);
  	for (int i = 0; i < layer.getChildren().size(); i++) {
  	  IFigure figure = (IFigure) layer.getChildren().get(i);
  		layer.remove(figure);  	  
  	}
  }  
  
  protected void removeSelectionHandles() 
  {
  	if (handles == null)  	  
  		return;
  	
  	IFigure layer = getLayer(LayerConstants.HANDLE_LAYER);  	
  	if (layer.getChildren().isEmpty())
  	  return;
  	
  	for (int i = 0; i < handles.size(); i++) {
  	  if (layer.getChildren().contains(handles.get(i)))
  	    layer.remove((IFigure)handles.get(i));  	  
  	}
  	
  	handles = null;
  }  
            
  public Rectangle getConstraintFor(Rectangle rectangle) {
    return EditorUtil.oldToAbsolute((GraphicalEditPart)getHost(), rectangle);
  }
//  public Rectangle getConstraintFor(Rectangle rectangle) {
//    return EditorUtil.toAbsoluteWithScrollOffset(getHost(), rectangle);
//  }
  
  public Point getConstraintFor(Point point){
    return EditorUtil.toAbsolute((GraphicalEditPart)getHost(), point);
  } 
//  public Point getConstraintFor(Point point){
//    return EditorUtil.toAbsoluteWithScrollOffset(getHost(), point.x, point.y);
//  } 
  
  protected Point getScrollOffset() {
    return EditorUtil.getScrollOffset(getHost());
  }
  
// ****************************** BEGIN Workaround private feedback *****************************  
  
  protected IFigure feedback;
  
  /**
   * Lazily creates and returns the feedback figure used during drags.
   * @return the feedback figure
   */
  protected IFigure getDragSourceFeedbackFigure() 
  {
  	if (feedback == null)
  		feedback = createDragSourceFeedbackFigure();
  	return feedback;
  }
      
  /**
   * @see org.eclipse.gef.EditPolicy#deactivate()
   */
  public void deactivate() {
  	if (feedback != null) {
  		removeFeedback(feedback);
  		feedback = null;
  	}
  	hideFocus();
  	super.deactivate();
  }  
    
// ****************************** END Workaround private feedback *****************************

  protected boolean showFeedBackText = true;
  protected Label feedbackLabel;
    
  protected void showChangeBoundsFeedback(ChangeBoundsRequest request) 
  {
  	IFigure feedback = getDragSourceFeedbackFigure();
  	
  	PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
  	getHostFigure().translateToAbsolute(rect);
  	rect.translate(request.getMoveDelta());
  	rect.resize(request.getSizeDelta());
  	
  	feedback.translateToRelative(rect);
  	feedback.setBounds(rect);

  	if (showFeedBackText) {
  		showFeedbackText(request);
  	}
  	  	
  	getFeedbackLayer().repaint();  	
  }    
  
  /**
   * Erases drag feedback.  This method called whenever an erase feedback request is
   * received of the appropriate type.
   * @param request the request
   */
  protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) 
  {
  	if (feedback != null) {
  		removeFeedback(feedback);
  		if (showFeedBackText) {
  			eraseFeedbackText();
  		}
  	}
  	feedback = null;
  }   
  
  protected Label getFeedbackTextFigure()
  {
  	if (feedbackLabel == null)
  		feedbackLabel = createFeedbackTextFigure("");
  	return feedbackLabel;
  }
  
  protected Color outlineColor = ColorConstants.black;
  protected Color getOutlineColor() {
  	return outlineColor;
  }
  
  protected Label createFeedbackTextFigure(String text) 
  {       	
    Label l = new Label(text);
  	l.setForegroundColor(getOutlineColor());    
  	l.setBounds(getInitialFeedbackBounds());
  	addFeedback(l);  	  	
  	return l;
  }  
    
  protected void showFeedbackText(ChangeBoundsRequest request) 
  {
  	Label feedbackText = getFeedbackTextFigure(); 
  	
  	feedbackText.setText(getText(request));
  	feedbackText.setLocation(getFeedbackTextLocation(request));  	
  	feedbackText.setSize(100, 20);
  	  	
  	getFeedbackLayer().repaint();
  	
  	LOGGER.debug("showFeedbackText!");
  }  
  
  protected static final Dimension EMPTY_DIMENSION = new Dimension(0,0);
  protected static final Point EMPTY_POINT = new Point(0,0);
  
  protected Point getFeedbackTextLocation(ChangeBoundsRequest request) 
  {
  	Point loc = request.getLocation();
  	if (loc != null) {
  		loc.translate(EditorUtil.getScrollOffset(getHost()));  	
  		return loc;  		
  	} else {
  		return new Point();
  	}
  }
  
  protected String getText(ChangeBoundsRequest request) 
  {
  	Dimension sizeDelta = request.getSizeDelta();
//  	Point moveDelta = request.getMoveDelta();  
  	StringBuffer sb = new StringBuffer();
  	Rectangle feedbackBounds = getDragSourceFeedbackFigure().getBounds();
  	
  	if (sizeDelta.equals(EMPTY_DIMENSION)) {  	
    	Point absoluteLocation = EditorUtil.toAbsolute(getHost(), feedbackBounds.x, feedbackBounds.y);
  		
    	String x = "X";
    	String y = "Y";
    	sb.append(x+" ");
    	sb.append(absoluteLocation.x);
    	sb.append(", ");
    	sb.append(y+" ");
    	sb.append(absoluteLocation.y);    	  		
  	}
  	else {
    	Point absoluteSize = EditorUtil.toAbsolute(getHost(), feedbackBounds.width, feedbackBounds.height);
    	
    	String width = "W";
    	String height = "H";
    	sb.append(width+" ");
    	sb.append(absoluteSize.x);
    	sb.append(", ");
    	sb.append(height+" ");
    	sb.append(absoluteSize.y);    	  		   		
  	}
  	return sb.toString();  	
  }
    
  protected void eraseFeedbackText() 
  {
    if (feedbackLabel != null)
      removeFeedback(feedbackLabel);
    
    feedbackLabel = null;
  }  
}
