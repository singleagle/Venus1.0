package com.enjoy.venus.persistence;

import java.io.Writer;

public interface IEntityWriter {
	public void write(Writer writer, IEntity entity);
}
