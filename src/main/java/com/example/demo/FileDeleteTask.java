package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class FileDeleteTask implements Tasklet {

  private Resource[] inputResources;

  private String pathInput;

  private String filePattern;

  public FileDeleteTask(String pathInput, String filePattern) {
    this.pathInput = pathInput;
    this.filePattern = filePattern;
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    try {
      this.inputResources = resourcePatternResolver.getResources("file:" + pathInput + filePattern);
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Resource inputResource : inputResources) {
      Files.delete(Paths.get(inputResource.getFile().getPath()));
    }
    return RepeatStatus.FINISHED;
  }

  public void setInputResources(Resource[] inputResources) {
    this.inputResources = inputResources;
  }

}
