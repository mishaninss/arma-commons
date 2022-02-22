/*
 * Copyright (c) 2021 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.arma.config;

import com.github.mishaninss.arma.reporting.IReporter;
import com.github.mishaninss.arma.reporting.Reporter;
import com.github.mishaninss.arma.reporting.Slf4jReporter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan(value = "com.github.mishaninss.arma",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,
            value = {Configuration.class})
    })
public class CommonsConfig {

  private static final String ARMA_PROPERTIES_FILE = "arma.properties";
  private static final String ARMA_CONFIG_FILES_PROPERTY = "arma.config.files";

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setTrimValues(true);
    configurer.setIgnoreResourceNotFound(true);

    List<Resource> configFiles = new ArrayList<>();
    configFiles.add(new ClassPathResource(ARMA_PROPERTIES_FILE));
    List<String> additionalFiles = getConfigFiles();
    if (CollectionUtils.isNotEmpty(additionalFiles)) {
      additionalFiles.forEach(file -> {
        if (StringUtils.isNotBlank(file)) {
          configFiles.add(new ClassPathResource(file));
        }
      });
    }
    configurer.setLocations(configFiles.toArray(new Resource[0]));

    return configurer;
  }

  private static List<String> getConfigFiles() {
    String configFilesValue = System.getProperty(ARMA_CONFIG_FILES_PROPERTY);
    if (StringUtils.isBlank(configFilesValue)) {
      try (InputStream is = CommonsConfig.class.getResourceAsStream("/" + ARMA_PROPERTIES_FILE)) {
        Properties armaProperties = new Properties();
        armaProperties.load(is);
        configFilesValue = armaProperties.getProperty(ARMA_CONFIG_FILES_PROPERTY);
      } catch (Exception ex) {
        //IGNORE EXCEPTION
      }
    }
    return configFilesValue == null ? null : Arrays.asList(configFilesValue.split(","));
  }

  @Bean(IReporter.QUALIFIER)
  @Reporter
  public IReporter reporter() {
    return new Slf4jReporter();
  }
}
