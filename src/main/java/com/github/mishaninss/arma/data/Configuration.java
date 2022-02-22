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

package com.github.mishaninss.arma.data;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Configuration implements InitializingBean, DisposableBean
{
    @Autowired
    private Environment environment;
    @Autowired
    private ApplicationContext applicationContext;

    private static final ThreadLocal<Configuration> INSTANCES = new ThreadLocal<>();


    @Override
    public void afterPropertiesSet()
    {
        INSTANCES.set(this);
    }

    @Override
    public void destroy()
    {
        INSTANCES.remove();
    }

    public String getProperty(String property)
    {
        return environment.getProperty(property);
    }

    public void refreshContext()
    {
        ((ConfigurableApplicationContext) applicationContext).refresh();
    }
}
