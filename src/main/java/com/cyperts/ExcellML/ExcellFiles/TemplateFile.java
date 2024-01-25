package com.cyperts.ExcellML.ExcellFiles;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class TemplateFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fileId")
	long id;
	@Column(name = "name")
	String fileName;
	@Lob
	@Column(name = "content", columnDefinition = "LONGBLOB")
	byte[] content;
	@Column(name = "filetype")
	private String fileType;

	@Column(name = "userId")
	private long userId;

	@Column(name = "createdOn")
	private long createdOn;

	@Column(name = "editedOn")
	private long editedOn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public long getEditedOn() {
		return editedOn;
	}

	public void setEditedOn(long editedOn) {
		this.editedOn = editedOn;
	}

	@Override
	public String toString() {
		return "TemplateFile [id=" + id + ", fileName=" + fileName + ", content=" + Arrays.toString(content)
				+ ", fileType=" + fileType + ", userId=" + userId + ", createdOn=" + createdOn + ", editedOn="
				+ editedOn + "]";
	}

	public TemplateFile(String fileName, byte[] content, String fileType, long userId) {
		super();
		this.fileName = fileName;
		this.content = content;
		this.fileType = fileType;
		this.userId = userId;
	}

	public TemplateFile(long id, String fileName, byte[] content, String fileType, long userId, long createdOn,
			long editedOn) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.content = content;
		this.fileType = fileType;
		this.userId = userId;
		this.createdOn = createdOn;
		this.editedOn = editedOn;
	}

	@PrePersist
	protected void prePersistFunction() {
		this.createdOn = System.currentTimeMillis();
		this.editedOn = System.currentTimeMillis();
	}

	@PreUpdate
	protected void preUpdateFunction() {
		this.editedOn = System.currentTimeMillis();
	}

	public TemplateFile() {
		super();
	}

}
