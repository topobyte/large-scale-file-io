// Copyright 2015 Sebastian Kuerten
//
// This file is part of large-scale-file-io.
//
// large-scale-file-io is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// large-scale-file-io is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with large-scale-file-io. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.largescalefileio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class ClosingFileOutputStream extends OutputStream
{

	private ClosingFileOutputStreamPool pool;
	private Path file;
	private int id;
	private boolean append = false;

	public ClosingFileOutputStream(ClosingFileOutputStreamPool pool, Path file,
			int id) throws IOException
	{
		this.pool = pool;
		this.file = file;
		this.id = id;

		// Open and close the file to emulate FileOutputStream behavior
		// concerning file truncation
		OutputStream out = Files.newOutputStream(file);
		out.close();
	}

	public int getId()
	{
		return id;
	}

	@Override
	public void write(int b) throws IOException
	{
		OutputStream fos = pool.create(file, id, append);
		append = true;
		fos.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		OutputStream fos = pool.create(file, id, append);
		append = true;
		fos.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		OutputStream fos = pool.create(file, id, append);
		append = true;
		fos.write(b, off, len);
	}

	@Override
	public void flush() throws IOException
	{
		pool.flush(id);
	}

	@Override
	public void close() throws IOException
	{
		pool.close(id);
	}

}
