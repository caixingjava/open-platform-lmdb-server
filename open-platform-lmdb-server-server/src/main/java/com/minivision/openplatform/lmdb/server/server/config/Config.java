package com.minivision.openplatform.lmdb.server.server.config;

import org.springframework.context.annotation.Import;

@Import({com.minivision.openplatform.lmdb.server.common.config.Config.class,
        com.minivision.openplatform.lmdb.server.api.config.Config.class,
        com.minivision.openplatform.lmdb.server.core.config.Config.class})
public class Config {
}
