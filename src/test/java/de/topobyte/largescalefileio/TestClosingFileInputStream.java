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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestClosingFileInputStream
{

	private Set<Path> allFiles = new HashSet<>();
	private Path[] files;

	@After
	public void cleanup() throws IOException
	{
		for (Path file : allFiles) {
			Files.delete(file);
		}
	}

	@Test
	public void test() throws IOException
	{
		test(1);
		test(2);
		test(10);
	}

	public void test(int n) throws IOException
	{
		files = new Path[n];
		for (int i = 0; i < n; i++) {
			files[i] = Files.createTempFile("closing-fis", ".dat");
		}
		allFiles.addAll(Arrays.asList(files));

		ByteArrayGenerator generator = new ByteArrayGenerator();
		byte[][] bytes = new byte[n][];
		for (int i = 0; i < n; i++) {
			bytes[i] = generator.generateBytes(1024);
		}

		for (int i = 0; i < n; i++) {
			Files.write(files[i], bytes[i]);
		}

		ClosingFileInputStreamFactory factory = new SimpleClosingFileInputStreamFactory();

		InputStream[] inputs = new InputStream[n];
		for (int i = 0; i < n; i++) {
			inputs[i] = factory.create(files[i]);
		}

		byte[][] results = ReaderUtil.readInterleaved(inputs);

		for (int i = 0; i < n; i++) {
			Assert.assertArrayEquals(bytes[i], results[i]);
		}
	}

}
