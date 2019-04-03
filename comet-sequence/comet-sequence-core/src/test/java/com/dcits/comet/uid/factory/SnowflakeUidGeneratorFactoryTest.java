package com.dcits.comet.uid.factory;

import com.dcits.comet.uid.UidGenerator;
import com.dcits.comet.uid.UidGeneratorProxy;
import com.dcits.comet.uid.impl.DefaultUidGenerator;
import com.dcits.comet.uid.worker.DisposableWorkerIdAssigner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class SnowflakeUidGeneratorFactoryTest {
    @Mock
    SnowflakeUidGeneratorFactory inst;
    @Mock
    UidGeneratorProxy uidGeneratorProxy;
    @Mock
    Logger log;
    @Mock
    DisposableWorkerIdAssigner workerIdAssigner;
    @InjectMocks
    SnowflakeUidGeneratorFactory snowflakeUidGeneratorFactory;

    @BeforeMethod
    public void setUp() {
        //create mock
        List mockedList = mock(List.class);
        //use mock object
        mockedList.add("one");
        mockedList.clear();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetKey() {
        when(uidGeneratorProxy.getProxy()).thenReturn(new DefaultUidGenerator());

        long result = snowflakeUidGeneratorFactory.getKey("name");
        Assert.assertEquals(result, 0L);
    }

    @Test
    public void testGetKey2() {
        when(inst.getKey(anyString())).thenReturn(0L);
        when(uidGeneratorProxy.getProxy()).thenReturn(new DefaultUidGenerator());

        long result = snowflakeUidGeneratorFactory.getKey();
        Assert.assertEquals(result, 0L);
    }

    @Test
    public void testGetKeyList() {
        when(uidGeneratorProxy.getProxy()).thenReturn(new DefaultUidGenerator());

        List<Long> result = snowflakeUidGeneratorFactory.getKeyList("bizTag", 0L);
        Assert.assertEquals(result, Arrays.<Long>asList(Long.valueOf(1)));
    }

    @Test
    public void testGetKeyList2() {
        when(inst.getKeyList(anyString(), anyLong())).thenReturn(Arrays.<Long>asList(Long.valueOf(1)));
        when(uidGeneratorProxy.getProxy()).thenReturn(new DefaultUidGenerator());

        List<Long> result = snowflakeUidGeneratorFactory.getKeyList(0L);
        Assert.assertEquals(result, Arrays.<Long>asList(Long.valueOf(1)));
    }

    @Test
    public void testGetUidGenerator2() {
        when(inst.createUidGenerator(anyString())).thenReturn(new DefaultUidGenerator());

        UidGenerator result = SnowflakeUidGeneratorFactory.getUidGenerator("name");
        Assert.assertEquals(result, new DefaultUidGenerator());
    }

    @Test
    public void testCreateUidGenerator() {
        UidGenerator result = SnowflakeUidGeneratorFactory.createUidGenerator("name");
        Assert.assertEquals(result, new DefaultUidGenerator());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
