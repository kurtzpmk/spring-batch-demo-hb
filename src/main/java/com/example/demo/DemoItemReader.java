package com.example.demo;

import java.io.IOException;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class DemoItemReader extends MultiResourceItemReader<Game> {

  public DemoItemReader(String pathInput, String filePattern) {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    Resource[] inputResources = {};
    try {
      inputResources = resourcePatternResolver.getResources("file:" + pathInput + filePattern);
    } catch (IOException e) {
      e.printStackTrace();
    }
    setName("MultiResourceItemReader");
    setResources(inputResources);
    setDelegate(customFlatFileItemReader());
  }

  public FlatFileItemReader<Game> customFlatFileItemReader() {
    return new FlatFileItemReaderBuilder<Game>()
        .name("FlatFileItemReader")
        .delimited()
        .names("gameNumber", "gameLength")
        .targetType(Game.class)
        .build();
  }

}
