package com.dyzwj.rpc.spring.client;

import com.dyzwj.common.core.Request;
import com.dyzwj.common.core.Response;

public interface Client {

    Response send(Request request);

}
