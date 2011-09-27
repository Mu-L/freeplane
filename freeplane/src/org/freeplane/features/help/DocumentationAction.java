/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.help;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.MenuBuilder;
import org.freeplane.core.ui.components.FreeplaneMenuBar;
import org.freeplane.core.util.HtmlUtils;
import org.freeplane.core.util.LogUtils;
import org.freeplane.core.util.MenuUtils;
import org.freeplane.core.util.MenuUtils.MenuEntry;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.map.MapController;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.mode.browsemode.BModeController;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.features.styles.LogicalStyleModel;
import org.freeplane.features.styles.StyleFactory;

class DocumentationAction extends AFreeplaneAction {
	private static final long serialVersionUID = 1L;
	private final String document;

	DocumentationAction( final String actionName, final String document) {
		super(actionName);
		this.document = document;
	}

	public void actionPerformed(final ActionEvent e) {
		final ResourceController resourceController = ResourceController.getResourceController();
		final File baseDir = new File(resourceController.getResourceBaseDir()).getAbsoluteFile().getParentFile();
		final File file;
		final int extPosition = document.lastIndexOf('.');
		if (extPosition != -1) {
			final String languageCode = resourceController.getLanguageCode();
			final String map = document.substring(0, extPosition) + "_" + languageCode
			        + document.substring(extPosition);
			final File localFile = new File(baseDir, map);
			if (localFile.canRead()) {
				file = localFile;
			}
			else {
				file = new File(baseDir, document);
			}
		}
		else {
			file = new File(baseDir, document);
		}
		try {
			final URL endUrl = file.toURL();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						if (endUrl.getFile().endsWith(".mm")) {
							 Controller.getCurrentController().selectMode(BModeController.MODENAME);
							 Controller.getCurrentModeController().getMapController().newMap(endUrl, false);
						}
						else {
							Controller.getCurrentController().getViewController().openDocument(endUrl);
						}
					}
					catch (final Exception e1) {
						LogUtils.severe(e1);
					}
				}
			});
		}
		catch (final MalformedURLException e1) {
			LogUtils.warn(e1);
		}
	}

	@Override
	public void afterMapChange(final Object newMap) {
	}

}
