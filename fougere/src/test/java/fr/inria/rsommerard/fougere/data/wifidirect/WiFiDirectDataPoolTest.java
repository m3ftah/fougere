package fr.inria.rsommerard.fougere.data.wifidirect;

import android.os.Build;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import fr.inria.rsommerard.fougere.BuildConfig;

/**
 * Created by Romain on 15/08/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WiFiDirectDataPoolTest {

    private WiFiDirectDataPool wiFiDirectDataPool;

    @Before
    public void setup() {
        this.wiFiDirectDataPool = new WiFiDirectDataPool(RuntimeEnvironment.application);
    }

    @Test
    public void getAllData() {
        WiFiDirectData data1 = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data1);

        WiFiDirectData data2 = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data2);

        Assert.assertEquals(2, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertAValidData() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);

        Assert.assertEquals(1, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertAValidDataTwoTimes() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);
        this.wiFiDirectDataPool.insert(data);

        Assert.assertEquals(1, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertTwoValidData() {
        WiFiDirectData data1 = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data1);

        WiFiDirectData data2 = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data2);

        Assert.assertEquals(2, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullId() {
        WiFiDirectData data1 = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data1);

        WiFiDirectData data2 = new WiFiDirectData(null, data1.getIdentifier(), data1.getContent());
        this.wiFiDirectDataPool.insert(data2);

        Assert.assertEquals(1, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullIdentifier() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        WiFiDirectData data1 = new WiFiDirectData(null, null, data.getContent());
        this.wiFiDirectDataPool.insert(data1);

        Assert.assertEquals(0, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullContent() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        WiFiDirectData data1 = new WiFiDirectData(null, data.getIdentifier(), null);
        this.wiFiDirectDataPool.insert(data1);

        Assert.assertEquals(0, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void deleteAValidData() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);

        this.wiFiDirectDataPool.delete(data);
        Assert.assertEquals(0, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullId() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);

        WiFiDirectData data1 = new WiFiDirectData(null, data.getIdentifier(), data.getContent());
        this.wiFiDirectDataPool.delete(data1);

        Assert.assertEquals(0, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullIdentifier() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);

        WiFiDirectData data1 = new WiFiDirectData(null, null, data.getContent());
        this.wiFiDirectDataPool.delete(data1);

        Assert.assertEquals(1, this.wiFiDirectDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullContent() {
        WiFiDirectData data = WiFiDirectDataProducer.produce();
        this.wiFiDirectDataPool.insert(data);

        WiFiDirectData data1 = new WiFiDirectData(null, data.getIdentifier(), null);
        this.wiFiDirectDataPool.delete(data1);

        Assert.assertEquals(0, this.wiFiDirectDataPool.getAll().size());
    }
}