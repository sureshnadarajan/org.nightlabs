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

package org.nightlabs.editor2d.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nightlabs.editor2d.EditorPlugin;
import org.nightlabs.editor2d.ImageDrawComponent;
import org.nightlabs.editor2d.image.RenderModeMetaData;

public class CreateImageCommand 
extends CreateDrawComponentCommand 
{
	public static final Logger LOGGER = Logger.getLogger(CreateImageCommand.class);
  public CreateImageCommand() 
  {
    super();
    setLabel(EditorPlugin.getResourceString("command.create.image"));  
  }
  
  public ImageDrawComponent getImageDrawComponent() {
    return (ImageDrawComponent) getChild();
  }
    
  protected String fileName = null;
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  protected String simpleFileName = null;    
  public void setSimpleFileName(String simpleFileName) {
    this.simpleFileName = simpleFileName;
  }
    
  protected List<RenderModeMetaData> renderModeMetaDatas = new LinkedList<RenderModeMetaData>();
//  public void addRenderModeMetaData(RenderModeMetaData renderModeMetaData) {
//  	renderModeMetaDatas.add(renderModeMetaData);
//  }
  public void setRenderModeMetaData(List<RenderModeMetaData> renderModeMetaDatas) {
  	this.renderModeMetaDatas = renderModeMetaDatas;
  }  
  
  public void execute() 
  {
  	super.execute();
    try {
    	File file = new File(fileName);
    	FileInputStream fis = new FileInputStream(file);
   		getImageDrawComponent().loadImage(simpleFileName, file.lastModified(), fis);
   		getImageDrawComponent().getRenderModeMetaDataList().addAll(renderModeMetaDatas);
    }
    catch (FileNotFoundException e) {
    	throw new RuntimeException(e);    
    }    
  }  
    
}
