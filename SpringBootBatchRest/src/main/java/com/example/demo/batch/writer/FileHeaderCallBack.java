package com.example.demo.batch.writer;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

public class FileHeaderCallBack implements FlatFileHeaderCallback {

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write("");
	}

}
