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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestClosingFileOutputStream
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
		test(1, false);
		test(2, false);
		test(10, false);
		test(1, true);
		test(2, true);
		test(10, true);
	}

	public void test(int n, boolean existingFiles) throws IOException
	{
		files = new Path[n];
		for (int i = 0; i < n; i++) {
			files[i] = Files.createTempFile("closing-fos", ".dat");
		}
		allFiles.addAll(Arrays.asList(files));

		ByteArrayGenerator generator = new ByteArrayGenerator();
		byte[][] bytes = new byte[n][];
		for (int i = 0; i < n; i++) {
			bytes[i] = generator.generateBytes(1024);
		}

		if (existingFiles) {
			for (int i = 0; i < n; i++) {
				writeSomeData(files[i], generator);
			}
		}

		ClosingFileOutputStreamFactory factory = new SimpleClosingFileOutputStreamFactory();

		OutputStream[] outputs = new OutputStream[n];
		for (int i = 0; i < n; i++) {
			outputs[i] = factory.create(files[i]);
		}

		WriterUtil.writeInterleaved(outputs, bytes);

		for (int i = 0; i < n; i++) {
			outputs[i].close();
		}

		for (int i = 0; i < n; i++) {
			byte[] read = Files.readAllBytes(files[i]);
			Assert.assertArrayEquals(bytes[i], read);
		}
	}

	@Test
	@SuppressWarnings("resource")
	public void testOpenTruncatesFile() throws IOException
	{
		Path file = Files.createTempFile("closing-fos", ".dat");
		allFiles.add(file);

		ByteArrayGenerator generator = new ByteArrayGenerator();
		writeSomeData(file, generator);

		SimpleClosingFileOutputStreamPool factory = new SimpleClosingFileOutputStreamPool();
		new ClosingFileOutputStream(factory, file, 0);

		byte[] read = Files.readAllBytes(file);
		Assert.assertEquals(0, read.length);
	}

	private void writeSomeData(Path file, ByteArrayGenerator generator)
			throws IOException
	{
		byte[] bytes = generator.generateBytes(100);
		try (OutputStream fos = Files.newOutputStream(file)) {
			fos.write(bytes);
		}
	}

}
