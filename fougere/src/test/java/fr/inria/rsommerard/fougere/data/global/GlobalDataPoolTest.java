package fr.inria.rsommerard.fougere.data.global;

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
 * Created by Romain on 14/08/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class GlobalDataPoolTest {

    private GlobalDataPool globalDataPool;

    @Before
    public void setup() {
        this.globalDataPool = new GlobalDataPool(RuntimeEnvironment.application);
    }

    @Test
    public void getAllData() {
        GlobalData data1 = GlobalDataProducer.produce();
        this.globalDataPool.insert(data1);

        GlobalData data2 = GlobalDataProducer.produce();
        this.globalDataPool.insert(data2);

        Assert.assertEquals(2, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertAValidData() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);

        Assert.assertEquals(1, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertAValidDataTwoTimes() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);
        this.globalDataPool.insert(data);

        Assert.assertEquals(1, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertTwoValidData() {
        GlobalData data1 = GlobalDataProducer.produce();
        this.globalDataPool.insert(data1);

        GlobalData data2 = GlobalDataProducer.produce();
        this.globalDataPool.insert(data2);

        Assert.assertEquals(2, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullId() {
        GlobalData data1 = GlobalDataProducer.produce();
        this.globalDataPool.insert(data1);

        GlobalData data2 = new GlobalData(null, data1.getIdentifier(), data1.getContent());
        this.globalDataPool.insert(data2);

        Assert.assertEquals(1, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullIdentifier() {
        GlobalData data = GlobalDataProducer.produce();
        GlobalData data1 = new GlobalData(null, null, data.getContent());
        this.globalDataPool.insert(data1);

        Assert.assertEquals(0, this.globalDataPool.getAll().size());
    }

    @Test
    public void insertADataWithNullContent() {
        GlobalData data = GlobalDataProducer.produce();
        GlobalData data1 = new GlobalData(null, data.getIdentifier(), null);
        this.globalDataPool.insert(data1);

        Assert.assertEquals(0, this.globalDataPool.getAll().size());
    }

    @Test
    public void deleteAValidData() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);

        this.globalDataPool.delete(data);
        Assert.assertEquals(0, this.globalDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullId() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);

        GlobalData data1 = new GlobalData(null, data.getIdentifier(), data.getContent());
        this.globalDataPool.delete(data1);

        Assert.assertEquals(0, this.globalDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullIdentifier() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);

        GlobalData data1 = new GlobalData(null, null, data.getContent());
        this.globalDataPool.delete(data1);

        Assert.assertEquals(1, this.globalDataPool.getAll().size());
    }

    @Test
    public void deleteADataWithNullContent() {
        GlobalData data = GlobalDataProducer.produce();
        this.globalDataPool.insert(data);

        GlobalData data1 = new GlobalData(null, data.getIdentifier(), null);
        this.globalDataPool.delete(data1);

        Assert.assertEquals(0, this.globalDataPool.getAll().size());
    }
}
