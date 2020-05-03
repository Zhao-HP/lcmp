package com.zhp.lcmp.service;

import com.zhp.lcmp.LcmpApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LcmpApplication.class)
public class ServerInfoServiceTest {

    @Autowired
    private IServerInfoService serverInfoService;

}
