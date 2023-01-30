package com.example.demo;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;

public class DemoItemWriter implements ItemWriter<Game> {

  private String pathOutput;

  @Override
  public void write(Chunk<? extends Game> items) throws Exception {
    for (Chunk<? extends Game>.ChunkIterator chunkIterator = items.iterator(); chunkIterator.hasNext();) {
      Game i = chunkIterator.next();
      FlatFileItemWriter<Game> itemWriter = new FlatFileItemWriter<>();
      itemWriter.setName("DemoItemWriter");
      itemWriter.setResource(new FileSystemResource(pathOutput + i.getFileName()));
      itemWriter.setLineAggregator(customLineAggregator());
      itemWriter.setAppendAllowed(true);
      itemWriter.open(new ExecutionContext());
      itemWriter.write(new Chunk<>(i));
      itemWriter.close();
    }
  }

  public DemoItemWriter(String pathOutput) {
    this.pathOutput = pathOutput;
  }

  public LineAggregator<Game> customLineAggregator() {
    DelimitedLineAggregator<Game> delimitedLineAggregator = new DelimitedLineAggregator<>();
    delimitedLineAggregator.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
    delimitedLineAggregator.setFieldExtractor(customFieldExtractor());
    return delimitedLineAggregator;
  }

  public FieldExtractor<Game> customFieldExtractor() {
    BeanWrapperFieldExtractor<Game> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
    beanWrapperFieldExtractor.setNames(new String[] { "gameNumber", "gameLength" });
    return beanWrapperFieldExtractor;
  }

}
