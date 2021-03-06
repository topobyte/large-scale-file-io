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
import java.nio.file.Files;
import java.nio.file.Path;

class SimpleClosingFileInputStreamPool implements ClosingFileInputStreamPool
{

	private InputStream cache = null;
	private int cacheId = -1;

	@Override
	public InputStream create(Path file, int id, long pos) throws IOException
	{
		if (cache == null) {
			cache = Files.newInputStream(file);
			seek(pos);
			cacheId = id;
			return cache;
		} else if (cacheId == id) {
			return cache;
		} else {
			cache.close();
			cache = Files.newInputStream(file);
			seek(pos);
			cacheId = id;
			return cache;
		}
	}

	private void seek(long pos) throws IOException
	{
		long remaining = pos;
		while (remaining > 0) {
			long s = cache.skip(remaining);
			if (s < 0) {
				throw new IOException();
			}
			remaining -= s;
		}
	}

	@Override
	public void close(int id) throws IOException
	{
		if (id == cacheId) {
			cache.close();
			cache = null;
			cacheId = -1;
		}
	}

}
