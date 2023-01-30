package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private PlatformTransactionManager platformTransactionManager;

  @Value("${pathInput}")
  private String pathInput;

  @Value("${pathOutput}")
  private String pathOutput;

  @Value("${filePattern}")
  private String filePattern;

  @Bean
  public Job createJob() {
    return new JobBuilder("CreateJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(createStep())
        .next(fileDeleteStep())
        .build();
  }

  @Bean
  public Step createStep() {
    return new StepBuilder("CreateStep", jobRepository)
        .<Game, Game>chunk(2, platformTransactionManager)
        .reader(demoItemReader())
        .writer(demoItemWriter())
        .build();
  }

  @Bean
  public Step fileDeleteStep() {
    return new StepBuilder("FileDeleteStep", jobRepository)
        .tasklet(fileDeleteTask(), platformTransactionManager)
        .build();
  }

  @Bean
  public DemoItemReader demoItemReader() {
    return new DemoItemReader(pathInput, filePattern);
  }

  @Bean
  public DemoItemWriter demoItemWriter() {
    return new DemoItemWriter(pathOutput);
  }

  @Bean
  public FileDeleteTask fileDeleteTask() {
    return new FileDeleteTask(pathInput, filePattern);
  }

}
