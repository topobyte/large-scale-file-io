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

import java.io.InputStream;
import java.nio.file.Path;

public class SimpleClosingFileInputStreamFactory
		implements ClosingFileInputStreamFactory
{

	private int idFactory = 0;
	private ClosingFileInputStreamPool pool = new SimpleClosingFileInputStreamPool();

	@Override
	public InputStream create(Path file)
	{
		return new ClosingFileInputStream(pool, file, idFactory++);
	}

}
