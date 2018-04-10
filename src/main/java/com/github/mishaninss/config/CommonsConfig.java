/*
 *
 * Copyright 2018 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.config;

import com.github.mishaninss.reporting.IReporter;
import com.github.mishaninss.reporting.Reporter;
import com.github.mishaninss.reporting.Slf4jReporter;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = {"classpath:arma.properties","classpath:${arma.config.file}"}, ignoreResourceNotFound = true)
@ComponentScan(value = "com.github.mishaninss",
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,
                value = {Configuration.class})
})
public class CommonsConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setTrimValues(true);
        return configurer;
    }

    @Bean(IReporter.QUALIFIER) @Reporter
    public IReporter reporter(){
        return new Slf4jReporter();
    }
}
