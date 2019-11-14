package com.monese.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class AppConfiguration extends Configuration {
  @NotNull
  @Valid
  private DataSourceFactory dataSourceFactory
      = new DataSourceFactory();

  @JsonProperty("database")
  DataSourceFactory getDataSourceFactory() {
    return dataSourceFactory;
  }
}
