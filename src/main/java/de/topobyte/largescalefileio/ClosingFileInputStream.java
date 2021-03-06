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
import java.io.InputStream;
import java.nio.file.Path;

class ClosingFileInputStream extends InputStream
{

	private ClosingFileInputStreamPool pool;
	private Path file;
	private int id;
	private long pos = 0;

	public ClosingFileInputStream(ClosingFileInputStreamPool pool, Path file,
			int id)
	{
		this.pool = pool;
		this.file = file;
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	@Override
	public void close() throws IOException
	{
		pool.close(id);
	}

	@Override
	public int read() throws IOException
	{
		InputStream fis = pool.create(file, id, pos);
		int r = fis.read();
		if (r >= 0) {
			pos++;
		}
		return r;
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		InputStream fis = pool.create(file, id, pos);
		int r = fis.read(b);
		if (r >= 0) {
			pos += r;
		}
		return r;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		InputStream fis = pool.create(file, id, pos);
		int r = fis.read(b, off, len);
		if (r >= 0) {
			pos += r;
		}
		return r;
	}

	@Override
	public long skip(long n) throws IOException
	{
		InputStream fis = pool.create(file, id, pos);
		long r = fis.skip(n);
		if (r >= 0) {
			pos += r;
		}
		return r;
	}

	@Override
	public int available() throws IOException
	{
		InputStream fis = pool.create(file, id, pos);
		return fis.available();
	}

}
