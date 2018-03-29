package com.swing.win.tools.droptarget;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class DropTargetTools {

	/**
	 * The get DropTarget
	 * 
	 * @param pane
	 * @param l
	 * @return
	 */
	public static DropTarget getDropTarget(Component pane, final DropFileListener l){
		return new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE,
				new DropTargetListener() {// Implementation of the
			// DropTargetListener interface
			public void dragEnter(DropTargetDragEvent dtde) {
				// Get the type of object being transferred and
				// determine
				// whether it is appropriate.
				checkTransferType(dtde);
				// Accept or reject the drag.
				acceptOrRejectDrag(dtde);
			}

			public void dragExit(DropTargetEvent dte) {}

			public void dragOver(DropTargetDragEvent dtde) {
				// Accept or reject the drag
				acceptOrRejectDrag(dtde);
			}

			public void dropActionChanged(DropTargetDragEvent dtde) {
				// Accept or reject the drag
				acceptOrRejectDrag(dtde);
			}

			public void drop(DropTargetDropEvent dtde) {
				// Check the drop action
				if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
					// Accept the drop and get the transfer data
					dtde.acceptDrop(dtde.getDropAction());
					Transferable transferable = dtde.getTransferable();
					try {
						boolean result = dropFile(transferable);
						dtde.dropComplete(result);
					} catch (Exception e) {
						dtde.dropComplete(false);
					}
				} else {
					dtde.rejectDrop();
				}
			}

			// Internal methods start here

			protected boolean acceptableType;
			protected boolean acceptOrRejectDrag(
					DropTargetDragEvent dtde) {
				int dropAction = dtde.getDropAction();
				int sourceActions = dtde.getSourceActions();
				boolean acceptedDrag = false;

				// Reject if the object being transferred
				// or the operations available are not acceptable.
				if (!acceptableType
						|| (sourceActions & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
					dtde.rejectDrag();
				} else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE) == 0) {
					// Not offering copy or move - suggest a copy
					dtde.acceptDrag(DnDConstants.ACTION_COPY);
					acceptedDrag = true;
				} else {
					// Offering an acceptable operation: accept
					dtde.acceptDrag(dropAction);
					acceptedDrag = true;
				}

				return acceptedDrag;
			}

			protected void checkTransferType(DropTargetDragEvent dtde) {
				// Only accept a list of files
				acceptableType = dtde
						.isDataFlavorSupported(DataFlavor.javaFileListFlavor);

			}

			// This method handles a drop for a list of files
			@SuppressWarnings("unchecked")
			protected boolean dropFile(Transferable transferable)
					throws IOException, UnsupportedFlavorException,
					MalformedURLException {
				List<File> fileList = (List<File>) transferable
						.getTransferData(DataFlavor.javaFileListFlavor);
				if(l != null)
					l.dropFile(fileList);
				return true;
			}
		}, true, null);
	}
	
}

