/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jegroupware.egroupware;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * EGroupwareHttpResumable
 * @author Stefan Werfling
 */
public class EgroupwareHttpResumable {

	/**
	 * resumble chunk size
	 */
	protected long _resumableChunkSize = 1048576;

	/**
	 * resumble current chunk size
	 */
	protected long _resumableCurrentChunkSize = 1048576;

	/**
	 * resumble chunk number
	 */
	protected int _resumableChunkNumber;

	/**
	 * resumble total size
	 */
	protected long _resumableTotalSize;

	/**
	 * resumble type
	 */
	protected String _resumableType;

	/**
	 * resumble identifier
	 */
	protected String _resumableIdentifier;

	/**
	 * resumble filename
	 */
	protected String _resumableFilename;

	/**
	 * resumble relative path
	 */
	protected String _resumableRelativePath;

	/**
	 * resumble total chunks
	 */
	protected long _resumableTotalChunks;

	/**
	 * EgroupwareHttpResumable
	 * @param connection
	 */
	public EgroupwareHttpResumable(File lfile) throws FileNotFoundException, IOException {
		FileInputStream ainputStream = new FileInputStream(lfile);

		this._resumableTotalSize	= ainputStream.available();
		this._resumableType			= "application/x-file";
		this._resumableFilename		= lfile.getName();
		this._resumableRelativePath = this._resumableFilename;
		this._resumableChunkNumber	= 0;
		this._resumableIdentifier	= this._createIdent(this._resumableFilename);

		double totalchunks = 1;

		if( this._resumableTotalSize > this._resumableChunkSize ) {
			totalchunks = (double) this._resumableTotalSize / (double) this._resumableChunkSize;
		}

		this._resumableTotalChunks = (long) Math.floor(totalchunks);
		ainputStream.close();
	}

	/**
	 * _createIdent
	 * @param filename
	 * @return
	 */
	protected String _createIdent(String filename) {
		String ident = filename.replace(".", "");

		ident = ident.replace("-", "");
		ident = ident.replace("_", "");

		return Long.toString(this._resumableTotalSize) + "-" + ident;
	}

	/**
	 * getTotalSize
	 * @return
	 */
	public long getTotalSize() {
		return this._resumableTotalSize;
	}

	/**
	 * getChunkNumber
	 * @return
	 */
	public long getChunkNumber() {
		return this._resumableChunkNumber;
	}

	/**
	 * getChunkSize
	 * @return
	 */
	public long getChunkSize() {
		return this._resumableChunkSize;
	}

	/**
	 * getCurrentChunkSize
	 * @return
	 */
	public long getCurrentChunkSize() {
		return this._resumableCurrentChunkSize;
	}

	/**
	 * setCurrentChunkSize
	 * @param size
	 */
	public void setCurrentChunkSize(long size) {
		this._resumableCurrentChunkSize = size;
	}

	/**
	 * getType
	 * @return
	 */
	public String getType() {
		return this._resumableType;
	}

	/**
	 * getIdentifier
	 * @return
	 */
	public String getIdentifier() {
		return this._resumableIdentifier;
	}

	/**
	 * getFilename
	 * @return
	 */
	public String getFilename() {
		return this._resumableFilename;
	}

	/**
	 * getRelativePath
	 * @return
	 */
	public String getRelativePath() {
		return this._resumableRelativePath;
	}

	/**
	 * getTotalChunks
	 * @return
	 */
	public long getTotalChunks() {
		return this._resumableTotalChunks;
	}

	/**
	 * nextChunk
	 */
	public void nextChunk() {
		this._resumableChunkNumber++;
	}

	/**
	 * isLastChunk
	 * @return
	 */
	public Boolean isLastChunk() {
		if( this._resumableChunkNumber == this._resumableTotalChunks ) {
			return true;
		}

		return false;
	}
}