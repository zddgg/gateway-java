/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gateway.server.result;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.io.Serializable;

@Data
public class DefaultGatewayEntity implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private static final int ERROR = 500;

    private Integer code;

    private String message;

    @JsonBackReference
    private Object data;

    /**
     * Instantiates a new shenyu result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    public DefaultGatewayEntity(final Integer code, final String message, final Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static DefaultGatewayEntity error(final String msg) {
        return error(ERROR, msg);
    }

    public static DefaultGatewayEntity error(final int code, final String msg) {
        return get(code, msg, null);
    }

    public static DefaultGatewayEntity error(final int code, final String msg, final Object data) {
        return get(code, msg, data);
    }

    public static DefaultGatewayEntity timeout(final String msg) {
        return error(ERROR, msg);
    }

    private static DefaultGatewayEntity get(final int code, final String msg, final Object data) {
        return new DefaultGatewayEntity(code, msg, data);
    }
}
