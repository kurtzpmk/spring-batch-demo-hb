package com.example.demo;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import lombok.Data;

@Data
public class Game implements ResourceAware {

  private String gameNumber;

  private String gameLength;
  
  private String fileName;

  @Override
  public String toString() {
    return "gameNumber: " + gameNumber + " gameLength: " + gameLength;
  }

  @Override
  public void setResource(Resource inputResource) {
    this.fileName = inputResource.getFilename();
  }

}
